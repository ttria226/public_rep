package com.xsrw.wms.inout.strategy;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.base.domain.TLocation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 策略由近到远
 */
public class LeftToRightStrategy implements PutWayStrategy {

    @Override
    public WCSStrategyResult recommended(WCSStrategyParam param) {

        WCSStrategyResult WCSStrategyResult = new WCSStrategyResult();

        if(param != null && !CollectionUtils.isEmpty(param.getGroupByList())){
            List<Long> zCoordList = param.getGroupByList().keySet().stream().sorted().collect(Collectors.toList());

            // 取得最下层
            Long zCoord = zCoordList.get(0);
            List<TLocation> zList = param.getGroupByList().get(zCoord);

            // 如果是最后一个那么直接选定
            if (param.getIsLast() == 1) {
                // 取得最左边的(如果有多个选择最左边)
                TLocation result = zList.stream().sorted(Comparator.comparing(TLocation::getLocationRow)).collect(Collectors.toList()).get(0);
                WCSStrategyResult.setResult(result);
                return WCSStrategyResult;
            } else {
                Map<Long, List<TLocation>> groupByList = new HashMap<>();
                // 过滤结果每层选定最左
                for (Long temp : zCoordList) {
                    List<TLocation> tempList = param.getGroupByList().get(temp);
                    TLocation tempLocation = tempList.stream().sorted(Comparator.comparing(TLocation::getLocationRow)).collect(Collectors.toList()).get(0);
                    List<TLocation> sortedList = new ArrayList<>();
                    sortedList.add(tempLocation);
                    groupByList.put(temp, sortedList);
                }
                WCSStrategyResult.setGroupByList(groupByList);
            }
        }

        return WCSStrategyResult;
    }
}
