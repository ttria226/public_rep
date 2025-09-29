package com.xsrw.wms.web.domain;

/**
 * @author wxr
 * @date 2023/10/23 19:04
 */
public class AgvEntity {

    /**
     * 类型
     */
    private String type;
    /**
     * 开始点位
     */
    private String startPoint;
    /**
     * 结束点位
     */
    private String endPoint;
    /**
     * 载具编号
     */
    private String trayCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }
}
