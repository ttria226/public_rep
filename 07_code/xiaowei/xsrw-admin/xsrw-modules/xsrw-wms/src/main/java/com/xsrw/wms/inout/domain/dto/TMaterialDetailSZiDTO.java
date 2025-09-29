package com.xsrw.wms.inout.domain.dto;

import java.util.Date;

/**
 * 物料入库详情对象 t_material_detail
 *
 */
//物料入库扫描子表入参
public class TMaterialDetailSZiDTO {
    private static final long serialVersionUID = 1L;

    //入库子单据号
    private String rukuCode;

    public String getRukuCode() {
        return rukuCode;
    }

    public void setRukuCode(String rukuCode) {
        this.rukuCode = rukuCode;
    }
}
