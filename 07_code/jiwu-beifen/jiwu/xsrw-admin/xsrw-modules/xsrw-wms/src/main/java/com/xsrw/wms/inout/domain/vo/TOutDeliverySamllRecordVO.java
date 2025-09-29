package com.xsrw.wms.inout.domain.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 小件出库记录对象 t_out_delivery_samll_record
 *
 * @author zyq
 * @date 2023-05-13
 */
@TableName("t_out_delivery_samll_record")
public class TOutDeliverySamllRecordVO extends BaseEntity
{
    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 物料标识 */
   private Long materialDetailId;
    @Excel(name = "物料编码")
    private String materialCode;
    @Excel(name = "物料名称")
    private String materialName;
    @Excel(name = "计量单位")
    private String unitName;
    @Excel(name = "库位")
    private String locationName;
    @Excel(name = "库区")
    private String reservoirName;

    @Excel(name = "区域")
    private String areaName;
    @Excel(name = "载具编号")
    private String trayCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Excel(name = "日期",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Excel(name = "领取数据")
    private Long smallReceiveCount;
    /**
     * 载具编号
     */


    /** 库位标识 */
    private Long locationId;



    private Long unitId;

     private Long areaId;


    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public Long getSmallReceiveCount() {
        return smallReceiveCount;
    }

    public void setSmallReceiveCount(Long smallReceiveCount) {
        this.smallReceiveCount = smallReceiveCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(Long materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }
}
