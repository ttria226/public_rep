package com.xsrw.wms.stock.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockChangeLog;

/**
 * 库存对象 t_stock_change_log
 *
 */
public class StockChangeLogVo extends TStockChangeLog {
    /**
     * 物料标识
     */
    @Excel(name = "物料标识")
    private Long materialId;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String materialCode;
    /**
     * 库位标识名称
     */
    @Excel(name = "库位标识名称")
    private String locationName;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;
    /**
     * rfid
     */
    @Excel(name = "rfidHead")
    private String rfidHead;

    /**
     * 调整次数
     */
    @Excel(name = "调整次数")
    private Long tzcs;
    /**
     * 库位标识
     */
    @Excel(name = "库位标识")
    private Long locationId;
    /**
     * 当前库存数量
     */
    @Excel(name = "当前库存数量")
    private Long stockCount;
    public String getLocationName() {
        return locationName;
    }
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    public String getRfidHead() {
        return rfidHead;
    }

    public void setRfidHead(String rfidHead) {
        this.rfidHead = rfidHead;
    }
    public Long getStockCount() {
        return stockCount;
    }
    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }
    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getTzcs() {
        return tzcs;
    }

    public void setTzcs(Long tzcs) {
        this.tzcs = tzcs;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }
}
