package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TUnit;

/**
 * 单位Mapper接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface TUnitMapper extends BaseMapper<TUnit>
{

    /**
     * 查询单位列表
     *
     * @param tUnit 单位
     * @return 单位集合
     */
    List<TUnit> selectTUnitList(TUnit tUnit);


    /**
     * 删除单位
     *
     * @param id 单位主键
     * @return 结果
     */
    int deleteTUnitById(Long id);

    /**
     * 批量删除单位
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTUnitByIds(Long[] ids);
}
