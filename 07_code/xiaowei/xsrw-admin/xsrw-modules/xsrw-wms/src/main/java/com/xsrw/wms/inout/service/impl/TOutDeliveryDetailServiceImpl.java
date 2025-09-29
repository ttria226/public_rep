package com.xsrw.wms.inout.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOutDeliveryDetailMapper;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.service.ITOutDeliveryDetailService;

/**
 * 出库单详情Service业务层处理
 *
 * @author zyq
 * @date 2023-05-09
 */
@Service
public class TOutDeliveryDetailServiceImpl extends ServiceImpl<TOutDeliveryDetailMapper, TOutDeliveryDetail> implements ITOutDeliveryDetailService
{
    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;


    /**
     * 查询出库单详情列表
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 出库单详情
     */
    @Override
    public List<TOutDeliveryDetail> selectTOutDeliveryDetailList(TOutDeliveryDetail tOutDeliveryDetail)
    {
        return tOutDeliveryDetailMapper.selectTOutDeliveryDetailList(tOutDeliveryDetail);
    }

    /**
     * 查询出库单详情
     *
     * @param id 出库单详情主键
     * @return 出库单详情
     */
    @Override
    public TOutDeliveryDetail selectTOutDeliveryDetailById(Long id)
    {
        return tOutDeliveryDetailMapper.selectById(id);
    }

    /**
     * 新增出库单详情
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 结果
     */
    @Override
    public int insertTOutDeliveryDetail(TOutDeliveryDetail tOutDeliveryDetail)
    {
        return tOutDeliveryDetailMapper.insert(tOutDeliveryDetail);
    }

    /**
     * 修改出库单详情
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 结果
     */
    @Override
    public int updateTOutDeliveryDetail(TOutDeliveryDetail tOutDeliveryDetail)
    {
        return tOutDeliveryDetailMapper.updateById(tOutDeliveryDetail);
    }


    /**
     * 批量删除出库单详情
     *
     * @param ids 需要删除的出库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOutDeliveryDetailByIds(Long[] ids)
    {
        return tOutDeliveryDetailMapper.deleteTOutDeliveryDetailByIds(ids);
    }

    /**
     * 删除出库单详情信息
     *
     * @param id 出库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOutDeliveryDetailById(Long id)
    {
        return tOutDeliveryDetailMapper.deleteTOutDeliveryDetailById(id);
    }
}
