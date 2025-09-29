package com.xsrw.wms.check.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TCheckAreaHistory;

/**
 * 平库盘点提交历史Mapper接口
 * 
 * @author lyx
 * @date 2023-05-11
 */
public interface TCheckAreaHistoryMapper extends BaseMapper<TCheckAreaHistory>
{

    /**
     * 查询平库盘点提交历史列表
     * 
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 平库盘点提交历史集合
     */
    public List<TCheckAreaHistory> selectTCheckAreaHistoryList(TCheckAreaHistory tCheckAreaHistory);


    /**
     * 删除平库盘点提交历史
     * 
     * @param id 平库盘点提交历史主键
     * @return 结果
     */
    public int deleteTCheckAreaHistoryById(Long id);

    /**
     * 批量删除平库盘点提交历史
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTCheckAreaHistoryByIds(Long[] ids);
}
