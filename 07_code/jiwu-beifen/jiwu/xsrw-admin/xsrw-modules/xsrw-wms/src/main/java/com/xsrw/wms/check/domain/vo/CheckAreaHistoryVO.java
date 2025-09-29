package com.xsrw.wms.check.domain.vo;

import com.xsrw.wms.check.domain.TCheckAreaHistory;

/**
 * @Description:
 * @Author XMING
 * @Date 2022-07-23
 */
public class CheckAreaHistoryVO extends TCheckAreaHistory {


    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 区域名称
     */
    private String areaName;

    /** 盘差 */
    private Long checkDifferenceCount;


    public Long getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    public void setCheckDifferenceCount(Long checkDifferenceCount) {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
