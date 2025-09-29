package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 库存盘点对象 t_task_detail
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_task_detail")
public class TTaskDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务标识 */
    @Excel(name = "任务标识")
    private Long taskId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private Long predictCount;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private Long actualCount;

    /** 状态(0未完成，1进行中 2已完成 3审核中  4已审核  5已驳回) */
    @Excel(name = "状态(0未完成，1进行中 2已完成 3审核中  4已审核  5已驳回)")
    private String status;

    /** 载具(托盘id) */
    @Excel(name = "载具(托盘id)")
    private Long trayId;

    /** 库位标识 */
    @Excel(name = "库位标识")
    private Long locationId;

    /** 移库用原库位id */
    @Excel(name = "移库用原库位id")
    private Long orgLocationId;

    /** 值班人员 */
    @Excel(name = "值班人员")
    private String dutyPersonnel;

    /** 批号 */
    @Excel(name = "批号")
    private String batchNumber;

    /** 库存标识 */
    @Excel(name = "库存标识")
    private Long stockId;

    /** 复盘次数 */
    @Excel(name = "复盘次数")
    private Long checkCount;

    /**
     * 来源类型（1-出库计划 2-波次计划 3-空盘上架 4-物料盘点 5-库区盘点 6-区域盘点）
     */
    private String deliveryType;

    /**
     * wcs任务id
     */
    private Long wcsId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getTaskId()
    {
        return taskId;
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
    public void setOrgLocationId(Long orgLocationId)
    {
        this.orgLocationId = orgLocationId;
    }

    public Long getOrgLocationId()
    {
        return orgLocationId;
    }
    public void setDutyPersonnel(String dutyPersonnel)
    {
        this.dutyPersonnel = dutyPersonnel;
    }

    public String getDutyPersonnel()
    {
        return dutyPersonnel;
    }
    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }
    public void setStockId(Long stockId)
    {
        this.stockId = stockId;
    }

    public Long getStockId()
    {
        return stockId;
    }
    public void setCheckCount(Long checkCount)
    {
        this.checkCount = checkCount;
    }

    public Long getCheckCount()
    {
        return checkCount;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getWcsId() {
        return wcsId;
    }

    public void setWcsId(Long wcsId) {
        this.wcsId = wcsId;
    }


    @Override
    public String toString() {
        return "TTaskDetail{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", materialId=" + materialId +
                ", predictCount=" + predictCount +
                ", actualCount=" + actualCount +
                ", status='" + status + '\'' +
                ", trayId=" + trayId +
                ", locationId=" + locationId +
                ", orgLocationId=" + orgLocationId +
                ", dutyPersonnel='" + dutyPersonnel + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", stockId=" + stockId +
                ", checkCount=" + checkCount +
                ", deliveryType='" + deliveryType + '\'' +
                ", wcsId=" + wcsId +
                '}';
    }
}
