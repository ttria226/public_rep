package com.xsrw.wms.equipment.service;

import java.text.ParseException;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenancePlan;

/**
 * 设备保养计划Service接口
 *
 * @author zjj
 * @date 2023-05-11
 */
public interface IDEquipmentMaintenancePlanService extends IService<DEquipmentMaintenancePlan>
{

    /**
     * 查询设备保养计划列表
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 设备保养计划集合
     */
    public List<DEquipmentMaintenancePlan> selectDEquipmentMaintenancePlanList(DEquipmentMaintenancePlan dEquipmentMaintenancePlan);

    /**
     * 查询设备保养计划
     *
     * @param id 设备保养计划主键
     * @return 设备保养计划
     */
    public DEquipmentMaintenancePlan selectDEquipmentMaintenancePlanById(Long id);

    /**
     * 新增设备保养计划
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 结果
     */
    public int insertDEquipmentMaintenancePlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan);

    /**
     * 修改设备保养计划
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 结果
     */
    public AjaxResult updateDEquipmentMaintenancePlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan);

    /**
     * 批量删除设备保养计划
     *
     * @param ids 需要删除的设备保养计划主键集合
     * @return 结果
     */
    public int deleteDEquipmentMaintenancePlanByIds(Long[] ids);

    /**
     * 删除设备保养计划信息
     *
     * @param id 设备保养计划主键
     * @return 结果
     */
    public int deleteDEquipmentMaintenancePlanById(Long id);

    AjaxResult startPlan(Long id) throws Exception;

    AjaxResult cancelPlan(Long id) throws Exception;

    /**
     * 新增设备保养计划
     *
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 结果
     */
    public AjaxResult insertRepairPlan(DEquipmentMaintenancePlan dEquipmentMaintenancePlan) throws ParseException;

}
