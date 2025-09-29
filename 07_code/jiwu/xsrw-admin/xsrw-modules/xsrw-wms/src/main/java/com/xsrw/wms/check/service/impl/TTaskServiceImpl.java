package com.xsrw.wms.check.service.impl;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.C;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.*;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.check.domain.*;
import com.xsrw.wms.check.domain.dto.AddTaskDTO;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.*;
import com.xsrw.wms.check.mapper.TCheckDeliveryMapper;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.check.service.ITCheckHistoryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.TTaskWcsDetail;
import com.xsrw.wms.inout.domain.dto.TTaskWcsRecordDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsRecordVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.inout.service.ITTaskWcsDetailService;
import com.xsrw.wms.inout.service.ITTaskWcsRecordService;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.inout.strategy.RecommendedLocationUtil;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsMoveUtil;
import com.xsrw.wms.web.util.WcsReportUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 盘点任务Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TTaskServiceImpl extends ServiceImpl<TTaskMapper, TTask> implements ITTaskService {
    @Autowired
    private TTaskMapper tTaskMapper;

    @Autowired
    private TCheckDeliveryMapper checkDeliveryMapper;

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
    private TTaskDetailMapper tTaskDetailMapper;

    @Autowired
    private TStockMapper tStockMapper;

    @Autowired
    private TTrayMapper tTrayMapper;

    @Autowired
    private ITCodeConfigService itCodeConfigService;

    @Autowired
    private ITLocationService itLocationService;

    @Autowired
    @Lazy
    private ITTaskDetailService itTaskDetailService;

    @Autowired
    private ITTaskWcsService taskWcsService;

    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITStockDetailService stockDetailService;

    @Autowired
    private ITCheckHistoryService checkHistoryService;

    @Autowired
    private ITMaterialService materialService;
    @Autowired
    private WcsReportUtil wcsReportUtil;
    @Autowired
    private WcsMoveUtil wcsMoveUtil;
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;
    @Autowired
    private AgvReportUtil agvReportUtil;
    @Autowired
    private RecommendedLocationUtil recommendedLocationUtil;
    @Autowired
    private ITTaskWcsRecordService tTaskWcsRecordService;

    /**
     * 查询盘点任务列表
     *
     * @param task 盘点任务
     * @return 盘点任务
     */
    @Override
    public List<TaskVO> selectTTaskList(TTask task) {
        task.setTaskType(Constants.TASK_TYPE_CHECK);
        List<TaskVO> list = tTaskMapper.selectTTaskList(task);
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        // 盘点任务时查询物料、盘点策略、库区、区域
        if (Constants.TASK_TYPE_CHECK.equals(task.getTaskType())) {
            //盘点计划
            List<Long> checkDeliveryId = list.stream().map(TaskVO::getSourceId).collect(Collectors.toList());
            //物料名称
            List<Long> tMaterialIds = null;
            if (StringUtils.isNotNull(task.getMaterialName())) {
                List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                        .like(TMaterial::getName, task.getMaterialName())
                                .like(TMaterial::getCode,task.getMaterialCode())
                        .eq(TMaterial::getDelFlag, Constants.NO));
                tMaterialIds = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
            }
            List<TCheckDelivery> deliveryList = checkDeliveryMapper.selectList(Wrappers.lambdaQuery(TCheckDelivery.class)
                    .in(TCheckDelivery::getId, checkDeliveryId)
                    .in(StringUtils.isNotEmpty(tMaterialIds), TCheckDelivery::getMaterialId, tMaterialIds)
            );
//                    .eq(TCheckDelivery::getDelFlag, Constants.NO));
            if (deliveryList.size() > 0) {
                Map<Long, TCheckDelivery> deliveryMap = deliveryList.stream().collect(Collectors.toMap(TCheckDelivery::getId, Function.identity()));

                list.forEach(e -> {
                    TCheckDelivery delivery = deliveryMap.get(e.getSourceId());
                    if (StringUtils.isNotNull(delivery)) {
                        e.setCheckType(delivery.getCheckType());
                        //物料
                        if (StringUtils.isNotNull(delivery.getMaterialId())) {
                            TMaterial material = tMaterialMapper.selectById(delivery.getMaterialId());
                            if (material != null) {
                                e.setMaterialName(material.getName());
                                e.setMaterialCode(material.getCode());
                            }
                        }

                        //库区
                        if (StringUtils.isNotNull(delivery.getReservoirId())) {
                            TReservoir reservoir = tReservoirMapper.selectById(delivery.getReservoirId());
                            if (reservoir != null) {
                                e.setReservoirName(reservoir.getName());
                            }
                        }

                        //区域
                        if (StringUtils.isNotNull(delivery.getAreaId())) {
                            TArea area = tAreaMapper.selectById(delivery.getAreaId());
                            if (area != null) {
                                e.setAreaName(area.getName());
                            }
                        }

                        if (Constants.TASK_TYPE_CHECK.equals(e.getTaskType())) {
                            TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
                            taskDetailDTO.setTaskId(e.getId());
                            taskDetailDTO.setTaskType(e.getTaskType());
                            List<TaskDetailVO> taskDetailVOList = tTaskDetailMapper.selectTaskDetailListCheck2(taskDetailDTO);
                            if (StringUtils.isNotEmpty(taskDetailVOList) && taskDetailVOList.size() > 0) {
                                TaskDetailVO taskDetailVO = taskDetailVOList.get(0);
                                //计划数量
                                e.setPredictCount(taskDetailVO.getPredictCount());
                                //实际数量
                                e.setActualCount(taskDetailVO.getActualCount());
                                //盘差
                                if (e.getPredictCount() != null && e.getActualCount() != null) {
                                    //计算盘差(盘点数量-原来库存数量)
                                    e.setCheckDifferenceCount(e.getActualCount().subtract(e.getPredictCount()));
                                }
                            }
                        }

                        // 盘点任务状态特殊处理  合并为三种状态  未执行、执行中、执行完成
                        if (Constants.TASK_STATUS_NO.equals(e.getTaskStatus())) {
                            e.setTaskStatusName("未执行");
                        }
                        if (Constants.TASK_STATUS_ING.equals(e.getTaskStatus())) {
                            e.setTaskStatusName("执行中");
                        }
                        if (Constants.TASK_STATUS_END.equals(e.getTaskStatus()) || Constants.TASK_STATUS_APPROVE_ING.equals(e.getTaskStatus())
                                || Constants.TASK_STATUS_APPROVED.equals(e.getTaskStatus())) {
                            e.setTaskStatusName("执行完成");
                        }
                    }
                });
            }

        }
        return list;
    }

    /**
     * 查询盘点任务
     *
     * @param id 盘点任务主键
     * @return 盘点任务
     */
    @Override
    public TaskVO selectTTaskById(Long id) {
        TaskVO taskVO = tTaskMapper.selectTaskById(id);
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setTaskId(id);
        List<TaskDetailVO> taskDetailVOs = tTaskDetailMapper.selectTaskDetailList(taskDetailDTO);
        if (!CollectionUtils.isEmpty(taskDetailVOs)) {
            //todo 盘点任务用不到上架，暂时注释上架任务
//            // 上架任务，关联收货单单号
//            if (Constants.TASK_TYPE_PUT.equals(taskVO.getTaskType())){
//                List<Long> sourceIds = taskDetailVOs.stream().map(TaskDetailVO::getSourceId).collect(Collectors.toList());
//
//                List<TTakeDelivery> takeDeliveryList = takeDeliveryMapper.selectBatchIds(sourceIds);
//
//                taskDetailVOs.forEach(model -> takeDeliveryList.forEach(takeDelivery ->{
//                    if (model.getSourceId() - takeDelivery.getId() == 0){
//                        model.setOriginCode(takeDelivery.getCode());
//                    }
//                }));
//            } else {
//                taskDetailVOs.forEach(model -> {
//                    model.setOriginCode(taskVO.getOriginCode());
//                });
//            }
            taskDetailVOs.forEach(e -> {
                if (StringUtils.isNotNull(e.getMaterialId())) {
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
                }
                if(StringUtils.isNotNull(e.getStockId())){
                    TStock tStock = tStockMapper.selectById(e.getStockId());
                    if(tStock != null){
                        e.setStockNum(tStock.getCount());
                    }

                }

                if (StringUtils.isNotNull(e.getLocationId())) {
                    // 库位名称
                    TLocation locationData = tLocationMapper.selectById(e.getLocationId());
                    if (locationData != null) {
                        // 库位名称
                        e.setLocationName(locationData.getName());
                        // 库位编码
                        e.setLocationCode(locationData.getCode());
                        //区域名称
                        TArea tArea = tAreaMapper.selectById(locationData.getAreaId());
                        if (StringUtils.isNotNull(tArea)) {
                            e.setAreaName(tArea.getName());
                        }
                        //库区名称
                        TReservoir tReservoir = tReservoirMapper.selectById(locationData.getReservoirId());
                        if (StringUtils.isNotNull(tReservoir)) {
                            e.setReservoirName(tReservoir.getName());
                        }
                    }
                }

//                if (finalAreaMap != null){
//                    // 库位名称
//                    Area area = finalAreaMap.get(e.getAreaId());
//                    if (area != null) {
//                        e.setAreaName(area.getName());
//                    }
//                }

            });
        }
        taskVO.setTaskDetailVOList(taskDetailVOs);
        return taskVO;
    }

    @Override
    public AjaxResult check(TTaskCheckVO tTaskCheckVO) {
        TTask task = tTaskMapper.selectById(tTaskCheckVO.getId());
        if (task == null) {
            return AjaxResult.error("未找到相关任务");
        }
        if (task.getTaskStatus().equals(2) || task.getTaskStatus().equals(4)) {
            return AjaxResult.error("当前状态不可盘点");
        }
        List<TTaskDetail> list = new ArrayList<>();
        for (TaskDetailCheckVO taskDetailCheckVO : tTaskCheckVO.getTaskDetailCheckVOS()) {
            taskDetailCheckVO.setTaskDetailId(taskDetailCheckVO.getId());
            TTaskDetail taskDetail = itTaskDetailService.getById(taskDetailCheckVO.getTaskDetailId());
            if (taskDetail == null) {
                return AjaxResult.error("未找到物料相关盘点任务");
            }
            if(StringUtils.isBlank(taskDetailCheckVO.getBatchNumber())){
                return AjaxResult.error("所盘物料的批次号不可为空");
            }
            if(!taskDetail.getBatchNumber().equals(taskDetailCheckVO.getBatchNumber())){
                return AjaxResult.error("所盘批次号与任务不匹配");
            }
            if(taskDetailCheckVO.getRealyNum() == null){
                return AjaxResult.error("实盘数量不能为空");
            }
            if (!taskDetail.getStatus().equals("0") && !taskDetail.getStatus().equals("1")) {
                return AjaxResult.error("当前状态不可盘点");
            }
            //更新判断任务信息
            taskDetail.setStatus(Constants.TASK_STATUS_END);
            taskDetail.setActualCount(taskDetailCheckVO.getRealyNum());
            list.add(taskDetail);

            // 提交记录
            TCheckHistory history = checkHistoryService.getOne(new QueryWrapper<TCheckHistory>()
                    .eq("task_detail_id", taskDetailCheckVO.getId())
                    .eq("stock_id", taskDetailCheckVO.getStockId())
                    .eq("tray_id", taskDetailCheckVO.getTrayId())
                    .eq("material_code", taskDetailCheckVO.getMaterialCode()));
            if (history != null){
                history.setActualCount(taskDetailCheckVO.getRealyNum());
                history.setPredictCount(taskDetailCheckVO.getStockNum());
                checkHistoryService.updateById(history);
            }else {

                TCheckHistory tCheckHistory = new TCheckHistory();
                tCheckHistory.setTaskDetailId(taskDetailCheckVO.getId());
                tCheckHistory.setStockId(taskDetailCheckVO.getStockId());
                tCheckHistory.setPredictCount(taskDetailCheckVO.getStockNum());
                tCheckHistory.setActualCount(taskDetailCheckVO.getRealyNum());
                tCheckHistory.setMaterialId(taskDetailCheckVO.getMaterialId());
                tCheckHistory.setMaterialCode(taskDetailCheckVO.getMaterialCode());
                tCheckHistory.setTrayId(taskDetailCheckVO.getTrayId());

                checkHistoryService.save(tCheckHistory);
            }
        }

        Long unFinishedCount = itTaskDetailService.count(new QueryWrapper<TTaskDetail>()
                .eq("task_id",task.getId())
                .notIn("status", Constants.TASK_DETAIL_STATUS_END, Constants.TASK_DETAIL_STATUS_APPROVE_ING, Constants.TASK_DETAIL_STATUS_APPROVED));

        if (unFinishedCount > 0) {
            // 执行中
            task.setTaskStatus(Constants.TASK_STATUS_ING);
        } else {
            // 已完成
            task.setTaskStatus(Constants.TASK_STATUS_END);
        }
        tTaskMapper.updateById(task);

        itTaskDetailService.updateBatchById(list);
        return AjaxResult.success();
    }

    /**
     * 新增盘点任务
     *
     * @param tTask 盘点任务
     * @return 结果
     */
    @Override
    public int insertTTask(TTask tTask) {
        return tTaskMapper.insert(tTask);
    }

    /**
     * 修改盘点任务
     *
     * @param tTask 盘点任务
     * @return 结果
     */
    @Override
    public int updateTTask(TTask tTask) {
        return tTaskMapper.updateById(tTask);
    }


    /**
     * 批量删除盘点任务
     *
     * @param ids 需要删除的盘点任务主键
     * @return 结果
     */
    @Override
    public AjaxResult deleteTTaskByIds(Long[] ids) {
        //判断数据是否都已完成
        List<TTask> tasks = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                .in(TTask::getId, ids)
                .eq(TTask::getDelFlag, Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTask> tasks1 = tasks.stream().filter(e -> !Constants.TASK_STATUS_NO.equals(e.getTaskStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tasks1)) {
            String code = tasks1.stream().map(TTask::getCode).collect(Collectors.joining(","));
            return AjaxResult.error(code + "任务已执行，不可删除");
        }
        if (tTaskMapper.deleteTTaskByIds(ids) > 0) {
            //解冻库存
            List<TTaskDetail> taskDetailList = tTaskDetailMapper.selectList(Wrappers.lambdaQuery(TTaskDetail.class)
                    .in(TTaskDetail::getTaskId, ids)
                    .eq(TTaskDetail::getDelFlag, Constants.NO));
            List<Long> stockIds = taskDetailList.stream().filter(e -> e.getStockId() != null).map(TTaskDetail::getStockId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(stockIds)) {
                //以物料为维度的删除
                tStockMapper.updateFreezeByIds(stockIds, Constants.STOCK_IS_FREEZE_NO, "");
            } else {//以库区为维度的删除
                // 要冻结的库存数据
                List<TStock> freezeList = new ArrayList<>();
                List<Long> locationId = taskDetailList.stream().map(TTaskDetail::getLocationId).collect(Collectors.toList());
                List<TStock> stockList = tStockMapper.selectList(
                        new QueryWrapper<TStock>()
                                .in("location_id", locationId).ne("count", 0));
                freezeList.addAll(stockList);
                stockIds = freezeList.stream().map(TStock::getId).collect(Collectors.toList());
                tStockMapper.updateFreezeByIds(stockIds, Constants.STOCK_IS_FREEZE_NO, "");
            }
            return AjaxResult.success(tTaskDetailMapper.deleteDetailByTaskIds(ids));
        }
        return AjaxResult.error();
    }

    /**
     * 删除盘点任务信息
     *
     * @param id 盘点任务主键
     * @return 结果
     */
    @Override
    public int deleteTTaskById(Long id) {
        return tTaskMapper.deleteTTaskById(id);
    }

    @Override
    public List<ExcelTaskVO> export(TTask tTask) {
        List<ExcelTaskVO> export = tTaskMapper.export(tTask);
        if (export.size() > 0) {
            for (ExcelTaskVO excelTaskVO : export) {
                //物料信息
                if (StringUtils.isNotNull(excelTaskVO.getMaterialId())) {
                    TMaterial tMaterial = tMaterialMapper.selectById(excelTaskVO.getMaterialId());
                    excelTaskVO.setMaterialCode(tMaterial.getCode());
                    excelTaskVO.setMaterialName(tMaterial.getName());
                    excelTaskVO.setSpecifications(tMaterial.getSpecifications());
                    //单位信息
                    if (StringUtils.isNotNull(tMaterial.getUnitId())) {
                        TUnit tUnit = tUnitMapper.selectById(tMaterial.getUnitId());
                        excelTaskVO.setUnitName(tUnit.getName());
                    }
                }
                //库位信息
                if (StringUtils.isNotNull(excelTaskVO.getLocationId())) {
                    TLocation tLocation = tLocationMapper.selectById(excelTaskVO.getLocationId());
                    excelTaskVO.setLocationName(tLocation.getName());
                }
            }
        }
        return export;
    }

    /**
     * 批量审核
     *
     * @param ids
     * @return
     */
    @Override
    public AjaxResult approveCheck(Long[] ids) {
        //判断数据是否都已完成
        List<TTask> tasks = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                .in(TTask::getId, ids)
                .eq(TTask::getDelFlag, Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTask> unFinishList = tasks.stream().filter(e -> !Constants.TASK_STATUS_END.equals(e.getTaskStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unFinishList)) {
            return AjaxResult.error("只有已完成的盘点任务才能进行审核");
        }
        //更新任务状态为审核中
        Boolean flage = this.updateTaskStatusBatch(ids, Constants.TASK_STATUS_APPROVE_ING);
        if (flage) {
            tTaskDetailMapper.updateStatusByTaskIds(ids, Constants.TASK_DETAIL_STATUS_APPROVE_ING, Constants.TASK_DETAIL_STATUS_APPROVED);
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 激活盘点任务，逐条执行WCS指令
     *
     * @param ids
     * @return
     */
    @Override
    public AjaxResult activeCheck(Long[] ids) {
        System.out.printf("激活盘点任务开始执行");
        //判断数据是否都已完成
        List<TTask> tasks = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                .in(TTask::getId, ids)
                .eq(TTask::getActivateStatus,0)
                .eq(TTask::getTaskStatus, Constants.TASK_STATUS_NO)
                .eq(TTask::getDelFlag, Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTask> unFinishList = tasks.stream().filter(e -> !"0".equals(e.getActivateStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unFinishList)) {
            return AjaxResult.error("只有未激活的的盘点任务才能进行激活");
        }
        //验证载具的状态
        List<Long> trayIds = tasks.stream().map(e -> e.getTrayId()).collect(Collectors.toList());
        List <TTray> tTrayList = tTrayMapper.selectList(Wrappers.lambdaQuery(TTray.class)
                .in(TTray::getId, trayIds)
                .eq(TTray::getDelFlag, Constants.NO));
        for (TTray item:tTrayList) {
            TTaskWcsRecordDTO tTaskWcsRecordDTO = new TTaskWcsRecordDTO();
            tTaskWcsRecordDTO.setTrayId(item.getId());
            List<TTaskWcsRecordVO> tTaskWcsRecordVOList = tTaskWcsRecordService.getListByTray(tTaskWcsRecordDTO);
            List<TTaskWcsRecordVO> unfinishedList = tTaskWcsRecordVOList.stream().filter(e -> e.getTaskStatus().equals(Constants.WCS_EXECUTE_STATUS_ING) || e.getTaskStatus().equals(Constants.WCS_EXECUTE_STATUS_FAIL)).collect(Collectors.toList());
            //载具空闲、或者载具任务有执行中、执行失败状态的记录
            if (item.getStatus().equals(Constants.TRAY_STATUS_LEISURE) || unfinishedList.size() > 0 ){
                //任务列表去掉 异常的载具
                tasks= tasks.stream().filter(e -> !e.getTrayId().equals(item.getId())).collect(Collectors.toList());
            }
        }

        if (tasks.size()>0) {
            //批量更新任务激活状态和、执行状态
            tasks = tasks.stream().map(item -> {
                item.setActivateStatus("1");  //任务激活状态（0未激活  1已激活）
                //item.setTaskStatus(Constants.TASK_STATUS_ING);  //执行状态为执行中
                this.updateById(item);
                return item;
            }).collect(Collectors.toList());

            TTask tTask = tasks.get(0);
            if (tTask.getTrayType().equals("1")) {  //载具类型 1托盘  物料自动出库
                TTray tTray = tTrayMapper.selectById(tTask.getTrayId());
                takeOut(tTray,tTask.getId());
            }
        }else {
            return AjaxResult.error("没有符合要求的盘点任务可以激活");
        }
        return AjaxResult.success();
    }

    /**
     * 终止已激活的盘点任务
     *
     * @param ids
     * @return
     */
    @Override
    public AjaxResult stopCheck(Long[] ids) {
        //判断数据是否都已完成
        List<TTask> tasks = tTaskMapper.selectList(Wrappers.lambdaQuery(TTask.class)
                .in(TTask::getId, ids)
                .eq(TTask::getDelFlag, Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTask> unFinishList = tasks.stream().filter(e -> "0".equals(e.getActivateStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unFinishList)) {
            return AjaxResult.error("只能终止已激活的的盘点任务");
        }
        //更新激活状态为未激活、任务执行状态1未执行
        List<TTask> updateList = new ArrayList<>();
        for (Long id : ids) {
            TTask task = new TTask();
            task.setId(id);
            task.setActivateStatus("0");
            task.setTaskStatus(Constants.TASK_STATUS_NO);
            updateList.add(task);
        }
        Boolean flage = this.updateBatchById(updateList);
        //批量修改盘点任务明细的执行状态为未执行
//        tTaskDetailMapper.updateStatusByTaskIds(ids, Constants.WCS_EXECUTE_STATUS_NOT, null);
        if (flage) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 批量更新任务状态
     *
     * @param taskIdFinishIds
     * @param status
     * @return
     */
    @Override
    public Boolean updateTaskStatusBatch(Long[] taskIdFinishIds, String status) {
        List<TTask> updateList = new ArrayList<>();
        for (Long id : taskIdFinishIds) {
            TTask task = new TTask();
            task.setId(id);
            task.setTaskStatus(status);
            updateList.add(task);
        }
        return this.updateBatchById(updateList);
    }

    @Transactional
    @Override
    public AjaxResult addTask(AddTaskDTO addTaskDTO) {

        //盘点任务
        if (addTaskDTO.getType().equals(Constants.TASK_TYPE_CHECK)) {
            return addCheckTask(addTaskDTO.getDeliveryIds());
        }
        // 移库任务
        if (Constants.TASK_TYPE_MOVE.equals(addTaskDTO.getType())) {
            return addMoveLibrary(addTaskDTO.getMoveLibrary());
        }
        return null;
    }

    /**
     * 生成盘点任务
     *
     * @param deliveryIds
     * @return
     */
    public AjaxResult addCheckTask(List<Long> deliveryIds) {

        //查询所选盘点计划ids
        CheckDeliveryDTO checkDeliveryDTO = new CheckDeliveryDTO();
        checkDeliveryDTO.setIds(deliveryIds);
        List<CheckDeliveryVO> deliveryVOList = checkDeliveryMapper.selectCheckDeliveryList(checkDeliveryDTO);
        if (CollectionUtils.isEmpty(deliveryVOList)) {
            return AjaxResult.error("未查询到所选计划信息！");
        }
        //判断是否有已生成的盘点任务
        if(deliveryVOList.stream().filter(e -> e.getStatus()!=null && !e.getStatus().equals("0")).collect(Collectors.toList()).size() > 0){
            return AjaxResult.error("已生成的盘点任务不能重复生成！");
        }
        // 要冻结的库存数据
        List<TStock> freezeList = new ArrayList<>();
        List<TStock> freezeListSum = new ArrayList<>();  //根据载具统计该物料的所有库存信息

        // 判断盘点计划是否可以合并生成任务
        CheckDeliveryVO deliveryVO = deliveryVOList.get(0);

        // 以地堆查询时，查询库位为地堆的
        String locationType = "0";
        if (Constants.CHECK_TRAY_TYPE_LAND.equals(deliveryVO.getTrayType())){
            locationType = "1";
        }

        // 以物料为维度
        if (Constants.CHECK_DELIVERY_MATERIAL.equals(deliveryVO.getCheckType())) {
            //得到所有库位
//            List<TLocation> locationList = itLocationService.list(
//                    new QueryWrapper<TLocation>()
//                            .eq("location_type",locationType)  //库位类型(0其他,1地堆)
//                            .eq("del_flag",Constants.DEL_FLAG_NO));
//            if (locationList.size() == 0) {
//                return AjaxResult.error("不存在符合的库位，无法生成盘点任务");
//            }
//            List<Long> locationId = locationList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<Long> locationId = deliveryVOList.stream().filter(e->e.getLocationId()!= null).map(e -> e.getLocationId()).collect(Collectors.toList());
            List<Long> materialIds = deliveryVOList.stream().filter(e->e.getMaterialId()!= null).map(e -> e.getMaterialId()).collect(Collectors.toList());
            List<String> batchCodes = deliveryVOList.stream().filter(e->StringUtils.isNotEmpty(e.getBatchCode())).map(e -> e.getBatchCode()).collect(Collectors.toList());

            List<TStock> stockListSum = tStockMapper.selectDeliveryTrayStock(deliveryVO.getTrayType(),deliveryVO.getAreaId(),null,materialIds,batchCodes,locationId);
            List<TStock> stockList = tStockMapper.selectDeliveryStock2(deliveryVO.getTrayType(),deliveryVO.getAreaId(),null,materialIds,batchCodes,locationId);

            if (stockList.size() == 0){
                return AjaxResult.error("库存中不存在符合的数据，无法生成盘点任务");
            }
            freezeList.addAll(stockList);
            freezeListSum.addAll(stockListSum);
        }

        // 以库区为维度
        if (Constants.CHECK_DELIVERY_LOCATION.equals(deliveryVO.getCheckType())) {
            List<TLocation> locationList = itLocationService.list(
                    new QueryWrapper<TLocation>()
                            .eq("area_id",deliveryVO.getAreaId())
                            .eq("reservoir_id",deliveryVO.getReservoirId())
                            .eq("location_type",locationType)
                            .eq("del_flag",Constants.DEL_FLAG_NO));
            if (locationList.size() == 0) {
                return AjaxResult.error("库区下无合适的库位");
            }
            List<Long> locationId = locationList.stream().map(e -> e.getId()).collect(Collectors.toList());

            List<TStock> stockList = tStockMapper.selectDeliveryStock(deliveryVO.getTrayType(),null,null,locationId);
            if (stockList.size() == 0){
                return AjaxResult.error("库存中不存在符合的数据，无法生成盘点任务");
            }

            freezeList.addAll(stockList);
        }

        // 动碰盘点
        if (Constants.CHECK_DELIVERY_HISTORY.equals(deliveryVO.getCheckType())){

            String star = DateUtils.parseDateToStr("yyyy-MM-dd", deliveryVO.getStartTime());
            String end = DateUtils.parseDateToStr("yyyy-MM-dd", deliveryVO.getEndTime());

            // 查询时间段内发生过库存变动的库存信息
            List<TStockDetail> stockDetailList = stockDetailService.list(new QueryWrapper<TStockDetail>()
                    .between("create_time", star+" 00:00:00", end+" 23:59:59")
                    .eq("del_flag", Constants.DEL_FLAG_NO));
            List<Long> collect = stockDetailList.stream().map(TStockDetail::getLocationId).collect(Collectors.toList());

            if (collect.size() == 0){
                return AjaxResult.error("不存在符合的库位，无法生成盘点任务");
            }

            List<TLocation> locationList = itLocationService.list(
                    new QueryWrapper<TLocation>()
                            .eq("location_type",locationType)
                            .in("id",new HashSet<>(collect))
                            .eq("del_flag",Constants.DEL_FLAG_NO));


            List<Long> locationId = locationList.stream().map(TLocation::getId).collect(Collectors.toList());
            List<TStock> stockList = tStockMapper.selectDeliveryStock(deliveryVO.getTrayType(),null,null,locationId);
            if (stockList.size() == 0){
                return AjaxResult.error("库存中不存在符合的数据，无法生成盘点任务");
            }

            freezeList.addAll(stockList);
        }

        // 随机盘点
        if (Constants.CHECK_DELIVERY_RNADOM.equals(deliveryVO.getCheckType())){
            List<TLocation> locationList = itLocationService.list(
                    new QueryWrapper<TLocation>()
                            .eq("location_type",locationType)
                            .eq("del_flag",Constants.DEL_FLAG_NO));
            if (locationList.size() == 0) {
                return AjaxResult.error("不存在符合的库位，无法生成盘点任务");
            }
            List<Long> locationId = locationList.stream().map(e -> e.getId()).collect(Collectors.toList());

            // 查询要随机盘点的物料
            List<Long> roundMaterial = tStockMapper.getRoundMaterial(deliveryVO.getRandomNum());

            List<TStock> stockList = tStockMapper.selectDeliveryStock(deliveryVO.getTrayType(),null,roundMaterial,locationId);
            if (stockList.size() == 0){
                return AjaxResult.error("此次随机未查询到合适数据，请尝试重新生成盘点任务");
            }

            freezeList.addAll(stockList);
        }

        // 空货位盘点
        if (Constants.CHECK_DELIVERY_EMPTY.equals(deliveryVO.getCheckType())){

            List<TLocation> locationList = itLocationService.list(
                    new QueryWrapper<TLocation>()
                            .eq("area_id",deliveryVO.getAreaId())
                            .eq("reservoir_id",deliveryVO.getReservoirId())
                            .eq("location_type",locationType)
                            .eq("goods_allocation_status","1")
                            .eq("del_flag",Constants.DEL_FLAG_NO));
            if (locationList.size() == 0) {
                return AjaxResult.error("库区下无合适的库位");
            }

            List<Long> locationId = locationList.stream().map(TLocation::getId).collect(Collectors.toList());
            // 查询库位在库存中是否存在数据
            List<TStock> stockList = tStockMapper.selectDeliveryStock(deliveryVO.getTrayType(),null,null,locationId);

            if (stockList.size() > 0){
                freezeList.addAll(stockList);
            }else {
                return AjaxResult.error("库区下无合适的库位");
            }
        }

        if (freezeList.size() == 0) {
            return AjaxResult.error("无库存信息，无法生成盘点任务");
        }

        List<TTask> newTaskList = new ArrayList<>();
        for (TStock item: freezeListSum) {
            //如果库位和批次有值，以物料，库位，批次分
            List<CheckDeliveryVO> devList = deliveryVOList.stream().filter(e -> e.getMaterialId().equals(item.getMaterialId()) && (StringUtils.isNotEmpty(e.getBatchCode()) && e.getBatchCode().equals(item.getBatchCode())) && (e.getLocationId() != null && e.getLocationId().equals(item.getLocationId()))).collect(Collectors.toList());
            if(devList == null || devList.isEmpty()){
                continue;
            }
            Long checkDeliveryId = devList.size() > 0 ? devList.get(0).getId() : 0;
            //拼接盘点计划的ids
            StringJoiner checkDeliveryIdStr = new StringJoiner(",");
            if(devList != null && devList.size() > 0){
                for (CheckDeliveryVO checkDeliveryVO : devList) {
                    checkDeliveryIdStr = checkDeliveryIdStr.add("|" + checkDeliveryVO.getId() + "|");
                }
            }
            //判断是否一个载具是否已有不同物料，如果有，则累加物料数量
            List<TTask> isExistList = newTaskList.stream().filter(e -> e.getTrayId().equals(item.getTrayId()) && e.getLocationId().equals(item.getLocationId())).collect(Collectors.toList());
            if (isExistList.size() > 0) {  //存在
                TTask tTask = isExistList.get(0);
                Long oldCount = tTask.getMaterialCount();
                tTask.setPlanIds(tTask.getPlanIds() + "," + checkDeliveryIdStr.toString() + "");
                tTask.setMaterialCount(oldCount + item.getCount().longValue());
            } else {
                // 生成盘点任务
                TTask task = new TTask();
                task.setTaskType(Constants.TASK_TYPE_CHECK);  //任务类型（3.盘点任务、5移库任务）
                // 获取编号
                String code = itCodeConfigService.getCode("MRWLB");
                if (StringUtils.isEmpty(code)) {
                    throw new ServiceException("编号生成失败");
                }
                task.setCode(code);  //任务编号
                task.setStatus("1");  //任务生成状态（0未生成1已生成）
                task.setCheckType(deliveryVO.getCheckType());  //盘点维度  1物料  2库区
                task.setSourceId(checkDeliveryId);  //盘点计划id
                task.setPlanIds(checkDeliveryIdStr.toString());
                task.setAreaId(item.getAreaId());
                task.setLocationId(item.getLocationId());  //库位ID标识item = {TStock@17239} "TStock{id=null, areaId=8, locationId=4664, materialId=27737, batchCode='2403120001', unitId=null, count=4.000, availableCount=null, status='null', isFreeze='null', originType='null', code='null', trayId=346, trayCategory=null, locationType='null', beginDate=null, endDate=null, producedDate=null, expireDate=null, position='null'}"
                task.setTrayId(item.getTrayId());  //载具(托盘id)
                task.setTrayType(devList.size() > 0 ? devList.get(0).getTrayType() : null);
                task.setMaterialCount(item.getCount().longValue());  //任务详情数量
                task.setTaskStatus(Constants.TASK_STATUS_NO);  //执行状态（0未执行1执行中 2执行完成4已审核）
                task.setDeptId(freezeList.get(0).getDeptId());
                task.setDeptName(freezeList.get(0).getDeptName());


                newTaskList.add(task);
            }
        }
        this.saveBatch(newTaskList);

        //生成任务子表
        List<TTaskDetail> taskDetailSaveList = new ArrayList<>();
        freezeList.forEach(stock -> {
            TTaskDetail taskDetail = new TTaskDetail();
            List<TTask> gettastk = newTaskList.stream().filter(e -> e.getLocationId().equals(stock.getLocationId()) && e.getTrayId().equals(stock.getTrayId())).collect(Collectors.toList());
            if (gettastk.size() > 0) {
                TTask tTask = gettastk.get(0);
                taskDetail.setTaskId(tTask.getId());  //任务id
                taskDetail.setPlanId(tTask.getSourceId());
            }
            taskDetail.setStatus(Constants.TASK_DETAIL_STATUS_NO);
            taskDetail.setDeptId(stock.getDeptId());
            taskDetail.setDeptName(stock.getDeptName());
            taskDetail.setLocationId(stock.getLocationId());  //库位ID标识 库位标识(二楼1排1列1层\二楼1排1列2层\二楼1排1列3层...)
            taskDetail.setMaterialId(stock.getMaterialId());  //物料编号
            taskDetail.setRfid(stock.getRfid());
            taskDetail.setPredictCount(stock.getCount());  //计划数量
            taskDetail.setStockId(stock.getId());   //库存标识
            taskDetail.setTrayId(stock.getTrayId());  //载具(托盘id)
            taskDetail.setBatchNumber(stock.getBatchCode());  //批号
//            taskDetail.setRfidHead(stock.getrf);
//            taskDetail.setDeliveryType("4");
            taskDetailSaveList.add(taskDetail);
        });

        if (itTaskDetailService.saveBatch(taskDetailSaveList)) {
            //更新盘点计划的执行状态
            for (CheckDeliveryVO item: deliveryVOList) {
                item.setStatus("1");
                item.setTaskStatus("1");  //任务生成状态（同步t_task表中的status）
                List<TTask> tTaskList = newTaskList.stream().filter(e -> e.getPlanIds().contains("|" + item.getId() + "|")).collect(Collectors.toList());
                if (tTaskList.size() > 0) {
                    item.setTaskId(tTaskList.get(0).getId());
                }
                checkDeliveryMapper.updateById(item);
            }
            //生成盘点任务后，删除所选的盘点计划
//            Long[] ids = new Long[deliveryIds.size()];
//            deliveryIds.toArray(ids);
//            checkDeliveryMapper.deleteTCheckDeliveryByIds(ids);
            //冻结库存
            List<Long> stockIds = freezeList.stream().map(TStock::getId).collect(Collectors.toList());
            tStockMapper.updateFreezeByIds(stockIds, Constants.STOCK_IS_FREEZE_YES, Constants.STOCK_ORIGIN_TYPE_CHECK);
        }

        return AjaxResult.success();
    }

    /**
     * 生成移库任务
     *
     * @param moveLibrary
     * @return
     */
    private AjaxResult addMoveLibrary(MoveLibraryVo moveLibrary) {
        TTask task = new TTask();
        task.setOriginCode(moveLibrary.getCode());
        task.setSourceId(moveLibrary.getId());
        task.setTaskType(Constants.TASK_TYPE_MOVE);
        task.setTaskCount((long) moveLibrary.getMoveLibraryDetailVoList().size());
        // 获取编号
        String code = itCodeConfigService.getCode("MRWLB");
        if (StringUtils.isEmpty(code)) {
            throw new ServiceException("编号生成失败");
        }
        task.setCode(code);
        SysUser sysUser = SecurityUtils.getLoginUser().getSysUser();
        task.setDeptId(sysUser.getDeptId());
        task.setTaskStatus(Constants.TASK_STATUS_NO);
        tTaskMapper.insert(task);

        List<MoveLibraryDetailVo> detailList = moveLibrary.getMoveLibraryDetailVoList();

        List<Long> taskDetailIds = new ArrayList<>();
//        List<TTaskDetail> taskDetails = new ArrayList<>();
        detailList.forEach(data -> {
            TTaskDetail taskDetail = new TTaskDetail();
            taskDetail.setTaskId(task.getId());
            taskDetail.setLocationId(moveLibrary.getLocationInId());
            taskDetail.setOrgLocationId(moveLibrary.getLocationOutId());
            taskDetail.setTrayId(moveLibrary.getTrayId());
            taskDetail.setMaterialId(data.getMaterialId());
            // 移库任务数量
            taskDetail.setPredictCount(data.getCount());
            taskDetail.setDeptId(moveLibrary.getDeptId());
            taskDetail.setBatchNumber(data.getBatchCode());
            taskDetail.setStatus(Constants.TASK_DETAIL_STATUS_NO);
//            taskDetails.add(taskDetail);
            itTaskDetailService.save(taskDetail);
            taskDetailIds.add(taskDetail.getId());
        });

//        itTaskDetailService.saveBatch(taskDetails);
        Long[] taskIds = {task.getId()};
//        // 调用WCS移库
//        this.updateTaskByMoveLibrary(taskIds, Constants.TASK_TYPE_MOVE);

        //生成wcs任务
        TTaskWcs tTaskWcs = new TTaskWcs();
        tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_MOVE);
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        //托盘信息
        TTray tTray = tTrayMapper.selectById(moveLibrary.getTrayId());
        tTaskWcs.setTrayId(tTray.getId());
        tTaskWcs.setTrayCode(tTray.getCode());
        tTaskWcs.setLocationId(moveLibrary.getLocationInId());
        tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
        tTaskWcs.setPurposePosition(moveLibrary.getLocationInName());
        tTaskWcs.setStartPosition(moveLibrary.getLocationOutName());
        taskWcsService.save(tTaskWcs);

        //保存wcs
        taskDetailIds.forEach( taskDetailId -> {
            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
            tTaskWcsDetail.setTaskId(tTaskWcs.getId());
            tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_MOVE);
            tTaskWcsDetail.setOriginId(taskDetailId);
            taskWcsDetailService.save(tTaskWcsDetail);
        });

        // 更新主数据信息
        tTaskMapper.updateTaskStatus(taskIds, Constants.TASK_STATUS_EXECUTING);
        return AjaxResult.success();
    }

    /**
     * 上架，移库任务调用wcs
     *
     * @param ids
     * @return
     */
    private AjaxResult updateTaskByMoveLibrary(Long[] ids, String status) {

        try {
            List<TaskParam> taskParamList = new ArrayList<>();
            List<TTask> taskList = new ArrayList<>();
            for (Long taskId : ids) {
                List<TTaskDetail> taskDetailList = tTaskDetailMapper.selectList(new LambdaQueryWrapper<TTaskDetail>()
                        .eq(TTaskDetail::getTaskId, taskId)
                        .eq(TTaskDetail::getDelFlag, Constants.DEL_FLAG_NO));
                if (taskDetailList == null || taskDetailList.size() == 0) {
                    throw new ServiceException("执行的任务数量为0！");
                }

                TTask selectTask = tTaskMapper.selectById(taskId);

                if (Constants.YES.equals(selectTask.getStatus())) {
                    throw new ServiceException("任务编号" + selectTask.getCode() + "已上架");
                }

                // 获取其第一条数据
                TTaskDetail taskDetail = taskDetailList.get(0);

                // 获取目标库位和原库位信息
                TaskParam taskParam = new TaskParam();

                // 获取目标库位信息
                TLocation location = tLocationMapper.selectById(taskDetail.getLocationId());

                // 根据托盘id获取托盘信息
                TTray tray = tTrayMapper.selectById(taskDetail.getTrayId());
                //唯一的  任务明细的标识
                taskParam.setOriginTaskId(taskDetail.getId());
                taskParam.setTrayId(taskDetail.getTrayId());
                taskParam.setTrayCode(tray.getCode());

                // 移位
                if (Constants.TASK_TYPE_MOVE.equals(status)) {
                    //1.上架任务、2.拣货任务3.盘点任务、4.回库任务、5移位任务、6.空托盘取出
                    taskParam.setTaskType(5);
                    // 获取原库位信息
                    TLocation orgLocation = tLocationMapper.selectById(taskDetail.getOrgLocationId());
                    // 计划起始位置
                    if (orgLocation != null) {
                        taskParam.setPalnStartPosotionX(Integer.parseInt(orgLocation.getLocationRow() + ""));
                        taskParam.setPalnStartPosotionY(Integer.parseInt(orgLocation.getLocationColumn() + ""));
                        taskParam.setPalnStartPosotionZ(Integer.parseInt(orgLocation.getLocationPlies() + ""));
                    }
                }
                // 计划目的位置
                if (location != null) {
                    taskParam.setAreaId(location.getAreaId());
                    taskParam.setReservoirId(location.getReservoirId());
                    taskParam.setPalnPurposePosotionX(Integer.parseInt(location.getLocationRow() + ""));
                    taskParam.setPalnPurposePosotionY(Integer.parseInt(location.getLocationColumn() + ""));
                    taskParam.setPalnPurposePosotionZ(Integer.parseInt(location.getLocationPlies() + ""));
                }

                TTask task = new TTask();
                task.setId(taskId);
                task.setTaskStatus(Constants.TASK_STATUS_ING);
                taskList.add(task);
                taskParamList.add(taskParam);
            }
            //todo 移位调度机器指令
//            if(Constants.TASK_TYPE_PUT.equals(status)){
//                //入库
//                wcsFegin.warehousingList(taskParamList);
//            }else if(Constants.TASK_TYPE_MOVE.equals(status)){
//                //移位
//                wcsFegin.transposition(taskParamList);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AjaxResult.success();
    }

//    /**
//     * 验证托盘出库或者回库时是否在库，是否在出库的输送线上
//     * @param addTaskDTO
//     */
//    public void vilocation(AddTaskDTO addTaskDTO) {
//        if(StringUtils.isNotEmpty(addTaskDTO.getPalletNum())){
//            List<TLocation> locationListByPalletNum = tLocationMapper.selectList(Wrappers.lambdaQuery(TLocation.class)
//                    .eq(TLocation::getPalletNum,addTaskDTO.getPalletNum())
//                    .eq(TLocation::getDelFlag,Constants.NO));
//            if(locationListByPalletNum != null && locationListByPalletNum.size()>0){
//                throw new ServiceException("托盘在库，不能使用");
//            }
//            if(Constants.TASK_TYPE_PUT.equals(addTaskDTO.getType())){
//                List<String> line_array = redisService.getValuesByPrefix("line_");
//                if(line_array.contains(palletNum)){
//                    throw new ServiceException("托盘在出库口，不能使用");
//                }
//            }
//        }else{
//            addTaskDTO.getTakeDeliveryDetailRecordList().forEach(data -> {
//                TTray tray = tTrayMapper.selectTrayById(data.getTrayId());
//                List<TLocation> locationListByPalletNum = cimsFegin.getLocationListByPalletNum(tray.getCode());
//                if(locationListByPalletNum != null && locationListByPalletNum.size()>0){
//                    throw new ServiceException("托盘在库，不能使用");
//                }
//                if(Constants.TASK_TYPE_PUT.equals(addTaskDTO.getType())){
//                    List<String> line_array = redisService.getValuesByPrefix("line_");
//                    if(line_array.contains(tray.getCode())){
//                        throw new ServiceException("托盘在出库口，不能使用");
//                    }
//                }
//            });
//        }
//    }

//    /**
//     * 回库任务
//     * @param addTaskDTO
//     */
//    private AjaxResult addBackTack(AddTaskDTO addTaskDTO){
//
//        if (StringUtils.isEmpty(addTaskDTO.getPalletNum())){
//            return AjaxResult.error("托盘编号不可为空");
//        }
//
//        // 查询托盘信息
//        TTray tray = tTrayMapper.selectOne(new QueryWrapper<TTray>().eq("code", addTaskDTO.getPalletNum()));
//        if (tray == null){
//            return AjaxResult.error("该托盘不存在，请查证编号");
//        }
//
//        // 查询托盘所在库位信息
//        TLocation location = tLocationMapper.selectById(tray.getLocationId());
//
//        TLocation locationRecommend = null;
//        // 固定托盘、盘点回库时不更改位置  原路返回
//        if (Constants.BACK_TYPE_CHECK.equals(addTaskDTO.getBackType())){
//            locationRecommend = location;
//            locationRecommend.setStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
//            locationRecommend.setPalletNum(tray.getCode());
//            tLocationMapper.updateById(locationRecommend);
//            //盘点任务，原路返回
//        } else {
//            // 获取推荐库位
//            StrategyParam strategyParam = new StrategyParam();
//            if(location != null){
//                strategyParam.setCategoryId(location.getCategoryId());
//                strategyParam.setSameBatchFlag(location.getSameBatchFlag());
//                strategyParam.setSameMaterialFlag(location.getSameMaterialFlag());
//            }
//            //非固定托盘推荐
//            R<Long> recommended = remoteStrategyService.unfixedRecommended(strategyParam, SecurityConstants.INNER);
//            if(R.FAIL == recommended.getCode()){
//                throw new ServiceException(recommended.getMsg());
//            }
////            List<Long> locationIds = new ArrayList<>();
////            locationIds.add(recommended.getData());
////            Map<Long, TLocationVO> locationMaps = cimsFegin.getLocationByIds(locationIds);
////            TLocationVO locationVO = locationMaps.get(recommended.getData());
//            TLocation tLocation = tLocationMapper.selectById(recommended.getData());
//            if (tLocation == null){
//                return AjaxResult.error("无推荐库位");
//            }
//            locationRecommend = tLocation;
//            locationRecommend.setStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4);
//            locationRecommend.setPalletNum(tray.getCode());
//            tLocationMapper.updateById(locationRecommend);
//        }
//        // 创建任务
//        TTask task = new TTask();
//        task.setTaskStatus(Constants.TASK_STATUS_EXECUTING);
//        task.setTaskType(Constants.TASK_TYPE_BACK);
//        task.setTaskCount(1L);
//        // 获取编号
//        String code = itCodeConfigService.getCode("MRWLB");
//        if (StringUtils.isEmpty(code)){
//            throw new ServiceException("编号生成失败");
//        }
//        task.setCode(code);
//        task.setDeptId(locationRecommend.getDeptId());
//        tTaskMapper.insert(task);
//
//        TTaskDetail taskDetail = new TTaskDetail();
//        taskDetail.setTaskId(task.getId());
//        taskDetail.setStatus(Constants.TASK_DETAIL_STATUS_NO);
//        taskDetail.setTrayId(tray.getId());
//        if(location!=null){
//            taskDetail.setOrgLocationId(location.getId());
//        }
//        taskDetail.setLocationId(locationRecommend.getId());
//        taskDetail.setDeptId(locationRecommend.getDeptId());
//
//        tTaskDetailMapper.insert(taskDetail);
//
//        List<TaskParam> taskParamList = new ArrayList<>();
//        TaskParam taskParam = new TaskParam();
//        taskParam.setOriginTaskId(taskDetail.getId());
//        taskParam.setTaskType(Integer.valueOf(Constants.TASK_TYPE_BACK));
//        taskParam.setTrayId(tray.getId());
//        taskParam.setAreaId(locationRecommend.getAreaId());
//        taskParam.setReservoirId(locationRecommend.getReservoirId());
//        taskParam.setPalnPurposePosotionX(locationRecommend.getLocationRow().intValue());
//        taskParam.setPalnPurposePosotionY(locationRecommend.getLocationColumn().intValue());
//        taskParam.setPalnPurposePosotionZ(locationRecommend.getLocationPlies().intValue());
//        taskParamList.add(taskParam);
//        //todo 回库调度机器指令
////        wcsFegin.back(taskParamList);
//        return AjaxResult.success();
//    }
    @Override
    public     TaskVO getTaskInfoByTaskWcsId(Long taskwcsId){
        return  tTaskMapper.getTaskInfoByTaskWcsId(taskwcsId);
    }

    /**
     * 盘点计划任务激活，发送出库指令
     * @param tTray
     * @return
     */
    @Override
    public AjaxResult takeOut(TTray tTray,Long taskId) {
        System.out.printf("开始执行盘点计划任务激活发送出库指令");
//        托盘取出：1.task_wcs记录
//        2.库位location   pallet_num托盘编号 为空    goods_allocation_status货位状态(1,无货,2,有货,3,标记出库,4,标记入库)  变为无货
        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        if (StringUtils.isEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("托盘未在库");
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
//        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
//        if (taskCount > 0) {
//            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
//        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
            List<TTaskWcs> taskWcsList = tTaskWcsMapper.selectList(taskQw);
            if (taskWcsList.size() > 0) {
                List<TTaskWcs> unfinishedList = taskWcsList.stream().filter(item -> item.getTaskStatus().equals(Constants.WCS_EXECUTE_STATUS_FAIL) || item.getTaskStatus().equals(Constants.WCS_EXECUTE_STATUS_ING)).collect(Collectors.toList());
                if (unfinishedList.size() == 0) {
                    return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
                }
            }
            if (tTrayVO.getLocationId() == null) {
                return AjaxResult.error("未查询到托盘库位信息");
            }

        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_OUT);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记出库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_3));
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                String endStation = WcsReportUtil.stationOut;
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsOutReportCheck(orderDTO,tTrayVO.getId(),taskId);  //发送盘点出库命令
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = locationInfo.getCode();
                String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcs.getId(), status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum("");
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }

    /**
     * 盘点计划任务，发送入库指令
     * @param tTray
     * @return
     */
    public AjaxResult recycle(TTray tTray) {
        System.out.printf("开始执行盘点计划任务激活发送入库指令");
//        托盘回库：1.task_wcs记录
//        2.如果tray中有location值则回到原始位置，如果没有没有推荐库位 填充
//        3.库位location   pallet_num托盘编号 为有值    goods_allocation_status货位状态(1,无货,2,有货,3,标记出库,4,标记入库)  根据托盘是否有货  填充是否有货
        TTrayApiVO tTrayVO = tTrayMapper.getTrayInfo(StringUtils.clearLine(tTray.getCode()), tTray.getId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到托盘信息");
        }
        //查询是否有正在执行中的载具
        QueryWrapper<TTaskWcs> taskQw = new QueryWrapper<>();
        taskQw.eq("del_flag", Constants.DEL_FLAG_NO);
        taskQw.notIn("task_status", Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        taskQw.eq("tray_id", tTrayVO.getId());
        Long taskCount = tTaskWcsMapper.selectCount(taskQw);
        if (taskCount > 0) {
            return AjaxResult.error("所选载具有未执行完成的任务，请先执行任务");
        }
        if (StringUtils.isNotEmpty(tTrayVO.getPalletNum())){
            return AjaxResult.error("当前托盘不可执行回库操作");
        }
        if (tTrayVO.getLocationId() == null) {
            TLocation locationInfoById = tTrayMapper.getLocationById(tTrayVO.getId());
            if (locationInfoById == null) {
                Long locationId = recommendedLocationUtil.recommendedLocation(null, tTrayVO.getId(), null, null);
                if (locationId == null) {
                    return AjaxResult.error("未获取到可用库位");
                } else {
                    tTrayVO.setLocationId(locationId);
                    //推荐库位时，不推荐地堆库位
                    tTrayVO.setLocationType(Constants.LOCATION_TYPE_DEFAULT);
                }
            } else {
                tTrayVO.setLocationId(locationInfoById.getId());
                tTrayVO.setLocationType(locationInfoById.getLocationType());
            }
        }
        TTray taryDTO = new TTray();
        taryDTO.setId(tTrayVO.getId());
        taryDTO.setLocationId(tTrayVO.getLocationId());
        tTrayMapper.updateById(taryDTO);
        //判断是否地堆，地堆不生成wcs
        if (Constants.LOCATION_TYPE_DEFAULT.equals(tTrayVO.getLocationType())) {
            TTaskWcs taskWcs = new TTaskWcs();
            taskWcs.setTaskType(Constants.TASK_TYPE_BACK);
            taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
            taskWcs.setTrayId(tTrayVO.getId());
            taskWcs.setTrayCode(tTrayVO.getCode());
            taskWcs.setLocationId(tTrayVO.getLocationId());
            taskWcs.setTaskNo(itCodeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcs.setMainTaskNo(taskWcs.getTaskNo());
            tTaskWcsMapper.insert(taskWcs);
            //更新库位为标记入库
            tLocationMapper.update(new TLocation(),
                    new UpdateWrapper<TLocation>()
                            .eq("id", tTrayVO.getLocationId())
                            .set("goods_allocation_status", Constants.LOCATION_GOODS_ALLOCATION_STATUS_4));
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
                String startStation = WcsReportUtil.stationIn;
                String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                //出库参数
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                //移库参数
                WcsOrderDTO taskNoMove = wcsMoveUtil.dealDoubleExtension(null, taskWcs.getTaskNo(), locationInfo.getCode());
                if (taskNoMove != null) {
                    taskNoMove.setMainTaskNo(orderDTO.getTaskNo());
                    wcsReportUtil.sendWcsMoveReport(taskNoMove);
                    orderDTO.setMoveTaskNo(taskNoMove.getTaskNo());
                }
                wcsReportUtil.sendWcsInReportCheck(orderDTO);  //盘点完成，发送入库指令
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
//                //发送命令
//                TLocation locationInfo = tLocationMapper.selectById(tTrayVO.getLocationId());
//                String startStation = Constants.SHELF_POINT_SECOND_LINE_IN;
//                String endStation = locationInfo.getCode();
//                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcs.getId(), taskWcs.getTaskNo(), startStation, endStation, tTrayVO.getCode());
//                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
//                tTaskWcsMapper.updateStuasById(taskWcs.getId(),status);
            }
        } else {
            //地堆，直接更改
            TLocation tLocation = new TLocation();
            tLocation.setId(tTrayVO.getLocationId());
            tLocation.setPalletNum(tTrayVO.getCode());
            //根据载具去查stock表查询是否有货
            QueryWrapper<TStock> stockQw = new QueryWrapper<>();
            stockQw.eq("del_flag", Constants.DEL_FLAG_NO);
            stockQw.eq("tray_id", tTrayVO.getId());
            Long trayStockCount = tStockMapper.selectCount(stockQw);
            if (trayStockCount != null && trayStockCount > 0) {//有货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            } else {//无货
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }
            tLocationMapper.updateById(tLocation);
        }
        return AjaxResult.success();
    }
}
