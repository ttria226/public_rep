package com.xsrw.wms.loan.domain.dto;

import com.xsrw.wms.loan.domain.DLoanRegister;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/9 17:04
 */
public class DLoanRegisterDTO extends DLoanRegister {

    /**
     * 设备编号
     */
    private String equipmentCode;
    /**
     * 设备名称
     */
    private String equipmentName;

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
