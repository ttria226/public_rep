package com.xsrw.wms.base.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TGoodsAllocation;
import com.xsrw.wms.base.service.ITGoodsAllocationService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 接货位Controller
 *
 * @author zjj
 * @date 2023-06-12
 */
@RestController
@RequestMapping("/allocation")
public class TGoodsAllocationController extends BaseController
{
    @Autowired
    private ITGoodsAllocationService tGoodsAllocationService;

    /**
     * 查询接货位列表
     */
    @RequiresPermissions("base:allocation:list")
    @GetMapping("/list")
    public TableDataInfo list(TGoodsAllocation tGoodsAllocation)
    {
        startPage();
        List<TGoodsAllocation> list = tGoodsAllocationService.selectTGoodsAllocationList(tGoodsAllocation);
        return getDataTable(list);
    }

    /**
     * 导出接货位列表
     */
    @RequiresPermissions("base:allocation:export")
    @Log(title = "接货位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TGoodsAllocation tGoodsAllocation)
    {
        List<TGoodsAllocation> list = tGoodsAllocationService.selectTGoodsAllocationList(tGoodsAllocation);
        ExcelUtil<TGoodsAllocation> util = new ExcelUtil<TGoodsAllocation>(TGoodsAllocation.class);
        util.exportExcel(response, list, "接货位数据");
    }

    /**
     * 获取接货位详细信息
     */
    @RequiresPermissions("base:allocation:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tGoodsAllocationService.selectTGoodsAllocationById(id));
    }

    /**
     * 新增接货位
     */
    @RequiresPermissions("base:allocation:add")
    @Log(title = "接货位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TGoodsAllocation tGoodsAllocation)
    {
        return toAjax(tGoodsAllocationService.insertTGoodsAllocation(tGoodsAllocation));
    }

    /**
     * 修改接货位
     */
    @RequiresPermissions("base:allocation:edit")
    @Log(title = "接货位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TGoodsAllocation tGoodsAllocation)
    {
        return toAjax(tGoodsAllocationService.updateTGoodsAllocation(tGoodsAllocation));
    }

    /**
     * 删除接货位
     */
    @RequiresPermissions("base:allocation:remove")
    @Log(title = "接货位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tGoodsAllocationService.deleteTGoodsAllocationByIds(ids));
    }
}
