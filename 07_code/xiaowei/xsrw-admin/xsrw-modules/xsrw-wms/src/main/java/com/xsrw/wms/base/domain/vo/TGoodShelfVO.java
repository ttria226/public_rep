package com.xsrw.wms.base.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TGoodShelf;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/1 10:57
 */
public class TGoodShelfVO extends TGoodShelf {

    /**
     * 库区名称
     */
    @Excel(name = "库区")
    private String reservoirName;
    /**
     * 区域名称
     */
    @Excel(name = "区域")
    private String areaName;

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
