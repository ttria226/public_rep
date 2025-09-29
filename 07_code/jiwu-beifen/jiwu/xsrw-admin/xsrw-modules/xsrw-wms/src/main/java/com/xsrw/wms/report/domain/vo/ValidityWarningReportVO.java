package com.xsrw.wms.report.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * @author wangxueru
 * @description 库存有效期预警
 * @date 2023/6/14 16:01
 */
public class ValidityWarningReportVO {

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
     * 物料描述
     */
    @Excel(name = "物料描述")
    private String description;


    /**
     * 物料类别名称
     */
    @Excel(name = "物料类别")
    private String categoryName;

    /**
     * 单位名称
     */
    @Excel(name = "计量单位")
    private String unitName;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", dateFormat = "yyyy-MM-dd")
    private Date producedDate;
    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "失效日期", dateFormat = "yyyy-MM-dd")
    private Date expiryDate;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
