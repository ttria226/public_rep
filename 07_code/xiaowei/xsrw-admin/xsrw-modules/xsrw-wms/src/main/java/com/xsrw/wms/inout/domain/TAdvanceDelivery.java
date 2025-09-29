package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 入库单对象 t_advance_delivery
 *
 * @author wxr
 * @date 2023-05-08
 */
@TableName("t_advance_delivery")
public class TAdvanceDelivery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编号
     */
    @Excel(name = "入库单号", sort = 1)
    private String code;

    /**
     * 源单单号
     */
//    @Excel(name = "源单单号")
    private String originCode;

    /**
     * 源单日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "源单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date originDate;

    /**
     * 源单内容
     */
//    @Excel(name = "源单内容")
    private String originData;

    /**
     * 单据类型字典（0ERP 1采购收货入库 2生产产品入库  3领用退还入库 4借货入库 5借出还入）
     */
    @Excel(name = "入库类型", readConverterExp = "0=ERP;1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入,6=系统生成", sort = 1)
    private String type;

    /**
     * 订单状态（1待审核，2.审核通过  3.已检测 4.已登记 5.部分登记 9.审核不通过）
     */
    @Excel(name = "状态", sort = 4, readConverterExp = "1=待审核,2=审核通过,3=已检测,4=已收货,5=部分收货,6=部分退货,7=全部退货,9=已作废,10=部分上架,11=已上架")
    private String status;

    /**
     * 审核人
     */
    @Excel(name = "审核人", sort = 6)
    private String auditor;

    /** 审核备注 */
    private String auditRemark;

    /**
     * 来源字典（1.本地创建 2.erp接口 3.调拨单）
     */
    @Excel(name = "来源", sort = 3, readConverterExp = "1=本地创建,2=erp接口,3=调拨单")
    private String newLocal;

    /**
     * 完成状态（未完成 部分完成  已完成）
     */
//    @Excel(name = "完成状态", sort = 5, readConverterExp = "1=未完成,2=部分完成,3=已完成")
    private String completeState;

    /**
     * 是否转化为质检单(0否 1是)
     */
    private String qualityStatus;

    /**
     * 是否转化为入库单(0否 1是)
     */
    private String putStatus;
    /** bom标识 */
    private Long bomId;
    /**
     * bom套数
     */
    private Long bomCount;

    /** 单据来源 */
    private String deliveryModule;

    /**
     * 计划交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planDate;

    /**
     * 实际交货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date predictDate;


    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Date getPredictDate() {
        return predictDate;
    }

    public void setPredictDate(Date predictDate) {
        this.predictDate = predictDate;
    }

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

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public String getNewLocal() {
        return newLocal;
    }

    public String getCompleteState() {
        return completeState;
    }

    public void setCompleteState(String completeState) {
        this.completeState = completeState;
    }

    public String getQualityStatus() {
        return qualityStatus;
    }

    public void setQualityStatus(String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    public String getPutStatus() {
        return putStatus;
    }

    public void setPutStatus(String putStatus) {
        this.putStatus = putStatus;
    }

    public Long getBomId() {
        return bomId;
    }

    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }

    public Long getBomCount() {
        return bomCount;
    }

    public void setBomCount(Long bomCount) {
        this.bomCount = bomCount;
    }

    public String getDeliveryModule() {
        return deliveryModule;
    }

    public void setDeliveryModule(String deliveryModule) {
        this.deliveryModule = deliveryModule;
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
                .toString();
    }
}
