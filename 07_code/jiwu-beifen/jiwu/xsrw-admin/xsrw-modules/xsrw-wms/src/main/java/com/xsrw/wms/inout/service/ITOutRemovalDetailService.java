package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TOutRemovalDetail;

/**
 * 出库发货退货单详情Service接口
 *
 * @author wxr
 * @date 2023-06-09
 */
public interface ITOutRemovalDetailService extends IService<TOutRemovalDetail> {

    /**
     * 查询出库发货退货单详情列表
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 出库发货退货单详情集合
     */
    public List<TOutRemovalDetail> selectTOutRemovalDetailList(TOutRemovalDetail tOutRemovalDetail);

    /**
     * 查询出库发货退货单详情
     *
     * @param id 出库发货退货单详情主键
     * @return 出库发货退货单详情
     */
    public TOutRemovalDetail selectTOutRemovalDetailById(Long id);

    /**
     * 新增出库发货退货单详情
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 结果
     */
    public int insertTOutRemovalDetail(TOutRemovalDetail tOutRemovalDetail);

    /**
     * 修改出库发货退货单详情
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 结果
     */
    public int updateTOutRemovalDetail(TOutRemovalDetail tOutRemovalDetail);

    /**
     * 批量删除出库发货退货单详情
     *
     * @param ids 需要删除的出库发货退货单详情主键集合
     * @return 结果
     */
    public int deleteTOutRemovalDetailByIds(Long[] ids);

    /**
     * 删除出库发货退货单详情信息
     *
     * @param id 出库发货退货单详情主键
     * @return 结果
     */
    public int deleteTOutRemovalDetailById(Long id);
}
