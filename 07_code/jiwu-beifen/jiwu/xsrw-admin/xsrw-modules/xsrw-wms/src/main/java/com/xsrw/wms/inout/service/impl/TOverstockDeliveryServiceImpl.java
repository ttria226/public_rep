package com.xsrw.wms.inout.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TOverstockDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TOverstockDeliveryVO;
import com.xsrw.wms.inout.mapper.TOverstockDeliveryDetailMapper;
import com.xsrw.wms.inout.service.ITOverstockDeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOverstockDeliveryMapper;
import com.xsrw.wms.inout.service.ITOverstockDeliveryService;

/**
 * 越库单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-25
 */
@Service
public class TOverstockDeliveryServiceImpl extends ServiceImpl<TOverstockDeliveryMapper, TOverstockDelivery> implements ITOverstockDeliveryService {
    @Autowired
    private TOverstockDeliveryMapper tOverstockDeliveryMapper;
    @Autowired
    private TOverstockDeliveryDetailMapper tOverstockDeliveryDetailMapper;
    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private ITOverstockDeliveryDetailService overstockDeliveryDetailService;

    /**
     * 查询越库单列表
     *
     * @param tOverstockDelivery 越库单
     * @return 越库单
     */
    @Override
    public List<TOverstockDelivery> selectTOverstockDeliveryList(TOverstockDelivery tOverstockDelivery) {
        return tOverstockDeliveryMapper.selectTOverstockDeliveryList(tOverstockDelivery);
    }

    /**
     * 查询越库单
     *
     * @param id 越库单主键
     * @return 越库单
     */
    @Override
    public TOverstockDeliveryVO selectTOverstockDeliveryById(Long id) {
        TOverstockDeliveryVO overstockDeliveryVO = new TOverstockDeliveryVO();
        TOverstockDelivery overstockDelivery = tOverstockDeliveryMapper.selectById(id);
        BeanUtils.copyBeanProp(overstockDeliveryVO, overstockDelivery);
        overstockDeliveryVO.setDeliveryDetailList(tOverstockDeliveryDetailMapper.selectDetailListByDeliveryId(id));
        return overstockDeliveryVO;
    }

    /**
     * 新增越库单
     *
     * @param tOverstockDelivery 越库单
     * @return 结果
     */
    @Override
    public int insertTOverstockDelivery(TOverstockDeliveryDTO tOverstockDelivery) {
        TOverstockDelivery overstockDelivery = new TOverstockDelivery();
        BeanUtils.copyBeanProp(overstockDelivery, tOverstockDelivery);
        overstockDelivery.setCode(codeConfigService.getCode(CodeEnum.MYK.getCodeName()));
        overstockDelivery.setNewLocal(Constants.DELIVERY_IN_TYPE_LOCAL);
        overstockDelivery.setStatus(Constants.OVERSTOCK_STATUS_WAITING);
        tOverstockDeliveryMapper.insert(overstockDelivery);
        List<TOverstockDeliveryDetail> deliveryDetailList = tOverstockDelivery.getDeliveryDetailList();
        if (!CollectionUtils.isEmpty(deliveryDetailList)) {
            deliveryDetailList.forEach(e -> {
                e.setOverDeliveryId(overstockDelivery.getId());
                e.setNextFlag(Constants.INOUT_NEXTFLAG_NOT);
                e.setReceiveCount(0L);
                e.setRegistrationCount(0L);
            });
            overstockDeliveryDetailService.saveBatch(deliveryDetailList);
        }
        return 1;
    }

    /**
     * 修改越库单
     *
     * @param tOverstockDelivery 越库单
     * @return 结果
     */
    @Override
    public int updateTOverstockDelivery(TOverstockDeliveryDTO tOverstockDelivery) {
        TOverstockDelivery overstockDelivery = new TOverstockDelivery();
        BeanUtils.copyBeanProp(overstockDelivery, tOverstockDelivery);
        tOverstockDeliveryMapper.updateById(overstockDelivery);
        //删除子表信息
        Long[] ids = new Long[1];
        ids[0] = overstockDelivery.getId();
        //子表
        overstockDeliveryDetailService.deleteDetailByOverstockIds(ids);

        List<TOverstockDeliveryDetail> deliveryDetailList = tOverstockDelivery.getDeliveryDetailList();
        if (!CollectionUtils.isEmpty(deliveryDetailList)) {
            deliveryDetailList.forEach(e -> {
                e.setOverDeliveryId(overstockDelivery.getId());
                e.setNextFlag(Constants.INOUT_NEXTFLAG_NOT);
                e.setReceiveCount(0L);
                e.setRegistrationCount(0L);
            });
            overstockDeliveryDetailService.saveBatch(deliveryDetailList);
        }
        return 1;
    }


    /**
     * 批量删除越库单
     *
     * @param ids 需要删除的越库单主键
     * @return 结果
     */
    @Override
    public int deleteTOverstockDeliveryByIds(Long[] ids) {
        //子表
        overstockDeliveryDetailService.deleteDetailByOverstockIds(ids);
        return tOverstockDeliveryMapper.deleteTOverstockDeliveryByIds(ids);
    }

    /**
     * 越库单收货
     *
     * @param tOverstockDelivery
     * @return
     */
    @Override
    public AjaxResult registerDelivery(TOverstockDeliveryDTO tOverstockDelivery) {
        List<TOverstockDeliveryDetail> deliveryDetailList = tOverstockDelivery.getDeliveryDetailList();
        if (CollectionUtils.isEmpty(deliveryDetailList)) {
            return AjaxResult.error("物料列表不可为空");
        }
        //检验入库单状态和数据
        TOverstockDelivery oldDelivery = tOverstockDeliveryMapper.selectById(tOverstockDelivery.getId());
        if (oldDelivery == null
                || (!Constants.OVERSTOCK_STATUS_WAITING.equals(oldDelivery.getStatus()) &&
                !Constants.OVERSTOCK_STATUS_REGISTER_PART.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可收货");
        }
        //批量更新子表登记数量
        overstockDeliveryDetailService.updateBatchById(deliveryDetailList);
        //更新入库单检测状态
        String status = getDetailCountStatus(oldDelivery.getId(), 1);
        if (StringUtils.isNotEmpty(status)) {
            TOverstockDelivery updateDelivery = new TOverstockDelivery();
            updateDelivery.setId(oldDelivery.getId());
            updateDelivery.setStatus(status);
            tOverstockDeliveryMapper.updateById(updateDelivery);
        }
        return AjaxResult.success();
    }

    /**
     * 越库单出库
     *
     * @param tOverstockDelivery
     * @return
     */
    @Override
    public AjaxResult outDelivery(TOverstockDeliveryDTO tOverstockDelivery) {
        List<TOverstockDeliveryDetail> deliveryDetailList = tOverstockDelivery.getDeliveryDetailList();
        if (CollectionUtils.isEmpty(deliveryDetailList)) {
            return AjaxResult.error("物料列表不可为空");
        }
        //检验入库单状态和数据
        TOverstockDelivery oldDelivery = tOverstockDeliveryMapper.selectById(tOverstockDelivery.getId());
        if (oldDelivery == null
                || (!Constants.OVERSTOCK_STATUS_REGISTER.equals(oldDelivery.getStatus()) &&
                !Constants.OVERSTOCK_STATUS_OUT_PART.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可出库");
        }
        //批量更新子表登记数量
        overstockDeliveryDetailService.updateBatchById(deliveryDetailList);
        //更新入库单检测状态
        String status = getDetailCountStatus(oldDelivery.getId(), 2);
        if (StringUtils.isNotEmpty(status)) {
            TOverstockDelivery updateDelivery = new TOverstockDelivery();
            updateDelivery.setId(oldDelivery.getId());
            updateDelivery.setStatus(status);
            tOverstockDeliveryMapper.updateById(updateDelivery);
        }
        return AjaxResult.success();
    }

    /**
     * 根据id统计子表数量状态
     *
     * @param deliveryId
     * @param type
     * @return
     */
    public String getDetailCountStatus(Long deliveryId, int type) {
        String status = "";
        QueryWrapper<TOverstockDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("over_delivery_id", deliveryId);
        List<TOverstockDeliveryDetail> tAdvanceDeliveryDetails = tOverstockDeliveryDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(tAdvanceDeliveryDetails)) {
            if (type == 1) {
                List<TOverstockDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getPredictCount().equals(e.getRegistrationCount())).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.OVERSTOCK_STATUS_REGISTER;
                } else {
                    status = Constants.OVERSTOCK_STATUS_REGISTER_PART;
                }
            } else {
                List<TOverstockDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getRegistrationCount().equals(e.getReceiveCount())).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.OVERSTOCK_STATUS_OUT;
                } else {
                    status = Constants.OVERSTOCK_STATUS_OUT_PART;
                }
            }
        }
        return status;
    }

}
