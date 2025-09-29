package com.xsrw.wms.report.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * @Description: 库存统计 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class StoreStatisticsDTO extends BaseEntity {

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     **/
    private String materialName;

    /**
     * 批次号
     **/
    private String batchCode;

    /**
     * 库区id
     **/
    private Integer reservoirId;

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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Integer reservoirId) {
        this.reservoirId = reservoirId;
    }
}
