package com.xsrw.wms.inout.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TMergeDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TMergeDeliveryDetailVO;

/**
 * 波次计划详情Service接口
 *
 * @author zjj
 * @date 2023-06-25
 */
public interface ITMergeDeliveryDetailService extends IService<TMergeDeliveryDetail>
{

    /**
     * 查询波次计划详情列表
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 波次计划详情集合
     */
    public List<TMergeDeliveryDetailVO> selectTMergeDeliveryDetailList(TMergeDeliveryDetail tMergeDeliveryDetail);

    /**
     * 查询波次计划详情
     *
     * @param id 波次计划详情主键
     * @return 波次计划详情
     */
    public TMergeDeliveryDetail selectTMergeDeliveryDetailById(Long id);

    /**
     * 新增波次计划详情
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 结果
     */
    public int insertTMergeDeliveryDetail(TMergeDeliveryDetail tMergeDeliveryDetail);

    /**
     * 修改波次计划详情
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 结果
     */
    public int updateTMergeDeliveryDetail(TMergeDeliveryDetail tMergeDeliveryDetail);

    /**
     * 批量删除波次计划详情
     *
     * @param ids 需要删除的波次计划详情主键集合
     * @return 结果
     */
    public int deleteTMergeDeliveryDetailByIds(Long[] ids);

    /**
     * 删除波次计划详情信息
     *
     * @param id 波次计划详情主键
     * @return 结果
     */
    public int deleteTMergeDeliveryDetailById(Long id);
}
