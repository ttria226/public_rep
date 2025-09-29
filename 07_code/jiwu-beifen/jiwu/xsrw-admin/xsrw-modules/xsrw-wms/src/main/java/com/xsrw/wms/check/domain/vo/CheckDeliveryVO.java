package com.xsrw.wms.check.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TCheckDelivery;

import java.util.Date;

/**
 * 盘点计划返回VO
 */
public class CheckDeliveryVO extends TCheckDelivery {

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 规格型号
     */
    @Excel(name = "规格型号")
    private String specifications;

    /**
     * 单位名称
     */
    @Excel(name = "单位")
    private String unitName;

    /**
     * 包装方式名称
     */
    @Excel(name = "包装方式")
    private String packUnitName;

    /**
     * 数量
     */
    @Excel(name = "在库数量")
    private Long libraryCount;

    /**
     * 可用数量
     */
    @Excel(name = "可用数量")
    private Long availableCount;

    /**
     * 区域名称
     */
//    @Excel(name = "区域")
    private String areaName;

    /**
     * 库区名称
     */
//    @Excel(name = "库区")
    private String reservoirName;

    /**
     * 库区状态
     */
//    @Excel(name = "库区状态",readConverterExp="0=启用,1=禁用")
    private String reservoirStatus;

    /**
     * 制单人
     */
    @Excel(name = "制单人")
    private String createBy;

    /**
     * 制单日期
     */
    @Excel(name = "制单日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date producedDate;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 规格（单位标识）
     */
    private Long unitId;

    /**
     * 物料包装Id（主数据管理--单位）
     */
    private Long packUnitId;

    /**
     * 区域
     */
    private Long areaId;

    /**
     * 库区
     */
    private Long reservoirId;

    /**
     * 库位标识
     */
    private Long locationId;

    /**
     * 库位编码
     */
    private String locationCode;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 托盘标识
     */
    private Long trayId;

    /**
     * 托盘编码
     */
    private String trayCode;

    /**
     * 是否冻结（0：解冻，1：冻结）
     */
    private String isFreeze;

    /**
     * 库存编号
     */
    private String code;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 状态（0：不可用 1：不可用）
     */
    private String status;

    /**
     * 不可用的来源（1.手动冻结2.盘点计划 3.出库计划）
     */
    private String originType;

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

    public String getPackUnitName() {
        return packUnitName;
    }

    public void setPackUnitName(String packUnitName) {
        this.packUnitName = packUnitName;
    }

    public Long getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Long libraryCount) {
        this.libraryCount = libraryCount;
    }

    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getReservoirStatus() {
        return reservoirStatus;
    }

    public void setReservoirStatus(String reservoirStatus) {
        this.reservoirStatus = reservoirStatus;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    @Override
    public Long getMaterialId() {
        return materialId;
    }

    @Override
    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getPackUnitId() {
        return packUnitId;
    }

    public void setPackUnitId(Long packUnitId) {
        this.packUnitId = packUnitId;
    }

    @Override
    public Long getAreaId() {
        return areaId;
    }

    @Override
    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    @Override
    public Long getReservoirId() {
        return reservoirId;
    }

    @Override
    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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

    public String getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(String isFreeze) {
        this.isFreeze = isFreeze;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }
}
