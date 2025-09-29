package com.xsrw.wms.equipment.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xsrw.wms.equipment.domain.DInspectionPlanDay;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 巡检记录Mapper接口
 * 
 * @author zjj
 * @date 2023-05-09
 */
public interface DInspectionPlanDayMapper extends BaseMapper<DInspectionPlanDay>
{

    /**
     * 查询巡检记录列表
     * 
     * @param dInspectionPlanDay 巡检记录
     * @return 巡检记录集合
     */
    public List<DInspectionPlanDay> selectDInspectionPlanDayList(DInspectionPlanDay dInspectionPlanDay);


    /**
     * 删除巡检记录
     * 
     * @param id 巡检记录主键
     * @return 结果
     */
    public int deleteDInspectionPlanDayById(Long id);

    /**
     * 批量删除巡检记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDInspectionPlanDayByIds(Long[] ids);


    public List<DInspectionPlanDay> appPlanList( @Param(Constants.WRAPPER) Wrapper<DInspectionPlanDay> queryWrapper);


    public List<WmsInspectionPlanDetail> getInfoApp(Long planid);

}
