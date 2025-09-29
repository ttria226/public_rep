package com.xsrw.wms.stock.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TStockRecheck;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/21 11:09
 */
public class TStockRecheckVO extends TStockRecheck {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 1)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 1)
    private String materialName;

    /**
     * 单位名称
     */
    @Excel(name = "计量单位", sort = 1)
    private String unitName;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 1)
    private String batchCode;

    /**
     * 库区名称
     */
    @Excel(name = "库区", sort = 1)
    private String reservoirName;

    /**
     * 库位id
     */
    private Long locationId;

    /**
     * 库位
     */
    @Excel(name = "库位", sort = 1)
    private String locationName;

    /**
     * 库存
     */
    @Excel(name = "库存", sort = 1)
    private Long count;
    /**
     * 复检结果
     */
    @Excel(name = "复检结果")
    private String recheckResult;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getRecheckResult() {
        return recheckResult;
    }

    public void setRecheckResult(String recheckResult) {
        this.recheckResult = recheckResult;
    }
}
