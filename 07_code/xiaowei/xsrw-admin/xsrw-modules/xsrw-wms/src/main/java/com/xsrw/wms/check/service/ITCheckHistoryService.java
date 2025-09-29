package com.xsrw.wms.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.check.domain.TCheckHistory;

import java.util.List;

/**
 * 盘点历史记录Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITCheckHistoryService extends IService<TCheckHistory>
{

    /**
     * 查询盘点历史记录列表
     *
     * @param tCheckHistory 盘点历史记录
     * @return 盘点历史记录集合
     */
    public List<TCheckHistory> selectTCheckHistoryList(TCheckHistory tCheckHistory);

    /**
     * 查询盘点历史记录
     *
     * @param id 盘点历史记录主键
     * @return 盘点历史记录
     */
    public TCheckHistory selectTCheckHistoryById(Long id);

    /**
     * 新增盘点历史记录
     *
     * @param tCheckHistory 盘点历史记录
     * @return 结果
     */
    public int insertTCheckHistory(TCheckHistory tCheckHistory);

    /**
     * 修改盘点历史记录
     *
     * @param tCheckHistory 盘点历史记录
     * @return 结果
     */
    public int updateTCheckHistory(TCheckHistory tCheckHistory);

    /**
     * 批量删除盘点历史记录
     *
     * @param ids 需要删除的盘点历史记录主键集合
     * @return 结果
     */
    public int deleteTCheckHistoryByIds(Long[] ids);

    /**
     * 删除盘点历史记录信息
     *
     * @param id 盘点历史记录主键
     * @return 结果
     */
    public int deleteTCheckHistoryById(Long id);
}
