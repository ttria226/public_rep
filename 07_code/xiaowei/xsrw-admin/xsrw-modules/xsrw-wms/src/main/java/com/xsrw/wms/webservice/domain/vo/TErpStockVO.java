package com.xsrw.wms.webservice.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.webservice.domain.TErpStock;

import java.math.BigDecimal;

/**
 * @author 863Soft
 * @date 2024/8/28
 * @description <p>备注：</p>
 */
public class TErpStockVO extends TErpStock {

    /**
     * 仓位名称
     */
    @Excel(name = "仓位")
    private String locationName;

    /**
     * 库位存量
     */
    private BigDecimal stockCount;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public BigDecimal getStockCount() {
        return stockCount;
    }

    public void setStockCount(BigDecimal stockCount) {
        this.stockCount = stockCount;
    }
}
