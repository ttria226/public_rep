package com.xsrw.wms.inout.strategy;

import com.xsrw.wms.base.domain.TLocation;

import java.util.List;
import java.util.Map;

public class WCSStrategyResult {

    /**
     * 推荐结果
     */
    private TLocation result;

    /**
     * 缩小范围的候选列表
     */
    private Map<Long, List<TLocation>> groupByList;

    public TLocation getResult() {
        return result;
    }

    public void setResult(TLocation result) {
        this.result = result;
    }

    public Map<Long, List<TLocation>> getGroupByList() {
        return groupByList;
    }

    public void setGroupByList(Map<Long, List<TLocation>> groupByList) {
        this.groupByList = groupByList;
    }
}
