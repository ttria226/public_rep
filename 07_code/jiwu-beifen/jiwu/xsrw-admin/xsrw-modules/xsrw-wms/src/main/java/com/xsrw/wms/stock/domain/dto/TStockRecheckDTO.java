package com.xsrw.wms.stock.domain.dto;

import com.xsrw.wms.stock.domain.TStockRecheck;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/21 15:48
 */
public class TStockRecheckDTO extends TStockRecheck {

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;


    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 库区
     */
    private String reservoirId;

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

    public String getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(String reservoirId) {
        this.reservoirId = reservoirId;
    }
}
