package com.xsrw.wms.report.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 库龄分析 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class WareHouseAgeAnalyseDTO {

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     **/
    private String materialName;

    /**
     * 批次号
     **/
    private String batchCode;

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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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
