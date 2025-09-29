package com.xsrw.wms.check.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 盘点差异报表DTO
 */
public class CheckResultDTO extends BaseEntity {

    private static final long serialVersionUID=1L;

    /** 物料标识 */
    private Long materialId;

    /** 物料名称 **/
    private String materialName;

    /** 部门标识 */
    private Long factory;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /** 物料id **/
    List<Long> materialIds;

    /** 创建时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 仓库 **/
    private Long currentWarehouseId;

    private String materialCode;

    /**
     * 部门标识
     */
    private Long deptId;

    private String planName;

    private String planId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Long getCurrentWarehouseId() {
        return currentWarehouseId;
    }

    public void setCurrentWarehouseId(Long currentWarehouseId) {
        this.currentWarehouseId = currentWarehouseId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<Long> materialIds) {
        this.materialIds = materialIds;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getFactory() {
        return factory;
    }

    public void setFactory(Long factory) {
        this.factory = factory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getPlanName (){return  planName;}
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanId (){return  planId;}
    public void setPlanId(String planId) { this.planId = planId; }
}
