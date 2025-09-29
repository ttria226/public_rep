package com.xsrw.wms.loan.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/9 18:01
 */
public class DLoanReturnRecordVO extends DLoanReturnRecord {
    /**
     * 设备编号
     */
    @Excel(name = "设备编号", sort = 1)
    private String equipmentCode;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", sort = 1)
    private String equipmentName;

    /**
     * 可用数量
     */
    @Excel(name = "可用数量", sort = 3)
    private Long availableCount;

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

    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
    }
}
