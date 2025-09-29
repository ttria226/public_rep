package com.xsrw.wms.check.domain;

import java.io.Serializable;

public class StrategyParam implements Serializable {

    /**
     * 物料类别
     */
    private Long categoryId;

    /**
     * 包装方式
     */
    private Long packId;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 是否允许物料混放  0 否  1是
     */
    private String sameMaterialFlag;

    /**
     * 是否允许混批次  0 否 1是
     */
    private String sameBatchFlag;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPackId() {
        return packId;
    }

    public void setPackId(Long packId) {
        this.packId = packId;
    }

    public String getSameMaterialFlag() {
        return sameMaterialFlag;
    }

    public void setSameMaterialFlag(String sameMaterialFlag) {
        this.sameMaterialFlag = sameMaterialFlag;
    }

    public String getSameBatchFlag() {
        return sameBatchFlag;
    }

    public void setSameBatchFlag(String sameBatchFlag) {
        this.sameBatchFlag = sameBatchFlag;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
}
