package com.xsrw.wms.report.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 工作统计返回参数类
 */
public class WorkStatisticsListsVO {

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 1)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位", sort = 3)
    private String unitName;


    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 4)
    private String batchCode;

    /**
     * 类型  1入库 2出库
     */
    @Excel(name = "类型", sort = 5,readConverterExp = "1=入库,2=出库")
    private String type;

    /**
     * 数量
     */
    @Excel(name = "数量", sort = 6)
    private Integer count;

    /**
     * 耗时(小时)
     */
    @Excel(name = "工作时间（h）", sort = 7)
    private double timeConsume;

    /**
     * 日期
     */
    @Excel(name = "日期", sort = 8,dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(double timeConsume) {
        this.timeConsume = timeConsume;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
