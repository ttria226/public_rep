package com.xsrw.wms.kanban.domain.vo;


/**
 * 设备统计返回参数类
 */
public class EquipmentStatisticsVO {


    /**
     * 设备总数
     */
    private Integer totalCount;

    /**
     * 正常设备数量
     */
    private Integer normalCount;

    /**
     * 故障设备数量
     */
    private Integer faultyCount;

    /**
     * 巡检设备数量
     */
    private Integer inspectionCount;

    /**
     * 保养设备数量
     */
    private Integer maintenanceCount;

    public Integer getMaintenanceCount() {
        return maintenanceCount;
    }

    public void setMaintenanceCount(Integer maintenanceCount) {
        this.maintenanceCount = maintenanceCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(Integer normalCount) {
        this.normalCount = normalCount;
    }

    public Integer getFaultyCount() {
        return faultyCount;
    }

    public void setFaultyCount(Integer faultyCount) {
        this.faultyCount = faultyCount;
    }

    public Integer getInspectionCount() {
        return inspectionCount;
    }

    public void setInspectionCount(Integer inspectionCount) {
        this.inspectionCount = inspectionCount;
    }
}
