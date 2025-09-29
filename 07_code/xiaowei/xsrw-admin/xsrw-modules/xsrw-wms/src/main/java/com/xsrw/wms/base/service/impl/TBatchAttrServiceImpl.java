package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.domain.TBatchAttr;
import com.xsrw.wms.base.mapper.TBatchAttrMapper;
import com.xsrw.wms.base.service.ITBatchAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批次属性Service业务层处理
 *
 * @author lyx
 * @date 2023-05-05
 */
@Service
public class TBatchAttrServiceImpl extends ServiceImpl<TBatchAttrMapper, TBatchAttr> implements ITBatchAttrService
{
    @Autowired
    private TBatchAttrMapper tBatchAttrMapper;


    /**
     * 查询批次属性列表
     *
     * @param entity 批次属性
     * @return 批次属性
     */
    @Override
    public List<TBatchAttr> selectTBatchAttrList(TBatchAttr entity)
    {
        return tBatchAttrMapper.selectTBatchAttrList(entity);
    }

    /**
     * 查询批次属性
     *
     * @param id 批次属性主键
     * @return 批次属性
     */
    @Override
    public TBatchAttr selectTBatchAttrById(Long id)
    {
        return tBatchAttrMapper.selectById(id);
    }

    /**
     * 新增批次属性
     *
     * @param entity 批次属性
     * @return 结果
     */
    @Override
    public int insertTBatchAttr(TBatchAttr entity)
    {
        return tBatchAttrMapper.insert(entity);
    }

    /**
     * 修改批次属性
     *
     * @param entity 批次属性
     * @return 结果
     */
    @Override
    public int updateTBatchAttr(TBatchAttr entity)
    {
        return tBatchAttrMapper.updateById(entity);
    }


    /**
     * 批量删除批次属性
     *
     * @param ids 需要删除的批次属性主键
     * @return 结果
     */
    @Override
    public int deleteTBatchAttrByIds(Long[] ids)
    {
        return tBatchAttrMapper.deleteTBatchAttrByIds(ids);
    }

    /**
     * 删除批次属性信息
     *
     * @param id 批次属性主键
     * @return 结果
     */
    @Override
    public int deleteTBatchAttrById(Long id)
    {
        return tBatchAttrMapper.deleteTBatchAttrById(id);
    }
}
