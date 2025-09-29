package com.xsrw.wms.check.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TCheckResult;

import java.math.BigDecimal;

/**
 * 盘点差异报表VO
 */
public class CheckResultVO extends TCheckResult {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点差异数量
     */
    @Excel(name = "盘差", sort = 3)
    private BigDecimal checkDifferenceCount;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 物料
     */
    @Excel(name = "物料编码", sort = 1)
    private String materialCode;

    /**
     * 物料
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;

    /**
     * 托盘标识
     */
    private String trayId;

    /**
     * 托盘
     */
    private String trayCode;

    private String taskCode;

    private String planName;
    private String unitame;
    private Long planId;
    private String locationName;
    /**
     * 任务明细的执行状态(0未完成，1进行中 2已完成 3审核中  4已审核  5已驳回)
     */
    private String status;
    @Override
    public BigDecimal getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    @Override
    public void setCheckDifferenceCount(BigDecimal checkDifferenceCount) {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
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

    public String getTrayId() {
        return trayId;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
    public String getPlanName (){return  planName;}
    public void setPlanName(String planName) { this.planName = planName; }

    public String getUnitName (){return  unitame;}
    public void setUnitName(String unitame) { this.unitame = unitame; }

    public Long getPlanId (){return  planId;}
    public void setPlanId(Long planId) { this.planId = planId; }

    public String getLocationName (){return  locationName;}
    public void setLocationName(String locationName) { this.locationName = locationName; }
    public String getStatus (){return  status;}
    public void setStatus(String status) { this.status = status; }
}
