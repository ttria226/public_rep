package com.xsrw.wms.inout.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.api.domain.dto.TAdvanceDeliveryApiDTO;
import com.xsrw.wms.api.domain.vo.TMaterialDetailApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TBom;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.mapper.TBomDetailMapper;
import com.xsrw.wms.base.mapper.TBomMapper;
import com.xsrw.wms.base.mapper.TMaterialDetailPrintMapper;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITRuleService;
import com.xsrw.wms.base.service.ITUnitConfigService;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAllotMapper;
import com.xsrw.wms.inout.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库单Service业务层处理
 *
 * @author wxr
 * @date 2023-05-08
 */
@Service
public class TAdvanceDeliveryServiceImpl extends ServiceImpl<TAdvanceDeliveryMapper, TAdvanceDelivery> implements ITAdvanceDeliveryService {
    @Autowired
    private TAdvanceDeliveryMapper tAdvanceDeliveryMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;
    @Autowired
    private TMaterialMapper tMaterialMapper;
    @Autowired
    private TBomMapper tBomMapper;

    @Autowired
    private TBomDetailMapper tBomDetailMapper;
    @Autowired
    private TAllotMapper tAllotMapper;
    @Autowired
    private TMaterialDetailPrintMapper tMaterialDetailPrintMapper;
    @Autowired
    private ITRuleService ruleService;
    @Autowired
    private ITMaterialService materialService;
    @Autowired
    private ITAdvanceDeliveryDetailService advanceDeliveryDetailService;
    @Autowired
    private ITAdvanceRegistrationService advanceRegistrationService;
    @Autowired
    private ITRejectionDetailService rejectionDetailService;
    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITMaterialDetailService materialDetailService;
    @Autowired
    private ITUnitConfigService unitConfigService;
    @Autowired
    private RedisService redisService;


    /**
     * 查询入库单列表
     *
     * @param tAdvanceDelivery 入库单
     * @return 入库单
     */
    @Override
    public List<TAdvanceDeliveryVO> selectTAdvanceDeliveryList(TAdvanceDeliveryDTO tAdvanceDelivery) {
        if (StringUtils.isEmpty(tAdvanceDelivery.getDeliveryModule())) {
            tAdvanceDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
        }
        return tAdvanceDeliveryMapper.selectTAdvanceDeliveryList(tAdvanceDelivery);
    }

    /**
     * 查询入库单
     *
     * @param id 入库单主键
     * @return 入库单
     */
    @Override
    public TAdvanceDeliveryVO selectTAdvanceDeliveryById(Long id) {
        TAdvanceDeliveryVO tAdvanceDeliveryVO = new TAdvanceDeliveryVO();
        TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(id);
        BeanUtils.copyBeanProp(tAdvanceDeliveryVO, tAdvanceDelivery);
        if (tAdvanceDelivery.getBomId() != null) {
            TBom tBom = tBomMapper.selectById(tAdvanceDelivery.getBomId());
            tAdvanceDeliveryVO.setBomName(tBom == null ? null : tBom.getName());
        }
        tAdvanceDeliveryVO.setDeliveryDetailList(advanceDeliveryDetailService.selectDetailListByDeliveryId(id, null));
        return tAdvanceDeliveryVO;
    }

    /**
     * 根据code查询入库单
     *
     * @param code
     * @return
     */
    @Override
    public TAdvanceDeliveryVO getDetailByCode(String code) {
        QueryWrapper<TAdvanceDelivery> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("code", code);
        TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectOne(queryWrapper);
        if (tAdvanceDelivery == null) {
            return null;
        }
        if (!Constants.INOUT_STATUS_CHECKED.equals(tAdvanceDelivery.getStatus())
                && !Constants.INOUT_STATUS_COMPLETE_PART.equals(tAdvanceDelivery.getStatus())) {
            throw new ServiceException("此单据当前状态不可收货组盘");
        }
        return this.selectTAdvanceDeliveryById(tAdvanceDelivery.getId());
    }

    /**
     * 新增入库单
     *
     * @param tAdvanceDeliveryDTO 入库单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDeliveryDTO) {
        TAdvanceDelivery tAdvanceDelivery = new TAdvanceDelivery();
        BeanUtils.copyBeanProp(tAdvanceDelivery, tAdvanceDeliveryDTO);
        if(StringUtils.isEmpty(tAdvanceDelivery.getCode())){
            tAdvanceDelivery.setCode(codeConfigService.getCode(CodeEnum.MRK.getCodeName()));
        }
        tAdvanceDelivery.setCompleteState(Constants.INOUT_STATUS_NOT);
        if(StringUtils.isEmpty(tAdvanceDelivery.getNewLocal())){
            tAdvanceDelivery.setNewLocal(Constants.DELIVERY_IN_TYPE_LOCAL);
        }
        if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(tAdvanceDelivery.getDeliveryModule())) {
            tAdvanceDelivery.setStatus(Constants.INOUT_STATUS_WAITING);
        } else {
            tAdvanceDelivery.setStatus(Constants.INOUT_STATUS_PASS);
        }
        tAdvanceDeliveryMapper.insert(tAdvanceDelivery);
        List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDeliveryDTO.getDeliveryDetailList();
        if (!CollectionUtils.isEmpty(deliveryDetailList)) {
            deliveryDetailList.forEach(e -> {
                e.setAdvanceDeliveryId(tAdvanceDelivery.getId());
                e.setNextFlag(Constants.INOUT_NEXTFLAG_NOT);
                e.setReceiveCount(BigDecimal.ZERO);
                e.setDetectionCount(BigDecimal.ZERO);
                e.setPutawayCount(BigDecimal.ZERO);
            });
            advanceDeliveryDetailService.saveBatch(deliveryDetailList);
        }
        return 1;
    }

    /**
     * 修改入库单
     *
     * @param tAdvanceDeliveryDTO 入库单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDeliveryDTO) {
        TAdvanceDelivery tAdvanceDelivery = new TAdvanceDelivery();
        BeanUtils.copyBeanProp(tAdvanceDelivery, tAdvanceDeliveryDTO);
        tAdvanceDeliveryMapper.updateById(tAdvanceDelivery);
        //删除子表信息
        Long[] ids = new Long[1];
        ids[0] = tAdvanceDelivery.getId();

        //子表
        advanceDeliveryDetailService.deleteDetailByDeliveryIds(ids);

        List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDeliveryDTO.getDeliveryDetailList();
        if (!CollectionUtils.isEmpty(deliveryDetailList)) {
            deliveryDetailList.forEach(e -> {
                e.setId(null);
                e.setAdvanceDeliveryId(tAdvanceDelivery.getId());
                e.setNextFlag(Constants.INOUT_NEXTFLAG_NOT);
                e.setReceiveCount(BigDecimal.ZERO);
                e.setDetectionCount(BigDecimal.ZERO);
                e.setPutawayCount(BigDecimal.ZERO);
            });
            //新增子表信息
            advanceDeliveryDetailService.saveBatch(deliveryDetailList);
        }
        return 1;
    }


    /**
     * 批量删除入库单
     *
     * @param ids 需要删除的入库单主键
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult deleteTAdvanceDeliveryByIds(Long[] ids) {
        Integer count = tAdvanceDeliveryMapper.selectDelStatusCountByIds(ids);
        if (count > 0) {
            return AjaxResult.error("所选入库单已审核不可删除");
        }
        //子表信息
        advanceDeliveryDetailService.deleteDetailByDeliveryIds(ids);
        //主表
        tAdvanceDeliveryMapper.deleteTAdvanceDeliveryByIds(ids);
        return AjaxResult.success();

    }

    /**
     * 删除入库单信息
     *
     * @param id 入库单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceDeliveryById(Long id) {
        return tAdvanceDeliveryMapper.deleteTAdvanceDeliveryById(id);
    }


    /**
     * 获取单据物料选择列表
     *
     * @param tMaterial
     * @return
     */
    @Override
    public List<TMaterialVO> getMaterialSelectList(TMaterialDTO tMaterial) {
        Date makeDate = tMaterial.getMakeDate();
        makeDate = makeDate == null ? DateUtils.getNowDate() : makeDate;
        List<TMaterialVO> list = tMaterialMapper.getMaterialSelectList(tMaterial);
//        if (!CollectionUtils.isEmpty(list)) {
//            Date finalMakeDate = makeDate;
//            list.forEach(e -> {
//                e.setBatchCode(codeConfigService.getBatchCode1(e.getId(), finalMakeDate));
//            });
//        }
        return list;
    }

    /**
     * 审核
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult approveTAdvanceDelivery(TAdvanceDelivery tAdvanceDelivery) {
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null || !Constants.INOUT_STATUS_WAITING.equals(oldDelivery.getStatus())) {
            return AjaxResult.error("当前状态不可审核");
        }
        if (Constants.INOUT_STATUS_PASS.equals(tAdvanceDelivery.getStatus()) || Constants.INOUT_STATUS_FAILED.equals(tAdvanceDelivery.getStatus())) {
            tAdvanceDelivery.setAuditor(SecurityUtils.getUsername());
        }
        tAdvanceDeliveryMapper.updateById(tAdvanceDelivery);
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult registerTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery) {
        //检验入库单状态和数据
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null
                || (!Constants.INOUT_STATUS_PASS.equals(oldDelivery.getStatus()) &&
                !Constants.INOUT_STATUS_REGISTER_PART.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可登记");
        }
        List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDelivery.getDeliveryDetailList();
        if (CollectionUtils.isEmpty(deliveryDetailList)) {
            return AjaxResult.error("物料列表不可为空");
        }

        //过滤掉小于等于0的数量
        deliveryDetailList = deliveryDetailList.stream().filter(e -> e.getRegistrationCount() != null && e.getRegistrationCount().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(deliveryDetailList)) {

            //查询入库单详情信息
            List<TAdvanceDeliveryDetailVO> tAdvanceDeliveryDetailVOS = advanceDeliveryDetailService.selectDetailListByDeliveryId(tAdvanceDelivery.getId(), null);
            Map<Long, TAdvanceDeliveryDetailVO> detailVOMap = tAdvanceDeliveryDetailVOS.stream().collect(Collectors.toMap(TAdvanceDeliveryDetailVO::getId, Function.identity()));
            int registerEndCount = 0;//总完成登记数
            for (TAdvanceDeliveryDetail tAdvanceDeliveryDetail : deliveryDetailList) {
                TAdvanceDeliveryDetailVO oldDO = detailVOMap.get(tAdvanceDeliveryDetail.getId());
                if (oldDO == null) {
                    return AjaxResult.error("物料信息未获取到");
                }
                BigDecimal oldReceiveCount = oldDO.getReceiveCount() == null ? BigDecimal.ZERO : oldDO.getReceiveCount();//原实收数量
                BigDecimal receiveCount = tAdvanceDeliveryDetail.getRegistrationCount().add(oldReceiveCount);//现总共入库数量
                if (receiveCount.compareTo(oldDO.getPredictCount().subtract(oldDO.getDetectionCount())) == 1) {
                    return AjaxResult.error(oldDO.getMaterialName() + "不可超出预计入库数量");
                }
                if (receiveCount.compareTo(oldDO.getPredictCount().subtract(oldDO.getDetectionCount())) == 0) {//全部登记
                    tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_END);
                    registerEndCount++;
                } else {//部分登记
                    tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_PART);
                }
                tAdvanceDeliveryDetail.setReceiveCount(receiveCount);

                //生成登记任务
                TAdvanceRegistration registration = new TAdvanceRegistration();
                registration.setAdvanceDeliveryId(tAdvanceDelivery.getId());
                registration.setAdvanceDeliveryDetailId(tAdvanceDeliveryDetail.getId());
                registration.setMaterialId(oldDO.getMaterialId());
                registration.setBatchCode(oldDO.getBatchCode());
                registration.setNextFlag(Constants.NO);
                registration.setPredictCount(tAdvanceDeliveryDetail.getRegistrationCount());
                registration.setActualCount(BigDecimal.ZERO);
                registration.setStatus(Constants.INOUT_STATUS_NOT);
                advanceRegistrationService.save(registration);
            }

            //批量更新子表登记数量
            advanceDeliveryDetailService.updateBatchById(deliveryDetailList);


            //处理主表状态
            TAdvanceDelivery advanceDelivery = new TAdvanceDelivery();
            advanceDelivery.setId(tAdvanceDelivery.getId());
            advanceDelivery.setStatus(registerEndCount == deliveryDetailList.size() ? Constants.INOUT_STATUS_REGISTER : Constants.INOUT_STATUS_REGISTER_PART);
            tAdvanceDeliveryMapper.updateById(advanceDelivery);
        }
        return AjaxResult.success();
    }

    /**
     * 检测
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult checkTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery) {
        //检验入库单状态和数据
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null
                || (!Constants.INOUT_STATUS_PASS.equals(oldDelivery.getStatus()) &&
                !Constants.INOUT_STATUS_REGISTER.equals(oldDelivery.getStatus()) &&
                !Constants.INOUT_STATUS_REGISTER_PART.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可检测");
        }
        List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDelivery.getDeliveryDetailList();
        if (CollectionUtils.isEmpty(deliveryDetailList)) {
            return AjaxResult.error("物料列表不可为空");
        }

        if (!CollectionUtils.isEmpty(deliveryDetailList)) {
            //查询入库单详情信息
            List<TAdvanceDeliveryDetailVO> tAdvanceDeliveryDetailVOS = advanceDeliveryDetailService.selectDetailListByDeliveryId(tAdvanceDelivery.getId(), null);
            Map<Long, TAdvanceDeliveryDetailVO> detailVOMap = tAdvanceDeliveryDetailVOS.stream().collect(Collectors.toMap(TAdvanceDeliveryDetailVO::getId, Function.identity()));
            List<TRejectionDetail> saveRejectionList = new ArrayList<>();
            for (TAdvanceDeliveryDetail tAdvanceDeliveryDetail : deliveryDetailList) {
                TAdvanceDeliveryDetailVO oldDO = detailVOMap.get(tAdvanceDeliveryDetail.getId());
                if (oldDO == null) {
                    return AjaxResult.error("物料信息未获取到");
                }
                //判断数量
                BigDecimal oldDetectionCount = oldDO.getDetectionCount() == null ? BigDecimal.ZERO : oldDO.getDetectionCount();
                //总不合格数量
                BigDecimal detectionCountSum = tAdvanceDeliveryDetail.getDetectionCount();
                if (detectionCountSum.compareTo(oldDO.getPredictCount()) == 1) {
                    return AjaxResult.error(oldDO.getMaterialName() + "检测失败数量不可超出预计入库数量");
                }
                //本次不合格数量
                BigDecimal detectionCountNow = tAdvanceDeliveryDetail.getDetectionCount().subtract(oldDetectionCount);
                if (detectionCountNow.compareTo(BigDecimal.ZERO) != 0) {
                    //生成拒收
                    //当本次检测出来的不合格数量和上个不变时，不再生成拒收，有变化，则根据数量生成正负数量
                    TRejectionDetail tRejectionDetail = new TRejectionDetail();
                    tRejectionDetail.setMaterialId(oldDO.getMaterialId());//物料id
                    tRejectionDetail.setAdvanceDeliveryDetailId(tAdvanceDeliveryDetail.getId());//入库单详情id
                    tRejectionDetail.setAdvanceDeliveryId(oldDO.getAdvanceDeliveryId());//入库单id
                    tRejectionDetail.setRejectionCount(detectionCountNow);//拒收数量
                    tRejectionDetail.setRejectionFailType(tAdvanceDeliveryDetail.getDetectionFailType());//拒收类型
                    tRejectionDetail.setRejectionFailRemark(tAdvanceDeliveryDetail.getDetectionFailRemark());//拒收备注
                    tRejectionDetail.setBatchCode(oldDO.getBatchCode());//批次号
                    tRejectionDetail.setRemark(oldDO.getRemark());//备注
                    saveRejectionList.add(tRejectionDetail);
                }
            }
            advanceDeliveryDetailService.updateBatchById(deliveryDetailList);
            //生成拒收管理
            rejectionDetailService.saveRejectionList(saveRejectionList);
        }
        return AjaxResult.success();
    }

    @Override
    public List<TAdvanceDeliveryDetailVO> getDeatilListByIds(Long[] ids) {
        List<TAdvanceDeliveryDetailVO> tAdvanceDeliveryDetailVOS = advanceDeliveryDetailService.selectDetailListByDeliveryId(null, ids);
        return tAdvanceDeliveryDetailVOS;
    }

    /**
     * pda登记
     *
     * @param advanceDeliveryList
     * @return
     */
    @Override
    @Transactional
    public AjaxResult registerCount(List<TAdvanceDeliveryApiDTO> advanceDeliveryList) {
        for (TAdvanceDeliveryApiDTO tAdvanceDeliveryApiDTO : advanceDeliveryList) {
            BigDecimal predictCount = tAdvanceDeliveryApiDTO.getCount();//入库数量
            List<TAdvanceDeliveryDetailVO> detailByMaterial = tAdvanceDeliveryDetailMapper.getDetailByMaterial(tAdvanceDeliveryApiDTO.getMaterialId(), tAdvanceDeliveryApiDTO.getBatchCode());
            if (!CollectionUtils.isEmpty(detailByMaterial)) {
                BigDecimal predictCountSum = detailByMaterial.stream().map(TAdvanceDeliveryDetail::getPredictCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal receiveCountSum = detailByMaterial.stream().map(TAdvanceDeliveryDetailVO::getReceiveCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal residueCountSum = predictCountSum.subtract(receiveCountSum);
                if (tAdvanceDeliveryApiDTO.getCount().compareTo(residueCountSum) == 1) {
                    throw new ServiceException("超出可登记数量，不可登记");
                }
                for (TAdvanceDeliveryDetailVO e : detailByMaterial) {
                    if (predictCount.compareTo(BigDecimal.ZERO) != 1 ) {
                        break;
                    }
                    TAdvanceDeliveryDetail tAdvanceDeliveryDetail = new TAdvanceDeliveryDetailVO();
                    tAdvanceDeliveryDetail.setId(e.getId());
                    BigDecimal oldpredictCount = e.getPredictCount();//原预计数量
                    BigDecimal oldReceiveCount = e.getReceiveCount();//原实收数量
                    BigDecimal residueCount = oldpredictCount.subtract(oldReceiveCount);//现单剩余数量
                    if (predictCount.compareTo(residueCount) != -1) {//全部登记
                        tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_END);
                        tAdvanceDeliveryDetail.setReceiveCount(oldpredictCount);
                        tAdvanceDeliveryDetail.setRegistrationCount(oldpredictCount);
                        predictCount = predictCount.subtract(residueCount);
                    } else {//部分登记
                        tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_PART);
                        tAdvanceDeliveryDetail.setReceiveCount(oldReceiveCount.add(predictCount));
                        tAdvanceDeliveryDetail.setRegistrationCount(predictCount);
                        predictCount = BigDecimal.ZERO;
                    }
                    advanceDeliveryDetailService.updateById(tAdvanceDeliveryDetail);

                    //生成登记任务
                    TAdvanceRegistration registration = new TAdvanceRegistration();
                    registration.setAdvanceDeliveryId(e.getAdvanceDeliveryId());
                    registration.setAdvanceDeliveryDetailId(e.getId());
                    registration.setMaterialId(e.getMaterialId());
                    registration.setBatchCode(e.getBatchCode());
                    registration.setNextFlag(Constants.NO);
                    registration.setPredictCount(tAdvanceDeliveryDetail.getRegistrationCount());
                    registration.setActualCount(BigDecimal.ZERO);
                    registration.setStatus(Constants.INOUT_STATUS_NOT);
                    advanceRegistrationService.save(registration);

                    //查询入库单详情状态，更改主表状态
                    QueryWrapper<TAdvanceDeliveryDetail> detailQw = new QueryWrapper<>();
                    detailQw.eq("del_flag", Constants.DEL_FLAG_NO);
                    detailQw.eq("advance_delivery_id", e.getAdvanceDeliveryId());
                    detailQw.eq("next_flag", Constants.INOUT_STATUS_PART);//存在部分登记数据
                    long partCount = advanceDeliveryDetailService.count(detailQw);
                    TAdvanceDelivery tAdvanceDelivery = new TAdvanceDelivery();
                    tAdvanceDelivery.setId(e.getAdvanceDeliveryId());
                    tAdvanceDelivery.setStatus(partCount > 0 ? Constants.INOUT_STATUS_REGISTER_PART : Constants.INOUT_STATUS_REGISTER);
                    tAdvanceDeliveryMapper.updateById(tAdvanceDelivery);
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * pda登记-新
     *
     * @param advanceDeliveryList
     * @return
     */
    @Override
    @Transactional
    public AjaxResult registerCountNew(List<TAdvanceDeliveryApiDTO> advanceDeliveryList) {
        for (TAdvanceDeliveryApiDTO tAdvanceDeliveryApiDTO : advanceDeliveryList) {
            BigDecimal predictCount = tAdvanceDeliveryApiDTO.getCount();//入库数量
            List<TAdvanceDeliveryDetailVO> detailByMaterial = tAdvanceDeliveryDetailMapper.getDetailByMaterial(tAdvanceDeliveryApiDTO.getMaterialId(), tAdvanceDeliveryApiDTO.getBatchCode());
            if (!CollectionUtils.isEmpty(detailByMaterial)) {
                BigDecimal predictCountSum = detailByMaterial.stream().map(TAdvanceDeliveryDetailVO::getPredictCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal receiveCountSum = detailByMaterial.stream().map(TAdvanceDeliveryDetailVO::getReceiveCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal residueCountSum = predictCountSum.subtract(receiveCountSum);
                if (tAdvanceDeliveryApiDTO.getCount().compareTo(residueCountSum) == 1) {
                    throw new ServiceException("超出可登记数量，不可登记");
                }
                for (TAdvanceDeliveryDetailVO e : detailByMaterial) {
                    if (predictCount.compareTo(BigDecimal.ZERO) != 1) {
                        break;
                    }
                    TAdvanceDeliveryDetail tAdvanceDeliveryDetail = new TAdvanceDeliveryDetailVO();
                    tAdvanceDeliveryDetail.setId(e.getId());
                    BigDecimal oldpredictCount = e.getPredictCount();//原预计数量
                    BigDecimal oldReceiveCount = e.getReceiveCount();//原实收数量
                    BigDecimal residueCount = oldpredictCount.subtract(oldReceiveCount);//现单剩余数量
                    if (predictCount.compareTo(residueCount) != -1) {//全部登记
//                        tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_END);
//                        tAdvanceDeliveryDetail.setReceiveCount(oldpredictCount);
                        tAdvanceDeliveryDetail.setRegistrationCount(oldpredictCount);
                        predictCount = predictCount.subtract(residueCount);
                    } else {//部分登记
//                        tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_STATUS_PART);
//                        tAdvanceDeliveryDetail.setReceiveCount(oldReceiveCount + predictCount);
                        tAdvanceDeliveryDetail.setRegistrationCount(predictCount);
                        predictCount = BigDecimal.ZERO;
                    }
                    advanceDeliveryDetailService.updateById(tAdvanceDeliveryDetail);

                    //查询入库单详情状态，更改主表状态
                    String status = getDetailCountStatus(e.getAdvanceDeliveryId(), 2);
                    if (StringUtils.isNotEmpty(status)) {
                        TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
                        updateDelivery.setId(e.getAdvanceDeliveryId());
                        updateDelivery.setStatus(status);
                        tAdvanceDeliveryMapper.updateById(updateDelivery);
                    }
                }
            }
        }
        return AjaxResult.success();
    }

    @Override
    public List<TMaterialDetailApiVO> getMaterialCountList() {
        return tAdvanceDeliveryDetailMapper.getMaterialCountList();
    }

    /**
     * 检测完成
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult checkDeliveryMaterial(TAdvanceDelivery tAdvanceDelivery) {
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null || (!Constants.INOUT_STATUS_REGISTER.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可检测");
        }
        //todo 查询是否全部检测失败
        //更新物料详情的检测状态
        List<TAdvanceDeliveryDetail> listByDeliveryIds = advanceDeliveryDetailService.getListByDeliveryId(tAdvanceDelivery.getId());
        if (CollectionUtils.isEmpty(listByDeliveryIds)) {
            return AjaxResult.error("未查询到对应的检测信息");
        }
        List<TAdvanceDeliveryDetail> detailUpdateList = new ArrayList<>();
        listByDeliveryIds.forEach(e -> {
            //更新入库单详情的检测数量
            TAdvanceDeliveryDetail updateDeliveyDetail = new TAdvanceDeliveryDetail();
            updateDeliveyDetail.setId(e.getId());
            updateDeliveyDetail.setDetectionCount(e.getPredictCount());
            updateDeliveyDetail.setNextFlag(Constants.INOUT_NEXTFLAG_CHECKED);
            detailUpdateList.add(updateDeliveyDetail);
        });

        advanceDeliveryDetailService.updateBatchById(detailUpdateList);
        //更新入库单检测状态
        TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
        updateDelivery.setId(oldDelivery.getId());
        updateDelivery.setStatus(Constants.INOUT_STATUS_CHECKED);
        tAdvanceDeliveryMapper.updateById(updateDelivery);
        return AjaxResult.success();
    }

    /**
     * 入库单登记-new
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult registerDelivery(TAdvanceDeliveryDTO tAdvanceDelivery) {
        List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDelivery.getDeliveryDetailList();
        if (CollectionUtils.isEmpty(deliveryDetailList)) {
            return AjaxResult.error("物料列表不可为空");
        }
        //检验入库单状态和数据
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null
                || (!Constants.INOUT_STATUS_PASS.equals(oldDelivery.getStatus()) &&
                !Constants.INOUT_STATUS_REGISTER_PART.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可登记");
        }

        //xwk-齐套入库没有检测，所以直接设置检测数量
        if (Constants.INOUT_DELIVERY_MODULE_COMPLETE.equals(tAdvanceDelivery.getDeliveryModule())) {
            deliveryDetailList.forEach(e -> e.setDetectionCount(e.getRegistrationCount()));
        }
        //批量更新子表登记数量
        deliveryDetailList.forEach(e -> {
            TAdvanceDeliveryDetail detail = advanceDeliveryDetailService.getById(e.getId());
            if (detail.getRegistrationCount().compareTo(BigDecimal.ZERO) != 0){
               e.setRegistrationCount(detail.getRegistrationCount().add(e.getRegistrationCount()));
            }

            if (e.getRegistrationCount().compareTo(detail.getPredictCount()) == 1){
                throw new ServiceException("登记数量不可大于预计数量");
            }
        });

        advanceDeliveryDetailService.updateBatchById(deliveryDetailList);
        //更新入库单检测状态
        String status = getDetailCountStatus(oldDelivery.getId(), 2);
        if (StringUtils.isNotEmpty(status)) {
            TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
            updateDelivery.setId(oldDelivery.getId());
            updateDelivery.setStatus(status);
            updateDelivery.setPredictDate(new Date());
            if (Constants.INOUT_STATUS_REGISTER.equals(status)) {
                generateOtherMaterialDetail(oldDelivery.getId());
                if (Constants.INOUT_DELIVERY_MODULE_COMPLETE.equals(tAdvanceDelivery.getDeliveryModule())) {
                    //xwk-齐套入库直接设置为已检测完成
                    updateDelivery.setStatus(Constants.INOUT_STATUS_CHECKED);
                }
            }
            tAdvanceDeliveryMapper.updateById(updateDelivery);
        }
        return AjaxResult.success();
    }


    /**
     * 生成超收的物料详情
     */
    public void generateOtherMaterialDetail(Long deliveryId) {
        //查询超收配置规则启用状态
        String ruleStatus = ruleService.getStatusByMoule(Constants.RULE_MODULE_OVERCHARGE);
        //物料详情表
        List<TMaterialDetail> saveMaterialList = new ArrayList<>();
        //查询预约单详情列表
        List<TAdvanceDeliveryDetail> deliveryDetailList = advanceDeliveryDetailService.getListByDeliveryId(deliveryId);
        //获取物料编码
        List<Long> materialIds = deliveryDetailList.stream().map(TAdvanceDeliveryDetail::getMaterialId).collect(Collectors.toList());
        Map<Long, TMaterial> materialCodeMap = materialService.getCodeByIds(materialIds);
        //获取物料小件数量
        Map<Long, Long> unitCountMap = unitConfigService.getUnitCount(materialIds);
        //生成物料详情
        for (TAdvanceDeliveryDetail detail : deliveryDetailList) {
            BigDecimal diffCount = detail.getRegistrationCount().subtract(detail.getPredictCount());
            if (diffCount.compareTo(BigDecimal.ZERO) == 1) {
                if (Constants.NO.equals(ruleStatus)) {
                    throw new ServiceException("收货数量不可超出预计数量");
                } else {
                    //生成物料详情
//                    for (int i = 0; i < diffCount; i++) {
//                        TMaterial tMaterialVO = materialCodeMap.get(detail.getMaterialId());
//                        saveMaterialList.add(buildMaterialDetailByDelivery(detail, tMaterialVO, unitCountMap));
//                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(saveMaterialList)) {
            materialDetailService.saveBatch(saveMaterialList);
        }
    }

    /**
     * 物料详情DO数据转换
     *
     * @param detail
     * @param tMaterialVO
     * @return
     */
    public TMaterialDetail buildMaterialDetailByDelivery(TAdvanceDeliveryDetail detail, TMaterial tMaterialVO, Map<Long, Long> unitCountMap) {
        TMaterialDetail tMaterialDetail = new TMaterialDetail();
        tMaterialDetail.setCode(codeConfigService.getCode(CodeEnum.IWLD.getCodeName()));
        tMaterialDetail.setMaterialId(detail.getMaterialId());
        tMaterialDetail.setBatchCode(detail.getBatchCode());
        tMaterialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_NO);
        tMaterialDetail.setSmallUnitCount(unitCountMap.get(detail.getMaterialId()));
        if (tMaterialVO != null) {
            tMaterialDetail.setRfid(codeConfigService.getRfIdCode(tMaterialVO.getCode(), detail.getRfidBatchCode()));
            tMaterialDetail.setMaterialCode(tMaterialVO.getCode());
            tMaterialDetail.setMaterialName(tMaterialVO.getName());
            tMaterialDetail.setPrice(tMaterialVO.getUnitPrice());
            tMaterialDetail.setWeight(tMaterialVO.getRoughWeight());
            //redis存放
            TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
            tMaterialDetailVO.setBatchCode(detail.getBatchCode());
            tMaterialDetailVO.setMaterialId(tMaterialVO.getId());
            tMaterialDetailVO.setMaterialName(tMaterialVO.getName());
            tMaterialDetailVO.setRfid(tMaterialDetail.getRfid());
//            redisService.setCacheObject("wms:materialDetail:" + tMaterialDetail.getRfid(), tMaterialDetailVO);
            tMaterialDetail.setAdvanceRegistrationId(detail.getId());
        }
        return tMaterialDetail;
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
        List<TAdvanceDeliveryDetail> tAdvanceDeliveryDetails = advanceDeliveryDetailService.getListByDeliveryId(deliveryId);
        if (CollectionUtils.isNotEmpty(tAdvanceDeliveryDetails)) {
            if (type == 1) {
                List<TAdvanceDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getRegistrationCount().equals(e.getDetectionCount())).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.INOUT_STATUS_CHECKED;
                } else {
                    status = Constants.INOUT_STATUS_CHECKED;
                }
            } else if (type == 2) {
                List<TAdvanceDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getPredictCount().compareTo(e.getRegistrationCount()) != 1).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.INOUT_STATUS_REGISTER;
                } else {
                    status = Constants.INOUT_STATUS_REGISTER_PART;
                }
            } else if (type == 3) {
                List<TAdvanceDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getDetectionCount().equals(e.getReceiveCount())).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.INOUT_STATUS_COMPLETE_END;
                } else {
                    status = Constants.INOUT_STATUS_COMPLETE_PART;
                }
            } else {//快捷入库
                List<TAdvanceDeliveryDetail> endCount = tAdvanceDeliveryDetails.stream().filter(e -> e.getPredictCount().equals(e.getReceiveCount())).collect(Collectors.toList());
                if (tAdvanceDeliveryDetails.size() == endCount.size()) {
                    status = Constants.INOUT_STATUS_COMPLETE_END;
                } else {
                    status = Constants.INOUT_STATUS_COMPLETE_PART;
                }
            }
        }
        return status;
    }

    /**
     * 入库单作废
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult cancellation(TAdvanceDelivery tAdvanceDelivery) {
        TAdvanceDelivery oldDelivery = tAdvanceDeliveryMapper.selectById(tAdvanceDelivery.getId());
        if (oldDelivery == null || (!Constants.INOUT_STATUS_WAITING.equals(oldDelivery.getStatus()))) {
            return AjaxResult.error("当前状态不可作废");
        }
        TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
        updateDelivery.setId(oldDelivery.getId());
        updateDelivery.setStatus(Constants.INOUT_STATUS_FAILED);
        tAdvanceDeliveryMapper.updateById(updateDelivery);
        //删除物料详情表
        Long[] ids = new Long[1];
        ids[0] = tAdvanceDelivery.getId();
        materialDetailService.deleteTMaterialDetailByDeliveryIds(ids);
        return AjaxResult.success();
    }

    /**
     * bom补料
     *
     * @param tBomDetail
     * @return
     */
    @Override
    public AjaxResult bomAdd(TBomDetail tBomDetail) {
        List<TMaterialVO> list = tBomDetailMapper.selectTBomDetailStockList(null, tBomDetail.getId());
        if (CollectionUtils.isNotEmpty(list)) {
            TMaterialVO tMaterialVO = list.get(0);
            Long redusicCount = tMaterialVO.getPredictCount() - tMaterialVO.getStockMin();
            if (redusicCount > 0) {
                TAdvanceDeliveryDTO tAdvanceDelivery = new TAdvanceDeliveryDTO();
                tAdvanceDelivery.setType("6");
                tAdvanceDelivery.setNewLocal(Constants.DELIVERY_IN_TYPE_BOM);
                tAdvanceDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
                List<TAdvanceDeliveryDetail> deliveryDetailList = new ArrayList<>();
                TAdvanceDeliveryDetail tAdvanceDeliveryDetail = new TAdvanceDeliveryDetail();
                tAdvanceDeliveryDetail.setPredictCount(new BigDecimal(redusicCount));
                tAdvanceDeliveryDetail.setMaterialId(tMaterialVO.getId());
                tAdvanceDeliveryDetail.setBatchCode(codeConfigService.getBatchCode1(tMaterialVO.getId(), DateUtils.getNowDate()));
                deliveryDetailList.add(tAdvanceDeliveryDetail);
                tAdvanceDelivery.setDeliveryDetailList(deliveryDetailList);
                int count = insertTAdvanceDelivery(tAdvanceDelivery);
                if (count > 0) {
                    return AjaxResult.success();
                }
            }
        }
        return AjaxResult.error();
    }

    /**
     * 根据单据id更新调拨状态
     *
     * @param id
     * @return
     */
    @Override
    public int updateAllotByDeliveryId(Long id) {
        TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(id);
        if (Constants.INOUT_STATUS_COMPLETE_END.equals(tAdvanceDelivery.getStatus())
                && Constants.DELIVERY_IN_TYPE_ALLOT.equals(tAdvanceDelivery.getNewLocal())) {
            //在全部上架和类型为调拨单的状态下，更新
            tAllotMapper.updateStatusByCode(Constants.ALLOT_STATUS_SUCCESS, tAdvanceDelivery.getOriginCode());
        }
        return 0;
    }

    /**
     * 根据单据号删除单据
     *
     * @param code
     * @return
     */
    @Override
    public AjaxResult deleteByCode(String code) {
        TAdvanceDelivery deliveryVO = this.getByCode(code);
        if (deliveryVO == null) {
            return AjaxResult.error("未查询到数据信息");
        }
        //查询单据详情
        QueryWrapper<TAdvanceDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("advance_delivery_id", deliveryVO.getId());
        List<TAdvanceDeliveryDetail> tAdvanceDeliveryDetails = tAdvanceDeliveryDetailMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(tAdvanceDeliveryDetails)) {
            //判断单据详情状态
            for (TAdvanceDeliveryDetail tAdvanceDeliveryDetail : tAdvanceDeliveryDetails) {
                if (tAdvanceDeliveryDetail.getPutawayCount().compareTo(BigDecimal.ZERO) == 1) {
                    return AjaxResult.error("所选入库单已组过盘不可删除");
                }
            }
            //单据详情ids
            List<Long> deliveryDetails = tAdvanceDeliveryDetails.stream().map(TAdvanceDeliveryDetail::getId).collect(Collectors.toList());
            Long[] detailIds = deliveryDetails.toArray(new Long[deliveryDetails.size()]);
            //删除物料详情以及redis值
            QueryWrapper<TMaterialDetail> detailWrapper = new QueryWrapper<>();
            detailWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
            detailWrapper.in("advance_registration_id", detailIds);
            List<TMaterialDetail> tMaterialDetails = materialDetailService.list(detailWrapper);
            if (CollectionUtils.isNotEmpty(tMaterialDetails)) {
                List<String> rfidHeads = tMaterialDetails.stream().filter(e -> StringUtils.isNotEmpty(e.getRfidHead())).map(TMaterialDetail::getRfidHead).distinct().collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(rfidHeads)) {
                    for (String rfidHead : rfidHeads) {
                        redisService.deleteObject("wms:materialDetail:" + rfidHead);
                    }
                }
                materialDetailService.deleteTMaterialDetailByDeliveryIds(detailIds);
            }
            //删除打印配置
            tMaterialDetailPrintMapper.deletePrintByRegisterIds(detailIds);
            //删除子表信息
            advanceDeliveryDetailService.deleteTAdvanceDeliveryDetailByIds(detailIds);
            //删除主表
            tAdvanceDeliveryMapper.deleteTAdvanceDeliveryById(deliveryVO.getId());
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 通过code查询单据信息
     *
     * @param code
     * @return
     */
    public TAdvanceDelivery getByCode(String code) {
        QueryWrapper<TAdvanceDelivery> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("code", code);
        queryWrapper.last("limit 1");
        return tAdvanceDeliveryMapper.selectOne(queryWrapper);
    }
}
