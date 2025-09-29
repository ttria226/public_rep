package com.xsrw.wms.equipment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.domain.maintenancePlanVO;
import com.xsrw.wms.equipment.mapper.DEquipmentMaintenanceDayMapper;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.equipment.utils.GenerateNumberUtil;
import com.xsrw.wms.equipment.utils.WeekDayUtil;
import com.xsrw.common.core.utils.bean.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DEquipmentMaintenancePlanMapper;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenancePlan;
import com.xsrw.wms.equipment.service.IDEquipmentMaintenancePlanService;

/**
 * 设备保养计划Service业务层处理
 *
 * @author zjj
 * @date 2023-05-11
 */
@Service
public class DEquipmentMaintenancePlanServiceImpl extends ServiceImpl<DEquipmentMaintenancePlanMapper, DEquipmentMaintenancePlan> implements IDEquipmentMaintenancePlanService
{
    @Autowired
    private DEquipmentMaintenancePlanMapper dEquipmentMaintenancePlanMapper;
    @Autowired
    private DEquipmentMaintenanceDayMapper dEquipmentMaintenanceDayMapper;

    @Autowired
    private WmsEquipmentMapper equipmentMapper;

    @Autowired
    private GenerateNumberUtil generateNumberUtil;

    @Autowired
    private ITCodeConfigService codeConfigService;


    /**
     * 查询设备保养计划列表
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 设备保养计划
     */
    @Override
    public List<DEquipmentMaintenancePlan> selectDEquipmentMaintenancePlanList(DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        List<DEquipmentMaintenancePlan> plans = dEquipmentMaintenancePlanMapper.selectDEquipmentMaintenancePlanList(dEquipmentMaintenancePlan);
        for (DEquipmentMaintenancePlan plan : plans) {
            plan.setEquName(equipmentMapper.selectById(plan.getEquipmentId()).getName());
            plan.setModel(equipmentMapper.selectById(plan.getEquipmentId()).getModel());
        }
        return plans;
    }

    /**
     * 查询设备保养计划
     *
     * @param id 设备保养计划主键
     * @return 设备保养计划
     */
    @Override
    public DEquipmentMaintenancePlan selectDEquipmentMaintenancePlanById(Long id)
    {
        DEquipmentMaintenancePlan dEquipmentMaintenancePlan = dEquipmentMaintenancePlanMapper.selectById(id);
        dEquipmentMaintenancePlan.setEquName(equipmentMapper.selectById(dEquipmentMaintenancePlan.getEquipmentId()).getName());
        List<maintenancePlanVO> planVOList = new ArrayList<>();
        List<DEquipmentMaintenanceDay> dayList = dEquipmentMaintenanceDayMapper.selectList(new LambdaQueryWrapper<DEquipmentMaintenanceDay>()
                .eq(DEquipmentMaintenanceDay::getPlanId, id));
        if (dayList.size()>0){
            for (DEquipmentMaintenanceDay day : dayList) {
                maintenancePlanVO planVO = new maintenancePlanVO();
                planVO.setPlanDay(day.getPlanDay());
                planVO.setRepairType(day.getRepairType());
                if (day.getContent()!=null){
                    planVO.setContent(day.getContent());
                }
                planVOList.add(planVO);
            }
        }
        dEquipmentMaintenancePlan.setPlanVOList(planVOList);
        return dEquipmentMaintenancePlan;
    }

    /**
     * 新增设备保养计划
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 结果
     */
    @Override
    public int insertDEquipmentMaintenancePlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        dEquipmentMaintenancePlan.setStatus(1);//创建计划默认为未启用状态  1：未启用 2：已启用 0：已作废
        int insert = dEquipmentMaintenancePlanMapper.insert(dEquipmentMaintenancePlan);
        return insert;
    }

    /**
     * 新增设备保养计划
     *
     * @param id 设备保养计划
     * @return 结果
     */
    @Override
    public AjaxResult startPlan(Long id) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DEquipmentMaintenancePlan dEquipmentMaintenancePlan = dEquipmentMaintenancePlanMapper.selectById(id);
        if (dEquipmentMaintenancePlan.getStatus() == 2){
            return AjaxResult.error("当前计划已启用！");
        }else if (dEquipmentMaintenancePlan.getStatus() == 0){
            return AjaxResult.error("已作废计划无法启用！");
        }else {
            Integer planType = dEquipmentMaintenancePlan.getPlanType();//计划类型 1：固定时间循环
            if (planType == 1){
                String planStartTime = simpleDateFormat.format(dEquipmentMaintenancePlan.getPlanStartTime());
                String planEndTime = simpleDateFormat.format(dEquipmentMaintenancePlan.getPlanEndTime());
                List<String> everyDay = WeekDayUtil.findEveryDay(planStartTime, planEndTime, dEquipmentMaintenancePlan.getIntervalDay());
                for (String day : everyDay) {
                    DEquipmentMaintenanceDay day1 = new DEquipmentMaintenanceDay();
                    String[] ignoreProperties= {"id"};
                    BeanUtils.copyProperties(dEquipmentMaintenancePlan, day1,ignoreProperties);
                    if (day1.getExecutorId()!=null){
                        day1.setStatus(2);//设置状态为已分派
                        day1.setExecutorName(equipmentMapper.getUserByUserId(day1.getExecutorId().toString()));
                    }else {
                        day1.setStatus(1);//1：未分派 2：已分派 0：撤销 3：已完成（已检测）
                    }
                    day1.setPlanDay(day);
                    day1.setSource(1);//来源 1：计划生成 2：手动新建
                    day1.setType(1);//1：保养工单 2：维修工单
                    day1.setPlanId(dEquipmentMaintenancePlan.getId());
                    day1.setDayNo(codeConfigService.getCode(CodeEnum.SBN.getCodeName()));
                    dEquipmentMaintenanceDayMapper.insert(day1);
                }
            } else if (planType == 2) {//2：单次计划
                String planStartTime = simpleDateFormat.format(dEquipmentMaintenancePlan.getPlanStartTime());
                DEquipmentMaintenanceDay day1 = new DEquipmentMaintenanceDay();
                String[] ignoreProperties= {"id"};
                BeanUtils.copyProperties(dEquipmentMaintenancePlan, day1,ignoreProperties);
                if (day1.getExecutorId()!=null){
                    day1.setStatus(2);//设置状态为已分派
                }else {
                    day1.setStatus(1);//1：未分派 2：已分派 0：撤销 3：已完成（已检测）
                }
                day1.setPlanDay(planStartTime);
                day1.setSource(1);//来源 1：保养计划 2：新建工单
                day1.setPlanId(dEquipmentMaintenancePlan.getId());
                day1.setDayNo(codeConfigService.getCode(CodeEnum.SBN.getCodeName()));
                dEquipmentMaintenanceDayMapper.insert(day1);
            }
            dEquipmentMaintenancePlan.setStatus(2);
            dEquipmentMaintenancePlanMapper.updateById(dEquipmentMaintenancePlan);
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult cancelPlan(Long id) throws Exception {
        DEquipmentMaintenancePlan plan = dEquipmentMaintenancePlanMapper.selectById(id);
        if (plan==null){
            return AjaxResult.error("未查询到计划工单！");
        }
        if (plan.getStatus() == 0){
            return AjaxResult.error("当前计划已经作废！");
        }
        plan.setStatus(0);//设置计划状态为 已作废
        List<DEquipmentMaintenanceDay> dayList = dEquipmentMaintenanceDayMapper.selectList(new LambdaQueryWrapper<DEquipmentMaintenanceDay>()
                .eq(DEquipmentMaintenanceDay::getPlanId, id)
                .ne(DEquipmentMaintenanceDay::getStatus,3)
                .eq(BaseEntity::getDelFlag,0));
        if (dayList.size()>0){
            for (DEquipmentMaintenanceDay day : dayList) {//设置计划下所排任务状态为 已作废
                day.setStatus(0);
                dEquipmentMaintenanceDayMapper.updateById(day);
            }
        }
        dEquipmentMaintenancePlanMapper.updateById(plan);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult insertRepairPlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan) throws ParseException {
        List<maintenancePlanVO> planVOList = dEquipmentMaintenancePlan.getPlanVOList();
        if (planVOList.size()<=0){
            return AjaxResult.error("请选择维修计划日期");
        }
        dEquipmentMaintenancePlan.setStatus(2);//创建计划默认为未启用状态  1：未启用 2：已启用 0：已作废
        dEquipmentMaintenancePlan.setType(2);//1:保养计划 2：维修计划
        dEquipmentMaintenancePlanMapper.insert(dEquipmentMaintenancePlan);
        if (planVOList.size()>0){
            for (maintenancePlanVO planVO : planVOList) {
                DEquipmentMaintenanceDay day = new DEquipmentMaintenanceDay();
                String[] ignoreProperties= {"id"};
                BeanUtils.copyProperties(dEquipmentMaintenancePlan, day,ignoreProperties);
                day.setPlanDay(planVO.getPlanDay());
                if (planVO.getContent()!=null){
                    day.setContent(planVO.getContent());
                }
                day.setRemark(planVO.getContent());
                day.setRepairType(planVO.getRepairType());
                day.setPlanId(dEquipmentMaintenancePlan.getId());
                day.setDayNo(generateNumberUtil.generateNum("WX",4));
                day.setStatus(1);
                day.setSource(1);
                day.setType(2);
                dEquipmentMaintenanceDayMapper.insert(day);
            }

            //方法1：先对年龄进行升序，结果进行反转
            List<maintenancePlanVO> collect = planVOList.stream().sorted(Comparator.comparing(maintenancePlanVO::getPlanDay).reversed()).collect(Collectors.toList());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //Date类型转String类型
            Date endtime = simpleDateFormat.parse(collect.get(0).getPlanDay());
            Date starttime = simpleDateFormat.parse(collect.get(collect.size() - 1).getPlanDay());

            dEquipmentMaintenancePlan.setPlanStartTime(starttime);
            dEquipmentMaintenancePlan.setPlanEndTime(endtime);
            dEquipmentMaintenancePlanMapper.updateById(dEquipmentMaintenancePlan);
//            planVOList.stream().sorted(Comparator.comparing(maintenancePlanVO::getPlanDay, Comparator.reverseOrder())).collect(Collectors.toList());
        }
        return AjaxResult.success();
    }

    /**
     * 修改设备保养计划
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 结果
     */
    @Override
    public AjaxResult updateDEquipmentMaintenancePlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        DEquipmentMaintenancePlan plan = dEquipmentMaintenancePlanMapper.selectById(dEquipmentMaintenancePlan.getId());
        if (plan.getStatus()!=1){
            return AjaxResult.error("只能对未启用计划进行编辑！");
        }
        dEquipmentMaintenancePlanMapper.updateById(dEquipmentMaintenancePlan);
        return AjaxResult.success();
    }


    /**
     * 批量删除设备保养计划
     *
     * @param ids 需要删除的设备保养计划主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentMaintenancePlanByIds(Long[] ids)
    {
        return dEquipmentMaintenancePlanMapper.deleteDEquipmentMaintenancePlanByIds(ids);
    }

    /**
     * 删除设备保养计划信息
     *
     * @param id 设备保养计划主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentMaintenancePlanById(Long id)
    {
        return dEquipmentMaintenancePlanMapper.deleteDEquipmentMaintenancePlanById(id);
    }
}
