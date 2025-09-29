package com.xsrw.wms.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.equipment.domain.*;
import com.xsrw.wms.equipment.mapper.*;
import com.xsrw.wms.equipment.service.IDInspectionPlanDayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 巡检记录Service业务层处理
 *
 * @author zjj
 * @date 2023-05-09
 */
@Service
public class DInspectionPlanDayServiceImpl extends ServiceImpl<DInspectionPlanDayMapper, DInspectionPlanDay> implements IDInspectionPlanDayService
{
    @Autowired
    private DInspectionPlanDayMapper dInspectionPlanDayMapper;
    @Autowired
    private DInspectionItemsMapper itemsMapper;
    @Autowired
    private DInspectionDayInfoMapper dayInfoMapper;

    @Autowired
    private WmsInspectionPlanMapper planMapper;

    @Autowired
    private WmsEquipmentMapper equipmentMapper;


    /**
     * 查询巡检记录列表
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 巡检记录
     */
    @Override
    public List<DInspectionPlanDay> selectDInspectionPlanDayList(DInspectionPlanDay dInspectionPlanDay)
    {
        List<DInspectionPlanDay> dInspectionPlanDays = dInspectionPlanDayMapper.selectDInspectionPlanDayList(dInspectionPlanDay);
        for (DInspectionPlanDay inspectionPlanDay : dInspectionPlanDays) {
            List<DInspectionDayInfo> dInspectionDayInfos = dayInfoMapper.selectList(new LambdaQueryWrapper<DInspectionDayInfo>().eq(DInspectionDayInfo::getDayId, inspectionPlanDay.getId()));
            inspectionPlanDay.setIsrepair(0);
            for (DInspectionDayInfo dInspectionDayInfo : dInspectionDayInfos) {
                if (dInspectionDayInfo.getStatus() == 2){
                    inspectionPlanDay.setIsrepair(1);
                }
            }
        }
        if (CollectionUtils.isEmpty(dInspectionPlanDays)){
            return dInspectionPlanDays;
        }
        List<Long> planIdList = dInspectionPlanDays.stream().map(DInspectionPlanDay::getPlanId).collect(Collectors.toList());
        List<WmsInspectionPlan> wmsInspectionPlans = planMapper.selectBatchIds(planIdList);
        if (CollectionUtils.isEmpty(wmsInspectionPlans)){
            return dInspectionPlanDays;
        }
        Map<Long, String> wmsInspectionPlanMap = wmsInspectionPlans.stream().collect(Collectors.toMap(WmsInspectionPlan::getId, WmsInspectionPlan::getName));
        dInspectionPlanDays.forEach(p->p.setPlanName(wmsInspectionPlanMap.get(p.getPlanId())));
        return dInspectionPlanDays;
    }

    /**
     * 查询巡检记录
     *
     * @param id 巡检记录主键
     * @return 巡检记录
     */
    @Override
    public DInspectionPlanDay selectDInspectionPlanDayById(Long id)
    {
        return dInspectionPlanDayMapper.selectById(id);
    }

    /**
     * 新增巡检记录
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 结果
     */
    @Override
    public int insertDInspectionPlanDay(DInspectionPlanDay dInspectionPlanDay)
    {
        return dInspectionPlanDayMapper.insert(dInspectionPlanDay);
    }

    /**
     * 修改巡检记录
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 结果
     */
    @Override
    public int updateDInspectionPlanDay(DInspectionPlanDay dInspectionPlanDay)
    {
        return dInspectionPlanDayMapper.updateById(dInspectionPlanDay);
    }


    /**
     * 批量删除巡检记录
     *
     * @param ids 需要删除的巡检记录主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionPlanDayByIds(Long[] ids)
    {
        return dInspectionPlanDayMapper.deleteDInspectionPlanDayByIds(ids);
    }

    /**
     * 删除巡检记录信息
     *
     * @param id 巡检记录主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionPlanDayById(Long id)
    {
        return dInspectionPlanDayMapper.deleteDInspectionPlanDayById(id);
    }

    @Override
    public AjaxResult exchange(String id, String userid, String reason, String username) {
        DInspectionPlanDay day = dInspectionPlanDayMapper.selectById(Long.valueOf(id));
        if (day.getStatus() == 1){
            return AjaxResult.error("此工单已完成，无法调班！");
        }
        day.setInspectorTrue(Long.valueOf(userid));
        day.setInspectorTrueName(equipmentMapper.getUserByUserId(userid.toString()));
        if (StringUtils.isNotBlank(reason)){
            day.setReason(reason);
        }
        dInspectionPlanDayMapper.updateById(day);
        return AjaxResult.success();
    }

    @Override
    public List<DInspectionPlanDay> appPlanList() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(date);
        QueryWrapper<DInspectionPlanDay> queryWrapper = new QueryWrapper<DInspectionPlanDay>();
        queryWrapper.eq("a.inspector_true", SecurityUtils.getUserId());
        queryWrapper.eq("a.day",time);//获取当天任务
        List<DInspectionPlanDay> dInspectionPlanDays = dInspectionPlanDayMapper.appPlanList(queryWrapper);
        for (DInspectionPlanDay dInspectionPlanDay : dInspectionPlanDays) {
            dInspectionPlanDay.setPlanName(planMapper.selectById(dInspectionPlanDay.getPlanId()).getName());
            dInspectionPlanDay.setInspectorName(planMapper.getuserName(dInspectionPlanDay.getInspector()));
            dInspectionPlanDay.setInspectionLine(planMapper.selectById(dInspectionPlanDay.getPlanId()).getInspectionLine());
        }
        return dInspectionPlanDays;
    }

    @Override
    public List<WmsInspectionPlanDetail> getInfoApp(Long planid,Long dayid) {
        List<WmsInspectionPlanDetail> infoApp = dInspectionPlanDayMapper.getInfoApp(planid);
        for (WmsInspectionPlanDetail detail : infoApp) {
            List<DInspectionItems> isadd = itemsMapper.isadd(detail.getId().toString());
            if (isadd.size()>0){
                detail.setContent(isadd.get(0).getRemark());
            }
            DInspectionDayInfo dInspectionDayInfos = dayInfoMapper.selectOne(new LambdaQueryWrapper<DInspectionDayInfo>()
                    .eq(DInspectionDayInfo::getDayId, dayid)
                    .eq(DInspectionDayInfo::getEquipmentId, detail.getId()));
            if (dInspectionDayInfos!=null){
                if (dInspectionDayInfos.getStatus() == 1){
                    detail.setStatus(1);
                } else if (dInspectionDayInfos.getStatus() == 2) {
                    detail.setStatus(3);
                }
            }else {
                detail.setStatus(2);
            }
        }
        return infoApp;
    }


}
