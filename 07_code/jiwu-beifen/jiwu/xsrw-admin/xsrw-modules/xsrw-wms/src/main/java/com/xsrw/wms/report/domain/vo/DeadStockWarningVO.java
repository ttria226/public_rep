package com.xsrw.wms.report.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 出入库流水报表返回参数类
 */
public class DeadStockWarningVO {

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 1)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;

    /**
     * 物料描述
     */
    @Excel(name = "物料描述", sort = 3)
    private String description;

    /**
     * 物料类型
     */
    @Excel(name = "物料类型", sort = 4)
    private String categoryName;

    /**
     * 数量
     */
    @Excel(name = "数量", sort = 5)
    private Integer count;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位", sort = 6)
    private String unitName;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 7)
    private String batchCode;



    /**
     * 呆滞时间
     */
    @Excel(name = "呆滞时间", sort = 8,dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date deadTime;

    /**
     * 呆滞期（天）
     */
    @Excel(name = "呆滞期（天）", sort = 9)
    private Integer deadDayCount;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public Integer getDeadDayCount() {
        return deadDayCount;
    }

    public void setDeadDayCount(Integer deadDayCount) {
        this.deadDayCount = deadDayCount;
    }
}
