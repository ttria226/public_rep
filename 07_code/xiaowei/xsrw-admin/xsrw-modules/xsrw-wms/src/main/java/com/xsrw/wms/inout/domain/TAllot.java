package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 调拨单对象 t_allot
 *
 * @author zjj
 * @date 2023-06-26
 */
@TableName("t_allot")
public class TAllot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 调拨单号 */
    @Excel(name = "调拨单号",sort = 1)
    private String code;

    /** 物料id */
    private Long materialId;

    /** 库存id */
    private Long stockId;

    /** 批次号 */
    @Excel(name = "批次",sort = 4)
    private String batchCode;


    /** 调拨数量 */
    @Excel(name = "数量",sort = 6)
    private BigDecimal allotNum;

    /** 原仓库id */
    private Long outWarehouseId;

    /** 原仓库名称 */
    @Excel(name = "原仓库",sort = 7)
    private String outWarehouseName;

    /** 迁入仓库 */
    private Long inWarehouseId;

    /** 迁入仓库名称 */
    @Excel(name = "新仓库",sort = 8)
    private String inWarehouseName;

    /** 状态 1待审核、2等待出库、3等待入库、4已完成 */
    @Excel(name = "状态",readConverterExp = "1=待审核,2=等待出库,3=等待入库,4=已完成",sort = 9)
    private String allotStatus;

    /** 审核人 */
    @Excel(name = "审核人",sort = 10)
    private String auditor;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setBatchCode(String batchCode)
    {
        this.batchCode = batchCode;
    }

    public String getBatchCode()
    {
        return batchCode;
    }

    public BigDecimal getAllotNum() {
        return allotNum;
    }

    public void setAllotNum(BigDecimal allotNum) {
        this.allotNum = allotNum;
    }

    public void setOutWarehouseId(Long outWarehouseId)
    {
        this.outWarehouseId = outWarehouseId;
    }

    public Long getOutWarehouseId()
    {
        return outWarehouseId;
    }
    public void setOutWarehouseName(String outWarehouseName)
    {
        this.outWarehouseName = outWarehouseName;
    }

    public String getOutWarehouseName()
    {
        return outWarehouseName;
    }
    public void setInWarehouseId(Long inWarehouseId)
    {
        this.inWarehouseId = inWarehouseId;
    }

    public Long getInWarehouseId()
    {
        return inWarehouseId;
    }
    public void setInWarehouseName(String inWarehouseName)
    {
        this.inWarehouseName = inWarehouseName;
    }

    public String getInWarehouseName()
    {
        return inWarehouseName;
    }
    public void setAllotStatus(String allotStatus)
    {
        this.allotStatus = allotStatus;
    }

    public String getAllotStatus()
    {
        return allotStatus;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAuditor()
    {
        return auditor;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    @Override
    public String toString() {
        return "TAllot{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", materialId=" + materialId +
                ", stockId=" + stockId +
                ", batchCode='" + batchCode + '\'' +
                ", allotNum=" + allotNum +
                ", outWarehouseId=" + outWarehouseId +
                ", outWarehouseName='" + outWarehouseName + '\'' +
                ", inWarehouseId=" + inWarehouseId +
                ", inWarehouseName='" + inWarehouseName + '\'' +
                ", allotStatus='" + allotStatus + '\'' +
                ", auditor='" + auditor + '\'' +
                '}';
    }
}
