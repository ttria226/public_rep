package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvanceQuality;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/6 9:43
 */
public class TAdvanceQualityDTO extends TAdvanceQuality {

    /**
     * 入库单类型
     */
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
