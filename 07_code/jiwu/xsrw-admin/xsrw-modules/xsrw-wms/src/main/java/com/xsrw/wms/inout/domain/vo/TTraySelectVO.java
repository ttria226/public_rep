package com.xsrw.wms.inout.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 载具管理对象 t_tray
 *
 * @author lyx
 * @date 2023-05-05
 */
@TableName("t_tray")
public class TTraySelectVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String code;

    /** 区域 */
    @Excel(name = "区域")
    private Long areaId;

    /** 库位 */
    @Excel(name = "库位")
    private Long locationId;

    /** 库区 */
    @Excel(name = "库区")
    private Long reservoirId;

    /** 手工创建（5手工创建6系统导入7系统生成） */
    @Excel(name = "手工创建", readConverterExp = "5=手工创建6系统导入7系统生成")
    private String type;

    /** 状态（0空闲 1半托 2全托） */
    @Excel(name = "状态", readConverterExp = "0=空闲 1半托 2全托")
    private String status;

    /** 载具管理类别（1托盘，2料箱 ，3货笼） */
    @Excel(name = "载具管理类别", readConverterExp = "1=托盘，2料箱 ，3货笼")
    private String trayCategory;

    /** 是否绑定标签（0否 1是） */
    @Excel(name = "是否绑定标签", readConverterExp = "0=否 1是")
    private String labelTemplateType;

    /** 标签模板 */
    @Excel(name = "标签模板")
    private Long labelTemplateId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId()
    {
        return areaId;
    }
    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public Long getLocationId()
    {
        return locationId;
    }
    public void setReservoirId(Long reservoirId)
    {
        this.reservoirId = reservoirId;
    }

    public Long getReservoirId()
    {
        return reservoirId;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setTrayCategory(String trayCategory)
    {
        this.trayCategory = trayCategory;
    }

    public String getTrayCategory()
    {
        return trayCategory;
    }
    public void setLabelTemplateType(String labelTemplateType)
    {
        this.labelTemplateType = labelTemplateType;
    }

    public String getLabelTemplateType()
    {
        return labelTemplateType;
    }
    public void setLabelTemplateId(Long labelTemplateId)
    {
        this.labelTemplateId = labelTemplateId;
    }

    public Long getLabelTemplateId()
    {
        return labelTemplateId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("areaId", getAreaId())
            .append("locationId", getLocationId())
            .append("reservoirId", getReservoirId())
            .append("type", getType())
            .append("status", getStatus())
            .append("trayCategory", getTrayCategory())
            .append("labelTemplateType", getLabelTemplateType())
            .append("labelTemplateId", getLabelTemplateId())
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
