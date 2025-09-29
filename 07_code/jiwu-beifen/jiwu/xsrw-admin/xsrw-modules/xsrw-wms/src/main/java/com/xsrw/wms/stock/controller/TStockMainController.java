package com.xsrw.wms.stock.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.vo.StockMainVo;
import com.xsrw.wms.stock.service.ITStockMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库存查询Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/stock/stockMain")
public class TStockMainController extends BaseController
{
    @Autowired
    private ITStockMainService tStockMainService;

    /**
     * 查询库存查询列表
     */
//    @RequiresPermissions("stock:stockMain:list")
    @GetMapping("/list")
    public TableDataInfo list(StockMainVo stockMain)
    {
        startPage();
        List<StockMainVo> list = tStockMainService.selectTStockMainList(stockMain);
        return getDataTable(list);
    }

    /**
     * 导出库存查询列表
     */
    @RequiresPermissions("stock:stockMain:export")
    @Log(title = "库存查询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockMainVo stockMain)
    {
        List<StockMainVo> list = tStockMainService.selectTStockMainList(stockMain);
        ExcelUtil<StockMainVo> util = new ExcelUtil<StockMainVo>(StockMainVo.class);
        util.exportExcel(response, list, "库存查询数据");
    }

    /**
     * 获取库存查询详细信息
     */
    @RequiresPermissions("stock:stockMain:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tStockMainService.selectTStockMainById(id));
    }

    /**
     * 新增库存查询
     */
    @RequiresPermissions("stock:stockMain:add")
    @Log(title = "库存查询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStockMain tStockMain)
    {
        return toAjax(tStockMainService.insertTStockMain(tStockMain));
    }

    /**
     * 修改库存查询
     */
    @RequiresPermissions("stock:stockMain:edit")
    @Log(title = "库存查询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStockMain tStockMain)
    {
        return toAjax(tStockMainService.updateTStockMain(tStockMain));
    }

    /**
     * 删除库存查询
     */
    @RequiresPermissions("stock:stockMain:remove")
    @Log(title = "库存查询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tStockMainService.deleteTStockMainByIds(ids));
    }


    /**
     * cims根据物料ID获取库存信息
     */
    @GetMapping("/feign/getStockByMaterialIds")
    public AjaxResult getStockByMaterialIds(@RequestParam("materialIds") List<Long> materialIds) {

        return AjaxResult.success(tStockMainService.getStockByMaterialIds(materialIds));
    }
}
