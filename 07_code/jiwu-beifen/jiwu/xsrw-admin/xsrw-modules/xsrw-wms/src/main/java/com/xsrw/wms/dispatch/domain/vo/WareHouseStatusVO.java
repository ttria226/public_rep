package com.xsrw.wms.dispatch.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 仓库状态列表返回参数类
 */
public class WareHouseStatusVO {

    /**
     * 仓库名称
     */
    @Excel(name = "仓库名称", sort = 1)
    private String deptName;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量", sort = 2)
    private Integer libraryCount;

    /**
     * 入库任务数量
     */
    @Excel(name = "入库任务", sort = 3)
    private Integer inTaskCount;

    /**
     * 出库任务数量
     */
    @Excel(name = "出库任务", sort = 4)
    private Integer outTaskCount;

    /**
     * 盘点任务数量
     */
    @Excel(name = "盘点任务", sort = 5)
    private Integer inventoryTaskCount;

    /**
     * 创建人
     */
    @Excel(name = "库管员", sort = 6)
    private String createBy;


    /**
     * 创建时间
     */
    @Excel(name = "日期", sort = 7,dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Integer libraryCount) {
        this.libraryCount = libraryCount;
    }

    public Integer getInTaskCount() {
        return inTaskCount;
    }

    public void setInTaskCount(Integer inTaskCount) {
        this.inTaskCount = inTaskCount;
    }

    public Integer getOutTaskCount() {
        return outTaskCount;
    }

    public void setOutTaskCount(Integer outTaskCount) {
        this.outTaskCount = outTaskCount;
    }

    public Integer getInventoryTaskCount() {
        return inventoryTaskCount;
    }

    public void setInventoryTaskCount(Integer inventoryTaskCount) {
        this.inventoryTaskCount = inventoryTaskCount;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
