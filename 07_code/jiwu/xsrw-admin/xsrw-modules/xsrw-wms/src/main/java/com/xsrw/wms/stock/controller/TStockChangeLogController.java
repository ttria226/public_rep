package com.xsrw.wms.stock.controller;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.Logical;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.TStockChangeLog;
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.vo.StockChangeLogVo;
import com.xsrw.wms.stock.domain.vo.StockVo;
import com.xsrw.wms.stock.service.ITStockChangeLogService;
import com.xsrw.wms.stock.service.ITStockDetailService;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库存详情Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/stock/changelog")
public class TStockChangeLogController extends BaseController {
    @Autowired
    private ITStockChangeLogService itStockChangeLogService;
    @Autowired
    private ITStockService itStockService;

    /**
     * 库存调整记录
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/list")
    public TableDataInfo list(StockChangeLogVo stockChangeLogVo) {
        startPage();
        List<StockChangeLogVo> list = itStockChangeLogService.stockChangLogList(stockChangeLogVo.getMaterialDetailId());
        return getDataTable(list);
    }

    /**
     * 查询库存详情列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/stocklist")
    public TableDataInfo stocklist(StockVo stock) {
        startPage();
        List<StockChangeLogVo> list = itStockChangeLogService.stocklist(stock.getMaterialCode());
        return getDataTable(list);
    }

    /**
     * 调整库存
     * @return
     */
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PostMapping("/amendStockInfo")
    public AjaxResult  amendStockInfo(@RequestBody TStockChangeLog tStockChangeLog) {
        return itStockChangeLogService.updateStock(tStockChangeLog);
    }
}
