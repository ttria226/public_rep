package com.xsrw.wms.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.check.domain.TCheckResult;
import com.xsrw.wms.check.domain.dto.CheckResultDTO;
import com.xsrw.wms.check.domain.vo.CheckResultVO;

import java.util.List;

/**
 * 盘点差异Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITCheckResultService extends IService<TCheckResult>
{

    /**
     * 查询盘点差异列表
     *
     * @param checkResult 盘点差异
     * @return 盘点差异集合
     */
    List<CheckResultVO> selectTCheckResultList(CheckResultDTO checkResult);

    /**
     * 查询盘点差异
     *
     * @param id 盘点差异主键
     * @return 盘点差异
     */
    TCheckResult selectTCheckResultById(Long id);

    /**
     * 新增盘点差异
     *
     * @param tCheckResult 盘点差异
     * @return 结果
     */
    int insertTCheckResult(List<TCheckResult> tCheckResultList);

    /**
     * 修改盘点差异
     *
     * @param tCheckResult 盘点差异
     * @return 结果
     */
    int updateTCheckResult(TCheckResult tCheckResult);

    /**
     * 批量删除盘点差异
     *
     * @param ids 需要删除的盘点差异主键集合
     * @return 结果
     */
    int deleteTCheckResultByIds(Long[] ids);

    /**
     * 删除盘点差异信息
     *
     * @param id 盘点差异主键
     * @return 结果
     */
    int deleteTCheckResultById(Long id);

    /**
     * 生成盘点差异报表
     * @param taskIds
     * @return
     */
    AjaxResult createCheckResult(Long[] taskIds);


    /**
     * 根据任务id生成盘点差异报表
     * @param taskId
     * @return
     */
    int saveCheckResultByTaskId(Long taskId);

    List<CheckResultVO> getCheckResultList(CheckResultDTO checkResult);

    /**
     * 盘差分析明细
     * @param checkResult
     * @return
     */
    List<CheckResultVO> getCheckResultDetail(CheckResultDTO checkResult);
}
