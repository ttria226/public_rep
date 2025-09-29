package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/8 15:33
 */
public class TAdvanceDeliveryDTO extends TAdvanceDelivery {


    /**
     * 多个状态
     */
    private String inStatus;
    /**
     * 入库单详情列表
     */
    private List<TAdvanceDeliveryDetail> deliveryDetailList;

    public String getInStatus() {
        return inStatus;
    }

    public void setInStatus(String inStatus) {
        this.inStatus = inStatus;
    }

    public List<TAdvanceDeliveryDetail> getDeliveryDetailList() {
        return deliveryDetailList;
    }

    public void setDeliveryDetailList(List<TAdvanceDeliveryDetail> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
    }

}
