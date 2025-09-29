package com.xsrw.wms.task.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 任务查询列表返回参数类
 */
public class TaskQueryVO {

    /**
     * 任务编号
     */
    @Excel(name = "任务编号", sort = 1)
    private String taskNo;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 2)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 3)
    private String batchCode;

    /**
     * 计划数量
     */
    @Excel(name = "计划数量", sort = 4)
    private Integer predictCount;

    /**
     * 实际数量
     */
    @Excel(name = "实际数量", sort = 5)
    private Integer actualCount;

    /**
     * 库区
     */
    @Excel(name = "库区", sort = 6)
    private String reservoirName;

    /**
     * 库位
     */
    @Excel(name = "库位", sort = 7)
    private String locationName;

    /**
     * 状态
     */
    @Excel(name = "状态", sort = 8,readConverterExp="1=未执行,2=执行中,3=已完成,4=执行失败")
    private String taskStatus;



    /**
     * 任务类型
     */
    @Excel(name = "任务类型", sort = 9,readConverterExp="1=上架任务,2=拣货任务,3=盘点任务,4=回库任务,5=移库任务,6=托盘取出")
    private String taskType;

    /**
     * 执行人
     */
    @Excel(name = "执行人", sort = 10)
    private String createBy;


    /**
     * 执行时间
     */
    @Excel(name = "执行时间", sort = 11,dateFormat = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Integer predictCount) {
        this.predictCount = predictCount;
    }

    public Integer getActualCount() {
        return actualCount;
    }

    public void setActualCount(Integer actualCount) {
        this.actualCount = actualCount;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
