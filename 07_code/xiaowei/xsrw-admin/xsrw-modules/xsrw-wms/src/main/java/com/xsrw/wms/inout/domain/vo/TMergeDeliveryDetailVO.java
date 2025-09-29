package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * @Description: 波次详情VO
 * @Author XMING
 * @Date 2023-06-27
 */
public class TMergeDeliveryDetailVO extends BaseEntity {


    /** 主键 **/
    private Long id;

    /** 物料 **/
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 出库单号
     */
    private String originCode;

    /**
     * 物料编号
     */
    private String materialCode;

    /**
     * 计量单位
     */
    private String unitName;

    /**
     * 出库数量
     */
    private Long num;

    private Long mergeDeliveryId;

    private Long predictReceiveCount;

    private String nextFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getMergeDeliveryId() {
        return mergeDeliveryId;
    }

    public void setMergeDeliveryId(Long mergeDeliveryId) {
        this.mergeDeliveryId = mergeDeliveryId;
    }

    public Long getPredictReceiveCount() {
        return predictReceiveCount;
    }

    public void setPredictReceiveCount(Long predictReceiveCount) {
        this.predictReceiveCount = predictReceiveCount;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    @Override
    public String toString() {
        return "TMergeDeliveryDetailVO{" +
                "id=" + id +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", originCode='" + originCode + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", num=" + num +
                ", mergeDeliveryId=" + mergeDeliveryId +
                ", predictReceiveCount=" + predictReceiveCount +
                ", nextFlag='" + nextFlag + '\'' +
                '}';
    }
}
