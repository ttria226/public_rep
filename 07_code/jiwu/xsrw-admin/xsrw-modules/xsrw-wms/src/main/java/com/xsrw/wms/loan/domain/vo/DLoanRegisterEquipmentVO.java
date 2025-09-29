package com.xsrw.wms.loan.domain.vo;

import com.xsrw.wms.equipment.domain.WmsEquipment;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/10 15:13
 */
public class DLoanRegisterEquipmentVO extends WmsEquipment {

    /**
     * 登记id
     */
    private Long registerId;

    /**
     * 可用数量
     */
    private Long availableCount;

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
    }
}
