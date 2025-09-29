package com.xsrw.wms.task.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * @Description: 任务查询 查询DTO
 * @Author tyk
 * @Date 2022-06-15
 */
public class TaskQueryDTO extends BaseEntity {

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 物料名称
     **/
    private String materialName;

    /**
     * 执行状态（1未执行,2执行中,3执行完成,4执行失败）
     **/
    private String taskStatus;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
