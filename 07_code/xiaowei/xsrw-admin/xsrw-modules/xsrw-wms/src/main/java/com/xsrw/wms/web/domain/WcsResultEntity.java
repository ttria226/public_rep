package com.xsrw.wms.web.domain;

/**
 * @Description: WCS返回结果实体
 * @Author XMING
 * @Date 2023-10-17
 */
public class WcsResultEntity {


    /**
     * 请求唯一码
     */
    private String reqID;

    /**
     * 接收时间
     */
    private String resultTime;

    /**
     * 结果状态码
     */
    private String resultCode;

    /**
     * 状态信息
     */
    private String resultMsg;

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "WcsResultEntity{" +
                "reqID='" + reqID + '\'' +
                ", resultTime='" + resultTime + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
