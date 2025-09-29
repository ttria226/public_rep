package com.xsrw.wms.inout.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 越库单对象 t_overstock_delivery
 *
 * @author wxr
 * @date 2023-06-25
 */
@TableName("t_overstock_delivery")
public class TOverstockDelivery extends BaseEntity {
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
     * 源单单号
     */
    @Excel(name = "源单单号")
    private String originCode;

    /**
     * 源单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "源单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date originDate;

    /**
     * 源单内容
     */
    @Excel(name = "源单内容")
    private String originData;

    /**
     * 单据类型
     */
    @Excel(name = "单据类型")
    private String type;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String status;

    /**
     * 审核人
     */
    @Excel(name = "审核人")
    private String auditor;

    /**
     * 来源字典（1.本地创建 2.erp接口 3.调拨单）
     */
    @Excel(name = "来源字典", readConverterExp = "1=.本地创建,2=.erp接口,3=.调拨单")
    private String newLocal;

    /**
     * 完成状态（未完成 部分完成  已完成）
     */
    @Excel(name = "完成状态", readConverterExp = "未=完成,部=分完成,已=完成")
    private String completeState;

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

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public Date getOriginDate() {
        return originDate;
    }

    public void setOriginData(String originData) {
        this.originData = originData;
    }

    public String getOriginData() {
        return originData;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public String getNewLocal() {
        return newLocal;
    }

    public void setCompleteState(String completeState) {
        this.completeState = completeState;
    }

    public String getCompleteState() {
        return completeState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("originCode", getOriginCode())
                .append("originDate", getOriginDate())
                .append("originData", getOriginData())
                .append("type", getType())
                .append("status", getStatus())
                .append("auditor", getAuditor())
                .append("newLocal", getNewLocal())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("delFlag", getDelFlag())
                .append("completeState", getCompleteState())
                .toString();
    }
}
