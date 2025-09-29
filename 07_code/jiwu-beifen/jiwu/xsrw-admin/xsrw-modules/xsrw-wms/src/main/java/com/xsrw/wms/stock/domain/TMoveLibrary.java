package com.xsrw.wms.stock.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 库内移位对象 t_move_library
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_move_library")
public class TMoveLibrary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 移库编码 */
    @Excel(name = "移库编码")
    private String code;

    /** 转出库位ID */
    @Excel(name = "转出库位ID")
    private Long locationOutId;

    /** 转入库位ID */
    @Excel(name = "转入库位ID")
    private Long locationInId;

    /** 托盘 */
    @Excel(name = "托盘")
    private Long trayId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 审核状态 0未审核 1已审核 */
    @Excel(name = "审核状态 0未审核 1已审核")
    private String auditorStatus;

    /** 审核人 */
    @Excel(name = "审核人")
    private String auditor;

    /** 审核人名称 */
    @Excel(name = "审核人名称")
    private String auditorName;

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
    public void setLocationOutId(Long locationOutId)
    {
        this.locationOutId = locationOutId;
    }

    public Long getLocationOutId()
    {
        return locationOutId;
    }
    public void setLocationInId(Long locationInId)
    {
        this.locationInId = locationInId;
    }

    public Long getLocationInId()
    {
        return locationInId;
    }
    public void setTrayId(Long trayId)
    {
        this.trayId = trayId;
    }

    public Long getTrayId()
    {
        return trayId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setAuditorStatus(String auditorStatus)
    {
        this.auditorStatus = auditorStatus;
    }

    public String getAuditorStatus()
    {
        return auditorStatus;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAuditor()
    {
        return auditor;
    }
    public void setAuditorName(String auditorName)
    {
        this.auditorName = auditorName;
    }

    public String getAuditorName()
    {
        return auditorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("locationOutId", getLocationOutId())
            .append("locationInId", getLocationInId())
            .append("trayId", getTrayId())
            .append("status", getStatus())
            .append("auditorStatus", getAuditorStatus())
            .append("auditor", getAuditor())
            .append("auditorName", getAuditorName())
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
