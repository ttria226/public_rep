package com.xsrw.wms.stock.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TStockMain;

/**
 * 库存主对象 t_stock_main
 *
 */
public class StockMainVo extends TStockMain {

    /** 物料编码  */
    @Excel(name = "物料编码" ,sort = 1)
    private String materialCode;

    /** 物料名称  */
    @Excel(name = "物料名称" ,sort = 2)
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号" ,sort = 3)
    private String specifications;

    /** 单位名称 */
    @Excel(name = "单位名称" ,sort = 4)
    private String unitName;

    /** 往来单位名称 */
    private String contactsUnitName;

    /** 物料类别名称 */
    @Excel(name = "物料类别" ,sort = 5)
    private String categoryName;

    /** 批次属性名称 */
    private String batchAttrName;

    /** 库存上限 **/
    @Excel(name = "库存上限" ,sort = 9)
    private Long stockMax;

    /** 库存下限 **/
    @Excel(name = "库存下限" ,sort = 8)
    private Long stockMin;

    /** 低于下限 **/
    @Excel(name = "低于下限" ,sort = 10)
    private String belowPercentage;

    /** 超过上限 **/
    @Excel(name = "超过上限" ,sort = 11)
    private String excessPercentage;
    /** 包装方式名称 */
//    @Excel(name = "包装方式",sort = 6)
    private String packUnitName;

    /**
     * 供应商
     */
    private Long contactsUnitId;

    public Long getContactsUnitId() {
        return contactsUnitId;
    }

    public void setContactsUnitId(Long contactsUnitId) {
        this.contactsUnitId = contactsUnitId;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public String getUnitName()
    {
        return unitName;
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

    public String getBelowPercentage() {
        return belowPercentage;
    }

    public void setBelowPercentage(String belowPercentage) {
        this.belowPercentage = belowPercentage;
    }

    public String getExcessPercentage() {
        return excessPercentage;
    }

    public void setExcessPercentage(String excessPercentage) {
        this.excessPercentage = excessPercentage;
    }

    public void setPackUnitName(String packUnitName)
    {
        this.packUnitName = packUnitName;
    }

    public String getPackUnitName()
    {
        return packUnitName;
    }
}
