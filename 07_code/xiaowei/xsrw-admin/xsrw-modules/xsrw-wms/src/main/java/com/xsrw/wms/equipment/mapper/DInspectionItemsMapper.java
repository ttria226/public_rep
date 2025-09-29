package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DInspectionItems;

/**
 * 巡检标准Mapper接口
 * 
 * @author zjj
 * @date 2023-05-08
 */
public interface DInspectionItemsMapper extends BaseMapper<DInspectionItems>
{

    /**
     * 查询巡检标准列表
     * 
     * @param dInspectionItems 巡检标准
     * @return 巡检标准集合
     */
    public List<DInspectionItems> selectDInspectionItemsList(DInspectionItems dInspectionItems);


    /**
     * 删除巡检标准
     * 
     * @param id 巡检标准主键
     * @return 结果
     */
    public int deleteDInspectionItemsById(Long id);

    /**
     * 批量删除巡检标准
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDInspectionItemsByIds(Long[] ids);

    List<DInspectionItems> isadd(String equipmentid);
}
