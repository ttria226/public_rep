package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

/**
 * 货位使用频率报表返回参数类
 */
public class FrequencyOfLocationVO {

    /**
     * 区域
     */
    @Excel(name = "区域", sort = 1)
    private String areaName;

    /**
     * 库区
     */
    @Excel(name = "库区", sort = 2)
    private String reservoirName;

    /**
     * 货架
     */
    @Excel(name = "货架", sort = 3)
    private String goodShelfName;

    /**
     * 货位
     */
    @Excel(name = "货位", sort = 4)
    private String locationName;

    /**
     * 入库次数
     */
    @Excel(name = "入库次数", sort = 5)
    private Integer inCount;



    /**
     * 出库次数
     **/
    @Excel(name = "出库次数", sort = 6)
    private Integer outCount;

    /**
     * 使用频率
     **/
    @Excel(name = "使用频率", sort = 7)
    private String frequency;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getGoodShelfName() {
        return goodShelfName;
    }

    public void setGoodShelfName(String goodShelfName) {
        this.goodShelfName = goodShelfName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getInCount() {
        return inCount;
    }

    public void setInCount(Integer inCount) {
        this.inCount = inCount;
    }

    public Integer getOutCount() {
        return outCount;
    }

    public void setOutCount(Integer outCount) {
        this.outCount = outCount;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
