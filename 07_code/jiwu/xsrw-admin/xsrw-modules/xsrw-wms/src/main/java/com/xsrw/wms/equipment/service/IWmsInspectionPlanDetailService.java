package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;

/**
 * 巡检计划设备列Service接口
 *
 * @author zjj
 * @date 2023-05-08
 */
public interface IWmsInspectionPlanDetailService extends IService<WmsInspectionPlanDetail>
{

    /**
     * 查询巡检计划设备列列表
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 巡检计划设备列集合
     */
    public List<WmsInspectionPlanDetail> selectWmsInspectionPlanDetailList(WmsInspectionPlanDetail wmsInspectionPlanDetail);

    /**
     * 查询巡检计划设备列
     *
     * @param id 巡检计划设备列主键
     * @return 巡检计划设备列
     */
    public WmsInspectionPlanDetail selectWmsInspectionPlanDetailById(Long id);

    /**
     * 新增巡检计划设备列
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 结果
     */
    public int insertWmsInspectionPlanDetail(WmsInspectionPlanDetail wmsInspectionPlanDetail);

    /**
     * 修改巡检计划设备列
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 结果
     */
    public int updateWmsInspectionPlanDetail(WmsInspectionPlanDetail wmsInspectionPlanDetail);

    /**
     * 批量删除巡检计划设备列
     *
     * @param ids 需要删除的巡检计划设备列主键集合
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailByIds(Long[] ids);

    /**
     * 删除巡检计划设备列信息
     *
     * @param id 巡检计划设备列主键
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailById(Long id);
}
