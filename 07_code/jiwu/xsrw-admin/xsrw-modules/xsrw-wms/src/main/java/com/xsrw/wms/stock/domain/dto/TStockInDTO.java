package com.xsrw.wms.stock.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/29 15:11
 */
public class TStockInDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 库存id
     */
    private Long stockId;

    /**
     * 任务类型（入库、出库、盘点、移库）
     */
    private String type;

    /**
     * 原单标识
     */
    private Long originId;

    /**
     * 原单号
     */
    private String originCode;
    /**
     * 库位标识
     */
    private Long locationId;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 托盘id
     */
    private Long trayId;
    /**
     * 物料标识
     */
    private Long materialId;
    /**
     * 批次号
     */
    private String batchCode;
    /**
     * 操作后当前数量
     */
    private BigDecimal count;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date producedDate;

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }
}
