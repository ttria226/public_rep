package com.xsrw.wms.loan.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/12 11:20
 */
public class DLoanReturnExcelVO {
    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    @Excel(name = "设备编号")
    private String equipmentCode;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String equipmentName;

    /**
     * 借出数量
     */
    @Excel(name = "借出数量")
    private Long loanCount;

    /**
     * 还入数量
     */
    @Excel(name = "还入数量")
    private Long returnCount;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 状态
     */
    @Excel(name = "状态",readConverterExp = "1=待还入,2=部分还入,3=已还入")
    private String status;

    /**
     * 还入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "还入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

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

    public Long getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(Long loanCount) {
        this.loanCount = loanCount;
    }

    public Long getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
}
