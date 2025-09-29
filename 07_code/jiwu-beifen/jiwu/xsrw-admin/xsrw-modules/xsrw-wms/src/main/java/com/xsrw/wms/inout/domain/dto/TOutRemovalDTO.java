package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TOutRemoval;
import com.xsrw.wms.inout.domain.TOutRemovalDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description 发货退货单DTO
 * @date 2023/6/9 14:07
 */
public class TOutRemovalDTO extends TOutRemoval {

    /**
     * 子表列表
     */
    private List<TOutRemovalDetail> tOutDeliveryDetailList;

    public List<TOutRemovalDetail> gettOutDeliveryDetailList() {
        return tOutDeliveryDetailList;
    }

    public void settOutDeliveryDetailList(List<TOutRemovalDetail> tOutDeliveryDetailList) {
        this.tOutDeliveryDetailList = tOutDeliveryDetailList;
    }
}
