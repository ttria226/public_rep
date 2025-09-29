package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库任务详情对象 t_task_out
 *
 * @author zyq
 * @date 2023-05-08
 */
@TableName("t_task_out")
public class TTaskOut extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 出库单标识 */
    @Excel(name = "出库单标识")
    private Long outDeliveryId;

    /** 出库单详情标识 */
    @Excel(name = "出库单详情标识")
    private Long outDeliveryDetailId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private Long predictCount;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private Long actualCount;

    /** 状态(0未完成，1进行中 2已完成 ) */
    @Excel(name = "状态(0未完成，1进行中 2已完成 )")
    private String status;

    /** wcs任务标识 */
    @Excel(name = "wcs任务标识")
    private Long wcsId;

    /** 库存 */
    @Excel(name = "库存")
    private Long stockId;

    /** 载具 */
    @Excel(name = "载具")
    private Long trayId;

    /** 库位 */
    @Excel(name = "库位")
    private Long locationId;

    public Long getWcsId() {
        return wcsId;
    }

    public void setWcsId(Long wcsId) {
        this.wcsId = wcsId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOutDeliveryId(Long outDeliveryId)
    {
        this.outDeliveryId = outDeliveryId;
    }

    public Long getOutDeliveryId()
    {
        return outDeliveryId;
    }

    public Long getOutDeliveryDetailId() {
        return outDeliveryDetailId;
    }

    public void setOutDeliveryDetailId(Long outDeliveryDetailId) {
        this.outDeliveryDetailId = outDeliveryDetailId;
    }

    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setPredictCount(Long predictCount)
    {
        this.predictCount = predictCount;
    }

    public Long getPredictCount()
    {
        return predictCount;
    }
    public void setActualCount(Long actualCount)
    {
        this.actualCount = actualCount;
    }

    public Long getActualCount()
    {
        return actualCount;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setStockId(Long stockId)
    {
        this.stockId = stockId;
    }

    public Long getStockId()
    {
        return stockId;
    }

    @Override
    public String toString() {
        return "TTaskOut{" +
                "id=" + id +
                ", outDeliveryId=" + outDeliveryId +
                ", outDeliveryDetailId='" + outDeliveryDetailId + '\'' +
                ", materialId=" + materialId +
                ", predictCount=" + predictCount +
                ", actualCount=" + actualCount +
                ", status='" + status + '\'' +
                ", stockId=" + stockId +
                ", trayId=" + trayId +
                ", locationId=" + locationId +
                '}';
    }
}
