package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

/**
 * 供应商质量统计报表返回参数类
 */
public class QualityReportVO {

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", sort = 1)
    private String contactsUnitName;

    /**
     * 联系人
     */
    @Excel(name = "联系人", sort = 2)
    private String contactsUnitContact;


    /**
     * 质检数量
     */
    @Excel(name = "质检数量", sort = 3)
    private Integer detectionCount;

    /**
     * 质检通过数量
     */
    @Excel(name = "质检通过数量", sort = 4)
    private Integer detectionPassCount;



    /**
     * 通过率
     */
    @Excel(name = "通过率", sort = 5)
    private String passRate;

    public String getContactsUnitName() {
        return contactsUnitName;
    }

    public void setContactsUnitName(String contactsUnitName) {
        this.contactsUnitName = contactsUnitName;
    }

    public String getContactsUnitContact() {
        return contactsUnitContact;
    }

    public void setContactsUnitContact(String contactsUnitContact) {
        this.contactsUnitContact = contactsUnitContact;
    }

    public Integer getDetectionCount() {
        return detectionCount;
    }

    public void setDetectionCount(Integer detectionCount) {
        this.detectionCount = detectionCount;
    }

    public Integer getDetectionPassCount() {
        return detectionPassCount;
    }

    public void setDetectionPassCount(Integer detectionPassCount) {
        this.detectionPassCount = detectionPassCount;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }
}
