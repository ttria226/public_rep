//package com.xsrw.wms.web;
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
//import com.github.pagehelper.PageHelper;
//import com.xsrw.common.core.utils.StringUtils;
//import com.xsrw.common.core.web.controller.BaseController;
//import com.xsrw.common.log.annotation.Log;
//import com.xsrw.common.log.enums.BusinessType;
//import com.xsrw.wms.base.common.Constants;
//import com.xsrw.wms.base.service.ITTrayService;
//import com.xsrw.wms.inout.domain.TTaskWcs;
//import com.xsrw.wms.inout.domain.TTaskWcsRecord;
//import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
//import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
//import com.xsrw.wms.inout.service.ITTaskWcsRecordService;
//import com.xsrw.wms.inout.service.ITTaskWcsService;
//import com.xsrw.wms.web.domain.*;
//import com.xsrw.wms.web.util.WcsReportUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//
///**
// * @author wxr
// * @date 2023/10/18 16:31
// */
//@RestController
//@RequestMapping("/agv")
//public class AgvRollbackController extends BaseController {
//
//    @Autowired
//    private ITTaskWcsService taskWcsService;
//    @Autowired
//    private ITTaskWcsRecordService taskWcsRecordService;
//    @Autowired
//    private ITTrayService trayService;
//    @Autowired
//    private WcsReportUtil wcsReportUtil;
//
//    /**
//     * 订单状态
//     * ;active正在处理;dispatchedAGV调度中;source_finish起点完成;waiting_cancel订单取消中;waiting_manually_finish订单手动完成中;
//     * finish订单完成;error订单出错;cancel_finish订单取消完成;cancel_finish订单取消完成;
//     */
//    //订单状态 finish订单完成
//    private static String AGV_REPORT_ORDER_STATUS_FINISH = "finish";
//    //订单状态 error订单出错
//    private static String AGV_REPORT_ORDER_STATUS_ERROR = "error";
//    //订单状态 cancel_finish订单取消完成
//    private static String AGV_REPORT_ORDER_STATUS_CANCEL = "cancel_finish";
//    //订单状态 cancel_finish订单取消完成
//    private static String AGV_REPORT_ORDER_STATUS_END = "manually_finish";
//
//    /**
//     * agv状态上报
//     *
//     * @return
//     */
//    @Log(title = "agv状态上报", businessType = BusinessType.INSERT)
//    @Transactional
//    @PostMapping("/orderStatusReport/")
//    public AgvResultEntity orderStatusReport(@RequestBody AgvReportEntity agvReportEntity) {
//        AgvResultEntity resultEntity = new AgvResultEntity();
//        resultEntity.setResultCode(0);
//        resultEntity.setMsg("success");
//        resultEntity.setOrderID(agvReportEntity.getOrderID());
//        if (StringUtils.isEmpty(agvReportEntity.getExtraInfo1())) {
//            resultEntity.setMsg("无载具信息");
//            return resultEntity;
//        }
//
//        // 获取载具编码信息
//        JSONObject extraIn = JSONObject.parseObject(agvReportEntity.getExtraInfo1());
//        if (ObjectUtils.isEmpty(extraIn.get("objectNum"))) {
//            resultEntity.setMsg("无载具信息");
//            return resultEntity;
//        }
//        String trayCode = extraIn.get("objectNum").toString();
//        String tsName = extraIn.get("taskType").toString();//任务类型
//
//
//        if (AGV_REPORT_ORDER_STATUS_FINISH.equals(agvReportEntity.getOrderStatus())
//                || AGV_REPORT_ORDER_STATUS_END.equals(agvReportEntity.getOrderStatus())) {
//            TTaskWcsVO taskWcsVO = null;
//
//            if (Constants.AGV_TS_NAME_SLIM.equals(tsName)) {
//                //一楼叉车
//            } else if (Constants.AGV_TS_NAME_PICKING.equals(tsName)) {
//                //二楼agv
//                //todo 处理业务 执行
//                PageHelper.clearPage();
//                taskWcsVO = taskWcsService.getTaskInfoByTrayCode(trayCode, null);
//                if (taskWcsVO != null) {
//                    if (Constants.WCS_TASK_TYPE_IN.equals(taskWcsVO.getTaskType())) {
//                        //入库
//                        TTaskWcsDTO taskWcsDTO = new TTaskWcsDTO();
//                        taskWcsDTO.setId(taskWcsVO.getId());
//                        taskWcsService.executeTaskNew(taskWcsDTO);
//                    } else if (Constants.WCS_TASK_TYPE_OUT.equals(taskWcsVO.getTaskType())) {
//                        //出库
//                        //更新wcs任务状态
//                        taskWcsService.update(
//                                new UpdateWrapper<TTaskWcs>()
//                                        .eq("id", taskWcsVO.getId())
//                                        .set("task_status", Constants.WCS_EXECUTE_STATUS_ING));
//                    } else if (Constants.TASK_TYPE_OUT.equals(taskWcsVO.getTaskType()) || Constants.TASK_TYPE_BACK.equals(taskWcsVO.getTaskType())) {
//                        //载具出库、回库
//                        trayService.completeTrayBack(taskWcsVO);
//                    }
//                }
//            }
//            //执行记录表
//            TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
//            taskWcsRecord.setTaskWcsId(taskWcsVO == null ? null : taskWcsVO.getId());
//            taskWcsRecord.setAcceptData(JSONObject.toJSONString(agvReportEntity));
//            taskWcsRecord.setWcsType("agv:" + tsName);//wcs
//            taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
//            taskWcsRecord.setStatus(Constants.YES);
//            taskWcsRecordService.insertTTaskWcsRecord(taskWcsRecord);
//        } else if (AGV_REPORT_ORDER_STATUS_ERROR.equals(agvReportEntity.getOrderStatus())
//                || AGV_REPORT_ORDER_STATUS_CANCEL.equals(agvReportEntity.getOrderStatus())) {
//            PageHelper.clearPage();
//            TTaskWcsVO taskWcsVO = taskWcsService.getTaskInfoByTrayCode(trayCode, null);
//            if (Constants.AGV_TS_NAME_SLIM.equals(tsName)) {
//                //一楼叉车
//            } else if (Constants.AGV_TS_NAME_PICKING.equals(tsName)) {
//                //二楼agv
//                //todo 处理业务 执行
//                if (taskWcsVO != null) {
//                    //更新任务状态
//                    TTaskWcs taskWcsDTO = new TTaskWcs();
//                    taskWcsDTO.setId(taskWcsVO.getId());
//                    taskWcsDTO.setTaskStatus(Constants.WCS_EXECUTE_STATUS_FAIL);
//                    taskWcsDTO.setErrorMessage("订单取消");
//                    taskWcsService.updateById(taskWcsDTO);
//                }
//            }
//            //执行记录表
//            TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
//            taskWcsRecord.setTaskWcsId(taskWcsVO == null ? null : taskWcsVO.getId());
//            taskWcsRecord.setAcceptData(JSONObject.toJSONString(agvReportEntity));
//            taskWcsRecord.setWcsType("agv:" + tsName);//wcs
//            taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_ROLLBACK);
//            taskWcsRecord.setStatus(Constants.NO);
//            taskWcsRecordService.insertTTaskWcsRecord(taskWcsRecord);
//        }
//        return resultEntity;
//    }
//
////    @GetMapping("/test")
////    public AjaxResult test() {
////        WcsSendEntity sendEntity = new WcsSendEntity();
////        WcsOrderEntity orderEntity = new WcsOrderEntity();
////        sendEntity.setProductDetails(Collections.singletonList(orderEntity));
////        WcsResultEntity wcsResult = wcsReportUtil.wcsSend(WcsReportUtil.wcsIn, sendEntity);
////        return AjaxResult.success(wcsResult);
////    }
//
//
//}
