package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TContactsUnit;
import com.xsrw.wms.base.service.ITContactsUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 供应商Controller
 *
 * @author lyx
 * @date 2023-05-06
 */
@RestController
@RequestMapping("/contacts/unit")
public class TContactsUnitController extends BaseController
{
    @Autowired
    private ITContactsUnitService tContactsUnitService;

    /**
     * 查询供应商列表
     */
//    @RequiresPermissions("wms:contacts/unit:list")
    @GetMapping("/list")
    public TableDataInfo list(TContactsUnit tContactsUnit)
    {
        startPage();
        List<TContactsUnit> list = tContactsUnitService.selectTContactsUnitList(tContactsUnit);
        return getDataTable(list);
    }

    /**
     * 导出供应商列表
     */
    @RequiresPermissions("wms:contacts/unit:export")
    @Log(title = "供应商", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TContactsUnit tContactsUnit)
    {
        List<TContactsUnit> list = tContactsUnitService.selectTContactsUnitList(tContactsUnit);
        ExcelUtil<TContactsUnit> util = new ExcelUtil<TContactsUnit>(TContactsUnit.class);
        util.exportExcel(response, list, "供应商数据");
    }

    /**
     * 获取供应商详细信息
     */
    @RequiresPermissions("wms:contacts/unit:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tContactsUnitService.selectTContactsUnitById(id));
    }

    /**
     * 新增供应商
     */
    @RequiresPermissions("wms:contacts/unit:add")
    @Log(title = "供应商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TContactsUnit tContactsUnit)
    {
        return tContactsUnitService.insertTContactsUnit(tContactsUnit);
    }

    /**
     * 修改供应商
     */
    @RequiresPermissions("wms:contacts/unit:edit")
    @Log(title = "供应商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TContactsUnit tContactsUnit)
    {
        return tContactsUnitService.updateTContactsUnit(tContactsUnit);
    }

    /**
     * 删除供应商
     */
    @RequiresPermissions("wms:contacts/unit:remove")
    @Log(title = "供应商", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tContactsUnitService.deleteTContactsUnitByIds(ids));
    }

    /**
     * 获取所有供应商
     */
//    @RequiresPermissions("wms:contacts/unit:list")
    @GetMapping("/getAll")
    public TableDataInfo getAll(TContactsUnit tContactsUnit)
    {
        List<TContactsUnit> list = tContactsUnitService.selectTContactsUnitList(tContactsUnit);
        return getDataTable(list);
    }
}
