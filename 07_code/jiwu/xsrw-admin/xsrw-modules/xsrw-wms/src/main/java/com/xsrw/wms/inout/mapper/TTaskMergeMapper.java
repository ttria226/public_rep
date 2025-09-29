package com.xsrw.wms.inout.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TTaskMerge;

/**
 * 出库任务详情Mapper接口
 * 
 * @author zjj
 * @date 2023-06-26
 */
public interface TTaskMergeMapper extends BaseMapper<TTaskMerge>
{

    /**
     * 查询出库任务详情列表
     * 
     * @param tTaskMerge 出库任务详情
     * @return 出库任务详情集合
     */
    public List<TTaskMerge> selectTTaskMergeList(TTaskMerge tTaskMerge);


    /**
     * 删除出库任务详情
     * 
     * @param id 出库任务详情主键
     * @return 结果
     */
    public int deleteTTaskMergeById(Long id);

    /**
     * 批量删除出库任务详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskMergeByIds(Long[] ids);
}
