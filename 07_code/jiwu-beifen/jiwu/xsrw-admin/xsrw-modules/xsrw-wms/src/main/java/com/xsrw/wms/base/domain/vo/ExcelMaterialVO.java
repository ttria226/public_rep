package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xsrw.common.core.annotation.Excel;

/**
 * 物料管理导出空白模板
 *
 */
public class ExcelMaterialVO {
    /** 所属组织名称 */
    private String orgName;

    /** 物料编码 */
//    @Excel(name = "物料编码")
    private String code;

    /** 主键 */
    private Long id;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String name;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specifications;

    /** 单位名称 */
    @Excel(name = "单位")
    @TableField(exist = false)
    private String unitName;

    /** 往来单位名称 */
    @TableField(exist = false)
    @Excel(name = "供应商")
    private String contactsUnitName;

    /** 物料类别名称 */
    @Excel(name = "物料类别")
    @TableField(exist = false)
    private String categoryName;

    /** 单位 */
    private Long unitId;

    /** 往来单位 */
    private Long contactsUnitId;

    /** 物料类别 */
    private Long categoryId;

    /** 是否启用批次 */
//    @Excel(name = "是否启用批次",readConverterExp = "1=是,0=否")
    private String batchFlag;

    /** 批次属性 */
    private Long batchAttrId;

    /** 基本单位 */
    private String baseUnitId;

    /** 批次属性名称 */
//    @Excel(name = "批次属性")
    private String batchAttrName;

    /** （托盘）是否允许混物料包装 */
//    @Excel(name = "是否混物料", readConverterExp = "1=是,0=否")
    private String sameMaterialFlag;

    /** （托盘）是否允许混批次包装 */
//    @Excel(name = "是否混批次", readConverterExp = "1=是,0=否")
    private String sameBatchFlag;

    /** 检验方式 */
//    @Excel(name = "检验方式", readConverterExp = "1=免检,2=抽检,3=全检")
    private String inspectionMethod;

    /** 是否启用保质期管理 */
//    @Excel(name = "是否启用有效期管理",readConverterExp = "0=否,1=是")
    private String expirationFlag;

    /** 保质期天数 */
//    @Excel(name = "有效期天数")
    private Integer expirationDate;


    /** 库存上限 */
    @Excel(name = "库存上限")
    private Long stockMax;

    /** 库存下限 */
    @Excel(name = "库存下限")
    private Long stockMin;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private Double unitPrice;
    /** 删除 */
    private String delFlag;
    /** 毛重 */
    @Excel(name = "重量(KG)")
    private Double roughWeight ;

    /** 净重 */
//    @Excel(name = "重量(KG)")
    private Double netWeight;
    /**
     * 长度（cm）
     */
    @Excel(name = "长度（cm）")
    private Long materialLength;

    /**
     * 高度（cm）
     */
    @Excel(name = "高度（cm）")
    private Long materialHeight;

    /**
     * 宽度（cm）
     */
    @Excel(name = "宽度（cm）")
    private Long materialWidth;

    @Excel(name = "财务凭证号")
    private String financeVoucherNo;

    @Excel(name = "物料描述")
    private String description;

    @Excel(name = "备注" , width = 20)
    private String remark;

    @Excel(name = "图片" )
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
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

    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }

    public Long getBatchAttrId() {
        return batchAttrId;
    }

    public void setBatchAttrId(Long batchAttrId) {
        this.batchAttrId = batchAttrId;
    }

    public String getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(String baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSameMaterialFlag() {
        return sameMaterialFlag;
    }

    public void setSameMaterialFlag(String sameMaterialFlag) {
        this.sameMaterialFlag = sameMaterialFlag;
    }

    public String getSameBatchFlag() {
        return sameBatchFlag;
    }

    public void setSameBatchFlag(String sameBatchFlag) {
        this.sameBatchFlag = sameBatchFlag;
    }

    public String getInspectionMethod() {
        return inspectionMethod;
    }

    public void setInspectionMethod(String inspectionMethod) {
        this.inspectionMethod = inspectionMethod;
    }

    public String getExpirationFlag() {
        return expirationFlag;
    }

    public void setExpirationFlag(String expirationFlag) {
        this.expirationFlag = expirationFlag;
    }

    public Integer getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Integer expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getStockMax() {
        return stockMax;
    }

    public void setStockMax(Long stockMax) {
        this.stockMax = stockMax;
    }

    public Long getStockMin() {
        return stockMin;
    }

    public void setStockMin(Long stockMin) {
        this.stockMin = stockMin;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
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

    public Double getRoughWeight() {
        return roughWeight;
    }

    public void setRoughWeight(Double roughWeight) {
        this.roughWeight = roughWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }
    public String getFinanceVoucherNo() {
        return financeVoucherNo;
    }

    public void setFinanceVoucherNo(String financeVoucherNo) {
        this.financeVoucherNo = financeVoucherNo;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getMaterialLength() {
        return materialLength;
    }

    public void setMaterialLength(Long materialLength) {
        this.materialLength = materialLength;
    }

    public Long getMaterialHeight() {
        return materialHeight;
    }

    public void setMaterialHeight(Long materialHeight) {
        this.materialHeight = materialHeight;
    }

    public Long getMaterialWidth() {
        return materialWidth;
    }

    public void setMaterialWidth(Long materialWidth) {
        this.materialWidth = materialWidth;
    }

    @Override
    public String toString() {
        return "MaterialVO{" +
            "orgName='" + orgName + '\'' +
            ", code='" + code + '\'' +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", specifications='" + specifications + '\'' +
            ", unitId=" + unitId +
            ", contactsUnitId=" + contactsUnitId +
            ", categoryId=" + categoryId +
            ", batchFlag='" + batchFlag + '\'' +
            ", batchAttrId=" + batchAttrId +
            ", baseUnitId='" + baseUnitId + '\'' +
            ", unitName='" + unitName + '\'' +
            ", sameMaterialFlag='" + sameMaterialFlag + '\'' +
            ", sameBatchFlag='" + sameBatchFlag + '\'' +
            ", inspectionMethod='" + inspectionMethod + '\'' +
            ", expirationFlag='" + expirationFlag + '\'' +
            ", expirationDate=" + expirationDate +
            ", stockMax=" + stockMax +
            ", stockMin=" + stockMin +
            ", delFlag='" + delFlag + '\'' +
            ", contactsUnitName='" + contactsUnitName + '\'' +
            ", categoryName='" + categoryName + '\'' +
            ", batchAttrName='" + batchAttrName + '\'' +
            ", roughWeight=" + roughWeight +
            ", netWeight=" + netWeight +
            ", remark='" + remark + '\'' +
            '}';
    }
}
