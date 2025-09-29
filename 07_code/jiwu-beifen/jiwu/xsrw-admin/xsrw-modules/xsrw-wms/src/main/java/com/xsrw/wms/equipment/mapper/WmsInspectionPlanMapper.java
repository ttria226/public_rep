package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.WmsInspectionPlan;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;

/**
 * 巡检计划Mapper接口
 * 
 * @author zjj
 * @date 2023-05-08
 */
public interface WmsInspectionPlanMapper extends BaseMapper<WmsInspectionPlan>
{

    /**
     * 查询巡检计划列表
     * 
     * @param wmsInspectionPlan 巡检计划
     * @return 巡检计划集合
     */
    public List<WmsInspectionPlan> selectWmsInspectionPlanList(WmsInspectionPlan wmsInspectionPlan);


    /**
     * 删除巡检计划
     * 
     * @param id 巡检计划主键
     * @return 结果
     */
    public int deleteWmsInspectionPlanById(Long id);

    /**
     * 批量删除巡检计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsInspectionPlanByIds(Long[] ids);

    /**
     * 批量删除巡检计划设备列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailByPlanIds(Long[] ids);
    
    /**
     * 批量新增巡检计划设备列
     * 
     * @param wmsInspectionPlanDetailList 巡检计划设备列列表
     * @return 结果
     */
    public int batchWmsInspectionPlanDetail(List<WmsInspectionPlanDetail> wmsInspectionPlanDetailList);
    

    /**
     * 通过巡检计划主键删除巡检计划设备列信息
     * 
     * @param id 巡检计划ID
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailByPlanId(Long id);

    String getuserName(Long userid);
}
