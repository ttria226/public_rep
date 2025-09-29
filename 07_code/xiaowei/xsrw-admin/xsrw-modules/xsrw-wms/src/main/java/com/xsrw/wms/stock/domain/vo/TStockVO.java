package com.xsrw.wms.stock.domain.vo;

import com.xsrw.wms.stock.domain.TStock;

/**
 * @author 863Soft
 * @date 2024/9/2
 * @description <p>备注：</p>
 */
public class TStockVO extends TStock {

    /**
     * 库区id
     */
    private Long reservoirId;

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }
}
