package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 每日巡检记录对象 d_inspection_day_info
 *
 * @author zjj
 * @date 2023-05-18
 */
@TableName("d_inspection_day_info")
public class DInspectionDayInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联巡检日期id */
    @Excel(name = "关联巡检日期id")
    private Long dayId;

    /** 所巡检设备id */
    @Excel(name = "所巡检设备id")
    private Long equipmentId;

    /** 所巡检设备名称 */
    @TableField(exist = false)
    private String equipmentName;

    /** 所巡检设备位置 */
    @TableField(exist = false)
    private String equipmentRegion;

    /** 照片/视频 */
    @Excel(name = "照片/视频")
    private String img;

    /** 1:正常巡检 2：已报修 */
    @Excel(name = "1:正常巡检 2：已报修")
    private Integer status;

    /** 经纬度*/
    @Excel(name = "经纬度")
    private String latAndLon;

    /** 所在位置 */
    @Excel(name = "所在位置")
    private String region;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultMessage;

    /** 故障等级 */
    @Excel(name = "故障等级")
    private Integer faultLv;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private Integer equipmentStatus;

    /** 是否停机 */
    @Excel(name = "是否停机")
    private Integer isShutdown;

    @TableField(exist = false)
    private  DRepairReport repairReport;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentRegion() {
        return equipmentRegion;
    }

    public void setEquipmentRegion(String equipmentRegion) {
        this.equipmentRegion = equipmentRegion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLatAndLon() {
        return latAndLon;
    }

    public void setLatAndLon(String latAndLon) {
        this.latAndLon = latAndLon;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFaultMessage() {
        return faultMessage;
    }

    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }

    public Integer getFaultLv() {
        return faultLv;
    }

    public void setFaultLv(Integer faultLv) {
        this.faultLv = faultLv;
    }

    public Integer getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(Integer equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public Integer getIsShutdown() {
        return isShutdown;
    }

    public void setIsShutdown(Integer isShutdown) {
        this.isShutdown = isShutdown;
    }

    public DRepairReport getRepairReport() {
        return repairReport;
    }

    public void setRepairReport(DRepairReport repairReport) {
        this.repairReport = repairReport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("dayId", getDayId())
            .append("equipmentId", getEquipmentId())
            .append("img", getImg())
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
