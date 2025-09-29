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
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.vo.StockVo;
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
@RequestMapping("/stock")
public class TStockController extends BaseController {
    @Autowired
    private ITStockService tStockService;

    /**
     * 查询库存详情列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
//    @RequiresPermissions("stock:stock:list")
    @GetMapping("/list")
    public TableDataInfo list(StockVo stock) {
        startPage();
        List<StockVo> list = tStockService.selectTStockList(stock);
        return getDataTable(list);
    }

    /**
     * 导出库存详情列表
     */
    @RequiresPermissions("stock:stock:export")
    @Log(title = "库存详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockVo stock) {
        List<StockVo> list = tStockService.selectTStockList(stock);
        ExcelUtil<StockVo> util = new ExcelUtil<StockVo>(StockVo.class);
        util.exportExcel(response, list, "库存详情数据");
    }

    /**
     * 获取库存详情详细信息
     */
    @RequiresPermissions("stock:stock:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tStockService.selectTStockById(id));
    }

    /**
     * 新增库存详情
     */
    @RequiresPermissions("stock:stock:add")
    @Log(title = "库存详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStock tStock) {
        return toAjax(tStockService.insertTStock(tStock));
    }

    /**
     * 修改库存详情
     */
    @RequiresPermissions("stock:stock:edit")
    @Log(title = "库存详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStock tStock) {
        return toAjax(tStockService.updateTStock(tStock));
    }

    /**
     * 删除库存详情
     */
    @RequiresPermissions("stock:stock:remove")
    @Log(title = "库存详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tStockService.deleteTStockByIds(ids));
    }


    /**
     * 冻结
     *
     * @param ids
     * @return
     */
    @RequiresPermissions(value = {"stock:stock:updateFreeze", "stock:stock:Freezeupdate"}, logical = Logical.OR)
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @PostMapping("/updateFreezeByIds")
    public AjaxResult freezeByIds(@RequestParam("ids") List<Long> ids, @RequestParam("isFreeze") String isFreeze, @RequestParam("originType") String originType) {
        return tStockService.updateFreezeByIds(ids, isFreeze, originType);
    }

    /**
     * 批次总数量
     */
    @RequiresPermissions("stock:stock:listbatchsum")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @GetMapping("/listBatchSum")
    public TableDataInfo listBatchSum(StockVo stock) {
        startPage();
        List<StockVo> list = tStockService.listBatchSum(stock);
        return getDataTable(list);
    }

    /**
     * 库内移位添加数据
     *
     * @param id 库存id
     */
    @RequiresPermissions("stock:stock:shift")
    @GetMapping(value = "/shift/{id}")
    public AjaxResult shift(@PathVariable Long id) {
        return tStockService.shift(id);
    }


    /**
     * 查询托盘物料总数
     *
     * @param trayCode
     * @return
     */
    @GetMapping(value = "/getTrayNum")
    public AjaxResult getTrayNum(String trayCode) {
        return tStockService.getTrayNum(trayCode);
    }


    /**
     * 通过库位id查询库存数据
     *
     * @param locationId
     */
    @GetMapping(value = "/feign/queryLocation")
    public AjaxResult queryLocation(@RequestParam("locationId") Long locationId) {
        return tStockService.queryLocation(locationId);
    }


    /**
     * 查询库存
     *
     * @param materialId
     * @param batchCode
     */
    @GetMapping(value = "/feign/queryStock")
    public AjaxResult queryStock(@RequestParam("materialId") Long materialId, @RequestParam("batchCode") String batchCode) {
        return tStockService.queryStock(materialId, batchCode);
    }


//    /**
//     * @description: 自动拣货
//     * @param outDeliveryId
//     */
//    @RequiresPermissions("wms:outDelivery:autoPicking")
//    @Log(title = "自动拣货", businessType = BusinessType.UPDATE)
//    @GetMapping("/autoPickTask")
//    public AjaxResult autoPickTask(Long outDeliveryId){
//        return outDeliveryDetailService.autoPickTask(outDeliveryId);
//    }

//    /**
//     * 查询出库库存列表
//     */
//    @RequiresPermissions("wms:stock:listByOut")
//    @GetMapping("/listByOut")
//    public AjaxResult selectStockListByOut(StockVo stock){
//        List<StockVo> list = stockService.selectStockListByOut(stock);
//        return AjaxResult.success(list);
//    }

    @GetMapping("/locationFreeze")
    public AjaxResult locationFreeze(HttpServletRequest request) {
        String locationId = request.getParameter("locationId");
        if (StringUtils.isEmpty(locationId)) {
            return AjaxResult.error("库位编号不可以为空！");
        }
        String type = request.getParameter("type");
        if (StringUtils.isEmpty(type)) {
            return AjaxResult.error("操作类型不可以为空！");
        }
        return tStockService.unFreeLocation(locationId, type, "1");
    }

    /**
     * 获取库位列表
     *
     * @param tStock
     * @return
     */
    @GetMapping("/getStockByMaterialList")
    public AjaxResult getStockByMaterialList(TStock tStock) {
        return AjaxResult.success(tStockService.getStockByMaterialList(tStock.getMaterialId()));
    }

    /**
     * 获取库存信息列表
     *
     * @param stockVo
     * @return
     */
    @GetMapping("/getStockInfoList")
    public TableDataInfo getStockInfoList(StockVo stockVo) {
        startPage();
        List<StockVo> list = tStockService.getStockList(stockVo);
        return getDataTable(list);
    }

    /**
     * 获取库存信息列表
     *
     * @param stockListDTO
     * @return
     */
    @GetMapping("/stockList")
    public TableDataInfo stockList(StockListDTO stockListDTO) {
        startPage();
        return getDataTable(tStockService.stockList(stockListDTO));
    }
}
