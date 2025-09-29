package com.xsrw.wms.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TCategory;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITGoodShelfService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.check.domain.TCheckResult;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.check.service.ITCheckResultService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TTaskOut;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailAPPVO;
import com.xsrw.wms.inout.domain.vo.TTaskOutDetailListVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.inout.service.ITTaskWcsRecordService;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITMoveLibraryNewService;
import com.xsrw.wms.stock.service.impl.TMoveLibraryNewServiceImpl;
import com.xsrw.wms.web.domain.WcsResultEntity;
import com.xsrw.wms.web.domain.AgvEntity;
import com.xsrw.wms.web.domain.WcsSendEntity;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsMoveUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Autowired
    private TTaskMapper tTaskMapper;
    @Autowired
    private ITTaskService itTaskService;
    @Autowired
    private ITTrayService itTrayService;
    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;
    @Autowired
    private ITTaskDetailService itTaskDetailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITCheckResultService itCheckResultService;

    @Autowired
    private ITMoveLibraryNewService itMoveLibraryNewService;


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

        //判断回库是否为一身位
        if ("1".equals(taskWcsVO.getExtentionType())){
            itMoveLibraryNewService.selectReissueMove(taskWcsVO);
        }


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

        // 查询任务是否已经下发过
        List<TTaskWcsRecord> taskWcsId = taskWcsRecordService.list(new QueryWrapper<TTaskWcsRecord>()
                .eq("task_wcs_id", taskWcsVO.getId())
                .eq("del_flag",Constants.DEL_FLAG_NO));
        if (taskWcsId.size() > 0){
            map.put("resultCode", -1);
            map.put("resultMsg", "请求失败，载具任务已下发");
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
        if(Constants.WCS_TASK_TYPE_IN.equals(type)) {
            //如果是入库，则判断是否有进行中的任务，如果有则返回
            //查询是否有入库或回库的未完成任务 taskType不等于入库1和回库4；且 taskStatus状态不等于3
            if(taskWcsService.countNotDone() > 0) {
                return AjaxResult.error("当前有正在执行的出库任务，请稍后操作");
            }
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


    /**
     * 盘点任务，wcs入库任务状态上报
     *
     * @return
     */
    @PostMapping("/inTaskCheck")
    @Log(title = "盘点任务wcs入库任务状态上报", businessType = BusinessType.INSERT)
    public Map<String, Object> inTaskCheck(@RequestBody Map<String, Object> param) {
        //入库回调
        //更改当前执行的这一次载具的执行状态
        //重新调用激活接口

        //激活接口：每次获取一条未执行状态的的载具编号，进行出库，发送出库指令
        //下一步进行出库回调：验证frid是否全部有效，有效了，执行入库指令
        //最后进行入库回调：更新状态-重新调用激活接口

        //停止任务： 将未执行状态的所有任务明细改为已停止状态。
        Map<String, Object> map = new HashMap<>();
        map.put("reqID", param.get("reqID"));
        map.put("resultTime", param.get("reqTime"));
//        map.put("taskStatus", param.get("taskStatus"));
//        map.put("trayNo", param.get("trayNo"));  //托盘条码

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

        TTaskWcsVO taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
        if (1 == sendEntity.getProductDetails().get(0).getTaskStatus()) {
            //获取盘点任务明细列表
//            List<TTaskDetail> taskDetailList =tTaskDetailMapper.selectList(Wrappers.lambdaQuery(TTaskDetail.class)
//                            .eq(TTaskDetail:: getLocationId,tTray.getLocationId())
//                            .eq(TTaskDetail::getTrayId,tTray.getId())
//                            .eq(TTaskDetail::getStatus,Constants.TASK_STATUS_ING));
//            //更新盘点任务明细的执行状态
//            List<Long> taskIds =taskDetailList.stream().map(TTaskDetail:: getTaskId).collect(Collectors.toList());
//            tTaskDetailMapper.updateStatusByTaskIds(taskIds.toArray(new Long[0]),Constants.WCS_EXECUTE_STATUS_END,null);
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
            //更改当前执行的载具的执行状态
            String trayNo =sendEntity.getProductDetails().get(0).getTrayNo(); //载具编号
            TTray tTray = tTrayMapper.getTrayInfo(trayNo,null);
            //获取盘点任务的执行状态
            List<TTask> tTaskList = itTaskService.list(new QueryWrapper<TTask>().eq("tray_id",tTray.getId()).eq("task_status",Constants.TASK_STATUS_ING));
            if (tTaskList.size()>0) {
                TTask tTask = tTaskList.get(0);
                tTask.setTaskStatus(Constants.TASK_STATUS_END);
                itTaskService.updateById(tTask);
            }

        }else {
            if (taskWcsVO != null) {
                taskWcsService.update(
                        new UpdateWrapper<TTaskWcs>()
                                .eq("task_no", sendEntity.getReqID())
                                .set("task_status", Constants.WCS_EXECUTE_STATUS_FAIL));
            }
        }

        //重新调用激活接口
        //获取已激活、执行中的任务列表
        List<TTask> tTaskList = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                .eq(TTask::getActivateStatus, 1)
                .eq(TTask::getTaskStatus, Constants.TASK_STATUS_NO)
        );
        if (tTaskList.size() > 0 ) {
            TTask tTask = tTaskList.get(0);
            TTray tTray = tTrayMapper.selectById(tTask.getTrayId());
            //调用WCS出库指令
            itTaskService.takeOut(tTray,tTask.getId());
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
     * 盘点任务，wcs出库任务状态上报
     *
     * @return
     */
    @Log(title = "盘点任务wcs出库任务状态上报", businessType = BusinessType.INSERT)
    @PostMapping("/outTaskCheck")
    @Transactional
    public Map<String, Object> outTaskCheck(@RequestBody Map<String, Object> param) {

        //接收到rfid, 去tmd表匹配
//        匹配到了，请求入库接口，下发入库指令，
//        匹配部分，返回1校验失败

        Map<String, Object> map = new HashMap<>();
        map.put("reqID", param.get("reqID"));
        map.put("resultTime", param.get("reqTime"));
        //taskStatus
//        1=任务完成（如盘点结果=1校验失败，WCS重新控制输送线及RFID进行RFID读取操作后，再次上报任务完成，由WMS判断盘点结果是否正确）；
//        2=任务执行中（任务异常恢复后，实时上报WMS）；
//        3=任务异常（设备执行异常时，实时上报WMS）
//        map.put("taskStatus", param.get("taskStatus"));
//        map.put("Rfids", param.get("Rfids"));  //执行结果=1任务完成时写入WCS读取到的托盘上RFID列表，多个RFID逗号区分，例：20290101,20290102,2290103.....


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

        // 记录wcs任务上报信息
        TTaskWcsRecord wcsRecord = new TTaskWcsRecord();
        wcsRecord.setTaskWcsId(taskWcsVO == null ? null : taskWcsVO.getId());
        wcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
        wcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
        wcsRecord.setAcceptData(JSONObject.toJSONString(sendEntity));
        wcsRecord.setStatus(Constants.YES);
        tTaskWcsRecordMapper.insert(wcsRecord);

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
            //载具
            TTray tTray = itTrayService.selectTTrayByCode(sendEntity.getProductDetails().get(0).getTrayNo());
            Object _obj = redisTemplate.opsForValue().get("requestNumber_" + tTray.getId());
            if (_obj!=null && Integer.parseInt(_obj + "") >= 2) {
                //调用入库指令
                itTaskService.recycle(tTray);
                //清除缓存
                redisTemplate.delete("requestNumber_" + tTray.getId());

                map.put("resultCode", 0);
                map.put("InventoryResult", 0);  //0=校验成功（盘点出库任务结束，WCS等待WMS通过盘点入库请求接口下发重新入库请求）
                map.put("resultMsg", "请求成功");
                return map;
            }

            Object object = sendEntity.getProductDetails().get(0).getRfids();
            String[] rfids = object.toString().split(",");
            List<TMaterialDetail> tMaterialDetailList = tMaterialDetailMapper.selectList(Wrappers.lambdaQuery(TMaterialDetail.class)
                    .in(TMaterialDetail::getRfid, rfids)
                    .eq(TMaterialDetail::getDelFlag, Constants.NO));
            if (tMaterialDetailList.size() == 0) {
                if (_obj == null){
                    redisTemplate.opsForValue().set("requestNumber_" + tTray.getId(), 1);
                }else {
                    Integer num = Integer.parseInt(_obj + "");
                    num =  num +1;
                    redisTemplate.opsForValue().set("requestNumber_" + tTray.getId(), num);
                }

                map.put("resultCode", 1);
                map.put("InventoryResult", 1);  //1=校验失败（需WCS重新读取RFID后再次上报）
                map.put("resultMsg", "rfid对应的物料信息不存在");
                return map;
            }
            //扫描这个载具下面的所有rfid 的实际数量
            TMaterialDetail tMaterialDetail = tMaterialDetailList.get(0);
            //任务主表
            List<TTask> tTaskList = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                    .eq(TTask::getTrayId, tMaterialDetail.getTrayId())
                    .eq(TTask::getTaskStatus, Constants.TASK_STATUS_ING)
            );
            if (tTaskList.size() > 0) {
                TTask tTask = tTaskList.get(0);

                //盘点明细
                List<TTaskDetail> taskDetailList = tTaskDetailMapper.selectList(Wrappers.lambdaQuery(TTaskDetail.class)
                        .eq(TTaskDetail::getTaskId, tTask.getId()));
                List<String> rfidlist = taskDetailList.stream().map(TTaskDetail::getRfid).collect(Collectors.toList());

                //判断扫描的rfid列表是否包含任务明细表中的物料
                List<String> result = rfidlist.stream()
                        .filter(Arrays.asList(rfids)::contains)
                        .collect(Collectors.toList());

                if (result.size() == rfidlist.size()) {
                    //更新任务子表的状态 为已完成
                    List<Long> detailIds = taskDetailList.stream().map(e -> e.getId()).collect(Collectors.toList());
                    tTaskDetailMapper.updateStatusByIds(detailIds.toArray(new Long[0]),Constants.TASK_DETAIL_STATUS_END,null);
                    taskDetailList = taskDetailList.stream().map(item -> {
                            item.setStatus(Constants.TASK_DETAIL_STATUS_END);
                            item.setActualCount(item.getPredictCount());

                            //生成盘点结果
                            TCheckResult tCheckResult = new TCheckResult();
                            tCheckResult.setMaterialId(item.getMaterialId());
                            tCheckResult.setTaskDetailId(item.getId());
                            tCheckResult.setActualCount(item.getPredictCount());
                            tCheckResult.setCheckDifferenceCount(item.getActualCount().subtract(item.getPredictCount()));
                            tCheckResult.setTaskDetailId(item.getId());
                            tCheckResult.setTaskId(item.getTaskId());
                            tCheckResult.setBatchCode(item.getBatchNumber());
                            itCheckResultService.save(tCheckResult);
                        return item;
                    }).collect(Collectors.toList());
                    itTaskDetailService.updateBatchById(taskDetailList);  //修改状态和实际数量
                    //请求入库接口，下发入库指令
                    itTaskService.recycle(tTray);
                } else {
                    //盘点数据不一致，累积请求次数，请求次数三次，自动入库
                    Object obj = redisTemplate.opsForValue().get("requestNumber_" + tTray.getId());
                    if (obj == null){
                        redisTemplate.opsForValue().set("requestNumber_" + tTray.getId(), 1);
                    }else {
                        Integer num = Integer.parseInt(obj + "");
                        if (num >= 2) {
                            //调用入库指令
//                            TTray tTray = itTrayService.selectTTrayById(tMaterialDetail.getTrayId());
                            itTaskService.recycle(tTray);

                            redisTemplate.delete("requestNumber_" + tTray.getId());

                            map.put("resultCode", 0);
                            map.put("InventoryResult", 0);  //0=校验成功（盘点出库任务结束，WCS等待WMS通过盘点入库请求接口下发重新入库请求）
                            map.put("resultMsg", "请求成功");
                            return map;
                        } else {
                            num =  num +1;
                            redisTemplate.opsForValue().set("requestNumber_" + tTray.getId(), num);
                        }
                    }

                    map.put("resultCode", 1);
                    map.put("InventoryResult", 1);  //1=校验失败（需WCS重新读取RFID后再次上报）
                    map.put("resultMsg", "实际数量与账面数量不一致");
                    return map;
                }
            }else {
                map.put("resultCode", 1);
                map.put("InventoryResult", 1);  //1=校验失败（需WCS重新读取RFID后再次上报）
                map.put("resultMsg", "未匹配到物料信息");
                return map;
            }
        }else {
            taskWcsVO = taskWcsService.getTaskInfoByTaskNo(sendEntity.getReqID());
            if (taskWcsVO != null) {
                //出库
                taskWcsService.update(
                        new UpdateWrapper<TTaskWcs>()
                                .eq("task_no", sendEntity.getReqID())
                                .set("task_status", Constants.WCS_EXECUTE_STATUS_FAIL));
            }
        }

        map.put("resultCode", 0);
        map.put("InventoryResult", 0);  //0=校验成功（盘点出库任务结束，WCS等待WMS通过盘点入库请求接口下发重新入库请求）
        map.put("resultMsg", "请求成功");
        return map;
    }

}
