package com.xsrw.wms.stock.controller;

/**
 * @author wxr
 * @date 2024/1/3 11:22
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.mapper.TTrayMapper;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.stock.domain.ErpWmsBatchCode;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理历史批次号问题
 */
@RestController
@RequestMapping("/test/batchCode")
public class TestBatchCodeController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(TestBatchCodeController.class);

    @Autowired
    private ITStockService tStockService;
    @Autowired
    private TTrayMapper tTrayMapper;

    @Autowired
    private TStockDetailMapper tStockDetailMapper;

    @Autowired
    private ITMaterialDetailService materialDetailService;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private RedisService redisService;

    @PostMapping("/test")
    public AjaxResult test() {
        return this.dealBatchCode2();
    }


    /**
     * 处理一对一数据
     *
     * @return
     */
    @Transactional
    public AjaxResult dealBatchCode()
    {
//        List<ErpWmsBatchCode> wmsList = tStockMapper.selectWmsList("1");
        List<ErpWmsBatchCode> wmsList = tStockMapper.selectWmsList("1");

        //type1:1对1
        for (ErpWmsBatchCode erpWmsBatchCode : wmsList) {
            String batchCode = erpWmsBatchCode.getWmsCode();
            String batchCodeErp = erpWmsBatchCode.getErpCode();
            Long materialId = erpWmsBatchCode.getMaterialId();
            QueryWrapper<TStock> queryStock = new QueryWrapper<>();
            queryStock.eq("del_flag", Constants.DEL_FLAG_NO);
            queryStock.eq("batch_code", batchCode);
            queryStock.eq("material_id", materialId);
            List<TStock> stockList = tStockService.list(queryStock);
            if (stockList == null || stockList.isEmpty()) {
                log.error("未查询到库存数据：batch_code:" + batchCode + "，wms_code:" + batchCodeErp);
                continue;
            }
            //更新库存stock的批次号
            UpdateWrapper<TStock> updateStock = new UpdateWrapper<>();
            updateStock.eq("del_flag", Constants.DEL_FLAG_NO);
            updateStock.eq("batch_code", batchCode);
            updateStock.eq("material_id", materialId);
            updateStock.set("batch_code", batchCodeErp);
            tStockService.update(updateStock);
            //更新库存stock_detail的批次号
            UpdateWrapper<TStockDetail> updateStockDetail = new UpdateWrapper<>();
            updateStockDetail.eq("del_flag", Constants.DEL_FLAG_NO);
            updateStockDetail.eq("batch_code", batchCode);
            updateStockDetail.set("batch_code", batchCodeErp);
            tStockDetailMapper.update(new TStockDetail(), updateStockDetail);
            //更新rfId
            QueryWrapper<TMaterialDetail> queryMaterialDetail = new QueryWrapper<>();
            queryMaterialDetail.select("rfid_head");
            queryMaterialDetail.eq("del_flag", Constants.DEL_FLAG_NO);
            queryMaterialDetail.eq("batch_code", batchCode);
            queryMaterialDetail.eq("material_id", materialId);
            queryMaterialDetail.in("status", "0", "1", "4");
            queryMaterialDetail.isNotNull("rfid_head");
            queryMaterialDetail.groupBy("rfid_head");
            List<TMaterialDetail> materialDetailList = materialDetailService.list(queryMaterialDetail);
            if (CollectionUtils.isNotEmpty(materialDetailList)) {
                String code = "wms:materialDetail:";
                List<String> rfIdList = materialDetailList.stream().map(TMaterialDetail::getRfidHead).collect(Collectors.toList());
                for (String rfId : rfIdList) {
                    TMaterialDetailRedisVO cacheObject = redisService.getCacheObject(code + rfId);
                    if (cacheObject != null) {
                        cacheObject.setBatchCode(batchCodeErp);
                        redisService.setCacheObject(code + rfId, cacheObject);
                    }
                }
            }
            //更新物料详情material_detail的批次号
            UpdateWrapper<TMaterialDetail> updateMaterialDetail = new UpdateWrapper<>();
            updateMaterialDetail.eq("del_flag", Constants.DEL_FLAG_NO);
            updateMaterialDetail.eq("batch_code", batchCode);
            updateMaterialDetail.eq("material_id", materialId);
            updateMaterialDetail.in("status", "0", "1", "4");
            updateMaterialDetail.set("batch_code", batchCodeErp);
            materialDetailService.update(updateMaterialDetail);
        }

        return AjaxResult.success(wmsList.size());
    }

    /**
     * 处理一对多数据
     * 数据需按，分割--还没测试
     *
     * @return
     */
    @Transactional
    public AjaxResult dealBatchCode2() {
        List<ErpWmsBatchCode> wmsList = tStockMapper.selectWmsList("2");
//        List<ErpWmsBatchCode> wmsList = new ArrayList<>();
//        ErpWmsBatchCode erp = new ErpWmsBatchCode();
//        erp.setErpCode("2307270055");
//        erp.setWmsCode("2311233312,2312019467");
//        erp.setMaterialId(21387L);
//        wmsList.add(erp);
        //type1:1对多
        for (ErpWmsBatchCode erpWmsBatchCode : wmsList) {
            String[] batchCodes = erpWmsBatchCode.getWmsCode().split(",");
            String batchCodeErp = erpWmsBatchCode.getErpCode();
            Long materialId = erpWmsBatchCode.getMaterialId();
            Long erpNum = erpWmsBatchCode.getErpNum();
            Long wmsNum = erpWmsBatchCode.getWmsNum();
            QueryWrapper<TStock> queryStock = new QueryWrapper<>();
            queryStock.eq("del_flag", Constants.DEL_FLAG_NO);
            queryStock.in("batch_code", batchCodes);
            queryStock.eq("material_id", materialId);
            queryStock.orderByDesc("id");
            List<TStock> stockList = tStockService.list(queryStock);
            if (stockList == null || stockList.isEmpty()) {
                log.error("未查询到库存数据：batch_code:" + batchCodes + "，wms_code:" + batchCodeErp);
                continue;
            }
            //更新库存stock的批次号
            UpdateWrapper<TStock> updateStock = new UpdateWrapper<>();
            updateStock.eq("del_flag", Constants.DEL_FLAG_NO);
            updateStock.in("batch_code", batchCodes);
            updateStock.eq("material_id", materialId);
            updateStock.set("del_flag", Constants.DEL_FLAG_YES);
            tStockService.update(updateStock);
            //根据载具分组，如果一个批次号在多个载具上，每个载具保留一条数据，数量为每个载具上多个批次号剩余总和
            LinkedHashMap<Long, List<TStock>> trayBatchCodeMap = stockList.stream().collect(Collectors.groupingBy(TStock::getTrayId, LinkedHashMap::new, Collectors.toList()));
            trayBatchCodeMap.forEach((key, value) -> {
                TStock tStockFirst = value.get(0);
                BigDecimal sumCount = value.stream().map(TStock::getCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                BigDecimal avaCount = value.stream().map(TStock::getAvailableCount).reduce(BigDecimal.ZERO,BigDecimal::add);
                //保存每个载具上面的第一个数据，总数为载具上所有批次总和
                TStock tStockUpdate = new TStock();
                tStockUpdate.setId(tStockFirst.getId());
                tStockUpdate.setBatchCode(batchCodeErp);
                tStockUpdate.setCount(sumCount);
                tStockUpdate.setAvailableCount(avaCount);
                tStockUpdate.setDelFlag(Constants.DEL_FLAG_NO);
                tStockService.updateById(tStockUpdate);
            });


            //更新库存stock_detail的批次号-不更新了

            //更新rfId
            QueryWrapper<TMaterialDetail> queryMaterialDetail = new QueryWrapper<>();
            queryMaterialDetail.select("rfid_head");
            queryMaterialDetail.eq("del_flag", Constants.DEL_FLAG_NO);
            queryMaterialDetail.in("batch_code", batchCodes);
            queryMaterialDetail.eq("material_id", materialId);
            queryMaterialDetail.isNotNull("rfid_head");
            queryMaterialDetail.in("status", "0", "1", "4");
            queryMaterialDetail.groupBy("rfid_head");
            List<TMaterialDetail> materialDetailList = materialDetailService.list(queryMaterialDetail);
            if (CollectionUtils.isNotEmpty(materialDetailList)) {
                String code = "wms:materialDetail:";
                List<String> rfIdList = materialDetailList.stream().map(TMaterialDetail::getRfidHead).collect(Collectors.toList());
                for (String rfId : rfIdList) {
                    TMaterialDetailRedisVO cacheObject = redisService.getCacheObject(code + rfId);
                    if (cacheObject != null) {
                        cacheObject.setBatchCode(batchCodeErp);
                        redisService.setCacheObject(code + rfId, cacheObject);
                    }
                }
            }
            //更新物料详情material_detail的批次号
            UpdateWrapper<TMaterialDetail> updateMaterialDetail = new UpdateWrapper<>();
            updateMaterialDetail.eq("del_flag", Constants.DEL_FLAG_NO);
            updateMaterialDetail.in("batch_code", batchCodes);
            updateMaterialDetail.eq("material_id", materialId);
            updateMaterialDetail.in("status", "0", "1", "4");
            updateMaterialDetail.set("batch_code", batchCodeErp);
            materialDetailService.update(updateMaterialDetail);
        }
        return AjaxResult.success(wmsList.size());
    }

}
