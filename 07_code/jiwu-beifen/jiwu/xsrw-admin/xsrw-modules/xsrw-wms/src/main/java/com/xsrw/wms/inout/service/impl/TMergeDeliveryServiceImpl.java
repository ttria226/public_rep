package com.xsrw.wms.inout.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.TUnitConfig;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.base.service.ITUnitConfigService;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.inout.mapper.TOutDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TOutDeliveryMapper;
import com.xsrw.wms.inout.mapper.TTaskMergeMapper;
import com.xsrw.wms.inout.service.*;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TMergeDeliveryMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * 波次计划Service业务层处理
 *
 * @author zjj
 * @date 2023-06-25
 */
@Service
public class TMergeDeliveryServiceImpl extends ServiceImpl<TMergeDeliveryMapper, TMergeDelivery> implements ITMergeDeliveryService
{
    @Autowired
    private TMergeDeliveryMapper tMergeDeliveryMapper;

    @Autowired
    private TOutDeliveryMapper tOutDeliveryMapper;

    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITMergeDeliveryDetailService mergeDeliveryDetailService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private ITStockService stockService;

    @Autowired
    ITLocationService locationService;

    @Autowired
    private ITTaskWcsService taskWcsService;

    @Autowired
    private ITTaskWcsDetailService taskWcsDetailService;

    @Autowired
    private TTaskMergeMapper tTaskMergeMapper;

    @Autowired
    private ITUnitConfigService unitConfigService;

    @Autowired
    ITMaterialDetailService materialDetailService;

    @Autowired
    private ITStockMainService stockMainService;

    @Autowired
    private ITStockDetailService stockDetailService;


    /**
     * 查询波次计划列表
     *
     * @param tMergeDelivery 波次计划
     * @return 波次计划
     */
    @Override
    public List<TMergeDelivery> selectTMergeDeliveryList(TMergeDelivery tMergeDelivery)
    {
        return tMergeDeliveryMapper.selectTMergeDeliveryList(tMergeDelivery);
    }

    /**
     * 查询波次计划
     *
     * @param id 波次计划主键
     * @return 波次计划
     */
    @Override
    public TMergeDeliveryVO selectTMergeDeliveryById(Long id)
    {
        TMergeDeliveryVO vo = new TMergeDeliveryVO();

        TMergeDelivery mergeDelivery = tMergeDeliveryMapper.selectById(id);
        vo.setCode(mergeDelivery.getCode());
        vo.setStatus(mergeDelivery.getStatus());
        vo.setDeptName(mergeDelivery.getDeptName());
        vo.setCreateBy(mergeDelivery.getCreateBy());
        vo.setCreateTime(mergeDelivery.getCreateTime());

        // 查询详情
        TMergeDeliveryDetail detail = new TMergeDeliveryDetail();
        detail.setMergeDeliveryId(id);
        List<TMergeDeliveryDetailVO> detailVOS = mergeDeliveryDetailService.selectTMergeDeliveryDetailList(detail);
        vo.setDetail(detailVOS);

        return vo;
    }

    /**
     * 新增波次计划
     *
     * @param tMergeDelivery 波次计划
     * @return 结果
     */
    @Override
    public int insertTMergeDelivery(TMergeDelivery tMergeDelivery)
    {
        return tMergeDeliveryMapper.insert(tMergeDelivery);
    }

    /**
     * 修改波次计划
     *
     * @param tMergeDelivery 波次计划
     * @return 结果
     */
    @Override
    public int updateTMergeDelivery(TMergeDelivery tMergeDelivery)
    {
        return tMergeDeliveryMapper.updateById(tMergeDelivery);
    }


    /**
     * 批量删除波次计划
     *
     * @param ids 需要删除的波次计划主键
     * @return 结果
     */
    @Override
    public int deleteTMergeDeliveryByIds(Long[] ids)
    {
        return tMergeDeliveryMapper.deleteTMergeDeliveryByIds(ids);
    }

    /**
     * 删除波次计划信息
     *
     * @param id 波次计划主键
     * @return 结果
     */
    @Override
    public int deleteTMergeDeliveryById(Long id)
    {
        return tMergeDeliveryMapper.deleteTMergeDeliveryById(id);
    }


    /**
     * 创建波次
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public AjaxResult createMergeDelivery(Long[] ids) {

        if (ids == null || ids.length == 0){
            return AjaxResult.error("参数不可为空");
        }
        if (ids.length == 1){
            return AjaxResult.error("请选择两条以上的出库计划数据");
        }

        // 查询出库计划数据
        List<TOutDelivery> outDeliveryList = tOutDeliveryMapper.selectList(
                new QueryWrapper<TOutDelivery>()
                        .eq("del_flag", Constants.DEL_FLAG_NO)
                        // 单据状态  2、审核通过
                        .eq("status", 2)
                        // 完成状态  1、未完成
                        .eq("complete_state", 1)
                        .in("id",ids));
        if (outDeliveryList.size() != ids.length){
            return AjaxResult.error("选择的出库计划数据需审核通过且未完成");
        }

        // 查询出库单详情
        List<TOutDeliveryDetail> outDeliveryDetails = tOutDeliveryDetailMapper.selectList(
                new QueryWrapper<TOutDeliveryDetail>()
                        .eq("del_flag", Constants.DEL_FLAG_NO)
                        .in("out_delivery_id", ids));

        if (outDeliveryDetails.size() > 0){
            // 合并物料数量
            Map<Long, Long> materialMap = outDeliveryDetails.stream().collect(
                    Collectors.groupingBy(TOutDeliveryDetail::getMaterialId, Collectors.summingLong(TOutDeliveryDetail::getPredictReceiveCount)));

            // 获取波次编码
            String code = codeConfigService.getCode(CodeEnum.MBC.getCodeName());

            List<String> collect = outDeliveryList.stream().map(TOutDelivery::getCode).collect(Collectors.toList());
            // 创建波次计划、波次计划详情
            TMergeDelivery mergeDelivery = new TMergeDelivery();
            mergeDelivery.setCode(code);
            mergeDelivery.setStatus("1");
            // 出库计划单号
            mergeDelivery.setOutDeliveryCode(String.join(",",collect));
            tMergeDeliveryMapper.insert(mergeDelivery);

            List<TMergeDeliveryDetail> detailList = new ArrayList<>();

            List<Long> materialId = outDeliveryDetails.stream().map(TOutDeliveryDetail::getMaterialId).collect(Collectors.toList());
            materialId.forEach(e -> {
                TMergeDeliveryDetail detail = new TMergeDeliveryDetail();
                detail.setMergeDeliveryId(mergeDelivery.getId());
                detail.setMaterialId(e);
                detail.setPredictReceiveCount(materialMap.get(e));
                detail.setNextFlag("0");

                detailList.add(detail);
            });
            mergeDeliveryDetailService.saveBatch(detailList);

            // 删除出库计划
            tOutDeliveryMapper.update(new TOutDelivery(),
                    new UpdateWrapper<TOutDelivery>().in("id",ids).set("del_flag",Constants.DEL_FLAG_YES));
            // 删除出库计划详情
            tOutDeliveryDetailMapper.update(new TOutDeliveryDetail(),
                    new UpdateWrapper<TOutDeliveryDetail>().in("out_delivery_id",ids).set("del_flag",Constants.DEL_FLAG_YES));
        }else {
            return AjaxResult.error("出库计划详情无数据");
        }

        return AjaxResult.success();
    }


    /**
     * 执行出库 生成任务及WCS相关
     * @param tTaskOutVO
     * @return
     */
    @Override
    public AjaxResult insertTTaskOut(TTaskOutVO tTaskOutVO) {

        //判断任务是否已执行，不可多次执行
        TMergeDeliveryDetail deliveryDetail = mergeDeliveryDetailService.getOne(new LambdaQueryWrapper<TMergeDeliveryDetail>()
                .eq(TMergeDeliveryDetail::getDelFlag,Constants.DEL_FLAG_NO)
                .eq(TMergeDeliveryDetail::getId,tTaskOutVO.getOutDeliveryDetailId()));
        if(deliveryDetail == null ){
            return AjaxResult.error("未找到该任务");
        }
        if(deliveryDetail.getNextFlag().equals(Constants.INOUT_NEXT_FLAG_YES) ){
            return AjaxResult.error("已执行出库的任务不可再次执行");
        }
        List<TTaskOutDetailListVO> tTaskOutDetailListVOS = tTaskOutVO.gettTaskOutDetailListVOS();
        //判断数量和单据的预计数量是否一致
        Long receiveCount = deliveryDetail.getPredictReceiveCount();
        long sum = tTaskOutDetailListVOS.stream().filter(e->e.getPredictCount()!=null).mapToLong(TTaskOutDetailListVO::getPredictCount).sum();
        if(sum != deliveryDetail.getPredictReceiveCount()){
            return AjaxResult.error("拣货数量和单据预计拣货数量不一致！");
        }
        List<TTaskOutDetailListVO> collect = tTaskOutDetailListVOS.stream().distinct().collect(Collectors.toList());
        //判断所选载具的库存是否
        for (TTaskOutDetailListVO tTaskOutDetailListVO : collect) {
            TLocation location = locationService.getById(tTaskOutDetailListVO.getLocationId());
            if(location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)){
                return AjaxResult.error("请选择有效库位");
            }
            TTray tTray = trayService.getById(tTaskOutDetailListVO.getTrayId());
            if(tTray == null){
                return AjaxResult.error("请选择有效载具");
            }
            TStock tStock = stockService.getById(tTaskOutDetailListVO.getStockId());
            if(tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)){
                return AjaxResult.error("请选择有效库存");
            }
//            //更新该物料的在库可用数量
//            if(deliveryDetail.getSmallPredictCount() == null){//不是小件领取
//                tStock.setAvailableCount(tStock.getAvailableCount()-tTaskOutDetailListVO.getPredictCount());
//                stockService.updateTStock(tStock);
//            }
            TTaskMerge tTaskMerge = new TTaskMerge();
            tTaskMerge.setMergeDeliveryId(deliveryDetail.getMergeDeliveryId());
            tTaskMerge.setMaterialId(deliveryDetail.getMaterialId());
            tTaskMerge.setPredictCount(tTaskOutDetailListVO.getPredictCount());
            tTaskMerge.setStockId(tTaskOutDetailListVO.getStockId());
            tTaskMerge.setLocationId(tTaskOutDetailListVO.getLocationId());
            tTaskMerge.setTrayId(tTaskOutDetailListVO.getTrayId());
            tTaskMerge.setMergeDeliveryDetailId(deliveryDetail.getId());


            deliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
            mergeDeliveryDetailService.updateById(deliveryDetail);
            //将库位状态标记为已出库
            location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(location);

            //生成t_task_wcs
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setLocationId(tTaskMerge.getLocationId());
            tTaskWcs.setTrayId(tTaskMerge.getTrayId());
            tTaskWcs.setTrayCode(tTray.getCode());
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_MERGE);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            taskWcsService.save(tTaskWcs);

            tTaskMerge.setWcsId(tTaskWcs.getId());
            tTaskMergeMapper.insert(tTaskMerge);

            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
            tTaskWcsDetail.setTaskId(tTaskWcs.getId());
            tTaskWcsDetail.setOriginId(tTaskMerge.getId());
            tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_OUT);
            taskWcsDetailService.save(tTaskWcsDetail);

        }
        return AjaxResult.success();
    }


    /**
     * 地堆出库
     * @param tTaskOutVO
     * @return
     */
    @Override
    public AjaxResult addTaskPile(TTaskOutVO tTaskOutVO) {

        if(tTaskOutVO.getOutDeliveryDetailId() == null
                ||(tTaskOutVO.gettTaskOutDetailListVOS()==null||tTaskOutVO.gettTaskOutDetailListVOS().size()<=0)){
            return  AjaxResult.error("参数错误！");
        }
        TMergeDeliveryDetail tMergeDeliveryDetail=mergeDeliveryDetailService.getById(tTaskOutVO.getOutDeliveryDetailId());
        if(tMergeDeliveryDetail==null){
            return  AjaxResult.error("波次单不存在！");
        }
        //已拣货数量
        Long outboundCount = tTaskMergeMapper.selectList(Wrappers.lambdaQuery(TTaskMerge.class)
                .eq(TTaskMerge::getMergeDeliveryDetailId,tTaskOutVO.getOutDeliveryDetailId())
                .eq(TTaskMerge::getDelFlag,Constants.DEL_FLAG_NO)
        ).stream().mapToLong(taskMerge -> taskMerge.getActualCount()).sum();
        List<TTaskMerge> tTaskMergeList=new ArrayList<>();
        for (TTaskOutDetailListVO task:tTaskOutVO.gettTaskOutDetailListVOS()) {
            TStock tStock = stockService.getById(task.getStockId());
            if(tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)){
                return AjaxResult.error("请选择有效库存");
            }
            TLocation location = locationService.getById(tStock.getLocationId());
            if(location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)){
                return AjaxResult.error("请选择有效库位");
            }
//            TTray tTray = trayService.getById(tStock.getTrayId());
//            if(tTray == null){
//                return AjaxResult.error("请选择有效载具");
//            }
            if(task.getReceiveCount()==null||task.getReceiveCount()<=0){
                return  AjaxResult.error("实际拣货数量不可以为空！");
            }
            //添加出库记录
            TTaskMerge taskMerge = new TTaskMerge();
            taskMerge.setMergeDeliveryId(tMergeDeliveryDetail.getMergeDeliveryId());
            taskMerge.setMergeDeliveryDetailId(tMergeDeliveryDetail.getId());
            taskMerge.setLocationId(location.getId());
            taskMerge.setStockId(task.getStockId());
            taskMerge.setMaterialId(tStock.getMaterialId());
            taskMerge.setActualCount(task.getReceiveCount());
            taskMerge.setPredictCount(tMergeDeliveryDetail.getPredictReceiveCount());
            taskMerge.setStatus("2");
            tTaskMergeList.add(taskMerge);

            outboundCount=outboundCount+task.getReceiveCount();
        }
        if(outboundCount.compareTo(tMergeDeliveryDetail.getPredictReceiveCount())>0){
            return  AjaxResult.error("实际拣货数量不可超过预计拣货数量！");
        }

        for ( TTaskMerge t: tTaskMergeList) {
            //更改库存
            TStockMain tStockMain=stockMainService.getOne(Wrappers.lambdaQuery(TStockMain.class)
                    .eq(TStockMain::getMaterialId,t.getMaterialId())
                    .eq(TStockMain::getDelFlag,Constants.NO)
            );
            if(tStockMain!=null){
//                tStockMain.setAvailableCount(tStockMain.getAvailableCount()-t.getActualCount());
                tStockMain.setLibraryCount(tStockMain.getLibraryCount()-t.getActualCount());
                if(tStockMain.getLibraryCount()<0){
                    return  AjaxResult.error("系统错误，库存不足！");
                }
                stockMainService.updateById(tStockMain);
            }
            tTaskMergeMapper.insert(t);
        }
        //更改出库单状态
        if(outboundCount.equals(tMergeDeliveryDetail.getPredictReceiveCount())){
            tMergeDeliveryDetail.setNextFlag("1");
        }else{
            tMergeDeliveryDetail.setNextFlag("2");
        }

        mergeDeliveryDetailService.updateById(tMergeDeliveryDetail);
        return  AjaxResult.success();
    }


    /**
     * 强制执行出库任务
     * @param tTaskWcsOutVO
     * @return
     */
    @Override
    public AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcsOutVO) {
        TTaskWcs tTaskWcs = taskWcsService.getById(tTaskWcsOutVO.getId());
        if(tTaskWcs == null || !Constants.WCS_TASK_TYPE_MERGE.equals(tTaskWcs.getTaskType())){
            return AjaxResult.error("请选择要强制执行的出库任务");
        }
        if(Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())){
            return AjaxResult.error("当前状态不可强制执行");
        }
        if(tTaskWcsOutVO.getReceiveCount() == null && tTaskWcsOutVO.getSmallReceiveCount() == null){
            return AjaxResult.error("实际拣货数量不可为空");
        }
        TTaskWcsDetail tTaskWcsDetailVOS = taskWcsDetailService.getOne(new LambdaQueryWrapper<TTaskWcsDetail>().eq(TTaskWcsDetail::getTaskId, tTaskWcs.getId()));
        //List<TTaskWcsDetailVO> tTaskWcsDetailVOS = taskWcsDetailService.selectStatusWcsListByTrayId(tTaskWcs.getTrayId(), tTaskWcs.getTaskType());
        if (tTaskWcsDetailVOS == null) {
            return AjaxResult.error("未查询到可执行数据");
        }
        //更新原单任务状态 t_task_merge
        TTaskMerge taskMerge = tTaskMergeMapper.selectById(tTaskWcsDetailVOS.getOriginId());
        if(!taskMerge.getWcsId().equals(tTaskWcs.getId())){
            return AjaxResult.error("任务不匹配");
        }
        //更新实际拣货数量
        TMergeDeliveryDetail mergeDeliveryDetail = mergeDeliveryDetailService.getById(taskMerge.getMergeDeliveryDetailId());

        //操作t_stock
        TStock stock = stockService.getById(taskMerge.getStockId());
        TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, stock.getMaterialId()).eq(TUnitConfig::getDelFlag, Constants.NO));
        if(unitConfig == null && tTaskWcsOutVO.getSmallReceiveCount() != null){
            return AjaxResult.error("该物料不可小件领取");
        }
        if(tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getRfid() == null){
            return AjaxResult.error("请输入小件领取的物料rfid");
        }
        if(tTaskWcsOutVO.getSmallReceiveCount() != null && tTaskWcsOutVO.getSmallReceiveCount() >= unitConfig.getCount()){
            return AjaxResult.error("小件领取的数据大于物品包装最大数");
        }
        if(tTaskWcsOutVO.getSmallReceiveCount() != null){
            TMaterialDetailSerachDTO materialDetail = new TMaterialDetailSerachDTO();
            materialDetail.setRfid(tTaskWcsOutVO.getRfid());
            List<TMaterialDetailVO> tMaterialDetailVOS = materialDetailService.selectTMaterialDetailList(materialDetail);
            Long samllCountSum = tMaterialDetailVOS.stream().mapToLong(TMaterialDetailVO::getUseCount).sum();
            Long currentsamllCountSum = tTaskWcsOutVO.getSmallReceiveCount()+samllCountSum;
            if(currentsamllCountSum > unitConfig.getCount()){
                return AjaxResult.error("该物料小件领取已超出");
            }
        }

        if(tTaskWcsOutVO.getReceiveCount()==null){
            tTaskWcsOutVO.setReceiveCount(0l);
        }
        Long stockOut =  tTaskWcsOutVO.getSmallReceiveCount()== null ? tTaskWcsOutVO.getReceiveCount():tTaskWcsOutVO.getReceiveCount()+1;
        if(stock.getCount()<stockOut){
            return AjaxResult.error("所选载具库存不足，无法出库");
        }

        //更新任务状态
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        taskWcsService.updateById(tTaskWcs);


        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setBeforeCount(stock.getAvailableCount());
        if(tTaskWcsOutVO.getReceiveCount() !=null) {
            mergeDeliveryDetail.setReceiveCount(tTaskWcsOutVO.getReceiveCount());
            //减去载具库存
            stock.setAvailableCount(stock.getAvailableCount() - tTaskWcsOutVO.getReceiveCount());
            stock.setCount(stock.getCount() - tTaskWcsOutVO.getReceiveCount());
            if(stock.getCount()<0 || stock.getAvailableCount()<0){
                return  AjaxResult.error("系统错误，库存不足！");
            }
            if(tTaskWcsOutVO.getSmallReceiveCount() != null){
                stock.setAvailableCount(stock.getAvailableCount() +1);
                stock.setCount(stock.getCount() + 1);
            }
            //更新载具、库位
            TTray tTray = trayService.getById(stock.getTrayId());
            TLocation tLocation = locationService.getById(stock.getLocationId());


            //判断现有库存是否为0
            //判断剩余库存是否为0
            if(stock.getCount() == 0 && stock.getAvailableCount() == 0){
                //该条库存标记为删除状态
                stock.setDelFlag(Constants.DEL_FLAG_YES);
                //更新载具信息
                tTray.setLocationId(null);
                tLocation.setPalletNum(null);
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            }else {
                tTray.setStatus(Constants.TRAY_STATUS_HALF);
                tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            }
            trayService.updateById(tTray);
            locationService.updateById(tLocation);
            //增加库存使用记录
            TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
            if(tTaskWcsOutVO.getSmallReceiveCount() != null){
                stockMain.setAvailableCount(stockMain.getAvailableCount()+1);
            }
            stockMain.setLibraryCount(stockMain.getLibraryCount()-tTaskWcsOutVO.getReceiveCount());
            if(stockMain.getLibraryCount()<0){
                return  AjaxResult.error("系统错误，库存不足！");
            }
            if(tTaskWcsOutVO.getSmallReceiveCount() != null){
                stockMain.setLibraryCount(stockMain.getLibraryCount() + 1);
            }
            stockMainService.updateById(stockMain);
        }
        //托盘信息是否需要更新为半托

        //使用记录
        tStockDetail.setLocationId(stock.getLocationId());
        tStockDetail.setMaterialId(stock.getMaterialId());
        tStockDetail.setCurrentCount(stock.getAvailableCount());
        tStockDetail.setType("2");
        tStockDetail.setOriginId(taskMerge.getId());
        tStockDetail.setBatchCode(stock.getBatchCode());
        stockDetailService.save(tStockDetail);

        stockService.updateById(stock);


        long l1 = (tTaskWcsOutVO.getReceiveCount() != null) ? (tTaskWcsOutVO.getSmallReceiveCount() == null ? tTaskWcsOutVO.getReceiveCount() : (tTaskWcsOutVO.getReceiveCount() + 1)) : (tTaskWcsOutVO.getSmallReceiveCount() != null ? 1 : 0);
        taskMerge.setActualCount(l1);
        taskMerge.setStatus(Constants.TASK_STATUS_END);
        tTaskMergeMapper.updateById(taskMerge);

        mergeDeliveryDetailService.updateById(mergeDeliveryDetail);

        //更新出库计划的出库状态
        LambdaQueryWrapper<TTaskMerge> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TTaskMerge::getMergeDeliveryId,taskMerge.getMergeDeliveryId());
        queryWrapper.eq(TTaskMerge::getDelFlag,Constants.DEL_FLAG_NO);
        List<TTaskMerge> tTaskMergeList = tTaskMergeMapper.selectList(queryWrapper);
        boolean flag = true;
        for (TTaskMerge tTaskMerge : tTaskMergeList) {
            if(tTaskMerge.getStatus().equals(Constants.TASK_STATUS_NO)){
                flag = false;
                break;
            }
            if(tTaskMerge.getActualCount() < tTaskMerge.getPredictCount()){
                flag = false;
                break;
            }
        }
        TMergeDelivery delivery = this.getById(taskMerge.getMergeDeliveryId());
        //判断是否部分出库  true全部出库    FALSE部分出库
        delivery.setStatus(flag?"3":"2");
        this.updateById(delivery);
        //如果在库数量不足生成补货记录

        return AjaxResult.success();
    }


    /**
     * 波次分拨
     * @param id
     * @return
     */
    @Transactional
    @Override
    public AjaxResult allocate(Long id) {

        TMergeDelivery byId = this.getById(id);
        if (byId == null){
            return AjaxResult.error("单据不存在");
        }

        String[] split = byId.getOutDeliveryCode().split(",");
        // 恢复出库计划数据、更新状态为已完成
        tOutDeliveryMapper.update(new TOutDelivery(),
                new UpdateWrapper<TOutDelivery>()
                        .in("code",split)
                        .set("complete_state",Constants.INOUT_STATUS_END)
                        .set("del_flag",Constants.DEL_FLAG_NO));

        List<TOutDelivery> deliveryList = tOutDeliveryMapper.selectList(new QueryWrapper<TOutDelivery>().in("code", split));
        List<Long> collect = deliveryList.stream().map(TOutDelivery::getId).collect(Collectors.toList());

        tOutDeliveryDetailMapper.update(new TOutDeliveryDetail(),
                new UpdateWrapper<TOutDeliveryDetail>()
                        .in("out_delivery_id",collect)
                        .set("next_flag","1")
                        .set("del_flag",Constants.DEL_FLAG_NO));

        // 波次单已分拨
        byId.setAllocateFlag("1");

        this.updateById(byId);
        return AjaxResult.success();
    }


    @Override
    public Map<String, Object> getOutDeliveryCount(String id) {
        Map<String,Object> map=new HashMap<>();
        Long predictReceiveCount=Long.parseLong("0");
        TMergeDeliveryDetail mergeDeliveryDetail = mergeDeliveryDetailService.getById(id);
        if(mergeDeliveryDetail!=null){
            predictReceiveCount= mergeDeliveryDetail.getPredictReceiveCount();
        }
        map.put("predictReceiveCount",predictReceiveCount);
        Long OutboundCount=tTaskMergeMapper.selectList(Wrappers.lambdaQuery(TTaskMerge.class)
                .eq(TTaskMerge::getMergeDeliveryDetailId,id)
                .eq(TTaskMerge::getDelFlag,Constants.DEL_FLAG_NO)
        ).stream().mapToLong(taskMerge -> taskMerge.getActualCount()==null?0:taskMerge.getActualCount()).sum();
        map.put("totalCount",OutboundCount);
        return  map;
    }

}
