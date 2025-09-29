package com.xsrw.wms.inout.domain;

import java.math.BigDecimal;
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
    private BigDecimal predictCount;

    /**
     * 实际收货数量
     */
    private BigDecimal receiveCount;

    /**
     * 检测数量
     */
    @Excel(name = "入库数量", sort = 6)
    private BigDecimal detectionCount;

    /**
     * 检测失败类型字典（1.外形错误，2.其他原因）
     */
    private String detectionFailType;

    /**
     * 检测失败备注
     */
    private String detectionFailRemark;


    /**
     * 检测失败状态（0未检测，1检测成功，2检测失败）
     */
    private String detectionFailStatus;

    /**
     * 登记数量
     */
    @Excel(name = "登记数量", sort = 5)
    private BigDecimal registrationCount;

    /**
     * 已上架数量
     */
    private BigDecimal putawayCount;

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
     * 行项目
     */
    private String tbpos;

    /**
     * rfid生成使用
     */
    @TableField(exist = false)
    private String rfidBatchCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvanceDeliveryId() {
        return advanceDeliveryId;
    }

    public void setAdvanceDeliveryId(Long advanceDeliveryId) {
        this.advanceDeliveryId = advanceDeliveryId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

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

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(BigDecimal receiveCount) {
        this.receiveCount = receiveCount;
    }

    public BigDecimal getDetectionCount() {
        return detectionCount;
    }

    public void setDetectionCount(BigDecimal detectionCount) {
        this.detectionCount = detectionCount;
    }

    public String getDetectionFailType() {
        return detectionFailType;
    }

    public void setDetectionFailType(String detectionFailType) {
        this.detectionFailType = detectionFailType;
    }

    public String getDetectionFailRemark() {
        return detectionFailRemark;
    }

    public void setDetectionFailRemark(String detectionFailRemark) {
        this.detectionFailRemark = detectionFailRemark;
    }

    public BigDecimal getRegistrationCount() {
        return registrationCount;
    }

    public void setRegistrationCount(BigDecimal registrationCount) {
        this.registrationCount = registrationCount;
    }

    public BigDecimal getPutawayCount() {
        return putawayCount;
    }

    public void setPutawayCount(BigDecimal putawayCount) {
        this.putawayCount = putawayCount;
    }

    public Date getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(Date producedDate) {
        this.producedDate = producedDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getTbpos() {
        return tbpos;
    }

    public void setTbpos(String tbpos) {
        this.tbpos = tbpos;
    }

    public String getRfidBatchCode() {
        return rfidBatchCode;
    }

    public void setRfidBatchCode(String rfidBatchCode) {
        this.rfidBatchCode = rfidBatchCode;
    }

    public String getDetectionFailStatus() {
        return detectionFailStatus;
    }

    public void setDetectionFailStatus(String detectionFailStatus) {
        this.detectionFailStatus = detectionFailStatus;
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
                ", detectionFailStatus='" + detectionFailStatus + '\'' +
                ", registrationCount=" + registrationCount +
                ", putawayCount=" + putawayCount +
                ", producedDate=" + producedDate +
                ", expireDate=" + expireDate +
                ", batchCode='" + batchCode + '\'' +
                ", rfidBatchCode='" + rfidBatchCode + '\'' +
                '}';
    }
}
