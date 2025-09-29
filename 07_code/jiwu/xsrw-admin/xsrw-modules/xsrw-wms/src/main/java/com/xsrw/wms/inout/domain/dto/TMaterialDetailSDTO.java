package com.xsrw.wms.inout.domain.dto;

import java.util.Date;

/**
 * 物料入库详情对象 t_material_detail
 *
 */
//物料入库扫描入参
public class TMaterialDetailSDTO {
    private static final long serialVersionUID = 1L;

    private String rukuCode;
    private String wuliaoCode;
    private Date saomiaoShijianStart;
    private Date saomiaoShijianEnd;

    public String getRukuCode() {
        return rukuCode;
    }

    public void setRukuCode(String rukuCode) {
        this.rukuCode = rukuCode;
    }

    public String getWuliaoCode() {
        return wuliaoCode;
    }

    public void setWuliaoCode(String wuliaoCode) {
        this.wuliaoCode = wuliaoCode;
    }

    public Date getSaomiaoShijianStart() {
        return saomiaoShijianStart;
    }

    public void setSaomiaoShijianStart(Date saomiaoShijianStart) {
        this.saomiaoShijianStart = saomiaoShijianStart;
    }

    public Date getSaomiaoShijianEnd() {
        return saomiaoShijianEnd;
    }

    public void setSaomiaoShijianEnd(Date saomiaoShijianEnd) {
        this.saomiaoShijianEnd = saomiaoShijianEnd;
    }
}
