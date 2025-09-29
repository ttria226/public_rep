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
 * 上架策略详情对象 t_put_away_rule_detail
 *
 * @author wxr
 * @date 2023-05-06
 */
@TableName("t_put_away_rule_detail")
public class TPutAwayRuleDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 上架策略ID
     */
    @Excel(name = "上架策略ID")
    private Long ruleId;

    /**
     * 行号
     */
    @Excel(name = "行号")
    private Integer ruleOrder;

    /**
     * 收货单类型
     */
    @Excel(name = "收货单类型")
    private Long receiptType;

    /**
     * 是否启用（0启用，1不启用）
     */
    @Excel(name = "是否启用", readConverterExp = "0=启用，1不启用")
    private Long status;

    /**
     * 上架规则
     */
    @Excel(name = "上架规则")
    private Integer rule;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public Integer getRuleOrder() {
        return ruleOrder;
    }

    public void setRuleOrder(Integer ruleOrder) {
        this.ruleOrder = ruleOrder;
    }

    public void setReceiptType(Long receiptType) {
        this.receiptType = receiptType;
    }

    public Long getReceiptType() {
        return receiptType;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStatus() {
        return status;
    }

    public Integer getRule() {
        return rule;
    }

    public void setRule(Integer rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ruleId", getRuleId())
                .append("ruleOrder", getRuleOrder())
                .append("receiptType", getReceiptType())
                .append("status", getStatus())
                .append("rule", getRule())
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
