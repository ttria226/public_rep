package com.xsrw.wms.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TRulePutaway;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/13 11:01
 */
public class TRulePutawayVO extends TRulePutaway {

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 1)
    private String materialCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 1)
    private String materialName;

    /**
     * 库位名称
     */
    @Excel(name = "绑定库位", sort = 1)
    private String locationName;


    /**
     * 创建者
     */
    @Excel(name = "创建者", sort = 3)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss", sort = 3)
    private Date createTime;

    /**
     * 库位id
     */
    private Long locationId;
    /**
     * 是否在库
     */
    private String inStockStatus;

    /**
     * 详情列表
     */
    private List<Long> detailList;


    /**
     * 区域标识
     */
    private Long areaId;

    /**
     * 库区标识
     */
    private Long reservoirId;

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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getInStockStatus() {
        return inStockStatus;
    }

    public void setInStockStatus(String inStockStatus) {
        this.inStockStatus = inStockStatus;
    }

    public List<Long> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Long> detailList) {
        this.detailList = detailList;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }
}
