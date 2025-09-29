package com.xsrw.wms.api.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/15 16:38
 */
public class TAdvanceDeliveryApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 物料id
     */
    private Long materialId;
    /**
     * 批次号
     */
    private String batchCode;
    /**
     * 数量
     */
    private Long count;

    /**
     * rfid
     */
    private List<String> rfids;

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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<String> getRfids() {
        return rfids;
    }

    public void setRfids(List<String> rfids) {
        this.rfids = rfids;
    }
}
