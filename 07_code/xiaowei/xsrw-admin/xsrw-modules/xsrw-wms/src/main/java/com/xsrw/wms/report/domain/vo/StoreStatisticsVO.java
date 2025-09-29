package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

/**
 * 库存统计返回参数类
 */
public class StoreStatisticsVO {

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
     * 计量单位
     */
    @Excel(name = "计量单位", sort = 3)
    private String unitName;

    /**
     * 物料类型
     */
    @Excel(name = "类别", sort = 4)
    private String categoryName;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 5)
    private String batchCode;



    /**
     * 库区名称
     **/
    @Excel(name = "库区", sort = 6)
    private String reservoirName;

    /**
     * 库位名称
     **/
    @Excel(name = "库位", sort = 7)
    private String locationName;

    /**
     * 库存数量
     */
    @Excel(name = "库存", sort = 8)
    private Integer count;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
