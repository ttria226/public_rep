package com.xsrw.wms.inout.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDetailDTO;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsDetailMapper;
import com.xsrw.wms.inout.service.ITTaskInService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import com.xsrw.wms.web.domain.dto.WcsOrderDTO;
import com.xsrw.wms.web.util.AgvReportUtil;
import com.xsrw.wms.web.util.WcsReportUtil;
import com.xsrw.wms.webservice.util.WmsToErpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * wcs任务Service业务层处理
 *
 * @author wxr
 * @date 2023-05-10
 */
@Service
public class TTaskWcsServiceImpl extends ServiceImpl<TTaskWcsMapper, TTaskWcs> implements ITTaskWcsService {
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;

    @Autowired
    private TTaskWcsDetailMapper tTaskWcsDetailMapper;

    @Autowired
    private ITTaskInService taskInService;
    @Autowired
    private ITStockService stockService;
    @Autowired
    private ITStockDetailService stockDetailService;
    @Autowired
    private ITStockMainService stockMainService;
    @Autowired
    private ITLocationService locationService;
    @Autowired
    private TTrayMapper tTrayMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Autowired
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private WcsReportUtil wcsReportUtil;
    @Autowired
    private AgvReportUtil agvReportUtil;
    @Autowired
    private WmsToErpUtils wmsToErpUtils;

    /**
     * 查询wcs任务列表
     *
     * @param tTaskWcs wcs任务
     * @return wcs任务
     */
    @Override
    public List<TTaskWcsVO> selectTTaskWcsList(TTaskWcs tTaskWcs) {
        return tTaskWcsMapper.selectTTaskWcsList(tTaskWcs);
    }

    /**
     * 查询wcs任务
     *
     * @param id wcs任务主键
     * @return wcs任务
     */
    @Override
    public TTaskWcsVO selectTTaskWcsById(Long id) {
        TTaskWcsVO taskWcsVO = new TTaskWcsVO();
        TTaskWcs tTaskWcs = tTaskWcsMapper.selectById(id);
        BeanUtils.copyBeanProp(taskWcsVO, tTaskWcs);
        taskWcsVO.setTaskWcsDetailVOList(tTaskWcsDetailMapper.getListByTaskId(id, tTaskWcs.getTaskType()));
        return taskWcsVO;
    }

    /**
     * 新增wcs任务
     *
     * @param tTaskWcs wcs任务
     * @return 结果
     */
    @Override
    public int insertTTaskWcs(TTaskWcs tTaskWcs) {
        return tTaskWcsMapper.insert(tTaskWcs);
    }

    /**
     * 修改wcs任务
     *
     * @param tTaskWcs wcs任务
     * @return 结果
     */
    @Override
    public int updateTTaskWcs(TTaskWcs tTaskWcs) {
        return tTaskWcsMapper.updateById(tTaskWcs);
    }

    @Override
    public AjaxResult executeOut(TTaskWcs tTaskWcs) {
        TTaskWcs taskWcs = tTaskWcsMapper.selectById(tTaskWcs.getId());
        if (taskWcs == null || !taskWcs.getTaskType().equals(Constants.WCS_TASK_TYPE_OUT)) {
            return AjaxResult.error("请选择要执行出库的任务");
        }
        //状态更新为执行中
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_ING);
        taskWcs.setSendData(taskWcs.toString());
        //调接口
        this.updateById(taskWcs);
        return AjaxResult.success();
    }


    /**
     * 批量删除wcs任务
     *
     * @param ids 需要删除的wcs任务主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsByIds(Long[] ids) {
        return tTaskWcsMapper.deleteTTaskWcsByIds(ids);
    }

    /**
     * 删除wcs任务信息
     *
     * @param id wcs任务主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsById(Long id) {
        return tTaskWcsMapper.deleteTTaskWcsById(id);
    }

    /**
     * 执行
     *
     * @param tTaskWcs
     * @return
     */
    @Override
    @Transactional
    public AjaxResult executeTask(TTaskWcs tTaskWcs) {
        TTaskWcs tTaskWcsVO = tTaskWcsMapper.selectById(tTaskWcs.getId());
        if (tTaskWcsVO == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcsVO.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        Long locationId = tTaskWcs.getLocationId() == null ? tTaskWcsVO.getLocationId() : tTaskWcs.getLocationId();
        if (locationId == null) {
            return AjaxResult.error("请先选取指定库位");
        }

        List<TTaskWcsDetailVO> tTaskWcsDetailVOS = tTaskWcsMapper.selectStatusWcsListByTrayId(tTaskWcsVO.getTrayId(), tTaskWcsVO.getTaskType());
        if (CollectionUtils.isEmpty(tTaskWcsDetailVOS)) {
            return AjaxResult.error("未查询到可执行数据");
        }
        List<Long> taskIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getTaskId).distinct().collect(Collectors.toList());
        List<Long> originIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getOriginId).distinct().collect(Collectors.toList());
        //更新任务状态
        tTaskWcs.setLocationId(locationId);
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateStatusByIds(taskIds, tTaskWcs);
        if (Constants.WCS_TASK_TYPE_IN.equals(tTaskWcsVO.getTaskType())) {
            List<TTaskInVO> tTaskInList = taskInService.selectTTaskInInfoByIds(originIds);
            //入库
            taskInService.executeEndTask(tTaskInList);
            //添加进库存
            this.inStock(tTaskWcs, tTaskInList);
            //更新载具状态
            TTray tTray = new TTray();
            tTray.setId(tTaskWcsVO.getTrayId());
            tTray.setLocationId(locationId);
            tTrayMapper.updateById(tTray);
            //更新库位状态
            TLocation tLocation = new TLocation();
            tLocation.setId(locationId);
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            tLocation.setPalletNum(String.valueOf(tTaskWcsVO.getTrayCode()));
            locationService.updateById(tLocation);
        } else {

        }
        //更新登记
        return AjaxResult.success();
    }

    @Override
    @Transactional
    public AjaxResult executeTaskNew(TTaskWcsDTO tTaskWcs) {
        TTaskWcs tTaskWcsVO = tTaskWcsMapper.selectById(tTaskWcs.getId());
        if (tTaskWcsVO == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcsVO.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        //查询对应的移库任务是否完成
        Long moveTaskCount = this.getMoveCountByMainNo(tTaskWcsVO.getMainTaskNo());
        if(moveTaskCount > 0){
            return AjaxResult.error("请先完成移库任务");
        }
//        Long locationId = tTaskWcs.getLocationId() == null ? tTaskWcsVO.getLocationId() : tTaskWcs.getLocationId();
//        if (locationId == null) {
//            return AjaxResult.error("请先选取指定库位");
//        }
        Long locationId = tTaskWcsVO.getLocationId();

        List<TTaskWcsDetailVO> tTaskWcsDetailVOS = tTaskWcsMapper.selectStatusWcsListByTrayId(tTaskWcsVO.getTrayId(), tTaskWcsVO.getTaskType());
        if (CollectionUtils.isEmpty(tTaskWcsDetailVOS)) {
            return AjaxResult.error("未查询到可执行数据");
        }
        List<Long> taskIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getTaskId).distinct().collect(Collectors.toList());
        List<Long> originIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getOriginId).distinct().collect(Collectors.toList());
        //更新任务状态
        tTaskWcs.setLocationId(locationId);
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateStatusByIds(taskIds, tTaskWcs);
        if (Constants.WCS_TASK_TYPE_IN.equals(tTaskWcsVO.getTaskType())) {
            List<TTaskInVO> tTaskInList = taskInService.selectTTaskInInfoByIds(originIds);
            if (CollectionUtils.isEmpty(tTaskInList)) {
                throw new ServiceException("更新失败！");
            }
            //入库
            taskInService.executeEndTask(tTaskInList);
            //添加进库存
            tTaskWcs.setCreateBy(tTaskWcsVO.getCreateBy());
            tTaskWcs.setDeptId(tTaskWcsVO.getDeptId());
            tTaskWcs.setDeptName(tTaskWcsVO.getDeptName());
            this.inStock(tTaskWcs, tTaskInList);
            //更新物料详情的库位信息
            if (CollectionUtils.isNotEmpty(tTaskWcs.getTaskWcsDetailVOList())) {
                this.updateMetailDetail(locationId, tTaskWcsVO.getTrayId(), tTaskWcs.getTaskWcsDetailVOList());
            } else {
                List<Long> advanceDetailIds = tTaskInList.stream().map(TTaskInVO::getAdvanceRegistrationId).distinct().collect(Collectors.toList());
                tMaterialDetailMapper.updateStatusByParam(Constants.MATERIAL_DETAIL_STATUS_IN, advanceDetailIds, tTaskWcsVO.getTrayId(), locationId);
            }
            //更新载具状态
            TTray tTray = new TTray();
            tTray.setId(tTaskWcsVO.getTrayId());
            tTray.setLocationId(locationId);
            tTrayMapper.updateById(tTray);
            //更新库位状态
            TLocation tLocation = new TLocation();
            tLocation.setId(locationId);
            tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            tLocation.setPalletNum(String.valueOf(tTaskWcsVO.getTrayCode()));
            locationService.updateById(tLocation);
            //TODO WMS-ERP对接出入库
            wmsToErpUtils.inoutPut(Constants.WCS_TASK_TYPE_IN, tTaskWcs.getId(), null, null);
        } else {

        }
        //更新登记
        return AjaxResult.success();
    }

    /**
     * 更新物料详情的库位信息
     *
     * @param locationId
     * @param trayId
     * @param taskWcsDetailVOList
     */
    private void updateMetailDetail(Long locationId, Long trayId, List<TTaskWcsDetailDTO> taskWcsDetailVOList) {
        if (CollectionUtils.isNotEmpty(taskWcsDetailVOList)) {
            for (TTaskWcsDetailDTO tTaskWcsDetailDTO : taskWcsDetailVOList) {
                TAdvanceDeliveryDetailDTO detailDTO = new TAdvanceDeliveryDetailDTO();
                detailDTO.setStatus(Constants.MATERIAL_DETAIL_STATUS_IN);
                detailDTO.setLocationId(locationId);
                detailDTO.setId(tTaskWcsDetailDTO.getAdvanceRegistrationId());
                detailDTO.setRfIds(tTaskWcsDetailDTO.getRfIds());
                tMaterialDetailMapper.updateInfoByIdsOrRelId(detailDTO, trayId);
            }
        }
    }

    /**
     * 查询对应的移库任务是否完成
     * @param mainTaskNo
     * @return
     */
    public Long getMoveCountByMainNo(String mainTaskNo){
        LambdaQueryWrapper<TTaskWcs> queryWrapper = Wrappers.lambdaQuery(TTaskWcs.class)
                .eq(TTaskWcs::getDelFlag, Constants.NO)
                .eq(TTaskWcs::getMainTaskNo, mainTaskNo)
                .eq(TTaskWcs::getTaskType, Constants.TASK_TYPE_MOVE)
                .notIn(TTaskWcs::getTaskStatus, Constants.WCS_EXECUTE_STATUS_END, Constants.WCS_EXECUTE_STATUS_CANCELLATION);
        Long moveTaskCount = tTaskWcsMapper.selectCount(queryWrapper);
        return moveTaskCount;
    }

    /**
     * @description: 查询是否有入库或回库的未完成任务 taskType不等于入库1和回库4；且 taskStatus状态不等于3
     * @author shizhiqiang
     * @date: 2024/5/21 16:15
     * @return Long
     */
    @Override
    public Long countNotDone() {
        return tTaskWcsMapper.countNotDone();
    }

    @Override
    @Transactional
    public AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcsOutVO) {
        /*TTaskWcs tTaskWcs = tTaskWcsMapper.selectById(tTaskWcsOutVO.getId());
        if(tTaskWcs == null || !Constants.WCS_TASK_TYPE_OUT.equals(tTaskWcs.getTaskType())){
            return AjaxResult.error("请选择要强制执行的出库任务");
        }
        if(Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcs.getTaskStatus())){
            return AjaxResult.error("当前状态不可强制执行");
        }
        if(tTaskWcsOutVO.getReceiveCount() == null && tTaskWcsOutVO.getSmallReceiveCount() == null){
            return AjaxResult.error("实际拣货数量不可为空");
        }
        List<TTaskWcsDetailVO> tTaskWcsDetailVOS = tTaskWcsMapper.selectStatusWcsListByTrayId(tTaskWcs.getTrayId(), tTaskWcs.getTaskType());
        if (CollectionUtils.isEmpty(tTaskWcsDetailVOS)) {
            return AjaxResult.error("未查询到可执行数据");
        }
        //更新任务状态
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateById(tTaskWcs);
        //更新原单任务状态 t_task_out
        TTaskOut taskOut = taskOutService.getById(tTaskWcsDetailVOS.get(0).getOriginId());
        if(!taskOut.getWcsId().equals(tTaskWcs.getId())){
            return AjaxResult.error("任务不匹配");
        }
        taskOut.setStatus(Constants.TASK_STATUS_END);
        taskOutService.updateById(taskOut);

        //操作t_stock
        TStock stock = stockService.getById(taskOut.getStockId());
        if(stock.getAvailableCount()<1){
            return AjaxResult.error("所选载具库存不足");
        }
        if(tTaskWcsOutVO.getSmallReceiveCount() != null && stock.getAvailableCount()<tTaskWcsOutVO.getReceiveCount()+1){
            return AjaxResult.error("所选载具库存不足");
        }
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setBeforeCount(stock.getAvailableCount());
        if(tTaskWcsOutVO.getReceiveCount() == null){
            //不需要减载具的库存，释放该载具，新增小件出库记录，更新托盘id的状态
            stock.setStatus(Constants.STOCK_USE_YES);
            stockService.updateById(stock);
            //是否需要更新托盘为半托

        }else {
            //必然有载具出库，减去载具库存
            stock.setAvailableCount(stock.getAvailableCount()-tTaskWcsOutVO.getReceiveCount());
            stock.setCount(stock.getCount()-tTaskWcsOutVO.getReceiveCount());
            if(tTaskWcsOutVO.getSmallReceiveCount() != null){
                //有小件出库，库存必然不会为0,更新库存信息，新增库存使用记录
                stock.setStatus(Constants.STOCK_USE_YES);
                stockService.updateById(stock);
                TStockMain stockMain = stockMainService.getOne(new LambdaQueryWrapper<TStockMain>().eq(TStockMain::getMaterialId, stock.getMaterialId()).eq(TStockMain::getDelFlag, 0));
                stockMain.setAvailableCount(stockMain.getAvailableCount()-tTaskWcsOutVO.getReceiveCount());
                stockMain.setLibraryCount(stockMain.getLibraryCount()-tTaskWcsOutVO.getReceiveCount());
                stockMainService.updateById(stockMain);
                //使用记录
                tStockDetail.setLocationId(stock.getLocationId());
                tStockDetail.setMaterialId(stock.getMaterialId());
                tStockDetail.setCurrentCount(stock.getAvailableCount());
                tStockDetail.setType("2");
                tStockDetail.setOriginId(taskOut.getId());
                stockDetailService.save(tStockDetail);
            //新增小件出库使用数量

            }
        }
       *//* else {
            if(tTaskWcsOutVO.getSmallReceiveCount() != null){
                //既有载具出库，也有小件出库,判断载具出库是否


            }else {
                //只有载具出库,判断该载具的库存是否已出完， 若出完删除该库存，未出完，更新可用状态，更新库存，新增库存出库记录
            }
        }*//*
        if(stock.getAvailableCount()<tTaskWcsOutVO.getReceiveCount()){

        }*/

        return AjaxResult.success();
    }

    /**
     * 通过载具强制执行入库
     *
     * @param tTaskWcs
     * @return
     */
    @Override
    public AjaxResult executeInByTray(TTaskWcs tTaskWcs) {
        TTray tTrayVO = tTrayMapper.selectById(tTaskWcs.getTrayId());
        if (tTrayVO == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        List<TTaskWcsDetailVO> tTaskWcsDetailVOS = tTaskWcsMapper.selectStatusWcsListByTrayId(tTrayVO.getId(), Constants.WCS_TASK_TYPE_IN);
        if (CollectionUtils.isEmpty(tTaskWcsDetailVOS)) {
            return AjaxResult.error("未查询到可执行数据");
        }
        List<Long> taskIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getTaskId).distinct().collect(Collectors.toList());
        List<Long> originIds = tTaskWcsDetailVOS.stream().map(TTaskWcsDetailVO::getOriginId).distinct().collect(Collectors.toList());
        //更新任务状态
        tTaskWcs.setLocationId(tTrayVO.getLocationId());
        tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateStatusByIds(taskIds, tTaskWcs);
        List<TTaskInVO> tTaskInList = taskInService.selectTTaskInInfoByIds(originIds);
        if (CollectionUtils.isEmpty(tTaskInList)) {
            throw new ServiceException("更新失败！");
        }
        //入库
        taskInService.executeEndTask(tTaskInList);
        //添加进库存
        this.inStock(tTaskWcs, tTaskInList);
        //更新载具状态
        TTray tTray = new TTray();
        tTray.setId(tTrayVO.getId());
        tTray.setLocationId(tTrayVO.getLocationId());
        tTrayMapper.updateById(tTray);
        //更新库位状态
        TLocation tLocation = new TLocation();
        tLocation.setId(tTrayVO.getLocationId());
        tLocation.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
        tLocation.setPalletNum(String.valueOf(tTrayVO.getCode()));
        locationService.updateById(tLocation);
        //更新登记
        return AjaxResult.success();
    }

    /**
     * 入库存
     */
    public void inStock(TTaskWcs tTaskWcs, List<TTaskInVO> tTaskInList) {
        //todo wxr 处理库存记录 物料+库位+批次号
        Long locationId = tTaskWcs.getLocationId();
        tTaskInList.forEach(e -> {
            TStockDetail tStockDetail = new TStockDetail();
            tStockDetail.setMaterialId(e.getMaterialId());
            tStockDetail.setType(Constants.WCS_TASK_TYPE_IN);
            tStockDetail.setLocationId(locationId);
            tStockDetail.setOriginCode(e.getOriginCode());
            tStockDetail.setOriginId(e.getAdvanceRegistrationId());//原单id,目前存的task_in_id
            tStockDetail.setStatus("0");
            tStockDetail.setBatchCode(e.getBatchCode());
            tStockDetail.setCurrentCount(e.getActualCount());
            tStockDetail.setCreateBy(tTaskWcs.getCreateBy());
            tStockDetail.setDeptId(tTaskWcs.getDeptId());
            tStockDetail.setDeptName(tTaskWcs.getDeptName());

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
                model.setExpireDate(e.getExpireDate());
                model.setCount(e.getActualCount());
                model.setAvailableCount(e.getActualCount());
                model.setTrayId(e.getTrayId());
//                model.setOriginType(Constants.WCS_TASK_TYPE_IN);
                model.setStatus("0");
                model.setIsFreeze("0");
                model.setCreateBy(tTaskWcs.getCreateBy());
                model.setDeptId(tTaskWcs.getDeptId());
                model.setDeptName(tTaskWcs.getDeptName());
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
                    stockMain.setCreateBy(tTaskWcs.getCreateBy());
                    stockMain.setDeptId(tTaskWcs.getDeptId());
                    stockMain.setDeptName(tTaskWcs.getDeptName());
                    stockMainService.save(stockMain);
                }
                stockService.save(model);
            }
        });
    }

    /**
     * 通过载具编号获取运行任务信息
     *
     * @param trayCode
     * @return
     */
    @Override
    public TTaskWcsVO getTaskInfoByTrayCode(String trayCode, String taskType) {
        PageHelper.clearPage();
        return tTaskWcsMapper.getTaskInfoByTrayCode(trayCode, taskType);
    }

    /**
     * 根据任务编号获取运行任务信息
     *
     * @param taskNo
     * @return
     */
    @Override
    public TTaskWcsVO getTaskInfoByTaskNo(String taskNo) {
        return tTaskWcsMapper.getTaskInfoByTaskNo(taskNo);
    }

    /**
     * 重新执行
     *
     * @param taskWcs
     * @return
     */
    @Override
    public AjaxResult enforcementDelivery(TTaskWcs taskWcs) {
        TTaskWcs taskWcsVO = tTaskWcsMapper.selectById(taskWcs.getId());
        if (taskWcsVO != null) {
            TTray tTrayVO = tTrayMapper.selectById(taskWcsVO.getTrayId());


            if (Constants.TASK_TYPE_PUT.equals(taskWcsVO.getTaskType()) || Constants.TASK_TYPE_BACK.equals(taskWcsVO.getTaskType())) {

                // 查询是否存在移库任务
                TTaskWcs tTaskMove = tTaskWcsMapper.selectOne(new QueryWrapper<TTaskWcs>()
                        .eq("main_task_no", taskWcsVO.getMainTaskNo())
                        .eq("task_type", Constants.TASK_TYPE_MOVE));

                TLocation locationInfo = locationService.getById(taskWcsVO.getLocationId());

                //入库任务
                //调用载具回库命令，托盘调wcs,料箱调agv
                if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = WcsReportUtil.stationIn;
                    String endStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                    if (tTaskMove != null){
                        orderDTO.setMoveTaskNo(tTaskMove.getTaskNo());
                    }
                    String status = wcsReportUtil.sendWcsInReport(orderDTO);
                    tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
                } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = Constants.SHELF_POINT_SECOND_LINE_IN;
                    String endStation = locationInfo.getCode();
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                    String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                    tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
                }

            } else if (Constants.TASK_TYPE_PICK.equals(taskWcsVO.getTaskType()) || Constants.TASK_TYPE_OUT.equals(taskWcsVO.getTaskType())) {

                // 查询是否存在移库任务
                TTaskWcs tTaskMove = tTaskWcsMapper.selectOne(new QueryWrapper<TTaskWcs>()
                        .eq("main_task_no", taskWcsVO.getMainTaskNo())
                        .eq("task_type", Constants.TASK_TYPE_MOVE));

                TLocation locationInfo = locationService.getById(taskWcsVO.getLocationId());
                //拣货任务
                //调用载具回库命令，托盘调wcs,料箱调agv
                if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                    String endStation = WcsReportUtil.stationOut;
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                    if (tTaskMove != null){
                        orderDTO.setMoveTaskNo(tTaskMove.getTaskNo());
                    }
                    String status = wcsReportUtil.sendWcsOutReport(orderDTO);
                    tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
                } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = locationInfo.getCode();
                    String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                    String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                    tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
                }
            } else if (Constants.TASK_TYPE_MOVE.equals(taskWcsVO.getTaskType())) {
                TLocation locationPrup = locationService.getByCode(taskWcsVO.getPurposePosition());
                TLocation locationStart = locationService.getByCode(taskWcsVO.getStartPosition());
                //移库任务
                //调用载具移库命令，托盘调wcs,料箱调agv
                if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = locationStart.getLocationPlies() + "-" + locationStart.getPalletNodeId();
                    String endStation = locationPrup.getLocationPlies() + "-" + locationPrup.getPalletNodeId();
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, locationPrup.getCode(), tTrayVO.getCode());

                    if (taskWcsVO.getMainTaskNo().equals(taskWcsVO.getTaskNo())){
                        // 如果任务号一致，则没有后置任务号，单纯的移库任务
                        orderDTO.setMainTaskNo(null);
                    }else {
                        orderDTO.setMainTaskNo(taskWcsVO.getMainTaskNo());
                    }
                    wcsReportUtil.sendWcsMoveReport(orderDTO);
                } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                    //发送命令
                    String startStation = locationStart.getCode();
                    String endStation = locationPrup.getCode();
                    WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                    orderDTO.setMainTaskNo(taskWcsVO.getMainTaskNo());
                    String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                    tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * 删除任务相关信息
     *
     * @param taskWcs
     * @param taskInIds
     */
    @Override
    public void deleteWcsTaskById(TTaskWcs taskWcs, List<Long> taskInIds) {
        tTaskWcsMapper.deleteTTaskWcsById(taskWcs.getId());
        tTaskWcsDetailMapper.deleteTTaskWcsDetailByTaskId(taskWcs.getId());
        Long[] tasks = taskInIds.stream().toArray(Long[]::new);
        taskInService.deleteTTaskInByIds(tasks);
        //放开库位
        TLocation tLocation = locationService.getById(taskWcs.getLocationId());
        if (tLocation != null && StringUtils.isNotNull(tLocation.getPalletNum())) {
            TLocation locationDTO = new TLocation();
            locationDTO.setId(tLocation.getId());
            locationDTO.setPalletNum("");
            locationDTO.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
            locationService.updateById(locationDTO);
        }
        //放开载具,状态设置为半托，库位id设置为null
        TTray tTray = new TTray();
        tTrayMapper.update(tTray, new UpdateWrapper<TTray>().set("location_id", null).set("status", Constants.TRAY_STATUS_HALF).eq("id", taskWcs.getTrayId()));
    }

    /**
     * 出库重新执行
     *
     * @param taskWcs
     * @return
     */
    @Override
    public AjaxResult enforcementDeliveryOut(TTaskWcs taskWcs) {
        TTaskWcs taskWcsVO = tTaskWcsMapper.selectById(taskWcs.getId());
        if (taskWcsVO != null) {
            TTray tTrayVO = tTrayMapper.selectById(taskWcsVO.getTrayId());
            //调用载具回库命令，托盘调wcs,料箱调agv
            if (Constants.TRAY_CATEGORY_TRAY.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = locationService.getById(taskWcsVO.getLocationId());
                String startStation = locationInfo.getLocationPlies() + "-" + locationInfo.getPalletNodeId();
                String endStation = WcsReportUtil.stationOut;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, locationInfo.getCode(), tTrayVO.getCode());
                String status = wcsReportUtil.sendWcsOutReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
            } else if (Constants.TRAY_CATEGORY_WORKBIN.equals(tTrayVO.getTrayCategory())) {
                //发送命令
                TLocation locationInfo = locationService.getById(taskWcsVO.getLocationId());
                String startStation = locationInfo.getCode();
                String endStation = Constants.SHELF_POINT_SECOND_LINE_OUT;
                WcsOrderDTO orderDTO = new WcsOrderDTO(taskWcsVO.getId(), taskWcsVO.getTaskNo(), startStation, endStation, tTrayVO.getCode());
                String status = agvReportUtil.sendAgvPickingReport(orderDTO);
                tTaskWcsMapper.updateStuasById(taskWcsVO.getId(), status);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 移库回调
     *
     * @param wcsVO
     * @return
     */
    @Override
    @Transactional
    public AjaxResult executeTaskMove(TTaskWcsVO wcsVO) {
        TTaskWcs tTaskWcsVO = tTaskWcsMapper.selectById(wcsVO.getId());
        if (tTaskWcsVO == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(tTaskWcsVO.getTaskStatus())) {
            return AjaxResult.error("当前状态不可执行");
        }
        //设置任务为执行完成
        TTaskWcs taskWcs = new TTaskWcs();
        taskWcs.setId(wcsVO.getId());
        taskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_END);
        tTaskWcsMapper.updateById(taskWcs);
        //查询移库原库位信息
        TLocation startLocation = locationService.getByCode(wcsVO.getStartPosition());
        if (startLocation == null) {
            return AjaxResult.error("未查询到开始库位");
        }
        TLocation tLocationEnd = new TLocation();
        tLocationEnd.setId(wcsVO.getLocationId());
        tLocationEnd.setPalletNum(wcsVO.getTrayCode());
        //根据载具去查stock表查询是否有货
        QueryWrapper<TStock> stockQw = new QueryWrapper<>();
        stockQw.eq("del_flag", Constants.DEL_FLAG_NO);
        stockQw.eq("tray_id", wcsVO.getTrayId());
        List<TStock> trayStockCount = tStockMapper.selectList(stockQw);
        if (trayStockCount != null && trayStockCount.size() > 0) {//有货
            tLocationEnd.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2);
            //有库存，更新库存信息
            List<TStockDetail> detailsList = new ArrayList<>();
            trayStockCount.forEach(e -> {
                if (!e.getLocationId().equals(wcsVO.getLocationId())) {
                    TStockDetail tStockDetail = new TStockDetail();
                    tStockDetail.setMaterialId(e.getMaterialId());
                    tStockDetail.setType(Constants.WCS_TASK_TYPE_MOVE);
                    tStockDetail.setLocationId(wcsVO.getLocationId());
                    tStockDetail.setOriginCode(wcsVO.getTaskNo());
                    tStockDetail.setOriginId(-2L);//原单标识(-1为在线拣选标识;-2直接移库标识)
                    tStockDetail.setStatus("0");
                    tStockDetail.setBatchCode(e.getBatchCode());
                    tStockDetail.setBeforeCount(e.getCount());// 操作前数量
                    tStockDetail.setCurrentCount(e.getCount());// 操作后当前数量
                    detailsList.add(tStockDetail);
                }
            });
            //保存库存详情记录
            if (CollectionUtils.isNotEmpty(detailsList)) {
                stockDetailService.saveBatch(detailsList);
            }
            //更新库存表
            tStockMapper.update(new TStock(),
                    new UpdateWrapper<TStock>()
                            .eq("tray_id", wcsVO.getTrayId())
                            .set("location_id", wcsVO.getLocationId()));
            //更新物料详情表库位
            tMaterialDetailMapper.update(new TMaterialDetail(),
                    new UpdateWrapper<TMaterialDetail>()
                            .eq("del_flag", Constants.DEL_FLAG_NO)
                            .eq("tray_id", wcsVO.getTrayId())
                            .eq("status", Constants.MATERIAL_DETAIL_STATUS_IN)
                            .set("location_id", wcsVO.getLocationId()));
        } else {//无货
            tLocationEnd.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
        }
        //更新移库目的库位
        locationService.updateById(tLocationEnd);
        //更新移库原始库位
        TLocation tLocationStart = new TLocation();
        tLocationStart.setId(startLocation.getId());
        tLocationStart.setPalletNum("");
        tLocationStart.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_1);
        locationService.updateById(tLocationStart);

        //更新库位
        TTray trayDTO = new TTray();
        trayDTO.setId(wcsVO.getTrayId());
        trayDTO.setLocationId(wcsVO.getLocationId());
        tTrayMapper.updateById(trayDTO);

        //TODO WMS-ERP对接移库
        wmsToErpUtils.movePut(tTaskWcsVO);
        return AjaxResult.success();
    }

}
