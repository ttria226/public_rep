package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 出库任务详情对象 t_task_merge
 *
 * @author zjj
 * @date 2023-06-26
 */
@TableName("t_task_merge")
public class TTaskMerge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 波次单标识 */
    @Excel(name = "波次单标识")
    private Long mergeDeliveryId;

    /** 波次单详情标识 */
    @Excel(name = "波次单详情标识")
    private Long mergeDeliveryDetailId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private BigDecimal predictCount;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private BigDecimal actualCount;

    /** 状态(0未完成，1进行中 2已完成 ) */
    @Excel(name = "状态(0未完成，1进行中 2已完成 )")
    private String status;

    /** 库存 */
    @Excel(name = "库存")
    private Long stockId;

    /** wcs任务标识 */
    @Excel(name = "wcs任务标识")
    private Long wcsId;

    /** 载具 */
    @Excel(name = "载具")
    private Long trayId;

    /** 库位 */
    @Excel(name = "库位")
    private Long locationId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMergeDeliveryId(Long mergeDeliveryId)
    {
        this.mergeDeliveryId = mergeDeliveryId;
    }

    public Long getMergeDeliveryId()
    {
        return mergeDeliveryId;
    }
    public void setMergeDeliveryDetailId(Long mergeDeliveryDetailId)
    {
        this.mergeDeliveryDetailId = mergeDeliveryDetailId;
    }

    public Long getMergeDeliveryDetailId()
    {
        return mergeDeliveryDetailId;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
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
    public void setWcsId(Long wcsId)
    {
        this.wcsId = wcsId;
    }

    public Long getWcsId()
    {
        return wcsId;
    }
    public void setTrayId(Long trayId)
    {
        this.trayId = trayId;
    }

    public Long getTrayId()
    {
        return trayId;
    }
    public void setLocationId(Long locationId)
    {
        this.locationId = locationId;
    }

    public Long getLocationId()
    {
        return locationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mergeDeliveryId", getMergeDeliveryId())
            .append("mergeDeliveryDetailId", getMergeDeliveryDetailId())
            .append("materialId", getMaterialId())
            .append("predictCount", getPredictCount())
            .append("actualCount", getActualCount())
            .append("status", getStatus())
            .append("stockId", getStockId())
            .append("wcsId", getWcsId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .append("trayId", getTrayId())
            .append("locationId", getLocationId())
            .toString();
    }
}
