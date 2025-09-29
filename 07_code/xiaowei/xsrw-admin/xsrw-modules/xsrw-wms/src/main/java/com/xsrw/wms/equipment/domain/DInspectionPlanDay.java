package com.xsrw.wms.equipment.domain;

import java.sql.Time;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 巡检记录对象 d_inspection_plan_day
 *
 * @author zjj
 * @date 2023-05-09
 */
@TableName("d_inspection_plan_day")
public class DInspectionPlanDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联计划id */
    @Excel(name = "关联计划id")
    private Long planId;

    /** 巡检日期 */
    @Excel(name = "巡检日期")
    private String day;

    /** 巡检开始日期-查询用 */
    @Excel(name = "巡检开始日期-查询用")
    @TableField(exist = false)
    private String dayBegin;

    /** 巡检结束日期-查询用 */
    @Excel(name = "巡检结束日期-查询用")
    @TableField(exist = false)
    private String dayEnd;

    /** 巡检开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "巡检开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Time inspectionStartTime;

    /** 巡检结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "巡检结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Time inspectionEndTime;

    /** 计划巡检员 */
    @Excel(name = "计划巡检员")
    private Long inspector;

    @Excel(name = "计划巡检员名称")
    private String inspectorName;

    /** 实际巡检员 */
    @Excel(name = "实际巡检员")
    private Long inspectorTrue;

    /** 实际巡检员 */
    @Excel(name = "实际巡检员")
    private String inspectorTrueName;

    /** 调班原因 */
    @Excel(name = "调班原因")
    private String reason;

    /** 状态 */
    @Excel(name = "状态",readConverterExp = "0_未完成,1_已完成,2_进行中")
    private Integer status;

    /** 巡检方式 */
    @Excel(name = "巡检方式1：现场拍照（默认定位） 2：现场定位")
    private Integer signType;

    /** 巡检计划名称 */
    @Excel(name = "巡检计划名称")
    @TableField(exist = false)
    private String planName;

    @Excel(name = "计划巡检线路")
    @TableField(exist = false)
    private String inspectionLine;

    /** 是否报修1：是 0：否 */
    @TableField(exist = false)
    private Integer isrepair;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setPlanId(Long planId)
    {
        this.planId = planId;
    }

    public Long getPlanId()
    {
        return planId;
    }
    public void setDay(String day)
    {
        this.day = day;
    }

    public String getDay()
    {
        return day;
    }
    public void setInspectionStartTime(Time inspectionStartTime)
    {
        this.inspectionStartTime = inspectionStartTime;
    }

    public Time getInspectionStartTime()
    {
        return inspectionStartTime;
    }
    public void setInspectionEndTime(Time inspectionEndTime)
    {
        this.inspectionEndTime = inspectionEndTime;
    }

    public Time getInspectionEndTime()
    {
        return inspectionEndTime;
    }
    public void setInspector(Long inspector)
    {
        this.inspector = inspector;
    }

    public Long getInspector()
    {
        return inspector;
    }

    public void setInspectorName(String inspectorName)
    {
        this.inspectorName = inspectorName;
    }

    public String getInspectorName()
    {
        return inspectorName;
    }


    public void setInspectorTrue(Long inspectorTrue)
    {
        this.inspectorTrue = inspectorTrue;
    }

    public Long getInspectorTrue()
    {
        return inspectorTrue;
    }

    public void setInspectorTrueName(String inspectorTrueName)
    {
        this.inspectorTrueName = inspectorTrueName;
    }

    public String getInspectorTrueName()
    {
        return inspectorTrueName;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setSignType(Integer signType)
    {
        this.signType = signType;
    }

    public Integer getSignType()
    {
        return signType;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getInspectionLine() {
        return inspectionLine;
    }

    public void setInspectionLine(String inspectionLine) {
        this.inspectionLine = inspectionLine;
    }

    public String getDayBegin() {
        return dayBegin;
    }

    public void setDayBegin(String dayBegin) {
        this.dayBegin = dayBegin;
    }

    public String getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(String dayEnd) {
        this.dayEnd = dayEnd;
    }

    public Integer getIsrepair() {
        return isrepair;
    }

    public void setIsrepair(Integer isrepair) {
        this.isrepair = isrepair;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("day", getDay())
            .append("inspectionStartTime", getInspectionStartTime())
            .append("inspectionEndTime", getInspectionEndTime())
            .append("inspector", getInspector())
            .append("inspectorName", getInspectorName())
            .append("inspectorTrue", getInspectorTrue())
            .append("inspectorTrueName", getInspectorTrueName())
            .append("reason", getReason())
            .append("status", getStatus())
            .append("signType", getSignType())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
