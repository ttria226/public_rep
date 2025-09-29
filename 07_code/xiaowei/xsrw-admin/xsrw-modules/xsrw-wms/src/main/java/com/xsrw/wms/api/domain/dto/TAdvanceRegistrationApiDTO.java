package com.xsrw.wms.api.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/17 14:02
 */
public class TAdvanceRegistrationApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 载具编号
     */
    private String trayCode;
    /**
     * 载具状态
     */
    private String trayStatus;
    /**
     * 库位id
     */
    private Long locationId;

    /**
     * 物料列表
     */
    private List<TAdvanceDeliveryApiDTO> materialList;

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getTrayStatus() {
        return trayStatus;
    }

    public void setTrayStatus(String trayStatus) {
        this.trayStatus = trayStatus;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public List<TAdvanceDeliveryApiDTO> getMaterialList() {
        return materialList;
    }

    public void setMaterialList(List<TAdvanceDeliveryApiDTO> materialList) {
        this.materialList = materialList;
    }
}
