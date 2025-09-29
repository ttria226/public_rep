package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.report.domain.dto.StoreStatisticsDTO;
import com.xsrw.wms.report.domain.dto.WareHouseAgeAnalyseDTO;
import com.xsrw.wms.report.domain.vo.DeadStockWarningVO;
import com.xsrw.wms.report.domain.vo.StoreStatisticsVO;
import com.xsrw.wms.report.domain.vo.ValidityWarningReportVO;
import com.xsrw.wms.report.domain.vo.WareHouseAgeAnalyseVO;
import com.xsrw.wms.stock.domain.ErpWmsBatchCode;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockChangeLog;
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.vo.StockVo;
import com.xsrw.wms.stock.domain.vo.TStockListVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存详情Mapper接口
 *
 * @author lyx
 * @date 2023-05-09
 */
@Repository
public interface TStockChangeLogMapper extends BaseMapper<TStockChangeLog>
{
    List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stockChangLogList(@Param("materialDetailId")Long materialDetailId);
    List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stocklist(@Param("materialCode") String materialCode);
}
