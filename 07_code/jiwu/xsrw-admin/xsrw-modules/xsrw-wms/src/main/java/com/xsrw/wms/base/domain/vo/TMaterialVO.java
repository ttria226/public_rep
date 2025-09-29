package com.xsrw.wms.base.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TMaterial;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 15:52
 */
public class TMaterialVO extends TMaterial {

    /**
     * 单位名称
     */
    @Excel(name = "计量单位", sort = 2)
    private String unitName;

    /**
     * 往来单位名称
     */
    @Excel(name = "供应商", type = Excel.Type.EXPORT, sort = 2)
    private String contactsUnitName;

    /**
     * 物料类别名称
     */
    @Excel(name = "物料类别", sort = 2)
    private String categoryName;

    /**
     * 批次属性名称
     */
//    @Excel(name = "批次属性")
    private String batchAttrName;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 预计数量
     */
    private Long predictCount;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getContactsUnitName() {
        return contactsUnitName;
    }

    public void setContactsUnitName(String contactsUnitName) {
        this.contactsUnitName = contactsUnitName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBatchAttrName() {
        return batchAttrName;
    }

    public void setBatchAttrName(String batchAttrName) {
        this.batchAttrName = batchAttrName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }
}
