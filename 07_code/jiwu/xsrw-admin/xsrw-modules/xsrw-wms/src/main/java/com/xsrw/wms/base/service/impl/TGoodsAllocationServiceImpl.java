package com.xsrw.wms.base.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TGoodsAllocationMapper;
import com.xsrw.wms.base.domain.TGoodsAllocation;
import com.xsrw.wms.base.service.ITGoodsAllocationService;

/**
 * 接货位Service业务层处理
 *
 * @author zjj
 * @date 2023-06-12
 */
@Service
public class TGoodsAllocationServiceImpl extends ServiceImpl<TGoodsAllocationMapper, TGoodsAllocation> implements ITGoodsAllocationService
{
    @Autowired
    private TGoodsAllocationMapper tGoodsAllocationMapper;


    /**
     * 查询接货位列表
     *
     * @param tGoodsAllocation 接货位
     * @return 接货位
     */
    @Override
    public List<TGoodsAllocation> selectTGoodsAllocationList(TGoodsAllocation tGoodsAllocation)
    {
        return tGoodsAllocationMapper.selectTGoodsAllocationList(tGoodsAllocation);
    }

    /**
     * 查询接货位
     *
     * @param id 接货位主键
     * @return 接货位
     */
    @Override
    public TGoodsAllocation selectTGoodsAllocationById(Long id)
    {
        return tGoodsAllocationMapper.selectById(id);
    }

    /**
     * 新增接货位
     *
     * @param tGoodsAllocation 接货位
     * @return 结果
     */
    @Override
    public int insertTGoodsAllocation(TGoodsAllocation tGoodsAllocation)
    {
        return tGoodsAllocationMapper.insert(tGoodsAllocation);
    }

    /**
     * 修改接货位
     *
     * @param tGoodsAllocation 接货位
     * @return 结果
     */
    @Override
    public int updateTGoodsAllocation(TGoodsAllocation tGoodsAllocation)
    {
        return tGoodsAllocationMapper.updateById(tGoodsAllocation);
    }


    /**
     * 批量删除接货位
     *
     * @param ids 需要删除的接货位主键
     * @return 结果
     */
    @Override
    public int deleteTGoodsAllocationByIds(Long[] ids)
    {
        return tGoodsAllocationMapper.deleteTGoodsAllocationByIds(ids);
    }

    /**
     * 删除接货位信息
     *
     * @param id 接货位主键
     * @return 结果
     */
    @Override
    public int deleteTGoodsAllocationById(Long id)
    {
        return tGoodsAllocationMapper.deleteTGoodsAllocationById(id);
    }
}
