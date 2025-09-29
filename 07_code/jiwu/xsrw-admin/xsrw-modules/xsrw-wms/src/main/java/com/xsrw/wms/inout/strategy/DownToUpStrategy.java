package com.xsrw.wms.inout.strategy;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.base.domain.TLocation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 策略由下到上
 */
public class DownToUpStrategy implements PutWayStrategy {

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
                // 过滤结果选定某一层
                Map<Long, List<TLocation>> groupByList = new HashMap<>();
                groupByList.put(zCoord, zList);
                WCSStrategyResult.setGroupByList(groupByList);
            }
        }
        return WCSStrategyResult;
    }
}
