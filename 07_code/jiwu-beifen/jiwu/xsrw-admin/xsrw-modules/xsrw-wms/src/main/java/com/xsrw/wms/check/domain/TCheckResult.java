package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 盘点差异对象 t_check_result
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_check_result")
public class TCheckResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 盘点计划详情标识 */
//    @Excel(name = "盘点计划详情标识")
    private Long taskDetailId;

    /** 订单状态(1审核通过 0未通过 ) */
//    @Excel(name = "订单状态(1审核通过 0未通过 )")
    private String status;

    /** 审核人 */
//    @Excel(name = "审核人")
    private String auditor;

    /** 盘点差异数量 */
//    @Excel(name = "盘点差异数量")
    private Long checkDifferenceCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTaskDetailId(Long taskDetailId)
    {
        this.taskDetailId = taskDetailId;
    }

    public Long getTaskDetailId()
    {
        return taskDetailId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAuditor()
    {
        return auditor;
    }
    public void setCheckDifferenceCount(Long checkDifferenceCount)
    {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public Long getCheckDifferenceCount()
    {
        return checkDifferenceCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskDetailId", getTaskDetailId())
            .append("status", getStatus())
            .append("auditor", getAuditor())
            .append("checkDifferenceCount", getCheckDifferenceCount())
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
