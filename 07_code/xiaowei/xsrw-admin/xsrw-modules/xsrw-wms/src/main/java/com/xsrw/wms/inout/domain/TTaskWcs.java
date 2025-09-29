package com.xsrw.wms.inout.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * wcs任务对象 t_task_wcs
 *
 * @author wxr
 * @date 2023-05-10
 */
@TableName("t_task_wcs")
public class TTaskWcs extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务类型（1.入库任务、2.出库任务3.盘点任务、4.回库任务、5移库任务、 6.托盘取出）
     */
//    @Excel(name = "任务类型", readConverterExp = "1=入库任务,2=出库任务,3=盘点任务,4=回库任务,5=移库任务,6=托盘取出")
    private String taskType;

    /**
     * 执行状态（1=未执行,2=执行中,3=已完成）
     */
    @Excel(name = "状态", sort = 4, readConverterExp = "1=未执行,2=执行中,3=已完成")
    private String taskStatus;

    /**
     * 托盘id
     */
    private Long trayId;

    /**
     * 托盘code
     */
    @Excel(name = "载具编号", sort = 2)
    private String trayCode;

    /**
     * 库位标识
     */
//    @Excel(name = "库位标识")
    private Long locationId;

    /**
     * 0:正常 1:异常
     */
//    @Excel(name = "0:正常 1:异常")
    private Long result;

    /**
     * 任务号
     */
    @Excel(name = "任务编号", sort = 1)
    private String taskNo;
    /**
     * 关联主任务编号（移库使用）
     */
    private String mainTaskNo;

    /**
     * 目的位置
     */
    private String purposePosition;

    /**
     * 起始位置
     */
    private String startPosition;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiveTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 错误码
     */
    private Long errorCode;

    /**
     * 错误信息
     */
    @Excel(name = "故障原因", sort = 5)
    private String errorMessage;

    /**
     * 当前使用的输送线编号
     */
    private Long currentLineNumber;

    /**
     * 请求执行次数
     */
    private Long requestCount;

    /**
     * 最后一次请求参数
     */
    private String sendData;

    /**
     * 返回数据
     */
    private String acceptData;

    /**
     * 出库类型 1应急出库 0 其他
     */
    private String deliveryType;


    /**
     * 优先级
     */
    private String priority;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public Long getResult() {
        return result;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getMainTaskNo() {
        return mainTaskNo;
    }

    public void setMainTaskNo(String mainTaskNo) {
        this.mainTaskNo = mainTaskNo;
    }

    public void setPurposePosition(String purposePosition) {
        this.purposePosition = purposePosition;
    }

    public String getPurposePosition() {
        return purposePosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setCurrentLineNumber(Long currentLineNumber) {
        this.currentLineNumber = currentLineNumber;
    }

    public Long getCurrentLineNumber() {
        return currentLineNumber;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public String getSendData() {
        return sendData;
    }

    public void setAcceptData(String acceptData) {
        this.acceptData = acceptData;
    }

    public String getAcceptData() {
        return acceptData;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    @Override
    public String toString() {
        return "TTaskWcs{" +
                "id=" + id +
                ", taskType='" + taskType + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", trayId=" + trayId +
                ", trayCode='" + trayCode + '\'' +
                ", locationId=" + locationId +
                ", result=" + result +
                ", taskNo='" + taskNo + '\'' +
                ", purposePosition='" + purposePosition + '\'' +
                ", startPosition='" + startPosition + '\'' +
                ", receiveTime=" + receiveTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", currentLineNumber=" + currentLineNumber +
                ", requestCount=" + requestCount +
                ", sendData='" + sendData + '\'' +
                ", acceptData='" + acceptData + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
