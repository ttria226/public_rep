package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 越库单详情对象 t_overstock_delivery_detail
 *
 * @author wxr
 * @date 2023-06-25
 */
@TableName("t_overstock_delivery_detail")
public class TOverstockDeliveryDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 越库单标识
     */
    @Excel(name = "越库单标识")
    private Long overDeliveryId;

    /**
     * 物料标识
     */
    @Excel(name = "物料标识")
    private Long materialId;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    /**
     * 转化状态
     */
    @Excel(name = "转化状态")
    private String nextFlag;

    /**
     * 本次预计数量
     */
    @Excel(name = "本次预计数量")
    private Long predictCount;

    /**
     * 收货数量
     */
    @Excel(name = "收货数量")
    private Long registrationCount;

    /**
     * 实际出库数量
     */
    @Excel(name = "实际出库数量")
    private Long receiveCount;

    /**
     * 出库库位id
     */
    @Excel(name = "出库库位id")
    private Long locationId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOverDeliveryId(Long overDeliveryId) {
        this.overDeliveryId = overDeliveryId;
    }

    public Long getOverDeliveryId() {
        return overDeliveryId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setRegistrationCount(Long registrationCount) {
        this.registrationCount = registrationCount;
    }

    public Long getRegistrationCount() {
        return registrationCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("overDeliveryId", getOverDeliveryId())
                .append("materialId", getMaterialId())
                .append("batchCode", getBatchCode())
                .append("nextFlag", getNextFlag())
                .append("predictCount", getPredictCount())
                .append("registrationCount", getRegistrationCount())
                .append("receiveCount", getReceiveCount())
                .append("locationId", getLocationId())
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
