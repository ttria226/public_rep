package com.xsrw.wms.report.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 出入库流水报表 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class InOutStatementDTO {

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     **/
    private String materialName;

    /**
     * 客户Id
     **/
    private Integer contactsUnitId;

    /**
     * 开始时间 yyyy-MM-dd
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;

    /**
     * 结束时间 yyyy-MM-dd
     **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 类型 1：入库 2：出库
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getContactsUnitId() {
        return contactsUnitId;
    }

    public void setContactsUnitId(Integer contactsUnitId) {
        this.contactsUnitId = contactsUnitId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
