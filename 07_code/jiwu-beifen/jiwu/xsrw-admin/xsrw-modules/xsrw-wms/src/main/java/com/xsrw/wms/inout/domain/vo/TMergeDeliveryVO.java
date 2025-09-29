package com.xsrw.wms.inout.domain.vo;

import com.xsrw.wms.inout.domain.TMergeDelivery;

import java.util.List;

/**
 * @Description: 波次单VO
 * @Author XMING
 * @Date 2023-06-27
 */
public class TMergeDeliveryVO extends TMergeDelivery {


    /**
     * 波次详情
     */
    List<TMergeDeliveryDetailVO>  detail;

    public List<TMergeDeliveryDetailVO> getDetail() {
        return detail;
    }

    public void setDetail(List<TMergeDeliveryDetailVO> detail) {
        this.detail = detail;
    }
}
