package com.xsrw.wms.equipment.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DInspectionPlanDay;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;

/**
 * 巡检记录Service接口
 *
 * @author zjj
 * @date 2023-05-09
 */
public interface IDInspectionPlanDayService extends IService<DInspectionPlanDay>
{

    /**
     * 查询巡检记录列表
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 巡检记录集合
     */
    public List<DInspectionPlanDay> selectDInspectionPlanDayList(DInspectionPlanDay dInspectionPlanDay);

    /**
     * 查询巡检记录
     *
     * @param id 巡检记录主键
     * @return 巡检记录
     */
    public DInspectionPlanDay selectDInspectionPlanDayById(Long id);

    /**
     * 新增巡检记录
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 结果
     */
    public int insertDInspectionPlanDay(DInspectionPlanDay dInspectionPlanDay);

    /**
     * 修改巡检记录
     *
     * @param dInspectionPlanDay 巡检记录
     * @return 结果
     */
    public int updateDInspectionPlanDay(DInspectionPlanDay dInspectionPlanDay);

    /**
     * 批量删除巡检记录
     *
     * @param ids 需要删除的巡检记录主键集合
     * @return 结果
     */
    public int deleteDInspectionPlanDayByIds(Long[] ids);

    /**
     * 删除巡检记录信息
     *
     * @param id 巡检记录主键
     * @return 结果
     */
    public int deleteDInspectionPlanDayById(Long id);

    AjaxResult exchange(String id, String userid, String reason, String username);

    public List<DInspectionPlanDay> appPlanList();

    public List<WmsInspectionPlanDetail> getInfoApp(Long planid,Long dayid);


}
