package com.xsrw.wms.loan.domain.dto;

import com.xsrw.wms.loan.domain.DLoanReturnRecord;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/9 18:01
 */
public class DLoanReturnRecordDTO extends DLoanReturnRecord {
    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 导出类型（1借出2还入）
     */
    private String exportType;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
}
