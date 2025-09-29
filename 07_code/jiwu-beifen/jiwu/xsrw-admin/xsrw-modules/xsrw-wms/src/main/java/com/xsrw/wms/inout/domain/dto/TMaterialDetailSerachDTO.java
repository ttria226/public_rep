package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TMaterialDetail;

/**
 * @author wangxueru
 * @description
 * @date 2023/8/1 16:05
 */
public class TMaterialDetailSerachDTO extends TMaterialDetail {

    /**
     * 库位状态(空全部1无库位2有库位)
     */
    private Integer locationStatus;

    public Integer getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(Integer locationStatus) {
        this.locationStatus = locationStatus;
    }
}
