package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TOutRemoval;
import com.xsrw.wms.inout.domain.TOutRemovalDetail;

import java.util.Date;
import java.util.List;

/**
 * @author jfy
 * @description 出库扫描DTO
 * @date 2023/6/9 14:07
 */
public class TOutboundScanningDTO {
    private static final long serialVersionUID = 1L;

    private String chukuCode;
    private String wuliaoCode;
    private Date saomiaoShijianStart;
    private Date saomiaoShijianEnd;

    public String getChukuCode() {
        return chukuCode;
    }

    public void setChukuCode(String chukuCode) {
        this.chukuCode = chukuCode;
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
