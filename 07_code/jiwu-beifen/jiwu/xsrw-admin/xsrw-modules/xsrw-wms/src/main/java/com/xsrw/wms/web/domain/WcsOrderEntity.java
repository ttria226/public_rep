package com.xsrw.wms.web.domain;

import io.swagger.models.auth.In;

/**
 * @Description: WCS单据实体类
 * @Author XMING
 * @Date 2023-10-17
 */
public class WcsOrderEntity {


    /**
     * 任务号
     */
    private String taskNo;

    /**
     * 前置任务号
     */
    private String beforeTaskNo;
    /**
     * 后置任务号
     */
    private String nextTaskNo;

    /**
     * 物料编码
     */
    private String productCode;

    /**
     * 物料描述
     */
    private String productDesc;

    /**
     * 托盘条码
     */
    private String trayNo;

    /**
     * 站台编码
     */
    private String startStation;

    /**
     * 库位编码
     */
    private String endStation;

    /**
     * 执行结果
     */
    public Integer taskStatus;

    /**
     * 结果说明
     */
    private String taskDesc;


    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getBeforeTaskNo() {
        return beforeTaskNo;
    }

    public void setBeforeTaskNo(String beforeTaskNo) {
        this.beforeTaskNo = beforeTaskNo;
    }

    public String getNextTaskNo() {
        return nextTaskNo;
    }

    public void setNextTaskNo(String nextTaskNo) {
        this.nextTaskNo = nextTaskNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getTrayNo() {
        return trayNo;
    }

    public void setTrayNo(String trayNo) {
        this.trayNo = trayNo;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
