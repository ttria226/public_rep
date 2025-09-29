package com.xsrw.wms.kanban.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 任务列表返回参数类
 */
public class TaskListVO {


    /**
     * 载具编号
     */
    private String trayCode;


    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 任务类型 1=入库任务,2=出库任务,3=盘点任务,4=回库任务,5=移库任务,6=托盘取出
     */
    private String taskType;


    /**
     * 状态 1=未执行,2=执行中,3=已完成,4=执行失败
     */
    private String taskStatus;

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
}
