package com.xsrw.wms.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.check.domain.TCheckAreaHistory;
import com.xsrw.wms.check.domain.vo.CheckAreaHistoryVO;

import java.util.List;

/**
 * 平库盘点提交历史Service接口
 *
 * @author lyx
 * @date 2023-05-11
 */
public interface ITCheckAreaHistoryService extends IService<TCheckAreaHistory>
{

    /**
     * 查询平库盘点提交历史列表
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 平库盘点提交历史集合
     */
    public List<TCheckAreaHistory> selectTCheckAreaHistoryList(TCheckAreaHistory tCheckAreaHistory);

    /**
     * 查询平库盘点提交历史
     *
     * @param id 平库盘点提交历史主键
     * @return 平库盘点提交历史
     */
    public TCheckAreaHistory selectTCheckAreaHistoryById(Long id);

    /**
     * 新增平库盘点提交历史
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 结果
     */
    public int insertTCheckAreaHistory(TCheckAreaHistory tCheckAreaHistory);

    /**
     * 修改平库盘点提交历史
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 结果
     */
    public int updateTCheckAreaHistory(TCheckAreaHistory tCheckAreaHistory);

    /**
     * 批量删除平库盘点提交历史
     *
     * @param ids 需要删除的平库盘点提交历史主键集合
     * @return 结果
     */
    public int deleteTCheckAreaHistoryByIds(Long[] ids);

    /**
     * 删除平库盘点提交历史信息
     *
     * @param id 平库盘点提交历史主键
     * @return 结果
     */
    public int deleteTCheckAreaHistoryById(Long id);

    /**
     * 查询平库盘点记录
     * @param taskDetailId
     * @param taskId
     * @param materialCode
     * @param trayCode
     * @param status
     * @return
     */
    List<CheckAreaHistoryVO> getCheckAreaHistory(Long taskDetailId, Long taskId, String materialCode, String trayCode, String status);
}
