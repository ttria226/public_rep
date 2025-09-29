package com.xsrw.wms.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TReservoir;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 18:04
 */
public class TReservoirVO extends TReservoir {

    /**
     * 区域名称
     */
    @Excel(name = "所属区域", sort = 2)
    private String areaName;

    private String categoryName;

    /**
     * 物料包装（主数据管理--单位）
     */
    private Long unitId;

    /**
     * 创建者
     */
    @Excel(name = "创建者", sort = 4)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss", sort = 5)
    private Date createTime;
    /** 备注 */
    @Excel(name = "备注")
    private String remark;

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

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
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
}
