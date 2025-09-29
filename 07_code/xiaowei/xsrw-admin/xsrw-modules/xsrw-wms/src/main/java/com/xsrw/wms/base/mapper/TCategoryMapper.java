package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TCategory;

/**
 * 物料类别Mapper接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface TCategoryMapper extends BaseMapper<TCategory>
{

    /**
     * 查询物料类别列表
     *
     * @param tCategory 物料类别
     * @return 物料类别集合
     */
    List<TCategory> selectTCategoryList(TCategory tCategory);


    /**
     * 删除物料类别
     *
     * @param id 物料类别主键
     * @return 结果
     */
    int deleteTCategoryById(Long id);

    /**
     * 批量删除物料类别
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTCategoryByIds(Long[] ids);
}
