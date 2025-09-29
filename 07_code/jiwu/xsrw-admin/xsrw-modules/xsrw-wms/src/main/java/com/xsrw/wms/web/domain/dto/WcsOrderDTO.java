package com.xsrw.wms.web.domain.dto;

/**
 * @author wxr
 * @date 2023/10/25 11:25
 */
public class WcsOrderDTO {


    /**
     * 任务id
     */
    private Long taskWcsId;
    /**
     * 任务号
     */
    private String taskNo;

    /**
     * 主任务编号
     */
    private String mainTaskNo;

    /**
     * 前置任务号
     */
    private String moveTaskNo;

    /**
     * 库位id
     */
    private Long locationId;
    /**
     *
     * 库位编码
     */
    private String locationCode;
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
    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 主任务优先级
     */
    private Integer mainSort;


    public Long getTaskWcsId() {
        return taskWcsId;
    }

    public void setTaskWcsId(Long taskWcsId) {
        this.taskWcsId = taskWcsId;
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

    public String getMoveTaskNo() {
        return moveTaskNo;
    }

    public void setMoveTaskNo(String moveTaskNo) {
        this.moveTaskNo = moveTaskNo;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getMainSort() {
        return mainSort;
    }

    public void setMainSort(Integer mainSort) {
        this.mainSort = mainSort;
    }

    public WcsOrderDTO() {

    }

    public WcsOrderDTO(Long taskWcsId, String taskNo, String startStation, String endStation, String locationCode, String trayNo) {
        this.taskWcsId = taskWcsId;
        this.taskNo = taskNo;
        this.startStation = startStation;
        this.endStation = endStation;
        this.locationCode = locationCode;
        this.trayNo = trayNo;

    }

    public WcsOrderDTO(Long taskWcsId, String taskNo, String startStation, String endStation,String trayNo) {
        this.taskWcsId = taskWcsId;
        this.taskNo = taskNo;
        this.startStation = startStation;
        this.endStation = endStation;
        this.trayNo = trayNo;
    }

}
