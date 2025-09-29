package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

/**
 * 物料合格率列表返回参数类
 */
public class MaterialQualificationRateVO {

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
    @Excel(name = "收货数量", sort = 3)
    private Integer registrationCount;



    /**
     * 检测通过数量
     **/
    @Excel(name = "检测通过数量", sort = 4)
    private Integer detectionCount;

    /**
     * 使用频率
     **/
    @Excel(name = "使用频率", sort = 5)
    private String passRate;

    /**
     * 月份
     */
    @Excel(name = "月份", sort = 6)
    private String month;

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

    public Integer getDetectionCount() {
        return detectionCount;
    }

    public void setDetectionCount(Integer detectionCount) {
        this.detectionCount = detectionCount;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
