package com.xsrw.wms.inout.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.LocationConstants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.vo.TRulePutawayVO;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TPutAwayRuleMapper;
import com.xsrw.wms.base.mapper.TRulePutawayMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITPutAwayRuleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/13 16:08
 */
@Component
public class RecommendedLocationUtil {

    @Autowired
    private TPutAwayRuleMapper tPutAwayRuleMapper;
    @Autowired
    private ITPutAwayRuleDetailService itPutAwayRuleDetailService;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TTrayMapper tTrayMapper;
    @Autowired
    private TRulePutawayMapper tRulePutawayMapper;


    private Map<Integer, PutWayStrategy> strategyMap = new HashMap<>();

    {
        strategyMap.put(0, new DownToUpStrategy());
        strategyMap.put(1, new LeftToRightStrategy());
        strategyMap.put(2, new WeightStrategy());
    }

    /**
     * 推荐库位
     *
     * @param removeLocations   要剔除的库位
     * @param trayId            载具id
     * @param materialCategorys 物料类别
     * @return
     */
    @Transactional
    public Long recommendedLocationOld(List<Long> removeLocations, Long trayId, Set<Long> materialCategorys, List<Long> materialIds) {

        //1.根据载具有没有库位id,如果有直接返回库位id;
        //2.如果没有库位id,查找库位（启用状态、非地堆、无货、没有托盘的）;
        //todo wxr 且托盘没有被绑定的库位
        //3.如果有物料类别，只有一个物料类别的时候，根据类别推荐

        String flooType = "";//库位楼层
        if (trayId != null) {
            TTray trayVO = tTrayMapper.getLocationDetailById(trayId);
            if (trayVO != null) {
                if(trayVO.getLocationId() != null){
                    return trayVO.getLocationId();
                }
                if(Constants.TRAY_CATEGORY_TRAY.equals(trayVO.getTrayCategory())){
                    flooType = Constants.LOCATION_FLOOR_FIRST;
                }else if(Constants.TRAY_CATEGORY_WORKBIN.equals(trayVO.getTrayCategory())){
                    flooType = Constants.LOCATION_FLOOR_SECOND;
                }else {
                    //货笼
                    flooType = Constants.LOCATION_FLOOR_FIRST;
                }
            }
        }

        //根据物料查询指定库位
        Long materialLocation = this.traversalMaterialLocation(materialIds);
        if (materialLocation != null) {
            if(materialLocation == 0L){
                //物料指定了库位，但库位不在可用状态
                return null;
            }else{
                return materialLocation;
            }
        }
        //查询可用库位列表-现在使用
        TLocation locationDTO = new TLocation();
        locationDTO.setStatus(Constants.LOCATION_STATUS_YES);
        locationDTO.setLocationType("0");
        locationDTO.setFloorType(flooType);
        locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
        if (materialCategorys != null && materialCategorys.size() == 1) {
            //添加物料类别,多个类别不查询，只有一个物料类别的时候，根据类别推荐
            Long depositCategoryId = materialCategorys.iterator().next();
            locationDTO.setDepositCategoryId(depositCategoryId);
        }
        List<TLocation> locationList = tLocationMapper.getRecommendedLocationList(locationDTO, removeLocations);
        List<Integer> putWayRule;

        // 获取上架策略
        TPutAwayRule putAwayRule = null;

        QueryWrapper<TPutAwayRule> ruleQw = new QueryWrapper<>();
        ruleQw.eq("del_flag", Constants.DEL_FLAG_NO);
        ruleQw.eq("status", 1);
        ruleQw.orderByDesc("create_time");
        List<TPutAwayRule> putAwayRules = tPutAwayRuleMapper.selectList(ruleQw);
        if(CollectionUtils.isNotEmpty(putAwayRules)){
            putAwayRule = putAwayRules.get(0);
        }

        // 没有设定上架策略,默认从下至上，从左到右
        if (putAwayRule == null || putAwayRule.getId() == null) {
            putWayRule = Arrays.asList(new Integer[]{0, 1});
        } else {
            List<TPutAwayRuleDetail> tAwayRuleDetailList = itPutAwayRuleDetailService.selectDetailListByRuleId(putAwayRule.getId(), true);
            putWayRule =
                    tAwayRuleDetailList.stream()
                            .sorted(Comparator.comparing(TPutAwayRuleDetail::getRuleOrder))
                            .map(TPutAwayRuleDetail::getRule)
                            .collect(Collectors.toList());
        }

        WCSStrategyParam param = new WCSStrategyParam();
        param.setGroupByList(locationList.stream().collect(Collectors.groupingBy(TLocation::getLocationPlies)));
        for (int i = 0; i < putWayRule.size(); i++) {
            if (i == putWayRule.size() - 1) {
                param.setIsLast(1);
            }
            WCSStrategyResult result = strategyMap.get(putWayRule.get(i)).recommended(param);
            if (result.getResult() != null) {
                return result.getResult().getId();
            }
            param.setGroupByList(result.getGroupByList());
        }

        return null;
    }
    /**
     * 推荐库位
     *
     * @param removeLocations   要剔除的库位
     * @param trayId            载具id
     * @param materialCategorys 物料类别
     * @return
     */
    @Transactional
    public synchronized Long recommendedLocation(List<Long> removeLocations, Long trayId, Set<Long> materialCategorys, List<Long> materialIds) {

        //1.根据载具有没有库位id,如果有直接返回库位id;
        //2.如果没有库位id,查找库位（启用状态、非地堆、无货、没有托盘的）;
        //todo wxr 且托盘没有被绑定的库位
        //3.如果有物料类别，只有一个物料类别的时候，根据类别推荐

        String flooType = "";//库位楼层
        if (trayId != null) {
            TTray trayVO = tTrayMapper.getLocationDetailById(trayId);
            if (trayVO != null) {
                if (trayVO.getLocationId() != null) {
                    return trayVO.getLocationId();
                }
                if (Constants.TRAY_CATEGORY_TRAY.equals(trayVO.getTrayCategory())) {
                    flooType = Constants.LOCATION_FLOOR_FIRST;
                } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(trayVO.getTrayCategory())) {
                    flooType = Constants.LOCATION_FLOOR_SECOND;
                }else {
                    //货笼
                    flooType = Constants.LOCATION_FLOOR_FIRST;
                }
            }
        }

        //根据物料查询指定库位
//        Long materialLocation = this.traversalMaterialLocation(materialIds);
//        if (materialLocation != null) {
//            if (materialLocation == 0L) {
//                //物料指定了库位，但库位不在可用状态
//                return null;
//            } else {
//                return materialLocation;
//            }
//        }
        //查询可用库位列表-现在使用
        TLocation locationDTO = new TLocation();
        locationDTO.setStatus(Constants.LOCATION_STATUS_YES);
        locationDTO.setLocationType("0");
        locationDTO.setFloorType(flooType);
        locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
//        if (materialCategorys != null && materialCategorys.size() == 1) {
//            //添加物料类别,多个类别不查询，只有一个物料类别的时候，根据类别推荐
//            Long depositCategoryId = materialCategorys.iterator().next();
//            locationDTO.setDepositCategoryId(depositCategoryId);
//        }
        List<TLocation> locationList = tLocationMapper.getRecommendedLocationList(locationDTO, removeLocations);

        if (Constants.LOCATION_FLOOR_FIRST.equals(flooType)) {
            //一楼
            return recommendedLocationFirst(locationList);
        } else {
            //二楼
            return recommendedLocationSecond(locationList);
        }
    }

    /**
     * 为移库推荐库位
     *
     * @return
     */
    public synchronized TLocation recommendedLocationForMove(List<Long> removeLocations, Long piles) {
        TLocation dataVO = null;
        //1、查询可用库位列表
        TLocation locationDTO = new TLocation();
        locationDTO.setStatus(Constants.LOCATION_STATUS_YES);
        locationDTO.setLocationType("0");
        locationDTO.setFloorType(Constants.LOCATION_FLOOR_FIRST);
        locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
        locationDTO.setLocationPlies(piles);//几层
        List<TLocation> locationList = tLocationMapper.getRecommendedLocationList(locationDTO,removeLocations);
        if (CollectionUtils.isNotEmpty(locationList)) {
            TLocation locationTemp = null;//临时存放第一个1伸位
            //2.1、去除暂存位
            List<TLocation> locationListOther = locationList.stream().filter(e -> !LocationConstants.WORKING_STORAGE_BIT.equals(e.getPalletNodeId())).collect(Collectors.toList());
            if (!locationListOther.isEmpty()) {
                //3、根据同组库位分组（一伸位id）,遍历库位
                //(用LinkedHashMap解决无序问题)
                LinkedHashMap<String, List<TLocation>> locationMap = locationListOther.stream().collect(Collectors.groupingBy(TLocation::getExtentionFristId, LinkedHashMap::new, Collectors.toList()));
                for (Map.Entry<String, List<TLocation>> entry : locationMap.entrySet()) {
                    String key = entry.getKey();
                    List<TLocation> list = entry.getValue();
                    //3.1、如果1伸位2伸位都为空，优先推荐此库位的2伸位
                    if (list != null && list.size() == 2) {
                        List<TLocation> secondLocationList = list.stream().filter(e -> e.getExtentionType().equals(2)).collect(Collectors.toList());
                        if (!secondLocationList.isEmpty()) {
                            dataVO = secondLocationList.get(0);
                            break;
                        }
                    }
                    if (locationTemp == null) {
                        //3.2、设置临时1伸位的临时存放位置
                        List<TLocation> firstLocationList = list.stream().filter(e -> e.getExtentionType().equals(1)).collect(Collectors.toList());
                        if (firstLocationList != null && firstLocationList.size() == 1) {
                            locationTemp = firstLocationList.get(0);
                        }
                    }
                }
                //3.3、未获取到两伸位都空的时候，直接推荐第一个1伸位
                if (dataVO == null) {
                    dataVO = locationTemp;
                }
            }
            if(dataVO == null){
                //4、当所有库位都满，以及无推荐移库库位的时候，启用暂存位
                List<TLocation> locationListTemp = locationList.stream().filter(e -> LocationConstants.WORKING_STORAGE_BIT.equals(e.getPalletNodeId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(locationListTemp)) {
                    dataVO = locationListTemp.get(0);
                }
            }
        }
        return dataVO;
    }


    /**
     * 推荐库位-一楼
     *
     * @param locationList
     * @return
     */
    private Long recommendedLocationFirst(List<TLocation> locationList) {
        Long locationId = null;
        if (CollectionUtils.isNotEmpty(locationList)) {
            //去除暂存位
            List<TLocation> locationListOther = locationList.stream().filter(e -> !LocationConstants.WORKING_STORAGE_BIT.equals(e.getPalletNodeId())).collect(Collectors.toList());
            if (!locationListOther.isEmpty()) {
                //推荐第一个数据
                return locationListOther.get(0).getId();
            }
        }

        return locationId;
    }

    /**
     * 根据上架规则推荐库位，适用于二楼
     *
     * @param locationList
     * @return
     */
    private Long recommendedLocationSecond(List<TLocation> locationList) {

        List<Integer> putWayRule;

        // 获取上架策略
        TPutAwayRule putAwayRule = null;

        QueryWrapper<TPutAwayRule> ruleQw = new QueryWrapper<>();
        ruleQw.eq("del_flag", Constants.DEL_FLAG_NO);
        ruleQw.eq("status", 1);
        ruleQw.orderByDesc("create_time");
        List<TPutAwayRule> putAwayRules = tPutAwayRuleMapper.selectList(ruleQw);
        if (CollectionUtils.isNotEmpty(putAwayRules)) {
            putAwayRule = putAwayRules.get(0);
        }

        // 没有设定上架策略,默认从下至上，从左到右
        if (putAwayRule == null || putAwayRule.getId() == null) {
            putWayRule = Arrays.asList(new Integer[]{0, 1});
        } else {
            List<TPutAwayRuleDetail> tAwayRuleDetailList = itPutAwayRuleDetailService.selectDetailListByRuleId(putAwayRule.getId(), true);
            putWayRule =
                    tAwayRuleDetailList.stream()
                            .sorted(Comparator.comparing(TPutAwayRuleDetail::getRuleOrder))
                            .map(TPutAwayRuleDetail::getRule)
                            .collect(Collectors.toList());
        }

        WCSStrategyParam param = new WCSStrategyParam();
        param.setGroupByList(locationList.stream().collect(Collectors.groupingBy(TLocation::getLocationPlies)));
        for (int i = 0; i < putWayRule.size(); i++) {
            if (i == putWayRule.size() - 1) {
                param.setIsLast(1);
            }
            WCSStrategyResult result = strategyMap.get(putWayRule.get(i)).recommended(param);
            if (result.getResult() != null) {
                return result.getResult().getId();
            }
            param.setGroupByList(result.getGroupByList());
        }
        return null;
    }


    /**
     * 遍历处理物料指定库位
     *
     * @param materialIds
     */
    public Long traversalMaterialLocation(List<Long> materialIds) {
        Long locationId = null;
        if (materialIds != null && materialIds.size() > 0) {
            List<List<TRulePutawayVO>> locationIdList = new ArrayList<>();
            List<TRulePutawayVO> dataList = tRulePutawayMapper.selectPutawayListByMaterIds(materialIds);
            if (CollectionUtils.isNotEmpty(dataList)) {
                Map<Long, List<TRulePutawayVO>> materialMap = dataList.stream().collect(Collectors.groupingBy(TRulePutawayVO::getMaterialId));
                materialMap.forEach((materialId, detailList) -> {
//                    List<Long> locationList = detailList.stream().map(TRulePutawayVO::getLocationId).distinct().collect(Collectors.toList());
//                    locationIdList.add(locationList);
                    locationIdList.add(detailList);
                });
            }
            List<TRulePutawayVO> location = getVOIntersection(locationIdList);
            if (CollectionUtils.isNotEmpty(location)) {
                List<TRulePutawayVO> unStockList = location.stream().filter(e -> StringUtils.isEmpty(e.getInStockStatus())).collect(Collectors.toList());
                if (unStockList.isEmpty()) {
                    //交集库位都已在库，无可用库位
                    locationId = 0L;
                } else {
                    locationId = unStockList.get(0).getLocationId();
                }
            }
        }
        return locationId;
    }

    /**
     * 从有值的list中取交集
     *
     * @param lists
     * @return
     */
    public static List<Long> getIntersection(List<List<Long>> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }
        ArrayList<List<Long>> arrayList = new ArrayList<>(lists);
        for (int i = 0; i < arrayList.size(); i++) {
            List<Long> list = arrayList.get(i);
            // 去除空集合
            if (list == null || list.size() == 0) {
                arrayList.remove(list);
                i--;
            }
        }
        // 都是空集合，返回null
        if (arrayList.size() == 0) {
            return null;
        }
        List<Long> intersection = arrayList.get(0);
        // 只有一个非空集合，结果就是它本身
        if (arrayList.size() == 1) {
            return intersection;
        }
        // 有多个非空集合，直接挨个求交集
        for (int i = 1; i < arrayList.size(); i++) {
            intersection.retainAll(arrayList.get(i));
        }
        return intersection;
    }

    public static List<TRulePutawayVO> getVOIntersection(List<List<TRulePutawayVO>> lists) {
        if (lists == null || lists.size() == 0) {
            return null;
        }
        ArrayList<List<TRulePutawayVO>> arrayList = new ArrayList<>(lists);
        for (int i = 0; i < arrayList.size(); i++) {
            List<TRulePutawayVO> list = arrayList.get(i);
            // 去除空集合
            if (list == null || list.size() == 0) {
                arrayList.remove(list);
                i--;
            }
        }
        // 都是空集合，返回null
        if (arrayList.size() == 0) {
            return null;
        }
        List<TRulePutawayVO> intersection = arrayList.get(0);
        // 只有一个非空集合，结果就是它本身
        if (arrayList.size() == 1) {
            return intersection;
        }
        // 有多个非空集合，直接挨个求交集
        for (int i = 1; i < arrayList.size(); i++) {
            List<TRulePutawayVO> dataList = arrayList.get(i);
            intersection = intersection.stream().filter(x -> dataList.stream().map(d -> d.getLocationId()).collect(Collectors.toList()).contains(x.getLocationId())).collect(Collectors.toList());
        }
        return intersection;
    }

}
