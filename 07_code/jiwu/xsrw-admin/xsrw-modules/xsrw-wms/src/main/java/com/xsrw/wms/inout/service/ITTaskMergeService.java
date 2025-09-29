package com.xsrw.wms.inout.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TTaskMerge;
import com.xsrw.wms.inout.domain.vo.TTaskMergeVO;

/**
 * 出库任务详情Service接口
 *
 * @author zjj
 * @date 2023-06-26
 */
public interface ITTaskMergeService extends IService<TTaskMerge>
{

    /**
     * 查询出库任务详情列表
     *
     * @param tTaskMerge 出库任务详情
     * @return 出库任务详情集合
     */
    public List<TTaskMergeVO> selectTTaskMergeList(TTaskMerge tTaskMerge);

    /**
     * 查询出库任务详情
     *
     * @param id 出库任务详情主键
     * @return 出库任务详情
     */
    public TTaskMergeVO selectTTaskMergeById(Long id);

    /**
     * 新增出库任务详情
     *
     * @param tTaskMerge 出库任务详情
     * @return 结果
     */
    public int insertTTaskMerge(TTaskMerge tTaskMerge);

    /**
     * 修改出库任务详情
     *
     * @param tTaskMerge 出库任务详情
     * @return 结果
     */
    public int updateTTaskMerge(TTaskMerge tTaskMerge);

    /**
     * 批量删除出库任务详情
     *
     * @param ids 需要删除的出库任务详情主键集合
     * @return 结果
     */
    public int deleteTTaskMergeByIds(Long[] ids);

    /**
     * 删除出库任务详情信息
     *
     * @param id 出库任务详情主键
     * @return 结果
     */
    public int deleteTTaskMergeById(Long id);


    /**
     * 执行出库--自动分配载具
     * @return
     */
    AjaxResult voluntarily(Long mergeDeliveryId, Long materialId);


    /**
     * 地堆出库--自动分配载具
     * @return
     */
    AjaxResult groundPileTrayListVoluntarily(Long mergeDeliveryId,Long materialId);

}
