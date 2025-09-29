package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.base.domain.TWarehouse;
import com.xsrw.wms.base.service.ITWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 仓库Controller
 *
 * @author tyk
 * @date 2023-07-12
 */
@RestController
@RequestMapping("/warehouse")
public class TWarehouseController extends BaseController
{
    @Autowired
    private ITWarehouseService tWarehouseService;

    /**
     * 查询仓库列表
     */
//    @RequiresPermissions("base:warehouse:list")
    @GetMapping("/list")
    public TableDataInfo list(TWarehouse tWarehouse)
    {
        startPage();
        List<TWarehouse> list = tWarehouseService.selectTWarehouseList(tWarehouse);
        return getDataTable(list);
    }

    /**
     * 导出仓库列表
     */
//    @RequiresPermissions("base:warehouse:export")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TWarehouse tWarehouse)
    {
        List<TWarehouse> list = tWarehouseService.selectTWarehouseList(tWarehouse);
        ExcelUtil<TWarehouse> util = new ExcelUtil<TWarehouse>(TWarehouse.class);
        util.exportExcel(response, list, "仓库数据");
    }

    /**
     * 获取仓库详细信息
     */
//    @RequiresPermissions("base:warehouse:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return tWarehouseService.selectTWarehouseById(id);
    }

    /**
     * 新增仓库
     */
//    @RequiresPermissions("base:warehouse:add")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TWarehouse tWarehouse)
    {
        return tWarehouseService.insertTWarehouse(tWarehouse);
    }

    /**
     * 修改仓库
     */
//    @RequiresPermissions("base:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TWarehouse tWarehouse)
    {
        return tWarehouseService.updateTWarehouse(tWarehouse);
    }

    /**
     * 删除仓库
     */
//    @RequiresPermissions("base:warehouse:remove")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return tWarehouseService.deleteTWarehouseByIds(ids);
    }

    /**
     * 修改状态(启用/禁用)
     */
//    @RequiresPermissions("base:warehouse:changeStatus")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody TWarehouse tWarehouse)
    {
        return tWarehouseService.changeStatus(tWarehouse);
    }
}
