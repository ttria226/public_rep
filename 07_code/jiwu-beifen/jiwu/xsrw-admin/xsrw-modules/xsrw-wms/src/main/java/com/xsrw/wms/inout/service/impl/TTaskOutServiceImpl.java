package com.xsrw.wms.inout.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.uuid.UUID;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.dto.TTaskOutDTO;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.inout.mapper.TAllotMapper;
import com.xsrw.wms.inout.mapper.TOutDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.inout.service.*;
import com.xsrw.wms.inout.strategy.RecommendedLocationUtil;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.dto.TStockInDTO;
import com.xsrw.wms.stock.domain.vo.StockVo;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import com.xsrw.wms.web.domain.WcsOrderEntity;
import com.xsrw.wms.web.domain.WcsResultEntity;
import com.xsrw.wms.web.domain.WcsSendEntity;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsMoveUtil;
import com.xsrw.wms.web.util.WcsReportUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 出库任务详情Service业务层处理
 *
 * @author zyq
 * @date 2023-05-08
 */
@Service
public class TTaskOutServiceImpl extends ServiceImpl<TTaskOutMapper, TTaskOut> implements ITTaskOutService {
    @Autowired
    private TTaskOutMapper tTaskOutMapper;

    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;

    @Autowired
    private ITOutDeliveryDetailService outDeliveryDetailService;

    @Autowired
    private ITOutDeliveryService outDeliveryService;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITTaskWcsService taskWcsService;

    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private ITStockService stockService;
    @Autowired
    ITLocationService locationService;

    @Autowired
    ITMaterialService materialService;

    @Autowired
    ITMaterialDetailService materialDetailService;

    @Autowired
    ITAreaService areaService;

    @Autowired
    private ITStockMainService stockMainService;

    @Autowired
    private ITStockDetailService stockDetailService;

    @Autowired
    private ITOutDeliverySamllRecordService outDeliverySamllRecordService;

    @Autowired
    private ITOutStrategyService outStrategyService;

    @Autowired
    private ITReservoirService reservoirService;

    @Autowired
    private ITUnitService unitService;

    @Autowired
    private ITUnitConfigService unitConfigService;

    @Autowired
    private ITPutAwayRuleService putAwayRuleService;
    @Autowired
    private RecommendedLocationUtil recommendedLocationUtil;

    @Autowired
    private TAllotMapper allotMapper;

    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;

    @Autowired
    private AgvReportUtil agvReportUtil;

    @Autowired
    private WcsReportUtil wcsReportUtil;

    @Autowired
    private RedisService redisService;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;
    /**
     * 查询出库任务详情列表
     *
     * @param tTaskOut 出库任务详情
     * @return 出库任务详情
     */
    @Override
    public List<TTaskOut> selectTTaskOutList(TTaskOut tTaskOut) {

        return tTaskOutMapper.selectTTaskOutList(tTaskOut);
    }

    @Override
    public List<TTrayVO> selectTTrayList(String id, TStock tStock) {
        //拣货策略
        LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO);
        queryWrapper.eq(TOutStrategy::getFlag, 1);
        TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
        String order = "";
        if (outStrategy != null) {
            if (("先进先出").equals(outStrategy.getName())) {
                order = "a.create_time ASC";
            } else if ("后进先出".equals(outStrategy.getName())) {
                order = "a.create_time DESC";
            } else if ("批次".equals(outStrategy.getName())) {
                order = "a.batch_code asc";
            }
        } else {
            order = "a.available_count ASC";
        }
        tStock.setMaterialId(Long.valueOf(id));
        tStock.setRemark(order);
        return tTaskOutMapper.selectTTrayList(tStock);
    }


    /**
     * 执行出库--自动分配载具
     *
     * @return
     */
    @Override
    public AjaxResult voluntarily(Long outDeliveryId, Long materialId) {

        // 查询物料拣货信息
        TOutDeliveryDetail deliveryDetail = outDeliveryDetailService.getOne(
                new QueryWrapper<TOutDeliveryDetail>()
                        .eq("out_delivery_id", outDeliveryId)
                        .eq("material_id", materialId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetail == null) {
            return AjaxResult.error("单据不存在");
        }

        // 查询可拣货的库存信息
        TStock tStock = new TStock();
        tStock.setLocationType("0");
        //拣货策略
        LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO);
        queryWrapper.eq(TOutStrategy::getFlag, 1);
        TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
        String order = "";
        if (outStrategy != null) {
            if (("先进先出").equals(outStrategy.getName())) {
                order = "a.create_time ASC";
            } else if ("后进先出".equals(outStrategy.getName())) {
                order = "a.create_time DESC";
            } else if ("批次".equals(outStrategy.getName())) {
                order = "a.batch_code asc";
            }
        } else {
            order = "a.available_count ASC";
        }
        tStock.setMaterialId(Long.valueOf(materialId));
        tStock.setRemark(order);
        List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

        // 分配库存
        Long receiveCount = deliveryDetail.getPredictReceiveCount();
        if (trayList.size() == 0) {
            return AjaxResult.success(new ArrayList<>());
        } else {
            List<TTrayVO> result = new ArrayList<>();
            for (TTrayVO trayVO : trayList) {
                if (receiveCount > trayVO.getAvailableCount()) {
                    Long count = receiveCount;
                    receiveCount = receiveCount - trayVO.getAvailableCount();
                    if (receiveCount >= 0) {
                        trayVO.setPredictCount(trayVO.getAvailableCount());
                    } else {
                        trayVO.setPredictCount(count);
                    }
                    result.add(trayVO);
                } else {
                    trayVO.setPredictCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
            return AjaxResult.success(result);
        }
    }


    @Override
    public AjaxResult voluntarilyAll(Long outDeliveryId) {

        // 查询物料拣货信息
        List<TOutDeliveryDetail> deliveryDetailList = outDeliveryDetailService.list(
                new QueryWrapper<TOutDeliveryDetail>()
                        .eq("out_delivery_id", outDeliveryId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetailList.size() == 0) {
            return AjaxResult.error("单据不存在");
        }

        List<TTrayVO> result = new ArrayList<>();

        deliveryDetailList.forEach(deliveryDetail -> {

            // 查询可拣货的库存信息
            TStock tStock = new TStock();
            tStock.setLocationType("0");
            //拣货策略
            LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO);
            queryWrapper.eq(TOutStrategy::getFlag, 1);
            TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
            String order = "";
            if (outStrategy != null) {
                if (("先进先出").equals(outStrategy.getName())) {
                    order = "a.create_time ASC";
                } else if ("后进先出".equals(outStrategy.getName())) {
                    order = "a.create_time DESC";
                } else if ("批次".equals(outStrategy.getName())) {
                    order = "a.batch_code asc";
                }
            } else {
                order = "a.available_count ASC";
            }
            tStock.setMaterialId(Long.valueOf(deliveryDetail.getMaterialId()));
            tStock.setRemark(order);
            List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

            // 分配库存
            Long receiveCount = deliveryDetail.getPredictReceiveCount();

            for (TTrayVO trayVO : trayList) {
                TMaterial material = materialService.getById(trayVO.getMaterialId());
                trayVO.setMaterialName(material.getName());
                trayVO.setMaterialCode(material.getCode());

                if (receiveCount > trayVO.getAvailableCount()) {
                    Long count = receiveCount;
                    receiveCount = receiveCount - trayVO.getAvailableCount();
                    if (receiveCount >= 0) {
                        trayVO.setPredictCount(trayVO.getAvailableCount());
                    } else {
                        trayVO.setPredictCount(count);
                    }
                    result.add(trayVO);
                } else {
                    trayVO.setPredictCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
        });

        return AjaxResult.success(result);

    }


    @Override
    public AjaxResult voluntarilyAllQuick(Long outDeliveryId) {

        // 查询物料拣货信息
        List<TOutDeliveryDetail> deliveryDetailList = outDeliveryDetailService.list(
                new QueryWrapper<TOutDeliveryDetail>()
                        .eq("out_delivery_id", outDeliveryId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetailList.size() == 0) {
            return AjaxResult.error("单据不存在");
        }

        List<TTrayVO> result = new ArrayList<>();

        deliveryDetailList.forEach(deliveryDetail -> {

            // 查询可拣货的库存信息
            TStock tStock = new TStock();
            tStock.setLocationType("0");
            //拣货策略
            LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO);
            queryWrapper.eq(TOutStrategy::getFlag, 1);
            TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
            String order = "";
            if (outStrategy != null) {
                if (("先进先出").equals(outStrategy.getName())) {
                    order = "a.create_time ASC";
                } else if ("后进先出".equals(outStrategy.getName())) {
                    order = "a.create_time DESC";
                } else if ("批次".equals(outStrategy.getName())) {
                    order = "a.batch_code asc";
                }
            } else {
                order = "a.available_count ASC";
            }
            tStock.setMaterialId(Long.valueOf(deliveryDetail.getMaterialId()));
            tStock.setRemark(order);
            List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

            // 分配库存
            Long receiveCount = deliveryDetail.getPredictReceiveCount();

            for (TTrayVO trayVO : trayList) {
                if (receiveCount > trayVO.getAvailableCount()) {
                    Long count = receiveCount;
                    receiveCount = receiveCount - trayVO.getAvailableCount();
                    if (receiveCount >= 0) {
                        trayVO.setPredictCount(trayVO.getAvailableCount());
                    } else {
                        trayVO.setPredictCount(count);
                    }
                    result.add(trayVO);
                } else {
                    trayVO.setPredictCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
        });

        List<Map<String, Object>> mapList = new ArrayList<>();
        deliveryDetailList.forEach(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("outDeliveryDetailId", e.getId());

            List<TTaskOutDetailListVO> tTaskOutDetailListVOS = new ArrayList<>();
            result.forEach(tray -> {
                if (e.getMaterialId().equals(tray.getMaterialId())) {
                    TTaskOutDetailListVO vo = new TTaskOutDetailListVO();
                    vo.setStockId(tray.getStockid());
                    vo.setTrayId(tray.getId());
                    vo.setLocationId(tray.getLocationId());
                    vo.setPredictCount(tray.getPredictCount());

                    tTaskOutDetailListVOS.add(vo);
                }
            });
            map.put("tTaskOutDetailListVOS", tTaskOutDetailListVOS);
            mapList.add(map);
        });

        return AjaxResult.success(mapList);

    }


    /**
     * 地堆出库--自动分配载具
     *
     * @return
     */
    @Override
    public AjaxResult groundPileTrayListVoluntarily(Long outDeliveryId, Long materialId) {
        // 查询物料拣货信息
        TOutDeliveryDetail deliveryDetail = outDeliveryDetailService.getOne(
                new QueryWrapper<TOutDeliveryDetail>()
                        .eq("out_delivery_id", outDeliveryId)
                        .eq("material_id", materialId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetail == null) {
            return AjaxResult.error("单据不存在");
        }

        // 查询可拣货的库存信息
        TStock tStock = new TStock();
        tStock.setLocationType("1");
        //拣货策略
        LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutStrategy::getDelFlag, Constants.DEL_FLAG_NO);
        queryWrapper.eq(TOutStrategy::getFlag, 1);
        TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
        String order = "";
        if (outStrategy != null) {
            if (("先进先出").equals(outStrategy.getName())) {
                order = "a.create_time ASC";
            } else if ("后进先出".equals(outStrategy.getName())) {
                order = "a.create_time DESC";
            } else if ("批次".equals(outStrategy.getName())) {
                order = "a.batch_code asc";
            }
        } else {
            order = "a.available_count ASC";
        }
        tStock.setMaterialId(Long.valueOf(materialId));
        tStock.setRemark(order);
        List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

        // 分配库存
        Long receiveCount = deliveryDetail.getPredictReceiveCount();

        Map<String, Object> map = new HashMap<>();
        map.put("predictReceiveCount", deliveryDetail.getPredictReceiveCount());

        if (trayList.size() == 0) {
            map.put("receiveCount", 0);
            map.put("dataList", new ArrayList<>());
            return AjaxResult.success(map);
        } else {
            List<TTrayVO> result = new ArrayList<>();
            for (TTrayVO trayVO : trayList) {
                if (receiveCount > trayVO.getAvailableCount()) {
                    Long count = receiveCount;
                    receiveCount = receiveCount - trayVO.getAvailableCount();
                    if (receiveCount >= 0) {
                        trayVO.setReceiveCount(trayVO.getAvailableCount());
                    } else {
                        trayVO.setReceiveCount(count);
                    }
                    result.add(trayVO);
                } else {
                    trayVO.setReceiveCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
            map.put("totalCount", deliveryDetail.getReceiveCount());
            map.put("dataList", result);
            return AjaxResult.success(map);
        }
    }


    @Override
    public Map<String, Object> getOutDeliveryCount(String id) {
        Map<String, Object> map = new HashMap<>();
        Long predictReceiveCount = Long.parseLong("0");
        TOutDeliveryDetail tOutDelivery = outDeliveryDetailService.getById(id);
        if (tOutDelivery != null) {
            predictReceiveCount = tOutDelivery.getPredictReceiveCount();
        }
        map.put("predictReceiveCount", predictReceiveCount);
        Long OutboundCount = tTaskOutMapper.selectList(Wrappers.lambdaQuery(TTaskOut.class)
                .eq(TTaskOut::getOutDeliveryDetailId, id)
                .eq(TTaskOut::getDelFlag, Constants.DEL_FLAG_NO)
        ).stream().mapToLong(tTaskOut -> tTaskOut.getActualCount() == null ? 0 : tTaskOut.getActualCount()).sum();
        map.put("totalCount", OutboundCount);
        return map;

    }

    /**
     * 查询出库任务详情
     *
     * @param id 出库任务详情主键
     * @return 出库任务详情
     */
    @Override
    public TTaskOutDTO selectTTaskOutById(Long id, Integer type) {
        TTaskOut tTaskOut = new TTaskOut();
        if (type == 2) {
            LambdaQueryWrapper<TTaskWcsDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TTaskWcsDetail::getTaskId, id);
            queryWrapper.eq(TTaskWcsDetail::getDelFlag, 0);
            queryWrapper.eq(TTaskWcsDetail::getType, Constants.WCS_TASK_TYPE_OUT);
            TTaskWcsDetail taskWcsDetail = taskWcsDetailService.getOne(queryWrapper);
            id = taskWcsDetail.getOriginId();
            tTaskOut = tTaskOutMapper.selectById(id);
        } else {
            tTaskOut = tTaskOutMapper.selectOne(new LambdaQueryWrapper<TTaskOut>().eq(TTaskOut::getOutDeliveryDetailId, id));
        }
        TTaskOutDTO tTaskOutDTO = new TTaskOutDTO();
        if (tTaskOut != null) {
            BeanUtils.copyProperties(tTaskOut, tTaskOutDTO);
            TStock stock = stockService.getById(tTaskOut.getStockId());
            tTaskOutDTO.setBatchCode(stock.getBatchCode());
            TMaterial material = materialService.getById(tTaskOut.getMaterialId());
            tTaskOutDTO.setMaterialCode(material.getCode());
            tTaskOutDTO.setMaterialName(material.getName());
            TUnit unit = unitService.getById(material.getUnitId());
            tTaskOutDTO.setUnitName(unit.getName());
            TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, material.getId()).eq(TUnitConfig::getDelFlag, 0));
            if (unitConfig != null && unitConfig.getMinUnitId() != null) {
                tTaskOutDTO.setSmallUnitName(unitService.getById(unitConfig.getMinUnitId()).getName());
                tTaskOutDTO.setCount(unitConfig.getCount());
            }
            TLocation location = locationService.getById(tTaskOut.getLocationId());
            tTaskOutDTO.setLocationName(location.getName());
            TReservoir tReservoir = reservoirService.getById(location.getReservoirId());
            tTaskOutDTO.setReservoirId(tReservoir.getId());
            tTaskOutDTO.setReservoirName(tReservoir.getName());
            TTray tray = trayService.getById(stock.getTrayId());
            tTaskOutDTO.setTrayCode(tray.getCode());
//            if (type == 2) {
//                TOutDeliveryDetail tOutDeliveryDetail = outDeliveryDetailService.getById(tTaskOut.getOutDeliveryDetailId());
//                if (tTaskOut.getStatus().equals(Constants.TASK_STATUS_END)) {
//                    tTaskOutDTO.setPredictCount(tOutDeliveryDetail.getReceiveCount());
//                }
////                else {
////                    tTaskOutDTO.setPredictCount(tOutDeliveryDetail.getPredictCount());
////                }
//                tTaskOutDTO.setSmallPredictCount(tOutDeliveryDetail.getSmallPredictCount());
//
//            }
            TStockMain tStockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                    .eq(TStockMain::getMaterialId, material.getId())
                    .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO)
            );
            if (tStockMain != null) {
                tTaskOutDTO.setAvailableCount(tStockMain.getLibraryCount());
            } else {
                tTaskOutDTO.setAvailableCount(new Long("0"));
            }
//            //预计拣货数量
//            tTaskOutDTO.setReceiveCount(tTaskOut.getPredictCount());
        }
        return tTaskOutDTO;
    }

    @Override
    public List<TMaterialDetail> getMaterialRfidList(Long locationId, String batchCode, String rfidHead,Long materialId) {
        QueryWrapper<TMaterialDetail> eq = new QueryWrapper<TMaterialDetail>()
                .eq("location_id", locationId)
                .eq("batch_code", batchCode)
                .eq("status", Constants.MATERIAL_DETAIL_STATUS_IN)
                .eq("material_id",materialId)
                .eq("del_flag", Constants.DEL_FLAG_NO);
        if(StringUtils.isNotNull(rfidHead)){
            eq.like("rfid_head", rfidHead);
        }
        List<TMaterialDetail> detailList = materialDetailService.list(eq);
        return detailList;
    }

    @Override
    public List<TTaskOutDTO> selectTTaskOut(Long id) {
        List<TTaskOutDTO> tTaskOutDTOList = new ArrayList<>();
        List<TTaskOut> tTaskOutList = tTaskOutMapper.selectList(new LambdaQueryWrapper<TTaskOut>().eq(TTaskOut::getOutDeliveryDetailId, id));
        for (TTaskOut tTaskOut : tTaskOutList) {
            TTaskOutDTO tTaskOutDTO = new TTaskOutDTO();
            if (tTaskOut != null) {
                BeanUtils.copyProperties(tTaskOut, tTaskOutDTO);
                TStock stock = stockService.getById(tTaskOut.getStockId());
                tTaskOutDTO.setBatchCode(stock.getBatchCode());
                TMaterial material = materialService.getById(tTaskOut.getMaterialId());
                tTaskOutDTO.setMaterialCode(material.getCode());
                tTaskOutDTO.setMaterialName(material.getName());
                TUnit unit = unitService.getById(material.getUnitId());
                tTaskOutDTO.setUnitName(unit.getName());
                TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, material.getId()).eq(TUnitConfig::getDelFlag, 0));
                if (unitConfig != null && unitConfig.getMinUnitId() != null) {
                    tTaskOutDTO.setSmallUnitName(unitService.getById(unitConfig.getMinUnitId()).getName());
                    tTaskOutDTO.setCount(unitConfig.getCount());
                }
                TLocation location = locationService.getById(tTaskOut.getLocationId());
                tTaskOutDTO.setLocationName(location.getName());
                TReservoir tReservoir = reservoirService.getById(location.getReservoirId());
                tTaskOutDTO.setReservoirId(tReservoir.getId());
                tTaskOutDTO.setReservoirName(tReservoir.getName());
                TTray tray = trayService.getById(stock.getTrayId());
                if (tray != null) {
                    tTaskOutDTO.setTrayCode(tray.getCode());
                }
                tTaskOutDTOList.add(tTaskOutDTO);
            }
        }

        return tTaskOutDTOList;
    }

    @Transactional
    @Override
    public AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcsOutVO) {

//        // 判断小件出库的rfid是否包含在整件出库的rfid列表中
//        if (tTaskWcsOutVO.getRfidList() != null && tTaskWcsOutVO.getRfid() != null) {
//            List<String> rfidList = tTaskWcsOutVO.getRfidList();
//            boolean contains = rfidList.contains(tTaskWcsOutVO.getRfid());
//            if (contains) {
//                return AjaxResult.error("小件出库使用的rfid不可与整件出库的物料rfid重复");
//            }
//        }

        if (tTaskWcsOutVO.getRfidList() == null && tTaskWcsOutVO.getRfidList().size() == 0) {
            return AjaxResult.error("RFID参数不可为空");
        }

        // 拣货总数
        int rfidNum = tTaskWcsOutVO.getRfidList().size();

        TTaskWcs tTaskWcs = taskWcsService.getById(tTaskWcsOutVO.getId());
        if (tTaskWcs == null || !Constants.WCS_TASK_TYPE_OUT.equals(tTaskWcs.getTaskType())) {
            return AjaxResult.error("请选择要强制执行的出库任务");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        if (tTaskWcsOutVO.getReceiveCount() == null && tTaskWcsOutVO.getSmallReceiveCount() == null) {
            return AjaxResult.error("实际拣货数量不可为空");
        }
        //查询对应的移库任务是否完成
        Long moveTaskCount = taskWcsService.getMoveCountByMainNo(tTaskWcs.getMainTaskNo());
        if(moveTaskCount > 0){
            return AjaxResult.error("请先完成移库任务");
        }
        TTaskWcsDetail tTaskWcsDetailVOS = taskWcsDetailService.getOne(new LambdaQueryWrapper<TTaskWcsDetail>().eq(TTaskWcsDetail::getTaskId, tTaskWcs.getId()));
        //List<TTaskWcsDetailVO> tTaskWcsDetailVOS = taskWcsDetailService.selectStatusWcsListByTrayId(tTaskWcs.getTrayId(), tTaskWcs.getTaskType());
        if (tTaskWcsDetailVOS == null) {
            return AjaxResult.error("未查询到可执行数据");
        }

        //更新原单任务状态 t_task_out
        TTaskOut taskOut = tTaskOutMapper.selectById(tTaskWcsDetailVOS.getOriginId());
        if (!taskOut.getWcsId().equals(tTaskWcs.getId())) {
            return AjaxResult.error("任务不匹配");
        }
        if (rfidNum > taskOut.getPredictCount().intValue() -
                (taskOut.getActualCount() == null ? 0:taskOut.getActualCount().intValue())){
            return AjaxResult.error("拣货数量不可大于计划数");
        }

        //更新实际拣货数量
        TOutDeliveryDetail deliveryDetail = outDeliveryDetailService.getById(taskOut.getOutDeliveryDetailId());

        //操作t_stock
        TStock stock = stockService.getById(taskOut.getStockId());
//        TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, stock.getMaterialId()).eq(TUnitConfig::getDelFlag, Constants.NO));
//        if (unitConfig == null && tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            return AjaxResult.error("该物料不可小件领取");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getRfid() == null) {
//            return AjaxResult.error("请输入小件领取的物料rfid");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getSmallReceiveCount() >= unitConfig.getCount()) {
//            return AjaxResult.error("小件领取的数据大于物品包装最大数");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            TMaterialDetailSerachDTO materialDetail = new TMaterialDetailSerachDTO();
//            materialDetail.setRfid(tTaskWcsOutVO.getRfid());
//            List<TMaterialDetailVO> tMaterialDetailVOS = materialDetailService.selectTMaterialDetailList(materialDetail);
//            Long samllCountSum = tMaterialDetailVOS.stream().mapToLong(TMaterialDetailVO::getUseCount).sum();
//            Long currentsamllCountSum = tTaskWcsOutVO.getSmallReceiveCount() + samllCountSum;
//            if (currentsamllCountSum > unitConfig.getCount()) {
//                return AjaxResult.error("该物料小件领取已超出");
//            }
//        }

        if (tTaskWcsOutVO.getReceiveCount() == null) {
            tTaskWcsOutVO.setReceiveCount(0l);
        }
        Long stockOut = tTaskWcsOutVO.getSmallReceiveCount() == null ? tTaskWcsOutVO.getReceiveCount() : tTaskWcsOutVO.getReceiveCount() + 1;
        if (stock.getCount() < stockOut) {
            return AjaxResult.error("所选载具库存不足，无法出库");
        }

        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setBeforeCount(stock.getCount());
        if (tTaskWcsOutVO.getReceiveCount() != null) {
            int count = 0;
            if (deliveryDetail.getReceiveCount() != null){
                count = tTaskWcsOutVO.getReceiveCount().intValue()+deliveryDetail.getReceiveCount().intValue();
            }else {
                count = tTaskWcsOutVO.getReceiveCount().intValue();
            }
            deliveryDetail.setReceiveCount(Long.valueOf(count));
            //减去载具库存
            stock.setAvailableCount(stock.getAvailableCount() - tTaskWcsOutVO.getReceiveCount());
            stock.setCount(stock.getCount() - tTaskWcsOutVO.getReceiveCount());
            if (stock.getCount() < 0 || stock.getAvailableCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }

            //更新载具、库位
            TTray tTray = trayService.getById(stock.getTrayId());
            TLocation tLocation = locationService.getById(stock.getLocationId());

            // 查询载具是否含有其它物料
            List<TStock> stockList = tStockMapper.selectList(new QueryWrapper<TStock>()
                    .eq("tray_id", tTray.getId())
                    .gt("count",0)
                    .eq("del_flag", Constants.DEL_FLAG_NO));

            //判断现有库存是否为0
            //判断剩余库存是否为0
            if (stock.getCount() == 0 && stock.getAvailableCount() == 0) {
                //该条库存标记为删除状态
                stock.setDelFlag(Constants.DEL_FLAG_YES);
                //更新载具信息 判断载具是否还有其它物料 如有其它物料则为半托 否则为空闲
                if (stockList.size() == 0){
                    tTray.setLocationId(null);
                    tTray.setStatus(Constants.TRAY_STATUS_LEISURE);
                }else {
                    tTray.setStatus(Constants.TRAY_STATUS_HALF);
                }
            } else {
                tTray.setStatus(Constants.TRAY_STATUS_HALF);
            }
            // 更新库存
            stockService.updateById(stock);

            // 更新库位状态为无货
            tLocation.setPalletNum("");

            if (stockList.size() == 0){
                trayService.update(tTray, new UpdateWrapper<TTray>().set("location_id", null).eq("id", tTray.getId()));
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }else {
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            }

            locationService.updateById(tLocation);

            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
            stockMain.setLibraryCount(stockMain.getLibraryCount() - tTaskWcsOutVO.getReceiveCount());
            if (stockMain.getLibraryCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }
            stockMainService.updateById(stockMain);

        } else {
            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
            if (stockMain.getLibraryCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }
        }

        //使用记录
        tStockDetail.setLocationId(stock.getLocationId());
        tStockDetail.setMaterialId(stock.getMaterialId());
        tStockDetail.setCurrentCount(stock.getCount());
        tStockDetail.setType("2");
        tStockDetail.setOriginId(taskOut.getId());
        tStockDetail.setBatchCode(stock.getBatchCode());
        stockDetailService.save(tStockDetail);


        // materialDetail id
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            deliveryDetail.setSmallReceiveCount(tTaskWcsOutVO.getSmallReceiveCount());
//            //有小件出库
//            TOutDeliverySamllRecord outDeliverySamllRecord = new TOutDeliverySamllRecord();
//            outDeliverySamllRecord.setOutDeliveryDetailId(Long.valueOf(taskOut.getOutDeliveryDetailId()));
//            TMaterialDetail materialDetail = materialDetailService.getOne(new LambdaQueryWrapper<TMaterialDetail>()
//                    .eq(TMaterialDetail::getRfid, tTaskWcsOutVO.getRfid()).eq(TMaterialDetail::getDelFlag, Constants.DEL_FLAG_NO));
//            if (materialDetail != null) {
//                outDeliverySamllRecord.setMaterialDetailId(materialDetail.getId());
//            }
//            materialDetail.setUseCount(materialDetail.getUseCount() != null ? materialDetail.getUseCount() + tTaskWcsOutVO.getSmallReceiveCount() : tTaskWcsOutVO.getSmallReceiveCount());
//            if (StringUtils.isNotNull(materialDetail.getWeight())) {
//                double v = materialDetail.getWeight() - (materialDetail.getWeight() / unitConfig.getCount() * tTaskWcsOutVO.getSmallReceiveCount());
//                materialDetail.setWeight(v);
//            }
//            if (StringUtils.isNotNull(materialDetail.getPrice())) {
//                double v = materialDetail.getPrice() - (materialDetail.getPrice() / unitConfig.getCount() * tTaskWcsOutVO.getSmallReceiveCount());
//                materialDetail.setPrice(v);
//            }
//
//            outDeliverySamllRecord.setLocationId(stock.getLocationId());
//            outDeliverySamllRecord.setTrayId(stock.getTrayId());
//            outDeliverySamllRecordService.save(outDeliverySamllRecord);
//            materialDetailService.updateById(materialDetail);
//
//            // 判断小件领取是否到达包装最大值  如该rfid领取完毕  扣除库存
//            TUnitConfig tUnitConfig = unitConfigService.getOne(new QueryWrapper<TUnitConfig>()
//                    .eq("material_id", materialDetail.getMaterialId()));
//
//            // 处理库存主表数据
//            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
//
//            if (tUnitConfig.getCount().equals(materialDetail.getUseCount())) {
//                stockMain.setLibraryCount(stockMain.getLibraryCount() - 1);
//
//                // 更新库存
//                stock.setAvailableCount(stock.getAvailableCount() - 1);
//                stock.setCount(stock.getCount() - 1);
//                stockService.updateById(stock);
//
//                //使用记录
//                tStockDetail.setId(null);
//                tStockDetail.setLocationId(stock.getLocationId());
//                tStockDetail.setMaterialId(stock.getMaterialId());
//                tStockDetail.setCurrentCount(stock.getCount());
//                tStockDetail.setType("2");
//                tStockDetail.setOriginId(taskOut.getId());
//                tStockDetail.setBatchCode(stock.getBatchCode());
//                stockDetailService.save(tStockDetail);
//
//                // 移除rfid
//                materialDetailService.update(new TMaterialDetail(),
//                        new UpdateWrapper<TMaterialDetail>()
//                                .eq("id", materialDetail.getId())
//                                .set("del_flag", Constants.DEL_FLAG_YES));
//            } else {
//                if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//                    stockMain.setAvailableCount(stockMain.getAvailableCount() + 1);
//                }
//            }
//            stockMainService.updateById(stockMain);
//        }

//        long l1 = (tTaskWcsOutVO.getReceiveCount() != null) ? (tTaskWcsOutVO.getSmallReceiveCount() == null ? tTaskWcsOutVO.getReceiveCount() : (tTaskWcsOutVO.getReceiveCount() + 1)) : (tTaskWcsOutVO.getSmallReceiveCount() != null ? 1 : 0);


        taskOut.setActualCount((taskOut.getActualCount() == null?0: taskOut.getActualCount().longValue())+tTaskWcsOutVO.getReceiveCount().longValue());
        if (taskOut.getPredictCount().equals(taskOut.getActualCount())){
            // 已完成
            taskOut.setStatus(Constants.TASK_STATUS_END);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        }else {
            // 执行中
            taskOut.setStatus(Constants.TASK_STATUS_ING);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
        }
        tTaskOutMapper.updateById(taskOut);
        //更新任务状态
        taskWcsService.updateById(tTaskWcs);

        outDeliveryDetailService.updateById(deliveryDetail);

        //更新出库计划的出库状态
        LambdaQueryWrapper<TTaskOut> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TTaskOut::getOutDeliveryId, taskOut.getOutDeliveryId());
        queryWrapper.eq(TTaskOut::getDelFlag, Constants.DEL_FLAG_NO);
        List<TTaskOut> tTaskOutList = tTaskOutMapper.selectList(queryWrapper);
        boolean flag = true;
        for (TTaskOut tTaskOut : tTaskOutList) {
            if (tTaskOut.getStatus().equals(Constants.TASK_STATUS_NO) || tTaskOut.getStatus().equals(Constants.TASK_STATUS_ING)) {
                flag = false;
                break;
            }
            if (tTaskOut.getActualCount() < tTaskOut.getPredictCount()) {
                flag = false;
                break;
            }
        }
        TOutDelivery delivery = outDeliveryService.getById(taskOut.getOutDeliveryId());
        //判断是否部分出库  true全部出库    FALSE部分出库
        delivery.setCompleteState(flag ? "3" : "2");
        outDeliveryService.updateById(delivery);
        //如果在库数量不足生成补货记录

        // 处理调拨状态
        if (StringUtils.isNotEmpty(delivery.getOriginCode())) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("allot_status", Constants.ALLOT_STATUS_IN);
            updateWrapper.eq("code", delivery.getOriginCode());
            allotMapper.update(new TAllot(), updateWrapper);
        }

        List<TMaterialDetail> rfid = materialDetailService.list(
                new QueryWrapper<TMaterialDetail>()
                        .in("rfid", tTaskWcsOutVO.getRfidList()));
        List<String> rfidHeads = rfid.stream().map(TMaterialDetail::getRfidHead).distinct().collect(Collectors.toList());

        // 处理物料RFID 更新物料详情状态
         materialDetailService.update(new TMaterialDetail(),
            new UpdateWrapper<TMaterialDetail>()
                    .in("rfid", tTaskWcsOutVO.getRfidList())
                    // 2已出库未复核
                    .set("status", 2)
                    .set("out_delivery_detail_id", deliveryDetail.getId()));

        List<TMaterialDetail> rfidHeadList = materialDetailService.list(
                new QueryWrapper<TMaterialDetail>()
                        .in("rfid_head", rfidHeads));

        // 处理redis中rfid
        Map<String, List<TMaterialDetail>> listMap = rfidHeadList.stream().collect(Collectors.groupingBy(TMaterialDetail::getRfidHead));
        listMap.forEach((key,value)->{

            List<TMaterialDetail> collect = value.stream().filter(e -> e.getStatus().equals("1")).collect(Collectors.toList());
            if (collect.size() == 0){
                redisService.deleteObject("wms:materialDetail:" + key);
            }else {
                TMaterialDetail detail = collect.get(0);

                TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                tMaterialDetailVO.setBatchCode(detail.getBatchCode());
                tMaterialDetailVO.setMaterialId(detail.getMaterialId());
                tMaterialDetailVO.setMaterialName(detail.getMaterialName());
                tMaterialDetailVO.setRfid(key);
                tMaterialDetailVO.setCount(collect.size());
                tMaterialDetailVO.setRfids(collect.stream().map(TMaterialDetail::getRfid).collect(Collectors.toList()));
                redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);
            }
        });

        return AjaxResult.success();
    }

    @Override
    public AjaxResult executeOutTaskPDA(TTaskWcsOutVO tTaskWcsOutVO) {

//        // 判断小件出库的rfid是否包含在整件出库的rfid列表中
//        if (tTaskWcsOutVO.getRfidList() != null && tTaskWcsOutVO.getRfid() != null) {
//            List<String> rfidList = tTaskWcsOutVO.getRfidList();
//            boolean contains = rfidList.contains(tTaskWcsOutVO.getRfid());
//            if (contains) {
//                return AjaxResult.error("小件出库使用的rfid不可与整件出库的物料rfid重复");
//            }
//        }

        if (tTaskWcsOutVO.getRfidList() == null && tTaskWcsOutVO.getRfidList().size() == 0) {
            return AjaxResult.error("RFID参数不可为空");
        }
        List<Map<String, Object>> rfidList = tTaskWcsOutVO.getRfidListPda();
        // 查询总RFID下是否可拣货当前数量
        rfidList.forEach(e -> {
            String rfid = e.get("rfid").toString();
            List<TMaterialDetail> detailList = materialDetailService.list(new QueryWrapper<TMaterialDetail>().eq("rfid_head", rfid).eq("status", 1));
            if (detailList.size() < Integer.valueOf(e.get("count").toString()).intValue() ){
                throw new ServiceException("RFID:"+rfid+"超出数量无法拣货");
            }
        });


        // 拣货总数
        int rfidNum = rfidList.stream().mapToInt(e -> Integer.valueOf(e.get("count").toString())).sum();

        TTaskWcs tTaskWcs = taskWcsService.getById(tTaskWcsOutVO.getId());
        if (tTaskWcs == null || !Constants.WCS_TASK_TYPE_OUT.equals(tTaskWcs.getTaskType())) {
            return AjaxResult.error("请选择要强制执行的出库任务");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        if (tTaskWcsOutVO.getReceiveCount() == null && tTaskWcsOutVO.getSmallReceiveCount() == null) {
            return AjaxResult.error("实际拣货数量不可为空");
        }
        //查询对应的移库任务是否完成
        Long moveTaskCount = taskWcsService.getMoveCountByMainNo(tTaskWcs.getMainTaskNo());
        if(moveTaskCount > 0){
            return AjaxResult.error("请先完成移库任务");
        }
        TTaskWcsDetail tTaskWcsDetailVOS = taskWcsDetailService.getOne(new LambdaQueryWrapper<TTaskWcsDetail>().eq(TTaskWcsDetail::getTaskId, tTaskWcs.getId()));
        //List<TTaskWcsDetailVO> tTaskWcsDetailVOS = taskWcsDetailService.selectStatusWcsListByTrayId(tTaskWcs.getTrayId(), tTaskWcs.getTaskType());
        if (tTaskWcsDetailVOS == null) {
            return AjaxResult.error("未查询到可执行数据");
        }

        //更新原单任务状态 t_task_out
        TTaskOut taskOut = tTaskOutMapper.selectById(tTaskWcsDetailVOS.getOriginId());
        if (!taskOut.getWcsId().equals(tTaskWcs.getId())) {
            return AjaxResult.error("任务不匹配");
        }
        if (rfidNum > taskOut.getPredictCount().intValue() - (taskOut.getActualCount() == null ? 0:taskOut.getActualCount().intValue())){
            return AjaxResult.error("拣货数量不可大于计划数");
        }

        //更新实际拣货数量
        TOutDeliveryDetail deliveryDetail = outDeliveryDetailService.getById(taskOut.getOutDeliveryDetailId());

        //操作t_stock
        TStock stock = stockService.getById(taskOut.getStockId());
//        TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, stock.getMaterialId()).eq(TUnitConfig::getDelFlag, Constants.NO));
//        if (unitConfig == null && tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            return AjaxResult.error("该物料不可小件领取");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getRfid() == null) {
//            return AjaxResult.error("请输入小件领取的物料rfid");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getSmallReceiveCount() >= unitConfig.getCount()) {
//            return AjaxResult.error("小件领取的数据大于物品包装最大数");
//        }
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            TMaterialDetailSerachDTO materialDetail = new TMaterialDetailSerachDTO();
//            materialDetail.setRfid(tTaskWcsOutVO.getRfid());
//            List<TMaterialDetailVO> tMaterialDetailVOS = materialDetailService.selectTMaterialDetailList(materialDetail);
//            Long samllCountSum = tMaterialDetailVOS.stream().mapToLong(TMaterialDetailVO::getUseCount).sum();
//            Long currentsamllCountSum = tTaskWcsOutVO.getSmallReceiveCount() + samllCountSum;
//            if (currentsamllCountSum > unitConfig.getCount()) {
//                return AjaxResult.error("该物料小件领取已超出");
//            }
//        }

        if (tTaskWcsOutVO.getReceiveCount() == null) {
            tTaskWcsOutVO.setReceiveCount(0l);
        }
        Long stockOut = tTaskWcsOutVO.getSmallReceiveCount() == null ? tTaskWcsOutVO.getReceiveCount() : tTaskWcsOutVO.getReceiveCount() + 1;
        if (stock.getCount() < stockOut) {
            return AjaxResult.error("所选载具库存不足，无法出库");
        }


        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setBeforeCount(stock.getCount());
        if (tTaskWcsOutVO.getReceiveCount() != null) {
            int count = 0;
            if (deliveryDetail.getReceiveCount() != null){
                count = tTaskWcsOutVO.getReceiveCount().intValue()+deliveryDetail.getReceiveCount().intValue();
            }else {
                count = tTaskWcsOutVO.getReceiveCount().intValue();
            }
            deliveryDetail.setReceiveCount(Long.valueOf(count));

            //减去载具库存
            stock.setAvailableCount(stock.getAvailableCount() - tTaskWcsOutVO.getReceiveCount());
            stock.setCount(stock.getCount() - tTaskWcsOutVO.getReceiveCount());
            if (stock.getCount() < 0 || stock.getAvailableCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }

            //更新载具、库位
            TTray tTray = trayService.getById(stock.getTrayId());
            TLocation tLocation = locationService.getById(stock.getLocationId());

            //判断现有库存是否为0
            //判断剩余库存是否为0
            if (stock.getCount() == 0 && stock.getAvailableCount() == 0) {
                //该条库存标记为删除状态
                stock.setDelFlag(Constants.DEL_FLAG_YES);
                //更新载具信息
                tTray.setLocationId(null);
                tTray.setStatus(Constants.TRAY_STATUS_LEISURE);
            } else {
                tTray.setStatus(Constants.TRAY_STATUS_HALF);
            }
            // 更新库存
            stockService.updateById(stock);

            // 更新库位状态为无货
            tLocation.setPalletNum("");

            List<TStock> stockList = tStockMapper.selectList(new QueryWrapper<TStock>()
                    .eq("tray_id", tTray.getId())
                    .gt("count",0)
                    .eq("del_flag", Constants.DEL_FLAG_NO));
            if (stockList.size() == 0){
                trayService.update(tTray, new UpdateWrapper<TTray>().set("location_id", null).eq("id", tTray.getId()));
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }else {
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            }
            locationService.updateById(tLocation);

            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
            stockMain.setLibraryCount(stockMain.getLibraryCount() - tTaskWcsOutVO.getReceiveCount());
            if (stockMain.getLibraryCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }
            stockMainService.updateById(stockMain);

        } else {
            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
            if (stockMain.getLibraryCount() < 0) {
                return AjaxResult.error("系统错误，库存不足！");
            }
        }

        //使用记录
        tStockDetail.setLocationId(stock.getLocationId());
        tStockDetail.setMaterialId(stock.getMaterialId());
        tStockDetail.setCurrentCount(stock.getCount());
        tStockDetail.setType("2");
        tStockDetail.setOriginId(taskOut.getId());
        tStockDetail.setBatchCode(stock.getBatchCode());
        stockDetailService.save(tStockDetail);


        // materialDetail id
//        if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//            deliveryDetail.setSmallReceiveCount(tTaskWcsOutVO.getSmallReceiveCount());
//            //有小件出库
//            TOutDeliverySamllRecord outDeliverySamllRecord = new TOutDeliverySamllRecord();
//            outDeliverySamllRecord.setOutDeliveryDetailId(Long.valueOf(taskOut.getOutDeliveryDetailId()));
//            TMaterialDetail materialDetail = materialDetailService.getOne(new LambdaQueryWrapper<TMaterialDetail>()
//                    .eq(TMaterialDetail::getRfid, tTaskWcsOutVO.getRfid()).eq(TMaterialDetail::getDelFlag, Constants.DEL_FLAG_NO));
//            if (materialDetail != null) {
//                outDeliverySamllRecord.setMaterialDetailId(materialDetail.getId());
//            }
//            materialDetail.setUseCount(materialDetail.getUseCount() != null ? materialDetail.getUseCount() + tTaskWcsOutVO.getSmallReceiveCount() : tTaskWcsOutVO.getSmallReceiveCount());
//            if (StringUtils.isNotNull(materialDetail.getWeight())) {
//                double v = materialDetail.getWeight() - (materialDetail.getWeight() / unitConfig.getCount() * tTaskWcsOutVO.getSmallReceiveCount());
//                materialDetail.setWeight(v);
//            }
//            if (StringUtils.isNotNull(materialDetail.getPrice())) {
//                double v = materialDetail.getPrice() - (materialDetail.getPrice() / unitConfig.getCount() * tTaskWcsOutVO.getSmallReceiveCount());
//                materialDetail.setPrice(v);
//            }
//
//            outDeliverySamllRecord.setLocationId(stock.getLocationId());
//            outDeliverySamllRecord.setTrayId(stock.getTrayId());
//            outDeliverySamllRecordService.save(outDeliverySamllRecord);
//            materialDetailService.updateById(materialDetail);
//
//            // 判断小件领取是否到达包装最大值  如该rfid领取完毕  扣除库存
//            TUnitConfig tUnitConfig = unitConfigService.getOne(new QueryWrapper<TUnitConfig>()
//                    .eq("material_id", materialDetail.getMaterialId()));
//
//            // 处理库存主表数据
//            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
//
//            if (tUnitConfig.getCount().equals(materialDetail.getUseCount())) {
//                stockMain.setLibraryCount(stockMain.getLibraryCount() - 1);
//
//                // 更新库存
//                stock.setAvailableCount(stock.getAvailableCount() - 1);
//                stock.setCount(stock.getCount() - 1);
//                stockService.updateById(stock);
//
//                //使用记录
//                tStockDetail.setId(null);
//                tStockDetail.setLocationId(stock.getLocationId());
//                tStockDetail.setMaterialId(stock.getMaterialId());
//                tStockDetail.setCurrentCount(stock.getCount());
//                tStockDetail.setType("2");
//                tStockDetail.setOriginId(taskOut.getId());
//                tStockDetail.setBatchCode(stock.getBatchCode());
//                stockDetailService.save(tStockDetail);
//
//                // 移除rfid
//                materialDetailService.update(new TMaterialDetail(),
//                        new UpdateWrapper<TMaterialDetail>()
//                                .eq("id", materialDetail.getId())
//                                .set("del_flag", Constants.DEL_FLAG_YES));
//            } else {
//                if (tTaskWcsOutVO.getSmallReceiveCount() != null) {
//                    stockMain.setAvailableCount(stockMain.getAvailableCount() + 1);
//                }
//            }
//            stockMainService.updateById(stockMain);
//        }

//        long l1 = (tTaskWcsOutVO.getReceiveCount() != null) ? (tTaskWcsOutVO.getSmallReceiveCount() == null ? tTaskWcsOutVO.getReceiveCount() : (tTaskWcsOutVO.getReceiveCount() + 1)) : (tTaskWcsOutVO.getSmallReceiveCount() != null ? 1 : 0);


        taskOut.setActualCount((taskOut.getActualCount() == null?0: taskOut.getActualCount().longValue()) + tTaskWcsOutVO.getReceiveCount().longValue());
        if (taskOut.getPredictCount().equals(taskOut.getActualCount())){
            // 已完成
            taskOut.setStatus(Constants.TASK_STATUS_END);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        }else {
            // 执行中
            taskOut.setStatus(Constants.TASK_STATUS_ING);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
        }
        tTaskOutMapper.updateById(taskOut);

        outDeliveryDetailService.updateById(deliveryDetail);
        //更新任务状态
        taskWcsService.updateById(tTaskWcs);

        //更新出库计划的出库状态
        LambdaQueryWrapper<TTaskOut> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TTaskOut::getOutDeliveryId, taskOut.getOutDeliveryId());
        queryWrapper.eq(TTaskOut::getDelFlag, Constants.DEL_FLAG_NO);
        List<TTaskOut> tTaskOutList = tTaskOutMapper.selectList(queryWrapper);
        boolean flag = true;
        for (TTaskOut tTaskOut : tTaskOutList) {
            if (tTaskOut.getStatus().equals(Constants.TASK_STATUS_NO) || tTaskOut.getStatus().equals(Constants.TASK_STATUS_ING)) {
                flag = false;
                break;
            }
            if (tTaskOut.getActualCount() < tTaskOut.getPredictCount()) {
                flag = false;
                break;
            }
        }
        TOutDelivery delivery = outDeliveryService.getById(taskOut.getOutDeliveryId());
        //判断是否部分出库  true全部出库    FALSE部分出库
        delivery.setCompleteState(flag ? "3" : "2");
        outDeliveryService.updateById(delivery);
        //如果在库数量不足生成补货记录

        // 处理调拨状态
        if (StringUtils.isNotEmpty(delivery.getOriginCode())) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("allot_status", Constants.ALLOT_STATUS_IN);
            updateWrapper.eq("code", delivery.getOriginCode());
            allotMapper.update(new TAllot(), updateWrapper);
        }

        // 处理物料RFID
        rfidList.forEach(e ->{
            String rfid = e.get("rfid").toString();
            Integer count = Integer.valueOf(e.get("count").toString());

            // 根据总rifd 随机扣除总rifd下相同数量的rfid
            List<TMaterialDetail> rfidHead = materialDetailService.list(
                    new QueryWrapper<TMaterialDetail>()
                            .eq("rfid_head", rfid)
                            .eq("status","1")
                            .last("order by rand() limit " + count));
            List<Long> collect = rfidHead.stream().map(TMaterialDetail::getId).collect(Collectors.toList());
            List<String> rfIds = rfidHead.stream().map(TMaterialDetail::getRfid).collect(Collectors.toList());
            materialDetailService.update(new TMaterialDetail(),
                    new UpdateWrapper<TMaterialDetail>()
                            .in("id", collect)
                            // 2已出库未复核
                            .set("status", "2")
                            .set("out_delivery_detail_id", deliveryDetail.getId()));

            List<TMaterialDetail> outRfid = materialDetailService.list(
                    new QueryWrapper<TMaterialDetail>().eq("rfid_head", rfid).eq("status", "1"));
            if (outRfid.size() == 0){
                redisService.deleteObject("wms:materialDetail:" + rfid);
            }else {
                TMaterialDetailRedisVO cacheObject = redisService.getCacheObject("wms:materialDetail:" + rfid);
                if(cacheObject != null){
                    //redis存放
                    List<String> rfidsList = cacheObject.getRfids();
                    rfidsList.removeAll(rfIds);
                    cacheObject.setCount(cacheObject.getCount() - count);
                    cacheObject.setRfids(rfidsList);
                    redisService.setCacheObject("wms:materialDetail:" + rfid, cacheObject);
                }
            }
        });

        return AjaxResult.success(flag);
    }

    @Override
    public AjaxResult scanTray(String trayCode, String rfid) {
        //张雅倩
        LambdaQueryWrapper<TTray> tTrayQueryWrapper = new LambdaQueryWrapper<>();
        //此处应改为和库位关联的载具，并且状态出去出库中的状态
        tTrayQueryWrapper.eq(TTray::getCode, trayCode);
        tTrayQueryWrapper.eq(TTray::getDelFlag, Constants.DEL_FLAG_NO);
        TTray tray = trayService.getOne(tTrayQueryWrapper);
        if (tray == null) {
            return AjaxResult.error("未找到扫描的载具信息");
        }
        /*LambdaQueryWrapper<TTaskWcs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TTaskWcs::getTaskType,Constants.WCS_TASK_TYPE_OUT);
        queryWrapper.eq(TTaskWcs::getTrayId,tray.getId());
        queryWrapper.eq(TTaskWcs::getTrayCode,tray.getCode());
        queryWrapper.eq(TTaskWcs::getTaskStatus,3);
        queryWrapper.eq(TTaskWcs::getDelFlag,Constants.DEL_FLAG_NO);
        TTaskWcs taskWcs = taskWcsService.getOne(queryWrapper);
        if(taskWcs == null){
            return AjaxResult.error("未找到载具相关的待执行任务");
        }*/
        PADTOutDeliveryDetailVO padtOutDeliveryDetailVO = tTaskOutMapper.scanTray(tray.getId().toString(), rfid);
        if (padtOutDeliveryDetailVO == null) {
            return AjaxResult.error("未查到该载具的出库待执行任务");
        }
        padtOutDeliveryDetailVO.setTrayId(tray.getId().toString());
        padtOutDeliveryDetailVO.setTrayName(tray.getCode());
        padtOutDeliveryDetailVO.setAreaId(tray.getAreaId().toString());
        padtOutDeliveryDetailVO.setAreaId(areaService.selectTAreaById(tray.getAreaId()).getName());
        TLocation location = locationService.getById(padtOutDeliveryDetailVO.getLocationId());
        padtOutDeliveryDetailVO.setLocationName(location.getName());
        return AjaxResult.success(padtOutDeliveryDetailVO);
    }

    @Override
    public AjaxResult trayBack(String trayCode) {
        //载具回库  无需更改库存
        LambdaQueryWrapper<TTray> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TTray::getCode, trayCode);
        queryWrapper.eq(TTray::getDelFlag, 0);
        TTray tray = trayService.getOne(queryWrapper);
        if (tray == null) {
            return AjaxResult.error("未找到该载具");
        }
        if (tray.getStatus().equals(Constants.TRAY_STATUS_LEISURE)) {
            return AjaxResult.error("该托盘为空，不可回库");
        }
        TTaskWcs tTaskWcs = new TTaskWcs();
        if (tray.getLocationId() != null) {
            //回到原来的位置
            tTaskWcs.setLocationId(tray.getLocationId());
        } else {
            //使用推荐库位的方法，给该载具推荐一个库位
            Long locationId = recommendedLocationUtil.recommendedLocation(null, tray.getId(), null, null);
            if (locationId == null) {
                throw new ServiceException("无可用库位!");
            }
            tTaskWcs.setLocationId(locationId);//推荐库位
            tray.setLocationId(locationId);
        }

        tTaskWcs.setTaskType(Constants.TASK_TYPE_BACK);
        tTaskWcs.setTaskStatus(Constants.TASK_STATUS_NO);
        tTaskWcs.setTrayCode(tray.getCode());
        tTaskWcs.setTrayId(tray.getId());
        tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));


        TLocation location = locationService.getById(tray.getLocationId());
        location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
        locationService.updateById(location);

        tTaskWcs.setPurposePosition(location.getName());
        taskWcsService.save(tTaskWcs);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult scanTrayNew(String trayCode) {
        //张雅倩
        LambdaQueryWrapper<TTray> tTrayQueryWrapper = new LambdaQueryWrapper<>();
        //此处应改为和库位关联的载具，并且状态出去出库中的状态
        tTrayQueryWrapper.eq(TTray::getCode, trayCode);
        tTrayQueryWrapper.eq(TTray::getDelFlag, Constants.DEL_FLAG_NO);
        TTray tray = trayService.getOne(tTrayQueryWrapper);
        if (tray == null) {
            return AjaxResult.error("未找到扫描的载具信息");
        }
        Map map = new HashMap();
        Map scanTrayNew = new HashMap();
        scanTrayNew = tTaskOutMapper.scanTrayNew(tray.getId());
        if (scanTrayNew != null) {
            map.put(scanTrayNew.get("batchCode"), scanTrayNew.get("predictCount"));
        }
        return AjaxResult.success(map);
    }

    /**
     * 新增出库任务详情
     *
     * @param tTaskOutVO 出库任务详情
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertTTaskOut(TTaskOutVO tTaskOutVO) {
        //判断任务是否已执行，不可多次执行
        TOutDeliveryDetail deliveryDetail = outDeliveryDetailService.getOne(new LambdaQueryWrapper<TOutDeliveryDetail>()
                .eq(TOutDeliveryDetail::getDelFlag, Constants.DEL_FLAG_NO)
                .eq(TOutDeliveryDetail::getId, tTaskOutVO.getOutDeliveryDetailId()));
        if (deliveryDetail == null) {
            return AjaxResult.error("未找到该任务");
        }
        if (deliveryDetail.getNextFlag().equals(Constants.INOUT_NEXT_FLAG_YES)) {
            return AjaxResult.error("已执行出库的任务不可再次执行");
        }
        List<TTaskOutDetailListVO> tTaskOutDetailListVOS = tTaskOutVO.gettTaskOutDetailListVOS();
        //判断数量和单据的预计数量是否一致
        Long receiveCount = deliveryDetail.getPredictReceiveCount();
        long sum = tTaskOutDetailListVOS.stream().filter(e -> e.getPredictCount() != null).mapToLong(TTaskOutDetailListVO::getPredictCount).sum();
        if (sum != receiveCount) {
            return AjaxResult.error("拣货数量和单据预计拣货数量不一致！");
        }
        List<TTaskOutDetailListVO> collect = tTaskOutDetailListVOS.stream().distinct().collect(Collectors.toList());

        // 处理wcs任务合并  多个物料在同一托盘  则只下发一个wcs任务
        List<TTaskWcs> wcsList = new ArrayList<>();
        List<Long> trayId = collect.stream().map(e -> e.getTrayId()).collect(Collectors.toList());
        trayId.forEach(e -> {
            TTray tTray = trayService.getById(e);

            //生成t_task_wcs
            TTaskWcs tTaskWcs = new TTaskWcs();
            collect.forEach(out -> {
                if (e.equals(out.getTrayId())){
                    tTaskWcs.setLocationId(out.getLocationId());
                }
            });
            tTaskWcs.setTrayId(e);
            tTaskWcs.setTrayCode(tTray.getCode());
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_OUT);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcs.setMainTaskNo(tTaskWcs.getTaskNo());
            taskWcsService.save(tTaskWcs);

            wcsList.add(tTaskWcs);
        });

        //判断所选载具的库存是否
        for (TTaskOutDetailListVO tTaskOutDetailListVO : collect) {
            TLocation location = locationService.getById(tTaskOutDetailListVO.getLocationId());
            if (location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)) {
                return AjaxResult.error("请选择有效库位");
            }
            String trayCode = "";
            TTray tTray = trayService.getById(tTaskOutDetailListVO.getTrayId());
            if (tTray == null) {
                //todo 先隐藏
//                return AjaxResult.error("请选择有效载具");
            } else {
                trayCode = tTray.getCode();
            }
            TStock tStock = stockService.getById(tTaskOutDetailListVO.getStockId());
            if (tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)) {
                return AjaxResult.error("请选择有效库存");
            }
//            //更新该物料的在库可用数量
//            if(deliveryDetail.getSmallPredictCount() == null){//不是小件领取
//                tStock.setAvailableCount(tStock.getAvailableCount()-tTaskOutDetailListVO.getPredictCount());
//                stockService.updateTStock(tStock);
//            }
            TTaskOut tTaskOut = new TTaskOut();
            tTaskOut.setOutDeliveryId(deliveryDetail.getOutDeliveryId());
            tTaskOut.setMaterialId(deliveryDetail.getMaterialId());
            tTaskOut.setPredictCount(tTaskOutDetailListVO.getPredictCount());
            tTaskOut.setStockId(tTaskOutDetailListVO.getStockId());
            tTaskOut.setLocationId(tTaskOutDetailListVO.getLocationId());
            tTaskOut.setTrayId(tTaskOutDetailListVO.getTrayId());
            tTaskOut.setOutDeliveryDetailId(deliveryDetail.getId());

            deliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
            outDeliveryDetailService.updateById(deliveryDetail);

            // 查询wcs主表id
            TTaskWcs tTaskWcs = new TTaskWcs();
            for (int i = 0; i < wcsList.size(); i++) {
                TTaskWcs taskWcs = wcsList.get(i);
                if (taskWcs.getTrayId().equals(tTaskOutDetailListVO.getTrayId())) {
                    // 查询wcs是否存在同一托盘的多条任务
                    // 此种情况出现于同一托盘存在同一物料的不同批次
                    // 分别绑定taskout的id  便于后续的出库处理
                    List<TTaskOut> wcsId = tTaskOutMapper.selectList(new QueryWrapper<TTaskOut>().eq("wcs_id", taskWcs.getId()));
                    if (wcsId.size() == 0){
                        tTaskWcs = taskWcs;
                    }
                }
            }

            tTaskOut.setWcsId(tTaskWcs.getId());
            save(tTaskOut);

            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
            tTaskWcsDetail.setTaskId(tTaskWcs.getId());
            tTaskWcsDetail.setOriginId(tTaskOut.getId());
            tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_OUT);
            taskWcsDetailService.save(tTaskWcsDetail);
        }

        // 更新出库单为 已分配
        TOutDelivery tOutDelivery = outDeliveryService.getById(deliveryDetail.getOutDeliveryId());
        tOutDelivery.setStatus("10");
        outDeliveryService.updateById(tOutDelivery);

        // 调用wcs、agv硬件服务
        // 去除同一托盘的多次任务  只给wcs下发一次硬件任务
        ArrayList<TTaskWcs> tTaskWcs = wcsList.stream().distinct().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TTaskWcs::getTrayId))), ArrayList::new));

        // 判断需要出库的载具的状态 未在库的载具不可出库
        List<Long> locationIds = tTaskWcs.stream().map(TTaskWcs::getLocationId).collect(Collectors.toList());
        List<TLocation> locationList = locationService.list(new QueryWrapper<TLocation>().in("id", locationIds));
        locationList.forEach(e -> {
            if (StringUtils.isEmpty(e.getPalletNum())){
                tTaskWcs.forEach(t -> {
                    if (t.getLocationId().equals(e.getId())){
                        TTray tTray = trayService.selectTTrayById(t.getTrayId());
                        throw new ServiceException("载具"+tTray.getCode()+"目前未在库，请稍后重新下发任务");
                    }
                });
            }
        });

        sendWcsOrAgv(tTaskWcs);

        return AjaxResult.success();
    }

    /**
     * 出库发送命令
     * @param wcsList
     */
    private void sendWcsOrAgv(List<TTaskWcs> wcsList) {
        List<WcsOrderDTO> sendList = new ArrayList<>();
        for (int i = 0; i < wcsList.size(); i++) {
            TTaskWcs taskWcs = wcsList.get(i);
            // 查询库位信息  在一楼则调用wcs 二楼则调用agv
            TLocation tLocation = locationService.getById(taskWcs.getLocationId());
            //组装出库参数
            String startStation = "";
            String endStation = "";
            if (Constants.LOCATION_FLOOR_FIRST.equals(tLocation.getFloorType())){
                startStation = tLocation.getLocationPlies() + "-" + tLocation.getPalletNodeId();
                endStation = WcsReportUtil.stationOut;
                Integer mainSort = tLocation.getExtentionType();
                // 一楼托盘 wcs
                //组装出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tLocation.getCode(), taskWcs.getTrayCode());
                orderDTO.setMainSort(mainSort);
                orderDTO.setMainTaskNo(taskWcs.getMainTaskNo());
                //组装移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), tLocation.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getMainTaskNo());
                    taskNoMove.setMainSort(mainSort);
                    sendList.add(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getMoveTaskNo());
                }
                sendList.add(orderDTO);
            }else if (Constants.LOCATION_FLOOR_SECOND.equals(tLocation.getFloorType())) {
                startStation = tLocation.getCode();
                endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                // 二楼料箱 agv
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, taskWcs.getTrayCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                // 更新WCS任务状态
                taskWcsService.update(
                        new UpdateWrapper<TTaskWcs>()
                                .eq("id", taskWcs.getId())
                                .set("task_status", status));
            }
            //将库位状态标记为已出库
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(tLocation);
        }
        //处理一楼wcs数据
        if(CollectionUtils.isNotEmpty(sendList)){
            //对发送列表进行排序，出库优先发送一伸位的
            List<WcsOrderDTO> sends = sendList.stream()
                    .sorted(Comparator.comparing(WcsOrderDTO::getMainSort)
                            .thenComparing(WcsOrderDTO::getMainTaskNo))
                    .collect(Collectors.toList());
            for (WcsOrderDTO order : sends) {
                //判断双伸位
                if (Constants.TASK_TYPE_MOVE.equals(order.getTaskType())) {
                    //发送移库
                    wcsReportUtil.sendWcsMoveReport(order);
                } else {
                    //组完盘，直接调wcs
                    wcsReportUtil.sendWcsOutReport(order);
                }
            }
        }
    }


    /**
     * 原出库发送命令-old
     * @param wcsList
     */
    private void sendWcsOrAgv1(List<TTaskWcs> wcsList) {

        String status = Constants.WCS_EXECUTE_STATUS_ING;

        for (int i = 0; i < wcsList.size(); i++) {
            TTaskWcs taskWcs = wcsList.get(i);

            // 查询库位信息  在一楼则调用wcs 二楼则调用agv
            TLocation tLocation = locationService.getById(taskWcs.getLocationId());
            if (Constants.LOCATION_GOODS_ALLOCATION_STATUS_3.equals(tLocation.getGoodsAllocationStatus())
                    || Constants.LOCATION_GOODS_ALLOCATION_STATUS_4.equals(tLocation.getGoodsAllocationStatus())) {
                throw new ServiceException("库位载具未在位置上，无法创建硬件出库任务");
            }

            // 查询托盘详情
            TTray tTray = trayService.getById(taskWcs.getTrayId());

            // 物料详情
            List<TTaskWcsDetail> detailList = taskWcsDetailService.list(
                    new QueryWrapper<TTaskWcsDetail>().eq("task_id", taskWcs.getId()));

            // 查询出库单信息
            TTaskOut tTaskOut = tTaskOutMapper.selectById(detailList.get(0).getOriginId());
            TOutDelivery tOutDelivery = outDeliveryService.getById(tTaskOut.getOutDeliveryId());

            // 一楼托盘 wcs
            if (Constants.LOCATION_FLOOR_FIRST.equals(tLocation.getFloorType())) {
                WcsSendEntity sendEntity = new WcsSendEntity();
                sendEntity.setReqID(taskWcs.getTaskNo());

                List<WcsOrderEntity> productDetails = new ArrayList<>();
                for (int j = 0; j < detailList.size(); j++) {

                    // 查询物料详情
                    TMaterial material = materialService.getById(tTaskOut.getMaterialId());

                    WcsOrderEntity wcsOrderEntity = new WcsOrderEntity();
                    wcsOrderEntity.setTaskNo(taskWcs.getTaskNo());
                    wcsOrderEntity.setEndStation(WcsReportUtil.stationOut);
                    // 此处需特别注意  一楼托盘的库位确定  需要 层+PalletNodeId
                    wcsOrderEntity.setStartStation(tLocation.getLocationPlies() + "-" + tLocation.getPalletNodeId());
                    wcsOrderEntity.setTrayNo(tTray.getCode());
                    wcsOrderEntity.setProductCode(material.getCode());

                    productDetails.add(wcsOrderEntity);
                }
                sendEntity.setProductDetails(productDetails);

                // 出库单号
                sendEntity.setOrderNo(tOutDelivery.getCode());

                // 记录wcs任务发送相关信息
                TTaskWcsRecord wcsRecord = new TTaskWcsRecord();
                wcsRecord.setTaskWcsId(taskWcs.getId());
                wcsRecord.setWcsType(Constants.TASK_HARDWARE_WCS);
                wcsRecord.setStartPosition(tLocation.getCode().toString());
                wcsRecord.setPurposePosition(WcsReportUtil.stationOut);
                wcsRecord.setSendData(JSONObject.toJSONString(sendEntity));
                wcsRecord.setOrderId(tTaskOut.getOutDeliveryId());
                wcsRecord.setInterfaceType(Constants.TASK_HARDWARE_INTERFACE_SEND);

                try {
                    WcsResultEntity resultEntity = wcsReportUtil.wcsSend(WcsReportUtil.wcsOut, sendEntity);
                    wcsRecord.setAcceptData(JSONObject.toJSONString(resultEntity));
                } catch (Exception e) {
                    e.printStackTrace();
                    wcsRecord.setAcceptData(e.getMessage());
                    status = Constants.WCS_EXECUTE_STATUS_NOT;
                }
                tTaskWcsRecordMapper.insert(wcsRecord);
            }


            // 二楼料箱 agv
            if (Constants.LOCATION_FLOOR_SECOND.equals(tLocation.getFloorType())) {
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), tLocation.getCode(), Constants.SHELF_POINT_SECOND_LINE_OUT, tTray.getCode());
                status = agvReportUtil.sendAgvPickingReport(orderDTO);
            }

            // 更新WCS任务状态
            taskWcsService.update(
                    new UpdateWrapper<TTaskWcs>()
                            .eq("id", taskWcs.getId())
                            .set("task_status", status));

            //将库位状态标记为已出库
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(tLocation);
        }

    }


    /**
     * 修改出库任务详情
     *
     * @param tTaskOut 出库任务详情
     * @return 结果
     */
    @Override
    public int updateTTaskOut(TTaskOut tTaskOut) {
        return tTaskOutMapper.updateById(tTaskOut);
    }


    /**
     * 批量删除出库任务详情
     *
     * @param ids 需要删除的出库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskOutByIds(Long[] ids) {
        return tTaskOutMapper.deleteTTaskOutByIds(ids);
    }

    /**
     * 删除出库任务详情信息
     *
     * @param id 出库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskOutById(Long id) {
        return tTaskOutMapper.deleteTTaskOutById(id);
    }

    @Transactional
    @Override
    public AjaxResult groundPileOutbound(TTaskOutVO tTaskOutVO) {

        if (tTaskOutVO.getOutDeliveryDetailId() == null
                || (tTaskOutVO.gettTaskOutDetailListVOS() == null || tTaskOutVO.gettTaskOutDetailListVOS().size() <= 0)) {
            return AjaxResult.error("参数错误！");
        }
        TOutDeliveryDetail tOutDeliveryDetail = outDeliveryDetailService.getById(tTaskOutVO.getOutDeliveryDetailId());
        if (tOutDeliveryDetail == null) {
            return AjaxResult.error("出库单不存在！");
        }
        //已拣货数量
        Long outboundCount = tTaskOutMapper.selectList(Wrappers.lambdaQuery(TTaskOut.class)
                .eq(TTaskOut::getOutDeliveryDetailId, tTaskOutVO.getOutDeliveryDetailId())
                .eq(TTaskOut::getDelFlag, Constants.DEL_FLAG_NO)
        ).stream().mapToLong(tTaskOut -> tTaskOut.getActualCount()).sum();
        List<TTaskOut> tTaskOutList = new ArrayList<>();
        for (TTaskOutDetailListVO task : tTaskOutVO.gettTaskOutDetailListVOS()) {
            TStock tStock = stockService.getById(task.getStockId());
            if (tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)) {
                return AjaxResult.error("请选择有效库存");
            }
            TLocation location = locationService.getById(tStock.getLocationId());
            if (location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)) {
                return AjaxResult.error("请选择有效库位");
            }
//            TTray tTray = trayService.getById(tStock.getTrayId());
//            if(tTray == null){
//                return AjaxResult.error("请选择有效载具");
//            }
            if (task.getReceiveCount() == null || task.getReceiveCount() <= 0) {
                return AjaxResult.error("实际拣货数量不可以为空！");
            }
            //添加出库记录
            TTaskOut tTaskOut = new TTaskOut();
            tTaskOut.setOutDeliveryId(tOutDeliveryDetail.getOutDeliveryId());
            tTaskOut.setOutDeliveryDetailId(tOutDeliveryDetail.getId());
            tTaskOut.setLocationId(location.getId());
            tTaskOut.setStockId(task.getStockId());
            tTaskOut.setMaterialId(tStock.getMaterialId());
            tTaskOut.setActualCount(task.getReceiveCount());
            tTaskOut.setPredictCount(tOutDeliveryDetail.getPredictReceiveCount());
            tTaskOut.setStatus("2");
            tTaskOutList.add(tTaskOut);

            outboundCount = outboundCount + task.getReceiveCount();
        }
        if (outboundCount.compareTo(tOutDeliveryDetail.getPredictReceiveCount()) > 0) {
            return AjaxResult.error("实际拣货数量不可超过预计拣货数量！");
        }

        for (TTaskOut t : tTaskOutList) {
            //更改主库存
            TStockMain tStockMain = stockMainService.getOne(Wrappers.lambdaQuery(TStockMain.class)
                    .eq(TStockMain::getMaterialId, t.getMaterialId())
                    .eq(TStockMain::getDelFlag, Constants.NO)
            );
            if (tStockMain != null) {
//                tStockMain.setAvailableCount(tStockMain.getAvailableCount()-t.getActualCount());
                tStockMain.setLibraryCount(tStockMain.getLibraryCount() - t.getActualCount());
                if (tStockMain.getLibraryCount() < 0) {
                    return AjaxResult.error("系统错误，库存不足！");
                }
                stockMainService.updateById(tStockMain);
            }

            // 查询库存数据
            TStock stock = stockService.getById(t.getStockId());

            // 记录库存变更
            TStockDetail stockDetail = new TStockDetail();
            stockDetail.setMaterialId(stock.getMaterialId());
            stockDetail.setLocationId(stock.getLocationId());
            stockDetail.setBeforeCount(stock.getCount());
            stockDetail.setCurrentCount(stock.getCount() - t.getActualCount());
            stockDetail.setType(Constants.TASK_TYPE_PICK);
            stockDetail.setBatchCode(stock.getBatchCode());
            stockDetailService.save(stockDetail);

            // 更改库存明细
            stock.setCount(stock.getCount() - t.getActualCount());
            stock.setAvailableCount(stock.getAvailableCount() - t.getActualCount());
            stockService.updateById(stock);

            tTaskOutMapper.insert(t);
        }

        //更改出库单相关状态
        TOutDelivery outDelivery = outDeliveryService.getById(tOutDeliveryDetail.getOutDeliveryId());

        if (outboundCount.equals(tOutDeliveryDetail.getPredictReceiveCount())) {
            // 出库单详情 1 已出库
            tOutDeliveryDetail.setNextFlag("1");
            // 出库单 12 已出库
            outDelivery.setStatus("12");
            // 出库单
            outDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_PART);
        } else {
            // 出库单详情 2 部分出库
            tOutDeliveryDetail.setNextFlag("2");
            // 出库单  11 部分出库
            outDelivery.setStatus("11");
            // 出库单
            outDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_COMPLETED);
        }
        outDeliveryDetailService.updateById(tOutDeliveryDetail);
        outDeliveryService.updateById(outDelivery);

        return AjaxResult.success();
    }

    /**
     * 通过载具拣出出库
     *
     * @param tTaskOut
     * @return
     */
    @Override
    public AjaxResult executeOutByTray(TTaskOutVO tTaskOut) {
        TTray tTrayVO = trayService.selectTTrayById(tTaskOut.getTrayId());
        if (tTrayVO == null || tTrayVO.getLocationId() == null) {
            return AjaxResult.error("未查询到载具相关信息");
        }
        List<TTaskOutDetailListVO> tTaskOutDetailListVOS = tTaskOut.gettTaskOutDetailListVOS();
        List<Long> stockIds = tTaskOutDetailListVOS.stream().map(TTaskOutDetailListVO::getStockId).collect(Collectors.toList());
        List<StockVo> stockVoList = tStockMapper.selectStockInfoByIds(stockIds);
        if (CollectionUtils.isEmpty(stockVoList)) {
            return AjaxResult.error("未获取到对应库存信息");
        }
        Map<Long, StockVo> stockVoMap = stockVoList.stream().collect(Collectors.toMap(StockVo::getId, Function.identity()));
        for (TTaskOutDetailListVO tTaskOutDetailListVO : tTaskOutDetailListVOS) {
            StockVo stockVo = stockVoMap.get(tTaskOutDetailListVO.getStockId());
            if (stockVo == null) {
                throw new ServiceException("未查询到对应库存信息");
            }
            //剩余数量
            Long receiveCount = stockVo.getCount() - tTaskOutDetailListVO.getReceiveCount();
            Long count = -(tTaskOutDetailListVO.getReceiveCount());
            if (receiveCount < 0) {
                throw new ServiceException("出库数量不可超出库存数量");
            }
            Long locationId = stockVo.getLocationId();
            //库存详情
            TStockDetail tStockDetail = new TStockDetail();
            tStockDetail.setMaterialId(stockVo.getMaterialId());
            tStockDetail.setType(Constants.WCS_TASK_TYPE_OUT);
            tStockDetail.setLocationId(locationId);
            tStockDetail.setOriginId(-1L);//-1为拣选出库
            tStockDetail.setStatus("0");
            tStockDetail.setBatchCode(stockVo.getBatchCode());
            tStockDetail.setCurrentCount(count);
            tStockDetail.setBeforeCount(stockVo.getCount());// 操作前数量
            tStockDetailMapper.insert(tStockDetail);

            if (receiveCount.equals(0L)) {
                stockService.deleteTStockById(stockVo.getId());
            } else {
                TStock stockUpdate = new TStock();
                stockUpdate.setId(stockVo.getId());
                stockUpdate.setCount(stockVo.getCount() + count);
                stockUpdate.setAvailableCount(stockVo.getAvailableCount() + count);
                stockService.updateById(stockUpdate);
            }

            //一个物料一条
            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                    .eq(TStockMain::getMaterialId, stockVo.getMaterialId())
                    .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));

            // 更新库存总数据
            if (stockMain != null) {
                TStockMain stockMainUpdate = new TStockMain();
                stockMainUpdate.setId(stockMain.getId());
                // 更新库存总数据
                stockMainUpdate.setId(stockMain.getId());
                stockMainUpdate.setLibraryCount(stockMain.getLibraryCount() + count.intValue());
                stockMainUpdate.setAvailableCount(stockMain.getAvailableCount() + count.intValue());
                //无数量删除
                if (stockMainUpdate.getLibraryCount().equals(0L) && stockMainUpdate.getAvailableCount().equals(0L)) {
                    stockMainService.deleteTStockMainById(stockMainUpdate.getId());
                } else {
                    stockMainService.updateById(stockMainUpdate);
                }
            }
        }
        List<StockVo> data = tStockMapper.getDeliveryDetailByTray(tTrayVO.getId());
        if (CollectionUtils.isEmpty(data)) {
            //判断是否还有货
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            locationService.updateById(tLocation);
        }
        return AjaxResult.success();
    }

    /**
     * 回流
     *
     * @param tOutDelivery
     * @return
     */
    @Override
    @Transactional
    public AjaxResult refluxOutDelivery(TOutDelivery tOutDelivery) {
        TOutDelivery outDeliveryVO = outDeliveryService.getById(tOutDelivery.getId());
        if (outDeliveryVO == null) {
            return AjaxResult.error("未查询到出库计划");
        }
        if (Constants.YES.equals(outDeliveryVO.getRefluxStatus())) {
            return AjaxResult.error("此单据已回流，不可再次回流");
        }
        //查询详情信息
        List<TStockInDTO> detailList = tOutDeliveryDetailMapper.getStockInfoByDeliveryId(tOutDelivery.getId());
        for (TStockInDTO tStockInDTO : detailList) {
            tStockInDTO.setType(Constants.WCS_TASK_TYPE_IN);
            stockService.moveInfoStock(tStockInDTO);
        }
        TOutDelivery updateDo = new TOutDelivery();
        updateDo.setId(tOutDelivery.getId());
        updateDo.setRefluxStatus(Constants.YES);
        outDeliveryService.updateById(updateDo);
        return AjaxResult.success();
    }

}
