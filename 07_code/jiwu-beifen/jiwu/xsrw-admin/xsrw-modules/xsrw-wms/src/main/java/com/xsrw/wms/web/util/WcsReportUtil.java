package com.xsrw.wms.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.uuid.UUID;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.web.domain.WcsOrderEntity;
import com.xsrw.wms.web.domain.WcsResultEntity;
import com.xsrw.wms.web.domain.WcsSendEntity;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: WCS 任务下发工具类
 * @Author XMING
 * @Date 2023-10-17
 */
@Component
public class WcsReportUtil {

    // 站台编码 -- 入库
    public static final String stationIn = "TrayStationIn";

    // 站台编码 -- 出库
    public static final String stationOut = "TrayStationOut";

    // 入库
    public static final String wcsIn = "1";

    // 出库
    public static final String wcsOut = "2";

    // 移库
    public static final String wcsMove = "3";

    // 出库地址
    @Value("${web.wcs.out-report-url}")
    private String outReportUrl;
    // 入库地址
    @Value("${web.wcs.in-report-url}")
    private String inReportUrl;
    // 移库地址
    @Value("${web.wcs.move-report-url}")
    private String moveReportUrl;

    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;

    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;

    /**
     * WCS 请求下发
     *
     * @param inOut      任务类型 1 出库、2入库、3移库
     * @param sendEntity 请求参数实体类
     * @return
     */
    public WcsResultEntity wcsSend(String inOut, WcsSendEntity sendEntity) {

        String url = "";
        // 入库
        if (wcsIn.equals(inOut)) {
            url = inReportUrl;
        }
        // 出库
        if (wcsOut.equals(inOut)) {
            url = outReportUrl;
        }
        // 移库
        if (wcsMove.equals(inOut)){
            url = moveReportUrl;
        }

        try {
            // 组装参数  发送请求
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            sendEntity.setReqTime(DateUtils.getTime());
            post.setEntity(new StringEntity(JSON.toJSONString(sendEntity).toString(), "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String st = EntityUtils.toString(entity, "UTF-8");

            // 处理请求结果
            WcsResultEntity resultEntity = JSON.parseObject(st, WcsResultEntity.class);
            return resultEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * 组完盘，发送wcs入库命令
     *
     * @param order
     */
    public String sendWcsInReport(WcsOrderDTO order) {
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
        //执行记录表
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.YES);
        WcsSendEntity sendEntity = new WcsSendEntity();
        sendEntity.setReqID(order.getTaskNo());
        sendEntity.setReqTime(DateUtils.getTime());
        sendEntity.setOrderNo(order.getTaskNo());
        WcsOrderEntity orderEntity = new WcsOrderEntity();
        orderEntity.setTaskNo(order.getTaskNo());
        if(StringUtils.isNotEmpty(order.getMoveTaskNo())){
            orderEntity.setBeforeTaskNo(order.getMoveTaskNo());
        }
        orderEntity.setStartStation(WcsReportUtil.stationIn);
        orderEntity.setEndStation(order.getEndStation());
        orderEntity.setTrayNo(order.getTrayNo());
        sendEntity.setProductDetails(Collections.singletonList(orderEntity));
        try {
            WcsResultEntity wcsResult = this.wcsSend(WcsReportUtil.wcsIn, sendEntity);
            if(wcsResult != null){
                taskWcsRecord.setAcceptData(wcsResult.toString());
                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
            }else{
                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskWcsRecord.setAcceptData(e.getMessage());
            taskWcsRecord.setStatus(Constants.NO);
            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
        }
        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
        taskWcsRecord.setPurposePosition(order.getEndStation());
        taskWcsRecord.setStartPosition(WcsReportUtil.stationIn );
        taskWcsRecord.setTaskWcsId(order.getTaskWcsId());
        taskWcsRecord.setMainTaskNo(order.getTaskNo());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);//wcs
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
        tTaskWcsRecordMapper.insert(taskWcsRecord);
        //更新wcs任务状态
        tTaskWcsMapper.updateStuasById(order.getTaskWcsId(),msgStatus);
        return msgStatus;
    }

    /**
     * 发送wcs出库请求
     * @param order
     */
    public String sendWcsOutReport(WcsOrderDTO order) {
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
        //执行记录表
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.YES);
        WcsSendEntity sendEntity = new WcsSendEntity();
        sendEntity.setReqID(order.getTaskNo());
        sendEntity.setReqTime(DateUtils.getTime());
        sendEntity.setOrderNo(order.getTaskNo());
        WcsOrderEntity orderEntity = new WcsOrderEntity();
        orderEntity.setTaskNo(order.getTaskNo());
        if(StringUtils.isNotEmpty(order.getMoveTaskNo())){
            orderEntity.setBeforeTaskNo(order.getMoveTaskNo());
        }
        orderEntity.setStartStation(order.getStartStation());
        orderEntity.setEndStation(order.getEndStation());
        orderEntity.setTrayNo(order.getTrayNo());
        sendEntity.setProductDetails(Collections.singletonList(orderEntity));
        try {
            WcsResultEntity wcsResult = this.wcsSend(WcsReportUtil.wcsOut, sendEntity);
            if(wcsResult != null){
                taskWcsRecord.setAcceptData(wcsResult.toString());
                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
            }else{
                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskWcsRecord.setAcceptData(e.getMessage());
            taskWcsRecord.setStatus(Constants.NO);
            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
        }
        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
        taskWcsRecord.setPurposePosition(order.getEndStation());
        taskWcsRecord.setStartPosition(order.getStartStation() );
        taskWcsRecord.setTaskWcsId(order.getTaskWcsId());
        taskWcsRecord.setMainTaskNo(order.getTaskNo());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);//wcs
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
        tTaskWcsRecordMapper.insert(taskWcsRecord);
        //更新wcs任务状态
        tTaskWcsMapper.updateStuasById(order.getTaskWcsId(),msgStatus);
        // 处理同一物料多个批次 同一托盘生成多个wcs任务 更新另外一条wcs任务的状态
        TTaskWcs taskWcs = tTaskWcsMapper.selectById(order.getTaskWcsId());
        List<TTaskWcs> wcsList = tTaskWcsMapper.selectList(
                new QueryWrapper<TTaskWcs>()
                        .eq("tray_id", taskWcs.getTrayId())
                        .eq("task_status", Constants.WCS_EXECUTE_STATUS_NOT)
        );
        if (wcsList.size() > 0){
            tTaskWcsMapper.update(new TTaskWcs(),
                    new UpdateWrapper<TTaskWcs>()
                            .in("id",wcsList.stream().map(TTaskWcs::getId).collect(Collectors.toList()))
                            .set("task_status",msgStatus));
        }
        return msgStatus;
    }

    /**
     * 发送移库请求
     * @param order
     * @return
     */
    public String sendWcsMoveReport(WcsOrderDTO order) {
        String msgStatus = Constants.WCS_EXECUTE_STATUS_NOT;
        //执行记录表
        TTaskWcsRecord taskWcsRecord = new TTaskWcsRecord();
        taskWcsRecord.setStatus(Constants.YES);
        WcsSendEntity sendEntity = new WcsSendEntity();
        sendEntity.setReqID(order.getTaskNo());
        sendEntity.setReqTime(DateUtils.getTime());
        sendEntity.setOrderNo(order.getTaskNo());
        WcsOrderEntity orderEntity = new WcsOrderEntity();
        orderEntity.setTaskNo(order.getTaskNo());
        if(StringUtils.isNotEmpty(order.getMainTaskNo())){
            orderEntity.setNextTaskNo(order.getMainTaskNo());
        }
        orderEntity.setStartStation(order.getStartStation());
        orderEntity.setEndStation(order.getEndStation());
        orderEntity.setTrayNo(order.getTrayNo());
        sendEntity.setProductDetails(Collections.singletonList(orderEntity));
        try {
            WcsResultEntity wcsResult = this.wcsSend(WcsReportUtil.wcsMove, sendEntity);
            if(wcsResult != null){
                taskWcsRecord.setAcceptData(wcsResult.toString());
                msgStatus = Constants.WCS_EXECUTE_STATUS_ING;
            }else{
                msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskWcsRecord.setAcceptData(e.getMessage());
            taskWcsRecord.setStatus(Constants.NO);
            msgStatus = Constants.WCS_EXECUTE_STATUS_FAIL;
        }
        taskWcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
        taskWcsRecord.setPurposePosition(order.getEndStation());
        taskWcsRecord.setStartPosition(order.getStartStation() );
        taskWcsRecord.setTaskWcsId(order.getTaskWcsId());
        taskWcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);//wcs
        taskWcsRecord.setMainTaskNo(order.getMainTaskNo());
        taskWcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);
        tTaskWcsRecordMapper.insert(taskWcsRecord);
        //更新wcs任务状态
        tTaskWcsMapper.updateStuasById(order.getTaskWcsId(),msgStatus);
        return msgStatus;
    }

}
