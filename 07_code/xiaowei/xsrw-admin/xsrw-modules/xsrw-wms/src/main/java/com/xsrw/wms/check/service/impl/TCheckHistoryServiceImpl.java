package com.xsrw.wms.check.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.check.mapper.TCheckHistoryMapper;
import com.xsrw.wms.check.domain.TCheckHistory;
import com.xsrw.wms.check.service.ITCheckHistoryService;

/**
 * 盘点历史记录Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TCheckHistoryServiceImpl extends ServiceImpl<TCheckHistoryMapper, TCheckHistory> implements ITCheckHistoryService
{
    @Autowired
    private TCheckHistoryMapper tCheckHistoryMapper;


    /**
     * 查询盘点历史记录列表
     *
     * @param tCheckHistory 盘点历史记录
     * @return 盘点历史记录
     */
    @Override
    public List<TCheckHistory> selectTCheckHistoryList(TCheckHistory tCheckHistory)
    {
        return tCheckHistoryMapper.selectTCheckHistoryList(tCheckHistory);
    }

    /**
     * 查询盘点历史记录
     *
     * @param id 盘点历史记录主键
     * @return 盘点历史记录
     */
    @Override
    public TCheckHistory selectTCheckHistoryById(Long id)
    {
        return tCheckHistoryMapper.selectById(id);
    }

    /**
     * 新增盘点历史记录
     *
     * @param tCheckHistory 盘点历史记录
     * @return 结果
     */
    @Override
    public int insertTCheckHistory(TCheckHistory tCheckHistory)
    {
        return tCheckHistoryMapper.insert(tCheckHistory);
    }

    /**
     * 修改盘点历史记录
     *
     * @param tCheckHistory 盘点历史记录
     * @return 结果
     */
    @Override
    public int updateTCheckHistory(TCheckHistory tCheckHistory)
    {
        return tCheckHistoryMapper.updateById(tCheckHistory);
    }


    /**
     * 批量删除盘点历史记录
     *
     * @param ids 需要删除的盘点历史记录主键
     * @return 结果
     */
    @Override
    public int deleteTCheckHistoryByIds(Long[] ids)
    {
        return tCheckHistoryMapper.deleteTCheckHistoryByIds(ids);
    }

    /**
     * 删除盘点历史记录信息
     *
     * @param id 盘点历史记录主键
     * @return 结果
     */
    @Override
    public int deleteTCheckHistoryById(Long id)
    {
        return tCheckHistoryMapper.deleteTCheckHistoryById(id);
    }
}
