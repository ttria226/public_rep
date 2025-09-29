package com.xsrw.wms.web.util;

import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.uuid.UUID;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wxr
 * @date 2023/10/17 17:00
 */
@Component
public class AgvReportUtil {
    // agv调用地址
    @Value("${web.agv.send-url}")
    private String agvReportUrl;
    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;


    /**
     * 发送agv命令
     *
     * @param startPoint
     * @param endPoint
     * @param tsName
     * @return
     */
    public Map<String, Object> sendAgvReport(String orderName, String startPoint, String endPoint, String tsName, String trayCode) {
        JsonObject paramMap = new JsonObject();
        orderName = StringUtils.isEmpty(orderName) ? UUID.randomUUID().toString() : orderName;
        paramMap.addProperty("order_name", orderName);//订单名称[uuid或者其他也行，不要超过uuid的长度就行]
        paramMap.addProperty("priority", 1);//优先级
        paramMap.addProperty("dead_line", DateUtils.getTime());//截至时间[发订单的时间戳]
        paramMap.addProperty("ts_name", tsName);//ts名称（可以理解成订单类型）叉车传slim,picking传picking，一楼slim；二楼picking
        paramMap.addProperty("created_user", "wms");//创建者
        //src起点名称-dst终点名称 [RA-12-24-7,输送线的位置到时候定义好]
        String parameters = "{\"src\":\"" + startPoint + "\",\"dst\":\"" + endPoint + "\",\"object_num\":\"" + trayCode + "\"}";
        paramMap.addProperty("parameters", parameters);//额外参数
        return HttpUtils.post(agvReportUrl, paramMap);
    }


    /**
     * 发送agv命令
     *
     * @param order
     */
    public String sendAgvPickingReport(WcsOrderDTO order) {
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
//        String tsName = Constants.AGV_TS_NAME_PICKING;
        //执行记录表
//        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
//        taskWcsRecord.setStatus(Constants.YES);
//        try {
//            Map<String, Object> picking = this.sendAgvReport(order.getTaskNo(), order.getStartStation(), order.getEndStation(), tsName, order.getTrayNo());
//            if (picking != null) {
//                taskWcsRecord.setSendData(String.valueOf(picking.get("sendData")));
//                taskWcsRecord.setAcceptData(String.valueOf(picking.get("acceptData")));
//                if (picking.get("orderId") != null) {
//                    taskWcsRecord.setOrderId(Long.valueOf(picking.get("orderId").toString()));
//                    msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
//                } else {
//                    taskWcsRecord.setStatus(Constants.NO);
//                    msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
//                }
//            }
//        } catch (Exception e) {
//            //todo 调不通设备是否抛异常
//            e.printStackTrace();
//            taskWcsRecord.setAcceptData(e.getMessage());
//            taskWcsRecord.setStatus(Constants.NO);
//            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
//        }
//        taskWcsRecord.setPurposePosition(order.getEndStation());
//        taskWcsRecord.setStartPosition(order.getStartStation());
//        taskWcsRecord.setTaskWcsId(order.getTaskWcsId());
//        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_AGV_PICKING);//picking
//        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
//        tTaskWcsRecordMapper.insert(taskWcsRecord);
        return msgStatus;
    }

}
