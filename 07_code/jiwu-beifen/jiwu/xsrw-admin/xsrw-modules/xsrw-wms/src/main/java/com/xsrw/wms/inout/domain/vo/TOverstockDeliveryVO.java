package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TOverstockDelivery;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/25 15:54
 */
public class TOverstockDeliveryVO extends TOverstockDelivery {

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
    @Excel(name = "创建时间", sort = 7, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 详情列表
     */
    private List<TOverstockDeliveryDetailVO> deliveryDetailList;

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

    public List<TOverstockDeliveryDetailVO> getDeliveryDetailList() {
        return deliveryDetailList;
    }

    public void setDeliveryDetailList(List<TOverstockDeliveryDetailVO> deliveryDetailList) {
        this.deliveryDetailList = deliveryDetailList;
    }
}
