package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/8 14:50
 */
public class TAdvanceDeliveryVO extends TAdvanceDelivery {


    /**
     * 部门名称
     */
    @Excel(name = "物料使用部门", sort = 2)
    private String deptName;
    /**
     * 创建者
     */
    @Excel(name = "制单人", sort = 2)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建日期", sort = 7, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * bom名称
     */
    private String BomName;

    /**
     * 备注
     */
    @Excel(name = "备注", sort = 8)
    private String remark;

    /**
     * 上架状态
     */
    private String putWayStatus;
    /**
     * 入库单详情列表
     */
    private List<TAdvanceDeliveryDetailVO> deliveryDetailList;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getBomName() {
        return BomName;
    }

    public void setBomName(String bomName) {
        BomName = bomName;
    }

    public String getPutWayStatus() {
        return putWayStatus;
    }

    public void setPutWayStatus(String putWayStatus) {
        this.putWayStatus = putWayStatus;
    }

    public List<TAdvanceDeliveryDetailVO> getDeliveryDetailList() {
        return deliveryDetailList;
    }

    public void setDeliveryDetailList(List<TAdvanceDeliveryDetailVO> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
    }
}
