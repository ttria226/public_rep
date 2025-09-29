package com.xsrw.wms.equipment.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.domain.DInspectionPlanDay;
import org.apache.ibatis.annotations.Param;

/**
 * 保养工单Mapper接口
 *
 * @author zjj
 * @date 2023-05-11
 */
public interface DEquipmentMaintenanceDayMapper extends BaseMapper<DEquipmentMaintenanceDay> {

    /**
     * 查询保养工单列表
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 保养工单集合
     */
    public List<DEquipmentMaintenanceDay> selectDEquipmentMaintenanceDayList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay);


    /**
     * 删除保养工单
     *
     * @param id 保养工单主键
     * @return 结果
     */
    public int deleteDEquipmentMaintenanceDayById(Long id);

    /**
     * 批量删除保养工单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDEquipmentMaintenanceDayByIds(Long[] ids);

    public List<DEquipmentMaintenanceDay> byListApp(@Param("type") Integer type, @Param("time") String time, @Param("executorId") Long executorId);

}
