package com.xsrw.wms.equipment.domain;

import java.sql.Time;
import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 巡检计划对象 d_inspection_plan
 *
 * @author zjj
 * @date 2023-05-08
 */
@TableName("d_inspection_plan")
public class WmsInspectionPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 计划名称 */
    @Excel(name = "计划名称")
    private String name;

    /** 计划巡检线路 */
    @Excel(name = "计划巡检线路")
    private String inspectionLine;

    /** 巡检周期类型 1：周 2：按月 */
    @Excel(name = "巡检周期类型 1：周 2：按月")
    private Integer type;

    @Excel(name = "巡检方式1：现场拍照（默认定位） 2：现场定位")
    private Integer signType;

    @Excel(name = "1：未启用 2：已启用 0：已作废")
    private Integer status;

    @Excel(name = "所选日期")
    private String monthOrDay;

    @Excel(name = "年月")
    private String yearOrMonth;


    /** 计划开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartTime;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndTime;

    /** 巡检开始时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "巡检开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Time inspectionStartTime;

    /** 巡检结束时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "巡检结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Time inspectionEndTime;

    /** 巡检员 */
    @Excel(name = "巡检员")
    private Long inspector;

    /** 巡检员 */
    @Excel(name = "巡检员")
    private String inspectorName;



    /** 巡检计划设备列信息 */
    @TableField(exist = false)
    private List<WmsInspectionPlanDetail> wmsInspectionPlanDetailList;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setInspectionLine(String inspectionLine)
    {
        this.inspectionLine = inspectionLine;
    }

    public String getInspectionLine()
    {
        return inspectionLine;
    }

    public void setmonthOrDay(String monthOrDay)
    {
        this.monthOrDay = monthOrDay;
    }

    public String getmonthOrDay()
    {
        return monthOrDay;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setSignType(Integer signType)
    {
        this.signType = signType;
    }

    public Integer getSignType()
    {
        return signType;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
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

    public List<WmsInspectionPlanDetail> getWmsInspectionPlanDetailList()
    {
        return wmsInspectionPlanDetailList;
    }

    public void setWmsInspectionPlanDetailList(List<WmsInspectionPlanDetail> wmsInspectionPlanDetailList)
    {
        this.wmsInspectionPlanDetailList = wmsInspectionPlanDetailList;
    }

    public String getYearOrMonth() {
        return yearOrMonth;
    }

    public void setYearOrMonth(String yearOrMonth) {
        this.yearOrMonth = yearOrMonth;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("inspectionLine", getInspectionLine())
            .append("monthOrDay", getmonthOrDay())
            .append("type", getType())
            .append("status", getStatus())
            .append("signType",getSignType())
            .append("planStartTime", getPlanStartTime())
            .append("planEndTime", getPlanEndTime())
            .append("inspectionStartTime", getInspectionStartTime())
            .append("inspectionEndTime", getInspectionEndTime())
            .append("inspector", getInspector())
            .append("inspectorName", getInspectorName())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .append("wmsInspectionPlanDetailList", getWmsInspectionPlanDetailList())
            .toString();
    }
}
