package com.xsrw.wms.check.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TCheckHistory;

/**
 * 盘点历史记录Mapper接口
 * 
 * @author lyx
 * @date 2023-05-09
 */
public interface TCheckHistoryMapper extends BaseMapper<TCheckHistory>
{

    /**
     * 查询盘点历史记录列表
     * 
     * @param tCheckHistory 盘点历史记录
     * @return 盘点历史记录集合
     */
    public List<TCheckHistory> selectTCheckHistoryList(TCheckHistory tCheckHistory);


    /**
     * 删除盘点历史记录
     * 
     * @param id 盘点历史记录主键
     * @return 结果
     */
    public int deleteTCheckHistoryById(Long id);

    /**
     * 批量删除盘点历史记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckHistoryByIds(Long[] ids);
}
