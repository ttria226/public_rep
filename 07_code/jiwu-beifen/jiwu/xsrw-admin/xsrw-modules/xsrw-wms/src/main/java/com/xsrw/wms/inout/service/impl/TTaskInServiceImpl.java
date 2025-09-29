package com.xsrw.wms.inout.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryDetailService;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TTaskInMapper;
import com.xsrw.wms.inout.domain.TTaskIn;
import com.xsrw.wms.inout.service.ITTaskInService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 入库任务详情Service业务层处理
 *
 * @author wxr
 * @date 2023-05-09
 */
@Service
public class TTaskInServiceImpl extends ServiceImpl<TTaskInMapper, TTaskIn> implements ITTaskInService {
    @Autowired
    private TTaskInMapper tTaskInMapper;
    @Autowired
    @Lazy
    private ITAdvanceDeliveryDetailService advanceDeliveryDetailService;
    @Autowired
    @Lazy
    private ITAdvanceDeliveryService advanceDeliveryService;

    /**
     * 查询入库任务详情列表
     *
     * @param tTaskIn 入库任务详情
     * @return 入库任务详情
     */
    @Override
    public List<TTaskIn> selectTTaskInList(TTaskIn tTaskIn) {
        return tTaskInMapper.selectTTaskInList(tTaskIn);
    }

    /**
     * 查询入库任务详情
     *
     * @param id 入库任务详情主键
     * @return 入库任务详情
     */
    @Override
    public TTaskIn selectTTaskInById(Long id) {
        return tTaskInMapper.selectById(id);
    }

    /**
     * 新增入库任务详情
     *
     * @param tTaskIn 入库任务详情
     * @return 结果
     */
    @Override
    public int insertTTaskIn(TTaskIn tTaskIn) {
        return tTaskInMapper.insert(tTaskIn);
    }

    /**
     * 修改入库任务详情
     *
     * @param tTaskIn 入库任务详情
     * @return 结果
     */
    @Override
    public int updateTTaskIn(TTaskIn tTaskIn) {
        return tTaskInMapper.updateById(tTaskIn);
    }


    /**
     * 批量删除入库任务详情
     *
     * @param ids 需要删除的入库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskInByIds(Long[] ids) {
        return tTaskInMapper.deleteTTaskInByIds(ids);
    }

    /**
     * 删除入库任务详情信息
     *
     * @param id 入库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskInById(Long id) {
        return tTaskInMapper.deleteTTaskInById(id);
    }

    /**
     * 入库执行后更新状态
     *
     * @param tTaskInList
     * @return
     */
    @Override
    @Transactional
    public int executeEndTask(List<TTaskInVO> tTaskInList) {
        //更新表状态
        List<Long> originIds = tTaskInList.stream().map(TTaskInVO::getId).distinct().collect(Collectors.toList());
        tTaskInMapper.updateStatusByIds(originIds, Constants.INOUT_STATUS_END);
        List<TAdvanceDeliveryDetail> upadteList = new ArrayList<>();
        Set<Long> deliveryIds = new HashSet<>();
        Map<Long, List<TTaskInVO>> taskInMap = tTaskInList.stream().collect(Collectors.groupingBy(TTaskInVO::getAdvanceRegistrationId));
        taskInMap.forEach((detailId, taskInList) -> {
            TAdvanceDeliveryDetail tAdvanceDeliveryDetail = new TAdvanceDeliveryDetail();
            tAdvanceDeliveryDetail.setId(detailId);
            //已上架数量
            Long putawayCountDelivery = taskInList.get(0).getPutawayCountDelivery();
            //检测成功数量
            Long detectionCountDelivery = taskInList.get(0).getDetectionCountDelivery();
            //已入库数量
            Long receiveCountDelivery = taskInList.get(0).getReceiveCountDelivery();
            Long deliveryId = taskInList.get(0).getOriginId();
            Long sumCount = taskInList.stream().mapToLong(TTaskIn::getActualCount).sum();
            Long redisCount = detectionCountDelivery - receiveCountDelivery;
            if (redisCount.equals(sumCount)) {
                tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_END);
                tAdvanceDeliveryDetail.setReceiveCount(detectionCountDelivery);
            } else {
                tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_PART);
                tAdvanceDeliveryDetail.setReceiveCount(sumCount + receiveCountDelivery);
            }
            upadteList.add(tAdvanceDeliveryDetail);
            deliveryIds.add(deliveryId);
        });
        if (!CollectionUtils.isEmpty(upadteList)) {
            //更新子表状态
            advanceDeliveryDetailService.updateBatchById(upadteList);
            deliveryIds.forEach(deliveryId -> {
                //查询入库单详情状态，更改主表状态
                String status = advanceDeliveryService.getDetailCountStatus(deliveryId, 3);
                if (StringUtils.isNotEmpty(status)) {
                    TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
                    updateDelivery.setId(deliveryId);
                    updateDelivery.setCompleteState(status);
                    updateDelivery.setStatus(status);
                    advanceDeliveryService.updateById(updateDelivery);
                    if(Constants.INOUT_STATUS_COMPLETE_END.equals(status)){
                        //全部完成，根据类型，更新调拨单
                        advanceDeliveryService.updateAllotByDeliveryId(deliveryId);
                    }
                }
            });
            return 1;
        }
        return 0;
    }

    @Override
    public List<TTaskInVO> selectTTaskInInfoByIds(List<Long> originIds) {
        return tTaskInMapper.selectTTaskInInfoByIds(originIds);
    }

    /**
     * 根据入库登记id查询详情
     *
     * @param registrationId
     * @return
     */
    @Override
    public List<TTaskInVO> selectTTaskInInfoByRegistrationId(Long registrationId) {
        return tTaskInMapper.selectTTaskInInfoByRegistrationId(registrationId);
    }


}
