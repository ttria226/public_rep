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
}
