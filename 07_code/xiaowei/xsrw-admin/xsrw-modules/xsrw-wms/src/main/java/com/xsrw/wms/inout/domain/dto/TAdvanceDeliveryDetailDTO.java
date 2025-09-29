package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TTaskIn;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/26 10:21
 */
public class TAdvanceDeliveryDetailDTO extends TAdvanceDeliveryDetail {

    /**
     * 入库单号
     */
    private String advanceDeliveryCode;
    /**
     * 单据类型字典1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入
     */
    private String advanceDeliveryType;

    /**
     * 单据来源
     */
    private String deliveryModule;

    /**
     * 库位id
     */
    private Long locationId;
    /**
     * 载具编号
     */
    private String trayCode;

    /**
     * 查询状态
     */
    private List<String> inStatusList;

    /**
     * 地堆状态（0部分1全部）
     */
    private String floorStatus;

    /**
     *
     */
    private List<Long> materialDetailIds;
    /**
     * rfid
     */
    private List<String> rfIds;

    /**
     * 任务列表
     */
    private List<TTaskIn> taskInList;

    /**
     * 状态(0新增;1已入库;2已出库未复核; 3已出库已复核)
     */
    private String status;

    /**
     * 是否查询打印次数
     */
    private String printStatus;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 不合格数量
     */
    private BigDecimal failCount;


    public String getAdvanceDeliveryCode() {
        return advanceDeliveryCode;
    }

    public void setAdvanceDeliveryCode(String advanceDeliveryCode) {
        this.advanceDeliveryCode = advanceDeliveryCode;
    }

    public String getAdvanceDeliveryType() {
        return advanceDeliveryType;
    }

    public void setAdvanceDeliveryType(String advanceDeliveryType) {
        this.advanceDeliveryType = advanceDeliveryType;
    }

    public String getDeliveryModule() {
        return deliveryModule;
    }

    public void setDeliveryModule(String deliveryModule) {
        this.deliveryModule = deliveryModule;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public List<String> getInStatusList() {
        return inStatusList;
    }

    public void setInStatusList(List<String> inStatusList) {
        this.inStatusList = inStatusList;
    }

    public String getFloorStatus() {
        return floorStatus;
    }

    public void setFloorStatus(String floorStatus) {
        this.floorStatus = floorStatus;
    }

    public List<String> getRfIds() {
        return rfIds;
    }

    public void setRfIds(List<String> rfIds) {
        this.rfIds = rfIds;
    }

    public List<Long> getMaterialDetailIds() {
        return materialDetailIds;
    }

    public void setMaterialDetailIds(List<Long> materialDetailIds) {
        this.materialDetailIds = materialDetailIds;
    }

    public List<TTaskIn> getTaskInList() {
        return taskInList;
    }

    public void setTaskInList(List<TTaskIn> taskInList) {
        this.taskInList = taskInList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getFailCount() {
        return failCount;
    }

    public void setFailCount(BigDecimal failCount) {
        this.failCount = failCount;
    }
}
