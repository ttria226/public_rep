package com.xsrw.wms.inout.strategy;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.base.domain.TLocation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 策略-按重量上架
 */
public class WeightStrategy implements PutWayStrategy {
    @Override
    public WCSStrategyResult recommended(WCSStrategyParam param) {

        WCSStrategyResult result = new WCSStrategyResult();

        if (param != null && !CollectionUtils.isEmpty(param.getGroupByList())) {

            List<Long> zCoordList = param.getGroupByList().keySet().stream().sorted().collect(Collectors.toList());

            // 根据重量阈值取得每层重量区间
            BigDecimal perWeight = new BigDecimal(1000).divide(new BigDecimal(zCoordList.size()), 0, RoundingMode.HALF_UP);

            for (int i = 0; i < zCoordList.size(); i++) {
                Long zCoord = zCoordList.get(i);

                if ((param.getWeight() < perWeight.multiply(new BigDecimal(zCoordList.size() - i)).floatValue()) && param.getWeight() > perWeight.multiply(new BigDecimal(zCoordList.size() - i - 1)).floatValue()) {
                    Map<Long, List<TLocation>> groupByList = new HashMap<>();
                    groupByList.put(zCoord, param.getGroupByList().get(zCoord));
                    result.setGroupByList(groupByList);

                    if (param.getIsLast() == 1) {
                        result.setResult(param.getGroupByList().get(zCoord).get(0));
                    }
                    break;
                }
            }
        }
        return result;
    }
}
