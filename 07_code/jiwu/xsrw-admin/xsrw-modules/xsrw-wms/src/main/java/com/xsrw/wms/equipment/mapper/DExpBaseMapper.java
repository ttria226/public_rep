package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DExpBase;

/**
 * 保养/维修经验库Mapper接口
 * 
 * @author zjj
 * @date 2023-05-11
 */
public interface DExpBaseMapper extends BaseMapper<DExpBase>
{

    /**
     * 查询保养/维修经验库列表
     * 
     * @param dExpBase 保养/维修经验库
     * @return 保养/维修经验库集合
     */
    public List<DExpBase> selectDExpBaseList(DExpBase dExpBase);


    /**
     * 删除保养/维修经验库
     * 
     * @param id 保养/维修经验库主键
     * @return 结果
     */
    public int deleteDExpBaseById(Long id);

    /**
     * 批量删除保养/维修经验库
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDExpBaseByIds(Long[] ids);
}
