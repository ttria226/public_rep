package com.xsrw.wms.inout.strategy;

/**
 * 标准上架策略接口
 */
public interface PutWayStrategy {

    WCSStrategyResult recommended(WCSStrategyParam param);

}
