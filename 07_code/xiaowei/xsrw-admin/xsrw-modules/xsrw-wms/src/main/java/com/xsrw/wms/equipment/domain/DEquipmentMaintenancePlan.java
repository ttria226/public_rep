package com.xsrw.wms.equipment.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 设备保养计划对象 d_equipment_maintenance_plan
 *
 * @author zjj
 * @date 2023-05-11
 */
@TableName("d_equipment_maintenance_plan")
public class DEquipmentMaintenancePlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @TableField(exist = false)
    private String equName;
    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备id */
    private Long equipmentId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String equNo;

    /** 配件名称 */
    private String partName;



    /** 计划名称 */
    @Excel(name = "计划名称")
    private String planName;

    /** 计划类型 1：固定时间循环 2：单次计划 */
    private Integer planType;

    /** 保养间隔 */
    private Integer intervalDay;

    /** 计划开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartTime;

    /** 计划结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndTime;

    /** 计划保养内容 */

    private String content;

    /** 保养类型1：一级保养 */
    private Long maintenanceType;

    /** 是否外部保养1：是 0：否 */
    private Long isExternal;

    /** 外部保养公司 */
    private String externalCompany;

    /** 1：未启用 2：已启用 0：已作废 */
    @Excel(name = "计划状态",readConverterExp="0=已作废,1=未启用,2=已启用")
    private Integer status;

    private Integer type;

    private Long companyId;

    /** 执行人id */
    private Long executorId;

    /** 规格型号 */
    @TableField(exist = false)
    private String model;



    @TableField(exist = false)
    List<maintenancePlanVO> planVOList;

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
    public void setEquNo(String equNo)
    {
        this.equNo = equNo;
    }

    public String getEquNo()
    {
        return equNo;
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
    public void setPlanName(String planName)
    {
        this.planName = planName;
    }

    public String getPlanName()
    {
        return planName;
    }
    public void setPlanType(Integer planType)
    {
        this.planType = planType;
    }

    public Integer getPlanType()
    {
        return planType;
    }
    public void setIntervalDay(Integer intervalDay)
    {
        this.intervalDay = intervalDay;
    }

    public Integer getIntervalDay()
    {
        return intervalDay;
    }
    public void setPlanStartTime(Date planStartTime)
    {
        this.planStartTime = planStartTime;
    }

    public Date getPlanStartTime()
    {
        return planStartTime;
    }
    public void setPlanEndTime(Date planEndTime)
    {
        this.planEndTime = planEndTime;
    }

    public Date getPlanEndTime()
    {
        return planEndTime;
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
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getEquName() {
        return equName;
    }

    public void setEquName(String equName) {
        this.equName = equName;
    }

    public List<maintenancePlanVO> getPlanVOList() {
        return planVOList;
    }

    public void setPlanVOList(List<maintenancePlanVO> planVOList) {
        this.planVOList = planVOList;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("equNo", getEquNo())
            .append("partName", getPartName())
            .append("content", getContent())
            .append("planName", getPlanName())
            .append("planType", getPlanType())
            .append("interval", getIntervalDay())
            .append("planStartTime", getPlanStartTime())
            .append("planEndTime", getPlanEndTime())
            .append("maintenanceType", getMaintenanceType())
            .append("isExternal", getIsExternal())
            .append("externalCompany", getExternalCompany())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .append("type", getType())
            .toString();
    }
}
