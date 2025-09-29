package com.xsrw.wms.stock.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存操作记录对象 t_stock_detail
 *
 * @author wxr
 * @date 2023-05-11
 */
@TableName("t_stock_detail")
public class TStockDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

   @TableField(exist = false)
    private String materialCode;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialName;

    /**
     * 物料规格
     */
    @TableField(exist = false)
    private String specifications;

    /**
     * 单位名称
     */
    @TableField(exist = false)
    private String unitName;
    /**
     * 库位标识
     */
    private Long locationId;

    /**
     * 物料标识
     */
    private Long materialId;


    /**
     * 操作前数量
     */
    private BigDecimal beforeCount;

    /**
     * 操作后当前数量
     */
    @Excel(name = "当前数量")
    private BigDecimal currentCount;

    /**
     * 任务类型（入库、出库、盘点、移库）
     */
    @Excel(name = "任务类型", readConverterExp = "入库、出库、盘点、移库")
    private String type;

    /**
     * 原单标识
     */
    private Long originId;

    /**
     * 原单号
     */
    private String originCode;

    /**
     * 状态（0不可用 1不可用）
     */
    private String status;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public BigDecimal getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(BigDecimal beforeCount) {
        this.beforeCount = beforeCount;
    }

    public BigDecimal getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(BigDecimal currentCount) {
        this.currentCount = currentCount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("locationId", getLocationId())
                .append("materialId", getMaterialId())
                .append("beforeCount", getBeforeCount())
                .append("currentCount", getCurrentCount())
                .append("type", getType())
                .append("originId", getOriginId())
                .append("originCode", getOriginCode())
                .append("status", getStatus())
                .append("batchCode", getBatchCode())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
