package com.xsrw.wms.inout.domain.vo;

import com.xsrw.wms.inout.domain.TTaskIn;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/11 10:58
 */
public class TTaskInVO extends TTaskIn {

    /**
     * 原单标识
     */
    private Long originId;

    /**
     * 原单单号
     */
    private String originCode;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料单位名称
     */
    private String unitName;

    /**
     * 托盘编号
     */
    private String trayCode;

    /**
     * 已上架数量
     */
    private BigDecimal putawayCountDelivery;
    /**
     * 实际收货数量
     */
    private BigDecimal receiveCountDelivery;

    /**
     * 检测数量
     */
    private BigDecimal detectionCountDelivery;

    /**
     * 生产日期
     */
    private Date producedDate;


    /**
     * 到期日期
     */
    private Date expireDate;

    public Long getOriginId() {
        return originId;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public BigDecimal getPutawayCountDelivery() {
        return putawayCountDelivery;
    }

    public void setPutawayCountDelivery(BigDecimal putawayCountDelivery) {
        this.putawayCountDelivery = putawayCountDelivery;
    }

    public BigDecimal getReceiveCountDelivery() {
        return receiveCountDelivery;
    }

    public void setReceiveCountDelivery(BigDecimal receiveCountDelivery) {
        this.receiveCountDelivery = receiveCountDelivery;
    }

    public BigDecimal getDetectionCountDelivery() {
        return detectionCountDelivery;
    }

    public void setDetectionCountDelivery(BigDecimal detectionCountDelivery) {
        this.detectionCountDelivery = detectionCountDelivery;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

}
