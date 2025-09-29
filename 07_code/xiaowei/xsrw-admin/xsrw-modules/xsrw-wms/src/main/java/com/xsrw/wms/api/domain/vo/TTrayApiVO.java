package com.xsrw.wms.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TTray;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/18 11:17
 */
public class TTrayApiVO extends TTray {

    /**
     * 库位名称
     */
    @Excel(name = "库位",sort = 3)
    private String locationName;
    /**
     * 货位状态
     */
    private String goodsAllocationStatus;
    /**
     * 创建者
     */
    @Excel(name = "创建者")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 库位载具编号
     */
    private String palletNum;

    /**
     * 库位类型(0其他,1地堆)
     */
    private String locationType;

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

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }

    public String getPalletNum() {
        return palletNum;
    }

    public void setPalletNum(String palletNum) {
        this.palletNum = palletNum;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }
}
