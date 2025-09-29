package com.xsrw.wms.inout.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;

/**
 * 出库单详情Service接口
 *
 * @author zyq
 * @date 2023-05-09
 */
public interface ITOutDeliveryDetailService extends IService<TOutDeliveryDetail>
{

    /**
     * 查询出库单详情列表
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 出库单详情集合
     */
    public List<TOutDeliveryDetail> selectTOutDeliveryDetailList(TOutDeliveryDetail tOutDeliveryDetail);

    /**
     * 查询出库单详情
     *
     * @param id 出库单详情主键
     * @return 出库单详情
     */
    public TOutDeliveryDetail selectTOutDeliveryDetailById(Long id);

    /**
     * 新增出库单详情
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 结果
     */
    public int insertTOutDeliveryDetail(TOutDeliveryDetail tOutDeliveryDetail);

    /**
     * 修改出库单详情
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 结果
     */
    public int updateTOutDeliveryDetail(TOutDeliveryDetail tOutDeliveryDetail);

    /**
     * 批量删除出库单详情
     *
     * @param ids 需要删除的出库单详情主键集合
     * @return 结果
     */
    public int deleteTOutDeliveryDetailByIds(Long[] ids);

    /**
     * 删除出库单详情信息
     *
     * @param id 出库单详情主键
     * @return 结果
     */
    public int deleteTOutDeliveryDetailById(Long id);
}
