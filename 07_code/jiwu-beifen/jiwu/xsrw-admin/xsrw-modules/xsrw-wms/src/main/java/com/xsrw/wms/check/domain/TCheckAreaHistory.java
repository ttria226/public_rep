package com.xsrw.wms.check.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 平库盘点提交历史对象 t_check_area_history
 *
 * @author lyx
 * @date 2023-05-11
 */
@TableName("t_check_area_history")
public class TCheckAreaHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 任务详情id */
    @Excel(name = "任务详情id")
    private Long taskDetailId;

    /** 物料编码(批次) */
    @Excel(name = "物料编码(批次)")
    private String batchNumber;

    /** 托盘编码 */
    @Excel(name = "托盘编码")
    private String trayCode;

    /** 实际数量 */
    @Excel(name = "实际数量")
    private Long actualCount;

    /** 是否提交   1是  0否 */
    @Excel(name = "是否提交   1是  0否")
    private String isDraft;

    /** 任务id */
    @Excel(name = "任务id")
    private Long taskId;

    /** 库存数量 */
    @Excel(name = "库存数量")
    private Long predictCount;

    /** 确认状态  1未确认  2已驳回  3已审核 */
    @Excel(name = "确认状态  1未确认  2已驳回  3已审核")
    private String status;

    /** 物料id */
    @Excel(name = "物料id")
    private Long materialId;

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
    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }
    public void setTrayCode(String trayCode)
    {
        this.trayCode = trayCode;
    }

    public String getTrayCode()
    {
        return trayCode;
    }
    public void setActualCount(Long actualCount)
    {
        this.actualCount = actualCount;
    }

    public Long getActualCount()
    {
        return actualCount;
    }
    public void setIsDraft(String isDraft)
    {
        this.isDraft = isDraft;
    }

    public String getIsDraft()
    {
        return isDraft;
    }
    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public Long getTaskId()
    {
        return taskId;
    }
    public void setPredictCount(Long predictCount)
    {
        this.predictCount = predictCount;
    }

    public Long getPredictCount()
    {
        return predictCount;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskDetailId", getTaskDetailId())
            .append("batchNumber", getBatchNumber())
            .append("trayCode", getTrayCode())
            .append("actualCount", getActualCount())
            .append("isDraft", getIsDraft())
            .append("taskId", getTaskId())
            .append("predictCount", getPredictCount())
            .append("status", getStatus())
            .append("materialId", getMaterialId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
