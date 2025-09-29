package com.xsrw.wms.inout.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TAdvanceDeliveryApiDTO;
import com.xsrw.wms.api.domain.dto.TAdvanceRegistrationApiDTO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.common.enums.TrayTypeEnum;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.service.*;
import com.xsrw.wms.inout.strategy.RecommendedLocationUtil;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.WcsMoveUtil;
import com.xsrw.wms.web.util.WcsReportUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库单详情Service业务层处理
 *
 * @author wxr
 * @date 2023-05-08
 */
@Service
public class TAdvanceDeliveryDetailServiceImpl extends ServiceImpl<TAdvanceDeliveryDetailMapper, TAdvanceDeliveryDetail> implements ITAdvanceDeliveryDetailService {
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;
    @Autowired
    private TTrayMapper tTrayMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;
    @Autowired
    private ITTaskInService taskInService;
    @Autowired
    private ITTrayService trayService;
    @Autowired
    private ITMaterialService materialService;
    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private RecommendedLocationUtil recommendedLocationUtil;
    @Autowired
    private ITTaskWcsService taskWcsService;
    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;
    @Autowired
    private ITLocationService locationService;
    @Autowired
    private ITStockMainService stockMainService;
    @Autowired
    private ITStockService stockService;
    @Autowired
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    @Lazy
    private ITAdvanceDeliveryService advanceDeliveryService;
    @Autowired
    private WcsReportUtil wcsReportUtil;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;

    @Autowired
    private ITRejectionDetailService rejectionDetailService;

    /**
     * 查询入库单详情列表
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 入库单详情
     */
    @Override
    public List<TAdvanceDeliveryDetailVO> selectTAdvanceDeliveryDetailList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        if (StringUtils.isEmpty(tAdvanceDeliveryDetail.getDeliveryModule())) {
            tAdvanceDeliveryDetail.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
        }
        return tAdvanceDeliveryDetailMapper.selectTAdvanceDeliveryDetailList(tAdvanceDeliveryDetail);
    }

    /**
     * 查询打印的入库单详情列表
     *
     * @param tAdvanceDeliveryDetail
     * @return
     */
    @Override
    public List<TAdvanceDeliveryDetailVO> selectPrintDetailList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        return tAdvanceDeliveryDetailMapper.selectPrintDetailList(tAdvanceDeliveryDetail);
    }

    /**
     * 查询入库单详情
     *
     * @param id 入库单详情主键
     * @return 入库单详情
     */
    @Override
    public TAdvanceDeliveryDetailVO selectTAdvanceDeliveryDetailById(Long id) {
        TAdvanceDeliveryDetailVO tAdvanceRegistrationVO = tAdvanceDeliveryDetailMapper.selectInfoById(id);
        tAdvanceRegistrationVO.setTaskInList(taskInService.selectTTaskInInfoByRegistrationId(id));
        return tAdvanceRegistrationVO;
    }

    /**
     * 新增入库单详情
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 结果
     */
    @Override
    public int insertTAdvanceDeliveryDetail(TAdvanceDeliveryDetail tAdvanceDeliveryDetail) {
        return tAdvanceDeliveryDetailMapper.insert(tAdvanceDeliveryDetail);
    }

    /**
     * 修改入库单详情
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 结果
     */
    @Override
    public int updateTAdvanceDeliveryDetail(TAdvanceDeliveryDetail tAdvanceDeliveryDetail) {
        return tAdvanceDeliveryDetailMapper.updateById(tAdvanceDeliveryDetail);
    }


    /**
     * 批量删除入库单详情
     *
     * @param ids 需要删除的入库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceDeliveryDetailByIds(Long[] ids) {
        return tAdvanceDeliveryDetailMapper.deleteTAdvanceDeliveryDetailByIds(ids);
    }

    /**
     * 删除入库单详情信息
     *
     * @param id 入库单详情主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceDeliveryDetailById(Long id) {
        return tAdvanceDeliveryDetailMapper.deleteTAdvanceDeliveryDetailById(id);
    }

    /**
     * 根据入库id获取详情列表
     *
     * @param deliveryId
     * @return
     */
    @Override
    public List<TAdvanceDeliveryDetailVO> selectDetailListByDeliveryId(Long deliveryId, Long[] deliveryIds) {
        if (deliveryId != null) {
            deliveryIds = new Long[1];
            deliveryIds[0] = deliveryId;
        }
        return tAdvanceDeliveryDetailMapper.selectDetailListByDeliveryId(deliveryIds);
    }

    /**
     * 根据入库id删除详情列表
     *
     * @param deliveryIds
     * @return
     */
    @Override
    public int deleteDetailByDeliveryIds(Long[] deliveryIds) {
        return tAdvanceDeliveryDetailMapper.deleteDetailByDeliveryIds(deliveryIds);
    }


    public List<TAdvanceDeliveryDetail> getListByDeliveryId(Long deliveryId) {
        QueryWrapper<TAdvanceDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("advance_delivery_id", deliveryId);
        return tAdvanceDeliveryDetailMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public synchronized AjaxResult putaway(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        //去除为0的数据
        List<TTaskIn> taskInList = tAdvanceDeliveryDetailDTO.getTaskInList();
        taskInList.forEach(e -> {
            e.setId(null);

            // 禁止rfid重复组盘
            List<String> rfIds = e.getRfIds();
            HashSet<String> hashSet = new HashSet<>(e.getRfIds());
            if (rfIds.size() != hashSet.size()){
                throw new ServiceException("rfid标签不可重复组盘，请检查数据");
            }
        });


        taskInList = taskInList.stream().filter(e -> e.getActualCount() != null && e.getActualCount().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskInList)) {
            return AjaxResult.error("上架数量不可为0");
        }
        //通过托盘ids获取对应的托盘信息
        List<Long> trays = taskInList.stream().map(TTaskIn::getTrayId).distinct().collect(Collectors.toList());
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.eq("task_type", Constants.TASK_TYPE_PUT);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.in("tray_id", trays);
        Long taskCount = taskWcsService.count(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行");
        }
        Map<Long, TTray> trayCodeMap = trayService.getTrayByIds(trays);
        //通过物料ids获取对应的物料信息
        List<Long> materials = taskInList.stream().map(TTaskIn::getMaterialId).distinct().collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
        //通过登记ids获取对应的登记信息
        List<Long> advanceRegistrationIds = taskInList.stream().map(TTaskIn::getAdvanceRegistrationId).distinct().collect(Collectors.toList());
        List<TAdvanceDeliveryDetail> advanceDeliveryDetailList = this.getListByIds(advanceRegistrationIds);
        if (CollectionUtils.isEmpty(advanceDeliveryDetailList)) {
            return AjaxResult.error("未查询到对应入库单");
        }
        //根据ids查询入库单未检测通过数量
        int count = tAdvanceDeliveryDetailMapper.getDeliveryNoCheckStatusByIds(advanceRegistrationIds);
        if (count > 0) {
            return AjaxResult.error("只有检测通过的入库单才可上架");
        }
        Map<Long, TAdvanceDeliveryDetail> registrationMap = advanceDeliveryDetailList.stream().collect(Collectors.toMap(TAdvanceDeliveryDetail::getId, Function.identity()));

        //更新登记信息列表
        List<TAdvanceDeliveryDetail> detailUpdateList = new ArrayList<>();
        //对每个登记数量进行判断
        Map<Long, List<TTaskIn>> registerGroupMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getAdvanceRegistrationId));
        registrationMap.forEach((key1, deliveryDetail) -> {
            List<TTaskIn> taskIns = registerGroupMap.get(key1);
            BigDecimal actualCount = taskIns.stream().map(TTaskIn::getActualCount).reduce(BigDecimal.ZERO,BigDecimal::add);//实际数量
            BigDecimal residueCount = deliveryDetail.getDetectionCount().subtract(deliveryDetail.getPutawayCount());//剩余数量
            if (actualCount.compareTo(residueCount) == 1 ) {
                throw new ServiceException("物料总上架数量不可超出预估上架数量");
            }
            if (taskIns.size() == 1 && deliveryDetail.getDetectionCount().compareTo(actualCount) == 0) {
                //判断是否全部上架，一个单据物料只有一条且实际上架数量=登记数量
                TTaskIn taskIn = taskIns.get(0);
                taskIn.setActualFlag(Constants.YES);
            }

            TAdvanceDeliveryDetail updatyeDO = new TAdvanceDeliveryDetail();
            updatyeDO.setId(key1);
            updatyeDO.setPutawayCount(actualCount.add(deliveryDetail.getPutawayCount()));
            if (Constants.INOUT_NEXTFLAG_CHECKED.equals(deliveryDetail.getNextFlag())) {
                //如果已部分执行，不更改数量
                updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_PUT);
            }
            detailUpdateList.add(updatyeDO);
        });

        //更新登记表转化状态
        this.updateBatchById(detailUpdateList);
        //保存taskIn表
        taskInService.saveBatch(taskInList);

        //库位更新
        List<TLocation> locationUpdateList = new ArrayList<>();
        //托盘状态更新
        List<TTray> trayUpdateList = new ArrayList<>();
        //任务详情列表
        List<TTaskWcsDetail> saveDetailList = new ArrayList<>();
        //推荐库位，排除已有库位
        List<Long> removeLocations = new ArrayList<>();
        //更新taskin
        List<TTaskIn> updatetTaskInList = new ArrayList<>();
        //发送命令
        List<WcsOrderDTO> wcsList = new ArrayList<>();
        //生成wcs任务
        Map<Long, List<TTaskIn>> trayInfoMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getTrayId));
        trayInfoMap.forEach((key, value) -> {
            List<TTaskIn> taskIns = value;
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_IN);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            tTaskWcs.setTrayId(key);
            TTray tTrayVO = trayCodeMap.get(key);
            if (tTrayVO == null) {
                throw new ServiceException("未获取到对应载具信息!");
            }
            if (tTrayVO.getLocationId() != null) {
                throw new ServiceException(tTrayVO.getCode() + "此载具已在库，请先出库!");
            }
            //生成上架任务，判断托盘承载重量
            final Double[] materialSum = {0d};
            Set<Long> categoryIds = new HashSet();
            List<Long> materialIds = new ArrayList<>();
            value.forEach(e -> {
                TMaterial tMaterialVO = materialMap.get(e.getMaterialId());
                if (tMaterialVO != null) {
                    if (tMaterialVO.getRoughWeight() != null) {
                        materialSum[0] = materialSum[0] + tMaterialVO.getRoughWeight();
                    }
                    Boolean trayStatus = TrayTypeEnum.compareTrayType(tTrayVO.getTrayCategory(), tMaterialVO.getMaterialLength(), tMaterialVO.getMaterialWidth(), tMaterialVO.getMaterialHeight());
                    if (!trayStatus) {
                        throw new ServiceException(tTrayVO.getCode() + "所选物料长宽高超出最大限制范围！");
                    }
                    categoryIds.add(tMaterialVO.getCategoryId());
                    materialIds.add(e.getMaterialId());
                }
            });
            if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
                throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
            }
            tTaskWcs.setTrayCode(tTrayVO.getCode());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcs.setMainTaskNo(tTaskWcs.getTaskNo());
            Long locationId = recommendedLocationUtil.recommendedLocation(removeLocations, tTrayVO.getId(), categoryIds, materialIds);
            if (locationId == null) {
                throw new ServiceException("无可用库位!");
            } else {
                removeLocations.add(locationId);
            }
            tTaskWcs.setLocationId(locationId);//推荐库位
            if (tTaskWcs.getLocationId() != null) {
                //更新库位【库位状态为标记入库】【托盘编号】
                TLocation tLocation = new TLocation();
                tLocation.setId(tTaskWcs.getLocationId());
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
                tLocation.setPalletNum(tTrayVO.getCode());
                locationUpdateList.add(tLocation);
            }
            taskWcsService.save(tTaskWcs);

            //如果是托盘，直接生成wcs任务
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                TLocation locationInfo = locationService.getById(locationId);
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                Integer mainSort = locationInfo.getExtentionType();
                //组装出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(tTaskWcs.getId(), tTaskWcs.getTaskNo(), WcsReportUtil.stationIn, endStation, locationInfo.getCode(), tTrayVO.getCode());
                orderDTO.setMainSort(mainSort);
                orderDTO.setMainTaskNo(tTaskWcs.getMainTaskNo());
                //组装移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(removeLocations, tTaskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    removeLocations.add(taskNoMove.getLocationId());
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    taskNoMove.setMainSort(mainSort);
                    wcsList.add(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getMoveTaskNo());
                }
                wcsList.add(orderDTO);
            }

            taskIns.forEach(e -> {
                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
                tTaskWcsDetail.setOriginId(e.getId());
                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
                saveDetailList.add(tTaskWcsDetail);
                TTaskIn taskInUpdate = new TTaskIn();
                taskInUpdate.setId(e.getId());
                taskInUpdate.setLocationId(locationId);
                updatetTaskInList.add(taskInUpdate);

                if (Constants.YES.equals(e.getActualFlag())) {
                    //全部上架,更新物料详情
                    tMaterialDetailMapper.updateInfoByDetailId(e.getAdvanceRegistrationId(), tTrayVO.getId(), locationId, Constants.MATERIAL_DETAIL_STATUS_IN_NO);
                } else {
                    //入库完成之后需要维护此表的库位和托盘等信息,更新状态为已组盘未入库
                    if (CollectionUtils.isNotEmpty(e.getRfIds())) {
                        tMaterialDetailMapper.updateInfoByRfIds(e.getRfIds(), tTrayVO.getId(), locationId, Constants.MATERIAL_DETAIL_STATUS_IN_NO);
                    }
                }
            });
            //托盘状态
            String trayStatus = taskIns.get(0).getTrayStatus();
            if (StringUtils.isNotBlank(trayStatus)) {
                TTray tray = new TTray();
                tray.setId(tTrayVO.getId());
                tray.setStatus(trayStatus);
                trayUpdateList.add(tray);
            }
        });

        //保存wcs表
        if (!CollectionUtils.isEmpty(saveDetailList)) {
            taskWcsDetailService.saveBatch(saveDetailList);
        }
        //更新库位的状态
        if (!CollectionUtils.isEmpty(locationUpdateList)) {
            locationService.updateBatchById(locationUpdateList);
        }
        //更新托盘的状态
        if (!CollectionUtils.isEmpty(trayUpdateList)) {
            trayService.updateBatchById(trayUpdateList);
        }
        //更新taskin表
        if (!CollectionUtils.isEmpty(updatetTaskInList)) {
            taskInService.updateBatchById(updatetTaskInList);
        }
        //组完盘，直接调wcs
        if (CollectionUtils.isNotEmpty(wcsList)) {
            //对发送列表进行排序，入库优先发送二伸位的
            List<WcsOrderDTO> sendList = wcsList.stream()
                    .sorted(Comparator.comparing(WcsOrderDTO::getMainSort).reversed()
                            .thenComparing(WcsOrderDTO::getMainTaskNo))
                    .collect(Collectors.toList());
            for (WcsOrderDTO orderDTO : sendList) {
                //判断双伸位
                if (Constants.TASK_TYPE_MOVE.equals(orderDTO.getTaskType())) {
                    //发送移库
                    wcsReportUtil.sendWcsMoveReport(orderDTO);
                } else {
                    //组完盘，直接调wcs
                    wcsReportUtil.sendWcsInReport(orderDTO);
                }
            }
        }
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult floorStocking(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        TAdvanceDeliveryDetailVO oldDO = tAdvanceDeliveryDetailMapper.selectInfoById(tAdvanceDeliveryDetailDTO.getId());
        if (oldDO == null) {
            return AjaxResult.error("未查询到数据");
        }
        if (Constants.INOUT_NEXTFLAG_NOT.equals(oldDO.getNextFlag())
                || Constants.INOUT_NEXTFLAG_EXE_END.equals(oldDO.getNextFlag())
                || Constants.INOUT_NEXTFLAG_ABOLISH.equals(oldDO.getNextFlag())) {
            return AjaxResult.error("当前状态下不可地堆上架");
        }
        if (oldDO.getDetectionCount().compareTo(oldDO.getPutawayCount()) == -1) {
            return AjaxResult.error("数量已上架完成，不可再地堆");
        }
        Long trayId = null;
        if (StringUtils.isNotBlank(tAdvanceDeliveryDetailDTO.getTrayCode())) {
            TTray tTray = trayService.selectTTrayByCode(tAdvanceDeliveryDetailDTO.getTrayCode());
            if (tTray == null) {
                return AjaxResult.error("未获取到对应的载具信息");
            }
            if (Constants.TRAY_STATUS_FULL.equals(tTray.getStatus())) {
                return AjaxResult.error("当前载具状态不可用");
            }
            if (tTray.getLocationId() != null && !tTray.getLocationId().equals(tAdvanceDeliveryDetailDTO.getLocationId())) {
                return AjaxResult.error("当前载具已绑定其他库位");
            }
            trayId = tTray.getId();
        }

        BigDecimal floorCount = oldDO.getDetectionCount();

        //对物料详情进行地堆
        tAdvanceDeliveryDetailDTO.setStatus(Constants.MATERIAL_DETAIL_STATUS_IN);
        if (Constants.YES.equals(tAdvanceDeliveryDetailDTO.getFloorStatus())) {
            tAdvanceDeliveryDetailDTO.setMaterialDetailIds(null);
            tMaterialDetailMapper.updateInfoByIdsOrRelId(tAdvanceDeliveryDetailDTO, trayId);
        } else {
            if (CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getMaterialDetailIds())
                    && CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getRfIds())) {
                throw new ServiceException("部分地堆请先选择物料");
            }

            List<TMaterialDetail> materialDetails = tMaterialDetailMapper.selectList(new QueryWrapper<TMaterialDetail>()
                    .eq("material_id", oldDO.getMaterialId())
                    .eq("batch_code", oldDO.getBatchCode())
                    .in(CollectionUtils.isNotEmpty(tAdvanceDeliveryDetailDTO.getRfIds()),"rfid_head",tAdvanceDeliveryDetailDTO.getRfIds())
                    .in(CollectionUtils.isNotEmpty(tAdvanceDeliveryDetailDTO.getMaterialDetailIds()),"id",tAdvanceDeliveryDetailDTO.getMaterialDetailIds())
                    .eq("advance_registration_id", oldDO.getId())
                    .eq("status", Constants.MATERIAL_DETAIL_STATUS_ADD));
            if (materialDetails.size()  == 0){
                throw new ServiceException("该标签不可用或者已上架");
            }

            if (materialDetails.size() > 0) {
                floorCount = materialDetails.stream().map(TMaterialDetail::getRfidCount).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            tMaterialDetailMapper.updateInfoByIdsOrRelId(tAdvanceDeliveryDetailDTO, trayId);
        }

        //更新状态
        TAdvanceDeliveryDetail tAdvanceDeliveryDetail = new TAdvanceDeliveryDetail();
        tAdvanceDeliveryDetail.setId(tAdvanceDeliveryDetailDTO.getId());
        tAdvanceDeliveryDetail.setReceiveCount(oldDO.getReceiveCount().add(floorCount));
        if (oldDO.getDetectionCount().compareTo(tAdvanceDeliveryDetail.getReceiveCount()) == 0) {
            tAdvanceDeliveryDetail.setPutawayCount(oldDO.getDetectionCount());
            //全部执行
            tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_END);
        } else {
            tAdvanceDeliveryDetail.setPutawayCount(oldDO.getPutawayCount().add(floorCount));
            //部分执行
            tAdvanceDeliveryDetail.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_PART);
        }
        tAdvanceDeliveryDetailMapper.updateById(tAdvanceDeliveryDetail);

        TTaskIn taskIn = new TTaskIn();
        taskIn.setTrayId(trayId);
        taskIn.setMaterialId(oldDO.getMaterialId());
        taskIn.setBatchCode(oldDO.getBatchCode());
        taskIn.setActualCount(floorCount);
        taskIn.setAdvanceRegistrationId(oldDO.getId());
        taskIn.setLocationId(tAdvanceDeliveryDetailDTO.getLocationId());
        taskInService.save(taskIn);

        //更新库存
        oldDO.setDetectionCount(floorCount);
        this.inStockByMaterial(oldDO, trayId, tAdvanceDeliveryDetailDTO.getLocationId());

        //查询入库单详情状态，更改主表状态
        String status = advanceDeliveryService.getDetailCountStatus(oldDO.getAdvanceDeliveryId(), 3);
        if (StringUtils.isNotEmpty(status)) {
            TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
            updateDelivery.setId(oldDO.getAdvanceDeliveryId());
            updateDelivery.setStatus(status);
            updateDelivery.setCompleteState(status);
            advanceDeliveryService.updateById(updateDelivery);
            if (Constants.INOUT_STATUS_COMPLETE_END.equals(status)) {
                //全部完成，根据类型，更新调拨单
                advanceDeliveryService.updateAllotByDeliveryId(oldDO.getAdvanceDeliveryId());
            }
        }
        //更新载具状态
        if (trayId != null) {
            TTray tTray = new TTray();
            tTray.setId(trayId);
            tTray.setLocationId(tAdvanceDeliveryDetailDTO.getLocationId());
            tTrayMapper.updateById(tTray);
        }
        //更新库位状态
        TLocation tLocation = new TLocation();
        tLocation.setId(tAdvanceDeliveryDetailDTO.getLocationId());
        tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
        tLocation.setPalletNum(tAdvanceDeliveryDetailDTO.getTrayCode());
        locationService.updateById(tLocation);
        return AjaxResult.success();
    }

    /**
     * 入库存
     */
    @Transactional
    public void inStockByMaterial(TAdvanceDeliveryDetailVO tAdvanceDeliveryDetailVO, Long trayId, Long locationId) {
        //todo wxr 处理库存记录 物料+库位+批次号
        BigDecimal count = tAdvanceDeliveryDetailVO.getDetectionCount();
        //库存详情
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setMaterialId(tAdvanceDeliveryDetailVO.getMaterialId());
        tStockDetail.setType(Constants.WCS_TASK_TYPE_IN);
        tStockDetail.setLocationId(locationId);
        tStockDetail.setOriginCode(tAdvanceDeliveryDetailVO.getAdvanceDeliveryCode());
        tStockDetail.setOriginId(tAdvanceDeliveryDetailVO.getId());//原单id,目前存的task_in_id
        tStockDetail.setStatus("0");
        tStockDetail.setBatchCode(tAdvanceDeliveryDetailVO.getBatchCode());
        tStockDetail.setCurrentCount(count);
        //查询库存之前的数量
        TStockDetail stockDetailDTO = new TStockDetail();
        stockDetailDTO.setLocationId(locationId);
        stockDetailDTO.setMaterialId(tAdvanceDeliveryDetailVO.getMaterialId());
        stockDetailDTO.setBatchCode(tAdvanceDeliveryDetailVO.getBatchCode());
        BigDecimal existCount = tStockDetailMapper.selectTStockDetailCountParam(stockDetailDTO);
        if (existCount == null) {
            tStockDetail.setBeforeCount(BigDecimal.ZERO);// 操作前数量
            tStockDetail.setCurrentCount(count);// 操作后当前数量
        } else {
            tStockDetail.setBeforeCount(existCount);// 操作前数量
            tStockDetail.setCurrentCount(existCount.add(count));// 操作后当前数量
        }
        tStockDetailMapper.insert(tStockDetail);

        //一个物料一个托盘一条
        TStock model = stockService.getOne(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getLocationId, locationId)
                .eq(TStock::getMaterialId, tAdvanceDeliveryDetailVO.getMaterialId())
                .eq(TStock::getBatchCode, tAdvanceDeliveryDetailVO.getBatchCode())
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));

        //一个物料一条
        TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                .eq(TStockMain::getMaterialId, tAdvanceDeliveryDetailVO.getMaterialId())
                .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));

        // 更新库存数据
        TStock stockUpdate = new TStock();

        // 更新库存总数据
        TStockMain stockMainUpdate = new TStockMain();

        // 如果同托盘下有数据，则更新库存
        if (ObjectUtils.isNotNull(model)) {

            // 更新库存数据
            stockUpdate.setId(model.getId());
            stockUpdate.setCount(model.getCount().add(count));
            stockUpdate.setAvailableCount(model.getAvailableCount().add(count));

            // 更新库存总数据
            stockMainUpdate.setId(stockMain.getId());
            stockMainUpdate.setLibraryCount(stockMain.getLibraryCount().add(count));
            stockMainUpdate.setAvailableCount(stockMain.getAvailableCount().add(count));

            stockService.updateById(stockUpdate);
            stockMainService.updateById(stockMainUpdate);
        } else {
            model = new TStock();
            TLocation locationVO = locationService.getById(locationId);
            Long areaId = locationVO != null ? locationVO.getAreaId() : null;
            // 可用数量
            model.setAvailableCount(count);
            // 开始时间
            model.setBeginDate(new Date());
            model.setLocationId(locationId);
            model.setAreaId(areaId);
            model.setMaterialId(tAdvanceDeliveryDetailVO.getMaterialId());
            model.setBatchCode(tAdvanceDeliveryDetailVO.getBatchCode());
            model.setProducedDate(tAdvanceDeliveryDetailVO.getProducedDate());
            model.setCount(count);
            model.setAvailableCount(count);
            model.setTrayId(trayId);
//            model.setOriginType(Constants.WCS_TASK_TYPE_IN);
            model.setStatus("0");
            model.setIsFreeze("0");

            if (ObjectUtils.isNotNull(stockMain)) {
                // 更新库存总数据
                stockMainUpdate.setId(stockMain.getId());
                stockMainUpdate.setLibraryCount(stockMain.getLibraryCount().add(model.getCount()));
                stockMainUpdate.setAvailableCount(stockMain.getAvailableCount().add(model.getCount()));
                stockMainService.updateById(stockMainUpdate);
            } else {
                stockMain = new TStockMain();
                stockMain.setMaterialId(tAdvanceDeliveryDetailVO.getMaterialId());
                stockMain.setLibraryCount(count);
                stockMain.setAvailableCount(count);
                stockMainService.save(stockMain);
            }
            stockService.save(model);
        }
    }

    /**
     * 根据ids获取列表
     *
     * @param advanceRegistrationIds
     * @return
     */
    public List<TAdvanceDeliveryDetail> getListByIds(List<Long> advanceRegistrationIds) {
        QueryWrapper<TAdvanceDeliveryDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("id", advanceRegistrationIds);
        return tAdvanceDeliveryDetailMapper.selectList(queryWrapper);
    }

    /**
     * pda上架新
     *
     * @param tAdvanceRegistrationApiDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult putawayTaskNew(TAdvanceRegistrationApiDTO tAdvanceRegistrationApiDTO) {
        TTray tTrayVO = trayService.selectTTrayByCode(tAdvanceRegistrationApiDTO.getTrayCode());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到当前托盘信息");
        }
        if (Constants.TRAY_STATUS_FULL.equals(tTrayVO.getStatus())) {
            return AjaxResult.error("当前托盘已满，请选择其他托盘");
        }

        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.eq("task_type", Constants.TASK_TYPE_PUT);
        taskQw.ne("task_status", Constants.INOUT_STATUS_END);
        taskQw.in("tray_id", tTrayVO.getId());
        Long taskCount = taskWcsService.count(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行");
        }
        List<TTaskIn> taskIns = new ArrayList<>();
        TTaskWcs tTaskWcs = new TTaskWcs();
        List<TTaskWcsDetail> saveDetailList = new ArrayList<>();
        List<TAdvanceDeliveryApiDTO> materialList = tAdvanceRegistrationApiDTO.getMaterialList();
        List<Long> materials = materialList.stream().map(TAdvanceDeliveryApiDTO::getMaterialId).collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
        Set<Long> materialCategorys = new HashSet<>();
        List<Long> materialIds = new ArrayList<>();
        //发送命令
        List<WcsOrderDTO> wcsList = new ArrayList<>();
        //生成上架任务，判断托盘承载重量
        final Double[] materialSum = {0d};
        materialList.forEach(e -> {
            TMaterial tMaterialVO = materialMap.get(e.getMaterialId());
            if (tMaterialVO != null) {
                if (tMaterialVO.getRoughWeight() != null) {
                    materialSum[0] = materialSum[0] + tMaterialVO.getRoughWeight();
                }
                Boolean trayStatus = TrayTypeEnum.compareTrayType(tTrayVO.getTrayCategory(), tMaterialVO.getMaterialLength(), tMaterialVO.getMaterialWidth(), tMaterialVO.getMaterialHeight());
                if (!trayStatus) {
                    throw new ServiceException(tTrayVO.getCode() + "所选物料长宽高超出最大限制范围！");
                }
                materialCategorys.add(tMaterialVO.getCategoryId());
                materialIds.add(e.getMaterialId());
            }
        });
        if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
            throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
        }
        List<String> rfIds = new ArrayList<>();
        for (TAdvanceDeliveryApiDTO tAdvanceDeliveryApiDTO : materialList) {
            BigDecimal count = tAdvanceDeliveryApiDTO.getCount();
            if (count.compareTo(BigDecimal.ZERO) != 1) {
                break;
            }
            QueryWrapper<TAdvanceDeliveryDetail> detailQw = new QueryWrapper<>();
            detailQw.eq("del_flag", Constants.DEL_FLAG_NO);
            detailQw.eq("material_id", tAdvanceDeliveryApiDTO.getMaterialId());
            detailQw.eq("batch_code", tAdvanceDeliveryApiDTO.getBatchCode());
            detailQw.in("status", Constants.INOUT_NEXTFLAG_PUT, Constants.INOUT_NEXTFLAG_EXE_PART);
            detailQw.orderByAsc("create_time");
            List<TAdvanceDeliveryDetail> deliveryDetailList = tAdvanceDeliveryDetailMapper.selectList(detailQw);
            if (!CollectionUtils.isEmpty(deliveryDetailList)) {
                BigDecimal checkCountSum = deliveryDetailList.stream().map(TAdvanceDeliveryDetail::getDetectionCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal actualCountSum = deliveryDetailList.stream().map(TAdvanceDeliveryDetail::getPutawayCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal residueCountSum = checkCountSum.subtract(actualCountSum);
                if (tAdvanceDeliveryApiDTO.getCount().compareTo(residueCountSum) == 1) {
                    throw new ServiceException("超出可上架数量，不可上架");
                }
                for (TAdvanceDeliveryDetail tAdvanceDeliveryDetail : deliveryDetailList) {
                    if (count.compareTo(BigDecimal.ZERO) != 1) {
                        break;
                    }
                    //更新登记表状态
                    TAdvanceDeliveryDetail updatyeDO = new TAdvanceDeliveryDetail();
                    updatyeDO.setId(tAdvanceDeliveryDetail.getId());
                    BigDecimal residueCount = tAdvanceDeliveryDetail.getDetectionCount().subtract(tAdvanceDeliveryDetail.getPutawayCount());//现单剩余数量
                    BigDecimal actualCount = BigDecimal.ZERO;
                    BigDecimal taskInActualCount = BigDecimal.ZERO;
                    if (count.compareTo(residueCount) != -1) {
                        actualCount = tAdvanceDeliveryDetail.getDetectionCount();
                        taskInActualCount = residueCount;
                        count = count.subtract(residueCount);
                        updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_END);
                    } else {
                        actualCount = tAdvanceDeliveryDetail.getPutawayCount().add(count);
                        taskInActualCount = count;
                        count = BigDecimal.ZERO;
                        updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_PART);
                    }
                    updatyeDO.setPutawayCount(actualCount);
                    this.updateById(updatyeDO);

                    //taskin表
                    TTaskIn taskIn = new TTaskIn();
                    taskIn.setAdvanceRegistrationId(tAdvanceDeliveryDetail.getId());
                    taskIn.setMaterialId(tAdvanceDeliveryApiDTO.getMaterialId());
                    taskIn.setTrayId(tTrayVO.getId());
                    taskIn.setActualCount(taskInActualCount);
                    taskIn.setBatchCode(tAdvanceDeliveryDetail.getBatchCode());
                    taskIns.add(taskIn);
                }
                //更新物料详情表的库位和托盘信息
                rfIds.addAll(tAdvanceDeliveryApiDTO.getRfids());
            }
        }

        if (!CollectionUtils.isEmpty(taskIns)) {
            //保存
            taskInService.saveBatch(taskIns);
            //保存wcs
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_IN);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);//pda直接执行
            tTaskWcs.setTrayId(tTrayVO.getId());
            tTaskWcs.setTrayCode(tTrayVO.getCode());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcs.setMainTaskNo(tTaskWcs.getTaskNo());
            //小微库，可选库位
//            Long locationId = tAdvanceRegistrationApiDTO.getLocationId();
//            if (locationId != null) {
//                TLocation tLocationVO = locationService.getById(locationId);
//                if (tLocationVO == null || !Constants.LOCATION_GOODS_ALLOCATION_STATUS_1.equals(tLocationVO.getGoodsAllocationStatus())) {
//                    throw new ServiceException("当前库位不可用!");
//                }
//            } else {
//                //机务库，使用推荐库位
//                locationId = recommendedLocationUtil.recommendedLocation(null, tTrayVO.getId(), materialCategorys, materialIds);
//            }
            //机务库，使用推荐库位
            Long locationId = recommendedLocationUtil.recommendedLocation(null, tTrayVO.getId(), materialCategorys, materialIds);
            if (locationId == null) {
                throw new ServiceException("无可用库位!");
            }
            tTaskWcs.setLocationId(locationId);//推荐库位
            if (tTaskWcs.getLocationId() != null) {
                //更新库位【库位状态为标记入库】【托盘编号】
                TLocation tLocation = new TLocation();
                tLocation.setId(tTaskWcs.getLocationId());
                tLocation.setStatus(tAdvanceRegistrationApiDTO.getTrayStatus());
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
                tLocation.setPalletNum(tTrayVO.getCode());
                locationService.updateById(tLocation);
            }
            taskWcsService.save(tTaskWcs);

            //如果是托盘，直接生成wcs任务
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                TLocation locationInfo = locationService.getById(locationId);
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                Integer mainSort = locationInfo.getExtentionType();
                //组装出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(tTaskWcs.getId(), tTaskWcs.getTaskNo(), WcsReportUtil.stationIn, endStation, locationInfo.getCode(), tTrayVO.getCode());
                orderDTO.setMainSort(mainSort);
                orderDTO.setMainTaskNo(tTaskWcs.getMainTaskNo());
                //组装移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, tTaskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    taskNoMove.setMainSort(mainSort);
                    wcsList.add(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getMoveTaskNo());
                }
                wcsList.add(orderDTO);
            }

            taskIns.forEach(e -> {
                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
                tTaskWcsDetail.setOriginId(e.getId());
                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
                saveDetailList.add(tTaskWcsDetail);
            });
            //保存wcs详情表
            taskWcsDetailService.saveBatch(saveDetailList);
            //入库完成之后需要维护此表的库位和托盘等信息,更新状态为已组盘未入库
            if (CollectionUtils.isNotEmpty(rfIds)) {
                tMaterialDetailMapper.updateInfoByRfIds(rfIds, tTrayVO.getId(), locationId, Constants.MATERIAL_DETAIL_STATUS_IN_NO);
            }
            //组完盘，直接调wcs
            if (CollectionUtils.isNotEmpty(wcsList)) {
                //对发送列表进行排序，入库优先发送二伸位的
                List<WcsOrderDTO> sendList = wcsList.stream()
                        .sorted(Comparator.comparing(WcsOrderDTO::getMainSort).reversed()
                                .thenComparing(WcsOrderDTO::getMainTaskNo))
                        .collect(Collectors.toList());
                for (WcsOrderDTO orderDTO : sendList) {
                    //判断双伸位
                    if (Constants.TASK_TYPE_MOVE.equals(orderDTO.getTaskType())) {
                        //发送移库
                        wcsReportUtil.sendWcsMoveReport(orderDTO);
                    } else {
                        //组完盘，直接调wcs
                        wcsReportUtil.sendWcsInReport(orderDTO);
                    }
                }
            }
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 重新组盘
     *
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult afreshPutaway(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        // 查询任务id
        Long taskWcsId = tAdvanceDeliveryDetailDTO.getId();
        //去除为0的数据
        List<TTaskIn> taskInList = tAdvanceDeliveryDetailDTO.getTaskInList();
        taskInList = taskInList.stream().filter(e -> e.getActualCount() != null && e.getActualCount().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskInList)) {
            return AjaxResult.error("上架数量不可为0");
        }
        TTaskWcs taskVO = taskWcsService.getById(taskWcsId);
        if (taskVO == null) {
            return AjaxResult.error("未查询到当前任务信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(taskVO.getTaskStatus()) || Constants.WCS_EXECUTE_STATUS_CANCELLATION.equals(taskVO.getTaskStatus())) {
            return AjaxResult.error("当前任务状态不可重新组盘");
        }

        List<TTaskWcsDetailVO> wcsDetailList = taskWcsDetailService.getListByTaskId(taskWcsId, Constants.TASK_TYPE_PUT);
        if (CollectionUtils.isEmpty(wcsDetailList)) {
            return AjaxResult.error("未查询到对应任务详细信息");
        }
        //通过登记ids获取对应的taskIn主键
        List<Long> taskInIds = wcsDetailList.stream().map(TTaskWcsDetailVO::getOriginId).distinct().collect(Collectors.toList());
        //通过登记ids获取对应的入库单详情主键
        List<Long> advanceIds = wcsDetailList.stream().map(TTaskWcsDetailVO::getAdvanceRegistrationId).distinct().collect(Collectors.toList());

        //删除wcs任务的数据
        taskWcsService.deleteWcsTaskById(taskVO, taskInIds);
        //查询这个任务原始的物料详情ids，为了更新之前选择的物料信息，删除原有信息后，新更新的物料详细表从此单据里面更新
        List<Long> orignMaDeIds = new ArrayList<>();
        List<TMaterialDetail> materialDeList = tMaterialDetailMapper.selectList(Wrappers.lambdaQuery(TMaterialDetail.class)
                .in(TMaterialDetail::getAdvanceRegistrationId, advanceIds)
                .eq(TMaterialDetail::getTrayId, taskVO.getTrayId())
                .eq(TMaterialDetail::getLocationId, taskVO.getLocationId())
                .eq(TMaterialDetail::getDetectionFailStatus, Constants.MATERIAL_DETAIL_CHECK_SUCESS)
                .eq(TMaterialDetail::getDelFlag, Constants.DEL_FLAG_NO));
        if (CollectionUtils.isNotEmpty(materialDeList)) {
            orignMaDeIds = materialDeList.stream().map(TMaterialDetail::getId).collect(Collectors.toList());
        }
        //还原物料详情
        tMaterialDetailMapper.update(new TMaterialDetail(),
                new UpdateWrapper<TMaterialDetail>()
                        .eq("del_flag", Constants.DEL_FLAG_NO)
                        .in("advance_registration_id", advanceIds)
                        .eq("tray_id", taskVO.getTrayId())
                        .eq("location_id", taskVO.getLocationId())
                        .eq("detection_fail_status", Constants.MATERIAL_DETAIL_CHECK_SUCESS)
                        .set("location_id", null)
                        .set("tray_id", null)
                        .set("status", Constants.MATERIAL_DETAIL_STATUS_ADD));

        //通过托盘ids获取对应的托盘信息
        List<Long> trays = taskInList.stream().map(TTaskIn::getTrayId).distinct().collect(Collectors.toList());
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.eq("task_type", Constants.TASK_TYPE_PUT);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.in("tray_id", trays);
        taskQw.ne("tray_id", taskVO.getTrayId());
        Long taskCount = taskWcsService.count(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行");
        }
        Map<Long, TTray> trayCodeMap = trayService.getTrayByIds(trays);
        //通过物料ids获取对应的物料信息
        List<Long> materials = taskInList.stream().map(TTaskIn::getMaterialId).distinct().collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);

        Map<Long, TTaskWcsDetailVO> registrationMap = wcsDetailList.stream().collect(Collectors.toMap(TTaskWcsDetailVO::getAdvanceRegistrationId, Function.identity()));

        //对每个登记数量进行判断
        Map<Long, List<TTaskIn>> registerGroupMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getAdvanceRegistrationId));
        registrationMap.forEach((key1, deliveryDetail) -> {
            List<TTaskIn> taskIns = registerGroupMap.get(key1);
            BigDecimal actualCount = taskIns.stream().map(TTaskIn::getActualCount).reduce(BigDecimal.ZERO,BigDecimal::add);//实际数量
            BigDecimal residueCount = deliveryDetail.getActualCount();//剩余数量
            if (actualCount.compareTo(residueCount) != 0) {
                throw new ServiceException("物料总上架数量和已组上架数量必须相等");
            }
            if (taskIns.size() == 1 && deliveryDetail.getActualCount().equals(actualCount)) {
                //判断是否全部上架，一个单据物料只有一条且实际上架数量=登记数量
                TTaskIn taskIn = taskIns.get(0);
                taskIn.setActualFlag(Constants.YES);
            }
        });

        //保存taskIn表
        taskInService.saveBatch(taskInList);

        //库位更新
        List<TLocation> locationUpdateList = new ArrayList<>();
        //托盘状态更新
        List<TTray> trayUpdateList = new ArrayList<>();
        //任务详情列表
        List<TTaskWcsDetail> saveDetailList = new ArrayList<>();
        //推荐库位，排除已有库位
        List<Long> removeLocations = new ArrayList<>();
        //更新taskin
        List<TTaskIn> updatetTaskInList = new ArrayList<>();
        //发送命令
        List<WcsOrderDTO> wcsList = new ArrayList<>();
        //生成wcs任务
        Map<Long, List<TTaskIn>> trayInfoMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getTrayId));
        List<Long> finalOrignMaDeIds = orignMaDeIds;
        trayInfoMap.forEach((key, value) -> {
            List<TTaskIn> taskIns = value;
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_IN);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            tTaskWcs.setTrayId(key);
            TTray tTrayVO = trayCodeMap.get(key);
            if (tTrayVO == null) {
                throw new ServiceException("未获取到对应载具信息!");
            }
            if (tTrayVO.getLocationId() != null) {
                throw new ServiceException(tTrayVO.getCode() + "此载具已在库，请先出库!");
            }
            //生成上架任务，判断托盘承载重量
            final Double[] materialSum = {0d};
            Set<Long> categoryIds = new HashSet();
            List<Long> materialIds = new ArrayList<>();
            value.forEach(e -> {
                TMaterial tMaterialVO = materialMap.get(e.getMaterialId());
                if (tMaterialVO != null) {
                    if (tMaterialVO.getRoughWeight() != null) {
                        materialSum[0] = materialSum[0] + tMaterialVO.getRoughWeight();
                    }
                    Boolean trayStatus = TrayTypeEnum.compareTrayType(tTrayVO.getTrayCategory(), tMaterialVO.getMaterialLength(), tMaterialVO.getMaterialWidth(), tMaterialVO.getMaterialHeight());
                    if (!trayStatus) {
                        throw new ServiceException(tTrayVO.getCode() + "所选物料长宽高超出最大限制范围！");
                    }
                    categoryIds.add(tMaterialVO.getCategoryId());
                    materialIds.add(e.getMaterialId());
                }
            });
            if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
                throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
            }
            tTaskWcs.setTrayCode(tTrayVO.getCode());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcs.setMainTaskNo(tTaskWcs.getTaskNo());
            Long locationId = recommendedLocationUtil.recommendedLocation(removeLocations, tTrayVO.getId(), categoryIds, materialIds);
            if (locationId == null) {
                throw new ServiceException("无可用库位!");
            } else {
                removeLocations.add(locationId);
            }
            tTaskWcs.setLocationId(locationId);//推荐库位
            if (tTaskWcs.getLocationId() != null) {
                //更新库位【库位状态为标记入库】【托盘编号】
                TLocation tLocation = new TLocation();
                tLocation.setId(tTaskWcs.getLocationId());
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
                tLocation.setPalletNum(tTrayVO.getCode());
                locationUpdateList.add(tLocation);
            }
            taskWcsService.save(tTaskWcs);

            //如果是托盘，直接生成wcs任务
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                TLocation locationInfo = locationService.getById(locationId);
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                Integer mainSort = locationInfo.getExtentionType();
                //组装出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(tTaskWcs.getId(), tTaskWcs.getTaskNo(), WcsReportUtil.stationIn, endStation, locationInfo.getCode(), tTrayVO.getCode());
                orderDTO.setMainSort(mainSort);
                orderDTO.setMainTaskNo(tTaskWcs.getMainTaskNo());
                //组装移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(removeLocations, tTaskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    removeLocations.add(taskNoMove.getLocationId());
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    taskNoMove.setMainSort(mainSort);
                    wcsList.add(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getMoveTaskNo());
                }
                wcsList.add(orderDTO);
            }

            taskIns.forEach(e -> {
                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
                tTaskWcsDetail.setOriginId(e.getId());
                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
                saveDetailList.add(tTaskWcsDetail);
                TTaskIn taskInUpdate = new TTaskIn();
                taskInUpdate.setId(e.getId());
                taskInUpdate.setLocationId(locationId);
                updatetTaskInList.add(taskInUpdate);

                //入库完成之后需要维护此表的库位和托盘等信息,更新状态为已组盘未入库
                if (CollectionUtils.isNotEmpty(e.getRfIds())) {
                    tMaterialDetailMapper.updateInfoByRfIds(e.getRfIds(), tTrayVO.getId(), locationId, Constants.MATERIAL_DETAIL_STATUS_IN_NO);
                } else {
                    if (Constants.YES.equals(e.getActualFlag())) {
                        //全部上架,更新物料详情
                        UpdateWrapper<TMaterialDetail> setMater = new UpdateWrapper<TMaterialDetail>()
                                .eq("material_id", e.getMaterialId())
                                .eq("batch_code", e.getBatchCode())
                                .set("location_id", locationId)
                                .set("tray_id", tTrayVO.getId())
                                .set("status", Constants.MATERIAL_DETAIL_STATUS_IN_NO);
                        if (CollectionUtils.isNotEmpty(finalOrignMaDeIds)) {
                            setMater.in("id", finalOrignMaDeIds);
                        }
                        tMaterialDetailMapper.update(new TMaterialDetail(), setMater);
                    }
                }
            });
            //托盘状态
            String trayStatus = taskIns.get(0).getTrayStatus();
            if (StringUtils.isNotBlank(trayStatus)) {
                TTray tray = new TTray();
                tray.setId(tTrayVO.getId());
                tray.setStatus(trayStatus);
                trayUpdateList.add(tray);
            }
        });

        //保存wcs表
        if (!CollectionUtils.isEmpty(saveDetailList)) {
            taskWcsDetailService.saveBatch(saveDetailList);
        }
        //更新库位的状态
        if (!CollectionUtils.isEmpty(locationUpdateList)) {
            locationService.updateBatchById(locationUpdateList);
        }
        //更新托盘的状态
        if (!CollectionUtils.isEmpty(trayUpdateList)) {
            trayService.updateBatchById(trayUpdateList);
        }
        //更新taskin表
        if (!CollectionUtils.isEmpty(updatetTaskInList)) {
            taskInService.updateBatchById(updatetTaskInList);
        }
        //组完盘，直接调wcs
        if (CollectionUtils.isNotEmpty(wcsList)) {
            //对发送列表进行排序，入库优先发送二伸位的
            List<WcsOrderDTO> sendList = wcsList.stream()
                    .sorted(Comparator.comparing(WcsOrderDTO::getMainSort).reversed()
                            .thenComparing(WcsOrderDTO::getMainTaskNo))
                    .collect(Collectors.toList());
            for (WcsOrderDTO orderDTO : sendList) {
                //判断双伸位
                if (Constants.TASK_TYPE_MOVE.equals(orderDTO.getTaskType())) {
                    //发送移库
                    wcsReportUtil.sendWcsMoveReport(orderDTO);
                } else {
                    //组完盘，直接调wcs
                    wcsReportUtil.sendWcsInReport(orderDTO);
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * 齐套入库上架
     *
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult putawayComplete(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {

        TAdvanceDelivery oldDelivery = advanceDeliveryService.getById(tAdvanceDeliveryDetailDTO.getId());
        if (Constants.INOUT_STATUS_COMPLETE_END.equals(oldDelivery.getStatus())) {
            return AjaxResult.error("当前单据状态已完成，不可再次上架");
        }

        //去除为0的数据
        List<TTaskIn> taskInList = tAdvanceDeliveryDetailDTO.getTaskInList();
        taskInList = taskInList.stream().filter(e -> e.getActualCount() != null && e.getActualCount().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskInList)) {
            return AjaxResult.error("上架数量不可为0");
        }

        //通过库位ids获取对应的库位信息
        List<Long> locations = taskInList.stream().map(TTaskIn::getLocationId).distinct().collect(Collectors.toList());
        Map<Long, TLocationVO> locationMap = locationService.getLocationByIds(locations);

        //通过托盘ids获取对应的托盘信息
        List<Long> trays = taskInList.stream().filter(e -> e.getTrayId() != null).map(TTaskIn::getTrayId).distinct().collect(Collectors.toList());
        Map<Long, TTray> trayCodeMap = trayService.getTrayByIds(trays);

        //通过物料ids获取对应的物料信息
        List<Long> materials = taskInList.stream().map(TTaskIn::getMaterialId).distinct().collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
        //通过登记ids获取对应的登记信息
        List<Long> advanceRegistrationIds = taskInList.stream().map(TTaskIn::getAdvanceRegistrationId).distinct().collect(Collectors.toList());
        List<TAdvanceDeliveryDetail> advanceDeliveryDetailList = this.getListByIds(advanceRegistrationIds);
        if (CollectionUtils.isEmpty(advanceDeliveryDetailList)) {
            return AjaxResult.error("未查询到对应入库单");
        }

        Map<Long, TAdvanceDeliveryDetail> registrationMap = advanceDeliveryDetailList.stream().collect(Collectors.toMap(TAdvanceDeliveryDetail::getId, Function.identity()));

        //更新登记信息列表
        List<TAdvanceDeliveryDetail> detailUpdateList = new ArrayList<>();
        //对每个登记数量进行判断
        Map<Long, List<TTaskIn>> registerGroupMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getAdvanceRegistrationId));
        registrationMap.forEach((key1, deliveryDetail) -> {
            List<TTaskIn> taskIns = registerGroupMap.get(key1);
            BigDecimal actualCount = taskIns.stream().map(TTaskIn::getActualCount).reduce(BigDecimal.ZERO,BigDecimal::add);//实际数量
            BigDecimal residueCount = BigDecimal.ZERO;//剩余数量
            if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())
                    || Constants.INOUT_DELIVERY_MODULE_FAST.equals(oldDelivery.getDeliveryModule())) {
                residueCount = deliveryDetail.getPredictCount().subtract(deliveryDetail.getPutawayCount());//剩余数量
            } else {
                residueCount = deliveryDetail.getRegistrationCount().subtract(deliveryDetail.getPutawayCount());//剩余数量
            }
            if (actualCount.compareTo(residueCount) == 1) {
                throw new ServiceException("物料总上架数量不可超出预估上架数量");
            }
            TAdvanceDeliveryDetail updatyeDO = new TAdvanceDeliveryDetail();
            updatyeDO.setId(key1);
            updatyeDO.setPutawayCount(actualCount.add(deliveryDetail.getPutawayCount()));
            updatyeDO.setReceiveCount(actualCount.add(deliveryDetail.getPutawayCount()));
            if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())
                    || Constants.INOUT_DELIVERY_MODULE_FAST.equals(oldDelivery.getDeliveryModule())) {
                //快捷入库
                updatyeDO.setRegistrationCount(actualCount.add(deliveryDetail.getRegistrationCount()));
                updatyeDO.setDetectionCount(actualCount.add(deliveryDetail.getRegistrationCount()));
            }
            if (residueCount.compareTo(BigDecimal.ZERO) == 0) {
                updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_END);
            } else {
                updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_PART);
            }
            detailUpdateList.add(updatyeDO);
        });

        //更新登记表转化状态
        this.updateBatchById(detailUpdateList);
        //保存taskIn表
        taskInService.saveBatch(taskInList);

        //库位更新
        List<TLocation> locationUpdateList = new ArrayList<>();
        //载具列表
        List<TTray> trayUpdateList = new ArrayList<>();
        //托盘状态更新
        //生成wcs任务
        Map<Long, List<TTaskIn>> trayInfoMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getLocationId));
        trayInfoMap.forEach((key, value) -> {
            List<TTaskIn> taskIns = value;
            Long trayId = taskIns.get(0).getTrayId();
            Long locationId = key;
            TLocationVO tLocationVO = locationMap.get(locationId);
            if (tLocationVO == null) {
                throw new ServiceException("未获取到对应库位信息!");
            }
            if (!Constants.LOCATION_GOODS_ALLOCATION_STATUS_1.equals(tLocationVO.getGoodsAllocationStatus())) {
                throw new ServiceException(tLocationVO.getName() + "此库位不可使用!");
            }
            if (trayId != null) {
                if (tLocationVO.getTrayId() != null && !tLocationVO.getTrayId().equals(trayId)) {
                    throw new ServiceException(tLocationVO.getName() + "此库位已绑定其他载具");
                }
            } else {
                trayId = tLocationVO.getTrayId();
            }
            TTray tTrayVO = trayCodeMap.get(trayId);
            if (tTrayVO != null) {
                //生成上架任务，判断托盘承载重量
                final Double[] materialSum = {0d};
                Set<Long> categoryIds = new HashSet();
                value.forEach(e -> {
                    TMaterial tMaterialVO = materialMap.get(e.getMaterialId());
                    if (tMaterialVO != null) {
                        if (tMaterialVO.getRoughWeight() != null) {
                            materialSum[0] = materialSum[0] + tMaterialVO.getRoughWeight();
                        }
                        Boolean trayStatus = TrayTypeEnum.compareTrayType(tTrayVO.getTrayCategory(), tMaterialVO.getMaterialLength(), tMaterialVO.getMaterialWidth(), tMaterialVO.getMaterialHeight());
                        if (!trayStatus) {
                            throw new ServiceException(tTrayVO.getCode() + "所选物料长宽高超出最大限制范围！");
                        }
                        categoryIds.add(tMaterialVO.getCategoryId());
                    }
                });
                if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
                    throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
                }
            }
            //更新库位【库位状态为有货】【托盘编号】
            TLocation tLocation = new TLocation();
            tLocation.setId(locationId);
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            tLocation.setPalletNum(tTrayVO == null ? "" : tTrayVO.getCode());
            locationUpdateList.add(tLocation);

            //更新载具
            if (trayId != null) {
                TTray tTrayUpdate = new TTray();
                tTrayUpdate.setId(trayId);
                tTrayUpdate.setLocationId(locationId);
                trayUpdateList.add(tTrayUpdate);
            }
        });

        //更新库位的状态
        if (!CollectionUtils.isEmpty(locationUpdateList)) {
            locationService.updateBatchById(locationUpdateList);
        }
        //更新载具
        if (!CollectionUtils.isEmpty(trayUpdateList)) {
            trayService.updateBatchById(trayUpdateList);
        }
        //入库
        List<Long> originIds = taskInList.stream().map(TTaskIn::getId).collect(Collectors.toList());
        List<TTaskInVO> tTaskInList = taskInService.selectTTaskInInfoByIds(originIds);
        inStockComplete(tTaskInList);
        //更改主单状态
        //查询入库单详情状态，更改主表状态
        if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())
                || Constants.INOUT_DELIVERY_MODULE_FAST.equals(oldDelivery.getDeliveryModule())) {
            String status = advanceDeliveryService.getDetailCountStatus(tAdvanceDeliveryDetailDTO.getId(), 4);
            if (StringUtils.isNotEmpty(status)) {
                TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
                updateDelivery.setId(tAdvanceDeliveryDetailDTO.getId());
                updateDelivery.setCompleteState(status);
                updateDelivery.setStatus(status);
                if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())) {
                    //xwk-快捷入库
                    updateDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_FAST);
                }
                advanceDeliveryService.updateById(updateDelivery);
            }
        } else {
            String status = advanceDeliveryService.getDetailCountStatus(tAdvanceDeliveryDetailDTO.getId(), 3);
            if (StringUtils.isNotEmpty(status)) {
                TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
                updateDelivery.setId(tAdvanceDeliveryDetailDTO.getId());
                updateDelivery.setCompleteState(status);
                updateDelivery.setStatus(status);
                advanceDeliveryService.updateById(updateDelivery);
                if (Constants.INOUT_STATUS_COMPLETE_END.equals(status)) {
                    //全部完成，根据类型，更新调拨单
                    advanceDeliveryService.updateAllotByDeliveryId(tAdvanceDeliveryDetailDTO.getId());
                }
            }
        }
        return AjaxResult.success();
    }


    @Override
    public List<TAdvanceDeliveryDetailVO> getRegistrationList(Long deliveryId) {
        return tAdvanceDeliveryDetailMapper.getRegistrationList(deliveryId);
    }


    /**
     * 入库单检测失败
     * @param dtoList
     * @return
     */
    @Transactional
    @Override
    public AjaxResult checkMaterial(List<TAdvanceDeliveryDetailDTO> dtoList) {

        List<TRejectionDetail> rejectionDetailList = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            TAdvanceDeliveryDetailDTO dto = dtoList.get(i);
            if (dto.getId() == null){
                return AjaxResult.error("参数不可为空");
            }

            TAdvanceDeliveryDetail detail = this.getById(dto.getId());
            if (detail == null){
                return AjaxResult.error("数据不存在");
            }

            this.update(new TAdvanceDeliveryDetail(),
                    new UpdateWrapper<TAdvanceDeliveryDetail>()
                            .eq("id",dto.getId())
                            .set("detection_count",detail.getRegistrationCount().subtract(dto.getFailCount()))
                            .set("detection_fail_type",dto.getDetectionFailType())
                            .set("detection_fail_remark",dto.getDetectionFailRemark())
                            .set("detection_fail_status","1"));


            //生成拒收
            TRejectionDetail tRejectionDetail = new TRejectionDetail();
            tRejectionDetail.setMaterialId(detail.getMaterialId());//物料id
            tRejectionDetail.setAdvanceDeliveryDetailId(detail.getId());//入库单详情id
            tRejectionDetail.setAdvanceDeliveryId(detail.getAdvanceDeliveryId());//入库单id
            tRejectionDetail.setRejectionCount(dto.getFailCount());//拒收数量
            tRejectionDetail.setRejectionFailType(dto.getDetectionFailType());//拒收类型
            tRejectionDetail.setRejectionFailRemark(dto.getDetectionFailRemark());//拒收备注
            tRejectionDetail.setBatchCode(detail.getBatchCode());//批次号
            tRejectionDetail.setRemark(dto.getDetectionFailRemark());//备注
            rejectionDetailList.add(tRejectionDetail);
        }

        //拒收数据
        if (CollectionUtils.isNotEmpty(rejectionDetailList)) {
            rejectionDetailService.saveBatch(rejectionDetailList);
        }

        return AjaxResult.success();
    }

    /**
     * 齐套入库存
     */
    public void inStockComplete(List<TTaskInVO> tTaskInList) {
        //todo wxr 处理库存记录 物料+库位+批次号
        tTaskInList.forEach(e -> {
            Long locationId = e.getLocationId();
            TStockDetail tStockDetail = new TStockDetail();
            tStockDetail.setMaterialId(e.getMaterialId());
            tStockDetail.setType(Constants.WCS_TASK_TYPE_IN);
            tStockDetail.setLocationId(locationId);
            tStockDetail.setOriginCode(e.getOriginCode());
            tStockDetail.setOriginId(e.getAdvanceRegistrationId());//原单id,目前存的task_in_id
            tStockDetail.setStatus("0");
            tStockDetail.setBatchCode(e.getBatchCode());
            tStockDetail.setCurrentCount(e.getActualCount());

            //查询库存之前的数量
            TStockDetail stockDetailDTO = new TStockDetail();
            stockDetailDTO.setLocationId(locationId);
            stockDetailDTO.setMaterialId(e.getMaterialId());
            stockDetailDTO.setBatchCode(e.getBatchCode());
            BigDecimal existCount = tStockDetailMapper.selectTStockDetailCountParam(stockDetailDTO);
            if (existCount == null) {
                tStockDetail.setBeforeCount(BigDecimal.ZERO);// 操作前数量
                tStockDetail.setCurrentCount(e.getActualCount());// 操作后当前数量
            } else {
                tStockDetail.setBeforeCount(existCount);// 操作前数量
                tStockDetail.setCurrentCount(existCount.add(e.getActualCount()));// 操作后当前数量
            }
            tStockDetailMapper.insert(tStockDetail);
            //一个物料一个托盘一条
            TStock model = stockService.getOne(new LambdaQueryWrapper<TStock>()
                    .eq(TStock::getLocationId, locationId)
                    .eq(TStock::getMaterialId, e.getMaterialId())
                    .eq(TStock::getBatchCode, e.getBatchCode())
                    .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));

            //一个物料一条
            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                    .eq(TStockMain::getMaterialId, e.getMaterialId())
                    .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));

            // 更新库存数据
            TStock stockUpdate = new TStock();

            // 更新库存总数据
            TStockMain stockMainUpdate = new TStockMain();

            // 如果同托盘下有数据，则更新库存
            if (ObjectUtils.isNotNull(model)) {

                // 更新库存数据
                stockUpdate.setId(model.getId());
                stockUpdate.setCount(model.getCount().add(e.getActualCount()));
                stockUpdate.setAvailableCount(model.getAvailableCount().add(e.getActualCount()));

                // 更新库存总数据
                stockMainUpdate.setId(stockMain.getId());
                stockMainUpdate.setLibraryCount(stockMain.getLibraryCount().add(e.getActualCount()));
                stockMainUpdate.setAvailableCount(stockMain.getAvailableCount().add(e.getActualCount()));

                stockService.updateById(stockUpdate);
                stockMainService.updateById(stockMainUpdate);
            } else {
                model = new TStock();
                model.setAreaId(e.getAreaId());
                // 可用数量
                model.setAvailableCount(e.getActualCount());
                // 开始时间
                model.setBeginDate(new Date());
                model.setLocationId(locationId);
                model.setMaterialId(e.getMaterialId());
                model.setBatchCode(e.getBatchCode());
                model.setProducedDate(e.getProducedDate());
                model.setCount(e.getActualCount());
                model.setAvailableCount(e.getActualCount());
                model.setTrayId(e.getTrayId());
//                model.setOriginType(Constants.WCS_TASK_TYPE_IN);
                model.setStatus("0");
                model.setIsFreeze("0");
                if (ObjectUtils.isNotNull(stockMain)) {
                    // 更新库存总数据
                    stockMainUpdate.setId(stockMain.getId());
                    stockMainUpdate.setLibraryCount(stockMain.getLibraryCount().add(model.getCount()));
                    stockMainUpdate.setAvailableCount(stockMain.getAvailableCount().add(model.getCount()));
                    stockMainService.updateById(stockMainUpdate);
                } else {
                    stockMain = new TStockMain();
                    stockMain.setMaterialId(e.getMaterialId());
                    stockMain.setLibraryCount(e.getActualCount());
                    stockMain.setAvailableCount(e.getActualCount());
                    stockMainService.save(stockMain);
                }
                stockService.save(model);
            }
        });
    }


//    /**
//     * 快捷入库-收货上架
//     * @param tAdvanceDeliveryDetailDTO
//     * @return
//     */
//    @Override
//    @Transactional
//    public AjaxResult putawayFask(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
//        TAdvanceDelivery oldDelivery = advanceDeliveryService.getById(tAdvanceDeliveryDetailDTO.getId());
//        if (Constants.INOUT_STATUS_COMPLETE_END.equals(oldDelivery.getStatus())) {
//            return AjaxResult.error("当前单据状态已完成，不可再次上架");
//        }
//
//        //去除为0的数据
//        List<TTaskIn> taskInList = tAdvanceDeliveryDetailDTO.getTaskInList();
//        taskInList = taskInList.stream().filter(e -> e.getActualCount() != null && e.getActualCount() > 0).collect(Collectors.toList());
//        if (CollectionUtils.isEmpty(taskInList)) {
//            return AjaxResult.error("上架数量不可为0");
//        }
//
//        //通过库位ids获取对应的库位信息
//        List<Long> locations = taskInList.stream().map(TTaskIn::getLocationId).distinct().collect(Collectors.toList());
//        Map<Long, TLocationVO> locationMap = locationService.getLocationByIds(locations);
//
//        //通过托盘ids获取对应的托盘信息
//        List<Long> trays = taskInList.stream().filter(e -> e.getTrayId() != null).map(TTaskIn::getTrayId).distinct().collect(Collectors.toList());
//        Map<Long, TTray> trayCodeMap = trayService.getTrayByIds(trays);
//
//        //通过物料ids获取对应的物料信息
//        List<Long> materials = taskInList.stream().map(TTaskIn::getMaterialId).distinct().collect(Collectors.toList());
//        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
//        //通过登记ids获取对应的登记信息
//        List<Long> advanceRegistrationIds = taskInList.stream().map(TTaskIn::getAdvanceRegistrationId).distinct().collect(Collectors.toList());
//        List<TAdvanceDeliveryDetail> advanceDeliveryDetailList = this.getListByIds(advanceRegistrationIds);
//        if (CollectionUtils.isEmpty(advanceDeliveryDetailList)) {
//            return AjaxResult.error("未查询到对应入库单");
//        }
//
//        Map<Long, TAdvanceDeliveryDetail> registrationMap = advanceDeliveryDetailList.stream().collect(Collectors.toMap(TAdvanceDeliveryDetail::getId, Function.identity()));
//
//        //更新登记信息列表
//        List<TAdvanceDeliveryDetail> detailUpdateList = new ArrayList<>();
//        //对每个登记数量进行判断
//        Map<Long, List<TTaskIn>> registerGroupMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getAdvanceRegistrationId));
//        registrationMap.forEach((key1, deliveryDetail) -> {
//            List<TTaskIn> taskIns = registerGroupMap.get(key1);
//            Long actualCount = taskIns.stream().mapToLong(TTaskIn::getActualCount).sum();//实际数量
//            Long residueCount = 0L;//剩余数量
//            if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())
//                    || Constants.INOUT_DELIVERY_MODULE_FAST.equals(oldDelivery.getDeliveryModule())) {
//                residueCount = deliveryDetail.getPredictCount() - deliveryDetail.getPutawayCount();//剩余数量
//            } else {
//                residueCount = deliveryDetail.getRegistrationCount() - deliveryDetail.getPutawayCount();//剩余数量
//            }
//            if (actualCount > residueCount) {
//                throw new ServiceException("物料总上架数量不可超出预估上架数量");
//            }
//            TAdvanceDeliveryDetail updatyeDO = new TAdvanceDeliveryDetail();
//            updatyeDO.setId(key1);
//            updatyeDO.setPutawayCount(actualCount + deliveryDetail.getPutawayCount());
////            updatyeDO.setReceiveCount(actualCount + deliveryDetail.getReceiveCount());
//            //快捷入库
//            updatyeDO.setRegistrationCount(actualCount + deliveryDetail.getRegistrationCount());
//            updatyeDO.setDetectionCount(actualCount + deliveryDetail.getRegistrationCount());
//            if (residueCount == 0) {
//                updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_END);
//            } else {
//                updatyeDO.setNextFlag(Constants.INOUT_NEXTFLAG_EXE_PART);
//            }
//            detailUpdateList.add(updatyeDO);
//        });
//
//        //更新登记表转化状态
//        this.updateBatchById(detailUpdateList);
//        //保存taskIn表
//        taskInService.saveBatch(taskInList);
//
//        //库位更新
//        List<TLocation> locationUpdateList = new ArrayList<>();
//        //载具列表
//        List<TTray> trayUpdateList = new ArrayList<>();
//        //任务详情列表
//        List<TTaskWcsDetail> saveDetailList = new ArrayList<>();
//        //更新taskin
//        List<TTaskIn> updatetTaskInList = new ArrayList<>();
//        //托盘状态更新
//        //生成wcs任务
//        Map<Long, List<TTaskIn>> trayInfoMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getLocationId));
//        trayInfoMap.forEach((key, value) -> {
//            List<TTaskIn> taskIns = value;
//            Long trayId = taskIns.get(0).getTrayId();
//            Long locationId = key;
//            TLocationVO tLocationVO = locationMap.get(locationId);
//            if (tLocationVO == null) {
//                throw new ServiceException("未获取到对应库位信息!");
//            }
//            if (!Constants.LOCATION_GOODS_ALLOCATION_STATUS_1.equals(tLocationVO.getGoodsAllocationStatus())) {
//                throw new ServiceException(tLocationVO.getName() + "此库位不可使用!");
//            }
//            if (trayId != null) {
//                if (tLocationVO.getTrayId() != null && !tLocationVO.getTrayId().equals(trayId)) {
//                    throw new ServiceException(tLocationVO.getName() + "此库位已绑定其他载具");
//                }
//            } else {
//                trayId = tLocationVO.getTrayId();
//            }
//            TTray tTrayVO = trayCodeMap.get(trayId);
//            if (tTrayVO != null) {
//                //生成上架任务，判断托盘承载重量
//                final Double[] materialSum = {0d};
//                Set<Long> categoryIds = new HashSet();
//                value.forEach(e -> {
//                    TMaterial tMaterialVO = materialMap.get(e.getMaterialId());
//                    if (tMaterialVO != null) {
//                        if (tMaterialVO.getRoughWeight() != null) {
//                            materialSum[0] = materialSum[0] + tMaterialVO.getRoughWeight();
//                        }
//                        Boolean trayStatus = TrayTypeEnum.compareTrayType(tTrayVO.getTrayCategory(), tMaterialVO.getMaterialLength(), tMaterialVO.getMaterialWidth(), tMaterialVO.getMaterialHeight());
//                        if (!trayStatus) {
//                            throw new ServiceException(tTrayVO.getCode() + "所选物料长宽高超出最大限制范围！");
//                        }
//                        categoryIds.add(tMaterialVO.getCategoryId());
//                    }
//                });
//                if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
//                    throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
//                }
//            }
//            //更新库位【库位状态为标记入库】【托盘编号】
//            TLocation tLocation = new TLocation();
//            tLocation.setId(locationId);
////            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
//            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
//            tLocation.setPalletNum(tTrayVO == null ? "" : tTrayVO.getCode());
//            locationUpdateList.add(tLocation);
//
//            //更新载具
////            if (trayId != null) {
////                TTray tTrayUpdate = new TTray();
////                tTrayUpdate.setId(trayId);
////                tTrayUpdate.setLocationId(locationId);
////                trayUpdateList.add(tTrayUpdate);
////            }
//            TTaskWcs tTaskWcs = new TTaskWcs();
//            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_IN);
//            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
//            tTaskWcs.setTrayId(key);
//            tTaskWcs.setTrayCode(tTrayVO.getCode());
//            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
//            tTaskWcs.setLocationId(locationId);//推荐库位
//            taskWcsService.save(tTaskWcs);
//            taskIns.forEach(e -> {
//                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
//                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
//                tTaskWcsDetail.setOriginId(e.getId());
//                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
//                saveDetailList.add(tTaskWcsDetail);
//                TTaskIn taskInUpdate = new TTaskIn();
//                taskInUpdate.setId(e.getId());
//                taskInUpdate.setLocationId(locationId);
//                updatetTaskInList.add(taskInUpdate);
//            });
//
//        });
//
//        //更新库位的状态
//        if (!CollectionUtils.isEmpty(locationUpdateList)) {
//            locationService.updateBatchById(locationUpdateList);
//        }
//        //更新载具
//        if (!CollectionUtils.isEmpty(trayUpdateList)) {
//            trayService.updateBatchById(trayUpdateList);
//        }
//        //保存wcs表
//        if (!CollectionUtils.isEmpty(saveDetailList)) {
//            taskWcsDetailService.saveBatch(saveDetailList);
//        }
//        //更新taskin表
//        if (!CollectionUtils.isEmpty(updatetTaskInList)) {
//            taskInService.updateBatchById(updatetTaskInList);
//        }
//        //更改主单状态
//        //查询入库单详情状态，更改主表状态
//        String status = advanceDeliveryService.getDetailCountStatus(tAdvanceDeliveryDetailDTO.getId(), 4);
//        if (StringUtils.isNotEmpty(status)) {
//            TAdvanceDelivery updateDelivery = new TAdvanceDelivery();
//            updateDelivery.setId(tAdvanceDeliveryDetailDTO.getId());
//            updateDelivery.setCompleteState(status);
//            updateDelivery.setStatus(status);
//            if (Constants.INOUT_DELIVERY_MODULE_ORDER.equals(oldDelivery.getDeliveryModule())) {
//                updateDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_FAST);
//            }
//            advanceDeliveryService.updateById(updateDelivery);
//            if(Constants.INOUT_STATUS_COMPLETE_END.equals(status)){
//                //全部完成，根据类型，更新调拨单
//                advanceDeliveryService.updateAllotByDeliveryId(tAdvanceDeliveryDetailDTO.getId());
//            }
//        }
//        return AjaxResult.success();
//    }
}
