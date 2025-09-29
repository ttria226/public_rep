package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TOverstockDeliveryDetail;

/**
 * 越库单详情Service接口
 *
 * @author wxr
 * @date 2023-06-25
 */
public interface ITOverstockDeliveryDetailService extends IService<TOverstockDeliveryDetail> {

    /**
     * 查询越库单详情列表
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 越库单详情集合
     */
    public List<TOverstockDeliveryDetail> selectTOverstockDeliveryDetailList(TOverstockDeliveryDetail tOverstockDeliveryDetail);

    /**
     * 查询越库单详情
     *
     * @param id 越库单详情主键
     * @return 越库单详情
     */
    public TOverstockDeliveryDetail selectTOverstockDeliveryDetailById(Long id);

    /**
     * 新增越库单详情
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 结果
     */
    public int insertTOverstockDeliveryDetail(TOverstockDeliveryDetail tOverstockDeliveryDetail);

    /**
     * 修改越库单详情
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 结果
     */
    public int updateTOverstockDeliveryDetail(TOverstockDeliveryDetail tOverstockDeliveryDetail);

    /**
     * 批量删除越库单详情
     *
     * @param ids 需要删除的越库单详情主键集合
     * @return 结果
     */
    public int deleteTOverstockDeliveryDetailByIds(Long[] ids);

    /**
     * 根据主表ids删除数据
     * @param ids
     * @return
     */
    public int deleteDetailByOverstockIds(Long[] ids);

    /**
     * 删除越库单详情信息
     *
     * @param id 越库单详情主键
     * @return 结果
     */
    public int deleteTOverstockDeliveryDetailById(Long id);
}
