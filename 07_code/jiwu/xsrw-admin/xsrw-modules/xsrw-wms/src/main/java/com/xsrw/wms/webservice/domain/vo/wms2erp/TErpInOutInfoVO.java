package com.xsrw.wms.webservice.domain.vo.wms2erp;

import com.xsrw.wms.webservice.domain.TErpInOut;

import java.math.BigDecimal;

public class TErpInOutInfoVO extends TErpInOut {

    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 实际数量
     */
    private BigDecimal actualCount;

    /**
     * erp库位编码
     */
    private String erpCode;

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

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }
}
