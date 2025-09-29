package com.xsrw.wms.stock.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.dto.StockDailySettlementDTO;
import com.xsrw.wms.stock.domain.vo.StockDailySettlementVO;
import com.xsrw.wms.stock.domain.vo.StockDealVO;
import com.xsrw.wms.stock.domain.vo.StockDetailLedgerVo;
import com.xsrw.wms.stock.service.ITStockDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库存操作记录Controller
 *
 * @author wxr
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/stock/detail")
public class TStockDetailController extends BaseController {
    @Autowired
    private ITStockDetailService tStockDetailService;
    @Autowired
    private ITMaterialService itMaterialService;

    /**
     * 查询库存操作记录列表
     */
    @RequiresPermissions("stock:detail:list")
    @GetMapping("/list")
    public TableDataInfo list(TStockDetail tStockDetail) {
        startPage();
        List<TStockDetail> list = tStockDetailService.selectTStockDetailList(tStockDetail);
        return getDataTable(list);
    }

    /**
     * 库存台账查询
     * @param stockDetailLedgerVo
     * @return
     */
//    @RequiresPermissions("stock:detail:stockDetailLedgerList")
    @GetMapping("/stockDetailLedgerList")
    public TableDataInfo stockDetailLedgerList(StockDetailLedgerVo stockDetailLedgerVo) {
        startPage();
        List<TStockDetail> stockDetailLedgerVos = tStockDetailService.selectTStockDetailListByLedger(stockDetailLedgerVo);
        return getDataTable(stockDetailLedgerVos);
    }

    /**
     * 导出库存操作记录列表
     */
    @RequiresPermissions("stock:detail:ledgerExport")
    @Log(title = "库存台账数据", businessType = BusinessType.EXPORT)
    @PostMapping("/ledgerExport")
    public void export(HttpServletResponse response, StockDetailLedgerVo stockDetailLedgerVo) {
        List<StockDetailLedgerVo> list = tStockDetailService.stockDetailLedgerList(stockDetailLedgerVo);
        ExcelUtil<StockDetailLedgerVo> util = new ExcelUtil<>(StockDetailLedgerVo.class);
        util.exportExcel(response, list, "库存台账数据");
    }

    /**
     * 获取库存操作记录详细信息
     */
    @RequiresPermissions("stock:detail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tStockDetailService.selectTStockDetailById(id));
    }

    /**
     * 新增库存操作记录
     */
    @RequiresPermissions("stock:detail:add")
    @Log(title = "库存操作记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStockDetail tStockDetail) {
        return toAjax(tStockDetailService.insertTStockDetail(tStockDetail));
    }

    /**
     * 修改库存操作记录
     */
    @RequiresPermissions("stock:detail:edit")
    @Log(title = "库存操作记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStockDetail tStockDetail) {
        return toAjax(tStockDetailService.updateTStockDetail(tStockDetail));
    }

    /**
     * 删除库存操作记录
     */
    @RequiresPermissions("stock:detail:remove")
    @Log(title = "库存操作记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tStockDetailService.deleteTStockDetailByIds(ids));
    }

    /**
     * 库存日结列表
     * @param request
     * @return
     */
    //    @RequiresPermissions("wms:storeDailySettlement:list")
    @GetMapping("/stockDailySettlement/list")
    public TableDataInfo stockDailySettlementList(StockDailySettlementDTO request) {
        startPage();
        List<StockDailySettlementVO> stockDailySettlementVOList = tStockDetailService.stockDailySettlementList(request);
        return getDataTable(stockDailySettlementVOList);
    }

    /**
     * 库存日结列表导出
     * @param response
     * @param request
     */
//    @RequiresPermissions("wms:storeDailySettlement:export")
    @PostMapping("/stockDailySettlement/export")
    public void stockDailySettlementExport(HttpServletResponse response, StockDailySettlementDTO request) {
        List<StockDailySettlementVO> stockDailySettlementList = tStockDetailService.stockDailySettlementList(request);
        ExcelUtil<StockDailySettlementVO> util = new ExcelUtil<>(StockDailySettlementVO.class);
        util.exportExcel(response, stockDailySettlementList, "sheet1");
    }

    /**
     * 库存交易列表
     * @param stockDealVO
     * @return
     */
    //    @RequiresPermissions("wms:stockDeal:list")
    @GetMapping("/stockDeal/list")
    public TableDataInfo stockDealList(StockDealVO stockDealVO) {
        startPage();
        List<StockDealVO> stockDealList = tStockDetailService.stockDealList(stockDealVO);
        return getDataTable(stockDealList);
    }

    /**
     * 库存交易列表导出
     * @param response
     */
//    @RequiresPermissions("wms:stockDeal:export")
    @PostMapping("/stockDeal/export")
    public void stockDealExport(HttpServletResponse response,StockDealVO stockDealVO) {
        List<StockDealVO> stockDealList = tStockDetailService.stockDealList(stockDealVO);
        ExcelUtil<StockDealVO> util = new ExcelUtil<>(StockDealVO.class);
        util.exportExcel(response, stockDealList, "sheet1");
    }
}
