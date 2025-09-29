package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TMaterialDetail;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/25 17:30
 */
public class TMaterialDetailVO extends TMaterialDetail {

    /**
     * 入库单id
     */
    private Long advanceDeliveryId;
    /**
     * 单位
     */
    private String unitName;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 描述
     */
    private String description;

    /**
     * 生产日期
     */
    private String producedDate;

    /** 规格型号 */
    private String specifications;

    /** 区载名称 */
    private String areaName;

    /** 区载id */
    private Long areaId;
    /** 区载名称 */
    private Long reservoirId;
    /** 库区名称 */
    private String reservoirName;

    private String libraryCount;

    private String lastCheckTime;

    public Long getAdvanceDeliveryId() {
        return advanceDeliveryId;
    }

    public void setAdvanceDeliveryId(Long advanceDeliveryId) {
        this.advanceDeliveryId = advanceDeliveryId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(String producedDate) {
        this.producedDate = producedDate;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

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
    public String getReservoirName() {
        return reservoirName;
    }
    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }
    public String getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(String libraryCount) {
        this.libraryCount = libraryCount;
    }
    public String getLastCheckTime(){return lastCheckTime; }
    public void  setLastCheckTime(String lastCheckTime){ this.lastCheckTime = lastCheckTime; }
}
