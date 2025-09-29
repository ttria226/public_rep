package com.xsrw.wms.inout.domain.vo;

import com.xsrw.wms.inout.domain.TMaterialDetail;

import java.math.BigDecimal;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/25 17:30
 */
public class TMaterialDetailSVO  {

    //入库单据号
    private String rukuCode;
    //入库子单据号
    private String rukuziCode;
    //物料编号
    private String wuliaoCode;
    //物料名称
    private String name;
    //批次号
    private String batchCode;
    //计划入库数量
    private BigDecimal predictCount;
    //扫描数量
    private BigDecimal saomiaoShuliang;
    //扫描时间
    private String saomiaoShijian;

    public String getRukuCode() {
        return rukuCode;
    }

    public void setRukuCode(String rukuCode) {
        this.rukuCode = rukuCode;
    }

    public String getRukuziCode() {
        return rukuziCode;
    }

    public void setRukuziCode(String rukuziCode) {
        this.rukuziCode = rukuziCode;
    }

    public String getWuliaoCode() {
        return wuliaoCode;
    }

    public void setWuliaoCode(String wuliaoCode) {
        this.wuliaoCode = wuliaoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getSaomiaoShuliang() {
        return saomiaoShuliang;
    }

    public void setSaomiaoShuliang(BigDecimal saomiaoShuliang) {
        this.saomiaoShuliang = saomiaoShuliang;
    }

    public String getSaomiaoShijian() {
        return saomiaoShijian;
    }

    public void setSaomiaoShijian(String saomiaoShijian) {
        this.saomiaoShijian = saomiaoShijian;
    }
}
