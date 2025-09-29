package com.xsrw.wms.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.dto.StockDailySettlementDTO;
import com.xsrw.wms.stock.domain.vo.StockDailySettlementVO;
import com.xsrw.wms.stock.domain.vo.StockDealVO;
import com.xsrw.wms.stock.domain.vo.StockDetailLedgerVo;

import java.util.List;

/**
 * 库存操作记录Service接口
 *
 * @author wxr
 * @date 2023-05-11
 */
public interface ITStockDetailService extends IService<TStockDetail>
{

    /**
     * 查询库存操作记录列表
     *
     * @param tStockDetail 库存操作记录
     * @return 库存操作记录集合
     */
    public List<TStockDetail> selectTStockDetailList(TStockDetail tStockDetail);

    /**
     * 查询库存操作记录
     *
     * @param id 库存操作记录主键
     * @return 库存操作记录
     */
    public TStockDetail selectTStockDetailById(Long id);

    /**
     * 新增库存操作记录
     *
     * @param tStockDetail 库存操作记录
     * @return 结果
     */
    public int insertTStockDetail(TStockDetail tStockDetail);

    /**
     * 修改库存操作记录
     *
     * @param tStockDetail 库存操作记录
     * @return 结果
     */
    public int updateTStockDetail(TStockDetail tStockDetail);

    /**
     * 批量删除库存操作记录
     *
     * @param ids 需要删除的库存操作记录主键集合
     * @return 结果
     */
    public int deleteTStockDetailByIds(Long[] ids);

    /**
     * 删除库存操作记录信息
     *
     * @param id 库存操作记录主键
     * @return 结果
     */
    public int deleteTStockDetailById(Long id);

    /**
     * 库存台账查询
     * @param stockDetailLedgerVo
     * @return
     */
    List<StockDetailLedgerVo> stockDetailLedgerList(StockDetailLedgerVo stockDetailLedgerVo);
    List<TStockDetail> selectTStockDetailListByLedger(StockDetailLedgerVo stockDetail);

    /**
     * 库存日结列表
     * @param request
     * @return
     */
    List<StockDailySettlementVO> stockDailySettlementList(StockDailySettlementDTO request);

    /**
     * 库存交易列表
     * @param stockDealVO
     * @return
     */
    List<StockDealVO> stockDealList(StockDealVO stockDealVO);
}
