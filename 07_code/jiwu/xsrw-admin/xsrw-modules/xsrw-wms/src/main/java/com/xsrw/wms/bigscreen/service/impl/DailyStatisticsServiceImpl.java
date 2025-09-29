package com.xsrw.wms.bigscreen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.bigscreen.service.DailyStatisticsService;
import com.xsrw.wms.inout.mapper.TTaskInMapper;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 大屏当日统计
 * @Author XMING
 * @Date 2023-11-20
 */
@Service
public class DailyStatisticsServiceImpl implements DailyStatisticsService {

    @Autowired
    private TTaskInMapper taskInMapper;

    @Autowired
    private TTaskOutMapper taskOutMapper;

    @Autowired
    private TStockMapper stockMapper;

    @Autowired
    private TLocationMapper locationMapper;

    @Autowired
    private TMaterialMapper materialMapper;

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
    @Override
    public AjaxResult numStatistic() {

        // 今日入库数量
        Long inNum = taskInMapper.getNowDayNum();

        // 今日出库数量
        Long outNum = taskOutMapper.getNowDayNum();

        // 实时库存
        BigDecimal realTimeStock = stockMapper.selectOne(
                new QueryWrapper<TStock>()
                        .select(" ifnull(sum(count),0) as count")
                        // 可用
                        .eq("status", "0")
                        .eq("del_flag", Constants.DEL_FLAG_NO)).getCount();

        // 总库位数量
        Long locationNumAll = locationMapper.selectCount(
                new QueryWrapper<TLocation>()
                        .eq("del_flag", Constants.DEL_FLAG_NO)
        );

        // 已用库位
        Long locationNumUse = locationMapper.selectCount(
                new QueryWrapper<TLocation>()
                        // 查询非无货状态的库位
                        .ne("goods_allocation_status", "1")
                        .eq("del_flag", Constants.DEL_FLAG_NO));

        // 总库位/已用库位 占比
        String locationRatio = "0%";
        if (locationNumAll.intValue() != 0){
            locationRatio = new BigDecimal(locationNumUse.toString())
                    .divide(new BigDecimal(locationNumAll.toString()),2,BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(0)+"%";
        }

        // 库存总额
        BigDecimal stockTotalAamount = stockMapper.stockTotalAamount();

        // 物资种类
        Long materialCategoryNum = materialMapper.selectOne(
                new QueryWrapper<TMaterial>()
                        .select(" count(distinct category_id) as category_id")
                        .eq("del_flag", Constants.DEL_FLAG_NO)).getCategoryId();


        Map<String,Object> result = new HashMap<>();
        result.put("inNum",inNum);
        result.put("outNum",outNum);
        result.put("realTimeStock",realTimeStock);
        result.put("locationNumAll",locationNumAll);
        result.put("locationNumUse",locationNumUse);
        result.put("locationRatio",locationRatio);
        result.put("stockTotalAamount",stockTotalAamount);
        result.put("materialCategoryNum",materialCategoryNum);
        return AjaxResult.success(result);
    }


    /**
     * 库存类别比例
     * @return
     */
    @Override
    public AjaxResult categoryRatio() {
        List<Map<String,Object>> result = new ArrayList<>();

        List<Map<String, Object>> common = stockMapper.categoryRatio("通用");
        List<Map<String, Object>> ele = stockMapper.categoryRatio("电力");
        List<Map<String, Object>> hnx = stockMapper.categoryRatio("内燃");

        List<Map<String, Object>> all = stockMapper.categoryRatio(null);

        Map<String,Object> commonMap = new HashMap<>();
        Map<String,Object> eleMap = new HashMap<>();
        Map<String,Object> hnxMap = new HashMap<>();
        Map<String,Object> otherMap = new HashMap<>();

/*        long commonCount = common.stream().mapToLong(e -> Long.valueOf(e.get("value").toString())).sum();
        commonMap.put("name","通用");
        commonMap.put("value",commonCount);

        long eleCount = ele.stream().mapToLong(e -> Long.valueOf(e.get("value").toString())).sum();
        eleMap.put("name","电力");
        eleMap.put("value",eleCount);

        long hnxCount = hnx.stream().mapToLong(e -> Long.valueOf(e.get("value").toString())).sum();
        hnxMap.put("name","内燃");
        hnxMap.put("value",hnxCount);

        long allCount = all.stream().mapToLong(e -> Long.valueOf(e.get("value").toString())).sum();
        otherMap.put("name","其它");
        otherMap.put("value",allCount-commonCount-eleCount-hnxCount);*/

        // 计算 commonCount
        BigDecimal commonCount = common.stream()
                .map(e -> new BigDecimal(e.get("value").toString())) // 将值转换为 BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 使用 reduce 进行求和
        commonMap.put("name", "通用");
        commonMap.put("value", commonCount); // 放入 BigDecimal

// 计算 eleCount
        BigDecimal eleCount = ele.stream()
                .map(e -> new BigDecimal(e.get("value").toString())) // 将值转换为 BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 使用 reduce 进行求和
        eleMap.put("name", "电力");
        eleMap.put("value", eleCount); // 放入 BigDecimal

// 计算 hnxCount
        BigDecimal hnxCount = hnx.stream()
                .map(e -> new BigDecimal(e.get("value").toString())) // 将值转换为 BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 使用 reduce 进行求和
        hnxMap.put("name", "内燃");
        hnxMap.put("value", hnxCount); // 放入 BigDecimal

// 计算 allCount 和 otherCount
        BigDecimal allCount = all.stream()
                .map(e -> new BigDecimal(e.get("value").toString())) // 将值转换为 BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add); // 使用 reduce 进行求和

// 计算 otherCount，使用 BigDecimal 的 subtract 方法
        BigDecimal otherCount = allCount.subtract(commonCount).subtract(eleCount).subtract(hnxCount);
        otherMap.put("name", "其它");
        otherMap.put("value", otherCount); // 放入 BigDecimal

        result.add(commonMap);
        result.add(eleMap);
        result.add(hnxMap);
        result.add(otherMap);
        return AjaxResult.success(result);
    }


    /**
     * 库存位置比例
     * @return
     */
    @Override
    public AjaxResult locationTypeRatio() {

        // 查询1号库库位(一楼托盘区域)
        Long oneLocation = locationMapper.selectCount(
                new QueryWrapper<TLocation>()
                        .eq("floor_type", "1")
                        .eq("del_flag", Constants.DEL_FLAG_NO));

        // 查询2号库库位(地堆区域)
        Long twoLocation = locationMapper.selectCount(
                new QueryWrapper<TLocation>()
                        .eq("location_type", "1")
                        .eq("del_flag", Constants.DEL_FLAG_NO));

        // 查询3号库库位(二楼料箱区域)
        Long threeLocation = locationMapper.selectCount(
                new QueryWrapper<TLocation>()
                        .eq("floor_type", "2")
                        .eq("del_flag", Constants.DEL_FLAG_NO));


        List<Map<String,Object>> result = new ArrayList<>();
        Map<String,Object> oneLocationMap = new HashMap<>();
        oneLocationMap.put("name","1号库");
        oneLocationMap.put("value",oneLocation);
        result.add(oneLocationMap);

        Map<String,Object> twoLocationMap = new HashMap<>();
        twoLocationMap.put("name","2号库");
        twoLocationMap.put("value",twoLocation);
        result.add(twoLocationMap);

        Map<String,Object> threeLocationMap = new HashMap<>();
        threeLocationMap.put("name","3号库");
        threeLocationMap.put("value",threeLocation);
        result.add(threeLocationMap);

        return AjaxResult.success(result);
    }


}
