package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 小件出库记录对象 t_out_delivery_samll_record
 *
 * @author zyq
 * @date 2023-05-13
 */
@TableName("t_out_delivery_samll_record")
public class TOutDeliverySamllRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 出库单详情标识 */
    @Excel(name = "出库单详情标识")
    private Long outDeliveryDetailId;

     @Excel(name = "物料名称")
     @TableField(exist = false)
     private String materialName;

     @Excel(name = "物料编码")
     @TableField(exist = false)
     private String materialCode;

     @Excel(name = "载具编号")
     @TableField(exist = false)
    private String trayCode;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialDetailId;

    /** 库位标识 */
    @Excel(name = "库位标识")
    private Long locationId;

    /** 托盘标识 */
    @Excel(name = "托盘标识")
    private Long trayId;

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

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOutDeliveryDetailId(Long outDeliveryDetailId)
    {
        this.outDeliveryDetailId = outDeliveryDetailId;
    }

    public Long getOutDeliveryDetailId()
    {
        return outDeliveryDetailId;
    }
    public void setMaterialDetailId(Long materialDetailId)
    {
        this.materialDetailId = materialDetailId;
    }

    public Long getMaterialDetailId()
    {
        return materialDetailId;
    }
    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public Long getLocationId()
    {
        return locationId;
    }
    public void setTrayId(Long trayId)
    {
        this.trayId = trayId;
    }

    public Long getTrayId()
    {
        return trayId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("outDeliveryDetailId", getOutDeliveryDetailId())
            .append("materialDetailId", getMaterialDetailId())
            .append("locationId", getLocationId())
            .append("trayId", getTrayId())
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
