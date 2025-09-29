package com.xsrw.wms.inout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITAreaService;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITReservoirService;
import com.xsrw.wms.base.service.ITUnitService;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import com.xsrw.wms.inout.mapper.*;
import com.xsrw.wms.inout.service.ITTaskWcsDetailService;
import com.xsrw.wms.stock.domain.*;
import com.xsrw.wms.stock.domain.vo.MoveLibraryDetailVo;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * wcs任务详情Service业务层处理
 *
 * @author wxr
 * @date 2023-05-10
 */
@Service
public class TTaskWcsDetailServiceImpl extends ServiceImpl<TTaskWcsDetailMapper, TTaskWcsDetail> implements ITTaskWcsDetailService {
    @Autowired
    private TTaskWcsDetailMapper tTaskWcsDetailMapper;
    @Autowired
    private TTaskMapper tTaskMapper;
    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;
    @Autowired
    private ITStockService itStockService;
    @Autowired
    private TLocationMapper tLocationMapper;
    @Autowired
    private TTrayMapper trayMapper;
    @Autowired
    private ITMoveLibraryService itMoveLibraryService;
    @Autowired
    private ITMoveLibraryDetailService itMoveLibraryDetailService;
    @Autowired
    private ITAreaService itAreaService;
    @Autowired
    private ITReservoirService itReservoirService;
    @Autowired
    private ITMaterialService itMaterialService;
    @Autowired
    private ITUnitService itUnitService;
    @Autowired
    private ITStockDetailService itStockDetailService;
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;
    @Autowired
    private TAdvanceDeliveryMapper advanceDeliveryMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper advanceDeliveryDetailMapper;
    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;
    @Autowired
    private TTaskOutMapper tTaskOutMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;
    @Autowired
    private TOutDeliveryMapper tOutDeliveryMapper;
    @Autowired
    private ITStockMainService stockMainService;


    /**
     * 查询wcs任务详情列表
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return wcs任务详情
     */
    @Override
    public List<TTaskWcsDetail> selectTTaskWcsDetailList(TTaskWcsDetail tTaskWcsDetail) {
        return tTaskWcsDetailMapper.selectTTaskWcsDetailList(tTaskWcsDetail);
    }

    /**
     * 查询wcs任务详情
     *
     * @param id wcs任务详情主键
     * @return wcs任务详情
     */
    @Override
    public TTaskWcsDetail selectTTaskWcsDetailById(Long id) {
        return tTaskWcsDetailMapper.selectById(id);
    }

    @Override
    public List<TTaskWcsDetailVO> selectStatusWcsListByTrayId(Long trayId, String taskType) {
        return tTaskWcsDetailMapper.selectStatusWcsListByTrayId(trayId,taskType);
    }

    /**
     * 新增wcs任务详情
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return 结果
     */
    @Override
    public int insertTTaskWcsDetail(TTaskWcsDetail tTaskWcsDetail) {
        return tTaskWcsDetailMapper.insert(tTaskWcsDetail);
    }

    /**
     * 修改wcs任务详情
     *
     * @param tTaskWcsDetail wcs任务详情
     * @return 结果
     */
    @Override
    public int updateTTaskWcsDetail(TTaskWcsDetail tTaskWcsDetail) {
        return tTaskWcsDetailMapper.updateById(tTaskWcsDetail);
    }


    /**
     * 批量删除wcs任务详情
     *
     * @param ids 需要删除的wcs任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsDetailByIds(Long[] ids) {
        return tTaskWcsDetailMapper.deleteTTaskWcsDetailByIds(ids);
    }

    /**
     * 删除wcs任务详情信息
     *
     * @param id wcs任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsDetailById(Long id) {
        return tTaskWcsDetailMapper.deleteTTaskWcsDetailById(id);
    }
    @Override
    public List<TTaskWcsDetailVO> getShiftDetail(Long taskwcsId){
        return  tTaskWcsDetailMapper.getShiftDetail(taskwcsId);
    }
    /**
     * 移库成功，更新库存消息
     * @param taskwcsId
     * @return
     */
    @Transactional
    @Override
    public AjaxResult updateStock(Long taskwcsId){
        //获取任务id
        TTask taskVO =tTaskMapper.getTaskInfoByTaskWcsId(taskwcsId);
        if(taskVO==null){
            return AjaxResult.error("任务不存在！");
        }
        if(taskVO.getTaskStatus().equals(Constants.TASK_STATUS_END)){
            return  AjaxResult.error("任务已完成！");
        }
        // 获取当前主数据
        MoveLibraryVo moveLibrary = selectMoveLibraryById(taskVO.getSourceId());
        if(moveLibrary==null){
            return AjaxResult.error("移库信息不存在！");
        }
        List<Long> stockIds = moveLibrary.getMoveLibraryDetailVoList().stream().map(MoveLibraryDetailVo::getStockId).collect(Collectors.toList());

        // 获取该载具上所有物料数据
        List<TStock> stockList = itStockService.list(new LambdaQueryWrapper<TStock>()
                .in(TStock::getId,stockIds)
                .gt(TStock::getCount,0)
                .eq(TStock::getDelFlag, Constants.DEL_FLAG_NO));

        // 获取库位信息
        List<Long> locationIds = new ArrayList<>();
        locationIds.add(moveLibrary.getLocationInId());
        TLocation location=tLocationMapper.selectById(moveLibrary.getLocationInId());
        // 移库详情
        for(TStock model:stockList){
            // 更新库位区域
            if (ObjectUtils.isNotEmpty(location.getAreaId())){
                model.setAreaId(location.getAreaId());
            }

            // 更新库位信息
            model.setLocationId(moveLibrary.getLocationInId());
        }
        itStockService.updateBatchById(stockList);
        // 解冻库存
        itStockService.updateFreezeByIds(stockIds,Constants.STOCK_IS_FREEZE_NO,Constants.STOCK_ORIGIN_TYPE_MOVE);

        TTray tray = trayMapper.selectById(moveLibrary.getTrayId());

        // 更新现库位信息
        if (Constants.TRAY_STATUS_LEISURE.equals(tray.getStatus())){
            // 移库任务更新库位信息
            updateStatusByMove(moveLibrary.getLocationOutId(),moveLibrary.getLocationInId(),tray.getCode(),Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);

        }else {
            // 移库任务更新库位信息
            updateStatusByMove(moveLibrary.getLocationOutId(),moveLibrary.getLocationInId(),tray.getCode(),Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
        }


        TTray updateTray = new TTray();
        updateTray.setId(moveLibrary.getTrayId());
        // 更新库位状态
        updateTray.setLocationId(moveLibrary.getLocationInId());

        // 更新托盘状态
        trayMapper.updateById(updateTray);
        //更新任务详情表
        List<TTaskDetail> list=tTaskDetailMapper.selectList(new LambdaQueryWrapper<TTaskDetail>()
                .eq(TTaskDetail::getTaskId,taskVO.getId())
                .eq(TTaskDetail::getDelFlag,Constants.DEL_FLAG_NO)
        );
        list.forEach(tTaskDetail -> {
            tTaskDetail.setActualCount(tTaskDetail.getPredictCount());
            tTaskDetail.setStatus(Constants.TASK_DETAIL_STATUS_END);
            tTaskDetailMapper.updateById(tTaskDetail);
        });
        //更新任务主表
        taskVO.setTaskStatus(Constants.TASK_DETAIL_STATUS_END);
        tTaskMapper.updateById(taskVO);
        //更新t_task_wcs
        TTaskWcs tTaskWcs=tTaskWcsMapper.selectById(taskwcsId);
        if(tTaskWcs!=null){
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
            tTaskWcsMapper.updateById(tTaskWcs);
        }
        // 新增库存详情
        saveBatchDetailByMove(stockList, taskVO.getId(), moveLibrary.getLocationOutId(),Constants.TASK_TYPE_MOVE);
        //更新移库物料详情信息
        this.moveTask(taskwcsId);

        return AjaxResult.success();
    }
    public void saveBatchDetailByMove(List<TStock> stockList, Long taskId, Long locationId, String type){

        List<TStockDetail> stockDetailInsertList = new ArrayList<>();

        // 循环新增
        stockList.forEach(stock ->{

//            List<StockDetail> stockDetailList = stockDetailMapper.selectList(new LambdaQueryWrapper<StockDetail>()
//                .eq(StockDetail::getLocationId,locationId)
//                .eq(StockDetail::getMaterialId,stock.getMaterialId())
//                .orderByDesc(StockDetail::getId));

            // 库位更新前库存详情
            TStockDetail stockOutDetail = new TStockDetail();

            // 库位标识
            stockOutDetail.setLocationId(locationId);
            // 物料标识
            stockOutDetail.setMaterialId(stock.getMaterialId());
            // 任务类型（上架任务、回库任务、盘点任务、移库任务、拣货任务）
            stockOutDetail.setType(type);
            // 任务ID
//            stockOutDetail.setTaskId(taskId);
            // 删除(0未删除 1:删除)
            stockOutDetail.setDelFlag(Constants.DEL_FLAG_NO);

            // 操作前数量
            stockOutDetail.setBeforeCount(stock.getCount());
            // 操作后当前数量
            stockOutDetail.setCurrentCount(0L);
            //批次号
            stockOutDetail.setBatchCode(stock.getBatchCode());
            stockDetailInsertList.add(stockOutDetail);

            // 库位更新后库存详情
            TStockDetail stockInDetail = new TStockDetail();

            // 库位标识
            stockInDetail.setLocationId(stock.getLocationId());
            // 物料标识
            stockInDetail.setMaterialId(stock.getMaterialId());
            // 任务类型（上架任务、回库任务、盘点任务、移库任务、拣货任务）
            stockInDetail.setType(type);
            // 任务ID
//            stockInDetail.setTaskId(taskId);
            // 删除(0未删除 1:删除)
            stockInDetail.setDelFlag(Constants.DEL_FLAG_NO);

            // 操作后当前数量
            stockInDetail.setCurrentCount(stock.getCount());
            // 操作前数量
            stockInDetail.setBeforeCount(0L);
            //批次号
            stockInDetail.setBatchCode(stock.getBatchCode());
            stockDetailInsertList.add(stockInDetail);

        });

        itStockDetailService.saveBatch(stockDetailInsertList);
    }

    /**
     * 更新移库物料详情信息
     * @param id
     */
    public void moveTask(Long id){
        List<TTaskWcsDetailVO> list = tTaskWcsDetailMapper.getListByTaskId(id, Constants.TASK_TYPE_MOVE);
        for (TTaskWcsDetailVO tTaskWcsDetailVO : list) {
            TMaterialDetail updateDo = new TMaterialDetail();
            updateDo.setLocationId(tTaskWcsDetailVO.getLocationId());
            updateDo.setTrayId(tTaskWcsDetailVO.getTrayId());
            tMaterialDetailMapper.update(updateDo,
                    new UpdateWrapper<TMaterialDetail>()
                            .set("del_flag",Constants.DEL_FLAG_NO)
                            .set("status",Constants.MATERIAL_DETAIL_STATUS_IN)
                            .eq("material_id",tTaskWcsDetailVO.getMaterialId())
                            .eq("batch_code",tTaskWcsDetailVO.getBatchCode())
                            .eq("location_id",tTaskWcsDetailVO.getOrgLocationId()));
        }

    }

    /**
     * 移库任务更新库位信息
     *
     * @param locationOutId 转出库位
     * @param locationInId  转入库位
     * @param palletNum     载具编号
     * @return
     */
    public  AjaxResult updateStatusByMove(Long locationOutId, Long locationInId, String palletNum, String goodsAllocationStatus) {
        TLocation locationOut = tLocationMapper.selectById(locationOutId);
        TLocation locationIn = tLocationMapper.selectById(locationInId);

        // 转入库位更新为有货。更新载具信息
        if (locationIn != null) {
            locationIn.setGoodsAllocationStatus(goodsAllocationStatus);
            locationIn.setPalletNum(palletNum);
            locationIn.setDepositCategoryId(locationOut.getDepositCategoryId());
            locationIn.setSameMaterialFlag(locationOut.getSameMaterialFlag());
            locationIn.setSameBatchFlag(locationOut.getSameBatchFlag());
        }

        // 转出库位,状态更新为无货，清空载具信息,更改库位状态信息
        if (locationOut != null) {
            locationOut.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            locationOut.setPalletNum("");
            locationOut.setDepositCategoryId(null);
            tLocationMapper.updateById(locationOut);
        }
        tLocationMapper.updateById(locationIn);
        return AjaxResult.success();
    }


    /**
     * 查询库内移位
     *
     * @param id 库内移位主键
     * @return 库内移位
     */
    public MoveLibraryVo selectMoveLibraryById(Long id) {
        MoveLibraryVo moveLibraryVo = new MoveLibraryVo();
        // 获取当前主数据
        TMoveLibrary moveLibrary = itMoveLibraryService.getById(id);

        BeanUtils.copyProperties(moveLibrary, moveLibraryVo); //第一个参数是：目标存储，第二个参数是：源数据

        // 获取库位信息
        TLocation locationOut=tLocationMapper.selectById(moveLibrary.getLocationOutId());
        if(ObjectUtils.isNotNull(locationOut)){

//            if(ObjectUtils.isNotEmpty(locationOut.getWarehouseId())){
//                //库房ID
//                moveLibraryVo.setWarehouseId(locationOut.getWarehouseId());
//                //库房CODE
//                moveLibraryVo.setWarehouseCode(locationOut.getWarehouseCode());
//                /** 转出仓库*/
//                moveLibraryVo.setWarehouseName(locationOut.getWarehouseName());
//            } else {
//                // 获取该组织下立库信息
//                Warehouse Warehouse = getStereoscopicWarehouse();
//
//                //库房ID
//                moveLibraryVo.setWarehouseId(Warehouse.getId());
//                //库房CODE
//                moveLibraryVo.setWarehouseCode(Warehouse.getCode());
//                /** 转出仓库*/
//                moveLibraryVo.setWarehouseName(Warehouse.getName());
//            }

            TArea tArea=itAreaService.selectTAreaById(locationOut.getAreaId());
            /** 转出区域 */
            moveLibraryVo.setAreaName(tArea.getName());
            TReservoir tReservoir=itReservoirService.getById(locationOut.getReservoirId());
            /** 转出库区 */
            moveLibraryVo.setReservoirName(tReservoir.getName());

            /** 转出库位 */
            moveLibraryVo.setLocationOutName(locationOut.getName());
        }

        // 转入库位信息
        TLocation locationIn = tLocationMapper.selectById(moveLibraryVo.getLocationInId());

        if(ObjectUtils.isNotNull(locationIn)){
            /** 转入库位 */
            moveLibraryVo.setLocationInName(locationIn.getName());
        }

        List<TMoveLibraryDetail> moveLibraryDetailList = itMoveLibraryDetailService.list(new LambdaQueryWrapper<TMoveLibraryDetail>()
                .eq(TMoveLibraryDetail::getMoveLibraryCode,moveLibrary.getCode())
                .eq(TMoveLibraryDetail::getDelFlag, Constants.DEL_FLAG_NO));
        List<MoveLibraryDetailVo> moveLibraryDetailVoList = new ArrayList<>();
        for(TMoveLibraryDetail moveLibraryDetail : moveLibraryDetailList){
            MoveLibraryDetailVo moveLibraryDetailVo = new MoveLibraryDetailVo();
            BeanUtils.copyProperties(moveLibraryDetail, moveLibraryDetailVo); //第一个参数是：目标存储，第二个参数是：源数据

            TMaterial materiaData = itMaterialService.getById(moveLibraryDetailVo.getMaterialId());
            if (materiaData != null) {
                // 物料编码
                moveLibraryDetailVo.setMaterialCode(materiaData.getCode());
                // 物料名称
                moveLibraryDetailVo.setMaterialName(materiaData.getName());
                // 规格型号
                moveLibraryDetailVo.setSpecifications(materiaData.getSpecifications());
                TUnit tUnit=itUnitService.getById(materiaData.getUnitId());
                // 单位名称
                moveLibraryDetailVo.setUnitName(tUnit.getName());
            }
            moveLibraryDetailVoList.add(moveLibraryDetailVo);
        }
        moveLibraryVo.setMoveLibraryDetailVoList(moveLibraryDetailVoList);
        return moveLibraryVo;
    }

    /**
     * 作废
     * @param taskWcs
     * @return
     */
    @Override
    @Transactional
    public AjaxResult cancellationDelivery(TTaskWcs taskWcs) {
        TTaskWcs taskWcsVO = tTaskWcsMapper.selectById(taskWcs.getId());
        if(taskWcsVO == null){
            return AjaxResult.error("未查询到对应任务信息");
        }
        if(!Constants.WCS_EXECUTE_STATUS_NOT.equals(taskWcsVO.getTaskStatus())
                && !Constants.WCS_EXECUTE_STATUS_FAIL.equals(taskWcsVO.getTaskStatus())){
            return AjaxResult.error("当前任务状态不可作废");
        }
        List<TAdvanceDelivery> deliveryList = tTaskWcsDetailMapper.getDeliveryIdsByTaskId(taskWcs.getId());
        if(CollectionUtils.isEmpty(deliveryList)){
            return AjaxResult.error("未查询到对应入库单信息");
        }
        List<TAdvanceDelivery> endList = deliveryList.stream().filter(e -> !Constants.INOUT_STATUS_NOT.equals(e.getCompleteState())).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(endList)){
            return AjaxResult.error("有已上架的入库单，不可作废");
        }
        List<Long> deliveryIds = deliveryList.stream().map(TAdvanceDelivery::getId).collect(Collectors.toList());
        //更新入库单主表状态为作废
        advanceDeliveryMapper.updateStatusByIds(Constants.INOUT_STATUS_FAILED, deliveryIds);
        //更新入库单子表状态为作废
        advanceDeliveryDetailMapper.updateStatusByDeliveryIds(Constants.INOUT_NEXTFLAG_ABOLISH, deliveryIds);
        // 更新任务状态为 已作废
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        tTaskWcsMapper.updateById(taskWcs);

        //放开库位
        TLocation tLocation = tLocationMapper.selectById(taskWcsVO.getLocationId());
        if(tLocation != null && StringUtils.isNotNull(tLocation.getPalletNum())){
            TLocation locationDTO = new TLocation();
            locationDTO.setId(tLocation.getId());
            locationDTO.setPalletNum("");
            locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            tLocationMapper.updateById(locationDTO);
        }
        //放开载具,状态设置为半托，库位id设置为null
        TTray tTray = new TTray();
        trayMapper.update(tTray, new UpdateWrapper<TTray>().set("location_id", null).set("status", Constants.TRAY_STATUS_HALF).eq("id", taskWcsVO.getTrayId()));
        return AjaxResult.success();
    }


    /**
     * 更新拣货任务优先级
     * @param id
     * @param priority
     * @return
     */
    @Override
    public AjaxResult updatePriority(Long id, String priority) {

        TTaskWcs tTaskWcs = tTaskWcsMapper.selectById(id);
        if (tTaskWcs == null){
            return AjaxResult.error("拣货任务不存在");
        }
        tTaskWcs.setPriority(priority);

        tTaskWcsMapper.updateById(tTaskWcs);
        return AjaxResult.success();
    }


    /**
     * 出库拣货任务作废
     * @param taskNo
     * @return
     */
    @Transactional
    @Override
    public AjaxResult outCancellation(String taskNo) {

        if (StringUtils.isEmpty(taskNo)){
            return AjaxResult.error("参数不可为空");
        }

        // 查询任务信息
        TTaskWcs tTaskWcs = tTaskWcsMapper.selectOne(new QueryWrapper<TTaskWcs>()
                .eq("task_no",taskNo).eq("del_flag",Constants.DEL_FLAG_NO));
        if (tTaskWcs == null){
            return AjaxResult.error("任务不存在");
        }

        // 已完成、已废弃的单据不可进行操作
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())
                || Constants.WCS_EXECUTE_STATUS_CANCELLATION.equals(tTaskWcs.getTaskStatus())){
            return AjaxResult.error("任务当前状态不可执行作废操作");
        }

        // 查询出库任务
        TTaskOut tTaskOut = tTaskOutMapper.selectOne(new QueryWrapper<TTaskOut>()
                .eq("wcs_id", tTaskWcs.getId()).eq("del_flag", Constants.DEL_FLAG_NO));

        // 查询出库单
        TOutDelivery tOutDelivery = tOutDeliveryMapper.selectById(tTaskOut.getOutDeliveryId());

        // 出库单为部分完成时不可作废
        if (Constants.INOUT_STATUS_PART.equals(tOutDelivery.getStatus())){
            return AjaxResult.error("出库计划部分完成，不可作废");
        }

        // 校验所有载具是否均已回库
        List<TTaskOut> taskOutList = tTaskOutMapper.selectList(new QueryWrapper<TTaskOut>().eq("out_delivery_id", tOutDelivery.getId()));
        List<Long> trayIds = taskOutList.stream().map(TTaskOut::getTrayId).collect(Collectors.toList());
        List<TTray> trayList = trayMapper.selectList(new QueryWrapper<TTray>().in("id", trayIds));
        List<String> palletNum = trayList.stream().map(TTray::getCode).collect(Collectors.toList());
        List<TLocation> locationList = tLocationMapper.selectList(new QueryWrapper<TLocation>().in("pallet_num", palletNum));
        if (trayList.size() != locationList.size()){
            return AjaxResult.error("任务载具未回库，不可作废");
        }

        // 更新出库单 为 已作废
        tOutDelivery.setStatus(Constants.INOUT_STATUS_REGISTER_ABOLISH);
        tOutDeliveryMapper.updateById(tOutDelivery);

        // 更新出库单详情 为 已作废
        TOutDeliveryDetail outDeliveryDetail = new TOutDeliveryDetail();
        outDeliveryDetail.setNextFlag("3");
        tOutDeliveryDetailMapper.update(outDeliveryDetail,
                new UpdateWrapper<TOutDeliveryDetail>().eq("out_delivery_id",tOutDelivery.getId()));

        // 更新任务状态为 已作废
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        tTaskWcsMapper.updateById(tTaskWcs);

        // 恢复物料在库可用数量
        List<TOutDeliveryDetail> tOutDeliveryDetails = tOutDeliveryDetailMapper.selectList(new QueryWrapper<TOutDeliveryDetail>()
                .eq("out_delivery_id", tOutDelivery.getId())
                .eq("del_flag", Constants.DEL_FLAG_NO));
        tOutDeliveryDetails.forEach(detail -> {
            TStockMain stockMain = stockMainService.getOne(
                    new QueryWrapper<TStockMain>()
                            .eq("material_id", detail.getMaterialId())
                            .eq("del_flag", Constants.DEL_FLAG_NO));
            stockMain.setAvailableCount(stockMain.getAvailableCount().longValue() + detail.getPredictReceiveCount().longValue());
            stockMainService.updateById(stockMain);
        });

        return AjaxResult.success();
    }



    /**
     * 移库任务作废
     * @param taskNo
     * @return
     */
    @Transactional
    @Override
    public AjaxResult moveCancellation(String taskNo) {

        if (StringUtils.isEmpty(taskNo)){
            return AjaxResult.error("参数不可为空");
        }

        // 查询任务信息
        TTaskWcs tTaskWcs = tTaskWcsMapper.selectOne(new QueryWrapper<TTaskWcs>()
                .eq("task_no",taskNo).eq("del_flag",Constants.DEL_FLAG_NO));
        if (tTaskWcs == null){
            return AjaxResult.error("任务不存在");
        }

        // 已完成、已废弃的单据不可进行操作
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())
                || Constants.WCS_EXECUTE_STATUS_CANCELLATION.equals(tTaskWcs.getTaskStatus())){
            return AjaxResult.error("任务当前状态不可执行作废操作");
        }

        // 查询wcs任务详情
        List<TTaskWcsDetail> taskWcsDetail = tTaskWcsDetailMapper.selectList(
                new QueryWrapper<TTaskWcsDetail>()
                        .eq("task_id", tTaskWcs.getId())
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        List<Long> originId = taskWcsDetail.stream().map(TTaskWcsDetail::getOriginId).collect(Collectors.toList());

        // 查询 task_detail
        List<TTaskDetail> taskDetail = tTaskDetailMapper.selectList(
                new QueryWrapper<TTaskDetail>()
                        .in("id", originId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        // 设置为作废状态
        List<Long> taskDetailId = taskDetail.stream().map(TTaskDetail::getId).collect(Collectors.toList());
        tTaskDetailMapper.update(new TTaskDetail(),
                new UpdateWrapper<TTaskDetail>()
                        .set("status",Constants.TASK_DETAIL_STATUS_CANCELLATION)
                        .in("id",taskDetailId));

        // 查询task
        TTask tTask = tTaskMapper.selectById(taskDetail.get(0).getTaskId());
        // 设置为作废状态
        tTask.setStatus(Constants.TASK_STATUS_CANCELLATION);
        tTaskMapper.updateById(tTask);

        // 查询移库表
        TMoveLibrary tMoveLibrary = itMoveLibraryService.getOne(
                new QueryWrapper<TMoveLibrary>()
                        .eq("code", tTask.getOriginCode())
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        // 设置为作废状态  1已作废
        tMoveLibrary.setStatus("1");
        itMoveLibraryService.updateById(tMoveLibrary);

        // 更新WCS任务状态为 已作废
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        tTaskWcsMapper.updateById(tTaskWcs);

        // 释放被冻结的库存数据
        List<TMoveLibraryDetail> libraryDetailList = itMoveLibraryDetailService.list(new QueryWrapper<TMoveLibraryDetail>()
                .eq("move_library_code", tMoveLibrary.getCode())
                .eq("del_flag", Constants.DEL_FLAG_NO));
        List<Long> stockId = libraryDetailList.stream().map(TMoveLibraryDetail::getStockId).collect(Collectors.toList());
        itStockService.update(new TStock(),
                new UpdateWrapper<TStock>()
                        .in("id",stockId)
                        .set("is_freeze",Constants.STOCK_IS_FREEZE_NO));

        return AjaxResult.success();
    }

    /**
     * 根据任务主表id获取子表列表
     * @param taskWcsId
     * @param taskTypePut
     * @return
     */
    @Override
    public List<TTaskWcsDetailVO> getListByTaskId(Long taskWcsId, String taskTypePut) {
        return tTaskWcsDetailMapper.getListByTaskId(taskWcsId,taskTypePut);
    }

}
