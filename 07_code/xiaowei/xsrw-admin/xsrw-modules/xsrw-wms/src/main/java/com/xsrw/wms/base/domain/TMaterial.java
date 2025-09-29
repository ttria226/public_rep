package com.xsrw.wms.base.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 物料对象 t_material
 *
 * @author wxr
 * @date 2023-05-05
 */
@TableName("t_material")
public class TMaterial extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    @Excel(name = "物料编码", sort = 1)
    private String code;

    /**
     * 名称
     */
    @Excel(name = "物料名称", sort = 1)
    private String name;

    /**
     * 规格型号
     */
    @Excel(name = "规格型号", sort = 1)
    private String specifications;

    /**
     * 单位
     */
    private Long unitId;

    /**
     * 供应商
     */
    private Long contactsUnitId;

    /**
     * 启用批次
     */
//    @Excel(name = "是否启用批次", readConverterExp = "1=是,0=否")
    private String batchFlag;

    /**
     * 批次属性
     */
    private Long batchAttrId;

    /**
     * 基本单位
     */
    private String baseUnitId;

    /**
     * 是否物料混合
     */
//    @Excel(name = "是否混物料", readConverterExp = "1=是,0=否")
    private String sameMaterialFlag;

    /**
     * 是否批次混合
     */
//    @Excel(name = "是否混批次", readConverterExp = "1=是,0=否")
    private String sameBatchFlag;

    /**
     * 物料类别
     */
    private Long categoryId;

    /**
     * 检验方式(1:免检 2抽检 3全检)
     */
//    @Excel(name = "检验方式", readConverterExp = "1=免检,2=抽检,3=全检", type = Excel.Type.EXPORT)
    private String inspectionMethod;

    /**
     * 启用保质期管理
     */
//    @Excel(name = "是否启用有效期管理", readConverterExp = "0=否,1=是", type = Excel.Type.EXPORT)
    private String expirationFlag;

    /**
     * 库存上限
     */
    @Excel(name = "库存上限", sort = 6)
    private Long stockMax;

    /**
     * 库存下限
     */
    @Excel(name = "库存下限", sort = 6)
    private Long stockMin;


    /**
     * 库存保险额
     */
    private Long stockInsure;

    /**
     * 保质期天数
     */
//    @Excel(name = "保质期天数")
    private Long expirationDate;

    /**
     * 毛重
     */
    @Excel(name = "毛重(KG)", type = Excel.Type.EXPORT, sort = 4)
    private Double roughWeight;

    /**
     * 净重（暂废）
     */
    private Double netWeight;

    /**
     * 财务凭证号
     */
    @Excel(name = "财务凭证号", sort = 7)
    private String financeVoucherNo;

    /**
     * 单价
     */
    @Excel(name = "单价", sort = 3)
    private Double unitPrice;

    /**
     * 描述
     */
    @Excel(name = "物料描述", sort = 7)
    private String description;

    /**
     * 长度（cm）
     */
    @Excel(name = "长度（cm）", sort = 5)
    private Long materialLength;

    /**
     * 高度（cm）
     */
    @Excel(name = "高度（cm）", sort = 5)
    private Long materialHeight;

    /**
     * 宽度（cm）
     */
    @Excel(name = "宽度（cm）", sort = 5)
    private Long materialWidth;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 50, sort = 8)
    private String remark;

    /**
     * 图片
     */
    private String img;
    /**
     * 重点物料标识（0否1是）
     */
    private String importantStatus;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
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
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImportantStatus() {
        return importantStatus;
    }

    public void setImportantStatus(String importantStatus) {
        this.importantStatus = importantStatus;
    }

    public Long getStockInsure() {
        return stockInsure;
    }

    public void setStockInsure(Long stockInsure) {
        this.stockInsure = stockInsure;
    }


    @Override
    public String toString() {
        return "TMaterial{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", specifications='" + specifications + '\'' +
                ", unitId=" + unitId +
                ", contactsUnitId=" + contactsUnitId +
                ", batchFlag='" + batchFlag + '\'' +
                ", batchAttrId=" + batchAttrId +
                ", baseUnitId='" + baseUnitId + '\'' +
                ", sameMaterialFlag='" + sameMaterialFlag + '\'' +
                ", sameBatchFlag='" + sameBatchFlag + '\'' +
                ", categoryId=" + categoryId +
                ", inspectionMethod='" + inspectionMethod + '\'' +
                ", expirationFlag='" + expirationFlag + '\'' +
                ", stockMax=" + stockMax +
                ", stockMin=" + stockMin +
                ", stockInsure=" + stockInsure +
                ", expirationDate=" + expirationDate +
                ", roughWeight=" + roughWeight +
                ", netWeight=" + netWeight +
                ", financeVoucherNo='" + financeVoucherNo + '\'' +
                ", unitPrice=" + unitPrice +
                ", description='" + description + '\'' +
                ", materialLength=" + materialLength +
                ", materialHeight=" + materialHeight +
                ", materialWidth=" + materialWidth +
                ", remark='" + remark + '\'' +
                ", img='" + img + '\'' +
                ", importantStatus='" + importantStatus + '\'' +
                '}';
    }
}
