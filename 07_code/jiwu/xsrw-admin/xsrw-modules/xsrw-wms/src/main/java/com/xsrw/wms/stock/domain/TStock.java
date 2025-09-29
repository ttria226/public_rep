package com.xsrw.wms.stock.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 库存详情对象 t_stock
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_stock")
public class TStock extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 区域ID
     */
    @Excel(name = "区域ID")
    private Long areaId;

    /**
     * 库位标识
     */
    @Excel(name = "库位标识")
    private Long locationId;

        /**
         * 物料标识
         */
        @Excel(name = "物料标识")
        private Long materialId;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    /**
     * 规格（单位标识）
     */
    @Excel(name = "规格", readConverterExp = "单=位标识")
    private Long unitId;

    /**
     * 在库数量
     */
    @Excel(name = "在库数量")
    private BigDecimal count;

    /**
     * 可用数量
     */
    @Excel(name = "可用数量")
    private BigDecimal availableCount;

    /**
     * 状态（0可用 1不可用）
     */
    @Excel(name = "状态", readConverterExp = "0=可用,1=不可用")
    private String status;

    /**
     * 是否冻结（0解冻，1冻结）
     */
    @Excel(name = "是否冻结", readConverterExp = "0=解冻，1冻结")
    private String isFreeze;

    /**
     * 不可用的来源（1.手动冻结2.盘点计划 3.出库计划）
     */
    @Excel(name = "不可用的来源", readConverterExp = "1=手动冻结,2=盘点计划,3=出库计划")
    private String originType;

     @TableField(exist = false)
    private String code;

    /**
     * 托盘id
     */
    @Excel(name = "托盘id")
    private Long trayId;

    @TableField(exist = false)
    private Long trayCategory;
    @TableField(exist = false)
    private String locationType;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date producedDate;


    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 平库位置信息
     */
    @Excel(name = "平库位置信息")
    private String position;

    @TableField(exist = false)
    private String rfid;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTrayCategory() {
        return trayCategory;
    }

    public void setTrayCategory(Long trayCategory) {
        this.trayCategory = trayCategory;
    }
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationType() {
        return locationType;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public BigDecimal getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(BigDecimal availableCount) {
        this.availableCount = availableCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setIsFreeze(String isFreeze) {
        this.isFreeze = isFreeze;
    }

    public String getIsFreeze() {
        return isFreeze;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public String getOriginType() {
        return originType;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }


    @Override
    public String toString() {
        return "TStock{" +
                "id=" + id +
                ", areaId=" + areaId +
                ", locationId=" + locationId +
                ", materialId=" + materialId +
                ", batchCode='" + batchCode + '\'' +
                ", unitId=" + unitId +
                ", count=" + count +
                ", availableCount=" + availableCount +
                ", status='" + status + '\'' +
                ", isFreeze='" + isFreeze + '\'' +
                ", originType='" + originType + '\'' +
                ", code='" + code + '\'' +
                ", trayId=" + trayId +
                ", trayCategory=" + trayCategory +
                ", locationType='" + locationType + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", producedDate=" + producedDate +
                ", expireDate=" + expireDate +
                ", position='" + position + '\'' +
                '}';
    }
}
