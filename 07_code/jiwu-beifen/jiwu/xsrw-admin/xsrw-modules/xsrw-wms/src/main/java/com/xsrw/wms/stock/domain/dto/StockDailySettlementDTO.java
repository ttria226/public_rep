package com.xsrw.wms.stock.domain.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 库存日结查询 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class StockDailySettlementDTO {

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     **/
    private String materialName;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 日期 yyyy-MM-dd
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
