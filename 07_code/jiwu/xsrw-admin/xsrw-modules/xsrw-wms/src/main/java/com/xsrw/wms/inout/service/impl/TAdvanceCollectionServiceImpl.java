package com.xsrw.wms.inout.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TAdvanceCollectionDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceCollectionVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import com.xsrw.wms.inout.service.ITAdvanceCollectionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceCollectionMapper;
import com.xsrw.wms.inout.service.ITAdvanceCollectionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库收货退货单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-06
 */
@Service
public class TAdvanceCollectionServiceImpl extends ServiceImpl<TAdvanceCollectionMapper, TAdvanceCollection> implements ITAdvanceCollectionService {
    @Autowired
    private TAdvanceCollectionMapper tAdvanceCollectionMapper;
    @Autowired
    private TAdvanceDeliveryMapper tAdvanceDeliveryMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;
    @Autowired
    private ITAdvanceCollectionDetailService advanceCollectionDetailService;


    /**
     * 查询入库收货退货单列表
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 入库收货退货单
     */
    @Override
    public List<TAdvanceCollectionVO> selectTAdvanceCollectionList(TAdvanceCollectionDTO tAdvanceCollection) {
        return tAdvanceCollectionMapper.selectTAdvanceCollectionList(tAdvanceCollection);
    }

    /**
     * 查询入库收货退货单
     *
     * @param id 入库收货退货单主键
     * @return 入库收货退货单
     */
    @Override
    public TAdvanceDeliveryVO selectTAdvanceCollectionById(Long id) {
        TAdvanceDeliveryVO tAdvanceDeliveryVO = new TAdvanceDeliveryVO();
        TAdvanceCollection tAdvanceCollection = tAdvanceCollectionMapper.selectById(id);
        if (tAdvanceCollection != null) {
            TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceCollection.getOriginId());
            if (tAdvanceDelivery != null) {
                BeanUtils.copyBeanProp(tAdvanceDeliveryVO, tAdvanceDelivery);
                tAdvanceDeliveryVO.setDeliveryDetailList(tAdvanceCollectionMapper.selectDetailListByDeliveryId(tAdvanceDelivery.getId()));
            }
        }
        tAdvanceDeliveryVO.setId(id);
        return tAdvanceDeliveryVO;
    }

    /**
     * 新增入库收货退货单
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertTAdvanceCollection(TAdvanceCollection tAdvanceCollection) {
        TAdvanceCollection advanceCollectionDTO = new TAdvanceCollection();
        advanceCollectionDTO.setId(tAdvanceCollection.getOriginId());
        Long count = this.getExistCountByOriginId(tAdvanceCollection.getOriginId());
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        tAdvanceCollection.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        tAdvanceCollectionMapper.insert(tAdvanceCollection);
        List<TAdvanceCollectionDetail> saveDetailList = new ArrayList<>();
        QueryWrapper<TAdvanceDeliveryDetail> deliveryDetailQw = new QueryWrapper();
        deliveryDetailQw.eq("del_flag", Constants.DEL_FLAG_NO);
        deliveryDetailQw.eq("advance_delivery_id", tAdvanceCollection.getOriginId());
        List<TAdvanceDeliveryDetail> advanceDeliveryDetailList = tAdvanceDeliveryDetailMapper.selectList(deliveryDetailQw);
        advanceDeliveryDetailList.forEach(e -> {
            TAdvanceCollectionDetail advanceCollectionDetail = new TAdvanceCollectionDetail();
            advanceCollectionDetail.setDetailOriginId(e.getId());
            advanceCollectionDetail.setReturnCount(0L);
            saveDetailList.add(advanceCollectionDetail);
        });
        advanceCollectionDetailService.saveBatch(saveDetailList);
        return AjaxResult.success();
    }
    /**
     * 获取原单是否已存在
     *
     * @param originId
     * @return
     */
    public Long getExistCountByOriginId(Long originId) {
        QueryWrapper<TAdvanceCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("origin_id", originId);
        return tAdvanceCollectionMapper.selectCount(queryWrapper);
    }

    /**
     * 修改入库收货退货单
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 结果
     */
    @Override
    public int updateTAdvanceCollection(TAdvanceCollection tAdvanceCollection) {
        return tAdvanceCollectionMapper.updateById(tAdvanceCollection);
    }


    /**
     * 批量删除入库收货退货单
     *
     * @param ids 需要删除的入库收货退货单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceCollectionByIds(Long[] ids) {
        return tAdvanceCollectionMapper.deleteTAdvanceCollectionByIds(ids);
    }

    /**
     * 删除入库收货退货单信息
     *
     * @param id 入库收货退货单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceCollectionById(Long id) {
        return tAdvanceCollectionMapper.deleteTAdvanceCollectionById(id);
    }

    /**
     * 入库收货退货单退货
     *
     * @param tAdvanceCollectionDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult returnStatus(TAdvanceCollectionDTO tAdvanceCollectionDTO) {
        TAdvanceCollection tAdvanceCollectionVO = tAdvanceCollectionMapper.selectById(tAdvanceCollectionDTO.getId());
        if (tAdvanceCollectionVO == null) {
            return AjaxResult.error("未查询到单据信息");
        }
        if (Constants.INOUT_STATUS_END.equals(tAdvanceCollectionVO.getStatus())) {
            return AjaxResult.error("已全部完成不可再次退货");
        }
        TAdvanceDelivery advanceDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceCollectionVO.getOriginId());
        if (advanceDelivery == null) {
            return AjaxResult.error("未查询到原单信息");
        }
        if (!Constants.INOUT_STATUS_REGISTER.equals(advanceDelivery.getStatus()) && !Constants.INOUT_STATUS_RETURN_PART.equals(advanceDelivery.getStatus())) {
            return AjaxResult.error("当前状态已不可退货");
        }
        List<TAdvanceCollectionDetail> detailList = tAdvanceCollectionDTO.getDetailList();
        advanceCollectionDetailService.updateBatchById(detailList);
        //获取主单数据信息
        List<TAdvanceDeliveryDetailVO> deliveryDetailVOS = tAdvanceCollectionMapper.selectDetailListByDeliveryId(tAdvanceCollectionVO.getOriginId());
        List<TAdvanceDeliveryDetailVO> endCount = deliveryDetailVOS.stream().filter(e -> e.getPredictCount().equals(e.getReturnCount())).collect(Collectors.toList());
        TAdvanceCollection updateCollection = new TAdvanceCollection();
        updateCollection.setId(tAdvanceCollectionDTO.getId());
        TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
        updateDelivery.setId(tAdvanceCollectionVO.getOriginId());
        if (deliveryDetailVOS.size() == endCount.size()) {
            updateDelivery.setStatus(Constants.INOUT_STATUS_RETURN);
            updateCollection.setStatus(Constants.INOUT_STATUS_END);
        } else {
            updateDelivery.setStatus(Constants.INOUT_STATUS_RETURN_PART);
            updateCollection.setStatus(Constants.INOUT_STATUS_PART);
        }
        //更新入库单状态
        tAdvanceDeliveryMapper.updateById(updateDelivery);
        //更新状态
        tAdvanceCollectionMapper.updateById(updateCollection);
        return AjaxResult.success();
    }
}
