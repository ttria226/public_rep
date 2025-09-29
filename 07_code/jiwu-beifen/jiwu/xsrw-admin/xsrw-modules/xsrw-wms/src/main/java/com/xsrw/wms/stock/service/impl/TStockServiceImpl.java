package com.xsrw.wms.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TStockMoveApiDTO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.*;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.dto.TStockInDTO;
import com.xsrw.wms.stock.domain.vo.TStockListVo;
import com.xsrw.wms.stock.domain.vo.StockVo;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存详情Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TStockServiceImpl extends ServiceImpl<TStockMapper, TStock> implements ITStockService {
    @Autowired
    private TStockMapper tStockMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;

    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Autowired
    private TReservoirMapper tReservoirMapper;

    @Autowired
    private TAreaMapper tAreaMapper;

    @Autowired
    private TLocationMapper tLocationMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    @Autowired
    private TUnitConfigMapper tUnitConfigMapper;

    @Autowired
    private TTrayMapper tTrayMapper;

    @Autowired
    private TStockDetailMapper tStockDetailMapper;

    @Autowired
    private TStockMainMapper tStockMainMapper;

    /**
     * 查询库存详情列表
     *
     * @param stock 库存详情
     * @return 库存详情
     */
    @Override
    public List<StockVo> selectTStockList(StockVo stock) {
        // 获取库存列表数据
        List<StockVo> stockList = tStockMapper.selectStockList(stock);
        return stockList;
    }

    /**
     * 查询库存详情
     *
     * @param id 库存详情主键
     * @return 库存详情
     */
    @Override
    public TStock selectTStockById(Long id) {
        return tStockMapper.selectById(id);
    }

    /**
     * 新增库存详情
     *
     * @param tStock 库存详情
     * @return 结果
     */
    @Override
    public int insertTStock(TStock tStock) {
        return tStockMapper.insert(tStock);
    }

    /**
     * 修改库存详情
     *
     * @param tStock 库存详情
     * @return 结果
     */
    @Override
    public int updateTStock(TStock tStock) {
        return tStockMapper.updateById(tStock);
    }


    /**
     * 批量删除库存详情
     *
     * @param ids 需要删除的库存详情主键
     * @return 结果
     */
    @Override
    public int deleteTStockByIds(Long[] ids) {
        return tStockMapper.deleteTStockByIds(ids);
    }

    /**
     * 删除库存详情信息
     *
     * @param id 库存详情主键
     * @return 结果
     */
    @Override
    public int deleteTStockById(Long id) {
        return tStockMapper.deleteTStockById(id);
    }

    /**
     * 根据ids--批量冻结/解冻
     *
     * @param stockIds
     * @param isFreeze   冻结标识 0 解冻，1 冻结
     * @param originType 冻结原因
     * @return
     */
    @Transactional
    @Override
    public AjaxResult updateFreezeByIds(List<Long> stockIds, String isFreeze, String originType) {

        List<TStock> stockList = tStockMapper.selectBatchIds(stockIds);
        // 物料id列表
        List<Long> materiaIds = new ArrayList<>();

        // 冻结校验
        if (Constants.STOCK_IS_FREEZE_YES.equals(isFreeze)) {
            for (TStock model : stockList) {
                // 判断该条数据是否被冻结
                if (Constants.STOCK_IS_FREEZE_YES.equals(model.getIsFreeze())) {
                    materiaIds.add(model.getMaterialId());
                }
            }
            if (materiaIds.size() > 0) {
                // 获取物料详情信息
                List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                        .in(TMaterial::getId, materiaIds)
                        .eq(TMaterial::getDelFlag, Constants.NO));
                String msg = "";
                for (TMaterial material : tMaterials) {
                    msg += "," + material.getName();
                }
                // 数据已经被冻结
                return AjaxResult.error(msg.substring(1) + "已经冻结，请刷新后重试");
            }

        } else {
            List<Long> updateStockIds = new ArrayList<>();
            for (TStock model : stockList) {
                // 冻结的数据判断是否是同类型解冻
                if (Constants.STOCK_IS_FREEZE_YES.equals(model.getIsFreeze())) {
                    updateStockIds.add(model.getId());
                    // 判断该条数据冻结来源是否一致
                    if (!originType.equals(model.getOriginType())) {
                        materiaIds.add(model.getMaterialId());
                    }
                }
            }
            if (materiaIds.size() > 0) {
                // 获取物料详情信息
                List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                        .eq(TMaterial::getDelFlag, Constants.NO)
                        .in(TMaterial::getId, materiaIds));
                String msg = "";
                for (TMaterial material : tMaterials) {
                    msg += "," + material.getName();
                }
                // 数据已经被冻结
                return AjaxResult.error(msg.substring(1) + "解冻类型和当前解冻来源不同，无法解冻");
            }
            originType = "";
            // 只更新冻结的数据
            stockIds = updateStockIds;
        }
        if (stockIds.size() > 0) {
            int rows = tStockMapper.updateFreezeByIds(stockIds, isFreeze, originType);
            return rows > 0 ? AjaxResult.success() : AjaxResult.error();
        }
        return AjaxResult.success();
    }

    /**
     * 批次总数量
     *
     * @param stock
     * @return
     */
    @Transactional
    @Override
    public List<StockVo> listBatchSum(StockVo stock) {

        // 检索条件中的物料ID列表
        List<Long> materialId = null;

        if (StringUtils.isNotEmpty(stock.getMaterialCode()) || StringUtils.isNotEmpty(stock.getMaterialName())) {
            // 根据检索条件中的物料编码和物料名称模糊查询，返回物料ID列表
            List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                    .eq(StringUtils.isNotNull(stock.getMaterialCode()), TMaterial::getCode, stock.getMaterialCode())
                    .like(StringUtils.isNotNull(stock.getMaterialName()), TMaterial::getName, stock.getMaterialName())
                    .eq(TMaterial::getDelFlag, Constants.NO));
            materialId = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(materialId)) {
                return new ArrayList<>();
            }
        }

        // 获取库存列表数据
        List<StockVo> stockList = tStockMapper.listBatchSum(stock, materialId);

        if (stockList.size() > 0) {

            stockList.forEach(model -> {

                TMaterial material = tMaterialMapper.selectById(model.getMaterialId());
                if (material != null) {
                    // 物料编码
                    model.setMaterialCode(material.getCode());
                    // 物料名称
                    model.setMaterialName(material.getName());
                }
            });
        }
        return stockList;
    }

    /**
     * 库内移位
     *
     * @param stockId 库存id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult shift(Long stockId) {

        TStock stock = tStockMapper.selectById(stockId);
        // 获取该载具上所有物料数据
        List<TStock> stockList = tStockMapper.selectList(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getTrayId, stock.getTrayId())
                .gt(TStock::getCount, 0)
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        // 获取库存列表数据
        List<StockVo> stockVoList = new ArrayList<>();
        if (stockList.size() > 0) {
            for (TStock model : stockList) {
                StockVo stockVo = new StockVo();
                //第一个参数是：目标存储，第二个参数是：源数据
                BeanUtils.copyProperties(model, stockVo);

                TMaterial material = tMaterialMapper.selectById(model.getMaterialId());
                if (material != null) {
                    // 物料编码
                    stockVo.setMaterialCode(material.getCode());
                    // 物料名称
                    stockVo.setMaterialName(material.getName());
                }
                if (Constants.STOCK_IS_FREEZE_YES.equals(model.getIsFreeze())) {
                    return AjaxResult.error("该库位上" + stockVo.getMaterialName() + "已被冻结，无法进行移库");
                }
                stockVoList.add(stockVo);
            }
        }
        // 返回该托盘上的物料
        return AjaxResult.success(stockVoList);

    }

    /**
     * 查询托盘物料总数
     *
     * @param trayCode
     * @return
     */
    @Override
    public AjaxResult getTrayNum(String trayCode) {

        TTray tray = tTrayMapper.selectOne(Wrappers.lambdaQuery(TTray.class).eq(TTray::getCode, trayCode));
        if (tray == null) {
            return AjaxResult.error("托盘不存在");
        }

        List<TStock> stockList = this.list(new QueryWrapper<TStock>().eq("tray_id", tray.getId()));
        long sum = stockList.stream().mapToLong(e -> e.getCount()).sum();
        return AjaxResult.success(sum);
    }

    /**
     * 通过库位id查询库存数据
     *
     * @param locationId
     */
    @Override
    public AjaxResult queryLocation(Long locationId) {
        List<TStock> stockList = tStockMapper.selectList(new QueryWrapper<TStock>().eq("location_id", locationId));
        return AjaxResult.success(stockList);
    }

    /**
     * 查询库存
     *
     * @param materialId
     * @param batchCode
     */
    @Override
    public AjaxResult queryStock(Long materialId, String batchCode) {
        List<TStock> stockList = tStockMapper.selectList(Wrappers.lambdaQuery(TStock.class)
                .eq(TStock::getMaterialId, materialId)
                .eq(StringUtils.isNotEmpty(batchCode), TStock::getBatchCode, batchCode));
        return AjaxResult.success(stockList);
    }

    /**
     * 根据载具id获取对应的库存信息
     *
     * @param trayId
     * @return
     */
    @Override
    public List<StockVo> getStockListByTrayId(Long trayId) {
        List<StockVo> stockList = tStockMapper.getStockListByTrayId(trayId);
        return stockList;
    }


    /**
     * 计算剩余有效期天数
     *
     * @param date           生产日期
     * @param expirationDate 保质期天数
     * @return
     */
    private static String differentDays(Date date, Integer expirationDate) {

        Integer validDays = 0;

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        //不同年
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                //闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            validDays = timeDistance + (day2 - day1);
        } else {
            // 同一年
            validDays = day2 - day1;
        }

        if (expirationDate - validDays <= 0) {
            return "已过期";
        } else {

            Integer remainingValidDays = expirationDate - validDays;

            return remainingValidDays.toString();
        }
    }


//    /**
//     * @description: 自动拣货任务
//     * @param outDeliveryId
//     */
//    @Transactional
//    @Override
//    public synchronized AjaxResult autoPickTask(Long outDeliveryId) {
//
//        if (outDeliveryId == null){
//            return AjaxResult.error("数据不可为空");
//        }
//
//        // 校验出库计划状态是否正确  要求为未执行状态、已审核、未生成波次计划
//        OutDelivery outDelivery = outDeliveryMapper.selectById(outDeliveryId);
//        if (outDelivery == null){
//            return AjaxResult.error("出库计划不存在");
//        }
//        if (WmsContstants.OUTDELIVERY_DOC_STATUS_ALL.equals(outDelivery.getDocStatus())){
//            return AjaxResult.error("出库计划已完成");
//        }
//        if (WmsContstants.OUTDELIVERY_AUDITOR_STATUS_NO.equals(outDelivery.getAuditorStatus())){
//            return AjaxResult.error("出库计划未审核");
//        }
//        if (StringUtils.isNotEmpty(outDelivery.getMergeCode())){
//            return AjaxResult.error("已生成波次计划，不可拣货");
//        }
//
//        // 查询出库计划详情 不包含已完成数据
//        List<OutDeliveryDetail> detailList = outDeliveryDetailMapper.selectList(
//                new QueryWrapper<OutDeliveryDetail>()
//                        .eq("del_flag", WmsContstants.DEL_FLAG_NO)
//                        .eq("out_delivery_id", outDeliveryId)
//                        .ne("status",WmsContstants.OUTDELIVERYDETAIL_STATUS_ALL));
//        if (detailList.size() == 0){
//            return AjaxResult.error("出库计划数据错误");
//        }
//
//        // 校验是否已经生成拣货任务且任务未完成
//        Long taskCount = taskService.count(new QueryWrapper<Task>()
//                .eq("origin_code", outDelivery.getCode())
//                .eq("source_id", outDelivery.getId())
//                .eq("factory", outDelivery.getOrgId())
//                .eq("current_warehouse_id", outDelivery.getWarehouseId())
//                .eq("task_status", WmsContstants.TASK_STATUS_NO));
//        if (taskCount > 0){
//            return AjaxResult.error("任务执行中，不可重复生成");
//        }
//
//        // 查询拣货策略  未启用批次则统一查询条件  启用批次则每个物料需要单独查询条件
//        String sql = "";
//        boolean strategyByIsBatch = outStrategyService.getStrategyByIsBatch();
//        if (!strategyByIsBatch){
//            sql = outStrategyService.getStrategy(null);
//        }
//        if (StringUtils.isEmpty(sql)){
//            return AjaxResult.error("请检查拣货策略是否已启用");
//        }
//
//        //  标识同一次生成的拣货记录，用于生成拣货任务
//        String batchCode = UUID.randomUUID().toString();
//
//        // 更新库存列表数据
//        List<Stock> stockUpdateList = new ArrayList<>();
//        // 新增出库计划拣货记录
//        List<OutDeliveryDetailRecord> recordInsertList = new ArrayList<>();
//
//        for (int i = 0; i < detailList.size(); i++) {
//            OutDeliveryDetail detail = detailList.get(i);
//
//            // 库存数据
//            List<Stock> stockListData = new ArrayList<>();
//            if (strategyByIsBatch){
//                // 启用批次
//                // 查询物料拣货策略sql
//                String strategy = outStrategyService.getStrategy(detail.getMaterialId());
//                stockListData = stockService.list(
//                        new QueryWrapper<Stock>()
//                                .eq("material_id",detail.getMaterialId())
//                                .eq("warehouse_id",outDelivery.getWarehouseId())
//                                .eq("isFreeze",WmsContstants.STOCK_IS_FREEZE_NO)
//                                .eq("del_flag",WmsContstants.DEL_FLAG_NO)
//                                .last(strategy));
//            }else {
//                // 未启用批次
//                stockListData = stockService.list(
//                        new QueryWrapper<Stock>()
//                                .eq("material_id",detail.getMaterialId())
//                                .eq("warehouse_id",outDelivery.getWarehouseId())
//                                .eq("isFreeze",WmsContstants.STOCK_IS_FREEZE_NO)
//                                .eq("del_flag",WmsContstants.DEL_FLAG_NO)
//                                .last(sql));
//            }
//
//            // 校验库存
//            if (stockListData.size() == 0){
//                return AjaxResult.error("无库存数据");
//            }
//
//            // 去除冻结的区域、库区下的仓库  立体库冻结库区、平库冻结区域
//            List<Long> warehouseIds = new ArrayList<>();
//            warehouseIds.add(outDelivery.getWarehouseId());
//            Map<Long, Warehouse> warehouseMap = cimsFegin.getWarehouseByIds(warehouseIds);
//
//            Warehouse warehouseData = warehouseMap.get(outDelivery.getWarehouseId());
//
//            List<Stock> stockList = new ArrayList<>();
//
//
//            if (warehouseData != null){
//                // 仓库为平库的场合
//                if (WmsContstants.WAREHOUSETYPE_2.equals(warehouseData.getType())){
//                    // 获取仓库下启用区域
//                    List<Long> checkAreaData = cimsFegin.checkArea(warehouseIds);
//                    if (checkAreaData.size() == 0){
//                        return AjaxResult.error("无可用库存数据");
//                    }
//
//                    stockListData.forEach(stock -> checkAreaData.forEach(data ->{
//                        if (stock.getAreaId().equals(data)){
//                            stockList.add(stock);
//                        }
//                    }));
//                } else {
//                    // 查询cims模块  获取库位信息
//                    List<Long> locationIds = stockListData.stream().map(Stock::getLocationId).collect(Collectors.toList());
//                    Map<Long, LocationVO> locationMap = cimsFegin.getLocationByIds(locationIds);
//
//                    // 获取仓库下启用库区
//                    List<Long> checkReservoirData = cimsFegin.checkReservoir(warehouseIds);
//                    if (checkReservoirData.size() == 0){
//                        return AjaxResult.error("无可用库存数据");
//                    }
//
//                    stockListData.forEach(stock -> {
//
//                        LocationVO locationVO = locationMap.get(stock.getLocationId());
//
//                        // 库位启用状态时判断库区是否启用
//                        if (!"0".equals(locationVO.getGoodsAllocationStatus())){
//
//                            checkReservoirData.forEach(data -> {
//                                if (locationVO.getReservoirId().equals(data)){
//                                    stockList.add(stock);
//                                }
//                            });
//                        }
//                    });
//                }
//            }
//
////            R<List<Long>> checkWarehouse = remoteWarehouseService.checkWarehouse(warehouseIds, SecurityConstants.INNER);
////            if(R.FAIL == checkWarehouse.getCode()){
////                throw new ServiceException(checkWarehouse.getMsg());
////            }
////            List<Long> checkWarehouseData = checkWarehouse.getData();
////            if (checkWarehouseData.size() == 0){
////                return AjaxResult.error("无可用库存数据");
////            }
////
////            stockListData.forEach(stock -> checkWarehouseData.forEach(data ->{
////                if (stock.getWarehouseId().equals(data)){
////                    stockList.add(stock);
////                }
////            }));
//
//            // 预计拣货数量
//            long predictCount = 0;
//            if (WmsContstants.OUTDELIVERYDETAIL_STATUS_LIT.equals(detail.getStatus())){
//                // 部分拣货时
//                predictCount = detail.getPredictCount().longValue()-detail.getReceiveCount().longValue();
//            }else {
//                // 未拣货时
//                predictCount = detail.getPredictCount().longValue();
//            }
//            long predictCountCopy = predictCount;
//
//            // 库存可用数量
//            Long stockSum = stockList.stream().map(e -> e.getAvailableCount()).reduce(Long::sum).get();
//
//            if (predictCount > stockSum){
//                return AjaxResult.error("库存可用数量不足");
//            }
//
//            // 拣货托盘分配  如果第一条数据未满足拣货数量，则继续往下扣除，直至满足拣货数量或到达库存总数
//            for (int j = 0; j < stockList.size(); j++) {
//                Stock stock = stockList.get(j);
//                if (stock.getAvailableCount().longValue() >= predictCount){
//
//                    // 插入拣货记录，记录对应的托盘等数据
//                    OutDeliveryDetailRecord detailRecord = new OutDeliveryDetailRecord();
//                    detailRecord.setLocationId(stock.getLocationId());
//                    detailRecord.setMaterialId(stock.getMaterialId());
//                    detailRecord.setBatchNumber(stock.getBatchCode());
//                    detailRecord.setOutDeliveryDetailId(detail.getId());
//                    detailRecord.setPredictCount(predictCount);
//                    detailRecord.setTrayId(stock.getTrayId());
//                    detailRecord.setReceiveCount(0L);
//                    detailRecord.setAreaId(stock.getAreaId());
//                    detailRecord.setBatchCode(batchCode);
//                    detailRecord.setPosition(stock.getPosition());
////                    outDeliveryDetailRecordService.save(detailRecord);
//                    recordInsertList.add(detailRecord);
//                    // 更新库存的可用数量
//                    stock.setAvailableCount(stock.getAvailableCount().longValue() - predictCount);
////                    stockService.updateById(stock);
//                    stockUpdateList.add(stock);
//                    break;
//                }else {
//
//                    // 插入拣货记录，记录对应的托盘等数据
//                    OutDeliveryDetailRecord detailRecord = new OutDeliveryDetailRecord();
//                    detailRecord.setLocationId(stock.getLocationId());
//                    detailRecord.setMaterialId(stock.getMaterialId());
//                    detailRecord.setBatchNumber(stock.getBatchCode());
//                    detailRecord.setOutDeliveryDetailId(detail.getId());
//                    detailRecord.setReceiveCount(0L);
//                    detailRecord.setTrayId(stock.getTrayId());
//                    detailRecord.setAreaId(stock.getAreaId());
//                    detailRecord.setBatchCode(batchCode);
//                    detailRecord.setPosition(stock.getPosition());
//
//                    detailRecord.setPredictCount(stock.getAvailableCount());
//                    // 更新库存的可用数量
//                    stock.setAvailableCount(0L);
//                    stockUpdateList.add(stock);
//                    recordInsertList.add(detailRecord);
////                    stockService.updateById(stock);
////                    outDeliveryDetailRecordService.save(detailRecord);
//                }
//            }
//        }
//
//        // 新增出库计划拣货记录
//        outDeliveryDetailRecordService.saveBatch(recordInsertList);
//        // 修改库存可用数量
//        stockService.updateBatchById(stockUpdateList);
//
//        // 生成拣货任务到任务列表
//        List<Long> collect = detailList.stream().map(OutDeliveryDetail::getId).collect(Collectors.toList());
//        AddTaskDTO addTaskDTO = new AddTaskDTO();
//        addTaskDTO.setType(WmsContstants.TASK_TYPE_PICK);
//        addTaskDTO.setOutDelivery(outDelivery);
//        addTaskDTO.setBatchCode(batchCode);
//        addTaskDTO.setOutDeliveryIdList(collect);
//        addTaskDTO.setPickType(WmsContstants.PICK_TASK_TYPE_DELIVERY);
//        AjaxResult result = taskService.addTask(addTaskDTO);
//        if (String.valueOf(HttpStatus.ERROR).equals(result.get("code").toString())){
//            throw new ServiceException(result.get("msg").toString());
//        }
//
//        return AjaxResult.success();
//    }


    //    /**
//     * 查询出库库存列表
//     * @param stock
//     * @return
//     */
//    @Override
//    public List<StockVo> selectStockListByOut(StockVo stock){
//
//        List<StockVo> stockList = new ArrayList<>();
//
//        List<StockVo> stockListData = stockMapper.selectStockListByOut(stock);
//
//        // 排除禁用区域，库区，库位
//        // 去除冻结的区域、库区下的仓库  立体库冻结库区、平库冻结区域
//        List<Long> warehouseIds = new ArrayList<>();
//        warehouseIds.add(stock.getWarehouseId());
//        Map<Long, Warehouse> warehouseMap = cimsFegin.getWarehouseByIds(warehouseIds);
//
//        Warehouse warehouseData = warehouseMap.get(stock.getWarehouseId());
//
//        if (warehouseData != null){
//            // 仓库为平库的场合
//            if (WmsContstants.WAREHOUSETYPE_2.equals(warehouseData.getType())){
//                // 获取仓库下启用区域
//                List<Long> checkAreaData = cimsFegin.checkArea(warehouseIds);
//                if (checkAreaData.size() == 0){
//                    return new ArrayList<>();
//                }
//
//                stockListData.forEach(model -> checkAreaData.forEach(data ->{
//                    if (model.getAreaId().equals(data)){
//                        stockList.add(model);
//                    }
//                }));
//            } else {
//                // 查询cims模块  获取库位信息
//                List<Long> locationIds = stockListData.stream().map(Stock::getLocationId).collect(Collectors.toList());
//                Map<Long, LocationVO> locationMap = cimsFegin.getLocationByIds(locationIds);
//
//                // 获取仓库下启用库区
//                List<Long> checkReservoirData = cimsFegin.checkReservoir(warehouseIds);
//                if (checkReservoirData.size() == 0){
//                    return new ArrayList<>();
//                }
//
//                stockListData.forEach(model -> {
//
//                    LocationVO locationData = locationMap.get(model.getLocationId());
//                    if (locationData != null) {
//                        // 库位启用状态时判断库区是否启用
//                        if (!"0".equals(locationData.getGoodsAllocationStatus())){
//
//                            for (Long data:checkReservoirData) {
//                                if (locationData.getReservoirId() - data == 0){
//                                    // 库位编码
//                                    model.setLocationCode(locationData.getCode());
//                                    // 库位名称
//                                    model.setLocationName(locationData.getName());
//                                    // 区域
//                                    model.setAreaName(locationData.getAreaName());
//                                    // 库区
//                                    model.setReservoirName(locationData.getReservoirName());
//
//                                    model.setNarrowAisleNum(locationData.getNarrowAisleNum());
//
//                                    stockList.add(model);
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        }
//
//        if (stockList.size() > 0){
//
////            Tray tray = null;
//
////            for(int i = 0; i < stockList.size(); i++){
////
////                if (ObjectUtils.isNotNull(stockList.get(i).getTrayId())){
////                    // 根据托盘id获取托盘信息
////                    tray = trayMapper.selectTrayById(stockList.get(i).getTrayId());
////
////                    if (ObjectUtils.isNotNull(tray)){
////                        stockList.get(i).setTrayCode(tray.getCode());
////                    }
////                }
////            }
//
//            // 物料id列表
//            List<Long> materiaIds = stockList.stream().map(StockVo::getMaterialId).collect(Collectors.toList());
//            // 获取物料详情信息
//            Map<Long, Material> materiaMap = cimsFegin.getMateriaByIds(materiaIds);
//
//
////            // 查询cims模块  获取库位信息
////            List<Long> locationIds = stockList.stream().map(StockVo::getLocationId).collect(Collectors.toList());
////            Map<Long, LocationVO> locationMap = cimsFegin.getLocationByIds(locationIds);
//
//            stockList.forEach(model -> {
//
//                Material materiaData = materiaMap.get(model.getMaterialId());
//                if (materiaData != null) {
//                    // 物料编码
//                    model.setMaterialCode(materiaData.getCode());
//                    // 物料名称
//                    model.setMaterialName(materiaData.getName());
//                    // 单位名称
//                    model.setUnitName(materiaData.getUnitName());
//                    model.setSpecifications(materiaData.getSpecifications());
//                }
////                Location locationData = locationMap.get(model.getLocationId());
////                if (locationData != null) {
////                    // 库位编码
////                    model.setLocationCode(locationData.getCode());
////                    // 库位名称
////                    model.setLocationName(locationData.getName());
////                }
//
//            });
//
//
//            if (warehouseData != null){
//                // 仓库为平库的场合
//                if (WmsContstants.WAREHOUSETYPE_2.equals(warehouseData.getType())){
//                    // 查询cims模块  获取区域信息
//                    List<Long> areaIds = stockList.stream().map(StockVo::getAreaId).collect(Collectors.toList());
//                    Map<Long, Area> areaMap = cimsFegin.getAreaListById(areaIds);
//
//                    stockList.forEach(model -> {
//                        Area area = areaMap.get(model.getAreaId());
//                        if (area != null) {
//                            // 区域名称
//                            model.setAreaName(area.getName());
//                        }
//                    });
//                }
//            }
//        }
//
//        return stockList;
//    }
    @Transactional
    @Override
    public AjaxResult unFreeLocation(String locationsId, String isFreeze, String originType) {

        List<TStock> stockList = tStockMapper.selectList(Wrappers.lambdaQuery(TStock.class)
                .eq(TStock::getLocationId, Long.parseLong(locationsId))
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO)
                .eq(TStock::getStatus, Constants.LOCATION_STATUS_NO)
        );
        List<Long> stockIds = new ArrayList<>();
        // 物料id列表
        List<Long> materiaIds = new ArrayList<>();

        // 冻结校验
        if (Constants.STOCK_IS_FREEZE_YES.equals(isFreeze)) {
            for (TStock model : stockList) {
                stockIds.add(model.getId());
                // 判断该条数据是否被冻结
                if (Constants.STOCK_IS_FREEZE_YES.equals(model.getIsFreeze())) {
                    materiaIds.add(model.getMaterialId());
                }
            }
            if (materiaIds.size() > 0) {
                // 获取物料详情信息
                List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                        .in(TMaterial::getId, materiaIds)
                        .eq(TMaterial::getDelFlag, Constants.NO));
                String msg = "";
                for (TMaterial material : tMaterials) {
                    msg += "," + material.getName();
                }
                // 数据已经被冻结
                return AjaxResult.error(msg.substring(1) + "已经冻结，请刷新后重试");
            }

        } else {
            List<Long> updateStockIds = new ArrayList<>();
            for (TStock model : stockList) {
                // 冻结的数据判断是否是同类型解冻
                if (Constants.STOCK_IS_FREEZE_YES.equals(model.getIsFreeze())) {
                    updateStockIds.add(model.getId());
                    // 判断该条数据冻结来源是否一致
                    if (!originType.equals(model.getOriginType())) {
                        materiaIds.add(model.getMaterialId());
                    }
                }
            }
            if (materiaIds.size() > 0) {
                // 获取物料详情信息
                List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                        .eq(TMaterial::getDelFlag, Constants.NO)
                        .in(TMaterial::getId, materiaIds));
                String msg = "";
                for (TMaterial material : tMaterials) {
                    msg += "," + material.getName();
                }
                // 数据已经被冻结
                return AjaxResult.error(msg.substring(1) + "解冻类型和当前解冻来源不同，无法解冻");
            }
            originType = "";
            // 只更新冻结的数据
            stockIds = updateStockIds;
        }
        if (stockIds.size() > 0) {
            int rows = tStockMapper.updateFreezeByIds(stockIds, isFreeze, originType);
            return rows > 0 ? AjaxResult.success() : AjaxResult.error();
        }
        return AjaxResult.success();
    }

    /**
     * pda库存查询
     *
     * @param stockVo
     * @return
     */
    @Override
    public List<StockVo> getStockList(StockVo stockVo) {
        return tStockMapper.getStockList(stockVo);
    }

    @Override
    public List<Map<String, Object>> getMaterialBatchByLocationId(List<Long> locationIds) {
        return tStockMapper.getMaterialBatchByLocationId(locationIds);
    }

    @Override
    public List<TStock> getStockByMaterialList(Long materialId) {
        return tStockMapper.getStockByMaterialList(materialId);
    }

    /**
     * 直接移库
     *
     * @param stockMoveApiDTO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult directTransfer(TStockMoveApiDTO stockMoveApiDTO) {
        TLocation location = tLocationMapper.selectOne(new LambdaQueryWrapper<TLocation>()
                .eq(TLocation::getDelFlag, Constants.DEL_FLAG_NO)
                .eq(TLocation::getCode, stockMoveApiDTO.getLocationCode()));
        if (location == null) {
            return AjaxResult.error("未查询到目标库位相关信息");
        }
        List<String> rfids = Collections.singletonList(stockMoveApiDTO.getRfid());
        List<TStockInDTO> stockInDTOList = tMaterialDetailMapper.selectStockMaterialByRfidIds(rfids);
        for (TStockInDTO stockInDTOS : stockInDTOList) {
            this.moveStock(stockInDTOS, stockMoveApiDTO.getStockId(), location.getId(), location.getAreaId());
        }
        //更新物料详情载具信息
        tMaterialDetailMapper.updateInfoByRfIds(rfids,null, location.getId(),null);
        return AjaxResult.success();
    }

    /**
     * 根据物料和批次号查询在库信息
     * @param stockMoveApiDTO
     * @return
     */
    @Override
    public List<StockVo> getStockListByMaterial(TStockMoveApiDTO stockMoveApiDTO) {
        List<StockVo> list = tStockMapper.getStockListByMaterial(stockMoveApiDTO.getRfid());
        return list;
    }

    /**
     * 单个物料入库信息
     */
    @Transactional
    public void moveStock(TStockInDTO stockInDTO, Long stockId, Long targetLocationId, Long targetAreaId) {
        //todo wxr 处理库存记录 物料+库位+批次号
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setMaterialId(stockInDTO.getMaterialId());
        tStockDetail.setType(Constants.WCS_TASK_TYPE_MOVE);
        tStockDetail.setLocationId(targetLocationId);
        tStockDetail.setOriginCode(stockInDTO.getOriginCode());
        tStockDetail.setOriginId(-2L);
        tStockDetail.setStatus("0");
        tStockDetail.setBatchCode(stockInDTO.getBatchCode());
        tStockDetail.setCurrentCount(stockInDTO.getCount());

        //查询库存之前的数量
        TStockDetail stockDetailDTO = new TStockDetail();
        stockDetailDTO.setLocationId(targetLocationId);
        stockDetailDTO.setMaterialId(stockInDTO.getMaterialId());
        stockDetailDTO.setBatchCode(stockInDTO.getBatchCode());
        Long existCount = tStockDetailMapper.selectTStockDetailCountParam(stockDetailDTO);
        if (existCount == null) {
            tStockDetail.setBeforeCount(0L);// 操作前数量
            tStockDetail.setCurrentCount(stockInDTO.getCount());// 操作后当前数量
        } else {
            tStockDetail.setBeforeCount(existCount);// 操作前数量
            tStockDetail.setCurrentCount(existCount + stockInDTO.getCount());// 操作后当前数量
        }
        tStockDetailMapper.insert(tStockDetail);
        //查询原库位的物料库存信息
        TStock modelOrg =  this.getById(stockId);
//        TStock modelOrg = this.getOne(new LambdaQueryWrapper<TStock>()
//                .eq(TStock::getLocationId, stockInDTO.getLocationId())//库位id不同
//                .eq(TStock::getMaterialId, stockInDTO.getMaterialId())
//                .eq(TStock::getBatchCode, stockInDTO.getBatchCode())
//                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        if(modelOrg != null){
            Long residueCount = modelOrg.getCount() - stockInDTO.getCount();
            Long residueAvaCount = modelOrg.getAvailableCount() - stockInDTO.getCount();
            if(residueCount == 0L && residueAvaCount == 0L){
                tStockMapper.deleteTStockById(modelOrg.getId());
            } else{
                TStock stockUpdate = new TStock();
                // 更新库存数据
                stockUpdate.setId(modelOrg.getId());
                stockUpdate.setCount(modelOrg.getCount() + stockInDTO.getCount());
                stockUpdate.setAvailableCount(modelOrg.getAvailableCount() + stockInDTO.getCount());
                this.updateById(stockUpdate);
            }
        }
        //查询目标库位的物料库存信息
        TStock modelTarget = this.getOne(new LambdaQueryWrapper<TStock>()
                .eq(TStock::getLocationId, targetLocationId)//库位id不同
                .eq(TStock::getMaterialId, stockInDTO.getMaterialId())
                .eq(TStock::getBatchCode, stockInDTO.getBatchCode())
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        if(modelTarget != null){
            TStock stockUpdate = new TStock();
            // 更新库存数据
            stockUpdate.setId(modelTarget.getId());
            stockUpdate.setCount(modelTarget.getCount() + stockInDTO.getCount());
            stockUpdate.setAvailableCount(modelTarget.getAvailableCount() + stockInDTO.getCount());
            this.updateById(stockUpdate);
        }else{
            modelTarget = new TStock();
            modelTarget.setAreaId(targetAreaId);
            // 可用数量
            modelTarget.setAvailableCount(stockInDTO.getCount());
            // 开始时间
            modelTarget.setBeginDate(new Date());
            modelTarget.setLocationId(targetLocationId);
            modelTarget.setMaterialId(stockInDTO.getMaterialId());
            modelTarget.setBatchCode(stockInDTO.getBatchCode());
            modelTarget.setProducedDate(stockInDTO.getProducedDate());
            modelTarget.setCount(stockInDTO.getCount());
            modelTarget.setAvailableCount(stockInDTO.getCount());
            modelTarget.setTrayId(stockInDTO.getTrayId());
            modelTarget.setOriginType(Constants.WCS_TASK_TYPE_MOVE);
            modelTarget.setStatus("0");
            modelTarget.setIsFreeze("0");
            this.save(modelTarget);
        }
        //查询物料主信息，一个物料一条
//        TStockMain stockMain = tStockMainMapper.selectOne(new LambdaQueryWrapper<TStockMain>()
//                .eq(TStockMain::getMaterialId, stockInDTO.getMaterialId())
//                .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));
//
//        if(stockMain != null){
//            // 更新库存总数据
//            TStockMain stockMainUpdate = new TStockMain();
//            stockMainUpdate.setId(stockMain.getId());
//            stockMainUpdate.setLibraryCount(stockMain.getLibraryCount() + stockInDTO.getCount().intValue());
//            stockMainUpdate.setAvailableCount(stockMain.getAvailableCount() + stockInDTO.getCount().intValue());
//            tStockMainMapper.updateById(stockMainUpdate);
//        }else{
//            stockMain = new TStockMain();
//            stockMain.setMaterialId(stockInDTO.getMaterialId());
//            stockMain.setLibraryCount(stockInDTO.getCount());
//            stockMain.setAvailableCount(stockInDTO.getCount());
//            tStockMainMapper.insert(stockMain);
//        }
    }

    /**
     * 入库存--通用款式
     * @param stockInDTO
     */
    @Transactional
    public void moveInfoStock(TStockInDTO stockInDTO) {
        //todo wxr 处理库存记录 物料+库位+批次号
        Long count = stockInDTO.getCount();
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setMaterialId(stockInDTO.getMaterialId());
        tStockDetail.setType(stockInDTO.getType());
        tStockDetail.setLocationId(stockInDTO.getLocationId());
        tStockDetail.setOriginCode(stockInDTO.getOriginCode());
        tStockDetail.setOriginId(stockInDTO.getOriginId());
        tStockDetail.setStatus(Constants.STOCK_USE_YES);
        tStockDetail.setBatchCode(stockInDTO.getBatchCode());
        tStockDetail.setCurrentCount(count);

        //查询库存之前的数量
        TStockDetail stockDetailDTO = new TStockDetail();
        stockDetailDTO.setLocationId(stockInDTO.getLocationId());
        stockDetailDTO.setMaterialId(stockInDTO.getMaterialId());
        stockDetailDTO.setBatchCode(stockInDTO.getBatchCode());
        Long existCount = tStockDetailMapper.selectTStockDetailCountParam(stockDetailDTO);
        if (existCount == null) {
            tStockDetail.setBeforeCount(0L);// 操作前数量
            tStockDetail.setCurrentCount(count);// 操作后当前数量
        } else {
            tStockDetail.setBeforeCount(existCount);// 操作前数量
            tStockDetail.setCurrentCount(existCount + count);// 操作后当前数量
        }
        tStockDetailMapper.insert(tStockDetail);
        //查询原库位的物料库存信息
        //一个物料一个托盘一条
        TStock model = null;
        if(stockInDTO.getStockId() != null){
            model = tStockMapper.selectById(stockInDTO.getStockId());
        }else{
            model = this.getOne(new LambdaQueryWrapper<TStock>()
                    .eq(TStock::getLocationId, stockInDTO.getLocationId())
                    .eq(TStock::getMaterialId, stockInDTO.getMaterialId())
                    .eq(TStock::getBatchCode, stockInDTO.getBatchCode())
                    .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));
        }
        if(model != null){
            Long receiveCount = model.getCount() - stockInDTO.getCount();
            Long aCount = model.getAvailableCount() - stockInDTO.getCount();
            if(receiveCount== 0L && aCount == 0L){
                //数据为0则删除此数据
                tStockMapper.deleteTStockById(model.getId());
            }else{
                TStock stockUpdate = new TStock();
                // 更新库存数据
                stockUpdate.setId(model.getId());
                stockUpdate.setCount(model.getCount() + stockInDTO.getCount());
                stockUpdate.setAvailableCount(model.getAvailableCount() + stockInDTO.getCount());
                tStockMapper.updateById(stockUpdate);
            }
        }else{
            model = new TStock();
            model.setAreaId(stockInDTO.getAreaId());
            // 可用数量
            model.setAvailableCount(stockInDTO.getCount());
            // 开始时间
            model.setBeginDate(new Date());
            model.setLocationId(stockInDTO.getLocationId());
            model.setMaterialId(stockInDTO.getMaterialId());
            model.setBatchCode(stockInDTO.getBatchCode());
            model.setProducedDate(stockInDTO.getProducedDate());
            model.setCount(stockInDTO.getCount());
            model.setAvailableCount(stockInDTO.getCount());
            model.setTrayId(stockInDTO.getTrayId());
            model.setOriginType(stockInDTO.getType());
            model.setStatus(Constants.STOCK_USE_YES);
            model.setIsFreeze(Constants.STOCK_IS_FREEZE_NO);
            tStockMapper.insert(model);
        }
        //查询物料主信息，一个物料一条
        TStockMain stockMain = tStockMainMapper.selectOne(new LambdaQueryWrapper<TStockMain>()
                .eq(TStockMain::getMaterialId, stockInDTO.getMaterialId())
                .eq(TStockMain::getDelFlag, Constants.DEL_FLAG_NO));

        if(stockMain != null){
            Long receiveCount = stockMain.getLibraryCount() - stockInDTO.getCount();
            Long aCount = stockMain.getAvailableCount() - stockInDTO.getCount();
            if(receiveCount== 0L && aCount == 0L){
                //数据为0则删除此数据
                tStockMainMapper.deleteTStockMainById(stockMain.getId());
            }else{
                // 更新库存总数据
                TStockMain stockMainUpdate = new TStockMain();
                stockMainUpdate.setId(stockMain.getId());
                stockMainUpdate.setLibraryCount(stockMain.getLibraryCount() + stockInDTO.getCount().intValue());
                stockMainUpdate.setAvailableCount(stockMain.getAvailableCount() + stockInDTO.getCount().intValue());
                tStockMainMapper.updateById(stockMainUpdate);
            }
        }else{
            stockMain = new TStockMain();
            stockMain.setMaterialId(stockInDTO.getMaterialId());
            stockMain.setLibraryCount(stockInDTO.getCount());
            stockMain.setAvailableCount(stockInDTO.getCount());
            tStockMainMapper.insert(stockMain);
        }
    }

    /**
     * 获取库存信息列表
     * @param stockListDTO
     * @return
     */
    @Override
    public List<TStockListVo> stockList(StockListDTO stockListDTO) {
        if (stockListDTO.getBeginDate()!=null){
            stockListDTO.setBeginDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 00:00:00",stockListDTO.getBeginDate())));
        }
        if (stockListDTO.getEndDate()!=null){
            stockListDTO.setEndDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 23:59:59",stockListDTO.getEndDate())));
        }
        return tStockMapper.selectListByParam(stockListDTO);
    }


}
