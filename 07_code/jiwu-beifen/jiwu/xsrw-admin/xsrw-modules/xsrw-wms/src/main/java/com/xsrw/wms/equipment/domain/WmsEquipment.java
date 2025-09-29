package com.xsrw.wms.equipment.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 设备台账对象 d_equipment
 *
 * @author zjj
 * @date 2023-05-08
 */
@TableName("d_equipment")
public class WmsEquipment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String name;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String equNo;

    /** 设备编号 */
    @Excel(name = "图片")
    private String img;

    /** 资产编号 */
    @Excel(name = "资产编号")
    private String assetNo;

    /** 序列号 */
    @Excel(name = "序列号")
    private String serialNo;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String model;

    /** 功能位置 */
    @Excel(name = "功能位置")
    private String functionLocation;

    /** 所属部门id */
    @Excel(name = "所属部门id")
    private Long depId;

    /** 所属部门名称 */
    @Excel(name = "所属部门名称")
    private String depName;

    /** 供应商 */
    @Excel(name = "供应商")
    private String supplier;

    /** 使用状态 */
    @Excel(name = "使用状态")
    private Integer useStatus;

    /** 供应商名称 */
    @TableField(exist = false)
    private String supplierName;


    /** 巡检标准 */
    @TableField(exist = false)
    private String inspectionItems;

    /** 责任人 */
    @Excel(name = "责任人")
    private String person;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setEquNo(String equNo)
    {
        this.equNo = equNo;
    }

    public String getEquNo()
    {
        return equNo;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }
    public void setAssetNo(String assetNo)
    {
        this.assetNo = assetNo;
    }

    public String getAssetNo()
    {
        return assetNo;
    }
    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getSerialNo()
    {
        return serialNo;
    }
    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return model;
    }
    public void setfunctionLocation(String functionLocation)
    {
        this.functionLocation = functionLocation;
    }

    public String getfunctionLocation()
    {
        return functionLocation;
    }
    public void setDepId(Long depId)
    {
        this.depId = depId;
    }

    public Long getDepId()
    {
        return depId;
    }
    public void setDepName(String depName)
    {
        this.depName = depName;
    }

    public String getDepName()
    {
        return depName;
    }
    public void setSupplier(String supplier)
    {
        this.supplier = supplier;
    }

    public String getSupplier()
    {
        return supplier;
    }
    public void setUseStatus(Integer useStatus)
    {
        this.useStatus = useStatus;
    }

    public Integer getUseStatus()
    {
        return useStatus;
    }
    public void setPerson(String person)
    {
        this.person = person;
    }

    public String getPerson()
    {
        return person;
    }


    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getInspectionItems() {
        return inspectionItems;
    }

    public void setInspectionItems(String inspectionItems) {
        this.inspectionItems = inspectionItems;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("equNo", getEquNo())
            .append("assetNo", getAssetNo())
            .append("serialNo", getSerialNo())
            .append("model", getModel())
            .append("functionLocation", getfunctionLocation())
            .append("supplierName", getSupplierName())
            .append("depId", getDepId())
            .append("depName", getDepName())
            .append("supplier", getSupplier())
            .append("useStatus", getUseStatus())
            .append("person", getPerson())
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
