package com.xsrw.wms.inout.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 出库单详情对象 t_out_delivery_detail
 *
 * @author zyq
 * @date 2023-05-09
 */
public class TOutDeliveryDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 出库单标识
     */
    @Excel(name = "出库单标识")
    private Long outDeliveryId;

    /**
     * 物料标识
     */
    @Excel(name = "物料标识")
    private Long materialId;

    @Excel(name = "物料标识")
    @TableField(exist = false)
    private String materialName;

    @Excel(name = "物料标识")
    @TableField(exist = false)
    private String materialUnit;

    @Excel(name = "物料标识")
    @TableField(exist = false)
    private String materialCode;

    /**
     * 是否转为拣货
     */
    @Excel(name = "是否转为拣货")
    private String nextFlag;

    /**
     * 本次预计数量
     */
    @Excel(name = "本次预计数量")
    private BigDecimal predictCount;

    @TableField(exist = false)
    private Long status;

    @TableField(exist = false)
    private Long type;

    @TableField(exist = false)
    private String originCode;

    /**
     * 预计实际数量
     */
    @Excel(name = "预计实际数量")
    private BigDecimal predictReceiveCount;

    /**
     * 实际拣货数量:小件领取需要用到
     */
    @Excel(name = "实际拣货数量")
    private BigDecimal receiveCount;

    /**
     * 小件预计数量
     */
    @Excel(name = "小件预计数量")
    private BigDecimal smallPredictCount;

    /**
     * 小件实际拣货数量
     */
    @Excel(name = "小件实际拣货数量")
    private BigDecimal smallReceiveCount;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 行项目
     */
    private String tbpos;

    @Excel(name = "库存")
    @TableField(exist = false)
    private Long stockId;

    /**
     * 已退货数量
     */
    @TableField(exist = false)
    private Long returnCount;

    /**
     * 小件单位
     */
    @TableField(exist = false)
    private String minUnitName;


    /**
     * pda端查询标识
     */
    @TableField(exist = false)
    private String padFlag;


    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOutDeliveryId() {
        return outDeliveryId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setOutDeliveryId(Long outDeliveryId) {
        this.outDeliveryId = outDeliveryId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getPredictReceiveCount() {
        return predictReceiveCount;
    }

    public void setPredictReceiveCount(BigDecimal predictReceiveCount) {
        this.predictReceiveCount = predictReceiveCount;
    }

    public BigDecimal getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(BigDecimal receiveCount) {
        this.receiveCount = receiveCount;
    }

    public BigDecimal getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(BigDecimal smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public BigDecimal getSmallReceiveCount() {
        return smallReceiveCount;
    }

    public void setSmallReceiveCount(BigDecimal smallReceiveCount) {
        this.smallReceiveCount = smallReceiveCount;
    }

    public Long getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public String getPadFlag() {
        return padFlag;
    }

    public void setPadFlag(String padFlag) {
        this.padFlag = padFlag;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getTbpos() {
        return tbpos;
    }

    public void setTbpos(String tbpos) {
        this.tbpos = tbpos;
    }

    @Override
    public String toString() {
        return "TOutDeliveryDetail{" +
                "id=" + id +
                ", outDeliveryId=" + outDeliveryId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialUnit='" + materialUnit + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", nextFlag='" + nextFlag + '\'' +
                ", predictCount=" + predictCount +
                ", status=" + status +
                ", type=" + type +
                ", originCode='" + originCode + '\'' +
                ", predictReceiveCount=" + predictReceiveCount +
                ", receiveCount=" + receiveCount +
                ", smallPredictCount=" + smallPredictCount +
                ", smallReceiveCount=" + smallReceiveCount +
                ", stockId=" + stockId +
                ", returnCount=" + returnCount +
                ", minUnitName='" + minUnitName + '\'' +
                ", padFlag='" + padFlag + '\'' +
                ", batchCode='" + batchCode + '\'' +
                '}';
    }
}
