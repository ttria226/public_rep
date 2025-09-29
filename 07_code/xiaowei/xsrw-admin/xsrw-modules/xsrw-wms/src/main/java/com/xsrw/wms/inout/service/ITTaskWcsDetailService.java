package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsDetail;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import org.apache.ibatis.annotations.Param;

/**
 * wcs任务详情Service接口
 *
 * @author wxr
 * @date 2023-05-10
 */
public interface ITTaskWcsDetailService extends IService<TTaskWcsDetail> {

    /**
     * 查询wcs任务详情列表
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return wcs任务详情集合
     */
    public List<TTaskWcsDetail> selectTTaskWcsDetailList(TTaskWcsDetail tTaskWcsDetail);

    /**
     * 查询wcs任务详情
     *
     * @param id wcs任务详情主键
     * @return wcs任务详情
     */
    public TTaskWcsDetail selectTTaskWcsDetailById(Long id);

    List<TTaskWcsDetailVO> selectStatusWcsListByTrayId(@Param("trayId") Long trayId, @Param("taskType") String taskType);

    /**
     * 新增wcs任务详情
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return 结果
     */
    public int insertTTaskWcsDetail(TTaskWcsDetail tTaskWcsDetail);

    /**
     * 修改wcs任务详情
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return 结果
     */
    public int updateTTaskWcsDetail(TTaskWcsDetail tTaskWcsDetail);

    /**
     * 批量删除wcs任务详情
     *
     * @param ids 需要删除的wcs任务详情主键集合
     * @return 结果
     */
    public int deleteTTaskWcsDetailByIds(Long[] ids);

    /**
     * 删除wcs任务详情信息
     *
     * @param id wcs任务详情主键
     * @return 结果
     */
    public int deleteTTaskWcsDetailById(Long id);
    List<TTaskWcsDetailVO> getShiftDetail(Long taskwcsId);
    AjaxResult updateStock(Long taskId);

    /**
     * 作废
     * @param taskWcs
     * @return
     */
    AjaxResult cancellationDelivery(TTaskWcs taskWcs);


    /**
     * 更新拣货任务优先级
     * @param id
     * @param priority
     * @return
     */
    AjaxResult updatePriority(Long id,String priority);


    /**
     * 出库拣货任务作废
     * @param taskNo
     * @return
     */
    AjaxResult outCancellation(String taskNo);


    /**
     * 移库任务作废
     * @param taskNo
     * @return
     */
    AjaxResult moveCancellation(String taskNo);

    /**
     * 根据任务主表id获取子表列表
     * @param taskWcsId
     * @param taskTypePut
     * @return
     */
    List<TTaskWcsDetailVO> getListByTaskId(Long taskWcsId, String taskTypePut);
}
