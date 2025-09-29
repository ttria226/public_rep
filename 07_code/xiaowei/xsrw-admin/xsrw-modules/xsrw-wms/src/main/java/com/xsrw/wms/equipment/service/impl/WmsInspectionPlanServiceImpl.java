package com.xsrw.wms.equipment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.equipment.domain.*;
import com.xsrw.wms.equipment.mapper.*;
import com.xsrw.wms.equipment.utils.WeekDayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.xsrw.common.core.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.xsrw.wms.equipment.service.IWmsInspectionPlanService;

/**
 * 巡检计划Service业务层处理
 *
 * @author zjj
 * @date 2023-05-08
 */
@Service
public class WmsInspectionPlanServiceImpl extends ServiceImpl<WmsInspectionPlanMapper, WmsInspectionPlan> implements IWmsInspectionPlanService
{
    @Autowired
    private WmsInspectionPlanMapper wmsInspectionPlanMapper;
    @Autowired
    private WmsInspectionPlanDetailMapper wmsInspectionPlanDetailMapper;
    @Autowired
    private DInspectionPlanDayMapper dayMapper;
    @Autowired
    private WmsEquipmentMapper equipmentMapper;
    @Autowired
    private DInspectionItemsMapper itemsMapper;


    /**
     * 查询巡检计划列表
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 巡检计划
     */
    @Override
    public List<WmsInspectionPlan> selectWmsInspectionPlanList(WmsInspectionPlan wmsInspectionPlan)
    {
        List<WmsInspectionPlan> wmsInspectionPlans = wmsInspectionPlanMapper.selectWmsInspectionPlanList(wmsInspectionPlan);
        for (WmsInspectionPlan inspectionPlan : wmsInspectionPlans) {
            Long inspector = inspectionPlan.getInspector();
            inspectionPlan.setInspectorName(wmsInspectionPlanMapper.getuserName(inspector));
        }
        return wmsInspectionPlans;
    }

    /**
     * 查询巡检计划
     *
     * @param id 巡检计划主键
     * @return 巡检计划
     */
    @Override
    public WmsInspectionPlan selectWmsInspectionPlanById(Long id)
    {
        WmsInspectionPlan wmsInspectionPlan = wmsInspectionPlanMapper.selectById(id);
        List<WmsInspectionPlanDetail> wmsInspectionPlanDetails = wmsInspectionPlanDetailMapper.selectList(new LambdaQueryWrapper<WmsInspectionPlanDetail>()
                .eq(WmsInspectionPlanDetail::getPlanId, id)
                .eq(BaseEntity::getDelFlag, 0));
        if (wmsInspectionPlanDetails.size()>0){
            for (WmsInspectionPlanDetail wmsInspectionPlanDetail : wmsInspectionPlanDetails) {
                WmsEquipment wmsEquipment = equipmentMapper.selectById(wmsInspectionPlanDetail.getEquipmentId());
                wmsInspectionPlanDetail.setName(wmsEquipment.getName());
                wmsInspectionPlanDetail.setEquNo(wmsEquipment.getEquNo());
                wmsInspectionPlanDetail.setSerialNo(wmsEquipment.getSerialNo());
                wmsInspectionPlanDetail.setFunctionLocation(wmsEquipment.getfunctionLocation());
                DInspectionItems dInspectionItems = itemsMapper.selectOne(
                        new LambdaQueryWrapper<DInspectionItems>().eq(DInspectionItems::getEquipmentId, wmsInspectionPlanDetail.getEquipmentId()));
                wmsInspectionPlanDetail.setInspectionItems(dInspectionItems.getName());
            }
            wmsInspectionPlan.setWmsInspectionPlanDetailList(wmsInspectionPlanDetails);
        }
        return wmsInspectionPlan;
    }

    /**
     * 新增巡检计划
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertWmsInspectionPlan(WmsInspectionPlan wmsInspectionPlan)
    {
        /*向计划表中插入数据*/
        /*向计划所包含的设备表中插入数据*/
        List<WmsInspectionPlanDetail> wmsInspectionPlanDetailList = wmsInspectionPlan.getWmsInspectionPlanDetailList();
        if (wmsInspectionPlanDetailList.size()==0){
            return AjaxResult.error("请选择设备");
        }
        List<WmsInspectionPlanDetail> collect = wmsInspectionPlanDetailList.stream().filter(distinctByKey(WmsInspectionPlanDetail::getId)).collect(Collectors.toList());
        wmsInspectionPlanMapper.insert(wmsInspectionPlan);
        for (WmsInspectionPlanDetail wmsInspectionPlanDetail : collect) {
            WmsInspectionPlanDetail detail = new WmsInspectionPlanDetail();
            String[] ignoreProperties= {"id"};
            BeanUtils.copyProperties(wmsInspectionPlanDetail, detail,ignoreProperties);
            detail.setPlanId(wmsInspectionPlan.getId());
            detail.setCreateTime(new Date());
            wmsInspectionPlanDetailMapper.insert(detail);
        }
        return AjaxResult.success();

    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * 修改巡检计划
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult updateWmsInspectionPlan(WmsInspectionPlan wmsInspectionPlan)
    {
        WmsInspectionPlan plan = wmsInspectionPlanMapper.selectById(wmsInspectionPlan.getId());
        if (plan.getStatus() == 2){
            return AjaxResult.error("当前计划已启用，无法编辑！");
        }
        wmsInspectionPlanMapper.updateById(wmsInspectionPlan);
        wmsInspectionPlanDetailMapper.delete(new LambdaQueryWrapper<WmsInspectionPlanDetail>()
                .eq(WmsInspectionPlanDetail::getPlanId, wmsInspectionPlan.getId()));//删除之前所选设备
        if (wmsInspectionPlan.getWmsInspectionPlanDetailList().size()>0){//新增所选设备
            for (WmsInspectionPlanDetail wmsInspectionPlanDetail : wmsInspectionPlan.getWmsInspectionPlanDetailList()) {
                WmsInspectionPlanDetail detail = new WmsInspectionPlanDetail();
                String[] ignoreProperties= {"id"};
                BeanUtils.copyProperties(wmsInspectionPlanDetail, detail,ignoreProperties);
                detail.setPlanId(wmsInspectionPlan.getId());
                wmsInspectionPlanDetailMapper.insert(detail);
            }
        }
        return AjaxResult.success();
    }


    /**
     * 批量删除巡检计划
     *
     * @param ids 需要删除的巡检计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsInspectionPlanByIds(Long[] ids)
    {
        wmsInspectionPlanMapper.deleteWmsInspectionPlanDetailByPlanIds(ids);
        return wmsInspectionPlanMapper.deleteWmsInspectionPlanByIds(ids);
    }

    /**
     * 删除巡检计划信息
     *
     * @param id 巡检计划主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteWmsInspectionPlanById(Long id)
    {
        wmsInspectionPlanMapper.deleteWmsInspectionPlanDetailByPlanId(id);
        return wmsInspectionPlanMapper.deleteWmsInspectionPlanById(id);
    }

    @Override
    public AjaxResult startPlan(Long id) throws Exception {
        WmsInspectionPlan wmsInspectionPlan = wmsInspectionPlanMapper.selectById(id);
        if (wmsInspectionPlan.getStatus() == 2){
            return AjaxResult.error("当前计划已启用！");
        }
        if (wmsInspectionPlan.getStatus() == 0){
            return AjaxResult.error("当前计划已作废！");
        }
        /*根据所选周期生成对应计划*/
        Integer type = wmsInspectionPlan.getType();//巡检周期类型 1：周 2：按月
        if (type == 1){//根据开始时间和结束时间获取对应星期
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String planStartTime = simpleDateFormat.format(wmsInspectionPlan.getPlanStartTime());
            String planEndTime = simpleDateFormat.format(wmsInspectionPlan.getPlanEndTime());
            String[] dates = WeekDayUtil.getDates(planStartTime, planEndTime, wmsInspectionPlan.getmonthOrDay());
            for (String date : dates) {
                DInspectionPlanDay day = new DInspectionPlanDay();
                day.setDay(date);
                day.setPlanId(wmsInspectionPlan.getId());
                day.setInspectionStartTime(wmsInspectionPlan.getInspectionStartTime());
                day.setInspectionEndTime(wmsInspectionPlan.getInspectionEndTime());
                day.setInspector(wmsInspectionPlan.getInspector());
                day.setInspectorName(equipmentMapper.getUserByUserId(wmsInspectionPlan.getInspector().toString()));

                day.setInspectorTrue(wmsInspectionPlan.getInspector());
                day.setInspectorTrueName(equipmentMapper.getUserByUserId(wmsInspectionPlan.getInspector().toString()));

                day.setSignType(wmsInspectionPlan.getSignType());
                dayMapper.insert(day);
            }
            wmsInspectionPlan.setStatus(2);//设置计划状态为 已启用
            wmsInspectionPlanMapper.updateById(wmsInspectionPlan);
        } else if (type == 2) { //
            String yearMonth = wmsInspectionPlan.getYearOrMonth();
            String[] dates = wmsInspectionPlan.getmonthOrDay().split(",");
            for (String date : dates) {
                if (Integer.valueOf(date)<10){
                    date = 0+date;
                }
                DInspectionPlanDay day = new DInspectionPlanDay();
                day.setDay(yearMonth+"-"+date);
                day.setPlanId(wmsInspectionPlan.getId());
                day.setInspectionStartTime(wmsInspectionPlan.getInspectionStartTime());
                day.setInspectionEndTime(wmsInspectionPlan.getInspectionEndTime());
                day.setInspector(wmsInspectionPlan.getInspector());
                day.setInspectorName(equipmentMapper.getUserByUserId(wmsInspectionPlan.getInspector().toString()));
                day.setInspectorTrue(wmsInspectionPlan.getInspector());
                day.setInspectorTrueName(equipmentMapper.getUserByUserId(wmsInspectionPlan.getInspector().toString()));
                day.setSignType(wmsInspectionPlan.getSignType());
                dayMapper.insert(day);
            }
            wmsInspectionPlan.setStatus(2);//设置计划状态为 已启用
            wmsInspectionPlanMapper.updateById(wmsInspectionPlan);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult endPlan(Long id) throws Exception {
        WmsInspectionPlan wmsInspectionPlan = wmsInspectionPlanMapper.selectById(id);
        if (wmsInspectionPlan.getStatus() == 0){
            return AjaxResult.error("当前计划已作废！");
        }
        List<DInspectionPlanDay> dInspectionPlanDays = dayMapper.selectList(new LambdaQueryWrapper<DInspectionPlanDay>().eq(DInspectionPlanDay::getPlanId, id).eq(DInspectionPlanDay::getStatus,0));
        if (dInspectionPlanDays.size()>0){
            for (DInspectionPlanDay dInspectionPlanDay : dInspectionPlanDays) {
                dInspectionPlanDay.setStatus(4);
                dayMapper.updateById(dInspectionPlanDay);
            }
        }
        wmsInspectionPlan.setStatus(0);
        wmsInspectionPlanMapper.updateById(wmsInspectionPlan);
        return AjaxResult.success();
    }

    /**
     * 新增巡检计划设备列信息
     *
     * @param wmsInspectionPlan 巡检计划对象
     */
    public void insertWmsInspectionPlanDetail(WmsInspectionPlan wmsInspectionPlan)
    {
        List<WmsInspectionPlanDetail> wmsInspectionPlanDetailList = wmsInspectionPlan.getWmsInspectionPlanDetailList();
        Long id = wmsInspectionPlan.getId();
        if (StringUtils.isNotNull(wmsInspectionPlanDetailList))
        {
            List<WmsInspectionPlanDetail> list = new ArrayList<WmsInspectionPlanDetail>();
            for (WmsInspectionPlanDetail wmsInspectionPlanDetail : wmsInspectionPlanDetailList)
            {
                wmsInspectionPlanDetail.setPlanId(id);
                list.add(wmsInspectionPlanDetail);
            }
            if (list.size() > 0)
            {
                wmsInspectionPlanMapper.batchWmsInspectionPlanDetail(list);
            }
        }
    }

}
