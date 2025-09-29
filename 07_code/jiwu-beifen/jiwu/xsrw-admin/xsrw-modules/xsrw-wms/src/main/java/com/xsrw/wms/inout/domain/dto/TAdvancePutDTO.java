package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvancePut;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/6 10:04
 */
public class TAdvancePutDTO extends TAdvancePut {

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
