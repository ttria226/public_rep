package com.xsrw.wms.check.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TTask;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description 任务返回vo
 */
public class TaskVO extends TTask {

    /**
     * 任务详情
     */
    private List<TaskDetailVO> taskDetailVOList;

    /**
     * 物料名称
     */
    private String materialName;

   private String code;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 盘点维度  1物料  2库区
     */
    private String checkType;

    /**
     * 所属库区
     */
    private String reservoirName;

    /**
     * 所属区域
     */
    private String areaName;

    /**
     * 所属区域
     */
    private String locationName;
    /**
     * 执行状态名称
     */
    private String taskStatusName;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private BigDecimal predictCount;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private BigDecimal actualCount;

    /** 物料数量 */
    @Excel(name = "物料数量")
    private Long taskCount;

    /**
     * 盘点差异数量
     */
    @Excel(name = "盘差")
    private BigDecimal checkDifferenceCount;

    /**
     * 载具类型 1托盘、2料箱、3地堆
     */
    private String trayType;

    /**
     * 载具编号
     */
    private String trayCode;

    /**
     * 执行人
     */
    private String executeName;

    /**
     * 盘点计划名称
     */
    private String planName;
    /**
     * 来源1.手工创建 2ERP导入
     */
    private String checkSource;
    /**
     * 任务激活状态（0未激活  1已激活）
     */
    private String activateStatus;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public BigDecimal getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    public void setCheckDifferenceCount(BigDecimal checkDifferenceCount) {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public List<TaskDetailVO> getTaskDetailVOList() {
        return taskDetailVOList;
    }

    public void setTaskDetailVOList(List<TaskDetailVO> taskDetailVOList) {
        this.taskDetailVOList = taskDetailVOList;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Override
    public String getCheckType() {
        return checkType;
    }

    @Override
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public String getTrayType() {
        return trayType;
    }

    public void setTrayType(String trayType) {
        this.trayType = trayType;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getExecuteName() {
        return executeName;
    }

    public void setExecuteName(String executeName) {
        this.executeName = executeName;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getCheckSource() {
        return checkSource;
    }

    public void setCheckSource(String checkSource) {
        this.checkSource = checkSource;
    }

    public String getActivateStatus() {
        return activateStatus;
    }

    public void setActivateStatus(String activateStatus) {
        this.activateStatus = activateStatus;
    }
}
