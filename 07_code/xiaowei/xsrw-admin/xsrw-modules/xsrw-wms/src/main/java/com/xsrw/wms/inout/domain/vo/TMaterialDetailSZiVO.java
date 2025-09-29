package com.xsrw.wms.inout.domain.vo;

import java.math.BigDecimal;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/25 17:30
 */
public class TMaterialDetailSZiVO extends TMaterialDetailSVO{

    //RFID标签ID
   private String rfid;
   //标签数量;
    private String rfidCount;
    //RFID扫描状态
    private String rfidFlag;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getRfidCount() {
        return rfidCount;
    }

    public void setRfidCount(String rfidCount) {
        this.rfidCount = rfidCount;
    }

    public String getRfidFlag() {
        return rfidFlag;
    }

    public void setRfidFlag(String rfidFlag) {
        this.rfidFlag = rfidFlag;
    }
}
