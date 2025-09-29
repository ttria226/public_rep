package com.xsrw.wms.inout.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 入库单详情对象 t_advance_delivery_detail
 *
 * @author wxr
 * @date 2023-05-08
 */
@TableName("t_advance_delivery_detail")
public class TAdvanceDeliveryDetail extends BaseEntity {
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
     * 物料标识
     */
    private Long materialId;

    /**
     * 登记状态
     */
    @Excel(name = "状态", sort = 7, readConverterExp = "0=未完成,1=已检测,2=已上架,3=部分执行,4=全部执行")
    private String nextFlag;

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

    /**
     * 本次预计数量
     */
    private Long predictCount;

    /**
     * 实际收货数量
     */
    private Long receiveCount;

    /**
     * 检测数量
     */
    @Excel(name = "入库数量", sort = 6)
    private Long detectionCount;

    /**
     * 检测失败类型字典（1.外形错误，2.其他原因）
     */
    private String detectionFailType;

    /**
     * 检测失败备注
     */
    private String detectionFailRemark;

    /**
     * 登记数量
     */
    @Excel(name = "登记数量", sort = 5)
    private Long registrationCount;

    /**
     * 已上架数量
     */
    private Long putawayCount;

    /**
     * 生产日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date producedDate;

    /**
     * 到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 3)
    private String batchCode;

    /**
     * rfid生成使用
     */
    @TableField(exist = false)
    private String rfidBatchCode;

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

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPredictDate(Date predictDate) {
        this.predictDate = predictDate;
    }

    public Date getPredictDate() {
        return predictDate;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setDetectionCount(Long detectionCount) {
        this.detectionCount = detectionCount;
    }

    public Long getDetectionCount() {
        return detectionCount;
    }

    public void setDetectionFailType(String detectionFailType) {
        this.detectionFailType = detectionFailType;
    }

    public String getDetectionFailType() {
        return detectionFailType;
    }

    public void setDetectionFailRemark(String detectionFailRemark) {
        this.detectionFailRemark = detectionFailRemark;
    }

    public String getDetectionFailRemark() {
        return detectionFailRemark;
    }

    public void setRegistrationCount(Long registrationCount) {
        this.registrationCount = registrationCount;
    }

    public Long getRegistrationCount() {
        return registrationCount;
    }

    public Long getPutawayCount() {
        return putawayCount;
    }

    public void setPutawayCount(Long putawayCount) {
        this.putawayCount = putawayCount;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }


    public String getRfidBatchCode() {
        return rfidBatchCode;
    }

    public void setRfidBatchCode(String rfidBatchCode) {
        this.rfidBatchCode = rfidBatchCode;
    }


    @Override
    public String toString() {
        return "TAdvanceDeliveryDetail{" +
                "id=" + id +
                ", advanceDeliveryId=" + advanceDeliveryId +
                ", materialId=" + materialId +
                ", nextFlag='" + nextFlag + '\'' +
                ", planDate=" + planDate +
                ", predictDate=" + predictDate +
                ", predictCount=" + predictCount +
                ", receiveCount=" + receiveCount +
                ", detectionCount=" + detectionCount +
                ", detectionFailType='" + detectionFailType + '\'' +
                ", detectionFailRemark='" + detectionFailRemark + '\'' +
                ", registrationCount=" + registrationCount +
                ", putawayCount=" + putawayCount +
                ", producedDate=" + producedDate +
                ", expireDate=" + expireDate +
                ", batchCode='" + batchCode + '\'' +
                ", rfidBatchCode='" + rfidBatchCode + '\'' +
                '}';
    }
}
