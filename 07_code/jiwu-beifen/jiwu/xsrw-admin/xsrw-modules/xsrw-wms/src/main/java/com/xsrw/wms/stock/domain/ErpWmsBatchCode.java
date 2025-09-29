package com.xsrw.wms.stock.domain;

import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * @author wxr
 * @date 2024/1/4 10:05
 */
public class ErpWmsBatchCode extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long materialId;

    private String materialCode;

    private String erpCode;

    private String wmsCode;

    private Long erpNum;

    private Long wmsNum;

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

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getErpCode() {
        return erpCode;
    }

    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }

    public String getWmsCode() {
        return wmsCode;
    }

    public void setWmsCode(String wmsCode) {
        this.wmsCode = wmsCode;
    }

    public Long getErpNum() {
        return erpNum;
    }

    public void setErpNum(Long erpNum) {
        this.erpNum = erpNum;
    }

    public Long getWmsNum() {
        return wmsNum;
    }

    public void setWmsNum(Long wmsNum) {
        this.wmsNum = wmsNum;
    }
}
