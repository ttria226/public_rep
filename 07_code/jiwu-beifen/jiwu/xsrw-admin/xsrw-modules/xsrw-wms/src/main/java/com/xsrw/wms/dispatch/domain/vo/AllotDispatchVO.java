package com.xsrw.wms.dispatch.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;

import java.util.Date;

/**
 * 调拨调度列表返回参数类
 */
public class AllotDispatchVO {

    /**
     * 调拨单号
     */
    @Excel(name = "调拨单号", sort = 1)
    private String code;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 2)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;

    /**
     * 批次号
     */
    @Excel(name = "批次号", sort = 3)
    private String batchCode;

    /**
     * 调拨数量
     */
    @Excel(name = "调拨数量", sort = 4)
    private Integer allotNum;

    /**
     * 原仓库名称
     */
    @Excel(name = "原仓库", sort = 5)
    private String outWarehouseName;

    /**
     * 迁入仓库名称
     */
    @Excel(name = "目标仓库", sort = 6)
    private String inWarehouseName;

    /**
     * 状态 1待审核、2等待出库、3等待入库、4已完成
     */
    @Excel(name = "状态", sort = 7,readConverterExp = "1=待审核,2=等待出库,3=等待入库,4=已完成,5=审核驳回")
    private String allotStatus;



    /**
     * 创建时间
     */
    @Excel(name = "创建时间", sort = 7,dateFormat = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getAllotNum() {
        return allotNum;
    }

    public void setAllotNum(Integer allotNum) {
        this.allotNum = allotNum;
    }

    public String getOutWarehouseName() {
        return outWarehouseName;
    }

    public void setOutWarehouseName(String outWarehouseName) {
        this.outWarehouseName = outWarehouseName;
    }

    public String getInWarehouseName() {
        return inWarehouseName;
    }

    public void setInWarehouseName(String inWarehouseName) {
        this.inWarehouseName = inWarehouseName;
    }

    public String getAllotStatus() {
        return allotStatus;
    }

    public void setAllotStatus(String allotStatus) {
        this.allotStatus = allotStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
