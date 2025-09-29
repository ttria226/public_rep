package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 盘点计划对象 t_check_delivery
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_check_delivery")
public class TCheckDelivery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 盘点计划名称 **/
    private String planName;

    /** 载具类型 1托盘、2料箱、3地堆 **/
    private String trayType;

    /** 盘点维度  1物料  2库区 3动碰  4随机  5空货位 */
//    @Excel(name = "盘点维度  1物料  2库区", readConverterExp = "平=库时为区域")
    private String checkType;

    /** 物料标识 */
//    @Excel(name = "物料标识")
    private Long materialId;

    /** 区域标识 */
//    @Excel(name = "区域标识")
    private Long areaId;

    /** 库区标识 */
//    @Excel(name = "库区标识")
    private Long reservoirId;
    /** 库位 */
    private Long locationId;
    /** 动碰开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 动碰结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 随机物料个数 **/
    private Integer randomNum;

    /** 审核人 */
//    @Excel(name = "审核人")
    private String auditor;

    /** 状态 0未开始  1进行中 2已完成 */
//    @Excel(name = "状态")
    private String status;

    private String checkSource;

    private String taskStatus;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    private Long taskId;
    /**
     * 批次号
     */
    private String batchCode;

    /**
     * 行项目
     */
    private String tbpos;

    public String getCheckSource()
    {
        return checkSource;
    }

    public void setCheckSource(String  checkSource)
    {
        this.checkSource = checkSource;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setCheckType(String checkType)
    {
        this.checkType = checkType;
    }

    public String getCheckType()
    {
        return checkType;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId()
    {
        return areaId;
    }
    public void setReservoirId(Long reservoirId)
    {
        this.reservoirId = reservoirId;
    }

    public Long getReservoirId()
    {
        return reservoirId;
    }
    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public Long getLocationId()
    {
        return locationId;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAuditor()
    {
        return auditor;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getTrayType() {
        return trayType;
    }

    public void setTrayType(String trayType) {
        this.trayType = trayType;
    }

    public String getBatchCode() {
    return batchCode;
}

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(Integer randomNum) {
        this.randomNum = randomNum;
    }
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTbpos() {
        return tbpos;
    }

    public void setTbpos(String tbpos) {
        this.tbpos = tbpos;
    }

    @Override
    public String toString() {
        return "TCheckDelivery{" +
                "id=" + id +
                ", trayType='" + trayType + '\'' +
                ", checkType='" + checkType + '\'' +
                ", materialId=" + materialId +
                ", areaId=" + areaId +
                ", reservoirId=" + reservoirId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", randomNum=" + randomNum +
                ", auditor='" + auditor + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
