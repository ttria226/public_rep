package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenancePlan;

/**
 * 设备保养计划Mapper接口
 * 
 * @author zjj
 * @date 2023-05-11
 */
public interface DEquipmentMaintenancePlanMapper extends BaseMapper<DEquipmentMaintenancePlan>
{

    /**
     * 查询设备保养计划列表
     * 
     * @param dEquipmentMaintenancePlan 设备保养计划
     * @return 设备保养计划集合
     */
    public List<DEquipmentMaintenancePlan> selectDEquipmentMaintenancePlanList(DEquipmentMaintenancePlan dEquipmentMaintenancePlan);


    /**
     * 删除设备保养计划
     * 
     * @param id 设备保养计划主键
     * @return 结果
     */
    public int deleteDEquipmentMaintenancePlanById(Long id);

    /**
     * 批量删除设备保养计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDEquipmentMaintenancePlanByIds(Long[] ids);
}
