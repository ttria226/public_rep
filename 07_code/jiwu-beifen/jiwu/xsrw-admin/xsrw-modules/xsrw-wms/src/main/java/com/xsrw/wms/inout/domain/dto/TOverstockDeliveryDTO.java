package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TOverstockDelivery;
import com.xsrw.wms.inout.domain.TOverstockDeliveryDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/25 14:50
 */
public class TOverstockDeliveryDTO extends TOverstockDelivery {

    /**
     * 详情列表
     */
    private List<TOverstockDeliveryDetail> deliveryDetailList;

    public List<TOverstockDeliveryDetail> getDeliveryDetailList() {
        return deliveryDetailList;
    }

    public void setDeliveryDetailList(List<TOverstockDeliveryDetail> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
    }

}
