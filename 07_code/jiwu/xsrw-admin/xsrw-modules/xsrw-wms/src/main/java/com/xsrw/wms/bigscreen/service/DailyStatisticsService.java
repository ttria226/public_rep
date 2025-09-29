package com.xsrw.wms.bigscreen.service;

import com.xsrw.common.core.web.domain.AjaxResult;

public interface DailyStatisticsService {


    /**
     * 数量统计
     *
     * 今日入库数量
     * 今日出库数量
     * 实时库存
     * 总库位/已用库位 占比
     * 库存总额
     * 物资种类
     *
     * @return
     */
    AjaxResult numStatistic();


    /**
     * 库存类别比例
     * @return
     */
    AjaxResult categoryRatio();


    /**
     * 库存位置比例
     * @return
     */
    AjaxResult locationTypeRatio();


}
