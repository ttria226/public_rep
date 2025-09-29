package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库发货退货单对象 t_out_removal
 *
 * @author zjj
 * @date 2023-06-05
 */
@TableName("t_out_removal")
public class TOutRemoval extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 源单id
     */
    @Excel(name = "源单id")
    private Long originId;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    @TableField(exist = false)
    private String code;
    @TableField(exist = false)
    private String creator;

    @TableField(exist = false)
    private String newLocal;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public String getNewLocal() {
        return newLocal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("originId", getOriginId())
                .append("status", getStatus())
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
