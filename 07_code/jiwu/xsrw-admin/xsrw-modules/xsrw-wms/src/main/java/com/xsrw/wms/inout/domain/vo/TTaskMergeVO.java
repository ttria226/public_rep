package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author XMING
 * @Date 2023-06-27
 */
public class TTaskMergeVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 出库单详情标识 */
    private String mergeDeliveryDetailId;

    private Long stockId;

    /** 在库数量 */
    private Long count;

    /** 物料标识 */
    private Long materialId;

    private BigDecimal receiveCount;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料单位名称
     */
    private String unitName;

    private String smallUnitName;

    /** 批次号 */
    private String batchCode;
    /** 本次预计数量 */
    private BigDecimal predictCount;
    /**
     * 可用数量
     */
    private BigDecimal availableCount;

    /** 小件预计数量 */
    private BigDecimal smallPredictCount;
    /** 载具 */
    private Long trayId;

    private Long status;

    /** 载具 */
    private String trayCode;

    /** 库位 */
    private Long locationId;

    /** 库位 */
    private String locationName;

    private Long reservoirId;

    /** 库位 */
    private String reservoirName;

    private String areaName;

    public String getMergeDeliveryDetailId() {
        return mergeDeliveryDetailId;
    }

    public void setMergeDeliveryDetailId(String mergeDeliveryDetailId) {
        this.mergeDeliveryDetailId = mergeDeliveryDetailId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(BigDecimal receiveCount) {
        this.receiveCount = receiveCount;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSmallUnitName() {
        return smallUnitName;
    }

    public void setSmallUnitName(String smallUnitName) {
        this.smallUnitName = smallUnitName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(BigDecimal availableCount) {
        this.availableCount = availableCount;
    }

    public BigDecimal getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(BigDecimal smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return "TTaskMergeVO{" +
                "mergeDeliveryDetailId='" + mergeDeliveryDetailId + '\'' +
                ", stockId=" + stockId +
                ", count=" + count +
                ", materialId=" + materialId +
                ", receiveCount=" + receiveCount +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", smallUnitName='" + smallUnitName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", predictCount=" + predictCount +
                ", availableCount=" + availableCount +
                ", smallPredictCount=" + smallPredictCount +
                ", trayId=" + trayId +
                ", status=" + status +
                ", trayCode='" + trayCode + '\'' +
                ", locationId=" + locationId +
                ", locationName='" + locationName + '\'' +
                ", reservoirId=" + reservoirId +
                ", reservoirName='" + reservoirName + '\'' +
                ", areaName='" + areaName + '\'' +
                '}';
    }
}
