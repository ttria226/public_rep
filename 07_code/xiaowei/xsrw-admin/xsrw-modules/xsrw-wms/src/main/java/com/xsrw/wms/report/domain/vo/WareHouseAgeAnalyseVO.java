package com.xsrw.wms.report.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 库龄分析返回参数类
 */
public class WareHouseAgeAnalyseVO {

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
     * 单价
     */
    @Excel(name = "单价", sort = 5)
    private double unitPrice;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量", sort = 6)
    private Integer count;

    /**
     * 库存重量
     */
    @Excel(name = "库存重量（kg）", sort = 7)
    private double weight;

    /**
     * 库存总金额
     */
    @Excel(name = "库存总金额", sort = 8)
    private double totalPrice;


    /**
     * 入库时间
     */
    @Excel(name = "入库时间", sort = 9,dateFormat = "yyyy/MM/dd")
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date createTime;


    /**
     * 库龄
     */
    @Excel(name = "库龄（天）", sort = 10)
    private Integer stockAge;

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

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStockAge() {
        return stockAge;
    }

    public void setStockAge(Integer stockAge) {
        this.stockAge = stockAge;
    }
}
