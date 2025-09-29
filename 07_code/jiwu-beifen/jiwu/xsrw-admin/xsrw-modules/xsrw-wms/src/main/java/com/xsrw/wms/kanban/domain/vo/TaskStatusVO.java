package com.xsrw.wms.kanban.domain.vo;


/**
 * 任务状态统计返回参数类
 */
public class TaskStatusVO {



    /**
     * 未完成数量
     */
    private int notCompleteCount;


    /**
     * 进行中数量
     */
    private int underwayCount;

    /**
     *  已完成数量
     */
    private int hasCompleteCount;

    public int getNotCompleteCount() {
        return notCompleteCount;
    }

    public void setNotCompleteCount(int notCompleteCount) {
        this.notCompleteCount = notCompleteCount;
    }

    public int getUnderwayCount() {
        return underwayCount;
    }

    public void setUnderwayCount(int underwayCount) {
        this.underwayCount = underwayCount;
    }

    public int getHasCompleteCount() {
        return hasCompleteCount;
    }

    public void setHasCompleteCount(int hasCompleteCount) {
        this.hasCompleteCount = hasCompleteCount;
    }
}
