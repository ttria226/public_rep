package com.xsrw.wms.web.domain;

/**
 * agv上报返回结果
 * @author wxr
 * @date 2023/10/19 10:20
 */
public class AgvResultEntity {

    /**
     * 返回值
     */
    private Integer resultCode;
    /**
     * 返回值描述
     */
    private String msg;
    /**
     * 当前反馈的orderID
     */
    private Long orderID;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }
}
