package com.xsrw.common.core.print;

/**
 * @Description: 打印机参数对象
 * @Author XMING
 * @Date 2023-10-13
 */
public class ZplPrint {

    /**
     * 物料名称
     */
    private String description;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 标签编码（rfid值）
     */
    private String rfid;

    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 单位
     */
    private String unitName;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public ZplPrint(){
    }

    /**
     *
     * @param description 物料全称
     * @param materialCode 物料编码
     * @param rfid 标签编码
     * @param batchCode 批次号
     * @param unitName 单位
     */
    public ZplPrint(String description, String materialCode,String rfid, String batchCode,String unitName) {
        this.description = description;
        this.materialCode = materialCode;
        this.rfid = rfid;
        this.batchCode = batchCode;
        this.unitName = unitName;
    }
}
