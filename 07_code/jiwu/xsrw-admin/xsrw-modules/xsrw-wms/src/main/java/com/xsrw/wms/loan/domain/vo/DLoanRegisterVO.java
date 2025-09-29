package com.xsrw.wms.loan.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.loan.domain.DLoanRegister;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/9 17:02
 */
public class DLoanRegisterVO extends DLoanRegister {

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
     * 创建者
     */
    @Excel(name = "制单人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "制单时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
