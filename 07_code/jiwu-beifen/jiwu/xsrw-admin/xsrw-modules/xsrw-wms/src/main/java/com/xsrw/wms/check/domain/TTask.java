package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 盘点任务对象 t_task
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_task")
public class TTask extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 源单单号（任务来源的原始单据编号） */
    @Excel(name = "源单单号", readConverterExp = "任=务来源的原始单据编号")
    private String originCode;

    /** 盘点id、移库id) */
    @Excel(name = "盘点id、移库id)")
    private Long sourceId;

    /** 任务类型（3.盘点任务、5移库任务） */
    @Excel(name = "任务类型", readConverterExp = "3=.盘点任务、5移库任务")
    private String taskType;

    /** 执行状态（0未执行1执行中 2执行完成4已审核） */
    @Excel(name = "执行状态", readConverterExp = "0=未执行1执行中,2=执行完成4已审核")
    private String taskStatus;

    /** 任务详情数量 */
    @Excel(name = "任务详情数量")
    private Long taskCount;

    /** 任务编号 */
    @Excel(name = "任务编号")
    private String code;

    /** 审核人 */
    @Excel(name = "审核人")
    private String auditor;

    /** 任务状态0pda看不到  1可以看到（生成的拣货或上架最后一步是否在PDA能看到） */
    @Excel(name = "任务状态0pda看不到  1可以看到", readConverterExp = "生=成的拣货或上架最后一步是否在PDA能看到")
    private String status;

    /** 是否重新组盘 */
    @Excel(name = "是否重新组盘")
    private String isReassemble;

    /** 关联任务编号 */
    @Excel(name = "关联任务编号")
    private String relationTaskCode;

    /** 盘点维度  1物料  2库区（平库时为区域） */
    @Excel(name = "盘点维度  1物料  2库区", readConverterExp = "平=库时为区域")
    private String checkType;

    /**
     * 盘点筛选任务状态使用
     */
    @TableField(exist = false)
    private String  checkDeliveryStatus;

    /**
     * 区域id
     */
    @TableField(exist = false)
    private Long areaId;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private String startTime;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String endTime;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialName;

     @TableField(exist = false)
    private String materialCode;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsReassemble() {
        return isReassemble;
    }

    public void setIsReassemble(String isReassemble) {
        this.isReassemble = isReassemble;
    }

    public String getRelationTaskCode() {
        return relationTaskCode;
    }

    public void setRelationTaskCode(String relationTaskCode) {
        this.relationTaskCode = relationTaskCode;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getCheckDeliveryStatus() {
        return checkDeliveryStatus;
    }

    public void setCheckDeliveryStatus(String checkDeliveryStatus) {
        this.checkDeliveryStatus = checkDeliveryStatus;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TTask{" +
                "id=" + id +
                ", originCode='" + originCode + '\'' +
                ", sourceId=" + sourceId +
                ", taskType='" + taskType + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskCount=" + taskCount +
                ", code='" + code + '\'' +
                ", auditor='" + auditor + '\'' +
                ", status='" + status + '\'' +
                ", isReassemble='" + isReassemble + '\'' +
                ", relationTaskCode='" + relationTaskCode + '\'' +
                ", checkType='" + checkType + '\'' +
                ", checkDeliveryStatus='" + checkDeliveryStatus + '\'' +
                ", areaId=" + areaId +
                ", materialName=" + materialName +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
