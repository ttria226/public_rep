package com.xsrw.wms.inout.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.common.enums.CodeEnum;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.base.domain.vo.TMaterialSelectVO;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.base.mapper.TBomMapper;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TReservoirMapper;
import com.xsrw.wms.base.service.*;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSZiDTO;
import com.xsrw.wms.inout.domain.dto.TOutboundScanningDTO;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.inout.mapper.*;
import com.xsrw.wms.inout.service.ITOutDeliveryDetailService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import com.xsrw.common.core.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.xsrw.wms.inout.service.ITOutDeliveryService;

/**
 * 出库单Service业务层处理
 *
 * @author zyq
 * @date 2023-05-09
 */
@Service
public class TOutDeliveryServiceImpl extends ServiceImpl<TOutDeliveryMapper, TOutDelivery> implements ITOutDeliveryService
{
    @Autowired
    private TOutDeliveryMapper tOutDeliveryMapper;

    @Autowired
    private ITOutDeliveryDetailService outDeliveryDetailService;

    @Autowired
    private ITCodeConfigService codeConfigService;

    @Autowired
    private ITMaterialService materialService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private TOutDeliveryDetailMapper tOutDeliveryDetailMapper;

    @Autowired
    private ITUnitService unitService;

    @Autowired
    private ITStockService stockService;

    @Autowired
    private ITUnitConfigService unitConfigService;

    @Autowired
    private ITStockMainService stockMainService;
    @Autowired
    private TTaskWcsMapper tTaskWcsMapper;
    @Autowired
    private TTaskWcsDetailMapper taskWcsDetailMapper;
    @Autowired
    private TTaskOutMapper tTaskOutMapper;
    @Autowired
    private ITLocationService locationService;

    @Autowired
    private ITBomDetailService itBomDetailService;

    @Autowired
    private ITOutStrategyService outStrategyService;

    @Autowired
    private TStockDetailMapper tStockDetailMapper;

    @Autowired
    private TBomMapper bomMapper;

    @Autowired
    private TReservoirMapper reservoirMapper;

    @Autowired
    private TStockMapper tStockMapper;

    /**
     * 查询出库单列表
     *
     * @param tOutDelivery 出库单
     * @return 出库单
     */
    @Override
    public List<TOutDeliveryVO> selectTOutDeliveryList(TOutDelivery tOutDelivery)
    {
        return tOutDeliveryMapper.selectTOutDeliveryList(tOutDelivery);
    }

    @Override
    public List<TOutDeliveryDetailVO> outTasklist(TOutDeliveryDetail tOutDeliveryDetail) {

        List<TOutDeliveryDetailVO> tOutDeliveryDetails = tOutDeliveryDetailMapper.selectTOutDeliveryDetailVOList(tOutDeliveryDetail);
        for (TOutDeliveryDetailVO outDeliveryDetailVO : tOutDeliveryDetails) {
            outDeliveryDetailVO.setNum(outDeliveryDetailVO.getPredictReceiveCount());
        }
        return tOutDeliveryDetails;
    }

    @Override
    public List<TTrayApiVO> traylist(TTray tTray) {
        tTray.setDelFlag(Constants.DEL_FLAG_NO);
        TTrayDTO tTrayDTO = new TTrayDTO();
        /*List<TTray> list = trayService.selectTTrayList(tTray);*/
        List<TTrayApiVO> list = trayService.selectTTrayList(tTrayDTO);
        return list;
    }

    /**
     * 查询出库单
     *
     * @param id 出库单主键
     * @return 出库单
     */
    @Override
    public TOutDeliveryVO selectTOutDeliveryById(Long id)
    {
        TOutDeliveryVO tOutDeliveryVO = new TOutDeliveryVO();
        TOutDelivery tOutDelivery = tOutDeliveryMapper.selectById(id);
        BeanUtils.copyBeanProp(tOutDeliveryVO,tOutDelivery);
        LambdaQueryWrapper<TOutDeliveryDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TOutDeliveryDetail::getOutDeliveryId,id);
        queryWrapper.eq(TOutDeliveryDetail::getDelFlag,Constants.DEL_FLAG_NO);
        List<TOutDeliveryDetail> tOutDeliveryDetails = outDeliveryDetailService.list(queryWrapper);
        if(CollectionUtils.isNotEmpty(tOutDeliveryDetails)){
            List<Long> materialIds = tOutDeliveryDetails.stream().map(TOutDeliveryDetail::getMaterialId).collect(Collectors.toList());
            Map<Long, TMaterial> materialMap = materialService.getCodeByIds(materialIds);
            for (TOutDeliveryDetail tOutDeliveryDetail : tOutDeliveryDetails) {
                TMaterial material = materialMap.get(tOutDeliveryDetail.getMaterialId());
                if(material!=null){
                    tOutDeliveryDetail.setMaterialName(material.getName());
                    tOutDeliveryDetail.setMaterialCode(material.getCode());
                    TUnit unit = unitService.getById(material.getUnitId());
                    if(unit != null) {
                        tOutDeliveryDetail.setMaterialUnit(unit.getName());
                    }
                    TUnitConfig tUnitConfig = unitConfigService.getOne(new QueryWrapper<TUnitConfig>().eq("material_id", material.getId()));
                    if (tUnitConfig != null){
                        TUnit tUnitMini = unitService.getById(tUnitConfig.getMinUnitId());
                        tOutDeliveryDetail.setMinUnitName(tUnitMini.getName());
                    }
                }

//                // 查询批次号
//                TTaskOut tTaskOut = tTaskOutMapper.selectOne(new QueryWrapper<TTaskOut>()
//                        .eq("out_delivery_detail_id", tOutDeliveryDetail.getId())
//                        .eq("out_delivery_id", tOutDeliveryDetail.getOutDeliveryId()));
//                if (tTaskOut != null){
//                    TStock tStock = tStockMapper.selectById(tTaskOut.getStockId());
//                    tOutDeliveryDetail.setBatchCode(tStock.getBatchCode());
//                }
            }
            tOutDeliveryVO.settOutDeliveryDetailList(tOutDeliveryDetails);
        }

        // 查询bom名称
        if (tOutDelivery.getBomId() != null){
            TBom tBom = bomMapper.selectById(tOutDelivery.getBomId());
            tOutDeliveryVO.setBomName(tBom.getName());
        }

        // 查询库区名称
        if (tOutDelivery.getReservoirId() != null){
            TReservoir tReservoir = reservoirMapper.selectById(tOutDelivery.getReservoirId());
            tOutDeliveryVO.setReservoirName(tReservoir.getName());
        }
        return tOutDeliveryVO;
    }

    /**
     * 新增出库单
     *
     * @param tOutDeliveryVO 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertTOutDelivery(TOutDeliveryVO tOutDeliveryVO)
    {

        TOutDelivery tOutDelivery = new TOutDelivery();
        tOutDelivery.setDeliveryModule(tOutDeliveryVO.getDeliveryModule());
        tOutDelivery.setType(tOutDeliveryVO.getType());
        tOutDelivery.setReservoirId(tOutDeliveryVO.getReservoirId());
        tOutDelivery.setRemark(tOutDeliveryVO.getRemark());
        tOutDelivery.setCreateBy(tOutDeliveryVO.getCreateBy());
        tOutDelivery.setDeptName(tOutDeliveryVO.getDeptName());
        if (StringUtils.isNotEmpty(tOutDeliveryVO.getNewLocal())){
            tOutDelivery.setNewLocal(tOutDeliveryVO.getNewLocal());
        }else {
            tOutDelivery.setNewLocal("1");
        }
        tOutDelivery.setNextFlag("0");
        tOutDelivery.setStatus(Constants.INOUT_STATUS_WAITING);
        if (StringUtils.isEmpty(tOutDeliveryVO.getCode())){
            tOutDelivery.setCode(codeConfigService.getCode(CodeEnum.CKJH.getCodeName()));
        } else {
            tOutDelivery.setCode(tOutDeliveryVO.getCode());
        }
        if (StringUtils.isNotEmpty(tOutDeliveryVO.getDeptName())){
            tOutDelivery.setDeptName(tOutDeliveryVO.getDeptName());
        }
        if (tOutDeliveryVO.getDeptId() != null){
            tOutDelivery.setDeptId(tOutDeliveryVO.getDeptId());
        }
        if (tOutDeliveryVO.getOriginDate() != null){
            tOutDelivery.setOriginDate(tOutDeliveryVO.getOriginDate());
        }
        if (StringUtils.isNotEmpty(tOutDeliveryVO.getOriginCode())){
            tOutDelivery.setOriginCode(tOutDeliveryVO.getOriginCode());
        }

        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDeliveryVO.gettOutDeliveryDetailList();
        if(tOutDeliveryDetailList.size() == 0 ){
            return AjaxResult.error("请输入计划出库的物料");
        }
        List<TOutDeliveryDetail> collect = tOutDeliveryDetailList.stream()
                .distinct()
                .collect(Collectors.toList());
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            TMaterial material = materialService.getById(tOutDeliveryDetail.getMaterialId());
            if(material == null){
                return AjaxResult.error("请选择计划出库的物料");
            }
            if(tOutDeliveryDetail.getPredictCount()==null && tOutDeliveryDetail.getSmallPredictCount() == null){
                return AjaxResult.error("请输入计划出库数量");
            }

            if (tOutDeliveryDetail.getPredictCount() != null){
                // 出库数量只能为地堆或货架  限制数量为地堆或货架可用数量内才允许单据创建
                // 货架库存
                BigDecimal materialTray = tStockMapper.getMaterialNum(tOutDeliveryDetail.getMaterialId(), tOutDeliveryDetail.getBatchCode(),"0");
                // 地堆库存
                BigDecimal materialLocal = tStockMapper.getMaterialNum(tOutDeliveryDetail.getMaterialId(),tOutDeliveryDetail.getBatchCode(), "1");
                if (tOutDeliveryDetail.getPredictCount().compareTo(materialTray) == 1
                        && tOutDeliveryDetail.getPredictCount().compareTo(materialLocal) == 1){
                    return AjaxResult.error("出库数量超出库存，无法创建计划");
                }
            }


            //出库数量是否大于在库可用数量
            LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TStockMain::getMaterialId,material.getId());
            queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
            TStockMain stockMain = stockMainService.getOne(queryWrapper);
            //判断小件领取数量是否超过包装单位最大数据--张雅倩
             if(tOutDeliveryDetail.getSmallPredictCount() != null){
                 TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId,material.getId()));
                 if(unitConfig == null){
                     return AjaxResult.error("该物料不可小件领取");
                 }
                 if(unitConfig.getMinUnitId() == 0){
                    return AjaxResult.error("该物料不可小件领取");
                }
                if(tOutDeliveryDetail.getSmallPredictCount().compareTo(new BigDecimal(unitConfig.getCount())) != -1){
                    return AjaxResult.error("小件领取数已足整件领取");
                }
            }
            tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictCount());
            if(tOutDeliveryDetail.getPredictCount() == null){
                tOutDeliveryDetail.setPredictReceiveCount(BigDecimal.ONE);
            }
            if(tOutDeliveryDetail.getPredictCount() != null && tOutDeliveryDetail.getSmallPredictCount() != null){
                tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictReceiveCount().add(BigDecimal.ONE));
            }
            if(tOutDeliveryDetail.getPredictReceiveCount().compareTo(stockMain.getAvailableCount()) == 1){
                return AjaxResult.error("计划出库数量不可大于在库可用数量");
            }

            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            tOutDeliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_WAIT);

            //更新该物料的在库可用数量
            stockMain.setAvailableCount(stockMain.getAvailableCount().subtract(tOutDeliveryDetail.getPredictReceiveCount()));
            stockMainService.updateById(stockMain);
        }
        tOutDeliveryMapper.insert(tOutDelivery);
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            outDeliveryDetailService.save(tOutDeliveryDetail);
        }

        return AjaxResult.success();
    }

    /**
     * 出库计划审核
     *
     * @param tOutDelivery 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult approveTOutDelivery(TOutDelivery tOutDelivery)
    {
        TOutDelivery outDelivery = tOutDeliveryMapper.selectById(tOutDelivery.getId());
        if(outDelivery == null || !Constants.INOUT_STATUS_WAITING.equals(outDelivery.getStatus())){
            return AjaxResult.error("当前状态不可审核");
        }
        LambdaQueryWrapper<TOutDeliveryDetail> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(TOutDeliveryDetail::getDelFlag,Constants.DEL_FLAG_NO);
        queryWrapper1.eq(TOutDeliveryDetail::getOutDeliveryId,tOutDelivery.getId());
        List<TOutDeliveryDetail> tOutDeliveryDetails = tOutDeliveryDetailMapper.selectList(queryWrapper1);
        tOutDeliveryDetails.forEach(tOutDeliveryDetail -> {
            //审核不通过时退回库存，返回库存的可用数量
            if(tOutDelivery.getStatus().equals(Constants.INOUT_STATUS_REGISTER_ABOLISH)){
                LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TStockMain::getMaterialId,tOutDeliveryDetail.getMaterialId());
                queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
                TStockMain stockMain = stockMainService.getOne(queryWrapper);
                stockMain.setAvailableCount(stockMain.getAvailableCount().add(tOutDeliveryDetail.getPredictReceiveCount()));
                stockMainService.updateById(stockMain);
            }
        });
        tOutDelivery.setAuditor(SecurityUtils.getUsername());
        tOutDeliveryMapper.updateById(tOutDelivery);

        // 审核通过 生成出库任务
        Long [] ids = {tOutDelivery.getId()};
        this.toOutTask(ids);

        return AjaxResult.success();
    }

    @Override
    public AjaxResult toOutTask(Long[] ids) {
        LambdaQueryWrapper<TOutDelivery> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(TOutDelivery::getStatus,Constants.INOUT_STATUS_PASS);
        queryWrapper.in(TOutDelivery::getId,ids);
        List<TOutDelivery> tOutDeliveries = tOutDeliveryMapper.selectList(queryWrapper);
        if(tOutDeliveries.size()>0){
            return AjaxResult.error("非通过的单子不可生成出库任务");
        }
        List<TOutDelivery> list = new ArrayList();
        for (Long id : ids) {
            TOutDelivery tOutDelivery = tOutDeliveryMapper.selectById(id);
            if(tOutDelivery != null){
                if(tOutDelivery.getNextFlag().equals(Constants.INOUT_NEXT_FLAG_YES)){
                    return AjaxResult.error("已生成出库任务，不可再次生成");
                }
                tOutDelivery.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
                list.add(tOutDelivery);
            }else {
                return AjaxResult.error("请选择数据");
            }
        }
        this.updateBatchById(list);
        return AjaxResult.success();
    }

    @Override
    public List<TMaterialSelectVO> getMaterialSelectList(TMaterialDTO tMaterial) {
        if (tMaterial.getReservoirId() != null){
            // 查询库区下库位
            List<TLocation> locationList = locationService.list(
                    new QueryWrapper<TLocation>()
                            .eq("reservoir_id", tMaterial.getReservoirId())
                            .eq("status", "1")
                            .eq("del_flag", Constants.DEL_FLAG_NO));
            if (locationList.size() == 0){
                return new ArrayList<>();
            }else {
                List<Long> collect = locationList.stream().map(TLocation::getId).collect(Collectors.toList());
                tMaterial.setLocationIds(collect);
            }

        }

        List<TMaterialSelectVO> materialList = tOutDeliveryMapper.getMaterialSelectList(tMaterial);
        if (materialList.size() > 0){
            // 查询物料在货架、地堆上的库存
            materialList.forEach(e -> {
                // 查询货架
                BigDecimal materialTray = tStockMapper.getMaterialNum(e.getId(),null, "0");
                // 查询地堆
                BigDecimal materiaLocal = tStockMapper.getMaterialNum(e.getId(), null, "1");

                e.setMaterialTray(materialTray);
                e.setMateriaLocal(materiaLocal);
            });
        }

        return materialList;
    }


    /**
     * 修改出库计划
     *
     * @param tOutDelivery 出库单
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult updateTOutDelivery(TOutDeliveryVO tOutDelivery)
    {
        TOutDelivery outDelivery = tOutDeliveryMapper.selectById(tOutDelivery.getId());
        if(outDelivery == null){
            return AjaxResult.error("未找到数据");
        }
        if(outDelivery.getStatus().equals(Constants.INOUT_STATUS_PASS)){
            return AjaxResult.error("已通过的计划不可编辑");
        }
        TOutDeliveryDetail outDeliveryDetail = new TOutDeliveryDetail();
        outDeliveryDetail.setOutDeliveryId(outDelivery.getId());
        List<TOutDeliveryDetail> tOutDeliveryDetails = tOutDeliveryDetailMapper.selectTOutDeliveryDetailList(outDeliveryDetail);
        // 删除数据时，将物料的可用数量追加
        for (TOutDeliveryDetail data:tOutDeliveryDetails) {
            // 校验物料拣货数量是否超过库存可用数量
            TStockMain stockMain = stockMainService.getOne(new QueryWrapper<TStockMain>()
                    .eq("material_id", data.getMaterialId())
                    .eq("del_flag", Constants.DEL_FLAG_NO));
            if(data.getMaterialId() == null){
                return AjaxResult.error("请选择计划出库的物料");
            }
            if(data.getPredictCount()==null && data.getSmallPredictCount() == null
            ){
                return AjaxResult.error("请输入计划出库数量");
            }
            // 更新库存中物料的可用数量
            if(stockMain!=null && data.getSmallPredictCount() == null){
                stockMain.setAvailableCount(stockMain.getAvailableCount().add(data.getPredictCount()));
                stockMainService.updateById(stockMain);
            }
        }
        outDeliveryDetailService.removeBatchByIds(tOutDeliveryDetails);
        outDelivery.setType(tOutDelivery.getType());
        outDelivery.setRemark(tOutDelivery.getRemark());
        tOutDeliveryMapper.updateById(outDelivery);
//        this.insertTOutDelivery(tOutDelivery);
        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDelivery.gettOutDeliveryDetailList();
        if(tOutDeliveryDetailList.size() == 0 ){
            return AjaxResult.error("请输入计划出库的物料");
        }
        List<TOutDeliveryDetail> collect = tOutDeliveryDetailList.stream()
                .distinct()
                .collect(Collectors.toList());
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            TMaterial material = materialService.getById(tOutDeliveryDetail.getMaterialId());
            if(material == null){
                return AjaxResult.error("请选择计划出库的物料");
            }
            if(tOutDeliveryDetail.getPredictCount()==null && tOutDeliveryDetail.getSmallPredictCount() == null
            ){
                return AjaxResult.error("请输入计划出库数量");
            }
            //出库数量是否大于在库可用数量
            LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TStockMain::getMaterialId,material.getId());
            queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
            TStockMain stockMain = stockMainService.getOne(queryWrapper);
            //判断小件领取数量是否超过包装单位最大数据--张雅倩
            if(tOutDeliveryDetail.getSmallPredictCount() != null){
                TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId,material.getId()));
                if(unitConfig == null){
                    return AjaxResult.error("该物料不可小件领取");
                }
                if(unitConfig.getMinUnitId() == 0){
                    return AjaxResult.error("该物料不可小件领取");
                }
                if(tOutDeliveryDetail.getSmallPredictCount().compareTo(new BigDecimal(unitConfig.getCount())) != -1){
                    return AjaxResult.error("小件领取数已足整件领取");
                }
            }
            tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictCount());
            if(tOutDeliveryDetail.getPredictCount() == null){
                tOutDeliveryDetail.setPredictReceiveCount(BigDecimal.ONE);
            }
            if(tOutDeliveryDetail.getPredictCount() != null && tOutDeliveryDetail.getSmallPredictCount() != null){
                tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictReceiveCount().add(BigDecimal.ONE));
            }
            if(tOutDeliveryDetail.getPredictReceiveCount().compareTo(stockMain.getAvailableCount()) == 1){
                return AjaxResult.error("计划出库数量不可大于在库可用数量");
            }

            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            tOutDeliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_WAIT);

            //更新该物料的在库可用数量
            stockMain.setAvailableCount(stockMain.getAvailableCount().subtract(tOutDeliveryDetail.getPredictReceiveCount()));
            if(stockMain.getAvailableCount().compareTo(BigDecimal.ZERO) == -1){
                return AjaxResult.error("可用数量不足");
            }
            stockMainService.updateById(stockMain);
        }
        outDeliveryDetailService.saveBatch(tOutDeliveryDetailList);
        return AjaxResult.success();
    }


    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的出库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult deleteTOutDeliveryByIds(Long[] ids)
    {
        LambdaQueryWrapper<TOutDelivery> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(TOutDelivery::getStatus,Constants.INOUT_STATUS_WAITING);
        queryWrapper.in(TOutDelivery::getId,ids);
        List<TOutDelivery> tOutDeliveries = tOutDeliveryMapper.selectList(queryWrapper);
        if(tOutDeliveries.size()>0){
            return AjaxResult.error("非未审核状态的单子不可删除");
        }
        List<TOutDeliveryDetail> outDeliveryDetailList = tOutDeliveryDetailMapper.selectList(
                new QueryWrapper<TOutDeliveryDetail>().in("out_delivery_id",ids));
        if(outDeliveryDetailList.size() > 0){
            TOutDelivery delivery = tOutDeliveryMapper.selectById(outDeliveryDetailList.get(0).getOutDeliveryId());
            // 删除数据时，将物料的可用数量追加
            for (TOutDeliveryDetail data:outDeliveryDetailList) {
                // 校验物料拣货数量是否超过库存可用数量
                TStockMain stockMain = stockMainService.getOne(new QueryWrapper<TStockMain>()
                        .eq("material_id", data.getMaterialId())
                        .eq("del_flag", Constants.DEL_FLAG_NO));
                // 更新库存中物料的可用数量
                stockMain.setAvailableCount(stockMain.getAvailableCount().add(data.getPredictReceiveCount()));
                stockMainService.updateById(stockMain);
            }
        }
        tOutDeliveryMapper.deleteTOutDeliveryByIds(ids);
        tOutDeliveryMapper.deleteTOutDeliveryDetailByAdvanceDeliveryIds(ids);
        return AjaxResult.success();
    }

   /**
     * 批量删除出库执行列表的任务
     *
     * @param ids 需要删除的出库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult deleteTOutDeliveryDetailByIds(Long[] ids)
    {
        LambdaQueryWrapper<TOutDeliveryDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(TOutDeliveryDetail::getNextFlag,Constants.INOUT_NEXT_FLAG_YES);
        queryWrapper.in(TOutDeliveryDetail::getId,ids);
        List<TOutDeliveryDetail> tOutDeliveries = outDeliveryDetailService.list(queryWrapper);
        if(tOutDeliveries.size()>0){
            return AjaxResult.error("非待执行的单子不可删除");
        }
        tOutDeliveryMapper.deleteTOutDeliveryDetailByAdvanceDeliveryIds(ids);
        return AjaxResult.success();
    }

    /**
     * 删除出库单信息
     *
     * @param id 出库单主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTOutDeliveryById(Long id)
    {
        tOutDeliveryMapper.deleteTOutDeliveryDetailByAdvanceDeliveryId(id);
        return tOutDeliveryMapper.deleteTOutDeliveryById(id);
    }

    /**
     * 新增出库单详情信息
     *
     * @param tOutDelivery 出库单对象
     */
    public void insertTOutDeliveryDetail(TOutDelivery tOutDelivery)
    {
        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDelivery.gettOutDeliveryDetailList();
        Long id = tOutDelivery.getId();
        if (StringUtils.isNotNull(tOutDeliveryDetailList))
        {
            List<TOutDeliveryDetail> list = new ArrayList<TOutDeliveryDetail>();
            for (TOutDeliveryDetail tOutDeliveryDetail : tOutDeliveryDetailList)
            {
                tOutDeliveryDetail.setOutDeliveryId(id);
                list.add(tOutDeliveryDetail);
            }
            if (list.size() > 0)
            {
                tOutDeliveryMapper.batchTOutDeliveryDetail(list);
            }
        }
    }

    /**
     * 出库
     * @param tOutDeliveryVO
     * @return
     */
    @Override
    public  AjaxResult outBound(TOutDeliveryVO tOutDeliveryVO){
        if(tOutDeliveryVO.getDeliveryType()==null||tOutDeliveryVO.getDeliveryType().equals("0")){
            //可视化出库
            return  visualOutbound(tOutDeliveryVO);
        }else{
            //应急出库
            return  emergencyOutbound(tOutDeliveryVO);
        }
    }

    @Override
    public List<TOutDelivery> getMergeList(TOutDelivery tOutDelivery) {
        return tOutDeliveryMapper.getMergeList(tOutDelivery);
    }



    /**
     * 齐套出库新增
     * @param tOutDeliveryVO
     * @return
     */
    @Transactional
    @Override
    public AjaxResult suitAdd(TOutDeliveryVO tOutDeliveryVO) throws ServiceException {

        if (tOutDeliveryVO.getSuitNum() == null || tOutDeliveryVO.getSuitNum().intValue() == 0){
            return AjaxResult.error("物料齐套数量需大于0");
        }

        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDeliveryVO.gettOutDeliveryDetailList();
        if (tOutDeliveryDetailList == null || tOutDeliveryDetailList.size() == 0){
            return AjaxResult.error("请选择齐套物料");
        }

        TOutDelivery tOutDelivery = new TOutDelivery();
        // 单据来源  1普通出库 2齐套出库
        tOutDelivery.setDeliveryModule("2");
        tOutDelivery.setType(tOutDeliveryVO.getType());
        // 来源字典（1.本地创建 2.erp接口 3.调拨单）
        tOutDelivery.setNewLocal("1");
        // 是否转为出库任务(0待转为出库任务，1 已转为出库任务)
        tOutDelivery.setNextFlag("0");
        tOutDelivery.setStatus(Constants.INOUT_STATUS_WAITING);
        tOutDelivery.setCode(codeConfigService.getCode(CodeEnum.CKJH.getCodeName()));
        tOutDelivery.setBomId(tOutDeliveryVO.getSuitMaterialId());
        tOutDelivery.setBomCount(tOutDeliveryVO.getSuitNum());
        tOutDeliveryMapper.insert(tOutDelivery);

        // 查询齐套物料详情
        List<TBomDetail> bomDetails = itBomDetailService.list(
                new QueryWrapper<TBomDetail>()
                        .eq("bom_id",tOutDeliveryVO.getSuitMaterialId())
                        .eq("del_flag",Constants.DEL_FLAG_NO));

        if (bomDetails.size() == 0){
            throw new ServiceException("齐套物料不存在");
        }

        for (TOutDeliveryDetail tOutDeliveryDetail : tOutDeliveryDetailList) {
            for (TBomDetail bomDetail : bomDetails) {
                if (tOutDeliveryDetail.getMaterialId().equals(bomDetail.getMaterialId())){
                    if (!tOutDeliveryDetail.getPredictCount().toString().equals(bomDetail.getCount())){
                        throw new ServiceException("齐套内物料数量不可更改");
                    }
                }
            }

            if(tOutDeliveryDetail.getPredictCount() == null ){
                throw new ServiceException("请输入计划出库数量");
            }

            //出库数量是否大于在库可用数量
            LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TStockMain::getMaterialId,tOutDeliveryDetail.getMaterialId());
            queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
            TStockMain stockMain = stockMainService.getOne(queryWrapper);

            tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictCount());
            if(tOutDeliveryDetail.getPredictReceiveCount().multiply(new BigDecimal(tOutDeliveryVO.getSuitNum())).compareTo(stockMain.getAvailableCount()) == 1){
                throw new ServiceException("计划出库数量不可大于在库可用数量");
            }
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            tOutDeliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_WAIT);

            //更新该物料的在库可用数量
            stockMain.setAvailableCount(stockMain.getAvailableCount().subtract(tOutDeliveryDetail.getPredictReceiveCount().multiply(new BigDecimal(tOutDeliveryVO.getSuitNum()))));
            stockMainService.updateById(stockMain);
        }

        outDeliveryDetailService.saveBatch(tOutDeliveryDetailList);

        return AjaxResult.success();
    }


    /**
     * 查询物料分配
     * @param materialId
     * @return
     */
    @Override
    public List<TTrayVO> suitMaterial(Long materialId,String type) {
        TStock tStock = new TStock();
        tStock.setLocationType(type);

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
        tStock.setMaterialId(materialId);
        tStock.setRemark(order);
        List<TTrayVO> tTrayVOS = tTaskOutMapper.selectTTrayList(tStock);
        return tTrayVOS;
    }


    /**
     * 执行出库
     * @param taskOutVO
     * @return
     */
    @Transactional
    @Override
    public AjaxResult suitAddTask(List<TTaskOutVO> taskOutVO) {

        for (TTaskOutVO tTaskOutVO : taskOutVO) {
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
            BigDecimal receiveCount = deliveryDetail.getPredictReceiveCount();
            BigDecimal sum = tTaskOutDetailListVOS.stream().filter(e -> e.getPredictCount() != null).map(TTaskOutDetailListVO::getPredictCount).reduce(BigDecimal.ZERO,BigDecimal::add);
            if (sum.compareTo(deliveryDetail.getPredictReceiveCount()) != 0) {
                return AjaxResult.error("拣货数量和单据预计拣货数量不一致！");
            }
            List<TTaskOutDetailListVO> collect = tTaskOutDetailListVOS.stream().distinct().collect(Collectors.toList());
            //判断所选载具的库存是否
            for (TTaskOutDetailListVO tTaskOutDetailListVO : collect) {
                TLocation location = locationService.getById(tTaskOutDetailListVO.getLocationId());
//                if (location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)) {
//                    throw new ServiceException("请选择有效库位");
//                }
//                TTray tTray = trayService.getById(tTaskOutDetailListVO.getTrayId());
//                if (tTray == null) {
//                    return AjaxResult.error("请选择有效载具");
//                }
                TStock tStock = stockService.getById(tTaskOutDetailListVO.getStockId());
                if (tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)) {
                    throw new ServiceException("请选择有效库存");
                }

                TTaskOut tTaskOut = new TTaskOut();
                tTaskOut.setOutDeliveryId(deliveryDetail.getOutDeliveryId());
                tTaskOut.setMaterialId(deliveryDetail.getMaterialId());
                tTaskOut.setPredictCount(tTaskOutDetailListVO.getPredictCount());
                tTaskOut.setStockId(tTaskOutDetailListVO.getStockId());
                tTaskOut.setLocationId(tTaskOutDetailListVO.getLocationId());
                tTaskOut.setTrayId(tTaskOutDetailListVO.getTrayId());
                tTaskOut.setOutDeliveryDetailId(deliveryDetail.getId());
                // 已完成
                tTaskOut.setStatus("2");
                tTaskOutMapper.insert(tTaskOut);

                deliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
                outDeliveryDetailService.updateById(deliveryDetail);
                //将库位状态标记为已出库
                location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
                locationService.updateById(location);

                // 记录库存扣减记录
                TStockDetail tStockDetail = new TStockDetail();
                tStockDetail.setMaterialId(deliveryDetail.getMaterialId());
                tStockDetail.setType(Constants.WCS_TASK_TYPE_OUT);
                tStockDetail.setLocationId(tStock.getLocationId());
                tStockDetail.setOriginId(deliveryDetail.getId());
                tStockDetail.setStatus("0");
                tStockDetail.setBatchCode(tStock.getBatchCode());
                tStockDetail.setCurrentCount(tStock.getCount().subtract(tTaskOutDetailListVO.getPredictCount()));
                tStockDetail.setBeforeCount(tStock.getCount());// 操作前数量
                tStockDetailMapper.insert(tStockDetail);

                // 扣减库存
                tStock.setCount(tStock.getCount().subtract(tTaskOutDetailListVO.getPredictCount()));
                stockService.updateById(tStock);

                // 扣减主库存
                TStockMain stockMain = stockMainService.getOne(
                        new QueryWrapper<TStockMain>().eq("material_id", deliveryDetail.getMaterialId()));
                stockMain.setLibraryCount(stockMain.getLibraryCount().subtract(tTaskOutDetailListVO.getPredictCount()));
                stockMainService.updateById(stockMain);
            }
        }
        return AjaxResult.success();
    }


    /**
     * 地堆出库
     * @param taskOutVO
     * @return
     */
    @Override
    public AjaxResult addTaskPile(List<TTaskOutVO> taskOutVO) {

        for (TTaskOutVO tTaskOutVO : taskOutVO) {
            if(tTaskOutVO.getOutDeliveryDetailId() == null
                    ||(tTaskOutVO.gettTaskOutDetailListVOS()==null||tTaskOutVO.gettTaskOutDetailListVOS().size()<=0)){
                throw new ServiceException("参数错误！");
            }
            TOutDeliveryDetail tOutDeliveryDetail=outDeliveryDetailService.getById(tTaskOutVO.getOutDeliveryDetailId());
            if(tOutDeliveryDetail==null){
                throw new ServiceException("出库单不存在！");
            }
            //已拣货数量
            BigDecimal outboundCount=tTaskOutMapper.selectList(Wrappers.lambdaQuery(TTaskOut.class)
                    .eq(TTaskOut::getOutDeliveryDetailId,tTaskOutVO.getOutDeliveryDetailId())
                    .eq(TTaskOut::getDelFlag,Constants.DEL_FLAG_NO)
            ).stream().map(tTaskOut -> tTaskOut.getActualCount()).reduce(BigDecimal.ZERO,BigDecimal::add);
            List<TTaskOut> tTaskOutList=new ArrayList<>();
            for (TTaskOutDetailListVO task:tTaskOutVO.gettTaskOutDetailListVOS()) {
                TStock tStock = stockService.getById(task.getStockId());
                if(tStock == null || !tStock.getStatus().equals(Constants.STOCK_USE_YES)){
                    throw new ServiceException("请选择有效库存");
                }
                TLocation location = locationService.getById(tStock.getLocationId());
                if(location == null || !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_2)){
                    throw new ServiceException("请选择有效库位");
                }
//            TTray tTray = trayService.getById(tStock.getTrayId());
//            if(tTray == null){
//                return AjaxResult.error("请选择有效载具");
//            }
                if(task.getReceiveCount()==null||task.getReceiveCount().compareTo(BigDecimal.ZERO) != 1){
                    throw new ServiceException("实际拣货数量不可以为空！");
                }
                //添加出库记录
                TTaskOut tTaskOut=new TTaskOut();
                tTaskOut.setOutDeliveryId(tOutDeliveryDetail.getOutDeliveryId());
                tTaskOut.setOutDeliveryDetailId(tOutDeliveryDetail.getId());
                tTaskOut.setLocationId(location.getId());
                tTaskOut.setStockId(task.getStockId());
                tTaskOut.setMaterialId(tStock.getMaterialId());
                tTaskOut.setActualCount(task.getReceiveCount());
                tTaskOut.setPredictCount(tOutDeliveryDetail.getPredictReceiveCount());
                tTaskOut.setStatus("2");
                tTaskOutList.add(tTaskOut);

                outboundCount=outboundCount.add(task.getReceiveCount());
            }
            if(outboundCount.compareTo(tOutDeliveryDetail.getPredictReceiveCount())>0){
                throw new ServiceException("实际拣货数量不可超过预计拣货数量！");
            }

            for ( TTaskOut t: tTaskOutList) {
                //更改库存
                TStockMain tStockMain=stockMainService.getOne(Wrappers.lambdaQuery(TStockMain.class)
                        .eq(TStockMain::getMaterialId,t.getMaterialId())
                        .eq(TStockMain::getDelFlag,Constants.NO)
                );
                if(tStockMain!=null){
//                tStockMain.setAvailableCount(tStockMain.getAvailableCount()-t.getActualCount());
                    tStockMain.setLibraryCount(tStockMain.getLibraryCount().subtract(t.getActualCount()));
                    if(tStockMain.getLibraryCount().compareTo(BigDecimal.ZERO) == -1){
                        throw new ServiceException("系统错误，库存不足！");
                    }
                    stockMainService.updateById(tStockMain);
                }
                tTaskOutMapper.insert(t);
            }
            //更改出库单状态
            if(outboundCount.equals(tOutDeliveryDetail.getPredictReceiveCount())){
                tOutDeliveryDetail.setNextFlag("1");
            }else{
                tOutDeliveryDetail.setNextFlag("2");
            }

            outDeliveryDetailService.updateById(tOutDeliveryDetail);
        }

        // 处理出库单状态
        Long deliveryDetailId = taskOutVO.get(0).getOutDeliveryDetailId();
        TOutDeliveryDetail outDeliveryDetail = tOutDeliveryDetailMapper.selectById(deliveryDetailId);

        TOutDelivery tOutDelivery = tOutDeliveryMapper.selectById(outDeliveryDetail.getOutDeliveryId());
        List<TOutDeliveryDetail> outDetailAlready = tOutDeliveryDetailMapper.selectList(new QueryWrapper<TOutDeliveryDetail>()
                .eq("status", 2)
                .eq("out_delivery_id", tOutDelivery.getId())
                .eq("del_flag", Constants.DEL_FLAG_NO));

        List<TOutDeliveryDetail> outDetailAll = tOutDeliveryDetailMapper.selectList(new QueryWrapper<TOutDeliveryDetail>()
                .eq("out_delivery_id", tOutDelivery.getId())
                .eq("del_flag", Constants.DEL_FLAG_NO));
        if (outDetailAlready.size() == outDetailAll.size()){
            tOutDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_COMPLETED);
        }else if (outDetailAlready.size() > 0){
            tOutDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_PART);
        }else {
            tOutDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_NOT);
        }

        tOutDeliveryMapper.updateById(tOutDelivery);

        return AjaxResult.success();
    }



    /**
     * 可视化出库
     *
     * @param tOutDeliveryVO 出库单
     * @return 结果
     */
    @Transactional
    public AjaxResult visualOutbound(TOutDeliveryVO tOutDeliveryVO)
    {
        TOutDelivery tOutDelivery = new TOutDelivery();
        tOutDelivery.setType(tOutDeliveryVO.getType());
        tOutDelivery.setNewLocal("1");
        tOutDelivery.setNextFlag("1");
        tOutDelivery.setStatus(Constants.INOUT_STATUS_PASS);
        tOutDelivery.setCode(codeConfigService.getCode(CodeEnum.CKJH.getCodeName()));
        tOutDelivery.setAuditor(SecurityUtils.getUsername());
        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDeliveryVO.gettOutDeliveryDetailList();
        if(tOutDeliveryDetailList==null||tOutDeliveryDetailList.size() == 0 ){
            return AjaxResult.error("请输入计划出库的物料");
        }
        List<TOutDeliveryDetail> collect = tOutDeliveryDetailList.stream()
                .distinct()
                .collect(Collectors.toList());
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            TMaterial material = materialService.getById(tOutDeliveryDetail.getMaterialId());
            if(material == null){
                return AjaxResult.error("请选择计划出库的物料");
            }
            if(tOutDeliveryDetail.getPredictCount()==null && tOutDeliveryDetail.getSmallPredictCount() == null
                    && tOutDeliveryDetail.getPredictCount().compareTo(BigDecimal.ZERO) == 0 && tOutDeliveryDetail.getSmallPredictCount().compareTo(BigDecimal.ZERO) == 0
            ){
                return AjaxResult.error("请输入计划出库数量");
            }
            //出库数量是否大于在库可用数量
            LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TStockMain::getMaterialId,material.getId());
            queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
            TStockMain stockMain = stockMainService.getOne(queryWrapper);
            //判断小件领取数量是否超过包装单位最大数据--张雅倩
            if(tOutDeliveryDetail.getSmallPredictCount() != null){
                TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId,material.getId()));
                if(unitConfig == null){
                    return AjaxResult.error("该物料不可小件领取");
                }
                if(unitConfig.getMinUnitId() == 0){
                    return AjaxResult.error("该物料不可小件领取");
                }
                if(tOutDeliveryDetail.getSmallPredictCount().compareTo(new BigDecimal(unitConfig.getCount())) != -1 ){
                    return AjaxResult.error("小件领取数已足整件领取");
                }
            }
            tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictCount());
            if(tOutDeliveryDetail.getPredictCount() == null){
                tOutDeliveryDetail.setPredictReceiveCount(BigDecimal.ONE);
            }
            if(tOutDeliveryDetail.getPredictCount() != null && tOutDeliveryDetail.getSmallPredictCount() != null){
                tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictReceiveCount().add(BigDecimal.ONE));
            }
            if(tOutDeliveryDetail.getPredictReceiveCount().compareTo(stockMain.getAvailableCount()) == 1){
                return AjaxResult.error("计划出库数量不可大于在库可用数量");
            }
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            tOutDeliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
            //更新该物料的在库可用数量
            stockMain.setAvailableCount(stockMain.getAvailableCount().subtract(tOutDeliveryDetail.getPredictReceiveCount()));
//            stockMain.setLibraryCount(stockMain.getLibraryCount()-tOutDeliveryDetail.getPredictReceiveCount());
            stockMainService.updateById(stockMain);
        }

        tOutDeliveryMapper.insert(tOutDelivery);
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            outDeliveryDetailService.save(tOutDeliveryDetail);

            TStock tStock=stockService.selectTStockById(tOutDeliveryDetail.getStockId());
            if(tStock==null){
                return  AjaxResult.error("库存不存在！");
            }

            TTray tTray= trayService.selectTTrayById(tStock.getTrayId());
            if(tTray==null){
                return AjaxResult.error("托盘不存在！");
            }
            TLocation location=locationService.getById(tStock.getLocationId());
            if(location==null){
                return  AjaxResult.error("库位不存在！");
            }
            TTaskOut tTaskOut = new TTaskOut();
            tTaskOut.setOutDeliveryId(tOutDelivery.getId());
            tTaskOut.setMaterialId(tOutDeliveryDetail.getMaterialId());
            tTaskOut.setPredictCount(tOutDeliveryDetail.getPredictReceiveCount());
            tTaskOut.setStockId(tStock.getId());
            tTaskOut.setLocationId(tStock.getLocationId());
            tTaskOut.setTrayId(tStock.getTrayId());
            tTaskOut.setOutDeliveryDetailId(tOutDeliveryDetail.getId());

            //将库位状态标记为已出库
            location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(location);

            //生成t_task_wcs
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setLocationId(tTaskOut.getLocationId());
            tTaskWcs.setTrayId(tTaskOut.getTrayId());
            tTaskWcs.setTrayCode(tTray.getCode());
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_OUT);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
            tTaskWcs.setDeliveryType(tOutDeliveryVO.getDeliveryType()==null?"0":tOutDeliveryVO.getDeliveryType());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcsMapper.insert(tTaskWcs);

            tTaskOut.setWcsId(tTaskWcs.getId());
            tTaskOutMapper.insert(tTaskOut);

            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
            tTaskWcsDetail.setTaskId(tTaskWcs.getId());
            tTaskWcsDetail.setOriginId(tTaskOut.getId());
            tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_OUT);
            taskWcsDetailMapper.insert(tTaskWcsDetail);
        }

        return AjaxResult.success();
    }

    /**
     * 应急出库
     * @param tOutDeliveryVO
     * @return
     */
    @Transactional
    public AjaxResult emergencyOutbound(TOutDeliveryVO tOutDeliveryVO){
        TOutDelivery tOutDelivery = new TOutDelivery();
        tOutDelivery.setType(tOutDeliveryVO.getType());
        tOutDelivery.setNewLocal("1");
        tOutDelivery.setNextFlag("1");
        tOutDelivery.setStatus(Constants.INOUT_STATUS_PASS);
        tOutDelivery.setCode(codeConfigService.getCode(CodeEnum.CKJH.getCodeName()));
        tOutDelivery.setAuditor(SecurityUtils.getUsername());
        List<TOutDeliveryDetail> tOutDeliveryDetailList = tOutDeliveryVO.gettOutDeliveryDetailList();
        if(tOutDeliveryDetailList==null||tOutDeliveryDetailList.size() == 0 ){
            return AjaxResult.error("请输入预计出库的物料");
        }
        if(tOutDeliveryVO.getPredictCount()==null && tOutDeliveryVO.getSmallPredictCount() == null
                && tOutDeliveryVO.getPredictCount().compareTo(BigDecimal.ZERO) == 0 &&
                tOutDeliveryVO.getSmallPredictCount().compareTo(BigDecimal.ZERO) == 0
        ){
            return AjaxResult.error("请输入预计出库数量");
        }
        TMaterial material = materialService.getById(tOutDeliveryVO.getMaterialId());
        if(material == null){
            return AjaxResult.error("请选择预计出库的物料");
        }
        List<TOutDeliveryDetail> collect = tOutDeliveryDetailList.stream()
                .distinct()
                .collect(Collectors.toList());
        //判断小件领取数量是否超过包装单位最大数据--张雅倩
        if(tOutDeliveryVO.getSmallPredictCount() != null){
            TUnitConfig unitConfig = unitConfigService.getOne(new LambdaQueryWrapper<TUnitConfig>().eq(TUnitConfig::getMaterialId,tOutDeliveryVO.getMaterialId()));
            if(unitConfig == null){
                return AjaxResult.error("该物料不可小件领取");
            }
            if(unitConfig.getMinUnitId() == 0){
                return AjaxResult.error("该物料不可小件领取");
            }
            if(tOutDeliveryVO.getSmallPredictCount().compareTo(new BigDecimal(unitConfig.getCount())) != -1){
                return AjaxResult.error("小件领取数已足整件领取");
            }
        }
        Set<Long> stockIds=collect.stream().map(TOutDeliveryDetail::getStockId).collect(Collectors.toSet());
        //获取选中库存的数据中总库存数量
        BigDecimal sumCount = stockService.getStockByMaterialList(tOutDeliveryVO.getMaterialId()).stream()
                .filter(tStock -> stockIds.contains(tStock.getId()))
                .map(TStock::getAvailableCount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal predictCont = tOutDeliveryVO.getPredictCount();
        if(tOutDeliveryVO.getSmallPredictCount() != null){
            predictCont = predictCont.add(tOutDeliveryVO.getSmallPredictCount());
        }
        if(predictCont.compareTo(sumCount)>1){
            return AjaxResult.error("预计出库数量不可大于在库可用数量");
        }
        //出库数量是否大于在库可用数量
        LambdaQueryWrapper<TStockMain> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TStockMain::getMaterialId,material.getId());
        queryWrapper.eq(TStockMain::getDelFlag,Constants.DEL_FLAG_NO);
        TStockMain stockMain = stockMainService.getOne(queryWrapper);
        if(predictCont.compareTo(stockMain.getAvailableCount()) == 1){
            return AjaxResult.error("预计出库数量不可大于在库可用数量");
        }
        //更新该物料的在库可用数量
        stockMain.setAvailableCount(stockMain.getAvailableCount().subtract(predictCont));
        //stockMain.setLibraryCount(stockMain.getLibraryCount()-tOutDeliveryVO.getPredictCount());
        stockMainService.updateById(stockMain);
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            tOutDeliveryDetail.setMaterialId(tOutDeliveryVO.getMaterialId());
            tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryVO.getPredictCount());
            tOutDeliveryDetail.setSmallPredictCount(tOutDeliveryVO.getSmallPredictCount());
            tOutDeliveryDetail.setPredictCount(tOutDeliveryVO.getPredictCount());
            if(tOutDeliveryDetail.getPredictCount() == null){
                tOutDeliveryDetail.setPredictReceiveCount(BigDecimal.ONE);
            }
            if(tOutDeliveryDetail.getPredictCount() != null && tOutDeliveryDetail.getSmallPredictCount() != null){
                tOutDeliveryDetail.setPredictReceiveCount(tOutDeliveryDetail.getPredictReceiveCount().add(BigDecimal.ONE));
            }
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            tOutDeliveryDetail.setNextFlag(Constants.INOUT_NEXT_FLAG_YES);
        }

        tOutDeliveryMapper.insert(tOutDelivery);
        for (TOutDeliveryDetail tOutDeliveryDetail : collect) {
            tOutDeliveryDetail.setOutDeliveryId(tOutDelivery.getId());
            outDeliveryDetailService.save(tOutDeliveryDetail);

            TStock tStock=stockService.selectTStockById(tOutDeliveryDetail.getStockId());
            if(tStock==null){
                return  AjaxResult.error("库存不存在！");
            }
            TTray tTray= trayService.selectTTrayById(tStock.getTrayId());
            if(tTray==null){
                return AjaxResult.error("托盘不存在！");
            }
            TLocation location=locationService.getById(tStock.getLocationId());
            if(location==null){
                return  AjaxResult.error("库位不存在！");
            }
            TTaskOut tTaskOut = new TTaskOut();
            tTaskOut.setOutDeliveryId(tOutDelivery.getId());
            tTaskOut.setMaterialId(tOutDeliveryVO.getMaterialId());
            tTaskOut.setPredictCount(tOutDeliveryDetail.getPredictReceiveCount());
            tTaskOut.setStockId(tStock.getId());
            tTaskOut.setLocationId(tStock.getLocationId());
            tTaskOut.setTrayId(tStock.getTrayId());
            tTaskOut.setOutDeliveryDetailId(tOutDeliveryDetail.getId());

            //将库位状态标记为已出库
            location.setGoodsAllocationStatus(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3);
            locationService.updateById(location);

            //生成t_task_wcs
            TTaskWcs tTaskWcs = new TTaskWcs();
            tTaskWcs.setLocationId(tTaskOut.getLocationId());
            tTaskWcs.setTrayId(tTaskOut.getTrayId());
            tTaskWcs.setTrayCode(tTray.getCode());
            tTaskWcs.setTaskType(Constants.WCS_TASK_TYPE_OUT);
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
            tTaskWcs.setDeliveryType(tOutDeliveryVO.getDeliveryType()==null?"0":tOutDeliveryVO.getDeliveryType());
            tTaskWcs.setTaskNo(codeConfigService.getCode(CodeEnum.CRW.getCodeName()));
            tTaskWcsMapper.insert(tTaskWcs);

            tTaskOut.setWcsId(tTaskWcs.getId());
            tTaskOutMapper.insert(tTaskOut);

            TTaskWcsDetail tTaskWcsDetail = new TTaskWcsDetail();
            tTaskWcsDetail.setTaskId(tTaskWcs.getId());
            tTaskWcsDetail.setOriginId(tTaskOut.getId());
            tTaskWcsDetail.setType(Constants.WCS_TASK_TYPE_OUT);
            taskWcsDetailMapper.insert(tTaskWcsDetail);
        }

        return AjaxResult.success();
    }

































    /**
     * 删除出库所有相关单据 -- 谨慎使用
     * @param tOutDelivery
     * @return
     */
    @Transactional
    @Override
    public AjaxResult delOutAll(TOutDelivery tOutDelivery) {
        if (StringUtils.isEmpty(tOutDelivery.getCode())){
            return AjaxResult.error("单号不可为空");
        }

        TOutDelivery delivery = tOutDeliveryMapper.selectOne(new QueryWrapper<TOutDelivery>().eq("code", tOutDelivery.getCode()));
        if (delivery == null){
            return AjaxResult.error("单据不存在");
        }

        List<TOutDeliveryDetail> outDeliveryDetails = tOutDeliveryDetailMapper.selectList(new QueryWrapper<TOutDeliveryDetail>().eq("out_delivery_id", delivery.getId()));

        List<TTaskOut> taskOutList = tTaskOutMapper.selectList(new QueryWrapper<TTaskOut>().eq("out_delivery_id", delivery.getId()));
        // 是否已下发硬件任务
        if (taskOutList.size() > 0){
            return AjaxResult.error("单据不可删除");
        }

        tOutDeliveryMapper.update(new TOutDelivery(),
                new UpdateWrapper<TOutDelivery>().eq("id",delivery.getId()).set("del_flag",Constants.DEL_FLAG_YES));
        List<Long> outDetailId = outDeliveryDetails.stream().map(TOutDeliveryDetail::getId).collect(Collectors.toList());
        tOutDeliveryDetailMapper.update(new TOutDeliveryDetail(),
                new UpdateWrapper<TOutDeliveryDetail>().in("id",outDetailId).set("del_flag",Constants.DEL_FLAG_YES));

        // 还原库存可用数量
        outDeliveryDetails.forEach(e ->{
            BigDecimal predictCount = e.getPredictCount();
            TStockMain tStockMain = stockMainService.getOne(new QueryWrapper<TStockMain>().eq("material_id", e.getMaterialId()));
            tStockMain.setAvailableCount(tStockMain.getAvailableCount().add(predictCount));
            stockMainService.updateById(tStockMain);
        });

        return AjaxResult.success();
    }



    /*
     * 物料出库扫描详情
     * */
    @Override
    public List<TOutboundScanningVO> selectChuKuList(TOutboundScanningDTO tOutboundScanningDTO) {
        return tOutDeliveryMapper.selectChuKuList(tOutboundScanningDTO);
    }
}
