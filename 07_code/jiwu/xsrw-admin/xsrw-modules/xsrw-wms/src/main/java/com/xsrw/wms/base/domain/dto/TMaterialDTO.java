package com.xsrw.wms.base.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.base.domain.TMaterial;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 15:42
 */
public class TMaterialDTO extends TMaterial {

    /**
     * 多个id,已，分割
     */
    private String ids;

    /**
     * 库存上限
     */
    private Long stockMax;

    /**
     * 库存下限
     */
    private Long stockMin;

    /**
     * 制单日期
     */
    private Date makeDate;

    /**
     * 库区
     */
    private Long reservoirId;


    /**
     * 库位id
     */
    private List<Long> locationIds;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getStockMax() {
        return stockMax;
    }

    public void setStockMax(Long stockMax) {
        this.stockMax = stockMax;
    }

    public Long getStockMin() {
        return stockMin;
    }

    public void setStockMin(Long stockMin) {
        this.stockMin = stockMin;
    }

    public Date getMakeDate() {
        return makeDate;
    }

    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public List<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Long> locationIds) {
        this.locationIds = locationIds;
    }
}
