package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TBatchAttr;

/**
 * 批次属性Mapper接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface TBatchAttrMapper extends BaseMapper<TBatchAttr>
{

    /**
     * 查询批次属性列表
     *
     * @param tBatchAttr 批次属性
     * @return 批次属性集合
     */
    List<TBatchAttr> selectTBatchAttrList(TBatchAttr tBatchAttr);


    /**
     * 删除批次属性
     *
     * @param id 批次属性主键
     * @return 结果
     */
    int deleteTBatchAttrById(Long id);

    /**
     * 批量删除批次属性
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTBatchAttrByIds(Long[] ids);
}
