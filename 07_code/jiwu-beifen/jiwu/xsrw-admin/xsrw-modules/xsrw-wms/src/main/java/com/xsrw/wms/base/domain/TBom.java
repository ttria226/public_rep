package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * bom对象 t_bom
 *
 * @author zjj
 * @date 2023-06-10
 */
@TableName("t_bom")
public class TBom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 名称 */
    @Excel(name = "名称")
    private String name;


    @TableField(exist = false)
    private List<TBomDetail> bomDetails;

    @TableField(exist = false)
    @Excel(name = "价格（元）")
    private Long sumPrice;

    @TableField(exist = false)
    @Excel(name = "重量（kg）")
    private Long sumWeight;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createBy;

    /** 部门名称 */
    @Excel(name = "部门")
    private String deptName;

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Long sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Long getSumWeight() {
        return sumWeight;
    }

    public void setSumWeight(Long sumWeight) {
        this.sumWeight = sumWeight;
    }

    public List<TBomDetail> getBomDetails() {
        return bomDetails;
    }

    public void setBomDetails(List<TBomDetail> bomDetails) {
        this.bomDetails = bomDetails;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
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
