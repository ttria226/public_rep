package com.xsrw.wms.bigscreen.service.impl;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.bigscreen.service.MonthStatisticsService;
import com.xsrw.wms.inout.mapper.TTaskInMapper;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 大屏当月统计
 * @Author XMING
 * @Date 2023-11-21
 */
@Service
public class MonthStatisticsImpl implements MonthStatisticsService {

    @Autowired
    private TTaskInMapper taskInMapper;

    @Autowired
    private TTaskOutMapper taskOutMapper;

    @Autowired
    private TStockDetailMapper stockDetailMapper;

    /**
     * 当月出入库总额
     * @return
     */
    @Override
    public AjaxResult inOutMoney() {

        // 入库总额
        BigDecimal inMonthMoney = taskInMapper.inMonthMoney();
        // 出库总额
        BigDecimal outMonthMoney = taskOutMapper.outMonthMoney();

        Map<String,Object> result = new HashMap<>();
        result.put("inMonthMoney",inMonthMoney);
        result.put("outMonthMoney",outMonthMoney);
        return AjaxResult.success(result);
    }



    /**
     * 重点物资月入出情况
     * @return
     */
    @Override
    public AjaxResult keyPointMaterial() {

        // 入库情况
        List<Map<String, Object>> inKeyPointMaterial = taskInMapper.inKeyPointMaterial();
        // 出库情况
        List<Map<String, Object>> outKeyPointMaterial = taskOutMapper.outKeyPointMaterial();

        if (inKeyPointMaterial.size() == 0 && outKeyPointMaterial.size() == 0){
            return AjaxResult.success(new ArrayList<>());
        }
        if (inKeyPointMaterial.size() > 0 && outKeyPointMaterial.size() == 0){
            inKeyPointMaterial.forEach(e -> {
                e.put("outAmount",0);
                e.put("outActualCount",0);
            });
            return AjaxResult.success(inKeyPointMaterial);
        }
        if (inKeyPointMaterial.size() == 0 && outKeyPointMaterial.size() > 0){
            outKeyPointMaterial.forEach(e -> {
                e.put("inAmount",0);
                e.put("inActualCount",0);
            });
            return AjaxResult.success(outKeyPointMaterial);
        }
        if (inKeyPointMaterial.size() > 0 && outKeyPointMaterial.size() > 0){

            List<Map<String,Object>> result = new ArrayList<>();

            List<String> materialIdIn = inKeyPointMaterial.stream().map(e -> e.get("materialId").toString()).collect(Collectors.toList());
            List<String> materialIdOut = outKeyPointMaterial.stream().map(e -> e.get("materialId").toString()).collect(Collectors.toList());

            List<String> unionList = new ArrayList<>(CollectionUtils.union(materialIdIn,materialIdOut));
            unionList.forEach(materialId -> {
                Map<String,Object> map = new HashMap<>();

                inKeyPointMaterial.forEach(in -> {
                    if (materialId.equals(in.get("materialId").toString())){
                        map.put("inAmount",in.get("inAmount"));
                        map.put("inActualCount",in.get("inActualCount"));
                        map.put("materialName",in.get("materialName"));
                    }
                });

                outKeyPointMaterial.forEach(out -> {
                    if (materialId.equals(out.get("materialId").toString())){
                        map.put("outAmount",out.get("outAmount"));
                        map.put("outActualCount",out.get("outActualCount"));
                        map.put("materialName",out.get("materialName"));
                    }
                });

                result.add(map);
            });


            result.forEach(e -> {
                if (e.get("inAmount") == null){
                    e.put("inAmount",0);
                    e.put("inActualCount",0);
                }
                if (e.get("outAmount") == null){
                    e.put("outAmount",0);
                    e.put("outActualCount",0);
                }
            });

            return AjaxResult.success(result);
        }

        return AjaxResult.success(new ArrayList<>());
    }


    /**
     * 入库金额
     * @return
     */
    @Override
    public AjaxResult inNumber() {

        List<String> dateList = new ArrayList<>();
        dateList.add("1月");
        dateList.add("2月");
        dateList.add("3月");
        dateList.add("4月");
        dateList.add("5月");
        dateList.add("6月");
        dateList.add("7月");
        dateList.add("8月");
        dateList.add("9月");
        dateList.add("10月");
        dateList.add("11月");
        dateList.add("12月");

        List<Object> numList = new ArrayList<>();

        List<Map<String, Object>> inMonthNum = stockDetailMapper.inMonthNum();
        if (inMonthNum.size() > 0){

            for (int i = 0; i < dateList.size(); i++) {
                String date = dateList.get(i);

                boolean flag = true;
                for (int j = 0; j < inMonthNum.size(); j++) {
                    Map<String, Object> in = inMonthNum.get(j);

                    int month = Integer.parseInt(in.get("month").toString().substring(5));
                    int replace = Integer.parseInt(date.replace("月", ""));
                    if (replace == month){
                        numList.add(in.get("count"));
                        flag = false;
                    }
                }

                if (flag){
                    numList.add(0);
                }
            }

        }else {
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("numList",numList);

        return AjaxResult.success(map);
    }


    /**
     * 出库金额
     * @return
     */
    @Override
    public AjaxResult outNumber() {
        List<String> dateList = new ArrayList<>();
        dateList.add("1月");
        dateList.add("2月");
        dateList.add("3月");
        dateList.add("4月");
        dateList.add("5月");
        dateList.add("6月");
        dateList.add("7月");
        dateList.add("8月");
        dateList.add("9月");
        dateList.add("10月");
        dateList.add("11月");
        dateList.add("12月");

        List<Object> numList = new ArrayList<>();

        List<Map<String, Object>> inMonthNum = stockDetailMapper.outMonthNum();
        if (inMonthNum.size() > 0){

            for (int i = 0; i < dateList.size(); i++) {
                String date = dateList.get(i);

                boolean flag = true;
                for (int j = 0; j < inMonthNum.size(); j++) {
                    Map<String, Object> in = inMonthNum.get(j);

                    int month = Integer.parseInt(in.get("month").toString().substring(5));
                    int replace = Integer.parseInt(date.replace("月", ""));
                    if (replace == month){
                        numList.add(in.get("count"));
                        flag = false;
                    }
                }

                if (flag){
                    numList.add(0);
                }
            }

        }else {
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
            numList.add(0);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("numList",numList);

        return AjaxResult.success(map);
    }


}
