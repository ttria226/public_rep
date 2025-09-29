package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;

/**
 * 巡检计划设备列Mapper接口
 * 
 * @author zjj
 * @date 2023-05-08
 */
public interface WmsInspectionPlanDetailMapper extends BaseMapper<WmsInspectionPlanDetail>
{

    /**
     * 查询巡检计划设备列列表
     * 
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 巡检计划设备列集合
     */
    public List<WmsInspectionPlanDetail> selectWmsInspectionPlanDetailList(WmsInspectionPlanDetail wmsInspectionPlanDetail);


    /**
     * 删除巡检计划设备列
     * 
     * @param id 巡检计划设备列主键
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailById(Long id);

    /**
     * 批量删除巡检计划设备列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWmsInspectionPlanDetailByIds(Long[] ids);
}
