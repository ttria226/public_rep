package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 入库登记对象 t_advance_registration
 *
 * @author wxr
 * @date 2023-05-09
 */
@TableName("t_advance_registration")
public class TAdvanceRegistration extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 入库单详情标识
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
     * 计划数量
     */
    @Excel(name = "数量", sort = 5)
    private Long predictCount;

    /**
     * 实际数量
     */
//    @Excel(name = "实际数量")
    private Long actualCount;

    /**
     * 状态(1未完成，2进行中 3已完成 )
     */
    @Excel(name = "状态", sort = 7, readConverterExp = "1=未完成,2=部分完成,3=全部完成")
    private String status;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 3)
    private String batchCode;
    /**
     * 转化状态
     */
    private String nextFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAdvanceDeliveryId(Long advanceDeliveryId) {
        this.advanceDeliveryId = advanceDeliveryId;
    }

    public Long getAdvanceDeliveryId() {
        return advanceDeliveryId;
    }

    public Long getAdvanceDeliveryDetailId() {
        return advanceDeliveryDetailId;
    }

    public void setAdvanceDeliveryDetailId(Long advanceDeliveryDetailId) {
        this.advanceDeliveryDetailId = advanceDeliveryDetailId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setActualCount(Long actualCount) {
        this.actualCount = actualCount;
    }

    public Long getActualCount() {
        return actualCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("advanceDeliveryId", getAdvanceDeliveryId())
                .append("advanceDeliveryDetailId", getAdvanceDeliveryDetailId())
                .append("materialId", getMaterialId())
                .append("predictCount", getPredictCount())
                .append("actualCount", getActualCount())
                .append("status", getStatus())
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
