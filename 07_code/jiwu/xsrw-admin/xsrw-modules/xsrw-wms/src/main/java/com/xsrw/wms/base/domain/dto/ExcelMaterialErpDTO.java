package com.xsrw.wms.base.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * @author wxr
 * @date 2023/10/1618:05
 */
public class ExcelMaterialErpDTO {


    /**
     * 主键
     */
    private Long id;

    @Excel(name = "物料号")
    private String code;

    @Excel(name = "物料描述")
    private String name;

    @Excel(name = "计量单位")
    private String unitName;

    /** 单位 */
    private Long unitId;
    /**
     * 往来单位名称
     */
//    @Excel(name = "工厂")
    private Long contactsUnitId;

    @Excel(name = "物料组")
    private Long categoryId;

    @Excel(name = "物料组描述")
    private String categoryName;

    @Excel(name = "创建人描述")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间")
    private Date createTime;

    @Excel(name = "修改人描述")
    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "修改时间")
    private Date updateTime;

    @Excel(name = "物料单价")
    private Double unitPrice;

//    @Excel(name = "物料描述")
    private String description;


    /** 批次属性 */
    private Long batchAttrId;

    /** 是否启用批次 */
//    @Excel(name = "是否启用批次",readConverterExp = "1=是,0=否")
    private String batchFlag;

    /** 检验方式 */
//    @Excel(name = "检验方式", readConverterExp = "1=免检,2=抽检,3=全检")
    private String inspectionMethod;

    /** 规格型号 */
    private String specifications;

    /** 是否启用保质期管理 */
    private String expirationFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getContactsUnitId() {
        return contactsUnitId;
    }

    public void setContactsUnitId(Long contactsUnitId) {
        this.contactsUnitId = contactsUnitId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBatchAttrId() {
        return batchAttrId;
    }

    public void setBatchAttrId(Long batchAttrId) {
        this.batchAttrId = batchAttrId;
    }

    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }

    public String getInspectionMethod() {
        return inspectionMethod;
    }

    public void setInspectionMethod(String inspectionMethod) {
        this.inspectionMethod = inspectionMethod;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getExpirationFlag() {
        return expirationFlag;
    }

    public void setExpirationFlag(String expirationFlag) {
        this.expirationFlag = expirationFlag;
    }
}
