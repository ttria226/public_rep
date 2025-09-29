package com.xsrw.wms.kanban.domain.vo;


/**
 * 任务看板返回参数类
 */
public class TaskKanbanVO {


    /**
     * 入库任务数量
     */
    private Integer inTaskCount;

    /**
     * 出库任务数量
     */
    private Integer outTaskCount;

    /**
     * 盘点任务数量
     */
    private Integer inventoryTaskCount;

    /**
     * 移库任务数量
     */
    private Integer moveTaskCount;

    public Integer getInTaskCount() {
        return inTaskCount;
    }

    public void setInTaskCount(Integer inTaskCount) {
        this.inTaskCount = inTaskCount;
    }

    public Integer getOutTaskCount() {
        return outTaskCount;
    }

    public void setOutTaskCount(Integer outTaskCount) {
        this.outTaskCount = outTaskCount;
    }

    public Integer getInventoryTaskCount() {
        return inventoryTaskCount;
    }

    public void setInventoryTaskCount(Integer inventoryTaskCount) {
        this.inventoryTaskCount = inventoryTaskCount;
    }

    public Integer getMoveTaskCount() {
        return moveTaskCount;
    }

    public void setMoveTaskCount(Integer moveTaskCount) {
        this.moveTaskCount = moveTaskCount;
    }
}
