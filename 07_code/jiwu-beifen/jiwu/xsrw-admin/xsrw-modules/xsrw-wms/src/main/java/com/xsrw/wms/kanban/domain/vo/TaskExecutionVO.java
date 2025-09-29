package com.xsrw.wms.kanban.domain.vo;


import java.util.List;
import java.util.Map;

/**
 * 任务执行情况返回参数类
 */
public class TaskExecutionVO {


    private List<Map<String,Object>> dataList;
    /**
     *
     */
    private List<String> xList;

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public List<String> getxList() {
        return xList;
    }

    public void setxList(List<String> xList) {
        this.xList = xList;
    }
}
