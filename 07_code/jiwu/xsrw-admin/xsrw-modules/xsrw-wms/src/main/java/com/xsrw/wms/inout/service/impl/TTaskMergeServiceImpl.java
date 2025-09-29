package com.xsrw.wms.inout.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.inout.domain.TMergeDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TTaskOutDTO;
import com.xsrw.wms.inout.domain.vo.TTaskMergeVO;
import com.xsrw.wms.inout.mapper.TTaskOutMapper;
import com.xsrw.wms.inout.service.ITMergeDeliveryDetailService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TTaskMergeMapper;
import com.xsrw.wms.inout.domain.TTaskMerge;
import com.xsrw.wms.inout.service.ITTaskMergeService;

/**
 * 出库任务详情Service业务层处理
 *
 * @author zjj
 * @date 2023-06-26
 */
@Service
public class TTaskMergeServiceImpl extends ServiceImpl<TTaskMergeMapper, TTaskMerge> implements ITTaskMergeService
{
    @Autowired
    private TTaskMergeMapper tTaskMergeMapper;

    @Autowired
    private ITStockService stockService;

    @Autowired
    ITLocationService locationService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    ITMaterialService materialService;

    @Autowired
    private ITUnitService unitService;

    @Autowired
    private ITUnitConfigService unitConfigService;

    @Autowired
    private ITReservoirService reservoirService;

    @Autowired
    private ITStockMainService stockMainService;

    @Autowired
    private ITMergeDeliveryDetailService itMergeDeliveryDetailService;

    @Autowired
    private ITOutStrategyService outStrategyService;

    @Autowired
    private TTaskOutMapper tTaskOutMapper;

    /**
     * 查询出库任务详情列表
     *
     * @param tTaskMerge 出库任务详情
     * @return 出库任务详情
     */
    @Override
    public List<TTaskMergeVO> selectTTaskMergeList(TTaskMerge tTaskMerge)
    {
        List<TTaskMergeVO> voList = new ArrayList<>();

        List<TTaskMerge> mergeList = tTaskMergeMapper.selectTTaskMergeList(tTaskMerge);
        for (TTaskMerge taskMerge : mergeList) {
            TTaskMergeVO vo = new TTaskMergeVO();
            if(taskMerge != null) {
                BeanUtils.copyProperties(taskMerge, vo);
                TStock stock = stockService.getById(taskMerge.getStockId());
                vo.setBatchCode(stock.getBatchCode());
                TMaterial material = materialService.getById(taskMerge.getMaterialId());
                vo.setMaterialCode(material.getCode());
                vo.setMaterialName(material.getName());
                TUnit unit = unitService.getById(material.getUnitId());
                vo.setUnitName(unit.getName());
                TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, material.getId()).eq(TUnitConfig::getDelFlag,0));
                if(unitConfig != null && unitConfig.getMinUnitId() != null){
                    vo.setSmallUnitName(unitService.getById(unitConfig.getMinUnitId()).getName());
                    vo.setCount(unitConfig.getCount());
                }
                TLocation location = locationService.getById(taskMerge.getLocationId());
                vo.setLocationName(location.getName());
                TReservoir tReservoir = reservoirService.getById(location.getReservoirId());
                vo.setReservoirId(tReservoir.getId());
                vo.setReservoirName(tReservoir.getName());
                TTray tray = trayService.getById(stock.getTrayId());
                if(tray!=null){
                    vo.setTrayCode(tray.getCode());
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    /**
     * 查询出库任务详情
     *
     * @param id 出库任务详情主键
     * @return 出库任务详情
     */
    @Override
    public TTaskMergeVO selectTTaskMergeById(Long id)
    {

        TTaskMerge tTaskMerge = tTaskMergeMapper.selectOne(
                new QueryWrapper<TTaskMerge>().eq("wcs_id",id)
                        .eq("del_flag",Constants.DEL_FLAG_NO));

        TTaskMergeVO tTaskMergeVO = new TTaskMergeVO();
        if(tTaskMerge != null) {
            BeanUtils.copyProperties(tTaskMerge, tTaskMergeVO);
            TStock stock = stockService.getById(tTaskMerge.getStockId());
            tTaskMergeVO.setBatchCode(stock.getBatchCode());
            TMaterial material = materialService.getById(tTaskMerge.getMaterialId());
            tTaskMergeVO.setMaterialCode(material.getCode());
            tTaskMergeVO.setMaterialName(material.getName());
            TUnit unit = unitService.getById(material.getUnitId());
            tTaskMergeVO.setUnitName(unit.getName());
            TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId, material.getId()).eq(TUnitConfig::getDelFlag,0));
            if(unitConfig != null && unitConfig.getMinUnitId() != null){
                tTaskMergeVO.setSmallUnitName(unitService.getById(unitConfig.getMinUnitId()).getName());
                tTaskMergeVO.setCount(unitConfig.getCount());
            }
            TLocation location = locationService.getById(tTaskMerge.getLocationId());
            tTaskMergeVO.setLocationName(location.getName());
            TReservoir tReservoir = reservoirService.getById(location.getReservoirId());
            tTaskMergeVO.setReservoirId(tReservoir.getId());
            tTaskMergeVO.setReservoirName(tReservoir.getName());
            TTray tray = trayService.getById(stock.getTrayId());
            tTaskMergeVO.setTrayCode(tray.getCode());
            TStockMain tStockMain=stockMainService.getOne(new LambdaQueryWrapper<TStockMain>()
                    .eq(TStockMain::getMaterialId,material.getId())
                    .eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO)
            );
            if(tStockMain!=null){
                tTaskMergeVO.setAvailableCount(tStockMain.getLibraryCount());
            }else{
                tTaskMergeVO.setAvailableCount(BigDecimal.ZERO);
            }
            tTaskMergeVO.setReceiveCount(tTaskMerge.getPredictCount());
        }
        return tTaskMergeVO;
    }

    /**
     * 新增出库任务详情
     *
     * @param tTaskMerge 出库任务详情
     * @return 结果
     */
    @Override
    public int insertTTaskMerge(TTaskMerge tTaskMerge)
    {
        return tTaskMergeMapper.insert(tTaskMerge);
    }

    /**
     * 修改出库任务详情
     *
     * @param tTaskMerge 出库任务详情
     * @return 结果
     */
    @Override
    public int updateTTaskMerge(TTaskMerge tTaskMerge)
    {
        return tTaskMergeMapper.updateById(tTaskMerge);
    }


    /**
     * 批量删除出库任务详情
     *
     * @param ids 需要删除的出库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskMergeByIds(Long[] ids)
    {
        return tTaskMergeMapper.deleteTTaskMergeByIds(ids);
    }

    /**
     * 删除出库任务详情信息
     *
     * @param id 出库任务详情主键
     * @return 结果
     */
    @Override
    public int deleteTTaskMergeById(Long id)
    {
        return tTaskMergeMapper.deleteTTaskMergeById(id);
    }

    @Override
    public AjaxResult voluntarily(Long mergeDeliveryId, Long materialId) {
        // 查询物料拣货信息
        TMergeDeliveryDetail deliveryDetail = itMergeDeliveryDetailService.getOne(
                new QueryWrapper<TMergeDeliveryDetail>()
                        .eq("merge_delivery_id",mergeDeliveryId)
                        .eq("material_id", materialId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetail == null){
            return AjaxResult.error("单据不存在");
        }

        // 查询可拣货的库存信息
        TStock tStock = new TStock();
        tStock.setLocationType("0");
        //拣货策略
        LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutStrategy::getDelFlag,Constants.DEL_FLAG_NO);
        queryWrapper.eq(TOutStrategy::getFlag,1);
        TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
        String  order = "";
        if(outStrategy != null) {
            if (("先进先出").equals(outStrategy.getName())) {
                order = "a.create_time ASC";
            } else if ("后进先出".equals(outStrategy.getName())) {
                order = "a.create_time DESC";
            }else if("批次".equals(outStrategy.getName())){
                order = "a.batch_code asc";
            }
        }else {
            order = "a.available_count ASC";
        }
        tStock.setMaterialId(Long.valueOf(materialId));
        tStock.setRemark(order);
        List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

        // 分配库存
        BigDecimal receiveCount = deliveryDetail.getPredictReceiveCount();
        if (trayList.size() == 0){
            return AjaxResult.success(new ArrayList<>());
        }else {
            List<TTrayVO> result = new ArrayList<>();
            for (TTrayVO trayVO : trayList) {
                if (receiveCount.compareTo(trayVO.getAvailableCount()) == 1){
                    BigDecimal count = receiveCount;
                    receiveCount = receiveCount.subtract(trayVO.getAvailableCount());
                    if (receiveCount.compareTo(BigDecimal.ZERO) != -1){
                        trayVO.setReceiveCount(trayVO.getAvailableCount());
                    }else {
                        trayVO.setReceiveCount(count);
                    }
                    result.add(trayVO);
                }else {
                    trayVO.setReceiveCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
            return AjaxResult.success(result);
        }
    }

    @Override
    public AjaxResult groundPileTrayListVoluntarily(Long outDeliveryId, Long materialId) {
        // 查询物料拣货信息
        TMergeDeliveryDetail deliveryDetail = itMergeDeliveryDetailService.getOne(
                new QueryWrapper<TMergeDeliveryDetail>()
                        .eq("merge_delivery_id",outDeliveryId)
                        .eq("material_id", materialId)
                        .eq("del_flag", Constants.DEL_FLAG_NO));
        if (deliveryDetail == null){
            return AjaxResult.error("单据不存在");
        }

        // 查询可拣货的库存信息
        TStock tStock = new TStock();
        tStock.setLocationType("1");
        //拣货策略
        LambdaQueryWrapper<TOutStrategy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutStrategy::getDelFlag,Constants.DEL_FLAG_NO);
        queryWrapper.eq(TOutStrategy::getFlag,1);
        TOutStrategy outStrategy = outStrategyService.getOne(queryWrapper);
        String  order = "";
        if(outStrategy != null) {
            if (("先进先出").equals(outStrategy.getName())) {
                order = "a.create_time ASC";
            } else if ("后进先出".equals(outStrategy.getName())) {
                order = "a.create_time DESC";
            }else if("批次".equals(outStrategy.getName())){
                order = "a.batch_code asc";
            }
        }else {
            order = "a.available_count ASC";
        }
        tStock.setMaterialId(Long.valueOf(materialId));
        tStock.setRemark(order);
        List<TTrayVO> trayList = tTaskOutMapper.selectTTrayList(tStock);

        // 分配库存
        BigDecimal receiveCount = deliveryDetail.getPredictReceiveCount();

        Map<String,Object> map = new HashMap<>();
        map.put("predictReceiveCount",deliveryDetail.getPredictReceiveCount());

        if (trayList.size() == 0){
            map.put("receiveCount",0);
            map.put("dataList",new ArrayList<>());
            return AjaxResult.success(map);
        }else {
            List<TTrayVO> result = new ArrayList<>();
            for (TTrayVO trayVO : trayList) {
                if (receiveCount.compareTo(trayVO.getAvailableCount()) == 1){
                    BigDecimal count = receiveCount;
                    receiveCount = receiveCount.subtract(trayVO.getAvailableCount());
                    if (receiveCount.compareTo(BigDecimal.ZERO) != -1){
                        trayVO.setReceiveCount(trayVO.getAvailableCount());
                    }else {
                        trayVO.setReceiveCount(count);
                    }
                    result.add(trayVO);
                }else {
                    trayVO.setReceiveCount(receiveCount);
                    result.add(trayVO);
                    break;
                }

            }
            map.put("receiveCount",result.stream().map(TTrayVO::getPredictCount).reduce(BigDecimal.ZERO,BigDecimal::add));
            map.put("dataList",result);
            return AjaxResult.success(map);
        }
    }
}
