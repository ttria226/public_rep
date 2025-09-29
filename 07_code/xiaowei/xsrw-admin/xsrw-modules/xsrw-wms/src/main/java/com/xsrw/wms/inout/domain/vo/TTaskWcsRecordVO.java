package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;

/**
 * @author wxr
 * @date 2023/11/15 17:20
 */
public class TTaskWcsRecordVO extends TTaskWcsRecord {

    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 任务类型（1.入库任务、2.出库任务3.盘点任务、4.回库任务、5移库任务、 6.托盘取出）
     */
    private String taskType;

    /**
     * 执行状态（1=未执行,2=执行中,3=已完成）
     */
    private String taskStatus;

    /**
     * 托盘id
     */
    private Long trayId;

    /**
     * 托盘code
     */
    private String trayCode;

    /**
     * 库位标识
     */
    private Long locationId;
    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 货位状态
     */
    private String goodsAllocationStatus;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }
}
