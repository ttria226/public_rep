package com.xsrw.wms.inout.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TOutRemovalDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TOutRemovalVO;
import com.xsrw.wms.inout.mapper.TOutDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TOutDeliveryMapper;
import com.xsrw.wms.inout.mapper.TOutRemovalMapper;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.inout.service.ITOutRemovalDetailService;
import com.xsrw.wms.inout.service.ITOutRemovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出库单Service业务层处理
 *
 * @author zjj
 * @date 2023-06-05
 */
@Service
public class TOutRemovalServiceImpl extends ServiceImpl<TOutRemovalMapper, TOutRemoval> implements ITOutRemovalService {
    @Autowired
    private TOutRemovalMapper tOutRemovalMapper;
    @Autowired
    private TOutDeliveryMapper tOutDeliveryMapper;
    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;
    @Autowired
    private ITOutDeliveryService outDeliveryService;
    @Autowired
    private ITOutRemovalDetailService outRemovalDetailService;

    /**
     * 查询出库单列表
     *
     * @param tOutRemoval 出库单
     * @return 出库单
     */
    @Override
    public List<TOutRemovalVO> selectTOutRemovalList(TOutRemoval tOutRemoval) {
        return tOutRemovalMapper.selectTOutRemovalList(tOutRemoval);
    }

    /**
     * 查询出库单
     *
     * @param id 出库单主键
     * @return 出库单
     */
    @Override
    public TOutDelivery selectTOutRemovalById(Long id) {
        TOutDelivery tOutDelivery = new TOutDelivery();
        TOutRemoval tOutRemoval = tOutRemovalMapper.selectById(id);
        if (tOutRemoval != null && tOutRemoval.getOriginId() != null) {
            tOutDelivery = outDeliveryService.getById(tOutRemoval.getOriginId());
            tOutDelivery.settOutDeliveryDetailList(tOutRemovalMapper.selectDetailListByDeliveryId(tOutRemoval.getOriginId()));
        }
        tOutDelivery.setId(id);
        return tOutDelivery;
    }

    /**
     * 新增出库单
     *
     * @param tOutRemoval 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertTOutRemoval(TOutRemoval tOutRemoval) {
        Long count = this.getExistCountByOriginId(tOutRemoval.getOriginId());
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        tOutRemoval.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        tOutRemovalMapper.insert(tOutRemoval);
        List<TOutRemovalDetail> saveDetailList = new ArrayList<>();
        QueryWrapper<TOutDeliveryDetail> deliveryDetailQw = new QueryWrapper();
        deliveryDetailQw.eq("del_flag", Constants.DEL_FLAG_NO);
        deliveryDetailQw.eq("out_delivery_id", tOutRemoval.getOriginId());
        List<TOutDeliveryDetail> advanceDeliveryDetailList = tOutDeliveryDetailMapper.selectList(deliveryDetailQw);
        advanceDeliveryDetailList.forEach(e -> {
            TOutRemovalDetail advanceRemovalDetail = new TOutRemovalDetail();
            advanceRemovalDetail.setDetailOriginId(e.getId());
            advanceRemovalDetail.setReturnCount(0L);
            saveDetailList.add(advanceRemovalDetail);
        });
        outRemovalDetailService.saveBatch(saveDetailList);
        return AjaxResult.success();
    }

    /**
     * 获取原单是否已存在
     *
     * @param originId
     * @return
     */
    public Long getExistCountByOriginId(Long originId) {
        QueryWrapper<TOutRemoval> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("origin_id", originId);
        return tOutRemovalMapper.selectCount(queryWrapper);
    }

    /**
     * 修改出库单
     *
     * @param tOutRemoval 出库单
     * @return 结果
     */
    @Override
    public int updateTOutRemoval(TOutRemoval tOutRemoval) {
        return tOutRemovalMapper.updateById(tOutRemoval);
    }


    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的出库单主键
     * @return 结果
     */
    @Override
    public int deleteTOutRemovalByIds(Long[] ids) {
        return tOutRemovalMapper.deleteTOutRemovalByIds(ids);
    }

    /**
     * 删除出库单信息
     *
     * @param id 出库单主键
     * @return 结果
     */
    @Override
    public int deleteTOutRemovalById(Long id) {
        return tOutRemovalMapper.deleteTOutRemovalById(id);
    }

    /**
     * 退货
     *
     * @param tOutRemovalDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult returnStatus(TOutRemovalDTO tOutRemovalDTO) {
        TOutRemoval tOutRemovalVO = tOutRemovalMapper.selectById(tOutRemovalDTO.getId());
        if (tOutRemovalVO == null) {
            return AjaxResult.error("未查询到单据信息");
        }
        if (Constants.INOUT_STATUS_END.equals(tOutRemovalVO.getStatus())) {
            return AjaxResult.error("已全部完成不可再次退货");
        }
        TOutDelivery outDelivery = tOutDeliveryMapper.selectById(tOutRemovalVO.getOriginId());
        if (outDelivery == null) {
            return AjaxResult.error("未查询到原单信息");
        }
        if (!Constants.INOUT_STATUS_END.equals(outDelivery.getCompleteState())
                || (!Constants.INOUT_STATUS_RETURN_PART.equals(outDelivery.getStatus()))
                && !Constants.INOUT_STATUS_PASS.equals(outDelivery.getStatus())) {
            return AjaxResult.error("当前状态已不可退货");
        }
        List<TOutRemovalDetail> detailList = tOutRemovalDTO.gettOutDeliveryDetailList();
        outRemovalDetailService.updateBatchById(detailList);
        //获取主单数据信息
        List<TOutDeliveryDetail> deliveryDetailVOS = tOutRemovalMapper.selectDetailListByDeliveryId(tOutRemovalVO.getOriginId());
        List<TOutDeliveryDetail> endCount = deliveryDetailVOS.stream().filter(e -> e.getPredictCount().equals(e.getReturnCount())).collect(Collectors.toList());
        TOutRemoval updateRemoval = new TOutRemoval();
        updateRemoval.setId(tOutRemovalDTO.getId());
        updateRemoval.setRemark(tOutRemovalDTO.getRemark());
        TOutDelivery updateDelivery = new TOutDelivery();
        updateDelivery.setId(tOutRemovalVO.getOriginId());
        if (deliveryDetailVOS.size() == endCount.size()) {
            updateDelivery.setStatus(Constants.INOUT_STATUS_RETURN);
            updateRemoval.setStatus(Constants.INOUT_STATUS_END);
        } else {
            updateDelivery.setStatus(Constants.INOUT_STATUS_RETURN_PART);
            updateRemoval.setStatus(Constants.INOUT_STATUS_PART);
        }
        //更新入库单状态
        tOutDeliveryMapper.updateById(updateDelivery);
        //更新状态
        tOutRemovalMapper.updateById(updateRemoval);
        return AjaxResult.success();
    }
}
