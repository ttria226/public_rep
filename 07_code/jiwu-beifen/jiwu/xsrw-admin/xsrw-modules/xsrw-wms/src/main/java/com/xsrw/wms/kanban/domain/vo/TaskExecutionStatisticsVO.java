package com.xsrw.wms.kanban.domain.vo;


/**
 * 任务执行情况返回参数类
 */
public class TaskExecutionStatisticsVO {


    /**
     * 入库任务数量
     */
    private Integer inTaskCount;

    /**
     * 出库任务数量
     */
    private Integer outTaskCount;


    /**
     * 移库任务数量
     */
    private Integer moveTaskCount;

    /**
     *  时间
     */
    private String statisticsTime;

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

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


    public Integer getMoveTaskCount() {
        return moveTaskCount;
    }

    public void setMoveTaskCount(Integer moveTaskCount) {
        this.moveTaskCount = moveTaskCount;
    }
}
