package com.xsrw.wms.web.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.LocationConstants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsDetail;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.inout.service.ITTaskWcsDetailService;
import com.xsrw.wms.inout.strategy.RecommendedLocationUtil;
import com.xsrw.wms.web.domain.WcsOrderEntity;
import com.xsrw.wms.web.domain.WcsResultEntity;
import com.xsrw.wms.web.domain.WcsSendEntity;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Description: 双伸位处理
 * @Author XMING
 * @Date 2023-11-09
 */
@Component
public class WcsMoveUtil {


    @Autowired
    private TLocationMapper tLocationMapper;

    @Autowired
    private RecommendedLocationUtil recommendedLocationUtil;

    @Autowired
    private WcsReportUtil wcsReportUtil;

    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;

    @Autowired
    private TTrayMapper tTrayMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;

    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;

    @Autowired
    private AgvReportUtil agvReportUtil;

    /**
     * 入库和出库的时候（和WCS下发任务时）判断库位是2伸位，如果是则判断1伸位是否有托盘，如果有则先生成移库任务（推荐的库位按照第2条）
     * 移库任务完成后回调方法需要更新库存信息，移库禁止从1层移动到2层
     */

    /**
     *  推荐库位，由近到远，由低到高的原则，优先推荐2伸位，只有当整个三层的2伸位满的时候才推荐1伸位
     */

    /**
     * 1、出入库时，发送命令之前先判断1伸位2伸位，如果是2伸位，判断1伸位是否有托盘，有的话，发送移库命令（wcs新增了此类参数），至目的库位（调用移库库位推荐），再发送出入库命令；
     * 2、移库回调后，更新库位、载具、库存信息（使用暂存位时更新库存到暂存位）；
     * 3、出入库回调成功后，查询同楼层的暂存位是否有托盘，有（调用移库库位推荐），有推荐库位的时候，生成移库将暂存位托盘移走至推荐库位，无推荐库位的时候不处理；
     */

    public WcsOrderDTO dealDoubleExtension(List<Long> removeLocations,String taskNoMain, String locationCode) {
        WcsOrderDTO taskNoMove = null;
        TLocation tLocation = tLocationMapper.selectOne(new LambdaQueryWrapper<TLocation>()
                .eq(TLocation::getDelFlag, Constants.DEL_FLAG_NO)
                .eq(TLocation::getCode, locationCode));
        //1、入库时，发送命令之前先判断1伸位2伸位
        if (tLocation != null
                && Constants.LOCATION_FLOOR_FIRST.equals(tLocation.getFloorType())
                && 2 == tLocation.getExtentionType()) {
            //如果是2伸位，判断1伸位是否有托盘
            TLocation locationFrist = tLocationMapper.selectByFirstNodeId(tLocation);
            if(locationFrist != null){
                //如果当前同一批操作的库位，不生成移库
                if(!CollectionUtils.isEmpty(removeLocations) && removeLocations.contains(locationFrist.getId())){
                    return null;
                }
                if(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3.equals(locationFrist.getGoodsAllocationStatus())
                        || Constants.LOCATION_GOODS_ALLOCATION_STATUS_4.equals(locationFrist.getGoodsAllocationStatus())){
                    throw new ServiceException("1伸位有任务，此库位("+locationFrist.getName()+")存在正在执行中任务，托盘号："+locationFrist.getPalletNum()+",请先完成");
                }
                if (StringUtils.isNotEmpty(locationFrist.getPalletNum())) {
                    //3、有的话，发送移库命令（wcs新增了此类参数），至目的库位（调用移库库位推荐），再发送出入库命令
                    // 获取推荐的库位
                    TLocation moveLocation = recommendedLocationUtil.recommendedLocationForMove(removeLocations, tLocation.getLocationPlies());
                    if (moveLocation == null) {
                        throw new ServiceException("已无可推荐移库库位，不可执行");
                    }
                    // 移库任务
                    taskNoMove = dealMoveTask(taskNoMain, moveLocation, locationFrist);
                    taskNoMove.setTaskType(Constants.WCS_TASK_TYPE_MOVE);
                }
            }
        }
        return taskNoMove;
    }

    /**
     * @param locationId    出入库 库位id
     * @param originId      原始单据id
     * @param wcsReportType WCS任务类型 1出库、2入库、3移库
     */
//    @Transactional
//    public void moveTray(Long locationId, Long originId, String wcsReportType) {
//
//        TLocation tLocation = tLocationMapper.selectById(locationId);
//        // 一楼托盘
//        if (Constants.LOCATION_FLOOR_FIRST.equals(tLocation.getFloorType())) {
//            // 双伸位类型（1伸位2伸位）
//            if ("2".equals(tLocation.getExtentionType())) {
//                // 查询1伸位信息 1伸位存在托盘  执行移库 给1伸位托盘重新推荐一个库位
//                TLocation locationFrist = tLocationMapper.selectById(tLocation.getExtentionFristId());
//                if (StringUtils.isNotEmpty(locationFrist.getPalletNum())) {
//                    // 获取推荐的库位
//                    TLocation moveLocation = recommendedLocationUtil.recommendedLocationForMove(locationFrist.getLocationPlies());
//
//                    // 移库任务
//                    moveTask(moveLocation, locationFrist, originId);
//
//                    // 出库、入库任务
//                    inOutTask(tLocation, wcsReportType, originId);
//                }
//            } else {
//                inOutTask(tLocation, wcsReportType, originId);
//            }
//        } else {
//            // 二楼料箱
//            // 入库不需要调用AGV 出库时调用
//            // 查询托盘信息
//            TTray tTray = tTrayMapper.selectOne(new QueryWrapper<TTray>().eq("code", tLocation.getPalletNum()));
//
//            TTaskWcs taskWcs = new TTaskWcs();
//            taskWcs.setTaskType(Constants.TASK_TYPE_PICK);
//            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
//            taskWcs.setTrayId(tTray.getId());
//            taskWcs.setTrayCode(tTray.getCode());
//            taskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
//            taskWcs.setLocationId(tLocation.getId());
//            tTaskWcsMapper.insert(taskWcs);
//
//            WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), tLocation.getCode(),
//                    Constants.SHELF_POINT_SECOND_LINE_OUT, tTray.getCode());
//            String taskStatus = agvReportUtil.sendAgvPickingReport(orderDTO);
//
//            // 更新WCS任务状态
//            taskWcs.setTaskStatus(taskStatus);
//            tTaskWcsMapper.updateById(taskWcs);
//
//            // 记录wcs任务详情
//            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
//            tTaskWcsDetail.setTaskId(taskWcs.getId());
//            tTaskWcsDetail.setOriginId(originId);
//            tTaskWcsDetail.setType(Constants.TASK_TYPE_PICK);
//            taskWcsDetailService.save(tTaskWcsDetail);
//        }
//
//    }


    /**
     * 移库
     *
     * @param taskNoMain     关联的出入库任务
     * @param moveLocation   目的库位
     * @param locationOrigin 起始库位
     */
    private WcsOrderDTO dealMoveTask(String taskNoMain, TLocation moveLocation, TLocation locationOrigin) {
        TTray tTrayOrigin = tTrayMapper.selectOne(new QueryWrapper<TTray>().eq("code", locationOrigin.getPalletNum()));

        // 创建移库任务
        String taskNo = codeConfigService.getCode(CodeEnum.CRW.getCodeName());
        //开始位置
        String startStation = locationOrigin.getLocationPlies() + "-" + locationOrigin.getPalletNodeId();
        //目的位置
        String endStation = moveLocation.getLocationPlies() + "-" + moveLocation.getPalletNodeId();

        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setTaskType(Constants.TASK_TYPE_MOVE);
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        taskWcs.setTrayId(tTrayOrigin.getId());
        taskWcs.setTrayCode(tTrayOrigin.getCode());
        taskWcs.setTaskNo(taskNo);
        taskWcs.setMainTaskNo(taskNo);
        taskWcs.setLocationId(moveLocation.getId());
        taskWcs.setStartPosition(locationOrigin.getCode());
        taskWcs.setPurposePosition(moveLocation.getCode());
        taskWcs.setMainTaskNo(taskNoMain);
        taskWcs.setPriority("1");
        tTaskWcsMapper.insert(taskWcs);

        WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, moveLocation.getCode(), taskWcs.getTrayCode());
        orderDTO.setMainTaskNo(taskWcs.getMainTaskNo());
        orderDTO.setMoveTaskNo(taskWcs.getTaskNo());
        orderDTO.setLocationId(moveLocation.getId());

//        WcsSendEntity sendEntity = new WcsSendEntity();
//        sendEntity.setReqID(taskNo);
//        sendEntity.setReqTime(DateUtils.getTime());
//        sendEntity.setOrderNo(taskNo);
//        WcsOrderEntity orderEntity = new WcsOrderEntity();
//        orderEntity.setTaskNo(taskNo);
//        orderEntity.setStartStation(startStation);
//        orderEntity.setEndStation(endStation);
//        orderEntity.setTrayNo(tTrayOrigin.getCode());
//        sendEntity.setProductDetails(Collections.singletonList(orderEntity));
//
//        // 记录硬件调用
//        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
//        taskWcsRecord.setStatus(Constants.YES);
//        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
//        try {
//            WcsResultEntity wcsResult = wcsReportUtil.wcsSend(WcsReportUtil.wcsMove, sendEntity);
//            if (wcsResult != null && StringUtils.isEmpty(wcsResult.getReqID())) {
//                taskWcsRecord.setAcceptData(wcsResult.toString());
//                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
//            } else {
//                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            taskWcsRecord.setAcceptData(e.getMessage());
//            taskWcsRecord.setStatus(Constants.NO);
//            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
//        }
//        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
//        taskWcsRecord.setPurposePosition(endStation);
//        taskWcsRecord.setStartPosition(startStation);
//        taskWcsRecord.setTaskWcsId(taskWcs.getId());
//        taskWcsRecord.setMainTaskNo(taskNoMain);
//        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
//        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
//        tTaskWcsRecordMapper.insert(taskWcsRecord);

        // 更新移库任务状态
//        taskWcs.setTaskStatus(msgStatus);
//        tTaskWcsMapper.updateById(taskWcs);

        // 处理库位状态
        TLocation locationOut = new TLocation();
        locationOut.setId(locationOrigin.getId());
        locationOut.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
        tLocationMapper.updateById(locationOut);
        TLocation locationIn = new TLocation();
        locationIn.setId(moveLocation.getId());
        locationIn.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
        tLocationMapper.updateById(locationIn);
        return orderDTO;
    }

    private void moveTask(TLocation moveLocation, TLocation locationFrist, Long originId) {

        TTray tTray = tTrayMapper.selectOne(new QueryWrapper<TTray>().eq("code", locationFrist.getPalletNum()));

        // 创建移库任务
        String taskNo = codeConfigService.getCode(CodeEnum.CRW.getCodeName());
        // 后置任务号
        String nextTaskNo = codeConfigService.getCode(CodeEnum.CRW.getCodeName());

        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setTaskType(Constants.TASK_TYPE_MOVE);
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        taskWcs.setTrayId(tTray.getId());
        taskWcs.setTrayCode(tTray.getCode());
        taskWcs.setTaskNo(taskNo);
        taskWcs.setLocationId(locationFrist.getId());
        taskWcs.setStartPosition(locationFrist.getCode());
        taskWcs.setPurposePosition(moveLocation.getCode());
        tTaskWcsMapper.insert(taskWcs);

        WcsSendEntity sendEntity = new WcsSendEntity();
        sendEntity.setReqID(taskNo);
        sendEntity.setReqTime(DateUtils.getTime());
        sendEntity.setOrderNo(taskNo);
        WcsOrderEntity orderEntity = new WcsOrderEntity();
        orderEntity.setTaskNo(taskNo);
        orderEntity.setNextTaskNo(nextTaskNo);
        orderEntity.setStartStation(locationFrist.getLocationPlies() + "-" + locationFrist.getPalletNodeId());
        orderEntity.setEndStation(moveLocation.getLocationPlies() + "-" + moveLocation.getPalletNodeId());
        orderEntity.setTrayNo(tTray.getCode());
        sendEntity.setProductDetails(Collections.singletonList(orderEntity));

        // 记录硬件调用
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.YES);
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
        try {
            WcsResultEntity wcsResult = wcsReportUtil.wcsSend(WcsReportUtil.wcsMove, sendEntity);
            if (wcsResult != null && StringUtils.isEmpty(wcsResult.getReqID())) {
                taskWcsRecord.setAcceptData(wcsResult.toString());
                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
            } else {
                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskWcsRecord.setAcceptData(e.getMessage());
            taskWcsRecord.setStatus(Constants.NO);
            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
        }
        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
        taskWcsRecord.setPurposePosition(moveLocation.getCode());
        taskWcsRecord.setStartPosition(locationFrist.getCode());
        taskWcsRecord.setTaskWcsId(taskWcs.getId());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
        tTaskWcsRecordMapper.insert(taskWcsRecord);

        // 更新移库任务状态
        taskWcs.setTaskStatus(msgStatus);
        tTaskWcsMapper.updateById(taskWcs);

        // 记录wcs任务详情
        TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
        tTaskWcsDetail.setTaskId(taskWcs.getId());
        tTaskWcsDetail.setOriginId(originId);
        tTaskWcsDetail.setType(Constants.TASK_TYPE_MOVE);
        taskWcsDetailService.save(tTaskWcsDetail);

        // 处理库位状态
        locationFrist.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
        moveLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
        tLocationMapper.updateById(locationFrist);
        tLocationMapper.updateById(moveLocation);
    }


    private void inOutTask(TLocation tLocation, String wcsReportType, Long originId) {

        // 查询托盘信息
        TTray tTray = tTrayMapper.selectOne(new QueryWrapper<TTray>().eq("code", tLocation.getPalletNum()));

        String taskNo = codeConfigService.getCode(CodeEnum.CRW.getCodeName());

        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setTaskType(Constants.TASK_TYPE_MOVE);
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        taskWcs.setTrayId(tTray.getId());
        taskWcs.setTrayCode(tTray.getCode());
        taskWcs.setTaskNo(taskNo);
        taskWcs.setLocationId(tLocation.getId());
        tTaskWcsMapper.insert(taskWcs);

        WcsSendEntity sendEntity = new WcsSendEntity();
        sendEntity.setReqID(taskNo);
        sendEntity.setReqTime(DateUtils.getTime());
        sendEntity.setOrderNo(taskNo);
        WcsOrderEntity orderEntity = new WcsOrderEntity();
        orderEntity.setTaskNo(taskNo);
        if (WcsReportUtil.wcsOut.equals(wcsReportType)) {
            orderEntity.setStartStation(tLocation.getLocationPlies() + "-" + tLocation.getPalletNodeId());
            orderEntity.setEndStation(WcsReportUtil.stationOut);
        }
        if (WcsReportUtil.wcsIn.equals(wcsReportType)) {
            orderEntity.setStartStation(WcsReportUtil.wcsIn);
            orderEntity.setEndStation(tLocation.getLocationPlies() + "-" + tLocation.getPalletNodeId());
        }
        orderEntity.setTrayNo(tTray.getCode());
        sendEntity.setProductDetails(Collections.singletonList(orderEntity));

        // 记录硬件调用
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.YES);
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
        try {
            WcsResultEntity wcsResult = wcsReportUtil.wcsSend(wcsReportType, sendEntity);
            if (wcsResult != null && StringUtils.isEmpty(wcsResult.getReqID())) {
                taskWcsRecord.setAcceptData(wcsResult.toString());
                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
            } else {
                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskWcsRecord.setAcceptData(e.getMessage());
            taskWcsRecord.setStatus(Constants.NO);
            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
        }
        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
        if (WcsReportUtil.wcsOut.equals(wcsReportType)) {
            taskWcsRecord.setStartPosition(tLocation.getCode());
            taskWcsRecord.setPurposePosition(WcsReportUtil.stationOut);
        }
        if (WcsReportUtil.wcsIn.equals(wcsReportType)) {
            orderEntity.setStartStation(WcsReportUtil.wcsIn);
            orderEntity.setEndStation(tLocation.getCode());
        }
        taskWcsRecord.setTaskWcsId(taskWcs.getId());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
        tTaskWcsRecordMapper.insert(taskWcsRecord);

        // 更新任务状态
        taskWcs.setTaskStatus(msgStatus);
        tTaskWcsMapper.updateById(taskWcs);

        // 记录wcs任务详情
        TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
        tTaskWcsDetail.setTaskId(taskWcs.getId());
        tTaskWcsDetail.setOriginId(originId);
        tTaskWcsDetail.setType(Constants.TASK_TYPE_MOVE);
        taskWcsDetailService.save(tTaskWcsDetail);

        // 处理库位状态
        if (WcsReportUtil.wcsOut.equals(wcsReportType)) {
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
        }
        if (WcsReportUtil.wcsIn.equals(wcsReportType)) {
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
        }
        tLocationMapper.updateById(tLocation);
    }

    /**
     * 出入库执行完
     */
    @Transactional
    public void wcsBackForMove(Long trayId) {
        //3、出入库回调成功后，查询同楼层的暂存位是否有托盘，有（调用移库库位推荐），有推荐库位的时候，生成移库将暂存位托盘移走至推荐库位，无推荐库位的时候不处理；
        TLocation tLocation = tLocationMapper.selectTempLocationInfoById(trayId, LocationConstants.WORKING_STORAGE_BIT);
        if (tLocation != null
                && (Constants.LOCATION_GOODS_ALLOCATION_STATUS_1.equals(tLocation.getGoodsAllocationStatus())
                || Constants.LOCATION_GOODS_ALLOCATION_STATUS_2.equals(tLocation.getGoodsAllocationStatus()))
                && StringUtils.isNotEmpty(tLocation.getPalletNum())) {
            TLocation moveLocation = recommendedLocationUtil.recommendedLocationForMove(null, tLocation.getLocationPlies());
            if (moveLocation != null) {
                //查询此载具最后一个移库
                String taskNoMain = "";
//                TTaskWcs taskWcs = tTaskWcsMapper.selectLastEndMove(tLocation.getPalletNum(),LocationConstants.WORKING_STORAGE_BIT);
//                if(taskWcs != null){
//                    taskNoMain = taskWcs.getMainTaskNo();
//                }
                WcsOrderDTO taskNoMove = this.dealMoveTask(taskNoMain, moveLocation, tLocation);
                if(taskNoMove != null){
                    taskNoMove.setMainTaskNo(null);
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                }
            }
        }
    }

}
