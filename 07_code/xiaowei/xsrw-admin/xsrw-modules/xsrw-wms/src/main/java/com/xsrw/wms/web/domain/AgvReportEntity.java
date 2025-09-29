package com.xsrw.wms.web.domain;

/**
 * agv上报参数
 *
 * @author wxr
 * @date 2023/10/19 10:13
 */
public class AgvReportEntity {


    /**
     * 订单ID
     */
    private Long orderID;
    /**
     * 订单名
     */
    private String orderName;
    /**
     * 订单状态
     * waiting新订单;active正在处理;dispatchedAGV调度中;source_finish起点完成;waiting_cancel订单取消中;waiting_manually_finish订单手动完成中;
     * finish订单完成;error订单出错;cancel_finish订单取消完成;manually_finish订单手动完成;
     */
    private String orderStatus;
    /**
     * 指派的AGV的ID列表
     */
    private String agvIDList;
    /**
     * 订单优先级
     */
    private Integer priority;
    /**
     * 当前目的地
     */
    private String currentDes;
    /**
     * 当前指令
     */
    private String currentCmd;
    /**
     * 错误码
     */
    private Integer errorCode;
    /**
     * 订单的截至时间
     */
    private String deadLine;
    /**
     * 订单的创建时间
     */
    private String createdTime;
    /**
     * 额外信息1
     */
    private String extraInfo1;
    /**
     * 额外信息2
     */
    private String extraInfo2;

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAgvIDList() {
        return agvIDList;
    }

    public void setAgvIDList(String agvIDList) {
        this.agvIDList = agvIDList;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCurrentDes() {
        return currentDes;
    }

    public void setCurrentDes(String currentDes) {
        this.currentDes = currentDes;
    }

    public String getCurrentCmd() {
        return currentCmd;
    }

    public void setCurrentCmd(String currentCmd) {
        this.currentCmd = currentCmd;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getExtraInfo1() {
        return extraInfo1;
    }

    public void setExtraInfo1(String extraInfo1) {
        this.extraInfo1 = extraInfo1;
    }

    public String getExtraInfo2() {
        return extraInfo2;
    }

    public void setExtraInfo2(String extraInfo2) {
        this.extraInfo2 = extraInfo2;
    }

    @Override
    public String toString() {
        return "AgvReportEntity{" +
                "orderID=" + orderID +
                ", orderName='" + orderName + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", agvIDList='" + agvIDList + '\'' +
                ", priority=" + priority +
                ", currentDes='" + currentDes + '\'' +
                ", currentCmd='" + currentCmd + '\'' +
                ", errorCode=" + errorCode +
                ", deadLine='" + deadLine + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", extraInfo1='" + extraInfo1 + '\'' +
                ", extraInfo2='" + extraInfo2 + '\'' +
                '}';
    }
}
