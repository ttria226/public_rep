package com.xsrw.wms.report.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * @Description: 效率统计 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class EfficiencyStatisticsDTO extends BaseEntity {

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
}
