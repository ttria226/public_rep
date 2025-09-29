package com.xsrw.wms.stock.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 库存交易返回参数类
 */
public class StockDealVO {

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
     * 批次号
     */
    @Excel(name = "批次号", sort = 4)
    private String batchCode;


    /**
     * 类型  1入库 2出库
     */
    @Excel(name = "类型", sort = 5,readConverterExp = "1=入库,2=出库,3=盘点,4=回库,5=移库")
    private String type;

    /**
     * 操作前数量
     */
    @Excel(name = "操作前数量", sort = 6)
    private Integer beforeCount;

    /**
     * 操作后数量
     */
    @Excel(name = "操作后数量", sort = 7)
    private Integer currentCount;
    /**
     * 变动数量
     **/
    @Excel(name = "变动数量", sort = 8)
    private Integer changeCount;

    /**
     * 创建时间
     */
    @Excel(name = "日期", sort = 8,dateFormat="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 规格型号
     */
    private String specifications;

    /**
     * 库位名称
     */
    private String locationName;

    public Integer getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(Integer beforeCount) {
        this.beforeCount = beforeCount;
    }

    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    public Integer getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(Integer changeCount) {
        this.changeCount = changeCount;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
