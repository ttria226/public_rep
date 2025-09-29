package com.xsrw.wms.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TLocation;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/6 10:09
 */
public class TLocationVO extends TLocation {

    /**
     * 库区名称
     */
    @Excel(name = "所属库区", sort = 7, width = 20)
    private String reservoirName;

    /**
     * 区域名称
     */
    @Excel(name = "所属区域", sort = 6, width = 20)
    private String areaName;

    @Excel(name = "存放物料类别", sort = 8)
    private String categoryName;

    @Excel(name = "存放物料包装方式", sort = 9)
    private String unitName;

    /**
     * 创建者
     */
    @Excel(name = "制单人", sort = 17)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "制单时间", sort = 18, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 备注
     */
    @Excel(name = "备注", sort = 19, width = 50)
    private String remark;

    /**
     * 存放物料类别名称
     */
    private String depositCategoryName;
    /**
     * 货架
     */
    @Excel(name = "货架", sort = 5)
    private String goodShelfName;

    /**
     * 托盘id
     */
    private Long trayId;

    /**
     * 一伸位货位状态
     */
    private String firstGoodsAllocationStatus;

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDepositCategoryName() {
        return depositCategoryName;
    }

    public void setDepositCategoryName(String depositCategoryName) {
        this.depositCategoryName = depositCategoryName;
    }

    public String getGoodShelfName() {
        return goodShelfName;
    }

    public void setGoodShelfName(String goodShelfName) {
        this.goodShelfName = goodShelfName;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public String getFirstGoodsAllocationStatus() {
        return firstGoodsAllocationStatus;
    }

    public void setFirstGoodsAllocationStatus(String firstGoodsAllocationStatus) {
        this.firstGoodsAllocationStatus = firstGoodsAllocationStatus;
    }
}
