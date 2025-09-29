package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class TTrayVO {

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String code;

    /** 区域 */
    @Excel(name = "区域")
    private Long areaId;

    @Excel(name = "区域")
    private String areaname;

     @Excel(name = "库区")
    private Long reservoirid;

    @Excel(name = "库区")
    private String reservoirname;

    /** 库位 */
    @Excel(name = "库位")
    private Long locationId;

    @Excel(name = "库存id")
    private Long stockid;

     @Excel(name = "库位")
    private String locationname;

   @Excel(name = "库存")
    private Long availableCount;
    /**
     * 实际拣货数量
     */
    private Long predictReceiveCount;

    private Long predictCount;

    private String locationType;

    /** 状态（0空闲 1半托 2全托） */
    @Excel(name = "载具管理类别", readConverterExp = "1=托盘，2料箱 ，3货笼")
    private String trayCategory;


    private Long receiveCount;

    private Long materialId;

    private String batchCode;

    private String materialName;

    private String materialCode;


    public Long getStockid() {
        return stockid;
    }

    public void setStockid(Long stockid) {
        this.stockid = stockid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getPredictReceiveCount() {
        return predictReceiveCount;
    }

    public void setPredictReceiveCount(Long predictReceiveCount) {
        this.predictReceiveCount = predictReceiveCount;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }


    public Long getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(Long availableCount) {
        this.availableCount = availableCount;
    }

    public String getTrayCategory() {
        return trayCategory;
    }

    public void setTrayCategory(String trayCategory) {
        this.trayCategory = trayCategory;
    }

    public Long getReservoirid() {
        return reservoirid;
    }

    public void setReservoirid(Long reservoirid) {
        this.reservoirid = reservoirid;
    }

    public String getReservoirname() {
        return reservoirname;
    }

    public void setReservoirname(String reservoirname) {
        this.reservoirname = reservoirname;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
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
}
