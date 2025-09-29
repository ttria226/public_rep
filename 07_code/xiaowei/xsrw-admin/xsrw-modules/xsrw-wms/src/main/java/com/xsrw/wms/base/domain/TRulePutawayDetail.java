package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 上架规则详情对象 t_rule_putaway_detail
 *
 * @author wxr
 * @date 2023-06-13
 */
@TableName("t_rule_putaway_detail")
public class TRulePutawayDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上架规则id
     */
    @Excel(name = "上架规则id")
    private Long rulePutawayId;

    /**
     * 库位id
     */
    @Excel(name = "库位id")
    private Long locationId;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setRulePutawayId(Long rulePutawayId) {
        this.rulePutawayId = rulePutawayId;
    }

    public Long getRulePutawayId() {
        return rulePutawayId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("rulePutawayId", getRulePutawayId())
                .append("locationId", getLocationId())
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
