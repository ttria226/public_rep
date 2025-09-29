package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

/**
 * 供应商交付统计报表返回参数类
 */
public class DeliveryReportVO {

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
     * 收货数量
     */
    @Excel(name = "收货数量", sort = 4)
    private Integer registrationCount;

    /**
     * 入库数量
     */
    @Excel(name = "入库数量", sort = 5)
    private Integer receiveCount;

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

    public Integer getRegistrationCount() {
        return registrationCount;
    }

    public void setRegistrationCount(Integer registrationCount) {
        this.registrationCount = registrationCount;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }
}
