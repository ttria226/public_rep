package com.xsrw.wms.web.domain;

import java.util.List;

/**
 * @Description: WCS发送实体类
 * @Author XMING
 * @Date 2023-10-17
 */
public class WcsSendEntity {


    /**
     * 请求唯一码
     */
    private String reqID;

    /**
     * 请求时间
     */
    private String reqTime;

    /**
     * 单据号
     */
    private String orderNo;


    /**
     * 物料明细
     */
    private List<WcsOrderEntity> productDetails;


    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<WcsOrderEntity> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<WcsOrderEntity> productDetails) {
        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return "{" +
                "reqID='" + reqID + '\'' +
                ", reqTime='" + reqTime + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", productDetails=" + productDetails +
                '}';
    }
}
