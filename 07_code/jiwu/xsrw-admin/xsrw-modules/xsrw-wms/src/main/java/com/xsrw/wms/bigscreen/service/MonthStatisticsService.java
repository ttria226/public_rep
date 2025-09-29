package com.xsrw.wms.bigscreen.service;

import com.xsrw.common.core.web.domain.AjaxResult;

public interface MonthStatisticsService {


    /**
     * 当月出入库总额
     * @return
     */
    AjaxResult inOutMoney();


    /**
     * 重点物资月入出情况
     * @return
     */
    AjaxResult keyPointMaterial();


    /**
     * 入库金额
     * @return
     */
    AjaxResult inNumber();


    /**
     * 出库金额
     * @return
     */
    AjaxResult outNumber();

}
