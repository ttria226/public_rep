package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库单详情对象 t_out_delivery_detail
 * 
 * @author zyq
 * @date 2023-05-09
 */
public class PADTOutDeliveryDetailVO
{
     /** 主键 */
    private String wcsId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private String materialId;

     @Excel(name = "批次号")
    private String batchCode;

    @Excel(name = "载具")
    private String trayId;

    @Excel(name = "载具")
    private String trayName;

    @Excel(name = "库位")
    private String locationId;

    @Excel(name = "库位")
    private String locationName;


    @Excel(name = "库区")
    private String reservoirName;


    @Excel(name = "库位")
    private String areaId;

    @Excel(name = "库位")
    private String areaName;



    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;


     @Excel(name = "物料名称")
    private String materialCode;


     @Excel(name = "单位")
    private String unitName;

     @Excel(name = "小件领取单位")
    private String smallUnitName;

    /** 数量 */
    @Excel(name = "本次预计数量")
    private Long predictCount;

    @Excel(name = "小件最大领取数量")
    private Long count;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getTrayId() {
        return trayId;
    }

    public void setTrayId(String trayId) {
        this.trayId = trayId;
    }

    public String getTrayName() {
        return trayName;
    }

    public void setTrayName(String trayName) {
        this.trayName = trayName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Excel(name = "数量")
    private Long smallPredictCount;

    public String getSmallUnitName() {
        return smallUnitName;
    }

    public void setSmallUnitName(String smallUnitName) {
        this.smallUnitName = smallUnitName;
    }

    public String getWcsId() {
        return wcsId;
    }

    public void setWcsId(String wcsId) {
        this.wcsId = wcsId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
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

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        count = count;
    }

    public Long getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(Long smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }
}
