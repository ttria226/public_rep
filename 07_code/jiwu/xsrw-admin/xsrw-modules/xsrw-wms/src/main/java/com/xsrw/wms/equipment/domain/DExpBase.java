package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 保养/维修经验库对象 d_exp_base
 *
 * @author zjj
 * @date 2023-05-11
 */
@TableName("d_exp_base")
public class DExpBase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 类型1：保养 2：维修 */
    private Long baseType;

    /** $column.columnComment */
    private Long type;

    /** 所属分类id */
    private Long treeId;

    /** 详情 */
    @Excel(name = "详情")
    private String content;

    /** 文件链接 */
    @Excel(name = "文件链接")
    private String fileLink;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setBaseType(Long baseType)
    {
        this.baseType = baseType;
    }

    public Long getBaseType()
    {
        return baseType;
    }
    public void setType(Long type)
    {
        this.type = type;
    }

    public Long getType()
    {
        return type;
    }
    public void setTreeId(Long treeId)
    {
        this.treeId = treeId;
    }

    public Long getTreeId()
    {
        return treeId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setFileLink(String fileLink)
    {
        this.fileLink = fileLink;
    }

    public String getFileLink()
    {
        return fileLink;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("baseType", getBaseType())
            .append("type", getType())
            .append("treeId", getTreeId())
            .append("content", getContent())
            .append("fileLink", getFileLink())
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
