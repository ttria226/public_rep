package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TContactsUnit;

/**
 * 供应商Mapper接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface TContactsUnitMapper extends BaseMapper<TContactsUnit>
{

    /**
     * 查询供应商列表
     *
     * @param tContactsUnit 供应商
     * @return 供应商集合
     */
    List<TContactsUnit> selectTContactsUnitList(TContactsUnit tContactsUnit);


    /**
     * 删除供应商
     *
     * @param id 供应商主键
     * @return 结果
     */
    int deleteTContactsUnitById(Long id);

    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTContactsUnitByIds(Long[] ids);
}
