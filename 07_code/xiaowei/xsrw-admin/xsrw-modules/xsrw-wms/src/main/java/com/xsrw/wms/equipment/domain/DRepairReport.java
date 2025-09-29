package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 故障报修对象 d_repair_report
 *
 * @author zjj
 * @date 2023-05-13
 */
@TableName("d_repair_report")
public class DRepairReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备id */
    @Excel(name = "设备id")
    private Long equipmentId;

    /** 故障配件名称 */
    @Excel(name = "故障配件名称")
    private String faultyAccessoryName;

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

    /** 上传图片或视频 */
    @Excel(name = "上传图片或视频")
    private String img;

    /** 保养部门id */
    @Excel(name = "保养部门id")
    private Long companyId;

    /** 执行人id */
    @Excel(name = "执行人id")
    private Long executorId;

    /** 生成工单状态 1：未生成 2：已生成 */
    @Excel(name = "生成工单状态 1：未生成 2：已生成")
    private Integer status;

    /** 1新建 2 设备巡检*/
    @Excel(name = "1新建 2 设备巡检")
    private Integer source;

    /** 定位*/
    @Excel(name = "定位")
    private String address;

    /** 经纬度*/
    @Excel(name = "经纬度")
    private String latAndLon;

    /** dayId */
    @Excel(name = "dayId")
    @TableField(exist = false)
    private Long dayId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @TableField(exist = false)
    private String equName;

    /** 设备编号 */
    @Excel(name = "equNo")
    @TableField(exist = false)
    private String equNo;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setEquipmentId(Long equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public Long getEquipmentId()
    {
        return equipmentId;
    }
    public void setFaultyAccessoryName(String faultyAccessoryName)
    {
        this.faultyAccessoryName = faultyAccessoryName;
    }

    public String getFaultyAccessoryName()
    {
        return faultyAccessoryName;
    }
    public void setFaultMessage(String faultMessage)
    {
        this.faultMessage = faultMessage;
    }

    public String getFaultMessage()
    {
        return faultMessage;
    }
    public void setFaultLv(Integer faultLv)
    {
        this.faultLv = faultLv;
    }

    public Integer getFaultLv()
    {
        return faultLv;
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

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setExecutorId(Long executorId)
    {
        this.executorId = executorId;
    }

    public Long getExecutorId()
    {
        return executorId;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEquName() {
        return equName;
    }

    public void setEquName(String equName) {
        this.equName = equName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getEquNo() {
        return equNo;
    }

    public void setEquNo(String equNo) {
        this.equNo = equNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatAndLon() {
        return latAndLon;
    }

    public void setLatAndLon(String latAndLon) {
        this.latAndLon = latAndLon;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("equipmentId", getEquipmentId())
            .append("faultyAccessoryName", getFaultyAccessoryName())
            .append("faultMessage", getFaultMessage())
            .append("faultLv", getFaultLv())
            .append("equipmentStatus", getEquipmentStatus())
            .append("isShutdown", getIsShutdown())
            .append("img", getImg())
            .append("companyId", getCompanyId())
            .append("executorId", getExecutorId())
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
