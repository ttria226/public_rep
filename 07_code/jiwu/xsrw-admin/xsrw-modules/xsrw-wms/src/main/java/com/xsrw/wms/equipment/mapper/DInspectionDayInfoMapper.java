package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DInspectionDayInfo;

/**
 * 每日巡检记录Mapper接口
 * 
 * @author zjj
 * @date 2023-05-18
 */
public interface DInspectionDayInfoMapper extends BaseMapper<DInspectionDayInfo>
{

    /**
     * 查询每日巡检记录列表
     * 
     * @param dInspectionDayInfo 每日巡检记录
     * @return 每日巡检记录集合
     */
    public List<DInspectionDayInfo> selectDInspectionDayInfoList(DInspectionDayInfo dInspectionDayInfo);


    /**
     * 删除每日巡检记录
     * 
     * @param id 每日巡检记录主键
     * @return 结果
     */
    public int deleteDInspectionDayInfoById(Long id);

    /**
     * 批量删除每日巡检记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDInspectionDayInfoByIds(Long[] ids);
}
