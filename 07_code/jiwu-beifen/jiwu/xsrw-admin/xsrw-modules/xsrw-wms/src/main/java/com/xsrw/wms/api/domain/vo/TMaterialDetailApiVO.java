package com.xsrw.wms.api.domain.vo;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/17 14:47
 */
public class TMaterialDetailApiVO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long materialId;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 预计数量
     */
    private Long predictCount;

    /**
     * 已收数量
     */
    private Long receiveCount;

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

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }
}
