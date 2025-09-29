package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvanceCollection;
import com.xsrw.wms.inout.domain.TAdvanceCollectionDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/6 14:04
 */
public class TAdvanceCollectionDTO extends TAdvanceCollection {

    /**
     * 入库单类型
     */
    private String type;
    /**
     * 详情列表
     */
    private List<TAdvanceCollectionDetail> detailList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<TAdvanceCollectionDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<TAdvanceCollectionDetail> detailList) {
        this.detailList = detailList;
    }
}
