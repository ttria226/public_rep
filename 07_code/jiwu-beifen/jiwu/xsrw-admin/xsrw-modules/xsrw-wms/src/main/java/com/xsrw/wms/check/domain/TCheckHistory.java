package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 盘点历史记录对象 t_check_history
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_check_history")
public class TCheckHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 任务详情id */
    @Excel(name = "任务详情id")
    private Long taskDetailId;

    /** 库存id */
    @Excel(name = "库存id")
    private Long stockId;

    /** 库存数量 */
    @Excel(name = "库存数量")
    private Long predictCount;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private Long actualCount;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchNumber;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 托盘id */
    @Excel(name = "托盘id")
    private Long trayId;

    /**
     * rfid
     */
    private String rfidHead;

    /**
     * rfid对应数量
     */
    private Integer rfidHeadCount;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTaskDetailId(Long taskDetailId)
    {
        this.taskDetailId = taskDetailId;
    }

    public Long getTaskDetailId()
    {
        return taskDetailId;
    }
    public void setStockId(Long stockId)
    {
        this.stockId = stockId;
    }

    public Long getStockId()
    {
        return stockId;
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
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }
    public void setMaterialCode(String materialCode)
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode()
    {
        return materialCode;
    }
    public void setTrayId(Long trayId)
    {
        this.trayId = trayId;
    }

    public Long getTrayId()
    {
        return trayId;
    }

    public String getRfidHead() {
        return rfidHead;
    }

    public void setRfidHead(String rfidHead) {
        this.rfidHead = rfidHead;
    }

    public Integer getRfidHeadCount() {
        return rfidHeadCount;
    }

    public void setRfidHeadCount(Integer rfidHeadCount) {
        this.rfidHeadCount = rfidHeadCount;
    }


    @Override
    public String toString() {
        return "TCheckHistory{" +
                "id=" + id +
                ", taskDetailId=" + taskDetailId +
                ", stockId=" + stockId +
                ", predictCount=" + predictCount +
                ", actualCount=" + actualCount +
                ", materialId=" + materialId +
                ", batchNumber='" + batchNumber + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", trayId=" + trayId +
                ", rfidHead='" + rfidHead + '\'' +
                ", rfidHeadCount=" + rfidHeadCount +
                '}';
    }
}
