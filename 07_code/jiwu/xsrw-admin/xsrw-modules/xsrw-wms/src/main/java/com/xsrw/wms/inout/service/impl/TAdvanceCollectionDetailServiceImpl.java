package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceCollectionDetailMapper;
import com.xsrw.wms.inout.domain.TAdvanceCollectionDetail;
import com.xsrw.wms.inout.service.ITAdvanceCollectionDetailService;

/**
 * 入库收货退货单详情Service业务层处理
 *
 * @author wxr
 * @date 2023-06-08
 */
@Service
public class TAdvanceCollectionDetailServiceImpl extends ServiceImpl<TAdvanceCollectionDetailMapper, TAdvanceCollectionDetail> implements ITAdvanceCollectionDetailService {
    @Autowired
    private TAdvanceCollectionDetailMapper tAdvanceCollectionDetailMapper;


    /**
     * 查询入库收货退货单详情列表
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 入库收货退货单详情
     */
    @Override
    public List<TAdvanceCollectionDetail> selectTAdvanceCollectionDetailList(TAdvanceCollectionDetail tAdvanceCollectionDetail) {
        return tAdvanceCollectionDetailMapper.selectTAdvanceCollectionDetailList(tAdvanceCollectionDetail);
    }

    /**
     * 查询入库收货退货单详情
     *
     * @param id 入库收货退货单详情主键
     * @return 入库收货退货单详情
     */
    @Override
    public TAdvanceCollectionDetail selectTAdvanceCollectionDetailById(Long id) {
        return tAdvanceCollectionDetailMapper.selectById(id);
    }

    /**
     * 新增入库收货退货单详情
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 结果
     */
    @Override
    public int insertTAdvanceCollectionDetail(TAdvanceCollectionDetail tAdvanceCollectionDetail) {
        return tAdvanceCollectionDetailMapper.insert(tAdvanceCollectionDetail);
    }

    /**
     * 修改入库收货退货单详情
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 结果
     */
    @Override
    public int updateTAdvanceCollectionDetail(TAdvanceCollectionDetail tAdvanceCollectionDetail) {
        return tAdvanceCollectionDetailMapper.updateById(tAdvanceCollectionDetail);
    }


    /**
     * 批量删除入库收货退货单详情
     *
     * @param ids 需要删除的入库收货退货单详情主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceCollectionDetailByIds(Long[] ids) {
        return tAdvanceCollectionDetailMapper.deleteTAdvanceCollectionDetailByIds(ids);
    }

    /**
     * 删除入库收货退货单详情信息
     *
     * @param id 入库收货退货单详情主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceCollectionDetailById(Long id) {
        return tAdvanceCollectionDetailMapper.deleteTAdvanceCollectionDetailById(id);
    }
}
