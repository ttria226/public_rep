package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 经验库分类树对象 d_equipment_tree
 *
 * @author zjj
 * @date 2023-05-11
 */
@TableName("d_equipment_tree")
public class DEquipmentTree extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 父id */
    @Excel(name = "父id")
    private Long pid;

    List<DEquipmentTree> childList;



    /** 类型 */
    @Excel(name = "类型")
    private Long type;

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
    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public Long getPid()
    {
        return pid;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }

    public List<DEquipmentTree> getChildList() {
        return childList;
    }

    public void setChildList(List<DEquipmentTree> childList) {
        this.childList = childList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("pid", getPid())
            .append("type", getType())
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
