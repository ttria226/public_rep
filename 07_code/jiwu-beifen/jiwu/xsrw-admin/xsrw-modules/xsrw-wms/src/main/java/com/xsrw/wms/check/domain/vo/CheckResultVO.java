package com.xsrw.wms.check.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TCheckResult;

/**
 * 盘点差异报表VO
 */
public class CheckResultVO extends TCheckResult {

    private static final long serialVersionUID = 1L;

    /**
     * 盘点差异数量
     */
    @Excel(name = "盘差", sort = 3)
    private Long checkDifferenceCount;

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

    @Override
    public Long getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    @Override
    public void setCheckDifferenceCount(Long checkDifferenceCount) {
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
}
