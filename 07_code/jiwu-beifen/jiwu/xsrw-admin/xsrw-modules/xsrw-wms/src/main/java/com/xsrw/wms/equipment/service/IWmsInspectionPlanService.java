package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.WmsInspectionPlan;

/**
 * 巡检计划Service接口
 *
 * @author zjj
 * @date 2023-05-08
 */
public interface IWmsInspectionPlanService extends IService<WmsInspectionPlan>
{

    /**
     * 查询巡检计划列表
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 巡检计划集合
     */
    public List<WmsInspectionPlan> selectWmsInspectionPlanList(WmsInspectionPlan wmsInspectionPlan);

    /**
     * 查询巡检计划
     *
     * @param id 巡检计划主键
     * @return 巡检计划
     */
    public WmsInspectionPlan selectWmsInspectionPlanById(Long id);

    /**
     * 新增巡检计划
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 结果
     */
    public AjaxResult insertWmsInspectionPlan(WmsInspectionPlan wmsInspectionPlan);

    /**
     * 修改巡检计划
     *
     * @param wmsInspectionPlan 巡检计划
     * @return 结果
     */
    public AjaxResult updateWmsInspectionPlan(WmsInspectionPlan wmsInspectionPlan);

    /**
     * 批量删除巡检计划
     *
     * @param ids 需要删除的巡检计划主键集合
     * @return 结果
     */
    public int deleteWmsInspectionPlanByIds(Long[] ids);

    /**
     * 删除巡检计划信息
     *
     * @param id 巡检计划主键
     * @return 结果
     */
    public int deleteWmsInspectionPlanById(Long id);


    AjaxResult startPlan(Long id) throws Exception;

    AjaxResult endPlan(Long id) throws Exception;


}
