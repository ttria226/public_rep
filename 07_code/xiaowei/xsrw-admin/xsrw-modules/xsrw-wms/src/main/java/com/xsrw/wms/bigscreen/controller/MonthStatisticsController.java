package com.xsrw.wms.bigscreen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.bigscreen.service.MonthStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 大屏当月统计
 * @Author XMING
 * @Date 2023-11-21
 */
@RestController
@RequestMapping("/bigscreen/month")
public class MonthStatisticsController {


    @Autowired
    private MonthStatisticsService monthStatisticsService;


    /**
     * 当月出入库总额
     * @return
     */
    @GetMapping("/inOutMoney")
    public AjaxResult inOutMoney(){
        return monthStatisticsService.inOutMoney();
    }


    /**
     * 重点物资月入出情况
     * @return
     */
    @GetMapping("/keyPointMaterial")
    public AjaxResult keyPointMaterial(){
        return monthStatisticsService.keyPointMaterial();
    }


    /**
     * 入库金额
     * @return
     */
    @GetMapping("/inNumber")
    public AjaxResult inNumber(){
        return monthStatisticsService.inNumber();
    }

    /**
     * 出库金额
     * @return
     */
    @GetMapping("/outNumber")
    public AjaxResult outNumber(){
        return monthStatisticsService.outNumber();
    }




}
