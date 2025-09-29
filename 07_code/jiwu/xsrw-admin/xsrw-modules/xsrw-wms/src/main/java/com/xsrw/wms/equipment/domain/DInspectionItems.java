package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 巡检标准对象 d_inspection_items
 *
 * @author zjj
 * @date 2023-05-08
 */
@TableName("d_inspection_items")
public class DInspectionItems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标准名称 */
    @Excel(name = "标准名称")
    private String name;

    /** 巡检编号 */
    @Excel(name = "巡检编号")
    private String itemNo;

    /** 所属部门id */
    @Excel(name = "所属部门id")
    private Long depId;

    /** 所属部门名称 */
    @Excel(name = "所属部门名称")
    private String depName;

    /** 设备id */
    @Excel(name = "设备id")
    private String equipmentId;

    @Excel(name = "1:启用 0：作废")
    private Integer status;

    @Excel(name = "设备名称")
    @TableField(exist = false)
    private String equipmentName;

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
    public void setItemNo(String itemNo)
    {
        this.itemNo = itemNo;
    }

    public String getItemNo()
    {
        return itemNo;
    }
    public void setDepId(Long depId)
    {
        this.depId = depId;
    }

    public Long getDepId()
    {
        return depId;
    }
    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getDepName()
    {
        return depName;
    }
    public void setEquipmentId(String equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentId()
    {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("itemNo", getItemNo())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .append("depId", getDepId())
            .append("depName", getDepName())
            .append("equipmentId", getEquipmentId())
            .toString();
    }
}
