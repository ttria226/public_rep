package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TAdvanceRegistration;
import com.xsrw.wms.inout.domain.TTaskIn;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/9 14:36
 */
public class TAdvanceRegistrationDTO extends TAdvanceRegistration {

    /**
     * 入库单号
     */
    private String advanceDeliveryCode;
    /**
     * 单据类型字典1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入
     */
    private String advanceDeliveryType;

    /**
     * 库位id
     */
    private Long locationId;
    /**
     * 载具编号
     */
    private String trayCode;


    /**
     * 任务列表
     */
    private List<TTaskIn> taskInList;

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

    public List<TTaskIn> getTaskInList() {
        return taskInList;
    }

    public void setTaskInList(List<TTaskIn> taskInList) {
        this.taskInList = taskInList;
    }
}
