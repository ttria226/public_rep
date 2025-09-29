package com.xsrw.wms.report.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 入库流水报表导出类
 */
public class InStatementExcelVO {

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
     * 批次号
     */
    @Excel(name = "批次号", sort = 3)
    private String batchCode;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位", sort = 4)
    private String unitName;

    /**
     * 所属客户
     */
    @Excel(name = "所属客户", sort = 5)
    private String contactsUnitName;

    /**
     * 类型  1入库 2出库
     */
    private String type;

    /**
     * 数量
     */
    @Excel(name = "入库数量", sort = 6)
    private int count;

    /**
     * 创建时间
     */
    @Excel(name = "入库时间", sort = 7,dateFormat="yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date createTime;

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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
