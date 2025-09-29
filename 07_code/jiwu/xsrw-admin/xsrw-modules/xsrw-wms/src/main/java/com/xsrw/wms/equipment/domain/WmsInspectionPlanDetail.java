package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 巡检计划设备列对象 d_inspection_plan_detail
 * 
 * @author zjj
 * @date 2023-05-08
 */
@TableName("d_inspection_plan_detail")
public class WmsInspectionPlanDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联巡检计划id */
    @Excel(name = "关联巡检计划id")
    private Long planId;

    /** 关联巡检计划id */
    @Excel(name = "排序")
    private Integer sort;

    /** 设备id */
    @Excel(name = "设备id")
    private Long equipmentId;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String equNo;

    @TableField(exist = false)
    private String serialNo;

    @TableField(exist = false)
    private String functionLocation;

    @TableField(exist = false)
    private String content;

    /** 状态1：已完成 0：未开始 */
    @TableField(exist = false)
    private Integer status;

    /** 巡检标准 */
    @TableField(exist = false)
    private String inspectionItems;

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

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setEquipmentId(Long equipmentId) 
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId() 
    {
        return equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquNo() {
        return equNo;
    }

    public void setEquNo(String equNo) {
        this.equNo = equNo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getFunctionLocation() {
        return functionLocation;
    }

    public void setFunctionLocation(String functionLocation) {
        this.functionLocation = functionLocation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(String inspectionItems) {
        this.inspectionItems = inspectionItems;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("equipmentId", getEquipmentId())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
