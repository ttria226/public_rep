package com.xsrw.wms.check.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 盘点任务对象 t_task
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_task")
public class TTaskCheckVO
{
     private Long id;


    private List<TaskDetailCheckVO> taskDetailCheckVOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TaskDetailCheckVO> getTaskDetailCheckVOS() {
        return taskDetailCheckVOS;
    }

    public void setTaskDetailCheckVOS(List<TaskDetailCheckVO> taskDetailCheckVOS) {
        this.taskDetailCheckVOS = taskDetailCheckVOS;
    }
}
