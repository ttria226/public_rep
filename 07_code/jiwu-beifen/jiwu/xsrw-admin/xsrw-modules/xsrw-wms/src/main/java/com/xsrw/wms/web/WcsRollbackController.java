package com.xsrw.wms.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITGoodShelfService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.inout.domain.TTaskOut;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.inout.service.ITTaskWcsRecordService;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.web.domain.WcsResultEntity;
import com.xsrw.wms.web.domain.AgvEntity;
import com.xsrw.wms.web.domain.WcsSendEntity;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsMoveUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: WCS回调
 * @Author XMING
 * @Date 2023-09-05
 */
@RestController
@RequestMapping("/wcs")
public class WcsRollbackController {

    @Autowired
    private ITTaskWcsService taskWcsService;
    @Autowired
    private ITGoodShelfService goodShelfService;
    @Autowired
    private ITTaskWcsRecordService taskWcsRecordService;
    @Autowired
    private AgvReportUtil agvReportUtil;
    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;
    @Autowired
    private TTaskOutMapper tTaskOutMapper;
    @Autowired
    private ITTrayService trayService;
    @Autowired
    private TTrayMapper tTrayMapper;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;

    /**
     * wcs入库任务状态上报
     *
     * @return
     */
    @PostMapping("/inTask")
    @Log(title = "wcs入库任务状态上报", businessType = BusinessType.INSERT)
    public Map<String, Object> inTask(@RequestBody Map<String, Object> param) {

        WcsSendEntity sendEntity = JSONObject.parseObject(JSONObject.toJSONString(param), WcsSendEntity.class);

        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(sendEntity.getReqID())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "reqID为空");
            return map;
        }
        if (CollectionUtils.isEmpty(sendEntity.getProductDetails())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "物料明细为空");
        }
        if (sendEntity.getProductDetails().get(0).getTaskStatus() == null) {
            map.put("resultCode", 1);
            map.put("resultMsg", "执行结果为空");
        }

        TTaskWcsVO taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
        if (1 == sendEntity.getProductDetails().get(0).getTaskStatus()) {
            //处理业务 执行
            if (taskWcsVO != null) {
                if (Constants.WCS_TASK_TYPE_IN.equals(taskWcsVO.getTaskType())) {
                    //入库
                    TTaskWcsDTO taskWcsDTO = new TTaskWcsDTO();
                    taskWcsDTO.setId(taskWcsVO.getId());
                    taskWcsService.executeTaskNew(taskWcsDTO);
                } else if (Constants.TASK_TYPE_OUT.equals(taskWcsVO.getTaskType()) || Constants.TASK_TYPE_BACK.equals(taskWcsVO.getTaskType())) {
                    //载具出库、回库
                    trayService.completeTrayBack(taskWcsVO);
                }
                //处理暂存位信息
                wcsMoveUtil.wcsBackForMove(taskWcsVO.getLocationId());
            } else {
                map.put("resultMsg", "未获取到任务数据");
            }
        }else {
            if (taskWcsVO != null) {
                taskWcsService.update(
                        new UpdateWrapper<TTaskWcs>()
                                .eq("task_no", sendEntity.getReqID())
                                .set("task_status", Constants.WCS_EXECUTE_STATUS_FAIL));
            }
        }
        // 记录wcs任务上报信息
        TTaskWcsRecord wcsRecord = new TTaskWcsRecord();
        wcsRecord.setTaskWcsId(taskWcsVO != null ? taskWcsVO.getId() : null);
        wcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        wcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        wcsRecord.setAcceptData(JSONObject.toJSONString(param));
        wcsRecord.setStatus(Constants.YES);
        tTaskWcsRecordMapper.insert(wcsRecord);

        map.put("resultCode", 0);
        map.put("resultMsg", "请求成功");
        return map;
    }


    /**
     * wcs出库任务状态上报
     *
     * @return
     */
    @Log(title = "wcs出库任务状态上报", businessType = BusinessType.INSERT)
    @PostMapping("/outTask")
    @Transactional
    public Map<String, Object> outTask(@RequestBody Map<String, Object> param) {

        Map<String, Object> map = new HashMap<>();
        map.put("reqID", param.get("reqID"));
        map.put("resultTime", param.get("reqTime"));

        WcsSendEntity sendEntity = JSONObject.parseObject(JSONObject.toJSONString(param), WcsSendEntity.class);
        if (StringUtils.isEmpty(sendEntity.getReqID())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "reqID为空");
            return map;
        }
        if (CollectionUtils.isEmpty(sendEntity.getProductDetails())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "物料明细为空");
        }
        if (sendEntity.getProductDetails().get(0).getTaskStatus() == null) {
            map.put("resultCode", 1);
            map.put("resultMsg", "执行结果为空");
        }

        // 更新wcs任务状态

        //wcs上报任务状态说明
        //1=任务完成；
        //2=任务取消(需AGV搬运回组盘位)；
        //3=强制作废（直接取消该入库单据）
        TTaskWcsVO taskWcsVO = null;


        if (1 == sendEntity.getProductDetails().get(0).getTaskStatus()) {
            taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
            if (taskWcsVO != null) {
                if (Constants.TASK_TYPE_OUT.equals(taskWcsVO.getTaskType()) || Constants.TASK_TYPE_BACK.equals(taskWcsVO.getTaskType())) {
                    //载具出库、回库
                    trayService.completeTrayBack(taskWcsVO);
                }
                //处理暂存位信息
                wcsMoveUtil.wcsBackForMove(taskWcsVO.getLocationId());
            }
        } else {
            taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
            if (taskWcsVO != null) {
                //出库
                taskWcsService.update(
                        new UpdateWrapper<TTaskWcs>()
                                .eq("task_no", sendEntity.getReqID())
                                .set("task_status", Constants.WCS_EXECUTE_STATUS_FAIL));
            }
        }

        // 记录wcs任务上报信息
        TTaskWcsRecord wcsRecord = new TTaskWcsRecord();
        wcsRecord.setTaskWcsId(taskWcsVO == null ? null : taskWcsVO.getId());
        wcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        wcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        wcsRecord.setAcceptData(JSONObject.toJSONString(sendEntity));
        wcsRecord.setStatus(Constants.YES);
        tTaskWcsRecordMapper.insert(wcsRecord);

        map.put("resultCode", 0);
        map.put("resultMsg", "请求成功");
        return map;
    }


    /**
     * wcs移库任务状态上报
     * @param param
     * @return
     */
    @Log(title = "wcs移库任务状态上报", businessType = BusinessType.INSERT)
    @PostMapping("/moveTask")
    @Transactional
    public Map<String, Object> moveTask(@RequestBody Map<String, Object> param){

        Map<String, Object> map = new HashMap<>();
        map.put("reqID", param.get("reqID"));
        map.put("resultTime", param.get("reqTime"));

        WcsSendEntity sendEntity = JSONObject.parseObject(JSONObject.toJSONString(param), WcsSendEntity.class);
        if (StringUtils.isEmpty(sendEntity.getReqID())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "reqID为空");
            return map;
        }
        if (CollectionUtils.isEmpty(sendEntity.getProductDetails())) {
            map.put("resultCode", 1);
            map.put("resultMsg", "物料明细为空");
            return map;
        }
        if (sendEntity.getProductDetails().get(0).getTaskStatus() == null) {
            map.put("resultCode", 1);
            map.put("resultMsg", "执行结果为空");
            return map;
        }

        //wcs上报任务状态说明
        //1=任务完成；
        //2=任务取消(需AGV搬运回组盘位)；
        //3=强制作废（直接取消该入库单据）
        TTaskWcsVO taskWcsVO = null;
        if (1 == sendEntity.getProductDetails().get(0).getTaskStatus()) {
            // 查询移库任务信息 更新库存信息 TODO
            taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
            if(taskWcsVO != null && Constants.TASK_TYPE_MOVE.equals(taskWcsVO.getTaskType())){
                taskWcsService.executeTaskMove(taskWcsVO);
            }
        } else {
            taskWcsService.update(
                    new UpdateWrapper<TTaskWcs>()
                            .eq("task_no", sendEntity.getReqID())
                            .set("task_status", Constants.WCS_EXECUTE_STATUS_FAIL));
        }
        // 记录wcs任务上报信息
        TTaskWcsRecord wcsRecord = new TTaskWcsRecord();
        wcsRecord.setTaskWcsId(taskWcsVO == null ? null : taskWcsVO.getId());
        wcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        wcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        wcsRecord.setAcceptData(JSONObject.toJSONString(sendEntity));
        wcsRecord.setStatus(Constants.YES);
        tTaskWcsRecordMapper.insert(wcsRecord);

        map.put("resultCode", 0);
        map.put("resultMsg", "请求成功");
        return map;
    }



    /**
     * wcs二楼输送线上报接口
     * trayCode 料箱编号
     *
     * @return
     */
    @Log(title = "wcs二楼输送线上报接口", businessType = BusinessType.INSERT)
    @PostMapping("/transLineIn")
    @Transactional
    public Map<String, Object> transLineBackIn1(@RequestBody Map<String, Object> param) {
        //todo 接收到料箱，查询到对应的wcs任务，获取到对应库位，判断是移库还是入库
        //执行记录表
        TTaskWcsRecord record = new TTaskWcsRecord();
        record.setAcceptData(JSONObject.toJSONString(param));
        record.setWcsType(Constants.TASK_HARDWARE_WCS);//二楼agv
        record.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        taskWcsRecordService.insertTTaskWcsRecord(record);

        Map<String, Object> map = new HashMap<>();
        if (ObjectUtils.isEmpty(param.get("trayCode"))) {
            map.put("resultCode", -1);
            map.put("resultMsg", "请求失败，未获取到载具信息");
            return map;
        }
        String trayCode = param.get("trayCode").toString();
        PageHelper.clearPage();
        TTaskWcsVO taskWcsVO = taskWcsService.getTaskInfoByTrayCode(trayCode, null);
        if (taskWcsVO == null || !Constants.LOCATION_FLOOR_SECOND.equals(taskWcsVO.getFloorType())) {
            map.put("resultCode", -1);
            map.put("resultMsg", "请求失败，未获取到任务信息或库位信息错误");
            return map;
        }
        String startPoint = "";
        String endPoint = "";
        if (Constants.WCS_TASK_TYPE_IN.equals(taskWcsVO.getTaskType())) {
            //入库
            endPoint = taskWcsVO.getLocationCode();
            startPoint = Constants.SHELF_POINT_SECOND_LINE_IN;
        } else if (Constants.WCS_TASK_TYPE_OUT.equals(taskWcsVO.getTaskType())) {
            //出库任务
            startPoint = taskWcsVO.getLocationCode();
            endPoint = Constants.SHELF_POINT_SECOND_LINE_OUT;
        }else if (Constants.TASK_TYPE_BACK.equals(taskWcsVO.getTaskType())){
            //回库
            endPoint = taskWcsVO.getLocationCode();
            startPoint = Constants.SHELF_POINT_SECOND_LINE_IN;
        }
        Map<String, Object> picking = agvReportUtil.sendAgvReport(taskWcsVO.getTaskNo(), startPoint, endPoint, "picking", trayCode);
        //执行记录表
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.NO);
        taskWcsRecord.setPurposePosition(endPoint);
        taskWcsRecord.setStartPosition(startPoint);
        taskWcsRecord.setTaskWcsId(taskWcsVO.getId());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_AGV_PICKING);//二楼agv
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        if (picking != null) {
            taskWcsRecord.setSendData(String.valueOf(picking.get("sendData")));
            taskWcsRecord.setAcceptData(String.valueOf(picking.get("acceptData")));
            if (picking.get("orderId") != null) {
                taskWcsRecord.setStatus(Constants.YES);
                taskWcsRecord.setOrderId(Long.valueOf(picking.get("orderId").toString()));
                map.put("resultCode", 0);
                map.put("resultMsg", "请求成功");
                map.put("resultInfo", picking);
            } else {
                map.put("reqID", param.get("trayCode"));
                map.put("resultCode", -1);
                map.put("resultMsg", "请求失败,执行agv任务失败！");
            }
        } else {
            map.put("resultCode", -1);
            map.put("resultMsg", "请求失败,执行agv任务失败！");
        }
        taskWcsRecordService.insertTTaskWcsRecord(taskWcsRecord);
        return map;
    }


    @Log(title = "agv一楼调用叉车接口", businessType = BusinessType.INSERT)
    @Transactional
    @PostMapping("/agvSlim")
    public AjaxResult agvSlim(@RequestBody AgvEntity param) {
        if (ObjectUtils.isEmpty(param.getStartPoint())
                || ObjectUtils.isEmpty(param.getEndPoint())) {
            return AjaxResult.error("参数不全");
        }
        String startPoint = param.getStartPoint();
        String endPoint = param.getEndPoint();
        String trayCode = "";
        String type = param.getType();
        if (Constants.WCS_TASK_TYPE_OUT.equals(type)) {
            //出库
        } else {
        }
        Map<String, Object> picking = agvReportUtil.sendAgvReport(null, startPoint, endPoint, "slim", trayCode);
        //执行记录表
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.NO);
        taskWcsRecord.setPurposePosition(endPoint);
        taskWcsRecord.setStartPosition(startPoint);
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_AGV_SLIM);//一楼叉车
        if (picking != null) {
            taskWcsRecord.setSendData(String.valueOf(picking.get("sendData")));
            taskWcsRecord.setAcceptData(String.valueOf(picking.get("acceptData")));
            if (picking.get("orderId") != null) {
                taskWcsRecord.setOrderId(Long.valueOf(picking.get("orderId").toString()));
                taskWcsRecord.setStatus(Constants.YES);
            }
        }
        taskWcsRecordService.insertTTaskWcsRecord(taskWcsRecord);
        return AjaxResult.success();
    }



}
