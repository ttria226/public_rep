package com.xsrw.wms.base.domain.vo;

import com.xsrw.wms.base.domain.TMaterialDetailPrint;

/**
 * @author wxr
 * @date 2023/11/9 9:41
 */
public class TMaterialDetailPrintVO extends TMaterialDetailPrint {

    /**
     * 单据总数
     */
    private Integer detectionCount;
    /**
     * 物料id
     */
    private Long materialId;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 批次号
     */
    private String batchCode;
    /**
     * 入库单号
     */
    private String advanceDeliveryCode;

    public Integer getDetectionCount() {
        return detectionCount;
    }

    public void setDetectionCount(Integer detectionCount) {
        this.detectionCount = detectionCount;
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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getAdvanceDeliveryCode() {
        return advanceDeliveryCode;
    }

    public void setAdvanceDeliveryCode(String advanceDeliveryCode) {
        this.advanceDeliveryCode = advanceDeliveryCode;
    }
}
