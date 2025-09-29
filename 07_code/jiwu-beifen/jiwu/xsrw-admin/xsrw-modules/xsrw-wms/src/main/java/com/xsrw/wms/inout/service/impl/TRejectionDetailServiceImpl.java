package com.xsrw.wms.inout.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TRejectionDetailDTO;
import com.xsrw.wms.inout.domain.vo.TRejectionDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TRejectionDetailMapper;
import com.xsrw.wms.inout.domain.TRejectionDetail;
import com.xsrw.wms.inout.service.ITRejectionDetailService;

/**
 * 拒收管理Service业务层处理
 *
 * @author wxr
 * @date 2023-05-09
 */
@Service
public class TRejectionDetailServiceImpl extends ServiceImpl<TRejectionDetailMapper, TRejectionDetail> implements ITRejectionDetailService {
    @Autowired
    private TRejectionDetailMapper tRejectionDetailMapper;


    /**
     * 查询拒收管理列表
     *
     * @param tRejectionDetail 拒收管理
     * @return 拒收管理
     */
    @Override
    public List<TRejectionDetailVO> selectTRejectionDetailList(TRejectionDetailDTO tRejectionDetail) {
        return tRejectionDetailMapper.selectTRejectionDetailList(tRejectionDetail);
    }

    /**
     * 查询拒收管理
     *
     * @param id 拒收管理主键
     * @return 拒收管理
     */
    @Override
    public TRejectionDetail selectTRejectionDetailById(Long id) {
        return tRejectionDetailMapper.selectById(id);
    }

    /**
     * 新增拒收管理
     *
     * @param tRejectionDetail 拒收管理
     * @return 结果
     */
    @Override
    public int insertTRejectionDetail(TRejectionDetail tRejectionDetail) {
        return tRejectionDetailMapper.insert(tRejectionDetail);
    }

    /**
     * 修改拒收管理
     *
     * @param tRejectionDetail 拒收管理
     * @return 结果
     */
    @Override
    public int updateTRejectionDetail(TRejectionDetail tRejectionDetail) {
        return tRejectionDetailMapper.updateById(tRejectionDetail);
    }


    /**
     * 批量删除拒收管理
     *
     * @param ids 需要删除的拒收管理主键
     * @return 结果
     */
    @Override
    public int deleteTRejectionDetailByIds(Long[] ids) {
        return tRejectionDetailMapper.deleteTRejectionDetailByIds(ids);
    }

    /**
     * 删除拒收管理信息
     *
     * @param id 拒收管理主键
     * @return 结果
     */
    @Override
    public int deleteTRejectionDetailById(Long id) {
        return tRejectionDetailMapper.deleteTRejectionDetailById(id);
    }

    @Override
    public Boolean saveRejectionList(List<TRejectionDetail> deliveryDetailList) {
        if(CollectionUtils.isEmpty(deliveryDetailList)){
           return false;
        }
        return this.saveBatch(deliveryDetailList);
    }
}
