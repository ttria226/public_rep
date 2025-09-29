package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 拒收管理对象 t_rejection_detail
 *
 * @author wxr
 * @date 2023-05-09
 */
@TableName("t_rejection_detail")
public class TRejectionDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单标识
     */
    private Long advanceDeliveryId;

    /**
     * 入库单详情标识
     */
    private Long advanceDeliveryDetailId;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 拒收数量
     */
    @Excel(name = "数量", sort = 5)
    private Long rejectionCount;

    /**
     * 检测失败类型字典（1.外形错误，2.其他原因）
     */
    @Excel(name = "原因", readConverterExp = "1=外形不合格,2=破损,3=其他", sort = 7)
    private String rejectionFailType;

    /**
     * 检测失败备注
     */
    @Excel(name = "备注", sort = 8)
    private String rejectionFailRemark;

    /**
     * 批次号
     */
    @Excel(name = "批次", sort = 4)
    private String batchCode;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getAdvanceDeliveryId() {
        return advanceDeliveryId;
    }

    public void setAdvanceDeliveryId(Long advanceDeliveryId) {
        this.advanceDeliveryId = advanceDeliveryId;
    }

    public void setAdvanceDeliveryDetailId(Long advanceDeliveryDetailId) {
        this.advanceDeliveryDetailId = advanceDeliveryDetailId;
    }

    public Long getAdvanceDeliveryDetailId() {
        return advanceDeliveryDetailId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setRejectionCount(Long rejectionCount) {
        this.rejectionCount = rejectionCount;
    }

    public Long getRejectionCount() {
        return rejectionCount;
    }

    public void setRejectionFailType(String rejectionFailType) {
        this.rejectionFailType = rejectionFailType;
    }

    public String getRejectionFailType() {
        return rejectionFailType;
    }

    public void setRejectionFailRemark(String rejectionFailRemark) {
        this.rejectionFailRemark = rejectionFailRemark;
    }

    public String getRejectionFailRemark() {
        return rejectionFailRemark;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("advanceDeliveryId", getAdvanceDeliveryId())
                .append("advanceDeliveryDetailId", getAdvanceDeliveryDetailId())
                .append("materialId", getMaterialId())
                .append("rejectionCount", getRejectionCount())
                .append("rejectionFailType", getRejectionFailType())
                .append("rejectionFailRemark", getRejectionFailRemark())
                .append("batchCode", getBatchCode())
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
