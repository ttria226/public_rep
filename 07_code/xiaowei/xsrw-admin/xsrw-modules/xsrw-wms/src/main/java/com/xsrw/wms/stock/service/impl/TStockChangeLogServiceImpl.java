package com.xsrw.wms.stock.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockChangeLog;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockChangeLogMapper;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockChangeLogService;

/**
 * 库存详情Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TStockChangeLogServiceImpl extends ServiceImpl<TStockChangeLogMapper, TStockChangeLog> implements ITStockChangeLogService {
    @Autowired
    private TStockChangeLogMapper tStockChangeLogMapper;
    @Autowired
    private TStockMapper tStockMapper;
    @Autowired
    private TStockMainMapper tStockMainMapper;
    @Autowired
    private TStockDetailMapper tStockDetailMapper;
    @Autowired
    private TMaterialDetailMapper tMaterialDetailMapper;

    @Override
    public List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stockChangLogList(Long materialDetailId){
        return tStockChangeLogMapper.stockChangLogList(materialDetailId);
    }

    @Override
    public List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stocklist(String materialCode){
        return tStockChangeLogMapper.stocklist(materialCode);
    }

    @Transactional
    @Override
    public AjaxResult updateStock(TStockChangeLog tStockChangeLog){
        TMaterialDetail tMaterialDetail = tMaterialDetailMapper.selectById(tStockChangeLog.getMaterialDetailId());
        if (tMaterialDetail==null){
            return AjaxResult.error("物料信息不存在");
        }
        if (!(tMaterialDetail.getRfidCount().compareTo(tStockChangeLog.getBeforeCount()) == 0)){
            return AjaxResult.error("库存信息原库存数量不正确");
        }
        BigDecimal beforeCount=new BigDecimal("0");  //物料详情操作前数量
        BigDecimal currCount=new BigDecimal("0");  //物料详情操作后当前数量
        beforeCount = tMaterialDetail.getRfidCount();  //调整前数量
        //修改物料详细数量
        if (tStockChangeLog.getChangeType().equals("0")){  //增加
            tMaterialDetail.setRfidCount(beforeCount.add(tStockChangeLog.getChangeNum()));
        }else if(tStockChangeLog.getChangeType().equals("1")){  //减少
            tMaterialDetail.setRfidCount(beforeCount.subtract(tStockChangeLog.getChangeNum()));
        }
        currCount = tMaterialDetail.getRfidCount();  //操作后当前数量
        tMaterialDetailMapper.updateById(tMaterialDetail);


        //修改库存
        QueryWrapper<TMaterialDetail> queryWrapper = new QueryWrapper<TMaterialDetail>();
        queryWrapper.eq("location_id",tMaterialDetail.getLocationId());
        queryWrapper.eq("material_id",tMaterialDetail.getMaterialId());
        queryWrapper.eq("batch_code",tMaterialDetail.getBatchCode());
        queryWrapper.eq("tray_id",tMaterialDetail.getTrayId());
        List<TMaterialDetail> tMaterialDetailList = tMaterialDetailMapper.selectList(queryWrapper);
        if (tMaterialDetailList.size() > 0){

            Double sumRfidCount = tMaterialDetailList.stream()
                    .mapToDouble(t -> t.getRfidCount().doubleValue())
                    .sum();

            QueryWrapper<TStock> queryWrapper1 = new QueryWrapper<TStock>();
            queryWrapper1.eq("location_id",tMaterialDetail.getLocationId());
            queryWrapper1.eq("material_id",tMaterialDetail.getMaterialId());
            queryWrapper1.eq("batch_code",tMaterialDetail.getBatchCode());
            queryWrapper1.eq("tray_id",tMaterialDetail.getTrayId());
            TStock tStock =tStockMapper.selectOne(queryWrapper1);
            if (tStock != null){
                if (tStockChangeLog.getChangeType().equals("0")) {  //增加
                    tStock.setCount(tStock.getCount().add(tStockChangeLog.getChangeNum()));  //在库数量
                    tStock.setAvailableCount(tStock.getAvailableCount().add(tStockChangeLog.getChangeNum()));  //可用数量
                }else if(tStockChangeLog.getChangeType().equals("1")) {  //减少
                    tStock.setCount(tStock.getCount().subtract(tStockChangeLog.getChangeNum()));  //在库数量
                    tStock.setAvailableCount(tStock.getAvailableCount().subtract(tStockChangeLog.getChangeNum()));  //可用数量
                }
                tStockMapper.updateById(tStock);
            }

            //更新 库存总览 表
            TStockMain tStockMain = tStockMainMapper.selectOne(new QueryWrapper<TStockMain>().eq("material_id",tStock.getMaterialId()));
            if (tStockMain != null){
                if (tStockChangeLog.getChangeType().equals("0")) {  //增加
                    tStockMain.setLibraryCount(tStockMain.getLibraryCount().add(tStockChangeLog.getChangeNum()));  //库存数量
                    tStockMain.setAvailableCount(tStockMain.getAvailableCount().add(tStockChangeLog.getChangeNum()));  //可用数量
                }else if (tStockChangeLog.getChangeType().equals("1")){  //减少
                    tStockMain.setLibraryCount(tStockMain.getLibraryCount().subtract(tStockChangeLog.getChangeNum()));  //库存数量
                    tStockMain.setAvailableCount(tStockMain.getAvailableCount().subtract(tStockChangeLog.getChangeNum()));  //可用数量
                }
                tStockMainMapper.updateById(tStockMain);
            }
        }
        //增加一条操作记录
        TStockDetail tStockDetail = new TStockDetail();
        tStockDetail.setLocationId(tMaterialDetail.getLocationId());
        tStockDetail.setMaterialId(tMaterialDetail.getMaterialId());
        tStockDetail.setBeforeCount(beforeCount);
        tStockDetail.setCurrentCount(currCount);
        tStockDetail.setType(Constants.WCS_TASK_TYPE_IN);
        tStockDetail.setBatchCode(tMaterialDetail.getBatchCode());
        tStockDetailMapper.insert(tStockDetail);

        //增加库存调整记录
        tStockChangeLogMapper.insert(tStockChangeLog);
        return AjaxResult.success("操作成功");
    }
}
