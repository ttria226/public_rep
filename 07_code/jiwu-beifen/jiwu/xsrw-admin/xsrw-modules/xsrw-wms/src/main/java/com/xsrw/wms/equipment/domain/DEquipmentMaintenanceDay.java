package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保养工单对象 d_equipment_maintenance_day
 *
 * @author zjj
 * @date 2023-05-11
 */
@TableName("d_equipment_maintenance_day")
public class DEquipmentMaintenanceDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 1：维修/保养单号 */
    @Excel(name = "单号")
    private String dayNo;

    @TableField(exist = false)
    @Excel(name = "设备名称")
    private String equName;

    @TableField(exist = false)
    @Excel(name = "设备编号")
    private String equNo;
    /** 设备id */
    private Long equipmentId;

    /** 计划id */
    private Long planId;

    /** 配件名称 */
    private String partName;

    /** 计划保养内容 */
    private String content;

    /** 保养类型1：一级保养2：二级 3：常规（字典） */
    private Long maintenanceType;

    /** 是否外部保养1：是 0：否 */
    private Long isExternal;

    /** 外部保养公司 */
    private String externalCompany;

    /** 计划保养日期 */
    @Excel(name = "计划保养日期")
    private String planDay;

    /** 实际保养日期 */
    @Excel(name = "实际保养日期")
    private String trueDay;

    /** 保养部门id */
    private Long companyId;

    /** 执行人id */
    private Long executorId;

    /** 执行人名称 */
    @Excel(name = "执行人名称")
    private String executorName;

    /** 1：未分派 2：已分派 0：撤销 3：已完成（已检测）*/
    private Integer status;
    /** 来源 1：保养计划 2：新建工单 */
    private Integer source;
    /** 保养前图片 */
    private String beforeImg;

    /** 保养后图片 */
    private String afterImg;

    /** 保养价格 */
    private BigDecimal price;
    /** 消耗物料 */
    private String material;

    /** 保养公司 */
    private String maintenanceCompany;
    /** 保养说明 */
    private String remark;
    /** 1：保养工单 2：维修工单 */
    private Integer type;

    @TableField(exist = false)
    private Long dayid;



    @TableField(exist = false)
    private String userName;



    /** 设备故障报修等级字典equ_fault_lv */
    private Integer equFaultLv;

    /** 是否停机 */
    private Integer isShutdown;

    @TableField(exist = false)
    private String createTimeSearch;

    /** 维修类型 */
    private Integer repairType;





    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEquipmentId(Long equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId()
    {
        return equipmentId;
    }
    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public Long getPlanId()
    {
        return planId;
    }
    public void setPartName(String partName)
    {
        this.partName = partName;
    }

    public String getPartName()
    {
        return partName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setMaintenanceType(Long maintenanceType)
    {
        this.maintenanceType = maintenanceType;
    }

    public Long getMaintenanceType()
    {
        return maintenanceType;
    }
    public void setIsExternal(Long isExternal)
    {
        this.isExternal = isExternal;
    }

    public Long getIsExternal()
    {
        return isExternal;
    }
    public void setExternalCompany(String externalCompany)
    {
        this.externalCompany = externalCompany;
    }

    public String getExternalCompany()
    {
        return externalCompany;
    }
    public void setPlanDay(String planDay)
    {
        this.planDay = planDay;
    }

    public String getPlanDay()
    {
        return planDay;
    }
    public void setTrueDay(String trueDay)
    {
        this.trueDay = trueDay;
    }

    public String getTrueDay()
    {
        return trueDay;
    }
    public void setExecutorId(Long executorId)
    {
        this.executorId = executorId;
    }

    public Long getExecutorId()
    {
        return executorId;
    }

    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setExecutorName(String executorName)
    {
        this.executorName = executorName;
    }

    public String getExecutorName()
    {
        return executorName;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setSource(Integer source)
    {
        this.source = source;
    }

    public Integer getSource()
    {
        return source;
    }

    public String getBeforeImg() {
        return beforeImg;
    }

    public void setBeforeImg(String beforeImg) {
        this.beforeImg = beforeImg;
    }

    public String getAfterImg() {
        return afterImg;
    }

    public void setAfterImg(String afterImg) {
        this.afterImg = afterImg;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getMaintenanceCompany() {
        return maintenanceCompany;
    }

    public void setMaintenanceCompany(String maintenanceCompany) {
        this.maintenanceCompany = maintenanceCompany;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDayid() {
        return dayid;
    }

    public void setDayid(Long dayid) {
        this.dayid = dayid;
    }

    public String getEquName() {
        return equName;
    }

    public void setEquName(String equName) {
        this.equName = equName;
    }

    public String getEquNo() {
        return equNo;
    }

    public void setEquNo(String equNo) {
        this.equNo = equNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDayNo() {
        return dayNo;
    }

    public void setDayNo(String dayNo) {
        this.dayNo = dayNo;
    }

    public Integer getEquFaultLv() {
        return equFaultLv;
    }

    public void setEquFaultLv(Integer equFaultLv) {
        this.equFaultLv = equFaultLv;
    }

    public Integer getIsShutdown() {
        return isShutdown;
    }

    public void setIsShutdown(Integer isShutdown) {
        this.isShutdown = isShutdown;
    }

    public String getCreateTimeSearch() {
        return createTimeSearch;
    }

    public void setCreateTimeSearch(String createTimeSearch) {
        this.createTimeSearch = createTimeSearch;
    }

    public Integer getRepairType() {
        return repairType;
    }

    public void setRepairType(Integer repairType) {
        this.repairType = repairType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("equipmentId", getEquipmentId())
            .append("partName", getPartName())
            .append("content", getContent())
            .append("maintenanceType", getMaintenanceType())
            .append("isExternal", getIsExternal())
            .append("externalCompany", getExternalCompany())
            .append("planDay", getPlanDay())
            .append("trueDay", getTrueDay())
            .append("companyId", getCompanyId())
            .append("executorId", getExecutorId())
            .append("executorName", getExecutorName())
            .append("status", getStatus())
            .append("source", getSource())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .append("beforeImg", getBeforeImg())
            .append("afterImg", getAfterImg())
            .append("price", getPrice())
            .append("material", getMaterial())
            .append("maintenanceCompany", getMaintenanceCompany())
            .append("type", getType())
            .toString();
    }
}
