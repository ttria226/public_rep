package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * @Description: 库存物资月报表VO
 * @Author: lyx
 * @Date: 2023/5/17
 */
public class TMaterialDetailMonthlyCountVo extends BaseEntity {

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
     * 批次号
     */
    @Excel(name = "批次号",sort = 3)
    private String batchCode;

    /**
     * 单位名称
     */
    @Excel(name = "计量单位",sort = 4)
    private String unitName;

    /**
     * 单价
     */
    @Excel(name = "单价",sort = 5)
    private Double unitPrice;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量",sort = 6)
    private Long currentCount;

    /**
     * 库存重量（kg）
     */
    @Excel(name = "库存重量（kg）",sort = 7)
    private Double totalWeight;

    /**
     * 库存总金额
     */
    @Excel(name = "库存总金额",sort = 8)
    private Double totalPrice;

    /**
     * 月份
     */
    @Excel(name = "月份",sort = 9)
    private String monthly;

    /**
     * 入库时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "入库时间",sort = 10,width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss" )
    private Date createTime;

    /**
     * 库龄（天）
     */
    @Excel(name = "库龄（天）",sort = 11)
    private String stockAge;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStockAge() {
        return stockAge;
    }

    public void setStockAge(String stockAge) {
        this.stockAge = stockAge;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TMaterialDetailMonthlyCountVo{" +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitPrice=" + unitPrice +
                ", currentCount=" + currentCount +
                ", totalWeight=" + totalWeight +
                ", totalPrice=" + totalPrice +
                ", monthly='" + monthly + '\'' +
                ", createTime=" + createTime +
                ", stockAge=" + stockAge +
                '}';
    }
}
