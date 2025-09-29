package com.xsrw.wms.stock.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TStock;

/**
 * 库存对象 t_stock
 *
 */
public class StockVo extends TStock {

    /** 物料编码  */
    @Excel(name = "物料编码",sort = 2)
    private String materialCode;

    /** 物料名称  */
    @Excel(name = "物料名称",sort = 3)
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号",sort = 4)
    private String specifications;

    /** 单位名称 */
    @Excel(name = "单位",sort = 5)
    private String unitName;

    /** 包装方式名称 */
    @Excel(name = "包装方式",sort = 6)
    private String packUnitName;

    /** 区域名称 */
    @Excel(name = "区域",sort = 7)
    private String areaName;

    /** 库区 */
    private Long reservoirId;

    /** 库区名称 */
    @Excel(name = "库区",sort = 8)
    private String reservoirName;

    /** 库位编码 */
//    @Excel(name = "库位编码",sort = 10)
    private String locationCode;

    /** 库位编码 */
    @Excel(name = "库位名称",sort = 10)
    private String locationName;


    /** 托盘Code */
    @Excel(name = "托盘",sort = 12)
    private String trayCode;

    /** 剩余有效期天数 */
    private String remainingValidDays;

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public void setPackUnitName(String packUnitName)
    {
        this.packUnitName = packUnitName;
    }

    public String getPackUnitName()
    {
        return packUnitName;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public void setReservoirName(String reservoirName)
    {
        this.reservoirName = reservoirName;
    }

    public String getReservoirName()
    {
        return reservoirName;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setRemainingValidDays(String remainingValidDays) {
        this.remainingValidDays = remainingValidDays;
    }

    public String getRemainingValidDays() {
        return remainingValidDays;
    }

}
