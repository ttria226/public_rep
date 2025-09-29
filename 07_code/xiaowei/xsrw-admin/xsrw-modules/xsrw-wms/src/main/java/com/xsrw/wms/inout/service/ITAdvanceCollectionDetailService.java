package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TAdvanceCollectionDetail;

/**
 * 入库收货退货单详情Service接口
 *
 * @author wxr
 * @date 2023-06-08
 */
public interface ITAdvanceCollectionDetailService extends IService<TAdvanceCollectionDetail> {

    /**
     * 查询入库收货退货单详情列表
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 入库收货退货单详情集合
     */
    public List<TAdvanceCollectionDetail> selectTAdvanceCollectionDetailList(TAdvanceCollectionDetail tAdvanceCollectionDetail);

    /**
     * 查询入库收货退货单详情
     *
     * @param id 入库收货退货单详情主键
     * @return 入库收货退货单详情
     */
    public TAdvanceCollectionDetail selectTAdvanceCollectionDetailById(Long id);

    /**
     * 新增入库收货退货单详情
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 结果
     */
    public int insertTAdvanceCollectionDetail(TAdvanceCollectionDetail tAdvanceCollectionDetail);

    /**
     * 修改入库收货退货单详情
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 结果
     */
    public int updateTAdvanceCollectionDetail(TAdvanceCollectionDetail tAdvanceCollectionDetail);

    /**
     * 批量删除入库收货退货单详情
     *
     * @param ids 需要删除的入库收货退货单详情主键集合
     * @return 结果
     */
    public int deleteTAdvanceCollectionDetailByIds(Long[] ids);

    /**
     * 删除入库收货退货单详情信息
     *
     * @param id 入库收货退货单详情主键
     * @return 结果
     */
    public int deleteTAdvanceCollectionDetailById(Long id);
}
