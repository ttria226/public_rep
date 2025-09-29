package com.xsrw.wms.inout.domain.vo;

import java.math.BigDecimal;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/25 17:30
 */
public class TOutboundScanningVO {

    //出库单据号
    private String chukuCode;
    //物料编号
    private String wuliaoCode;
    //物料名称
    private String name;
    //批次号
    private String batchCode;
    //RFID标签ID
    private String rfid;
    //换算数量
    private BigDecimal huansuanShuliang;
    //扫描时间
    private String saomiaoShijian;
    //出库状态
    private String chukuSaomiaoFlag;

    public String getChukuCode() {
        return chukuCode;
    }

    public void setChukuCode(String chukuCode) {
        this.chukuCode = chukuCode;
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

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public BigDecimal getHuansuanShuliang() {
        return huansuanShuliang;
    }

    public void setHuansuanShuliang(BigDecimal huansuanShuliang) {
        this.huansuanShuliang = huansuanShuliang;
    }

    public String getSaomiaoShijian() {
        return saomiaoShijian;
    }

    public void setSaomiaoShijian(String saomiaoShijian) {
        this.saomiaoShijian = saomiaoShijian;
    }

    public String getChukuSaomiaoFlag() {
        return chukuSaomiaoFlag;
    }

    public void setChukuSaomiaoFlag(String chukuSaomiaoFlag) {
        this.chukuSaomiaoFlag = chukuSaomiaoFlag;
    }
}
