package com.xsrw.wms.inout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.print.ZplPrint;
import com.xsrw.common.core.print.ZplUtils;
import com.xsrw.common.core.text.Convert;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.api.domain.dto.TAdvanceMaterialApiDTO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TRejectionDetail;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailMonthlyCountVo;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.inout.service.ITRejectionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 物料入库详情Service业务层处理
 *
 * @author wxr
 * @date 2023-05-11
 */
@Service
public class TMaterialDetailServiceImpl extends ServiceImpl<TMaterialDetailMapper, TMaterialDetail> implements ITMaterialDetailService {
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;
    @Autowired
    private TAdvanceDeliveryMapper tAdvanceDeliveryMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TMaterialMapper materialMapper;

    @Autowired
    private TUnitMapper unitMapper;
    @Autowired
    private ITRejectionDetailService rejectionDetailService;

    /**
     * 查询物料入库详情列表
     *
     * @param tMaterialDetail 物料入库详情
     * @return 物料入库详情
     */
    @Override
    public List<TMaterialDetailVO> selectTMaterialDetailList(TMaterialDetailSerachDTO tMaterialDetail) {
        return tMaterialDetailMapper.selectTMaterialDetailList(tMaterialDetail);
    }

    /**
     * 查询物料入库详情
     *
     * @param id 物料入库详情主键
     * @return 物料入库详情
     */
    @Override
    public TMaterialDetail selectTMaterialDetailById(Long id) {
        return tMaterialDetailMapper.selectById(id);
    }

    /**
     * 新增物料入库详情
     *
     * @param tMaterialDetail 物料入库详情
     * @return 结果
     */
    @Override
    public int insertTMaterialDetail(TMaterialDetail tMaterialDetail) {
        return tMaterialDetailMapper.insert(tMaterialDetail);
    }

    /**
     * 修改物料入库详情
     *
     * @param tMaterialDetail 物料入库详情
     * @return 结果
     */
    @Override
    public int updateTMaterialDetail(TMaterialDetail tMaterialDetail) {
        //redis存放
//        TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
//        tMaterialDetailVO.setBatchCode(tMaterialDetail.getBatchCode());
//        tMaterialDetailVO.setMaterialId(tMaterialDetail.getMaterialId());
//        tMaterialDetailVO.setMaterialName(tMaterialDetail.getMaterialName());
//        tMaterialDetailVO.setRfid(tMaterialDetail.getRfid());
//        redisService.setCacheObject("wms:materialDetail:" + tMaterialDetail.getRfid(), tMaterialDetailVO);

        return tMaterialDetailMapper.updateById(tMaterialDetail);
    }


    /**
     * 批量删除物料入库详情
     *
     * @param ids 需要删除的物料入库详情主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialDetailByIds(Long[] ids) {
        return tMaterialDetailMapper.deleteTMaterialDetailByIds(ids);
    }

    /**
     * 删除物料入库详情信息
     *
     * @param id 物料入库详情主键
     * @return 结果
     */
    @Override
    public int deleteTMaterialDetailById(Long id) {
        return tMaterialDetailMapper.deleteTMaterialDetailById(id);
    }

    /**
     * 根据入库单删除物料详情列表
     *
     * @param deliverIds
     * @return
     */
    @Override
    public int deleteTMaterialDetailByDeliveryIds(Long[] deliverIds) {
        QueryWrapper<TAdvanceDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("advance_delivery_id", deliverIds);
        List<TAdvanceDeliveryDetail> tAdvanceDeliveryDetails = tAdvanceDeliveryDetailMapper.selectList(queryWrapper);
        List<Long> deliveryDetails = tAdvanceDeliveryDetails.stream().map(TAdvanceDeliveryDetail::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deliveryDetails)) {
            //删除物料详情redis值
//            QueryWrapper<TMaterialDetail> detailWrapper = new QueryWrapper<>();
//            detailWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
//            detailWrapper.in("advance_registration_id", deliveryDetails);
//            List<TMaterialDetail> tMaterialDetails = tMaterialDetailMapper.selectList(detailWrapper);
//            if (CollectionUtils.isNotEmpty(tMaterialDetails)) {
//                List<String> rfids = tMaterialDetails.stream().map(TMaterialDetail::getRfid).collect(Collectors.toList());
//                for (String rfid : rfids) {
//                    redisService.deleteObject("wms:materialDetail:" + rfid);
//                }
//            }
            return tMaterialDetailMapper.deleteTMaterialDetailByDeliveryIds(deliveryDetails);
        }
        return 0;
    }

    /**
     * 查询物料详情信息
     * @param tMaterialDetail
     * @return
     */
    @Override
    public List<TMaterialDetailVO> selectTMaterialDetailAllList(TMaterialDetail tMaterialDetail) {
        return tMaterialDetailMapper.selectTMaterialDetailAllList(tMaterialDetail);
    }

    /**
     * 库存物资月报表查询
     *
     * @param monthlyCountVo
     * @return
     */
    @Override
    public List<TMaterialDetailMonthlyCountVo> materialDetailMonthlyCountList(TMaterialDetailMonthlyCountVo monthlyCountVo) {
        List<TMaterialDetailMonthlyCountVo> monthlyCountList = tMaterialDetailMapper.materialDetailMonthlyCountList(monthlyCountVo);
        if (StringUtils.isNotEmpty(monthlyCountList) && monthlyCountList.size() > 0) {
            monthlyCountList.forEach(e -> {
                List<TMaterialDetail> materialDetails = tMaterialDetailMapper.selectList(Wrappers.lambdaQuery(TMaterialDetail.class)
                        .eq(TMaterialDetail::getBatchCode, e.getBatchCode()));
                if(CollectionUtils.isNotEmpty(materialDetails)){
                    TMaterialDetail materialDetail = materialDetails.get(0);
                    //入库时间
                    e.setCreateTime(materialDetail.getCreateTime());
                    //月份
                    String dateTime = DateUtils.parseDateTime(e.getCreateTime());
                    dateTime = dateTime.replace("-", "年");
                    e.setMonthly(dateTime + "月");
                    //库龄
                    String timeDistance = DateUtils.timeDistance(materialDetail.getCreateTime());
                    e.setStockAge(timeDistance);
                }
            });
        }
        return monthlyCountList;
    }

    /**
     * 更根据入库详情标识更新物料详情的检测状态
     *
     * @param detailId
     * @return
     */
    @Override
    public int updateStatusByDeliveryId(Long detailId) {
        UpdateWrapper<TMaterialDetail> uw = new UpdateWrapper();
        uw.eq("advance_registration_id", detailId);
        uw.eq("del_flag", Constants.DEL_FLAG_NO);
        uw.eq("detection_fail_status", Constants.MATERIAL_DETAIL_CHECK_NO);
        TMaterialDetail tMaterialDetail = new TMaterialDetail();
        tMaterialDetail.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_SUCESS);
        return tMaterialDetailMapper.update(tMaterialDetail, uw);
    }

    /**
     * 入库单检测失败
     *
     * @param tMaterialDetail
     * @return
     */
    @Override
    @Transactional
    public AjaxResult checkMaterial(List<TMaterialDetail> tMaterialDetail) {
        List<Long> ids = tMaterialDetail.stream().map(TMaterialDetail::getId).collect(Collectors.toList());
        List<TMaterialDetailVO> tMaterialDetailVOList = tMaterialDetailMapper.selectTMaterialDetailInfoByIds(ids, null);
        if (CollectionUtils.isEmpty(tMaterialDetailVOList)) {
            return AjaxResult.error("未查询可检测信息");
        }
        List<TMaterialDetail> materialDetailList = new ArrayList<>();
        List<TRejectionDetail> rejectionDetailList = new ArrayList<>();
        Set<Long> deliveryDetailIds = new HashSet<>();

        Map<Long, TMaterialDetailVO> detailVOMap = tMaterialDetailVOList.stream().collect(Collectors.toMap(TMaterialDetailVO::getId, Function.identity()));
        tMaterialDetail.forEach(e -> {
            TMaterialDetailVO tMaterialDetailVO = detailVOMap.get(e.getId());
            if (tMaterialDetailVO != null) {
                //生成拒收
                TRejectionDetail tRejectionDetail = new TRejectionDetail();
                tRejectionDetail.setMaterialId(tMaterialDetailVO.getMaterialId());//物料id
                tRejectionDetail.setAdvanceDeliveryDetailId(tMaterialDetailVO.getAdvanceRegistrationId());//入库单详情id
                tRejectionDetail.setAdvanceDeliveryId(tMaterialDetailVO.getAdvanceDeliveryId());//入库单id
                tRejectionDetail.setRejectionCount(1L);//拒收数量
                tRejectionDetail.setRejectionFailType(e.getDetectionFailType());//拒收类型
                tRejectionDetail.setRejectionFailRemark(e.getDetectionFailRemark());//拒收备注
                tRejectionDetail.setBatchCode(tMaterialDetailVO.getBatchCode());//批次号
                tRejectionDetail.setRemark(e.getDetectionFailRemark());//备注
                rejectionDetailList.add(tRejectionDetail);
                deliveryDetailIds.add(tMaterialDetailVO.getAdvanceRegistrationId());
                //物料详情表
                TMaterialDetail updateDO = new TMaterialDetail();
                updateDO.setId(e.getId());
                updateDO.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_FAIL);
                updateDO.setDetectionFailRemark(e.getDetectionFailRemark());
                updateDO.setDetectionFailType(e.getDetectionFailType());
                materialDetailList.add(updateDO);
            }
        });
        //拒收数据
        if (CollectionUtils.isNotEmpty(rejectionDetailList)) {
            rejectionDetailService.saveBatch(rejectionDetailList);
        }
        //更新检测状态
        if (CollectionUtils.isNotEmpty(materialDetailList)) {
            this.updateBatchById(materialDetailList);
            this.updateRedisMaterial(deliveryDetailIds);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @Override
    public AjaxResult checkMaterialByDelivery(TAdvanceMaterialApiDTO materialApiDTO) {
        TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(materialApiDTO.getId());
        if (tAdvanceDelivery == null) {
            return AjaxResult.error("未查询到对应入库单信息");
        }
        if (!Constants.INOUT_STATUS_REGISTER.equals(tAdvanceDelivery.getStatus())) {
            return AjaxResult.error("当前状态不可监测");
        }
        List<TMaterialDetailVO> rfIdInfoList = tMaterialDetailMapper.selectTMaterialDetailInfoByIds(null, materialApiDTO.getId());
        if (CollectionUtils.isEmpty(rfIdInfoList)) {
            return AjaxResult.error("未查询可检测信息");
        }
        List<TMaterialDetail> materialDetailList = new ArrayList<>();
        List<TRejectionDetail> rejectionDetailList = new ArrayList<>();
        Set<Long> deliveryDetailIds = new HashSet<>();
        Map<String, TMaterialDetailVO> detailVOMap = rfIdInfoList.stream().collect(Collectors.toMap(TMaterialDetailVO::getRfid, Function.identity()));
        materialApiDTO.gettMaterialDetailList().forEach(e -> {
            TMaterialDetailVO tMaterialDetailVO = detailVOMap.get(e.getRfid());
            if (tMaterialDetailVO != null) {
                //生成拒收
                TRejectionDetail tRejectionDetail = new TRejectionDetail();
                tRejectionDetail.setMaterialId(tMaterialDetailVO.getMaterialId());//物料id
                tRejectionDetail.setAdvanceDeliveryDetailId(tMaterialDetailVO.getAdvanceRegistrationId());//入库单详情id
                tRejectionDetail.setAdvanceDeliveryId(tMaterialDetailVO.getAdvanceDeliveryId());//入库单id
                tRejectionDetail.setRejectionCount(1L);//拒收数量
                tRejectionDetail.setRejectionFailType(e.getDetectionFailType());//拒收类型
                tRejectionDetail.setRejectionFailRemark(e.getDetectionFailRemark());//拒收备注
                tRejectionDetail.setBatchCode(tMaterialDetailVO.getBatchCode());//批次号
                tRejectionDetail.setRemark(e.getDetectionFailRemark());//备注
                rejectionDetailList.add(tRejectionDetail);
                deliveryDetailIds.add(tMaterialDetailVO.getAdvanceRegistrationId());
                //物料详情表
                TMaterialDetail updateDO = new TMaterialDetail();
                updateDO.setId(tMaterialDetailVO.getId());
                updateDO.setDetectionFailStatus(Constants.MATERIAL_DETAIL_CHECK_FAIL);
                updateDO.setDetectionFailRemark(e.getDetectionFailRemark());
                updateDO.setDetectionFailType(e.getDetectionFailType());
                materialDetailList.add(updateDO);
            }
        });
        //拒收数据
        if (CollectionUtils.isNotEmpty(rejectionDetailList)) {
            rejectionDetailService.saveBatch(rejectionDetailList);
        }
        //更新检测状态
        if (CollectionUtils.isNotEmpty(materialDetailList)) {
            this.updateBatchById(materialDetailList);
            this.updateRedisMaterial(deliveryDetailIds);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 更新redis
     * @param deliveryDetailIds
     */
    public void updateRedisMaterial(Set<Long> deliveryDetailIds){
        if(deliveryDetailIds.size() > 0){
            QueryWrapper<TMaterialDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
            queryWrapper.eq("status", Constants.MATERIAL_DETAIL_STATUS_ADD);
            queryWrapper.isNotNull("rfid_head");
            queryWrapper.in("advance_registration_id", deliveryDetailIds);
            List<TMaterialDetail> materialDetails = tMaterialDetailMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(materialDetails)) {
                //根据rfidHead分组
                Map<String, List<TMaterialDetail>> detailMap = materialDetails.stream().collect(Collectors.groupingBy(TMaterialDetail::getRfidHead));
                detailMap.forEach((key, value) -> {
                    //过滤已检测过的数据
                    List<TMaterialDetail> data = value.stream().filter(e -> !Constants.MATERIAL_DETAIL_CHECK_FAIL.equals(e.getDetectionFailStatus())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(data)) {
                        //更新redis
                        TMaterialDetail tMaterialDetail = value.get(0);
                        List<String> rfids = data.stream().map(TMaterialDetail::getRfid).collect(Collectors.toList());
                        //redis存放
                        TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                        tMaterialDetailVO.setBatchCode(tMaterialDetail.getBatchCode());
                        tMaterialDetailVO.setMaterialId(tMaterialDetail.getMaterialId());
                        tMaterialDetailVO.setMaterialName(tMaterialDetail.getMaterialName());
                        tMaterialDetailVO.setRfid(key);
                        tMaterialDetailVO.setCount(rfids.size());
                        tMaterialDetailVO.setRfids(rfids);
                        redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);
                    } else {
                        //如果全部检测失败，删除redis
                        redisService.deleteObject("wms:materialDetail:" + key);
                    }
                });
            }
        }
    }

    /**
     * 在库检测失败
     *
     * @param tMaterialDetail
     * @return
     */
    @Override
    public AjaxResult checkStockMaDetail(List<TMaterialDetail> tMaterialDetail) {
        this.updateBatchById(tMaterialDetail);
//        List<Long> ids = tMaterialDetail.stream().map(TMaterialDetail::getId).collect(Collectors.toList());
//        QueryWrapper<TMaterialDetail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
//        queryWrapper.in("id", ids);
//        List<TMaterialDetail> materialDetails = tMaterialDetailMapper.selectList(queryWrapper);
//        if(CollectionUtils.isNotEmpty(materialDetails)){
//            Set<Long> deliveryDetailIds = new HashSet<>();
//            for (TMaterialDetail materialDetail : materialDetails) {
//                deliveryDetailIds.add(materialDetail.getAdvanceRegistrationId());
//            }
//            this.updateRedisMaterial(deliveryDetailIds);
//        }
        return AjaxResult.success();
    }

    /**
     * 根据入库单查询物料详情总数
     *
     * @param deliveryId
     * @param status
     * @return
     */
    @Override
    public int getMaterialDetailByDelivery(Long deliveryId, String status) {
        return tMaterialDetailMapper.getMaterialDetailByDelivery(deliveryId, status);
    }

    /**
     * 打印rfid标签
     *
     * @param id
     * @return
     */
    @Override
    public AjaxResult printMaterialDetailById(Long id,String printFloor) {
        TMaterialDetailVO detail = tMaterialDetailMapper.selectInfoById(id);
        if (detail == null) {
            return AjaxResult.error("未查询到标签信息");
        }
        //打印机ip
        String printIp = "";
        // 一楼
        if (printFloor.equals("1")){
            printIp = Convert.toStr(redisService.getCacheObject(Constants.PRINT_IP_ONE_FLOOR), "");
        }
        //二楼
        if (printFloor.equals("2")){
            printIp = Convert.toStr(redisService.getCacheObject(Constants.PRINT_IP_TWO_FLOOR), "");
        }
        //打印机端口
        String printPort = Convert.toStr(redisService.getCacheObject(Constants.PRINT_PORT), "");
        ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), detail.getRfidHead(), detail.getBatchCode(), detail.getUnitName());
        try {
            ZplUtils.zplPrint(printIp, printPort, zplPrint);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("单个打印rfid错误:" + e.getMessage());
            return AjaxResult.error("打印出错");
        }
        return null;
    }

//    /**
//     * 根据入库单物料打印rfid
//     *
//     * @param advanceId
//     * @return
//     */
//    @Override
//    public AjaxResult printMaterialDetailByAdvanceId(Long advanceId) {
//        List<TMaterialDetailVO> materialDetails = tMaterialDetailMapper.selectMDetailByAdvanceDetailId(advanceId);
//        if (CollectionUtils.isEmpty(materialDetails)) {
//            return AjaxResult.error("无可打印信息");
//        }
//        //打印机ip
//        String printIp = Convert.toStr(redisService.getCacheObject(Constants.PRINT_IP), "");
//        //打印机端口
//        String printPort = Convert.toStr(redisService.getCacheObject(Constants.PRINT_PORT), "");
//        for (TMaterialDetailVO detail : materialDetails) {
//            ZplPrint zplPrint = new ZplPrint(detail.getDescription(), detail.getMaterialCode(), detail.getRfid(), detail.getBatchCode(), detail.getUnitName());
//            try {
//                ZplUtils.zplPrint(printIp, printPort, zplPrint);
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error("多个打印错误：" + e.getMessage());
//                return AjaxResult.error("打印出错");
//            }
//        }
//        return null;
//    }

}
