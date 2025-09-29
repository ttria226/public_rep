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
 * 规则对象 t_rule
 *
 * @author wxr
 * @date 2023-06-12
 */
@TableName("t_rule")
public class TRule extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编号
     */
    @Excel(name = "编号")
    private String code;

    /**
     * 规则分类（有效期 分配 拣选 补货 波次 超收 出库 入库）
     */
    @Excel(name = "规则分类", readConverterExp = "有=效期,分=配,拣=选,补=货,波=次,超=收,出=库,入=库")
    private String ruleModule;

    /**
     * 是否启用
     */
    @Excel(name = "是否启用")
    private String flag;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 规则类型
     */
    @Excel(name = "规则类型")
    private String ruleType;

    /**
     * 优先级
     */
    @Excel(name = "优先级")
    private Long priority;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setRuleModule(String ruleModule) {
        this.ruleModule = ruleModule;
    }

    public String getRuleModule() {
        return ruleModule;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("ruleModule", getRuleModule())
                .append("flag", getFlag())
                .append("name", getName())
                .append("ruleType", getRuleType())
                .append("priority", getPriority())
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
