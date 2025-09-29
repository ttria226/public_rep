package com.xsrw.wms.bigscreen.service;

import com.xsrw.common.core.web.domain.AjaxResult;

public interface WarningStatisticsService {


    /**
     * 物资库龄分析
     * @return
     */
    AjaxResult storageAge();


    /**
     * 物资库龄情况
     * @return
     */
    AjaxResult storageAgeInfo();


    /**
     * 最低库存预警
     * @return
     */
    AjaxResult minimumStock();


    /**
     * 最高库存预警
     * @return
     */
    AjaxResult maximumStock();


}
