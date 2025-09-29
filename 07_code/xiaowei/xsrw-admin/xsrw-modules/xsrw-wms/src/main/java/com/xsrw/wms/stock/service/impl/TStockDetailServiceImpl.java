package com.xsrw.wms.stock.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.dto.StockDailySettlementDTO;
import com.xsrw.wms.stock.domain.vo.StockDailySettlementVO;
import com.xsrw.wms.stock.domain.vo.StockDealVO;
import com.xsrw.wms.stock.domain.vo.StockDetailLedgerVo;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.service.ITStockDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存操作记录Service业务层处理
 *
 * @author wxr
 * @date 2023-05-11
 */
@Service
public class TStockDetailServiceImpl extends ServiceImpl<TStockDetailMapper, TStockDetail> implements ITStockDetailService
{
    @Autowired
    private TStockDetailMapper tStockDetailMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;

    @Autowired
    private TUnitMapper tUnitMapper;


    /**
     * 查询库存操作记录列表
     *
     * @param tStockDetail 库存操作记录
     * @return 库存操作记录
     */
    @Override
    public List<TStockDetail> selectTStockDetailList(TStockDetail tStockDetail)
    {
        return tStockDetailMapper.selectTStockDetailList(tStockDetail);
    }

    /**
     * 查询库存操作记录
     *
     * @param id 库存操作记录主键
     * @return 库存操作记录
     */
    @Override
    public TStockDetail selectTStockDetailById(Long id)
    {
        return tStockDetailMapper.selectById(id);
    }

    /**
     * 新增库存操作记录
     *
     * @param tStockDetail 库存操作记录
     * @return 结果
     */
    @Override
    public int insertTStockDetail(TStockDetail tStockDetail)
    {
        return tStockDetailMapper.insert(tStockDetail);
    }

    /**
     * 修改库存操作记录
     *
     * @param tStockDetail 库存操作记录
     * @return 结果
     */
    @Override
    public int updateTStockDetail(TStockDetail tStockDetail)
    {
        return tStockDetailMapper.updateById(tStockDetail);
    }


    /**
     * 批量删除库存操作记录
     *
     * @param ids 需要删除的库存操作记录主键
     * @return 结果
     */
    @Override
    public int deleteTStockDetailByIds(Long[] ids)
    {
        return tStockDetailMapper.deleteTStockDetailByIds(ids);
    }

    /**
     * 删除库存操作记录信息
     *
     * @param id 库存操作记录主键
     * @return 结果
     */
    @Override
    public int deleteTStockDetailById(Long id)
    {
        return tStockDetailMapper.deleteTStockDetailById(id);
    }

    @Override
    public List<StockDetailLedgerVo> stockDetailLedgerList(StockDetailLedgerVo stockDetailLedgerVo) {
        List<StockDetailLedgerVo> stockDetailLedgerVos = new ArrayList<>();
        //物料编号、名称不为空模糊查询物料ID
        List<Long> tMaterialIds = null;
        if (StringUtils.isNotNull(stockDetailLedgerVo.getMaterialCode()) || StringUtils.isNotNull(stockDetailLedgerVo.getMaterialName())){
            List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                    .like(StringUtils.isNotNull(stockDetailLedgerVo.getMaterialCode()),TMaterial::getCode, stockDetailLedgerVo.getMaterialCode())
                    .like(StringUtils.isNotNull(stockDetailLedgerVo.getMaterialName()),TMaterial::getName, stockDetailLedgerVo.getMaterialName())
                    .eq(TMaterial::getDelFlag, Constants.NO));
            tMaterialIds = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
        }
        //根据条件查询台账
        List<TStockDetail> tStockDetails = tStockDetailMapper.selectList(Wrappers.lambdaQuery(TStockDetail.class)
                .in(StringUtils.isNotEmpty(tMaterialIds),TStockDetail::getMaterialId,tMaterialIds)
                .like(StringUtils.isNotNull(stockDetailLedgerVo.getBatchCode()),TStockDetail::getBatchCode, stockDetailLedgerVo.getBatchCode())
                .eq(TStockDetail::getDelFlag,Constants.NO)
                .orderByDesc(TStockDetail::getCreateTime));
        if (StringUtils.isNotEmpty(tStockDetails) && tStockDetails.size() > 0){
            tStockDetails.forEach( e -> {
                StockDetailLedgerVo vo = new StockDetailLedgerVo();
                BeanUtils.copyProperties(e,vo);
                TMaterial material = tMaterialMapper.selectById(e.getMaterialId());
                //物料编号
                vo.setMaterialCode(material.getCode());
                //物料名称
                vo.setMaterialName(material.getName());
                //物料规格
                vo.setSpecifications(material.getSpecifications());
                //单位名称
                TUnit tUnit = tUnitMapper.selectById(material.getUnitId());
                vo.setUnitName(tUnit.getName());
                stockDetailLedgerVos.add(vo);
            });
        }
        return stockDetailLedgerVos;
    }
    @Override
    public List<TStockDetail> selectTStockDetailListByLedger(StockDetailLedgerVo stockDetail){
        //物料编号、名称不为空模糊查询物料ID
        List<Long> tMaterialIds = new ArrayList<>();
        if (StringUtils.isNotNull(stockDetail.getMaterialCode()) || StringUtils.isNotNull(stockDetail.getMaterialName())){
            List<TMaterial> tMaterials = tMaterialMapper.selectList(Wrappers.lambdaQuery(TMaterial.class)
                    .like(StringUtils.isNotNull(stockDetail.getMaterialCode()),TMaterial::getCode, stockDetail.getMaterialCode())
                    .like(StringUtils.isNotNull(stockDetail.getMaterialName()),TMaterial::getName, stockDetail.getMaterialName())
                    .eq(TMaterial::getDelFlag, Constants.NO));
            tMaterialIds = tMaterials.stream().map(TMaterial::getId).collect(Collectors.toList());
        }
        stockDetail.setMaterialIds(tMaterialIds);
        return  tStockDetailMapper.selectTStockDetailListByLedger(stockDetail);
    }

    /**
     * 库存日结列表
     * @param request
     * @return
     */
    @Override
    public List<StockDailySettlementVO> stockDailySettlementList(StockDailySettlementDTO request) {
        if (request.getCreateTime()!=null){
            request.setBeginDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 00:00:00",request.getCreateTime())));
            request.setEndDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 23:59:59",request.getCreateTime())));
        }
        return tStockDetailMapper.selectListByKey(request);
    }

    /**
     * 库存交易列表
     *
     * @return
     */
    @Override
    public List<StockDealVO> stockDealList(StockDealVO stockDealVO) {
        return tStockDetailMapper.selectStockDetailList(stockDealVO);
    }
}
