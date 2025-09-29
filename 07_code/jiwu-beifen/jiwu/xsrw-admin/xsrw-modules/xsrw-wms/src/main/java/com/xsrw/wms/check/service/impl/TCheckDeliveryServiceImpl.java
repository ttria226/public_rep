package com.xsrw.wms.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.domain.TCheckHistory;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.vo.CheckDeliveryVO;
import com.xsrw.wms.check.mapper.TCheckDeliveryMapper;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.check.service.ITCheckHistoryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 盘点计划Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TCheckDeliveryServiceImpl extends ServiceImpl<TCheckDeliveryMapper, TCheckDelivery> implements ITCheckDeliveryService
{
    @Autowired
    private TCheckDeliveryMapper tCheckDeliveryMapper;

    @Autowired
    private TStockMapper tStockMapper;

    @Autowired
    private ITLocationService itLocationService;

    @Autowired
    private ITUnitService itUnitService;

    @Autowired
    private ITAreaService itAreaService;

    @Autowired
    private ITReservoirService itReservoirService;

    @Autowired
    private ITMaterialService itMaterialService;

    @Autowired
    private ITUnitConfigService itUnitConfigService;

    @Autowired
    private TLocationMapper locationMapper;

    @Autowired
    private ITCodeConfigService itCodeConfigService;

    @Autowired
    private ITTaskService taskService;

    @Autowired
    private ITTaskDetailService taskDetailService;

    @Autowired
    private TTaskDetailMapper taskDetailMapper;

    @Autowired
    private ITCheckHistoryService checkHistoryService;

    @Autowired
    private ITMaterialService materialService;

    @Autowired
    private ITStockService stockService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    /**
     * 查询盘点计划列表
     *
     * @param checkDelivery 盘点计划
     * @return 盘点计划
     */
    @Override
    public List<CheckDeliveryVO> selectTCheckDeliveryList(CheckDeliveryDTO checkDelivery)
    {
        //查询列表
        List<CheckDeliveryVO> list = tCheckDeliveryMapper.selectCheckDeliveryList(checkDelivery);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(e -> {
                //查询物料
                TMaterial material = itMaterialService.getById(e.getMaterialId());
                if (material != null) {
                    // 物料编码
                    e.setMaterialCode(material.getCode());
                    // 物料名称
                    e.setMaterialName(material.getName());
                    // 规格型号
                    e.setSpecifications(material.getSpecifications());
                    // 单位名称
                    TUnit unit = itUnitService.getById(material.getUnitId());
                    e.setUnitName(unit.getName());
                }

                //查询库位
                TLocation location = itLocationService.getById(e.getLocationId());
                if (location != null) {
                    // 库位编码
                    e.setLocationCode(location.getCode());
                    // 库位名称
                    e.setLocationName(location.getName());
                    // 区域名称
                    TArea area = itAreaService.getById(location.getAreaId());
                    e.setAreaName(area.getName());
                    // 库区名称
                    TReservoir reservoir = itReservoirService.getById(location.getReservoirId());
                    e.setReservoirName(reservoir.getName());
                }

                // 区域名称
                TArea area = itAreaService.getById(e.getAreaId());
                if (StringUtils.isNotNull(area)){
                    e.setAreaName(area.getName());
                }
                TReservoir reservoir = itReservoirService.getById(e.getReservoirId());
                if (StringUtils.isNotNull(reservoir)) {
                    // 库区名称
                    e.setReservoirName(reservoir.getName());
                    // 库区状态
                    e.setReservoirStatus(reservoir.getStatus());
                }

                // 包装单位
                TUnitConfig unitConfig = itUnitConfigService.getOne(Wrappers.lambdaQuery(TUnitConfig.class)
                        .eq(TUnitConfig::getMaterialId,e.getMaterialId())
                        .eq(TUnitConfig::getDelFlag,Constants.NO));
                if (unitConfig != null) {
                    // 包装方式
                    e.setPackUnitName(unitConfig.getMaxUnitName());
                }
            });
        }
        return list;
    }

    /**
     * 查询盘点计划
     *
     * @param id 盘点计划主键
     * @return 盘点计划
     */
    @Override
    public TCheckDelivery selectTCheckDeliveryById(Long id)
    {
        return tCheckDeliveryMapper.selectById(id);
    }

    /**
     * 新增盘点计划
     *
     * @param data 盘点计划
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertTCheckDelivery(Map<String,Object> data)
    {
        if (CollectionUtils.isEmpty(data)){
            return AjaxResult.error("参数不可为空");
        }

        // 载具类型
        String trayType = data.get("trayType").toString();
        // 盘点计划数据
        List<Map<String,Object>> maps = (List<Map<String, Object>>) data.get("data");
        List<TCheckDelivery> deliveryList = new ArrayList<>();
        maps.forEach(e -> {
            TCheckDelivery checkDelivery = new ObjectMapper().convertValue(e, TCheckDelivery.class);
            deliveryList.add(checkDelivery);
        });

        // 盘点维度
        String checkType = data.get("checkType").toString();

        // 物料、库区 维度
        if (Constants.CHECK_DELIVERY_MATERIAL.equals(checkType) || Constants.CHECK_DELIVERY_LOCATION.equals(checkType)){
            for (TCheckDelivery delivery : deliveryList) {
                QueryWrapper<TStock> stockQueryWrapper = new QueryWrapper<>();
                stockQueryWrapper.eq("material_id", delivery.getMaterialId());
                if (delivery.getReservoirId() != null) {
                    // 查询库区下所有库位
                    List<TLocation> locationList = itLocationService.locationList(delivery.getAreaId(), delivery.getReservoirId());
                    if (locationList.size() == 0) {
                        return AjaxResult.error("该库区下无库位信息");
                    }
                    List<Long> collect = locationList.stream().map(TLocation::getId).collect(Collectors.toList());
                    stockQueryWrapper.in("location_id", collect);
                }
                if (delivery.getAreaId() != null) {
                    stockQueryWrapper.eq("area_id", delivery.getAreaId());
                }

                List<TStock> stockList = tStockMapper.selectList(stockQueryWrapper);
                List<TStock> unFinishList = stockList.stream()
                        .filter(e -> Constants.STOCK_IS_FREEZE_YES.equals(e.getIsFreeze())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(unFinishList)) {
                    return AjaxResult.error("所选物料库存已全部冻结不可生成盘点计划");
                }

                // 新增盘点计划
                TCheckDelivery checkDelivery = new TCheckDelivery();
                checkDelivery.setCheckType(checkType);
                checkDelivery.setMaterialId(delivery.getMaterialId());
                checkDelivery.setReservoirId(delivery.getReservoirId());
                checkDelivery.setAreaId(delivery.getAreaId());
                checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
                checkDelivery.setTrayType(trayType);
                tCheckDeliveryMapper.insert(checkDelivery);
            }
        }

        // 动碰盘点
        if (Constants.CHECK_DELIVERY_HISTORY.equals(checkType)){
            TCheckDelivery checkDelivery = new TCheckDelivery();
            checkDelivery.setCheckType(checkType);
            checkDelivery.setTrayType(trayType);
            checkDelivery.setStartTime(deliveryList.get(0).getStartTime());
            checkDelivery.setEndTime(deliveryList.get(0).getEndTime());
            checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
            tCheckDeliveryMapper.insert(checkDelivery);
        }

        // 随机盘点
        if (Constants.CHECK_DELIVERY_RNADOM.equals(checkType)){
            TCheckDelivery checkDelivery = new TCheckDelivery();
            checkDelivery.setCheckType(checkType);
            checkDelivery.setTrayType(trayType);
            checkDelivery.setReservoirId(deliveryList.get(0).getReservoirId());
            checkDelivery.setAreaId(deliveryList.get(0).getAreaId());
            checkDelivery.setRandomNum(deliveryList.get(0).getRandomNum());
            checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
            tCheckDeliveryMapper.insert(checkDelivery);
        }

        // 空库位盘点
        if (Constants.CHECK_DELIVERY_EMPTY.equals(checkType)){
            TCheckDelivery checkDelivery = new TCheckDelivery();
            checkDelivery.setCheckType(checkType);
            checkDelivery.setTrayType(trayType);
            checkDelivery.setReservoirId(deliveryList.get(0).getReservoirId());
            checkDelivery.setAreaId(deliveryList.get(0).getAreaId());
            checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
            tCheckDeliveryMapper.insert(checkDelivery);
        }

        return AjaxResult.success();
    }

    /**
     * 修改盘点计划
     *
     * @param tCheckDelivery 盘点计划
     * @return 结果
     */
    @Override
    public int updateTCheckDelivery(TCheckDelivery tCheckDelivery)
    {
        return tCheckDeliveryMapper.updateById(tCheckDelivery);
    }


    /**
     * 批量删除盘点计划
     *
     * @param ids 需要删除的盘点计划主键
     * @return 结果
     */
    @Override
    public int deleteTCheckDeliveryByIds(Long[] ids)
    {
        return tCheckDeliveryMapper.deleteTCheckDeliveryByIds(ids);
    }

    /**
     * 删除盘点计划信息
     *
     * @param id 盘点计划主键
     * @return 结果
     */
    @Override
    public int deleteTCheckDeliveryById(Long id)
    {
        return tCheckDeliveryMapper.deleteTCheckDeliveryById(id);
    }


    /**
     * PDA查询库位信息
     * @param code
     * @return
     */
    @Override
    public AjaxResult locationInfo(String code) {

        TLocation location = locationMapper.selectOne(
                new QueryWrapper<TLocation>()
                        .eq("del_flag", Constants.DEL_FLAG_NO)
                        .eq("code", code));
        if (location == null){
            return AjaxResult.error("库位不存在");
        }

        // 查询库存信息
        List<TStock> stockList = tStockMapper.selectList(
                new QueryWrapper<TStock>()
                        .eq("location_id", location.getId())
                        .eq("status", "0")
                        .eq("del_flag", Constants.DEL_FLAG_NO));

        // 查询物料信息
        List<Long> collect = stockList.stream().map(TStock::getMaterialId).collect(Collectors.toList());
        List<TMaterial> materialList = itMaterialService.list(new QueryWrapper<TMaterial>().in("id", collect));


        List<Map<String,Object>> materialData = new ArrayList<>();
        stockList.forEach(stock -> materialList.forEach(material -> {
            Map<String,Object> map = new HashMap<>();
            if (stock.getMaterialId().equals(material.getId())){
                map.put("materialName",material.getName());
                map.put("stockId",stock.getId());
                map.put("locationName",location.getName());
                map.put("count",stock.getCount());
                map.put("materialId",stock.getMaterialId());
                materialData.add(map);
            }

        }));

        // 冻结库存数据
        List<Long> stockId = stockList.stream().map(TStock::getId).collect(Collectors.toList());
        tStockMapper.update(new TStock(),
                new UpdateWrapper<TStock>()
                        .in("id",stockId)
                        .set("is_freeze",Constants.STOCK_IS_FREEZE_YES));

        Map<String,Object> map = new HashMap<>();
        map.put("locationName",location.getName());
        map.put("materialData",materialData);

        return AjaxResult.success(map);
    }


    /**
     * 提交盘点
     * @param map
     * @return
     */
    @Transactional
    @Override
    public AjaxResult checkData(List<Map<String,Object>> map) {

        // 生成已完成的盘点任务
        TTask task = new TTask();
        task.setTaskType(Constants.TASK_TYPE_CHECK);
        task.setTaskStatus(Constants.TASK_STATUS_END);
        task.setTaskCount(Long.valueOf(map.size()));
        task.setCode(itCodeConfigService.getCode("MRWLB"));
        task.setAuditor(SecurityUtils.getUsername());
        task.setStatus("2");
        task.setCheckType(Constants.CHECK_DELIVERY_DIRECT);
        taskService.save(task);

        // 生成任务详情
        List<TTaskDetail> detailList = new ArrayList<>();
        for (Map<String, Object> objectMap : map) {

            // 查询库存信息
            TStock stockId = tStockMapper.selectById(Long.valueOf(objectMap.get("stockId").toString()));

            TTaskDetail detail = new TTaskDetail();
            detail.setTaskId(task.getId());
            detail.setMaterialId(Long.valueOf(objectMap.get("materialId").toString()));
            detail.setPredictCount(stockId.getCount());
            detail.setActualCount(Long.valueOf(objectMap.get("actualCount").toString()));
            detail.setTrayId(stockId.getTrayId());
            detail.setLocationId(stockId.getLocationId());
            detail.setBatchNumber(stockId.getBatchCode());
            detail.setStockId(stockId.getId());
            detail.setStatus("2");

            detailList.add(detail);
        }
        taskDetailService.saveBatch(detailList);

        return AjaxResult.success();
    }


    @Transactional
    @Override
    public AjaxResult checkdeliverySubmit(CheckDeliveryDTO checkDeliveryDTO) {

        if (ObjectUtils.isEmpty(checkDeliveryDTO)){
            return AjaxResult.error("参数不可为空");
        }

        List<TCheckHistory> data = checkDeliveryDTO.getHistoryList();
        String checkType = checkDeliveryDTO.getCheckType();

        // 校验盘点数据
        for (int i = 0; i < data.size(); i++) {
            TCheckHistory history = data.get(i);
            if (history.getActualCount() == null || history.getActualCount().intValue() < 0){
                return AjaxResult.error("物料实盘数量不可为空且不可为负数");
            }
        }

        List<Long> detailIdList = data.stream().map(e -> e.getTaskDetailId()).distinct().collect(Collectors.toList());


        // 立体库盘点校验任务状态
        TTaskDetail byId = taskDetailService.getById(detailIdList.get(0));

        // 查询任务状态
        TTask tTask = taskService.getById(byId.getTaskId());
        if (Constants.TASK_STATUS_END.equals(tTask.getTaskStatus())){
            return AjaxResult.error("盘点已执行");
        }

        // 查询盘点计划
        TCheckDelivery checkDelivery = tCheckDeliveryMapper.selectOne(
                new QueryWrapper<TCheckDelivery>().eq("id", tTask.getSourceId()));

//        List<TTaskDetail> detailList = taskDetailService.list(new QueryWrapper<TTaskDetail>()
//                .and(i -> i.eq("status", Constants.TASK_DETAIL_STATUS_NO).or().eq("status",Constants.TASK_DETAIL_STATUS_REJECT))
//                .in("id", detailIdList));
//        if (detailList.size() > 0){
//            return AjaxResult.error("请先执行任务！");
//        }


        // 以物料为维度盘点时  校验盘点的物料
        if (Constants.CHECK_DELIVERY_MATERIAL.equals(checkType) ||
                Constants.CHECK_DELIVERY_RNADOM.equals(checkType) ||
                Constants.CHECK_DELIVERY_HISTORY.equals(checkType)) {
            for (int i = 0; i < data.size(); i++) {
                TCheckHistory history = data.get(i);
                // 查询任务详情
                TTaskDetail taskDetail = taskDetailService.getById(history.getTaskDetailId());
                if (!taskDetail.getMaterialId().equals(history.getMaterialId())) {
                    return AjaxResult.error("该物料不在盘点任务范围内，请核实");
                }
            }
        }

        // 以库区为维度盘点时  会存在物料盘盈 库存中无数据的情况 所以校验是否盘点了托盘下的所有物料
        if (Constants.CHECK_DELIVERY_LOCATION.equals(checkType) ||
                Constants.CHECK_DELIVERY_EMPTY.equals(checkType)) {

//            Set detailId = new HashSet();
//            List<Long> collect = data.stream().map(e -> e.getTaskDetailId()).collect(Collectors.toList());
//            detailId.addAll(collect);
//
//            if (detailId.size() > 1){
//                return AjaxResult.error("盘点数据错误");
//            }
            // 校验盘点的物料是否维护
            for (int i = 0; i < data.size(); i++) {
                TCheckHistory checkHistory = data.get(i);
                String materialCode = checkHistory.getMaterialCode();

                TMaterial material = materialService.getCodeById(materialCode);
                if (material == null){
                    return AjaxResult.error ("未查询到物料"+checkHistory.getMaterialCode()+"的信息，请人工处理");
                }
                checkHistory.setMaterialId(material.getId());
            }

//            if (!Constants.CHECK_TRAY_TYPE_LAND.equals(checkDelivery.getTrayType())){
//                // 查询该托盘下需要盘点的物料
//                Long taskDetailId = data.get(0).getTaskDetailId();
//                TTaskDetail taskDetail = taskDetailService.getById(taskDetailId);
//
//                List<TStock> stockList = stockService.list(new QueryWrapper<TStock>()
//                        .eq("tray_id", taskDetail.getTrayId()));
//
//                List<Long> stockMaterial = stockList.stream().map(e -> e.getMaterialId()).distinct().collect(Collectors.toList());
//                List<Long> dataMaterial = data.stream().filter(e -> e.getMaterialId() != null).map(TCheckHistory::getMaterialId).distinct().collect(Collectors.toList());
//                if (stockMaterial.size() != dataMaterial.size()){
//                    return AjaxResult.error("盘点物料与计划不符，请重新扫码");
//                }
//            }

        }


        // 更新任务状态
        taskDetailService.update(new UpdateWrapper<TTaskDetail>()
                .set("status",Constants.TASK_DETAIL_STATUS_END)
                .in("id",detailIdList));

        // 查询是否已全部执行，是的话更新主任务状态为已完成，否执行中

        TTaskDetail taskDetail = taskDetailMapper.selectById(data.get(0).getTaskDetailId());

        TTask task = new TTask();
        task.setId(taskDetail.getTaskId());
//        int unFinishedCount = taskDetailMapper.selectUnFinishedCount(taskDetail.getTaskId(), WmsContstants.TASK_DETAIL_STATUS_END, WmsContstants.TASK_DETAIL_STATUS_APPROVED + ',' + WmsContstants.TASK_DETAIL_STATUS_APPROVE_ING);

        Long unFinishedCount = taskDetailMapper.selectCount(new QueryWrapper<TTaskDetail>()
                .eq("task_id",taskDetail.getTaskId())
                .notIn("status", Constants.TASK_DETAIL_STATUS_END, Constants.TASK_DETAIL_STATUS_APPROVE_ING, Constants.TASK_DETAIL_STATUS_APPROVED));

        if (unFinishedCount > 0) {
            // 执行中
            task.setTaskStatus(Constants.TASK_STATUS_ING);
        } else {
            // 已完成
            task.setTaskStatus(Constants.TASK_STATUS_END);
        }
        taskService.updateById(task);

        // 写入提交数据
        data.forEach(e -> {
            TCheckHistory history = checkHistoryService.getOne(new QueryWrapper<TCheckHistory>()
                    .eq("task_detail_id", e.getTaskDetailId())
                    .eq("stock_id", e.getStockId())
                    .eq("tray_id", e.getTrayId())
                    .eq("rfid_head",e.getRfidHead())
                    .eq("material_code", e.getMaterialCode()));
            if (history != null){
                history.setActualCount(e.getActualCount());
                history.setPredictCount(e.getPredictCount());
                checkHistoryService.updateById(history);
            }else {
                if (e.getMaterialId() == null){
                    TMaterial material = materialService.getCodeById(e.getMaterialCode());
                    e.setMaterialId(material.getId());
                }
                checkHistoryService.save(e);
            }
        });


        return AjaxResult.success();
    }

    @Override
    public AjaxResult executeTask(Long taskId, String trayCode, String checkType, String batch,String rfid) {
        if (taskId == null || StringUtils.isEmpty(checkType)||(StringUtils.isEmpty(trayCode)&& StringUtils.isEmpty(batch)
        &&StringUtils.isEmpty(rfid))){
            return AjaxResult.error("参数不可为空");
        }

        Long trayId = null;
        List<Long> stockId = null;
        //根据载具查询
        if(StringUtils.isNotEmpty(trayCode)){
            // 通过托盘查询任务详情、要盘点的物料信息
            TTray tray = trayService.selectTTrayByCode(trayCode);
            if (tray == null){
                return AjaxResult.error("载具信息未入库");
            }else{
                trayId=tray.getId();
            }
        }
        if(StringUtils.isNotEmpty(batch)){
            //根据批次号来查
            List<TStock> stockList = stockService.list(new QueryWrapper<TStock>()
                    .eq(trayId != null,"tray_id", trayId).eq("batch_code", batch).eq("del_flag", Constants.DEL_FLAG_NO));
            stockId = stockList.stream().map(TStock::getId).collect(Collectors.toList());
        }


        QueryWrapper<TTaskDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq("task_id", taskId);
        queryWrapper.in(stockId != null && stockId.size() > 0,"stock_id",stockId);
//        queryWrapper.and(i -> i.eq("status", WmsContstants.TASK_DETAIL_STATUS_ING).or().eq("status",WmsContstants.TASK_DETAIL_STATUS_NO).or().eq("status",WmsContstants.TASK_DETAIL_STATUS_REJECT));
        if(trayId!=null&&!trayId.equals(Long.parseLong("0"))){
            queryWrapper.eq("tray_id", trayId);
        }
//        if (Constants.CHECK_DELIVERY_MATERIAL.equals(checkType)){
//            queryWrapper.in("delivery_type",Constants.PICK_TASK_TYPE_CHECKDELIVERY_MATERIAL);
//        }
//        if (Constants.CHECK_DELIVERY_LOCATION.equals(checkType)){
//            queryWrapper.and(i -> i.eq("delivery_type",Constants.PICK_TASK_TYPE_CHECKDELIVERY_LOCATION)
//                    .or()
//                    .eq("delivery_type",Constants.PICK_TASK_TYPE_CHECKDELIVERY_AREA));
//        }

        // 返回结果
        List<Map<String, Object>> result = new ArrayList<>();

        List<TTaskDetail> detailList = taskDetailService.list(queryWrapper);
        if (detailList.size() == 0){
            return AjaxResult.error("该托盘不在盘点任务范围内或已经盘点，请重新选择托盘");
        }
        // 查询任务编号
        TTask task = taskService.getById(taskId);

        // 以物料、随机、动碰、空库位 为维度
        if (Constants.CHECK_DELIVERY_MATERIAL.equals(checkType) ||
                Constants.CHECK_DELIVERY_RNADOM.equals(checkType) ||
                Constants.CHECK_DELIVERY_HISTORY.equals(checkType) ||
                Constants.CHECK_DELIVERY_EMPTY.equals(checkType)){
            // 查询物料编码
            List<Long> materialIdList = detailList.stream().map(e -> e.getMaterialId()).collect(Collectors.toList());
            Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materialIdList);
            for (TTaskDetail e:detailList) {
                TMaterial material = materialMap.get(e.getMaterialId());
                Map<String, Object> map = new HashMap<>();
                map.put("taskCode",task.getCode());
                map.put("taskStatus",e.getStatus());
                map.put("trayId",trayId);
                map.put("materialId",e.getMaterialId());
                map.put("materialCode",material.getCode());
                map.put("materialName",material.getName());
                map.put("predictCount",e.getPredictCount());
                map.put("batchNumber",e.getBatchNumber());
                map.put("stockId",e.getStockId());
                map.put("taskDetailId",e.getId());
                result.add(map);
            }
        }

        // 以库区（区域）为维度
        if (Constants.CHECK_DELIVERY_LOCATION.equals(checkType)){

            QueryWrapper<TStock> stockQueryWrapper=new QueryWrapper<>();
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(batch)){
                stockQueryWrapper.eq("batch_code",batch);
            }
            if(trayId!=null&&!trayId.equals(Long.parseLong("0"))){
                stockQueryWrapper.eq("tray_id",trayId);
            }
            //获取任务下所有的库位
            List<Long> locationIds=detailList.stream().map(TTaskDetail::getLocationId).collect(Collectors.toList());
            if(locationIds!=null&&locationIds.size()>0){
                stockQueryWrapper.in("location_id",locationIds);
            }
            stockQueryWrapper.ne("count",0);
            // 查询托盘下库存数据
            List<TStock> stockList = stockService.list(stockQueryWrapper);
            // 查询物料编码
            List<Long> materialIdList = stockList.stream().map(e -> e.getMaterialId()).collect(Collectors.toList());
            Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materialIdList);
            stockList.forEach(e -> {
                Map<String, Object> map = new HashMap<>();
                TMaterial material = materialMap.get(e.getMaterialId());
                if(material!=null){
                    detailList.forEach(detail -> {
                        if (e.getId().equals(detail.getStockId())){
                            map.put("taskCode",task.getCode());
                            map.put("taskStatus",detail.getStatus());
                            map.put("materialId",e.getMaterialId());
                            map.put("materialCode",material.getCode());
                            map.put("materialName",material.getName());
                            map.put("trayId",e.getTrayId());
                            map.put("predictCount",e.getCount());
                            map.put("batchNumber",e.getBatchCode());
                            map.put("stockId",e.getId());
                            map.put("taskDetailId",detail.getId());
                            result.add(map);
                        }
                    });
                }
            });
        }

        List<Map<String, Object>> mapList = new ArrayList<>();
        // 查询RFID
        result.forEach(e -> {
            List<Map<String, Object>> list = tMaterialDetailMapper.selectMaps(
                    new QueryWrapper<TMaterialDetail>()
                            .select(" count(1) as num ,rfid_head as rfidHead")
                            .eq("tray_id", e.get("trayId"))
                            .eq("batch_code", e.get("batchNumber"))
                            // 已入库
                            .eq("status","1")
                            .eq("del_flag", Constants.DEL_FLAG_NO)
                            .groupBy("rfid_head")
            );

            list.forEach(i -> {
                Map<String, Object> map = new HashMap<>(e);
                map.put("rfidHead",i.get("rfidHead"));
                map.put("rfidHeadCount",i.get("num"));
                mapList.add(map);
            });
        });

        return AjaxResult.success(mapList);
    }


}
