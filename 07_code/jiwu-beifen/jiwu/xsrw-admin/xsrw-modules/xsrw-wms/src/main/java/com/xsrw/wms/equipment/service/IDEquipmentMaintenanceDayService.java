package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;

/**
 * 保养工单Service接口
 *
 * @author zjj
 * @date 2023-05-11
 */
public interface IDEquipmentMaintenanceDayService extends IService<DEquipmentMaintenanceDay>
{

    /**
     * 查询保养工单列表
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 保养工单集合
     */
    public List<DEquipmentMaintenanceDay> selectDEquipmentMaintenanceDayList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay);

    /**
     * 查询保养工单
     *
     * @param id 保养工单主键
     * @return 保养工单
     */
    public DEquipmentMaintenanceDay selectDEquipmentMaintenanceDayById(Long id);

    /**
     * 新增保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    public AjaxResult insertDEquipmentMaintenanceDay(DEquipmentMaintenanceDay dEquipmentMaintenanceDay);

    /**
     * 修改保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    public int updateDEquipmentMaintenanceDay(DEquipmentMaintenanceDay dEquipmentMaintenanceDay);

    /**
     * 批量删除保养工单
     *
     * @param ids 需要删除的保养工单主键集合
     * @return 结果
     */
    public int deleteDEquipmentMaintenanceDayByIds(Long[] ids);

    /**
     * 删除保养工单信息
     *
     * @param id 保养工单主键
     * @return 结果
     */
    public int deleteDEquipmentMaintenanceDayById(Long id);

    AjaxResult assign(Long id, Long companyId,Long executorId, String executorName);

    AjaxResult cancelDay(Long id);

    public List<DEquipmentMaintenanceDay> byListApp(Integer type);

    /**
     * 修改保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    public AjaxResult startBy(DEquipmentMaintenanceDay dEquipmentMaintenanceDay);


}
