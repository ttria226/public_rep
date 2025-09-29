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
 * wcs任务详情对象 t_task_wcs_detail
 *
 * @author wxr
 * @date 2023-05-10
 */
@TableName("t_task_wcs_detail")
public class TTaskWcsDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务标识
     */
    @Excel(name = "任务标识")
    private Long taskId;

    /**
     * 类型（1.入库任务2.出库任务）
     */
    @Excel(name = "类型", readConverterExp = "1=.入库任务2.出库任务")
    private String type;

    /**
     * 来源任务标识（task_in或task_out的标识）
     */
    @Excel(name = "来源任务标识", readConverterExp = "t=ask_in或task_out的标识")
    private Long originId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getOriginId() {
        return originId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("taskId", getTaskId())
                .append("type", getType())
                .append("originId", getOriginId())
                .toString();
    }
}
