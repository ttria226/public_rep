package com.xsrw.wms.report.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * @author tyk
 * @description
 * @date 2023/07/07
 */
public class DeliveryTimeStatisticsVO {


    /**
     * 单号
     */
    @Excel(name = "单号", sort = 1)
    private String code;
    /**
     * 创建者
     */
    @Excel(name = "预计数量", sort = 2)
    private Integer predictCount;

    /**
     * 收货数量
     */
    @Excel(name = "收货数量", sort = 3)
    private Integer receiveCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "创建日期", sort = 4, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 预计交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "预计交货日期", sort = 5, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planDate;

    /**
     * 实际交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @Excel(name = "实际交货日期", sort = 6, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date predictDate;

    /**
     * 时长
     */
    @Excel(name = "时长（h）", sort = 7)
    private Double time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Integer predictCount) {
        this.predictCount = predictCount;
    }

    public Integer getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Integer receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Date getPredictDate() {
        return predictDate;
    }

    public void setPredictDate(Date predictDate) {
        this.predictDate = predictDate;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
