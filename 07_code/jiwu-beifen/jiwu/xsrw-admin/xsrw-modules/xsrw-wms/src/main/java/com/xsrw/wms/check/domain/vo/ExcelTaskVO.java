package com.xsrw.wms.check.domain.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

public class ExcelTaskVO {
    /** 任务编号 */
    @Excel(name = "任务编号",sort = 1)
    private String code;

    /** 主键 */
    @TableId(value = "id")
    private Long id;

    /** 来源标识(收货id、盘点id、回库id、移库id) */
    private Long sourceId;

    /** 收货类型（1.上架任务、2.拣货任务3.盘点任务、4.回库任务、5移库任务） */
//    @Excel(name = "任务类型" , readConverterExp = "1=上架任务,2=拣货任务,3=盘点任务,4=回库任务,5=移库任务")
    private String taskType;

    /** 部门名称 */
//    @Excel(name = "所属组织")
    private String deptName;

    /** 执行状态（0未执行1:执行中 2：执行完成） */
//    @Excel(name = "执行状态" , readConverterExp = "0=未执行,1=部分完成,2=已完成,3=确认中,4=已确认,5=已驳回,6=任务失败,7=执行中")
    private String taskStatus;

    /** 任务详情数量 */
//    @Excel(name = "任务详情数量")
    private Long taskCount;


    /** 源单单号（任务来源的原始单据编号） */
//    @Excel(name = "源单单号")
    private String originCode;


    /** 关联任务编号 **/
//    @Excel(name = "关联任务单号")
    private String relationTaskCode;

    /**
     * 任务号
     */
    private String taskCode;


    /** 部门标识 */
    private Long deptId;


    /** 审核人 */
//    @Excel(name = "审核人")
    private String auditor;

    /** 删除(0:未删除 1:删除) */
    @TableLogic
    private String delFlag;

    /** 任务状态0：pda看不到  1：可以看到（生成的拣货或上架最后一步是否在PDA能看到） **/
    private String status;


    /** 任务详情 */
    /** 任务标识 */
    private Long taskId;

    /** 物料标识 */
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",sort = 3)
    private String materialName;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码",sort = 2)
    private String materialCode;

    /**
     * 单位名称
     */
//    @Excel(name = "单位")
    private String unitName;

    /**
     * 规格型号
     */
//    @Excel(name = "规格型号")
    private String specifications;

    /**
     * 库位名称
     */
//    @Excel(name = "库位")
    private String locationName;

    /**
     * 托盘名称
     */
//    @Excel(name = "载具")
    private String trayName;

    /** 值班人员 */
//    @Excel(name = "值班人")
    private String dutyPersonnel;

    /** 批号 */
//    @Excel(name = "批次号")
    private String batchNumber;

    /** 计划数量 */
    @Excel(name = "库存数量",sort = 4)
    private Long predictCount;


    /** 实际数量 */
    @Excel(name = "盘点数量",sort = 5)
    private Long actualCount;

    /** 实际重量(整数) */
//    @Excel(name = "实际重量")
    private Long actualWeight;

    /** 状态(0:未完成，1：进行中 2：已完成) */
//    @Excel(name = "任务状态",readConverterExp = "0=未完成,1=进行中,2=已完成,3=审核中,4=已审核,5=已驳回,6=任务失败")
    private String taskDetailsStatus;

    /**
     * 备注
     */
//    @Excel(name = "备注")
    private String remark;

    /** 载具(托盘id) */
    private Long trayId;

    /** 库位标识 */
    private Long locationId;


    /** 优先级 */
    private Long priority;


    /** 库存标识 */
    private Long stockId;

    /** 复盘 */
    private Integer checkCount;

    /** 删除(0:未删除 1:删除) */
    @TableLogic
    private String taskDelFlag;

    /**
     * 数据来源id
     */
    private Long taskSourceId;

    /**
     * 来源类型（出库任务使用  1 出库计划 2波次计划）
     */
    private String deliveryType;

    /**
     * 载具(托盘)
     */
    private String trayCode;

    /**
     * 库位编码
     */
    private String locationCode;

    /**
     * 库区名称
     */
    private String reservoirName;

    /**
     * 区域名称
     */
    private String areaName;


    /**
     * 盘点差异数量
     */
    @Excel(name = "盘差",sort = 6)
    private Long checkDifferenceCount;

    /**
     * 盘点人
     */
    @Excel(name = "盘点人",sort = 7)
    private String createBy;

    /**
     * 盘点时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "盘点时间",sort = 8, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public String getRelationTaskCode() {
        return relationTaskCode;
    }

    public void setRelationTaskCode(String relationTaskCode) {
        this.relationTaskCode = relationTaskCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public String getDutyPersonnel() {
        return dutyPersonnel;
    }

    public void setDutyPersonnel(String dutyPersonnel) {
        this.dutyPersonnel = dutyPersonnel;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getActualCount() {
        return actualCount;
    }

    public void setActualCount(Long actualCount) {
        this.actualCount = actualCount;
    }

    public Long getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(Long actualWeight) {
        this.actualWeight = actualWeight;
    }

    public String getTaskDetailsStatus() {
        return taskDetailsStatus;
    }

    public void setTaskDetailsStatus(String taskDetailsStatus) {
        this.taskDetailsStatus = taskDetailsStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    public String getTaskDelFlag() {
        return taskDelFlag;
    }

    public void setTaskDelFlag(String taskDelFlag) {
        this.taskDelFlag = taskDelFlag;
    }

    public Long getTaskSourceId() {
        return taskSourceId;
    }

    public void setTaskSourceId(Long taskSourceId) {
        this.taskSourceId = taskSourceId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Long getCheckDifferenceCount() {
        return checkDifferenceCount;
    }

    public void setCheckDifferenceCount(Long checkDifferenceCount) {
        this.checkDifferenceCount = checkDifferenceCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ExcelTaskVO{" +
                "code='" + code + '\'' +
                ", id=" + id +
                ", sourceId=" + sourceId +
                ", taskType='" + taskType + '\'' +
                ", deptName='" + deptName + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                ", taskCount=" + taskCount +
                ", originCode='" + originCode + '\'' +
                ", relationTaskCode='" + relationTaskCode + '\'' +
                ", taskCode='" + taskCode + '\'' +
                ", deptId=" + deptId +
                ", auditor='" + auditor + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", specifications='" + specifications + '\'' +
                ", locationName='" + locationName + '\'' +
                ", trayName='" + trayName + '\'' +
                ", dutyPersonnel='" + dutyPersonnel + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", predictCount=" + predictCount +
                ", actualCount=" + actualCount +
                ", actualWeight=" + actualWeight +
                ", taskDetailsStatus='" + taskDetailsStatus + '\'' +
                ", remark='" + remark + '\'' +
                ", trayId=" + trayId +
                ", locationId=" + locationId +
                ", priority=" + priority +
                ", stockId=" + stockId +
                ", checkCount=" + checkCount +
                ", taskDelFlag='" + taskDelFlag + '\'' +
                ", taskSourceId=" + taskSourceId +
                ", deliveryType='" + deliveryType + '\'' +
                ", trayCode='" + trayCode + '\'' +
                ", locationCode='" + locationCode + '\'' +
                ", reservoirName='" + reservoirName + '\'' +
                ", areaName='" + areaName + '\'' +
                ", checkDifferenceCount=" + checkDifferenceCount +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
