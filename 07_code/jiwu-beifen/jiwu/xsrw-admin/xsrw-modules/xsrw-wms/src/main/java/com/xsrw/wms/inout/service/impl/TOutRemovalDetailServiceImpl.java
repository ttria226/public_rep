package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOutRemovalDetailMapper;
import com.xsrw.wms.inout.domain.TOutRemovalDetail;
import com.xsrw.wms.inout.service.ITOutRemovalDetailService;

/**
 * 出库发货退货单详情Service业务层处理
 *
 * @author wxr
 * @date 2023-06-09
 */
@Service
public class TOutRemovalDetailServiceImpl extends ServiceImpl<TOutRemovalDetailMapper, TOutRemovalDetail> implements ITOutRemovalDetailService {
    @Autowired
    private TOutRemovalDetailMapper tOutRemovalDetailMapper;


    /**
     * 查询出库发货退货单详情列表
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 出库发货退货单详情
     */
    @Override
    public List<TOutRemovalDetail> selectTOutRemovalDetailList(TOutRemovalDetail tOutRemovalDetail) {
        return tOutRemovalDetailMapper.selectTOutRemovalDetailList(tOutRemovalDetail);
    }

    /**
     * 查询出库发货退货单详情
     *
     * @param id 出库发货退货单详情主键
     * @return 出库发货退货单详情
     */
    @Override
    public TOutRemovalDetail selectTOutRemovalDetailById(Long id) {
        return tOutRemovalDetailMapper.selectById(id);
    }

    /**
     * 新增出库发货退货单详情
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 结果
     */
    @Override
    public int insertTOutRemovalDetail(TOutRemovalDetail tOutRemovalDetail) {
        return tOutRemovalDetailMapper.insert(tOutRemovalDetail);
    }

    /**
     * 修改出库发货退货单详情
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 结果
     */
    @Override
    public int updateTOutRemovalDetail(TOutRemovalDetail tOutRemovalDetail) {
        return tOutRemovalDetailMapper.updateById(tOutRemovalDetail);
    }


    /**
     * 批量删除出库发货退货单详情
     *
     * @param ids 需要删除的出库发货退货单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOutRemovalDetailByIds(Long[] ids) {
        return tOutRemovalDetailMapper.deleteTOutRemovalDetailByIds(ids);
    }

    /**
     * 删除出库发货退货单详情信息
     *
     * @param id 出库发货退货单详情主键
     * @return 结果
     */
    @Override
    public int deleteTOutRemovalDetailById(Long id) {
        return tOutRemovalDetailMapper.deleteTOutRemovalDetailById(id);
    }
}
