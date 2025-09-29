package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.report.domain.dto.EfficiencyStatisticsDTO;
import com.xsrw.wms.report.domain.dto.InOutStatementDTO;
import com.xsrw.wms.report.domain.dto.WorkStatisticsDTO;
import com.xsrw.wms.report.domain.vo.EfficiencyStatisticsVO;
import com.xsrw.wms.report.domain.vo.InOutStatementVO;
import com.xsrw.wms.report.domain.vo.WorkStatisticsListsVO;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.dto.StockDailySettlementDTO;
import com.xsrw.wms.stock.domain.vo.StockDailySettlementVO;
import com.xsrw.wms.stock.domain.vo.StockDealVO;
import com.xsrw.wms.stock.domain.vo.StockDetailLedgerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存操作记录Mapper接口
 *
 * @author wxr
 * @date 2023-05-11
 */
@Repository
public interface TStockDetailMapper extends BaseMapper<TStockDetail>
{

    /**
     * 查询库存操作记录列表
     *
     * @param tStockDetail 库存操作记录
     * @return 库存操作记录集合
     */
    public List<TStockDetail> selectTStockDetailList(TStockDetail tStockDetail);


    /**
     * 删除库存操作记录
     *
     * @param id 库存操作记录主键
     * @return 结果
     */
    public int deleteTStockDetailById(Long id);

    /**
     * 批量删除库存操作记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTStockDetailByIds(Long[] ids);

    /**
     * 根据条件查询可用数量
     * @param stockDetail
     * @return
     */
    BigDecimal selectTStockDetailCountParam(TStockDetail stockDetail);
    List<TStockDetail> selectTStockDetailListByLedger(StockDetailLedgerVo stockDetail);

    List<InOutStatementVO> selectListByParam(InOutStatementDTO inOutStatementDTO);

    List<EfficiencyStatisticsVO> selectEfficiencyStatistics(EfficiencyStatisticsDTO request);

    List<WorkStatisticsListsVO> selectWorkStatistics(WorkStatisticsDTO request);

    List<WorkStatisticsListsVO> selectInOutCount();

    List<StockDailySettlementVO> selectListByKey(StockDailySettlementDTO request);

    /**
     * 库存交易列表
     * @param stockDealVO
     * @return
     */
    List<StockDealVO> selectStockDetailList(StockDealVO stockDealVO);


    /**
     * 每月入库金额
     * @return
     */
    List<Map<String,Object>> inMonthNum();

    /**
     * 每月出库金额
     * @return
     */
    List<Map<String,Object>> outMonthNum();


}

