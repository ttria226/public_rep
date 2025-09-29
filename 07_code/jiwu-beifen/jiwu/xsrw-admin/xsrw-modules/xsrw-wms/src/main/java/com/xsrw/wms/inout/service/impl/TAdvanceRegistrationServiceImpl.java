package com.xsrw.wms.inout.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.inout.domain.TTaskIn;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceRegistrationDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceRegistrationVO;
import com.xsrw.wms.inout.service.*;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceRegistrationMapper;
import com.xsrw.wms.inout.domain.TAdvanceRegistration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库登记Service业务层处理
 *
 * @author wxr
 * @date 2023-05-09
 */
@Service
public class TAdvanceRegistrationServiceImpl extends ServiceImpl<TAdvanceRegistrationMapper, TAdvanceRegistration> implements ITAdvanceRegistrationService {
    @Autowired
    private TAdvanceRegistrationMapper tAdvanceRegistrationMapper;
    @Autowired
    private ITTaskInService taskInService;
    @Autowired
    private ITTaskWcsService taskWcsService;
    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;
    @Autowired
    private ITTrayService trayService;
    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private ITPutAwayRuleService putAwayRuleService;
    @Autowired
    private ITStockService stockService;
    @Autowired
    private ITStockDetailService stockDetailService;
    @Autowired
    private ITStockMainService stockMainService;
    @Autowired
    private ITLocationService locationService;
    @Autowired
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    private TTrayMapper tTrayMapper;

    @Autowired
    private ITMaterialService materialService;


    /**
     * 查询入库登记列表
     *
     * @param tAdvanceRegistration 入库登记
     * @return 入库登记
     */
    @Override
    public List<TAdvanceRegistrationVO> selectTAdvanceRegistrationList(TAdvanceRegistrationDTO tAdvanceRegistration) {
        return tAdvanceRegistrationMapper.selectTAdvanceRegistrationList(tAdvanceRegistration);
    }

    /**
     * 查询入库登记
     *
     * @param id 入库登记主键
     * @return 入库登记
     */
    @Override
    public TAdvanceRegistrationVO selectTAdvanceRegistrationById(Long id) {
        TAdvanceRegistrationVO tAdvanceRegistrationVO = tAdvanceRegistrationMapper.selectInfoById(id);
        tAdvanceRegistrationVO.setTaskInList(taskInService.selectTTaskInInfoByRegistrationId(id));
        return tAdvanceRegistrationVO;
    }

    /**
     * 新增入库登记
     *
     * @param tAdvanceRegistration 入库登记
     * @return 结果
     */
    @Override
    public int insertTAdvanceRegistration(TAdvanceRegistration tAdvanceRegistration) {
        return tAdvanceRegistrationMapper.insert(tAdvanceRegistration);
    }

    /**
     * 修改入库登记
     *
     * @param tAdvanceRegistration 入库登记
     * @return 结果
     */
    @Override
    public int updateTAdvanceRegistration(TAdvanceRegistration tAdvanceRegistration) {
        return tAdvanceRegistrationMapper.updateById(tAdvanceRegistration);
    }


    /**
     * 批量删除入库登记
     *
     * @param ids 需要删除的入库登记主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceRegistrationByIds(Long[] ids) {
        return tAdvanceRegistrationMapper.deleteTAdvanceRegistrationByIds(ids);
    }

    /**
     * 删除入库登记信息
     *
     * @param id 入库登记主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceRegistrationById(Long id) {
        return tAdvanceRegistrationMapper.deleteTAdvanceRegistrationById(id);
    }

    /**
     * 生成上架任务
     */
    @Override
    @Transactional
    public AjaxResult putaway(TAdvanceRegistrationDTO tAdvanceRegistration) {
        //去除为0的数据
        List<TTaskIn> taskInList = tAdvanceRegistration.getTaskInList();
        taskInList = taskInList.stream().filter(e -> e.getActualCount() != null && e.getActualCount() > 0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskInList)) {
            return AjaxResult.error("上架数量不可为0");
        }
        //通过托盘ids获取对应的托盘信息
        List<Long> trays = taskInList.stream().map(TTaskIn::getTrayId).distinct().collect(Collectors.toList());
        Map<Long, TTray> trayCodeMap = trayService.getTrayByIds(trays);
        //通过物料ids获取对应的物料信息
        List<Long> materials = taskInList.stream().map(TTaskIn::getMaterialId).distinct().collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
        //通过登记ids获取对应的登记信息
        List<Long> advanceRegistrationIds = taskInList.stream().map(TTaskIn::getAdvanceRegistrationId).distinct().collect(Collectors.toList());
        Map<Long, TAdvanceRegistration> registrationMap = new HashMap<>();
        List<TAdvanceRegistration> registrationList = this.getListByIds(advanceRegistrationIds);
        if (!CollectionUtils.isEmpty(registrationList)) {
            registrationMap = registrationList.stream().collect(Collectors.toMap(TAdvanceRegistration::getId, Function.identity()));
        }
        //更新登记信息列表
        List<TAdvanceRegistration> registerUpdateList = new ArrayList<>();
        //对每个登记数量进行判断
        Map<Long, List<TTaskIn>> registerGroupMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getAdvanceRegistrationId));
        registrationMap.forEach((key1, value1) -> {
            List<TTaskIn> taskIns = registerGroupMap.get(key1);
            Long actualCount = taskIns.stream().mapToLong(TTaskIn::getActualCount).sum();//实际数量
            Long residueCount = value1.getPredictCount() - value1.getActualCount();//剩余数量
            if (actualCount > residueCount) {
                throw new ServiceException("物料总上架数量不可超出预估上架数量");
            }
            TAdvanceRegistration updatyeDO = new TAdvanceRegistration();
            updatyeDO.setId(key1);
            updatyeDO.setActualCount(actualCount + value1.getActualCount());
            updatyeDO.setNextFlag(Constants.YES);
            registerUpdateList.add(updatyeDO);
        });

        //更新登记表转化状态
        this.updateBatchById(registerUpdateList);
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
        //生成wcs任务
        Map<Long, List<TTaskIn>> trayInfoMap = taskInList.stream().collect(Collectors.groupingBy(TTaskIn::getTrayId));
        trayInfoMap.forEach((key, value) -> {
            List<TTaskIn> taskIns = value;
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_IN);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
            tTaskWcs.setTrayId(key);
            TTray tTrayVO = trayCodeMap.get(key);
            if (tTrayVO == null) {
                throw new ServiceException("未获取到对应托盘信息!");
            }
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
            tTaskWcs.setTrayCode(tTrayVO.getCode());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            Long locationId = putAwayRuleService.recommendedLocation(removeLocations, tTrayVO.getId(), categoryIds);
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
                tLocation.setPalletNum(String.valueOf(key));
                locationUpdateList.add(tLocation);
            }
            taskWcsService.save(tTaskWcs);
            taskIns.forEach(e -> {
                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
                tTaskWcsDetail.setOriginId(e.getId());
                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
                saveDetailList.add(tTaskWcsDetail);
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
        return AjaxResult.success();
    }

    /**
     * 根据ids获取列表
     *
     * @param advanceRegistrationIds
     * @return
     */
    @Override
    public List<TAdvanceRegistration> getListByIds(List<Long> advanceRegistrationIds) {
        QueryWrapper<TAdvanceRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("id", advanceRegistrationIds);
        return tAdvanceRegistrationMapper.selectList(queryWrapper);
    }

    /**
     * 地堆上架
     *
     * @param tAdvanceRegistrationDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult floorStocking(TAdvanceRegistrationDTO tAdvanceRegistrationDTO) {
        TAdvanceRegistrationVO oldDO = tAdvanceRegistrationMapper.selectInfoById(tAdvanceRegistrationDTO.getId());
        if (oldDO == null) {
            return AjaxResult.error("未查询到数据");
        }
        if (Constants.YES.equals(oldDO.getNextFlag())) {
            return AjaxResult.error("当前状态下不可地堆上架");
        }
        Long trayId = null;
        if (StringUtils.isNotBlank(tAdvanceRegistrationDTO.getTrayCode())) {
            TTray tTray = trayService.selectTTrayByCode(tAdvanceRegistrationDTO.getTrayCode());
            if (tTray == null) {
                return AjaxResult.error("未获取到对应的载具信息");
            }
            if (Constants.TRAY_STATUS_FULL.equals(tTray.getStatus())) {
                return AjaxResult.error("当前载具状态不可用");
            }
            trayId = tTray.getId();
        }

        //更新状态
        TAdvanceRegistration tAdvanceRegistration = new TAdvanceRegistrationDTO();
        tAdvanceRegistration.setId(tAdvanceRegistrationDTO.getId());
        tAdvanceRegistration.setStatus(Constants.INOUT_STATUS_END);
        tAdvanceRegistration.setActualCount(oldDO.getPredictCount());
        tAdvanceRegistrationMapper.updateById(tAdvanceRegistration);

        //更新库存
        this.inStockByMaterial(oldDO, trayId, tAdvanceRegistrationDTO.getLocationId());
        //更新载具状态
        TTray tTray = new TTray();
        tTray.setId(trayId);
        tTray.setLocationId(tAdvanceRegistrationDTO.getLocationId());
        tTrayMapper.updateById(tTray);
        //更新库位状态
        TLocation tLocation = new TLocation();
        tLocation.setId(tAdvanceRegistrationDTO.getLocationId());
        tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
        tLocation.setPalletNum(tAdvanceRegistrationDTO.getTrayCode());
        locationService.updateById(tLocation);
        return AjaxResult.success();
    }

    /**
     * pda上架
     *
     * @param tAdvanceRegistrationApiDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult putawayTask(TAdvanceRegistrationApiDTO tAdvanceRegistrationApiDTO) {
        TTray tTrayVO = trayService.selectTTrayByCode(tAdvanceRegistrationApiDTO.getTrayCode());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到当前托盘信息");
        }
        if (Constants.TRAY_STATUS_FULL.equals(tTrayVO.getStatus())) {
            return AjaxResult.error("当前托盘已满，请选择其他托盘");
        }

        List<TTaskIn> taskIns = new ArrayList<>();
        TTaskWcs tTaskWcs = new TTaskWcs();
        List<TTaskWcsDetail> saveDetailList = new ArrayList<>();
        List<TAdvanceDeliveryApiDTO> materialList = tAdvanceRegistrationApiDTO.getMaterialList();
        List<Long> materials = materialList.stream().map(TAdvanceDeliveryApiDTO::getMaterialId).collect(Collectors.toList());
        Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materials);
        Set<Long> materialCategorys = new HashSet<>();
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
            }
        });
        if (tTrayVO.getMaxWeight() != null && materialSum[0] > tTrayVO.getMaxWeight()) {
            throw new ServiceException(tTrayVO.getCode() + "托盘物料重量超出最大承重范围！");
        }
        for (TAdvanceDeliveryApiDTO tAdvanceDeliveryApiDTO : materialList) {
            Long count = tAdvanceDeliveryApiDTO.getCount();
            if (count <= 0) {
                break;
            }
            QueryWrapper<TAdvanceRegistration> registrationQw = new QueryWrapper<>();
            registrationQw.eq("del_flag", Constants.DEL_FLAG_NO);
            registrationQw.eq("material_id", tAdvanceDeliveryApiDTO.getMaterialId());
            registrationQw.eq("batch_code", tAdvanceDeliveryApiDTO.getBatchCode());
            registrationQw.ne("status", Constants.INOUT_STATUS_END);
            registrationQw.orderByAsc("create_time");
            List<TAdvanceRegistration> registrationList = tAdvanceRegistrationMapper.selectList(registrationQw);
            if (!CollectionUtils.isEmpty(registrationList)) {
                Long predictCountSum = registrationList.stream().mapToLong(TAdvanceRegistration::getPredictCount).sum();
                Long actualCountSum = registrationList.stream().mapToLong(TAdvanceRegistration::getActualCount).sum();
                Long residueCountSum = predictCountSum - actualCountSum;
                if (tAdvanceDeliveryApiDTO.getCount() > residueCountSum) {
                    throw new ServiceException("超出可上架数量，不可上架");
                }
                for (TAdvanceRegistration tAdvanceRegistration : registrationList) {
                    if (count <= 0) {
                        break;
                    }
                    //更新登记表状态
                    TAdvanceRegistration updatyeDO = new TAdvanceRegistration();
                    updatyeDO.setId(tAdvanceRegistration.getId());
                    Long residueCount = tAdvanceRegistration.getPredictCount() - tAdvanceRegistration.getActualCount();//现单剩余数量
                    Long actualCount = 0L;
                    Long taskInActualCount = 0L;
                    if (count >= residueCount) {
                        actualCount = tAdvanceRegistration.getPredictCount();
                        taskInActualCount = tAdvanceRegistration.getPredictCount();
                        count = count - residueCount;
                    } else {
                        actualCount = tAdvanceRegistration.getActualCount() + count;
                        taskInActualCount = count;
                        count = 0L;
                    }
                    updatyeDO.setActualCount(actualCount);
                    updatyeDO.setNextFlag(Constants.YES);
                    tAdvanceRegistrationMapper.updateById(updatyeDO);

                    //taskin表
                    TTaskIn taskIn = new TTaskIn();
                    taskIn.setAdvanceRegistrationId(tAdvanceRegistration.getId());
                    taskIn.setMaterialId(tAdvanceDeliveryApiDTO.getMaterialId());
                    taskIn.setTrayId(tTrayVO.getId());
                    taskIn.setActualCount(taskInActualCount);
                    taskIn.setBatchCode(tAdvanceRegistration.getBatchCode());
                    taskIns.add(taskIn);
                }
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
            Long locationId = putAwayRuleService.recommendedLocation(null, tTrayVO.getId(), materialCategorys);
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

            taskIns.forEach(e -> {
                TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
                tTaskWcsDetail.setTaskId(tTaskWcs.getId());
                tTaskWcsDetail.setOriginId(e.getId());
                tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_IN);
                saveDetailList.add(tTaskWcsDetail);
            });
            //保存wcs详情表
            taskWcsDetailService.saveBatch(saveDetailList);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 通过物料ids获取对应的推荐载具类型
     *
     * @param materialIds
     * @return
     */
    @Override
    public AjaxResult getTrayTypeByMaterials(Long[] materialIds) {
        QueryWrapper<TMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("id", materialIds);
        List<TMaterial> tMaterials = materialService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(tMaterials)) {
            Integer trayType = 0;
            for (TMaterial e : tMaterials) {
                Integer trayType1 = TrayTypeEnum.compareTrayType(e.getMaterialLength(), e.getMaterialWidth(), e.getMaterialHeight());
                if (trayType1 > trayType) {
                    trayType = trayType1;
                }
            }
            if (trayType > 0) {
                return AjaxResult.success(trayType);
            }
        }
        return AjaxResult.error("无可推荐载具，超出载具范围限制");
    }

    /**
     * 入库存
     */
    public void inStockByMaterial(TAdvanceRegistrationVO tAdvanceRegistration, Long trayId, Long locationId) {

        Long count = tAdvanceRegistration.getPredictCount();
        //库存详情
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setMaterialId(tAdvanceRegistration.getMaterialId());
        tStockDetail.setType(Constants.WCS_TASK_TYPE_IN);
        tStockDetail.setLocationId(locationId);
        tStockDetail.setOriginCode(tAdvanceRegistration.getAdvanceDeliveryCode());
        tStockDetail.setOriginId(tAdvanceRegistration.getId());//原单id,目前存的task_in_id
        tStockDetail.setStatus("0");
        tStockDetail.setBatchCode(tAdvanceRegistration.getBatchCode());
        tStockDetail.setCurrentCount(count);
        //查询库存之前的数量
        TStockDetail stockDetailDTO = new TStockDetail();
        stockDetailDTO.setLocationId(locationId);
        stockDetailDTO.setMaterialId(tAdvanceRegistration.getMaterialId());
        Long existCount = tStockDetailMapper.selectTStockDetailCountParam(stockDetailDTO);
        if (existCount == null) {
            tStockDetail.setBeforeCount(0L);// 操作前数量
            tStockDetail.setCurrentCount(count);// 操作后当前数量
        } else {
            tStockDetail.setBeforeCount(existCount);// 操作前数量
            tStockDetail.setCurrentCount(existCount + count);// 操作后当前数量
        }
        tStockDetailMapper.insert(tStockDetail);

        //一个物料一个托盘一条
        TStock model = stockService.getOne(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getLocationId, locationId)
                .eq(TStock::getMaterialId, tAdvanceRegistration.getMaterialId())
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));

        //一个物料一条
        TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                .eq(TStockMain::getMaterialId, tAdvanceRegistration.getMaterialId())
                .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));

        // 更新库存数据
        TStock stockUpdate = new TStock();

        // 更新库存总数据
        TStockMain stockMainUpdate = new TStockMain();

        // 如果同托盘下有数据，则更新库存
        if (ObjectUtils.isNotNull(model)) {

            // 更新库存数据
            stockUpdate.setId(model.getId());
            stockUpdate.setCount(model.getCount() + count);
            stockUpdate.setAvailableCount(model.getAvailableCount() + count);

            // 更新库存总数据
            stockMainUpdate.setId(stockMain.getId());
            stockMainUpdate.setLibraryCount(stockMain.getLibraryCount() + count.intValue());
            stockMainUpdate.setAvailableCount(stockMain.getAvailableCount() + count.intValue());

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
            model.setMaterialId(tAdvanceRegistration.getMaterialId());
            model.setBatchCode(tAdvanceRegistration.getBatchCode());
            model.setCount(count);
            model.setAvailableCount(count);
            model.setTrayId(trayId);
//            model.setOriginType(Constants.WCS_TASK_TYPE_IN);
            model.setStatus("0");
            model.setIsFreeze("0");

            if (ObjectUtils.isNotNull(stockMain)) {
                // 更新库存总数据
                stockMainUpdate.setId(stockMain.getId());
                stockMainUpdate.setLibraryCount(stockMain.getLibraryCount() + model.getCount().intValue());
                stockMainUpdate.setAvailableCount(stockMain.getAvailableCount() + model.getCount().intValue());
                stockMainService.updateById(stockMainUpdate);
            } else {
                stockMain = new TStockMain();
                stockMain.setMaterialId(tAdvanceRegistration.getMaterialId());
                stockMain.setLibraryCount(count);
                stockMain.setAvailableCount(count);
                stockMainService.save(stockMain);
            }
            stockService.save(model);
        }
    }


}
