package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 库位对象 t_location
 *
 * @author wxr
 * @date 2023-05-05
 */
@TableName("t_location")
public class TLocation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    @Excel(name = "名称", sort = 1)
    private String name;

    /**
     * 区域标识
     */
    private Long areaId;

    /**
     * 库区标识
     */
    private Long reservoirId;

    /**
     * 物料类别
     */
    private Long categoryId;

    /**
     * 存放物料类别
     */
    private Long depositCategoryId;

    /**
     * 包装单位
     */
    private Long unitId;

    /**
     * 是否允许物料混放
     */
    @Excel(name = "是否混物料存放", readConverterExp = "0=否,1=是", sort = 10)
    private String sameMaterialFlag;

    /**
     * 是否允许混批次
     */
    @Excel(name = "是否混批次", readConverterExp = "0=否,1=是", sort = 11)
    private String sameBatchFlag;

    /**
     * 存放数量上限
     */
    @Excel(name = "存放数量上限")
    private Long upperLimit;

    /**
     * 排
     */
    @Excel(name = "排", sort = 12)
    private Long locationRow;

    /**
     * 列
     */
    @Excel(name = "列", sort = 13)
    private Long locationColumn;

    /**
     * 层
     */
    @Excel(name = "层", sort = 14)
    private Long locationPlies;

    /**
     * 货位状态(1,无货,2,有货,3,标记出库,4,标记入库)
     */
    @Excel(name = "货位状态", readConverterExp = "0=禁用,1=无货,2=有货,3=标记出库,4=标记入库", sort = 4)
    private String goodsAllocationStatus;

    /**
     * 状态（0,禁用 1,启用）
     */
    @Excel(name = "状态", readConverterExp = "0=,禁用,1=启用", sort = 2)
    private String status;

    /**
     * 托盘编号
     */
    private String palletNum;
    /**
     * 库位类型
     */
    private String locationType;
    /**
     * 楼层（1一楼 2二楼）
     */
    private String floorType;
    /** 货架id */
    private Long goodShelfId;

    /** 一楼托盘编码，用于wcs通信使用 **/
    private String palletNodeId;

    /**
     * 双伸位类型（1伸位2伸位）
     */
    private Integer extentionType;
    /**
     * 双伸位一伸位id
     */
    private String extentionFristId;

    /**
     * erp库位编码
     */
    private String erpCode;

    public String getPalletNum() {
        return palletNum;
    }

    public void setPalletNum(String palletNum) {
        this.palletNum = palletNum;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setDepositCategoryId(Long depositCategoryId) {
        this.depositCategoryId = depositCategoryId;
    }

    public Long getDepositCategoryId() {
        return depositCategoryId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setSameMaterialFlag(String sameMaterialFlag) {
        this.sameMaterialFlag = sameMaterialFlag;
    }

    public String getSameMaterialFlag() {
        return sameMaterialFlag;
    }

    public void setSameBatchFlag(String sameBatchFlag) {
        this.sameBatchFlag = sameBatchFlag;
    }

    public String getSameBatchFlag() {
        return sameBatchFlag;
    }

    public void setUpperLimit(Long upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Long getUpperLimit() {
        return upperLimit;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationPlies(Long locationPlies) {
        this.locationPlies = locationPlies;
    }

    public Long getLocationPlies() {
        return locationPlies;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public Long getGoodShelfId() {
        return goodShelfId;
    }

    public void setGoodShelfId(Long goodShelfId) {
        this.goodShelfId = goodShelfId;
    }

    public String getPalletNodeId() {
        return palletNodeId;
    }

    public void setPalletNodeId(String palletNodeId) {
        this.palletNodeId = palletNodeId;
    }

    public Integer getExtentionType() {
        return extentionType;
    }

    public void setExtentionType(Integer extentionType) {
        this.extentionType = extentionType;
    }

    public String getExtentionFristId() {
        return extentionFristId;
    }

    public void setExtentionFristId(String extentionFristId) {
        this.extentionFristId = extentionFristId;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }


    @Override
    public String toString() {
        return "TLocation{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", areaId=" + areaId +
                ", reservoirId=" + reservoirId +
                ", categoryId=" + categoryId +
                ", depositCategoryId=" + depositCategoryId +
                ", unitId=" + unitId +
                ", sameMaterialFlag='" + sameMaterialFlag + '\'' +
                ", sameBatchFlag='" + sameBatchFlag + '\'' +
                ", upperLimit=" + upperLimit +
                ", locationRow=" + locationRow +
                ", locationColumn=" + locationColumn +
                ", locationPlies=" + locationPlies +
                ", goodsAllocationStatus='" + goodsAllocationStatus + '\'' +
                ", status='" + status + '\'' +
                ", palletNum='" + palletNum + '\'' +
                ", locationType='" + locationType + '\'' +
                ", floorType='" + floorType + '\'' +
                ", goodShelfId=" + goodShelfId +
                ", palletNodeId='" + palletNodeId + '\'' +
                ", extentionType=" + extentionType +
                ", extentionFristId='" + extentionFristId + '\'' +
                ", erpCode='" + erpCode + '\'' +
                '}';
    }
}
