package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 编码配置对象 t_code_config
 *
 * @author wxr
 * @date 2023-05-05
 */
@TableName("t_code_config")
public class TCodeConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.AUTO)
    @Excel(name = "编号")
    private Long id;

    /**
     * 模块名：中文名称
     */
    private String type;

    /**
     * 唯一标识
     */
    @Excel(name = "编码类型")
    private String typeCode;

    /**
     * 规则的前缀，模块的编码
     */
    @Excel(name = "前缀编码")
    private String beforeCode;

    /**
     * 中间的规则（1,日期（年月日）2,日期（年月日时分秒） 3,时间戳 ）
     */
    @Excel(name = "中间日期",readConverterExp = "0=无,1=日期(年月日),2=日期(年月日时分秒),3=时间戳")
    private String middleDate;

    /**
     * 最后的规则（1,自增长 2,随机数）
     */
    @Excel(name = "后缀", readConverterExp = "0=无,1=自增长,2=随机数")
    private String afterNumberType;

    /**
     * 规则值（自增长位数或者随机位数）
     */
    @Excel(name = "后缀位数")
    private String ruleValue;

    /**
     * 当前序号值
     */
    @TableField(exist = false)
    private Long currentIndex;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setBeforeCode(String beforeCode) {
        this.beforeCode = beforeCode;
    }

    public String getBeforeCode() {
        return beforeCode;
    }

    public void setMiddleDate(String middleDate) {
        this.middleDate = middleDate;
    }

    public String getMiddleDate() {
        return middleDate;
    }

    public void setAfterNumberType(String afterNumberType) {
        this.afterNumberType = afterNumberType;
    }

    public String getAfterNumberType() {
        return afterNumberType;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public Long getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(Long currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("type", getType())
                .append("typeCode", getTypeCode())
                .append("beforeCode", getBeforeCode())
                .append("middleDate", getMiddleDate())
                .append("afterNumberType", getAfterNumberType())
                .append("ruleValue", getRuleValue())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
