package com.xsrw.wms.check.domain.dto;


import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.check.domain.TCheckHistory;

import java.util.List;

/**
 * @description 盘点计划DTO
 */
public class CheckDeliveryDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 部门标识
     */
    private Long deptId;

    private String planName;

    /**
     * 库存标识
     */
    private Long stockDetailId;

    /**
     * 状态
     */
    private String status;

    /**
     * 库区标识
     */
    private Long reservoirId;

    /**
     * 库位标识
     */
    private Long locationId;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 批号
     */
    private String batchCode;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料规格
     */
    private String specifications;

    /**
     * 托盘
     */
    private String trayCode;
    /**
     * 物料ids
     */
    private List<Long> materialIds;

    /**
     * 区域
     */
    private Long areaId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 盘点策略
     */
    private String checkType;

    /**
     * 盘点策略细分 4物料、5库区、6区域
     */
    private String deliveryType;

    /**
     * 载具类型 1托盘、2料箱、3地堆
     */
    private String trayType;

    private Integer checkSource;

    /**
     * 盘点数据
     */
    private List<TCheckHistory> historyList;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getCheckSource() {
        return checkSource;
    }

    public void setCheckSource(Integer checkSource) {
        this.checkSource = checkSource;
    }
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getStockDetailId() {
        return stockDetailId;
    }

    public void setStockDetailId(Long stockDetailId) {
        this.stockDetailId = stockDetailId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public List<Long> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<Long> materialIds) {
        this.materialIds = materialIds;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public List<TCheckHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<TCheckHistory> historyList) {
        this.historyList = historyList;
    }

    public String getTrayType() {
        return trayType;
    }

    public void setTrayType(String trayType) {
        this.trayType = trayType;
    }

    @Override
    public String toString() {
        return "CheckDeliveryDTO{" +
                "deptId=" + deptId +
                ", stockDetailId=" + stockDetailId +
                ", status='" + status + '\'' +
                ", reservoirId=" + reservoirId +
                ", locationId=" + locationId +
                ", ids=" + ids +
                ", batchCode='" + batchCode + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specifications='" + specifications + '\'' +
                ", trayCode='" + trayCode + '\'' +
                ", materialIds=" + materialIds +
                ", areaId=" + areaId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", checkType='" + checkType + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", trayType='" + trayType + '\'' +
                ", historyList=" + historyList +
                '}';
    }
}
