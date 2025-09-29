package com.xsrw.wms.bigscreen.controller;

import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.bigscreen.service.DailyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description: 大屏当日统计
 * @Author XMING
 * @Date 2023-11-20
 */
@RestController
@RequestMapping("/bigscreen/day")
public class DailyStatisticsController {

    @Autowired
    private DailyStatisticsService dailyStatisticsService;


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
    @GetMapping("/numStatistic")
    public AjaxResult numStatistic(){
        return dailyStatisticsService.numStatistic();
    }


    /**
     * 库存类别比例
     * @return
     */
    @GetMapping("/categoryRatio")
    public AjaxResult categoryRatio(){
        return dailyStatisticsService.categoryRatio();
    }


    /**
     * 库存位置比例
     * @return
     */
    @GetMapping("/locationTypeRatio")
    public AjaxResult locationTypeRatio(){
        return dailyStatisticsService.locationTypeRatio();
    }


    /**
     * 获取当前时间
     * @return
     */
    @GetMapping("/getNowTime")
    public AjaxResult getNowTime(){
        String dateTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        return AjaxResult.success("",dateTime);
    }
}
