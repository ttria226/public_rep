package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOverstockDeliveryDetailMapper;
import com.xsrw.wms.inout.domain.TOverstockDeliveryDetail;
import com.xsrw.wms.inout.service.ITOverstockDeliveryDetailService;

/**
 * 越库单详情Service业务层处理
 *
 * @author wxr
 * @date 2023-06-25
 */
@Service
public class TOverstockDeliveryDetailServiceImpl extends ServiceImpl<TOverstockDeliveryDetailMapper, TOverstockDeliveryDetail> implements ITOverstockDeliveryDetailService {
    @Autowired
    private TOverstockDeliveryDetailMapper tOverstockDeliveryDetailMapper;


    /**
     * 查询越库单详情列表
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 越库单详情
     */
    @Override
    public List<TOverstockDeliveryDetail> selectTOverstockDeliveryDetailList(TOverstockDeliveryDetail tOverstockDeliveryDetail) {
        return tOverstockDeliveryDetailMapper.selectTOverstockDeliveryDetailList(tOverstockDeliveryDetail);
    }

    /**
     * 查询越库单详情
     *
     * @param id 越库单详情主键
     * @return 越库单详情
     */
    @Override
    public TOverstockDeliveryDetail selectTOverstockDeliveryDetailById(Long id) {
        return tOverstockDeliveryDetailMapper.selectById(id);
    }

    /**
     * 新增越库单详情
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 结果
     */
    @Override
    public int insertTOverstockDeliveryDetail(TOverstockDeliveryDetail tOverstockDeliveryDetail) {
        return tOverstockDeliveryDetailMapper.insert(tOverstockDeliveryDetail);
    }

    /**
     * 修改越库单详情
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 结果
     */
    @Override
    public int updateTOverstockDeliveryDetail(TOverstockDeliveryDetail tOverstockDeliveryDetail) {
        return tOverstockDeliveryDetailMapper.updateById(tOverstockDeliveryDetail);
    }


    /**
     * 批量删除越库单详情
     *
     * @param ids 需要删除的越库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOverstockDeliveryDetailByIds(Long[] ids) {
        return tOverstockDeliveryDetailMapper.deleteTOverstockDeliveryDetailByIds(ids);
    }

    /**
     * 根据主表ids删除数据
     * @param ids
     * @return
     */
    @Override
    public int deleteDetailByOverstockIds(Long[] ids) {
        return tOverstockDeliveryDetailMapper.deleteDetailByOverstockIds(ids);
    }

    /**
     * 删除越库单详情信息
     *
     * @param id 越库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOverstockDeliveryDetailById(Long id) {
        return tOverstockDeliveryDetailMapper.deleteTOverstockDeliveryDetailById(id);
    }
}
