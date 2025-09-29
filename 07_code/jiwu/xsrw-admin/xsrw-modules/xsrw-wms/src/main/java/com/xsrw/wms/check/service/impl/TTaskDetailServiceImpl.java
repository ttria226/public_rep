package com.xsrw.wms.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.*;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.check.domain.*;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.TaskDetailVO;
import com.xsrw.wms.check.mapper.TCheckDeliveryMapper;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.check.service.*;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockService;
import com.xsrw.wms.webservice.util.WmsToErpUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存盘点Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TTaskDetailServiceImpl extends ServiceImpl<TTaskDetailMapper, TTaskDetail> implements ITTaskDetailService
{
    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;

    @Autowired
    private TReservoirMapper tReservoirMapper;

    @Autowired
    private TAreaMapper tAreaMapper;

    @Autowired
    private TLocationMapper tLocationMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    @Autowired
    private TTaskMapper tTaskMapper;

    @Autowired
    private TStockMainMapper tStockMainMapper;

    @Autowired
    private ITStockService itStockService;

    @Autowired
    private ITCheckHistoryService itCheckHistoryService;

    @Autowired
    private ITStockDetailService itStockDetailService;

    @Autowired
    private ITCheckResultService itCheckResultService;

    @Autowired
    private ITCheckAreaHistoryService itCheckAreaHistoryService;

    @Autowired
    private ITTrayService itTrayService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private ITTaskService taskService;


    @Autowired
    private ITStockService stockService;

    @Autowired
    private ITMaterialService materialService;

    @Autowired
    private TTaskDetailMapper taskDetailMapper;

    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;

    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ITCodeConfigService codeConfigService;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private TCheckDeliveryMapper tCheckDeliveryMapper;
    @Autowired
    private WmsToErpUtils wmsToErpUtils;

    /**
     * 查询库存盘点列表
     *
     * @param taskDetailDTO 库存盘点
     * @return 库存盘点
     */
    @Override
    public List<TaskDetailVO> selectTTaskDetailList(TaskDetailDTO taskDetailDTO)
    {
        List<TaskDetailVO> list = new ArrayList<>();
        //feign调用查询 物料名称、物料编号
        if (StringUtils.isNotEmpty(taskDetailDTO.getMaterialCode()) || StringUtils.isNotEmpty(taskDetailDTO.getMaterialName())) {
            List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                    .eq(StringUtils.isNotNull(taskDetailDTO.getMaterialCode()),TMaterial::getCode,taskDetailDTO.getMaterialCode())
                    .like(StringUtils.isNotNull(taskDetailDTO.getMaterialName()),TMaterial::getName,taskDetailDTO.getMaterialName())
                    .eq(TMaterial::getDelFlag,Constants.NO));
            if (CollectionUtils.isEmpty(tMaterials)) {
                return list;
            } else {
                List<Long> materialIds = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
                taskDetailDTO.setMaterialIds(materialIds);
            }
        }

        // 查询区域下所有库位、库区下所有库位
        if (taskDetailDTO.getAreaId() != null || taskDetailDTO.getReservoirId() != null){
            List<TLocation> paramWareHouse = tLocationMapper.selectList(Wrappers.lambdaQuery(TLocation.class)
                    .eq(TLocation::getAreaId,taskDetailDTO.getAreaId())
                    .eq(TLocation::getReservoirId,taskDetailDTO.getReservoirId())
                    .eq(TLocation::getDelFlag,Constants.NO));
            if (CollectionUtils.isEmpty(paramWareHouse)) {
                return list;
            } else {
                List<Long> collect = paramWareHouse.stream().map(TLocation::getId).collect(Collectors.toList());
                taskDetailDTO.setLocationList(collect);
            }
        }

        if (Constants.TASK_DETAIL_STATUS_NO.equals(taskDetailDTO.getStatus())){
            taskDetailDTO.setStatus("");
            taskDetailDTO.setStatusStrs("0,6");
        }

        if (Constants.TASK_TYPE_CHECK.equals(taskDetailDTO.getTaskType())){
            list = tTaskDetailMapper.selectTaskDetailListCheck2(taskDetailDTO);
        }else {
            list = tTaskDetailMapper.selectTaskDetailList(taskDetailDTO);
        }
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(e -> {
                // 计算盘差
                if (e.getRfidHead() != null){
                    e.setCheckDifferenceCount(e.getActualCount().subtract(e.getPredictCount()));
                }

                TMaterial material = tMaterialMapper.selectById(e.getMaterialId());
                if (material != null) {
                    // 物料编码
                    e.setMaterialCode(material.getCode());
                    // 物料名称
                    e.setMaterialName(material.getName());
                    // 规格型号
                    e.setSpecifications(material.getSpecifications());
                    // 单位名称
                    TUnit tUnit = tUnitMapper.selectById(material.getUnitId());
                    e.setUnitName(tUnit.getName());
                }
                TLocation location = tLocationMapper.selectById(e.getLocationId());
                if (location != null) {
                    // 库位编码
                    e.setLocationCode(location.getCode());
                    // 库位名称
                    e.setLocationName(location.getName());
                    // 区域名称
                    TArea tArea = tAreaMapper.selectById(location.getAreaId());
                    if (StringUtils.isNotNull(tArea)){
                        e.setAreaName(tArea.getName());
                    }
                    // 库区名称
                    TReservoir tReservoir = tReservoirMapper.selectById(location.getReservoirId());
                    if (StringUtils.isNotNull(tReservoir)){
                        e.setReservoirName(tReservoir.getName());
                    }

                }

                // 查询WCS任务状态
                TTaskWcs taskWcs = tTaskWcsMapper.selectOne(
                        new QueryWrapper<TTaskWcs>().eq("task_type",Constants.TASK_TYPE_OUT)
                                .eq("id", e.getWcsId()));
                if (taskWcs == null){
                    // 未执行
                    e.setWcsTaskStatus("1");
                }else {
                    e.setWcsTaskStatus(taskWcs.getTaskStatus());
                    e.setWcsId(taskWcs.getId());
                }
            });
        }
        return list;
    }

    /**
     * 查询库存盘点
     *
     * @param id 库存盘点主键
     * @return 库存盘点
     */
    @Override
    public AjaxResult selectTTaskDetailById(Long id)
    {
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setId(id);

        List<TaskDetailVO> list  = tTaskDetailMapper.selectTaskDetailList(taskDetailDTO);

        TaskDetailVO taskDetailVO = new TaskDetailVO();

        if (!ObjectUtils.isEmpty(list) && list.size() > 0){
            taskDetailVO = list.get(0);

            //查询物料
            TMaterial material = tMaterialMapper.selectById(taskDetailVO.getMaterialId());
            if (material != null) {
                // 物料编码
                taskDetailVO.setMaterialCode(material.getCode());
                // 物料名称
                taskDetailVO.setMaterialName(material.getName());
            }
        }
        return AjaxResult.success(taskDetailVO);
    }

    /**
     * 新增库存盘点
     *
     * @param tTaskDetail 库存盘点
     * @return 结果
     */
    @Override
    public int insertTTaskDetail(TTaskDetail tTaskDetail)
    {
        return tTaskDetailMapper.insert(tTaskDetail);
    }

    /**
     * 修改库存盘点
     *
     * @param tTaskDetail 库存盘点
     * @return 结果
     */
    @Override
    public int updateTTaskDetail(TTaskDetail tTaskDetail)
    {
        return tTaskDetailMapper.updateById(tTaskDetail);
    }


    /**
     * 批量删除库存盘点
     *
     * @param ids 需要删除的库存盘点主键
     * @return 结果
     */
    @Override
    public int deleteTTaskDetailByIds(Long[] ids)
    {
        return tTaskDetailMapper.deleteTTaskDetailByIds(ids);
    }

    /**
     * 删除库存盘点信息
     *
     * @param id 库存盘点主键
     * @return 结果
     */
    @Override
    public int deleteTTaskDetailById(Long id)
    {
        return tTaskDetailMapper.deleteTTaskDetailById(id);
    }

    @Override
    public AjaxResult batchAdd(List<TTaskDetail> list) {
        // 由于前端设计问题，该接口可能同时包含新增和修改的数据，所以需要分开处理
        try {
            if (list != null && list.size() > 0) {
                Long taskId = list.get(0).getTaskId();
                if (taskId != 0) {
                    TTask task = new TTask();
                    task.setId(taskId);
                    task.setTaskCount((long) list.size());
                    tTaskMapper.updateById(task);
                }
                list.forEach(data -> {
                    if (data.getId() != null) {
                        this.updateById(data);
                    } else {
                        this.save(data);
                    }
                });
            }

        } catch (Exception e) {
            return AjaxResult.success("添加失败");
        }
        return AjaxResult.success("添加成功");
    }

    /**
     * 执行盘点
     *
     * @param id
     * @param checkNum
     * @return
     */
    @Override
    public AjaxResult performCheck(Long id, BigDecimal checkNum) {
        //判断是否已执行过
        TTaskDetail taskDetail = tTaskDetailMapper.selectById(id);
        if (taskDetail == null) {
            return AjaxResult.error("未查询到对应数据");
        }
        if (Constants.TASK_DETAIL_STATUS_APPROVE_ING.equals(taskDetail.getStatus())
                || Constants.TASK_DETAIL_STATUS_APPROVED.equals(taskDetail.getStatus())) {
            return AjaxResult.error("已盘点不可再次盘点");
        }
        //更新子表数据
        TTaskDetail updateDO = new TTaskDetail();
        updateDO.setId(id);
        //已盘点
        updateDO.setStatus(Constants.TASK_DETAIL_STATUS_END);
        updateDO.setActualCount(checkNum);
        this.updateById(updateDO);
        //查询是否已全部执行，是的话更新该任务状态为已完成，否执行中
        TTask task = new TTask();
        task.setId(taskDetail.getTaskId());
        Long unFinishedCount = tTaskDetailMapper.selectCount(new QueryWrapper<TTaskDetail>()
                .eq("task_id",taskDetail.getTaskId())
                .notIn("status", Constants.TASK_DETAIL_STATUS_END, Constants.TASK_DETAIL_STATUS_APPROVE_ING, Constants.TASK_DETAIL_STATUS_APPROVED));

        if (unFinishedCount > 0) {
            // 执行中
            task.setTaskStatus(Constants.TASK_STATUS_ING);
        } else {
            // 已完成
            task.setTaskStatus(Constants.TASK_STATUS_END);
        }
        return AjaxResult.success(tTaskMapper.updateById(task));
    }

    /**
     * 审核盘点子表
     *
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public AjaxResult approveCheck(Long[] ids, String status) {
        //判断数据是否都已完成
        List<TTaskDetail> tasks = tTaskDetailMapper.selectList(Wrappers.lambdaQuery(TTaskDetail.class)
                .in(TTaskDetail::getId,ids)
                .eq(TTaskDetail::getDelFlag,Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTaskDetail> noUnFinishList = tasks.stream().filter(e -> Constants.TASK_DETAIL_STATUS_NO.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(noUnFinishList)) {
            return AjaxResult.error("任务未完成，不可审核");
        }
        List<TTaskDetail> approvedUnFinishList = tasks.stream().filter(e -> Constants.TASK_DETAIL_STATUS_APPROVED.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(approvedUnFinishList)) {
            return AjaxResult.error("任务已审核");
        }
        List<TTaskDetail> rejectList = tasks.stream().filter(e -> Constants.TASK_DETAIL_STATUS_REJECT.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rejectList)) {
            return AjaxResult.error("任务驳回后需重新执行");
        }
        Integer checkCount = null;
        if (Constants.TASK_DETAIL_STATUS_APPROVED.equals(status)) {
            // 审核通过  更新库存

            tasks.forEach(e -> {

                // 查询盘点提交记录
                List<TCheckHistory> historyList = itCheckHistoryService.list(
                        new QueryWrapper<TCheckHistory>().eq("task_detail_id", e.getId()));

                // 更新的库存数据
                List<TStock> stocks = new ArrayList<>();
                // 库存变更记录
                List<TStockDetail> stockDetails = new ArrayList<>();

                Map<Long, List<TCheckHistory>> listMap = historyList.stream().collect(Collectors.groupingBy(TCheckHistory::getTaskDetailId));
                listMap.forEach((k,v) -> {

                    // 实际数量
                    BigDecimal sum = v.stream().map(f -> f.getActualCount()).reduce(BigDecimal.ZERO,BigDecimal::add);

                    TCheckHistory history = v.get(0);
                    // 查询原库存数据
                    TStock stock = itStockService.getById(history.getStockId());
                    // 盘库差值
                    BigDecimal differCount = stock.getCount().subtract(sum);

                    TStock stockNew = new TStock();
                    stockNew.setId(stock.getId());
                    stockNew.setCount(v.stream().map(TCheckHistory::getActualCount).reduce(BigDecimal.ZERO,BigDecimal::add));
                    //解冻
                    stockNew.setIsFreeze(Constants.STOCK_IS_FREEZE_NO);
                    stockNew.setOriginType("");
                    stockNew.setAvailableCount(stock.getAvailableCount().subtract(differCount));
                    stocks.add(stockNew);

                    //库存详情表，目前只是做个记录
                    TStockDetail stockDetail = new TStockDetail();
                    stockDetail.setMaterialId(stock.getMaterialId());
                    stockDetail.setLocationId(stock.getLocationId());
                    stockDetail.setBeforeCount(stock.getCount());
                    stockDetail.setCurrentCount(v.stream().map(TCheckHistory::getActualCount).reduce(BigDecimal.ZERO,BigDecimal::add));
                    stockDetail.setType(Constants.TASK_TYPE_CHECK);
                    stockDetail.setBatchCode(stock.getBatchCode());
                    stockDetails.add(stockDetail);

                    // 更新库存主表
                    TStockMain stockMainOrg = tStockMainMapper.selectOne(Wrappers.lambdaQuery(TStockMain.class)
                            .eq(TStockMain::getMaterialId,stock.getMaterialId())
                            .eq(TStockMain::getDeptId,stock.getDeptId())
                            .eq(TStockMain::getDelFlag,Constants.NO));
                    TStockMain stockMain = new TStockMain();
                    stockMain.setMaterialId(stock.getMaterialId());
                    stockMain.setDeptId(stock.getDeptId());
                    stockMain.setLibraryCount(stockMainOrg.getLibraryCount().subtract(differCount));
                    stockMain.setAvailableCount(stockMainOrg.getAvailableCount().subtract(differCount));
                    tStockMainMapper.updateCountByParam(stockMain);

                    // 处理RFID
                    v.forEach(f -> {
                        BigDecimal count = f.getRfidHeadCount();
                        BigDecimal actualCount = f.getActualCount();

                        // 查询rfid对应物料详情
                        TMaterialDetail detail = tMaterialDetailMapper.selectOne(
                                new QueryWrapper<TMaterialDetail>()
                                        // 已入库
                                        .eq("status", "1")
                                        .eq("rfid_head", f.getRfidHead())
                                        .eq("material_id",f.getMaterialId())
                                        .eq("del_flag", Constants.DEL_FLAG_NO)
                        );

                        // 更新rifd物料详情
                        detail.setRfidCount(actualCount);
                        tMaterialDetailMapper.updateById(detail);

                        TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                        tMaterialDetailVO.setBatchCode(detail.getBatchCode());
                        tMaterialDetailVO.setMaterialId(detail.getMaterialId());
                        tMaterialDetailVO.setMaterialName(detail.getMaterialName());
                        tMaterialDetailVO.setRfid(f.getRfidHead());
                        tMaterialDetailVO.setCount(actualCount);
                        tMaterialDetailVO.setRfids(new ArrayList<>());
                        redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);
                    });
                });

                if (stocks.size() > 0){
                    itStockService.updateBatchById(stocks);
                }
                if (stockDetails.size() > 0){
                    itStockDetailService.saveBatch(stockDetails);
                }

            });

        }else if (Constants.TASK_DETAIL_STATUS_REJECT.equals(status)) {
            // 驳回的话复盘数量加一
            checkCount = 1;
            // 更新主任务为部分完成
            tTaskMapper.update(new TTask(),
                    new UpdateWrapper<TTask>().set("task_status",Constants.TASK_STATUS_ING).eq("id",tasks.get(0).getTaskId()));
        }

        // 更新任务状态
        Integer flage = tTaskDetailMapper.updateStatusByIds(ids, status, checkCount);
        if (flage > 0) {
            if (Constants.TASK_DETAIL_STATUS_APPROVED.equals(status)) {
                //如果审核通过，查询是否已全部执行，是的话更新该任务状态为已审核
                Long taskId = tasks.get(0).getTaskId();
                int unFinishedCount = tTaskDetailMapper.selectUnFinishedCount(taskId, Constants.TASK_DETAIL_STATUS_APPROVED, null);
                if (unFinishedCount == 0) {
                    TTask task = new TTask();
                    task.setId(taskId);
                    //已审核
                    task.setTaskStatus(Constants.TASK_STATUS_APPROVED);
                    tTaskMapper.updateById(task);
                    //审核完生成盘点差异报表
                    itCheckResultService.saveCheckResultByTaskId(taskId);
                }
            }
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 审核区域盘点任务
     * @param taskDetailId
     * @param ids
     * @param status
     * @return
     */
    @Transactional
    @Override
    public AjaxResult approveAreaCheck(Long taskDetailId, List<Long> ids, String status) {

        TTaskDetail taskDetail = tTaskDetailMapper.selectById(taskDetailId);
        if (taskDetail == null){
            return AjaxResult.error("未查询到任务信息");
        }
        if (!Constants.TASK_DETAIL_STATUS_APPROVE_ING.equals(taskDetail.getStatus())){
            return AjaxResult.error("任务状态非审核中，不可审核");
        }

        // 查询提交记录
        List<TCheckAreaHistory> areaHistoryList = itCheckAreaHistoryService.list(new QueryWrapper<TCheckAreaHistory>()
                .in("id", ids)
                .eq("status", Constants.CHECK_AREA_NO)
                .eq("is_draft", Constants.YES)
                .eq("task_detail_id", taskDetailId));

        if (areaHistoryList.size() != ids.size()){
            return AjaxResult.error("任务状态为未确认时才可确认");
        }

        if (Constants.CHECK_AREA_CONFIRM.equals(status)){
            // 确认
            itCheckAreaHistoryService.update(
                    new UpdateWrapper<TCheckAreaHistory>().in("id",ids).set("status",Constants.CHECK_AREA_CONFIRM));

            // 查询提交记录
            List<TCheckAreaHistory> historyList = itCheckAreaHistoryService.list(new QueryWrapper<TCheckAreaHistory>()
                    .eq("status", Constants.CHECK_AREA_CONFIRM)
                    .eq("is_draft", Constants.YES)
                    .eq("task_detail_id", taskDetailId));

            // 新增库存数据
            List<TStock> addStock = new ArrayList<>();
            // 更新的库存数据
            List<TStock> stocks = new ArrayList<>();
            // 库存变更记录
            List<TStockDetail> stockDetails = new ArrayList<>();

            historyList.forEach(history -> {
                TTray tray = itTrayService.getOne(new QueryWrapper<TTray>().eq("code", history.getTrayCode()));
                String materialCode = history.getBatchNumber();
                String code = materialCode.substring(0,materialCode.lastIndexOf("-"));
                TMaterial material = tMaterialMapper.selectOne(Wrappers.lambdaQuery(TMaterial.class)
                        .eq(TMaterial::getCode,code)
                        .eq(TMaterial::getDelFlag,Constants.NO));

                // 查询库存中是否存在该物料数据
                TStock stock = itStockService.getOne(new QueryWrapper<TStock>()
                        .eq("tray_id", tray.getId())
                        .eq("material_id", material.getId())
                        .eq("batch_code", materialCode));
                if (stock != null){
                    // 盘库差值
                    BigDecimal differCount = stock.getCount().subtract(history.getActualCount());
                    stock.setCount(history.getActualCount());
                    stock.setAvailableCount(stock.getAvailableCount().subtract(differCount));
//                    stock.setIsfreeze(Constants.STOCK_IS_FREEZE_NO);//解冻
//                    stock.setOriginType("");
                    stocks.add(stock);

                    //库存详情表，目前只是做个记录
                    TStockDetail stockDetail = new TStockDetail();
                    stockDetail.setMaterialId(stock.getMaterialId());
                    stockDetail.setBeforeCount(stock.getCount());
                    stockDetail.setCurrentCount(stock.getAvailableCount().subtract(differCount));
                    stockDetail.setType(Constants.TASK_TYPE_CHECK);
                    stockDetails.add(stockDetail);

                    // 更新库存主表
                    TStockMain stockMainOrg = tStockMainMapper.selectOne(Wrappers.lambdaQuery(TStockMain.class)
                            .eq(TStockMain::getMaterialId,stock.getMaterialId())
                            .eq(TStockMain::getDeptId,stock.getDeptId())
                            .eq(TStockMain::getDelFlag,Constants.NO));
                    TStockMain stockMain = new TStockMain();
                    stockMain.setMaterialId(stock.getMaterialId());
                    stockMain.setDeptId(stock.getDeptId());
                    stockMain.setLibraryCount(stockMainOrg.getLibraryCount().subtract(differCount));
                    stockMain.setAvailableCount(stockMainOrg.getAvailableCount().subtract(differCount));
                    tStockMainMapper.updateCountByParam(stockMain);

                }else {
                    // 查询库存中其它区域是否存在该物料数据  存在该数据则更新库存的区域
                    TStock stockArea = itStockService.getOne(new QueryWrapper<TStock>()
                            .eq("tray_id", tray.getId())
                            .eq("material_id", material.getId())
                            .eq("batch_code", materialCode));
                    if (stockArea != null){
                        stockArea.setPosition("");
                        itStockService.updateById(stockArea);
                    }else{
                        TStock newStock = new TStock();
                        newStock.setDeptId(taskDetail.getDeptId());
                        newStock.setMaterialId(material.getId());
                        newStock.setBatchCode(materialCode);
                        newStock.setTrayId(tray.getId());
                        newStock.setCount(history.getActualCount());
                        newStock.setAvailableCount(history.getActualCount());
                        newStock.setIsFreeze(Constants.NO);
                        newStock.setStatus(Constants.NO);
                        newStock.setBeginDate(new Date());
//                        newStock.setProducedDate(getProduced(history.getBatchNumber(),material.getId()));
                        addStock.add(newStock);

                        //库存详情表，目前只是做个记录
                        TStockDetail stockDetail = new TStockDetail();
                        stockDetail.setMaterialId(newStock.getMaterialId());
                        stockDetail.setBeforeCount(BigDecimal.ZERO);
                        stockDetail.setCurrentCount(newStock.getCount());
                        stockDetail.setType(Constants.TASK_TYPE_CHECK);
                        stockDetail.setBatchCode(newStock.getBatchCode());
                        stockDetails.add(stockDetail);

                        // 更新库存主表
                        TStockMain stockMainOrg = tStockMainMapper.selectOne(Wrappers.lambdaQuery(TStockMain.class)
                                .eq(TStockMain::getMaterialId,material.getId())
                                .eq(TStockMain::getDeptId,taskDetail.getDeptId())
                                .eq(TStockMain::getDelFlag,Constants.NO));
                        if (stockMainOrg == null){
                            TStockMain stockMain = new TStockMain();
                            stockMain.setMaterialId(material.getId());
                            stockMain.setDeptId(taskDetail.getDeptId());
                            stockMain.setLibraryCount(history.getActualCount());
                            stockMain.setAvailableCount(history.getActualCount());
                            tStockMainMapper.insert(stockMain);
                        }else {
                            TStockMain stockMain = new TStockMain();
                            stockMain.setMaterialId(material.getId());
                            stockMain.setDeptId(taskDetail.getDeptId());
                            stockMain.setLibraryCount(stockMainOrg.getLibraryCount().add(history.getActualCount()));
                            stockMain.setAvailableCount(stockMainOrg.getAvailableCount().add(history.getActualCount()));
                            tStockMainMapper.updateCountByParam(stockMain);
                        }

                        TTray trayUpdate = new TTray();
                        trayUpdate.setId(tray.getId());
                        trayUpdate.setStatus(Constants.TRAY_STATUS_HALF);
                        itTrayService.updateById(trayUpdate);
                    }
                }
            });

            if (addStock.size() > 0){
                itStockService.saveBatch(addStock);
            }
            if (stocks.size() > 0){
                itStockService.updateBatchById(stocks);
            }
            if (stockDetails.size() > 0){
                itStockDetailService.saveBatch(stockDetails);
            }

            List<TCheckAreaHistory> allList = itCheckAreaHistoryService.list(new QueryWrapper<TCheckAreaHistory>()
                    .eq("is_draft", Constants.YES)
                    .eq("task_detail_id", taskDetailId));

            if (historyList.size() == allList.size()){
                taskDetail.setStatus(Constants.TASK_DETAIL_STATUS_APPROVED);
                tTaskDetailMapper.updateById(taskDetail);

                TTask task = tTaskMapper.selectById(taskDetail.getTaskId());
                task.setTaskStatus(Constants.TASK_STATUS_APPROVED);
                tTaskMapper.updateById(task);

                // 写入盘点报表数据
                itCheckResultService.saveCheckResultByTaskId(taskDetail.getTaskId());

                // 将库存数据解冻
                List<TStock> stockList = itStockService.list(
                        new QueryWrapper<TStock>()
                                .eq("is_freeze", Constants.YES));

                //解冻库存
                List<Long> stockIds = stockList.stream().map(TStock::getId).collect(Collectors.toList());
                itStockService.updateFreezeByIds(stockIds, Constants.STOCK_IS_FREEZE_NO, Constants.STOCK_ORIGIN_TYPE_CHECK);
            }

        }else if (Constants.CHECK_AREA_REJECT.equals(status)){
            // 驳回
            if (taskDetail.getCheckCount() != null){
                taskDetail.setCheckCount((long) taskDetail.getCheckCount().intValue()+1);
            }else {
                taskDetail.setCheckCount(1L);
            }
            tTaskDetailMapper.updateById(taskDetail);
            tTaskMapper.update(new TTask(),new UpdateWrapper<TTask>()
                    .eq("id",taskDetail.getTaskId()).set("task_status",Constants.TASK_STATUS_ING));

            itCheckAreaHistoryService.update(
                    new UpdateWrapper<TCheckAreaHistory>().in("id",ids).set("status",Constants.CHECK_AREA_REJECT));
        }else {
            return AjaxResult.error("审核状态错误");
        }
        return AjaxResult.success();
    }
    @Override
    public  AjaxResult getDropdownData(Long taskId){
        Map<String,Object> map=new HashMap<>();
        List<TTaskDetail> detailList = tTaskDetailMapper.selectList(new LambdaQueryWrapper<TTaskDetail>()
                .eq(TTaskDetail::getTaskId, taskId)
                .eq(TTaskDetail::getDelFlag, Constants.DEL_FLAG_NO)
        );
        List<Long> stockId = detailList.stream().map(TTaskDetail::getStockId).collect(Collectors.toList());
        List<TStock> stockList = stockService.list(
                new QueryWrapper<TStock>().in("id", stockId).eq("del_flag", Constants.DEL_FLAG_NO));

        //物料批次号
        List<Map<String,Object>> mapList = new ArrayList<>();
        stockList.forEach(e -> {
            Map<String,Object> data = new HashMap<>();
            data.put("id",e.getMaterialId());
            data.put("batchNumber",e.getBatchCode());

            mapList.add(data);
        });

        HashSet<Map<String,Object>> trayList = new HashSet<>(itTrayService.getTrayListByTaskId(taskId));
        map.put("batchList",new HashSet<>(mapList));
        map.put("trayList",trayList);
        return AjaxResult.success(map);
    }











    /**
     * 待盘点物料列表
     * @param taskId
     * @param trayCode
     * @param checkType
     * @return
     */
    @Override
    public AjaxResult trayDetail(Long taskId, String trayCode, String checkType) {
        if (taskId == null ||  StringUtils.isEmpty(checkType)||(StringUtils.isEmpty(trayCode))){
            return AjaxResult.error("参数不可为空");
        }
        Long trayId=new Long("0");
        //根据载具查询
        if(StringUtils.isNotEmpty(trayCode)){
            // 通过托盘查询任务详情、要盘点的物料信息
            TTray tray = trayService.selectTTrayByCode(trayCode);
            if (tray == null){
                return AjaxResult.error("载具信息未入库");
            }else{
                trayId=tray.getId();
            }
        }else{
            return AjaxResult.error("参数不可为空");
        }


        QueryWrapper<TTaskDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq("task_id", taskId);
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

        List<TTaskDetail> detailList = this.list(queryWrapper);
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
                Map<String, Object> map = new HashMap<>();
                TMaterial material = materialMap.get(e.getMaterialId());
                map.put("taskCode",task.getCode());
                map.put("taskStatus",e.getStatus());
                map.put("trayId",trayId);
                map.put("materialId",e.getMaterialId());
                map.put("materialCode",material.getCode());
                map.put("materialName",material.getName());
                map.put("trayId",trayId);
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
                    map.put("taskCode",task.getCode());
                    detailList.forEach(detail ->{
                        if (detail.getBatchNumber().equals(e.getBatchCode()) && detail.getTrayId().equals(e.getTrayId())){
                            map.put("taskStatus",detail.getStatus());
                            map.put("taskDetailId",detail.getId());
                        }
                    });

                    map.put("materialId",e.getMaterialId());
                    map.put("materialCode",material.getCode());
                    map.put("materialName",material.getName());
                    map.put("trayId",e.getTrayId());
                    map.put("predictCount",e.getCount());
                    map.put("batchNumber",e.getBatchCode());
                    map.put("stockId",e.getId());
                    result.add(map);
                }
            });
        }

        List<Map<String, Object>> mapList = new ArrayList<>();
        // 查询RFID
        result.forEach(e -> {
            List<Map<String, Object>> list = tMaterialDetailMapper.selectMaps(
                    new QueryWrapper<TMaterialDetail>()
                            .select("rfid_count as num ,rfid_head as rfidHead")
                            .eq("tray_id", e.get("trayId"))
                            .eq("batch_code", e.get("batchNumber"))
                            // 已入库
                            .eq("status","1")
                            .eq("del_flag", Constants.DEL_FLAG_NO)
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


    /**
     * 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    @Override
    public AjaxResult checkSubmit(CheckDeliveryDTO checkDeliveryDTO) {
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
        TTaskDetail byId = this.getById(detailIdList.get(0));

//        List<TTaskDetail> detailList = taskDetailService.list(new QueryWrapper<TTaskDetail>()
//                .and(i -> i.eq("status", Constants.TASK_DETAIL_STATUS_NO).or().eq("status",Constants.TASK_DETAIL_STATUS_REJECT))
//                .in("id", detailIdList));
//        if (detailList.size() > 0){
//            return AjaxResult.error("请先执行任务！");
//        }


        // 以物料为维度盘点时  校验盘点的物料
        if (Constants.CHECK_DELIVERY_MATERIAL.equals(checkType) || Constants.CHECK_DELIVERY_HISTORY.equals(checkType)
                || Constants.CHECK_DELIVERY_RNADOM.equals(checkType) || Constants.CHECK_DELIVERY_EMPTY.equals(checkType)) {
            for (int i = 0; i < data.size(); i++) {
                TCheckHistory history = data.get(i);
                // 查询任务详情
                TTaskDetail taskDetail = this.getById(history.getId());
                if (!taskDetail.getMaterialId().equals(history.getMaterialId())) {
                    return AjaxResult.error("该物料不在盘点任务范围内，请核实");
                }
            }
        }

        // 以库区为维度盘点时  会存在物料盘盈 库存中无数据的情况 所以校验是否盘点了托盘下的所有物料
        if (Constants.CHECK_DELIVERY_LOCATION.equals(checkType)) {

            Set detailId = new HashSet();
            List<Long> collect = data.stream().map(e -> e.getTaskDetailId()).collect(Collectors.toList());
            detailId.addAll(collect);

            if (detailId.size() > 1){
                return AjaxResult.error("盘点数据错误");
            }
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

            // 查询该托盘下需要盘点的物料
            Long taskDetailId = data.get(0).getTaskDetailId();
            TTaskDetail taskDetail = this.getById(taskDetailId);

            List<TStock> stockList = stockService.list(new QueryWrapper<TStock>()
                    .eq("tray_id", taskDetail.getTrayId()));

            List<Long> stockMaterial = stockList.stream().map(e -> e.getMaterialId()).distinct().collect(Collectors.toList());
            List<Long> dataMaterial = data.stream().filter(e -> e.getMaterialId() != null).map(TCheckHistory::getMaterialId).distinct().collect(Collectors.toList());
            if (stockMaterial.size() != dataMaterial.size()){
                return AjaxResult.error("盘点物料与计划不符，请重新扫码");
            }
        }


        // 更新任务状态
        this.update(new UpdateWrapper<TTaskDetail>()
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
            TCheckHistory history = itCheckHistoryService.getOne(new QueryWrapper<TCheckHistory>()
                    .eq("task_detail_id", e.getTaskDetailId())
                    .eq("stock_id", e.getStockId())
                    .eq("tray_id", e.getTrayId())
                    .eq("rfid_head",e.getRfidHead())
                    .eq("material_code", e.getMaterialCode()));
            if (history != null){
                history.setActualCount(e.getActualCount());
                history.setPredictCount(e.getPredictCount());
                itCheckHistoryService.updateById(history);
            }else {
                if (e.getMaterialId() == null){
                    TMaterial material = materialService.getCodeById(e.getMaterialCode());
                    e.setMaterialId(material.getId());
                }
                itCheckHistoryService.save(e);
            }
        });


        return AjaxResult.success();
    }

    public  List<TaskDetailVO> selectCheckTaskResult(TaskDetailDTO taskDetail){
        List<TaskDetailVO> taskDetailVOList = tTaskDetailMapper.selectCheckTaskResult(taskDetail);
        taskDetailVOList.forEach(e -> {
            //判断实盘数据
//            TTask tTask =  taskService.getById(e.getTaskId());
//            if (tTask!=null){
//                if (tTask.getTrayType().equals("1")){  //托盘
//                    //从盘点结果中获取
//                    QueryWrapper<TCheckResult> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
//                    List<TCheckResult> tCheckResultList = itCheckResultService.list(queryWrapper);
//                    if (tCheckResultList.size() > 0) {
//                        TCheckResult tCheckResult = tCheckResultList.get(0);
//                        e.setActualCount(tCheckResult.);
//                    }
//                }
//            }
        });
        return tTaskDetailMapper.selectCheckTaskResult(taskDetail);
    }

    /**
     * 审核盘点子表
     * @param ids
     * @param status
     * @return
     */
    @Transactional
    public AjaxResult checkTaskDetail(Long[] ids, String status){
        //判断数据是否都已完成
        List<TTaskDetail> taskDetailList = tTaskDetailMapper.selectList(Wrappers.lambdaQuery(TTaskDetail.class)
                .in(TTaskDetail::getId,ids)
                .eq(TTaskDetail::getDelFlag,Constants.NO));
        if (CollectionUtils.isEmpty(taskDetailList)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTaskDetail> noUnFinishList = taskDetailList.stream().filter(e -> Constants.TASK_DETAIL_STATUS_NO.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(noUnFinishList)) {
            return AjaxResult.error("任务未完成，不可审核");
        }
        List<TTaskDetail> approvedUnFinishList = taskDetailList.stream().filter(e -> Constants.TASK_DETAIL_STATUS_APPROVED.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(approvedUnFinishList)) {
            return AjaxResult.error("任务已审核");
        }
        List<TTaskDetail> rejectList = taskDetailList.stream().filter(e -> Constants.TASK_DETAIL_STATUS_REJECT.equals(e.getStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(rejectList)) {
            return AjaxResult.error("任务驳回后需重新执行");
        }
        Integer checkCount = null;
        if (Constants.TASK_DETAIL_STATUS_APPROVED.equals(status)) {
            taskDetailList.forEach(e -> {
                //更新库存冻结状态
                List<Long> stockIds = new ArrayList<>();
                stockIds.add(e.getStockId());
                tStockMapper.updateFreezeByIds(stockIds, Constants.STOCK_IS_FREEZE_NO, Constants.STOCK_ORIGIN_TYPE_CHECK);
                }
            );
        }else if (Constants.TASK_DETAIL_STATUS_REJECT.equals(status)) {
            // 驳回的话复盘数量加一
            checkCount = 1;
            // 更新主任务为部分完成
            tTaskMapper.update(new TTask(),
                    new UpdateWrapper<TTask>().set("task_status",Constants.TASK_STATUS_ING).eq("id",taskDetailList.get(0).getTaskId()));
        }
        // 更新任务明细状态
        Integer flage = tTaskDetailMapper.updateStatusByIds(ids, status, checkCount);
        if (flage > 0) {
            if (Constants.TASK_DETAIL_STATUS_APPROVED.equals(status)) {
                //如果审核通过，查询是否已全部执行，是的话更新该任务状态为已审核
                Long taskId = taskDetailList.get(0).getTaskId();
                Long planId = taskDetailList.get(0).getPlanId();


                int unFinishedCount = tTaskDetailMapper.selectUnFinishedCount(taskId, Constants.TASK_DETAIL_STATUS_APPROVED, null);
                if (unFinishedCount == 0) {
                    TTask task = new TTask();
                    task.setId(taskId);
                    //已审核
                    task.setTaskStatus(Constants.TASK_STATUS_APPROVED);
                    tTaskMapper.updateById(task);

                    //如果盘点任务已审核完，更新盘点计划的状态为已完成
                    int unFinishTaskCount = tTaskMapper.selectUnFinishedCount(planId,Constants.TASK_STATUS_APPROVED,task.getId());
                    if (unFinishTaskCount == 0){
                        TCheckDelivery  tCheckDelivery = new TCheckDelivery();
                        tCheckDelivery.setId(planId);
                        tCheckDelivery.setStatus(Constants.TASK_STATUS_END);
//                        tCheckDeliveryMapper.updateById(tCheckDelivery);
                        tCheckDeliveryMapper.updateCheckDeliveryStatus(planId,Constants.TASK_STATUS_END,task.getId());

                        //TODO WMS-ERP对接盘点
                        wmsToErpUtils.checkPut(task.getId());
                    }
                    //审核完生成盘点差异报表
                    //itCheckResultService.saveCheckResultByTaskId(taskId);
                }
            }
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
