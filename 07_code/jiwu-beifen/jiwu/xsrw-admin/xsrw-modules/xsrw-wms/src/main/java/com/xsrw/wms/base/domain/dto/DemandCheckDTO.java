package com.xsrw.wms.base.domain.dto;

/**
 * @Description: 需盘点 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class DemandCheckDTO{

    /**
     * 区域Id
     */
    private Long areaId;

    /**
     * 库区id
     **/
    private Long reservoirId;

    /**
     * 库位id
     **/
    private Long locationId;

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
