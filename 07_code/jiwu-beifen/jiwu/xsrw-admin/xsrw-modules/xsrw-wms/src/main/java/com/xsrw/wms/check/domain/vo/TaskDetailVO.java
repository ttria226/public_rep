package com.xsrw.wms.check.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TTaskDetail;
import io.swagger.models.auth.In;

/**
 * @description 盘点子表VO
 */
public class TaskDetailVO extends TTaskDetail {


    @Excel(name = "任务号",sort = 1)
    private String taskCode;

    /**
     * 物料编码
     */
    @Excel(name = "物料",sort = 2)
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 规格型号
     */
    private String specifications;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 载具(托盘)
     */
    @Excel(name = "载具(托盘),",sort = 3)
    private String trayCode;

    /**
     * 库位编码
     */
    @Excel(name = "库位")
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

    private Long stockNum;

    /**
     * 库区名称
     */
    @Excel(name = "库区")
    private String reservoirName;

    /**
     * 区域名称
     */
    @Excel(name = "区域")
    private String areaName;

    /**
     * 盘点维度  1物料  2库区
     */
    private String checkType;

    /**
     * 盘点差异数量
     */
    @Excel(name = "盘差")
    private Long checkDifferenceCount;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 执行状态
     */
    private String taskStatus;

    /**
     * 源单单号（任务来源的原始单据编号）
     */
    private String originCode;

    /**
     * 批次号
     */
    private String batchNumber;


    /**
     * wcs任务状态
     */
    private String wcsTaskStatus;

    /**
     * wcs任务id
     */
    private Long wcsId;

    /**
     * rfid
     */
    private String rfidHead;

    /**
     * rfid对应数量
     */
    private Integer rfidHeadCount;

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public Long getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    public void setCheckDifferenceCount(Long checkDifferenceCount) {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    @Override
    public String getBatchNumber() {
        return batchNumber;
    }

    @Override
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getWcsTaskStatus() {
        return wcsTaskStatus;
    }

    public void setWcsTaskStatus(String wcsTaskStatus) {
        this.wcsTaskStatus = wcsTaskStatus;
    }

    public Long getWcsId() {
        return wcsId;
    }

    public void setWcsId(Long wcsId) {
        this.wcsId = wcsId;
    }

    public String getRfidHead() {
        return rfidHead;
    }

    public void setRfidHead(String rfidHead) {
        this.rfidHead = rfidHead;
    }

    public Integer getRfidHeadCount() {
        return rfidHeadCount;
    }

    public void setRfidHeadCount(Integer rfidHeadCount) {
        this.rfidHeadCount = rfidHeadCount;
    }
}
