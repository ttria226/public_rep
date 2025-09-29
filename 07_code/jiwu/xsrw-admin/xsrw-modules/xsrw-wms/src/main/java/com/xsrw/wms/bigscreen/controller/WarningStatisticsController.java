package com.xsrw.wms.bigscreen.controller;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.bigscreen.service.WarningStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 大屏预警统计
 * @Author XMING
 * @Date 2023-11-22
 */
@RestController
@RequestMapping("/bigscreen/warn")
public class WarningStatisticsController {


    @Autowired
    private WarningStatisticsService warningStatisticsService;


    /**
     * 物资库龄分析
     * @return
     */
    @GetMapping("/storageAge")
    public AjaxResult storageAge(){
        return warningStatisticsService.storageAge();
    }


    /**
     * 物资库龄情况
     * @return
     */
    @GetMapping("/storageAgeInfo")
    public AjaxResult storageAgeInfo(){
        return warningStatisticsService.storageAgeInfo();
    }


    /**
     * 最低库存预警
     * @return
     */
    @GetMapping("/minimumStock")
    public AjaxResult minimumStock(){
        return warningStatisticsService.minimumStock();
    }


    /**
     * 最高库存预警
     * @return
     */
    @GetMapping("/maximumStock")
    public AjaxResult maximumStock(){
        return warningStatisticsService.maximumStock();
    }

}
