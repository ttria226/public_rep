package com.xsrw.wms.inout.domain.vo;

import com.xsrw.wms.inout.domain.TTaskWcsDetail;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/10 14:56
 */
public class TTaskWcsDetailVO extends TTaskWcsDetail {

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 计划数量
     */
    private Long predictCount;

    /**
     * 实际数量
     */
    private Long actualCount;
    /**
     * 批次号
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
     * 物料单位名称
     */
    private String unitName;

    /**
     * 托盘id
     */
    private Long trayId;

    /**
     * 托盘编号
     */
    private String trayCode;

    /**
     * 库区
     */
    private String reservoirName;
    /**
     * 库位标识
     */
    private Long locationId;
    /**
     * 库位名称
     */
    private String locationName;
    /**
     * 移库用原库位id
     */
    private Long orgLocationId;

    /**
     * 单据详情id
     */
    private Long advanceRegistrationId;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getActualCount() {
        return actualCount;
    }

    public void setActualCount(Long actualCount) {
        this.actualCount = actualCount;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
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

    public Long getOrgLocationId() {
        return orgLocationId;
    }

    public void setOrgLocationId(Long orgLocationId) {
        this.orgLocationId = orgLocationId;
    }

    public Long getAdvanceRegistrationId() {
        return advanceRegistrationId;
    }

    public void setAdvanceRegistrationId(Long advanceRegistrationId) {
        this.advanceRegistrationId = advanceRegistrationId;
    }
}
