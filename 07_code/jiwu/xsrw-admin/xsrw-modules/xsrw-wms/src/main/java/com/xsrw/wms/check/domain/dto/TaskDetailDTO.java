package com.xsrw.wms.check.domain.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务详情dto
 */
public class TaskDetailDTO {

    /**
     * 任务详情id
     */
    private Long id;

    /**
     * 任务标识
     */
    private Long taskId;

    private Long[] taskIds;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 状态
     */
    private String status;

    /**
     * 载具(托盘id)
     */
    private Long trayId;

    /**
     * 托盘code
     */
    private String trayCode;

    /**
     * 库位标识
     */
    private Long locationId;

    /**
     * 值班人员
     */
    private String dutyPersonnel;

    /**
     * 优先级
     */
    private Long priority;

    /**
     * 批号
     */
    private String batchNumber;

    /**
     * 部门标识
     */
    private Long deptId;

    /**
     * 库存标识
     */
    private Long stockId;

    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料ids
     */
    private List<Long> materialIds;

    /**
     * 多个状态，以,分割
     */
    private String statusStrs;

    /**
     * 盘点数量
     */
    private BigDecimal checkNum;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 计划数量
     */
    private Integer predictCount;

    /**
     * 托盘状态 空闲、半托、全托
     */
    private String trayStatus;

    /**
     * 托盘id
     */
    private List<Long> taryList;

    private String taskStatus;

    private Long areaId;

    private Long reservoirId;

    private List<Long> locationList;

    private String position;

    private String statusPda;


    /**
     * 盘点筛选时使用的任务状态字段
     */
    private String checkDeliveryStatus;


    public String getCheckDeliveryStatus() {
        return checkDeliveryStatus;
    }

    public void setCheckDeliveryStatus(String checkDeliveryStatus) {
        this.checkDeliveryStatus = checkDeliveryStatus;
    }

    public String getStatusPda() {
        return statusPda;
    }

    public void setStatusPda(String statusPda) {
        this.statusPda = statusPda;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long[] getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(Long[] taskIds) {
        this.taskIds = taskIds;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getDutyPersonnel() {
        return dutyPersonnel;
    }

    public void setDutyPersonnel(String dutyPersonnel) {
        this.dutyPersonnel = dutyPersonnel;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public List<Long> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<Long> materialIds) {
        this.materialIds = materialIds;
    }

    public String getStatusStrs() {
        return statusStrs;
    }

    public void setStatusStrs(String statusStrs) {
        this.statusStrs = statusStrs;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Integer predictCount) {
        this.predictCount = predictCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrayStatus() {
        return trayStatus;
    }

    public void setTrayStatus(String trayStatus) {
        this.trayStatus = trayStatus;
    }

    public List<Long> getTaryList() {
        return taryList;
    }

    public void setTaryList(List<Long> taryList) {
        this.taryList = taryList;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public List<Long> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Long> locationList) {
        this.locationList = locationList;
    }

    public BigDecimal getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(BigDecimal checkNum) {
        this.checkNum = checkNum;
    }
}
