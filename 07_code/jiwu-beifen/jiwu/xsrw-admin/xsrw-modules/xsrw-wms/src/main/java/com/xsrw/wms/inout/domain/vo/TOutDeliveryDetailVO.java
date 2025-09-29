package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.Date;

/**
 * 出库单详情对象 t_out_delivery_detail
 *
 * @author zyq
 * @date 2023-05-09
 */
public class TOutDeliveryDetailVO extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 出库单标识
     */
    private Long outDeliveryId;

    /**
     * 出库源单单号
     */
    @Excel(name = "出库单号")
    private String originCode;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    @Excel(name = "物料编号")
    private String materialCode;

    @Excel(name = "计量单位")
    private String unitName;

//    @Excel(name = "批次")
    private String batchCode;

    /**
     * 规格
     */
//    @Excel(name = "规格")
    private String materialSpecifications;

    /**
     * 数量
     */
    @Excel(name = "需出库数量")
    private Long num;

    /**
     * 预计数量
     */
    private Long predictCount;


    private Long predictReceiveCount;


    private Long smallPredictReceiveCount;

    @Excel(name = "小件数量")
    private Long smallPredictCount;

    /**
     * 出库类型（1调拨出库 2销售出库  3领用出库）
     */
    @Excel(name = "单据类型", readConverterExp = "1=调拨出库,2=销售出库,3=领用出库")
    private String type;

    /**
     * 是否转为拣货
     */
//    @Excel(name = "是否转为拣货 1待执行，2已执行")
    private String nextFlag;

    public Long getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(Long smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public Long getSmallPredictReceiveCount() {
        return smallPredictReceiveCount;
    }

    public void setSmallPredictReceiveCount(Long smallPredictReceiveCount) {
        this.smallPredictReceiveCount = smallPredictReceiveCount;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getPredictReceiveCount() {
        return predictReceiveCount;
    }

    public void setPredictReceiveCount(Long predictReceiveCount) {
        this.predictReceiveCount = predictReceiveCount;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getId() {
        return id;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOutDeliveryId() {
        return outDeliveryId;
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

    public String getMaterialName() {
        return materialName;
    }

    public String getMaterialSpecifications() {
        return materialSpecifications;
    }

    public void setMaterialSpecifications(String materialSpecifications) {
        this.materialSpecifications = materialSpecifications;
    }

    @Override
    public String toString() {
        return "TOutDeliveryDetailVO{" +
                "id=" + id +
                ", outDeliveryId=" + outDeliveryId +
                ", originCode='" + originCode + '\'' +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialSpecifications='" + materialSpecifications + '\'' +
                ", num=" + num +
                ", type='" + type + '\'' +
                ", nextFlag='" + nextFlag + '\'' +
                '}';
    }
}
