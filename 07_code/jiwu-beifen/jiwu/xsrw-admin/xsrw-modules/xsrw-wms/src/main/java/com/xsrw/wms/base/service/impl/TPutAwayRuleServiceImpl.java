package com.xsrw.wms.base.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TPutAwayRuleDetail;
import com.xsrw.wms.base.domain.vo.TPutAwayRuleVO;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITPutAwayRuleDetailService;
import com.xsrw.wms.inout.strategy.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TPutAwayRuleMapper;
import com.xsrw.wms.base.domain.TPutAwayRule;
import com.xsrw.wms.base.service.ITPutAwayRuleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上架策略Service业务层处理
 *
 * @author wxr
 * @date 2023-05-06
 */
@Service
public class TPutAwayRuleServiceImpl extends ServiceImpl<TPutAwayRuleMapper, TPutAwayRule> implements ITPutAwayRuleService {
    @Autowired
    private TPutAwayRuleMapper tPutAwayRuleMapper;
    @Autowired
    private ITPutAwayRuleDetailService itPutAwayRuleDetailService;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TTrayMapper tTrayMapper;
    /**
     * 查询上架策略列表
     *
     * @param tPutAwayRule 上架策略
     * @return 上架策略
     */
    @Override
    public List<TPutAwayRule> selectTPutAwayRuleList(TPutAwayRule tPutAwayRule) {
        return tPutAwayRuleMapper.selectTPutAwayRuleList(tPutAwayRule);
    }

    /**
     * 查询上架策略
     *
     * @param id 上架策略主键
     * @return 上架策略
     */
    @Override
    public TPutAwayRuleVO selectTPutAwayRuleById(Long id) {
        TPutAwayRule tPutAwayRule = tPutAwayRuleMapper.selectById(id);
        TPutAwayRuleVO tPutAwayRuleVO = new TPutAwayRuleVO();
        BeanUtils.copyProperties(tPutAwayRule, tPutAwayRuleVO);
        tPutAwayRuleVO.setPutAwayRuleDetailList(itPutAwayRuleDetailService.selectDetailListByRuleId(id, false));
        return tPutAwayRuleVO;
    }

    /**
     * 新增上架策略
     *
     * @param tPutAwayRule 上架策略
     * @return 结果
     */
    @Override
    public int insertTPutAwayRule(TPutAwayRuleVO tPutAwayRule) {
        tPutAwayRuleMapper.insert(tPutAwayRule);
        //添加子表数据
        List<TPutAwayRuleDetail> putAwayRuleDetailList = tPutAwayRule.getPutAwayRuleDetailList();
        Long id = tPutAwayRule.getId();
        if (!CollectionUtils.isEmpty(putAwayRuleDetailList)) {
            List<TPutAwayRuleDetail> list = new ArrayList<>();
            for (TPutAwayRuleDetail putAwayRuleDetail : putAwayRuleDetailList) {
                putAwayRuleDetail.setId(null);
                putAwayRuleDetail.setRuleId(id);
                list.add(putAwayRuleDetail);
            }
            if (!CollectionUtils.isEmpty(list)) {
                itPutAwayRuleDetailService.saveBatch(list);
            }
        }
        return 1;
    }

    /**
     * 修改上架策略
     *
     * @param tPutAwayRule 上架策略
     * @return 结果
     */
    @Override
    public int updateTPutAwayRule(TPutAwayRuleVO tPutAwayRule) {

        tPutAwayRuleMapper.updateById(tPutAwayRule);
        // 子表数据全部删除
        itPutAwayRuleDetailService.deleteDetailByRuleId(tPutAwayRule.getId());
        //添加子表数据
        List<TPutAwayRuleDetail> putAwayRuleDetailList = tPutAwayRule.getPutAwayRuleDetailList();
        Long id = tPutAwayRule.getId();
        if (!CollectionUtils.isEmpty(putAwayRuleDetailList)) {
            List<TPutAwayRuleDetail> list = new ArrayList<>();
            for (TPutAwayRuleDetail putAwayRuleDetail : putAwayRuleDetailList) {
                putAwayRuleDetail.setId(null);
                putAwayRuleDetail.setRuleId(id);
                list.add(putAwayRuleDetail);
            }
            if (!CollectionUtils.isEmpty(list)) {
                itPutAwayRuleDetailService.saveBatch(list);
            }
        }
        return 1;
    }


    /**
     * 批量删除上架策略
     *
     * @param ids 需要删除的上架策略主键
     * @return 结果
     */
    @Override
    public int deleteTPutAwayRuleByIds(Long[] ids) {
        return tPutAwayRuleMapper.deleteTPutAwayRuleByIds(ids);
    }

    /**
     * 删除上架策略信息
     *
     * @param id 上架策略主键
     * @return 结果
     */
    @Override
    public int deleteTPutAwayRuleById(Long id) {
        return tPutAwayRuleMapper.deleteTPutAwayRuleById(id);
    }


    private Map<Integer, PutWayStrategy> strategyMap = new HashMap<>();

    {
        strategyMap.put(0, new DownToUpStrategy());
        strategyMap.put(1, new LeftToRightStrategy());
        strategyMap.put(2, new WeightStrategy());
    }

    /**
     * 推荐库位
     * @param removeLocations 要剔除的库位
     * @param trayId 载具id
     * @param materialCategorys 物料类别
     * @return
     */
    @Transactional
    public Long recommendedLocation(List<Long> removeLocations, Long trayId, Set<Long> materialCategorys) {

        //1.根据载具有没有库位id,如果有直接返回库位id;
        //2.如果没有库位id,查找库位（启用状态、非地堆、无货、没有托盘的）;
        //todo wxr 且托盘没有被绑定的库位
        //3.如果有物料类别，只有一个物料类别的时候，根据类别推荐

        if(trayId != null){
            Long locationId = tTrayMapper.getLocationInfoById(trayId);
            if(locationId != null){
                return locationId;
            }
        }

        //查询可用库位列表-先注释
//        QueryWrapper<TLocation> locationQw = new QueryWrapper<>();
//        locationQw.eq("del_flag", Constants.DEL_FLAG_NO);
//        locationQw.eq("status", Constants.LOCATION_STATUS_YES);
//        locationQw.eq("location_type", "0");//非地堆
//        locationQw.eq("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
//        locationQw.and(wq -> {
//            wq.isNull("pallet_num")
//                    .or()
//                    .eq("pallet_num", "");
//        });
//        if (materialCategorys != null && materialCategorys.size() == 1) {
//            //添加物料类别,多个类别不查询，只有一个物料类别的时候，根据类别推荐
//            locationQw.and(i -> i.isNull("deposit_category_id").or().in("deposit_category_id", materialCategorys));
//            locationQw.orderByDesc("deposit_category_id");
//        }
//        if (!CollectionUtils.isEmpty(removeLocations)) {
//            locationQw.notIn("id", removeLocations);
//        }
//        List<TLocation> locationList = locationService.list(locationQw);
        //查询可用库位列表-现在使用
        TLocation locationDTO = new TLocation();
        locationDTO.setStatus(Constants.LOCATION_STATUS_YES);
        locationDTO.setLocationType("0");
        locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
        if (materialCategorys != null && materialCategorys.size() == 1) {
            //添加物料类别,多个类别不查询，只有一个物料类别的时候，根据类别推荐
            Long depositCategoryId = materialCategorys.iterator().next();
            locationDTO.setDepositCategoryId(depositCategoryId);
        }
        List<TLocation> locationList = tLocationMapper.getRecommendedLocationList(locationDTO, removeLocations);
        List<Integer> putWayRule;

        // 获取上架策略
        QueryWrapper<TPutAwayRule> ruleQw = new QueryWrapper<>();
        ruleQw.eq("del_flag", Constants.DEL_FLAG_NO);
        ruleQw.eq("status", 1);
        TPutAwayRule putAwayRule = tPutAwayRuleMapper.selectOne(ruleQw);

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
}
