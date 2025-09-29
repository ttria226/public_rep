package com.xsrw.wms.bigscreen.service.impl;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.bigscreen.service.WarningStatisticsService;
import com.xsrw.wms.stock.mapper.TStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 大屏预警统计
 * @Author XMING
 * @Date 2023-11-22
 */
@Service
public class WarningStatisticsServiceImpl implements WarningStatisticsService {


    @Autowired
    private TStockMapper stockMapper;


    /**
     * 物资库龄分析
     * @return
     */
    @Override
    public AjaxResult  storageAge() {

        Integer oneYear = stockMapper.storageAge("1");
        Integer oneThreeYear = stockMapper.storageAge("2");
        Integer threeFiveYear = stockMapper.storageAge("3");
        Integer fiveYear = stockMapper.storageAge("4");

        List<Map<String,Object>> result = new ArrayList<>();

        Map<String,Object> oneYearMap = new HashMap<>();
        oneYearMap.put("name","一年以下");
        oneYearMap.put("value",oneYear);

        Map<String,Object> oneThreeYearMap = new HashMap<>();
        oneThreeYearMap.put("name","一年到三年");
        oneThreeYearMap.put("value",oneThreeYear);

        Map<String,Object> threeFiveYearMap = new HashMap<>();
        threeFiveYearMap.put("name","三年到五年");
        threeFiveYearMap.put("value",threeFiveYear);

        Map<String,Object> fiveYearMap = new HashMap<>();
        fiveYearMap.put("name","五年以上");
        fiveYearMap.put("value",fiveYear);

        result.add(oneYearMap);
        result.add(oneThreeYearMap);
        result.add(threeFiveYearMap);
        result.add(fiveYearMap);

        return AjaxResult.success(result);
    }


    /**
     * 物资库龄情况
     * @return
     */
    @Override
    public AjaxResult storageAgeInfo() {

        // 一年以下
        Map<String, Object> oneYear = stockMapper.storageAgeInfo("1");
        oneYear.put("date","一年以下");
        // 一年到三年
        Map<String, Object> oneThreeYear = stockMapper.storageAgeInfo("2");
        oneThreeYear.put("date","一年到三年");
        // 三年到五年
        Map<String, Object> threeFiveYear = stockMapper.storageAgeInfo("3");
        threeFiveYear.put("date","三年到五年");
        // 五年以上
        Map<String, Object> fiveYear = stockMapper.storageAgeInfo("4");
        fiveYear.put("date","五年以上");

        List<Map<String,Object>> result = new ArrayList<>();
        result.add(oneYear);
        result.add(oneThreeYear);
        result.add(threeFiveYear);
        result.add(fiveYear);

        return AjaxResult.success(result);
    }


    /**
     * 最低库存预警
     * @return
     */
    @Override
    public AjaxResult minimumStock() {
        List<Map<String, Object>> minimumStock = stockMapper.minimumStock();
        return AjaxResult.success(minimumStock);
    }

    /**
     * 最高库存预警
     * @return
     */
    @Override
    public AjaxResult maximumStock() {
        List<Map<String, Object>> maximumStock = stockMapper.maximumStock();
        return AjaxResult.success(maximumStock);
    }
}
