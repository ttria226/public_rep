package com.xsrw.wms.api.domain.dto;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/29 14:44
 */
public class TStockMoveApiDTO {

    /**
     * 库位编码
     */
    private String locationCode;

    /**
     * 库存id
     */
    private Long stockId;

    /**
     * rfid
     */
    private String rfid;

    /**
     * 物料信息
     */
    private List<TAdvanceDeliveryApiDTO> materialList;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public List<TAdvanceDeliveryApiDTO> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<TAdvanceDeliveryApiDTO> materialList) {
        this.materialList = materialList;
    }

}
