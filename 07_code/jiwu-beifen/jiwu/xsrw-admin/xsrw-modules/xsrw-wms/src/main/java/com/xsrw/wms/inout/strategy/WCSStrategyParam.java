package com.xsrw.wms.inout.strategy;

import com.xsrw.wms.base.domain.TLocation;

import java.util.List;
import java.util.Map;

public class WCSStrategyParam {

    /**
     * 已经排序过的候选列表
     * key: 层数
     * value: 当层库位列表
     */
    private Map<Long, List<TLocation>> groupByList;

    /**
     * 是否最后一个策略，0：否 1：是
     */
    private Integer isLast = 0;

    /**
     * 重量时指定
     */
    private Float weight;

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Map<Long, List<TLocation>> getGroupByList() {
        return groupByList;
    }

    public void setGroupByList(Map<Long, List<TLocation>> groupByList) {
        this.groupByList = groupByList;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }
}
