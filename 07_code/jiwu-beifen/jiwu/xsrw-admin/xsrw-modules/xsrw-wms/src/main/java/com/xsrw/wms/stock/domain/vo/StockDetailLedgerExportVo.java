package com.xsrw.wms.stock.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TStockDetail;

/**
 * @Description: 库存台账VO
 * @Author: lyx
 * @Date: 2023/5/17
 */
public class StockDetailLedgerExportVo extends TStockDetail {

    /**
     * 物料编号
     */
    @Excel(name = "物料编号",sort = 1)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",sort = 2)
    private String materialName;

    /**
     * 物料规格
     */
    @Excel(name = "物料规格",sort = 3)
    private String specifications;

    /**
     * 单位名称
     */
    @Excel(name = "单位名称",sort = 7)
    private String unitName;

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

    @Override
    public String toString() {
        return "StockDetailVo{" +
                "materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specifications='" + specifications + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
