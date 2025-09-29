package com.xsrw.wms.check.domain;

import java.io.Serializable;

public class TaskParam implements Serializable {

    private static final long serialVersionUID=1L;

    /** 原始任务id(上游系统ID) */
    private Long originTaskId;

    /** 任务类型（1.上架任务、2.拣货任务3.盘点任务、4.回库任务、5移位任务、6.空托盘取出） */
    private Integer taskType;

    /** 托盘ID（有就指定） */
    private Long trayId;

    /** 区域ID */
    private Long areaId;

    /** 库区ID */
    private Long reservoirId;

    /** 托盘编号（有就指定） */
    private String trayCode;

    /** 计划起始位置列 */
    private Integer palnStartPosotionX;

    /** 计划起始位置排 */
    private Integer palnStartPosotionY;

    /** 计划起始位置层 */
    private Integer palnStartPosotionZ;

    /** 计划目的位置列 */
    private Integer palnPurposePosotionX;

    /** 计划目的位置排 */
    private Integer palnPurposePosotionY;

    /** 计划目的位置层 */
    private Integer palnPurposePosotionZ;

    public Long getOriginTaskId() {
        return originTaskId;
    }

    public void setOriginTaskId(Long originTaskId) {
        this.originTaskId = originTaskId;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public Integer getPalnStartPosotionX() {
        return palnStartPosotionX;
    }

    public void setPalnStartPosotionX(Integer palnStartPosotionX) {
        this.palnStartPosotionX = palnStartPosotionX;
    }

    public Integer getPalnStartPosotionY() {
        return palnStartPosotionY;
    }

    public void setPalnStartPosotionY(Integer palnStartPosotionY) {
        this.palnStartPosotionY = palnStartPosotionY;
    }

    public Integer getPalnStartPosotionZ() {
        return palnStartPosotionZ;
    }

    public void setPalnStartPosotionZ(Integer palnStartPosotionZ) {
        this.palnStartPosotionZ = palnStartPosotionZ;
    }

    public Integer getPalnPurposePosotionX() {
        return palnPurposePosotionX;
    }

    public void setPalnPurposePosotionX(Integer palnPurposePosotionX) {
        this.palnPurposePosotionX = palnPurposePosotionX;
    }

    public Integer getPalnPurposePosotionY() {
        return palnPurposePosotionY;
    }

    public void setPalnPurposePosotionY(Integer palnPurposePosotionY) {
        this.palnPurposePosotionY = palnPurposePosotionY;
    }

    public Integer getPalnPurposePosotionZ() {
        return palnPurposePosotionZ;
    }

    public void setPalnPurposePosotionZ(Integer palnPurposePosotionZ) {
        this.palnPurposePosotionZ = palnPurposePosotionZ;
    }
}
