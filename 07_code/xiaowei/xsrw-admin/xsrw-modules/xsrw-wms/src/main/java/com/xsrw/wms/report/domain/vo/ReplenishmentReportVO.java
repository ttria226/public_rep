package com.xsrw.wms.report.domain.vo;

import com.xsrw.common.core.annotation.Excel;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/14 16:01
 */
public class ReplenishmentReportVO {

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
     * 单位名称
     */
    @Excel(name = "计量单位")
    private String unitName;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long libraryCount;

    /**
     * 库存下限
     **/
    @Excel(name = "预警下限")
    private Long stockMin;

    /**
     * 补货数量
     */
    @Excel(name = "补货数量")
    private Long replenishmentCount;

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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Long libraryCount) {
        this.libraryCount = libraryCount;
    }

    public Long getStockMin() {
        return stockMin;
    }

    public void setStockMin(Long stockMin) {
        this.stockMin = stockMin;
    }

    public Long getReplenishmentCount() {
        return replenishmentCount;
    }

    public void setReplenishmentCount(Long replenishmentCount) {
        this.replenishmentCount = replenishmentCount;
    }
}
