package com.xsrw.wms.base.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 需盘点列表返回参数类
 */
public class DemandCheckVO {
    /**
     * 库位id
     */
    private Integer id;

    /**
     * 库位
     */
    @Excel(name = "库位", sort = 1)
    private String locationName;
    /**
     * 货架
     */
    @Excel(name = "货架", sort = 2)
    private String goodShelfName;

    /**
     * 库区
     */
    @Excel(name = "库区", sort = 3)
    private String reservoirName;

    /**
     * 区域
     */
    @Excel(name = "区域", sort = 4)
    private String areaName;

    /**
     * 货位状态(1,无货,2,有货,3,标记出库,4,标记入库)
     */
    @Excel(name = "状态", sort = 5,readConverterExp = "1=无货,2=有货,3=标记出库,4=标记入库")
    private String goodsAllocationStatus;

    /**
     * 库存条数
     */
    @Excel(name = "库存条数", sort = 6)
    private Integer stockCount;

    /**
     * 操作人
     */
    @Excel(name = "操作人", sort = 7)
    private String updateBy;

    /**
     * 操作时间
     */
    @Excel(name = "操作时间", sort = 8,dateFormat = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date updateTime;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGoodShelfName() {
        return goodShelfName;
    }

    public void setGoodShelfName(String goodShelfName) {
        this.goodShelfName = goodShelfName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
