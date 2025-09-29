package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 波次计划详情对象 t_merge_delivery_detail
 *
 * @author zjj
 * @date 2023-06-25
 */
@TableName("t_merge_delivery_detail")
public class TMergeDeliveryDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 出库单标识 */
    @Excel(name = "出库单标识")
    private Long mergeDeliveryId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;

    /**  0  待出库   1 已出库  2 部分出库 */
    @Excel(name = " 0  待出库   1 已出库  2 部分出库")
    private String nextFlag;

    /** 本次预计数量 */
    @Excel(name = "本次预计数量")
    private BigDecimal predictCount;

    /** 预计实际数量 */
    @Excel(name = "预计实际数量")
    private BigDecimal predictReceiveCount;

    /** 实际拣货数量 */
    @Excel(name = "实际拣货数量")
    private BigDecimal receiveCount;

    /** 小件预计数量 */
    @Excel(name = "小件预计数量")
    private BigDecimal smallPredictCount;

    /** 小件实际拣货数量 */
    @Excel(name = "小件实际拣货数量")
    private BigDecimal smallReceiveCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMergeDeliveryId(Long mergeDeliveryId)
    {
        this.mergeDeliveryId = mergeDeliveryId;
    }

    public Long getMergeDeliveryId()
    {
        return mergeDeliveryId;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setNextFlag(String nextFlag)
    {
        this.nextFlag = nextFlag;
    }

    public String getNextFlag()
    {
        return nextFlag;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getPredictReceiveCount() {
        return predictReceiveCount;
    }

    public void setPredictReceiveCount(BigDecimal predictReceiveCount) {
        this.predictReceiveCount = predictReceiveCount;
    }

    public BigDecimal getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(BigDecimal receiveCount) {
        this.receiveCount = receiveCount;
    }

    public BigDecimal getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(BigDecimal smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public BigDecimal getSmallReceiveCount() {
        return smallReceiveCount;
    }

    public void setSmallReceiveCount(BigDecimal smallReceiveCount) {
        this.smallReceiveCount = smallReceiveCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mergeDeliveryId", getMergeDeliveryId())
                .append("materialId", getMaterialId())
                .append("nextFlag", getNextFlag())
                .append("predictCount", getPredictCount())
                .append("predictReceiveCount", getPredictReceiveCount())
                .append("receiveCount", getReceiveCount())
                .append("smallPredictCount", getSmallPredictCount())
                .append("smallReceiveCount", getSmallReceiveCount())
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
