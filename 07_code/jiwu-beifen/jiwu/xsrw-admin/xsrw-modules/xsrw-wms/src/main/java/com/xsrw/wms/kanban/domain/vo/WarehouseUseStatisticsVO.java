package com.xsrw.wms.kanban.domain.vo;

/**
 * 仓库使用情况统计返回参数类
 */
public class WarehouseUseStatisticsVO {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 库区名称
     */
    private String reservoirName;

    /**
     * 总数
     */
    private Integer totalCount;


    /**
     * 使用中数量
     */
    private Integer useCount;

    /**
     * 使用率
     */
    private String useRate;

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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public String getUseRate() {
        return useRate;
    }

    public void setUseRate(String useRate) {
        this.useRate = useRate;
    }
}
