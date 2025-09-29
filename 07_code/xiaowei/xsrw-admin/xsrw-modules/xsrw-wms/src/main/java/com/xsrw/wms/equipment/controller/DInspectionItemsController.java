package com.xsrw.wms.equipment.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
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
import com.xsrw.wms.equipment.domain.DInspectionItems;
import com.xsrw.wms.equipment.service.IDInspectionItemsService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 巡检标准Controller
 *
 * @author zjj
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/items")
public class DInspectionItemsController extends BaseController
{
    @Autowired
    private IDInspectionItemsService dInspectionItemsService;

    /**
     * 查询巡检标准列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("equipmentItems:items:list")
    @GetMapping("/list")
    public TableDataInfo list(DInspectionItems dInspectionItems)
    {
        startPage();
        List<DInspectionItems> list = dInspectionItemsService.selectDInspectionItemsList(dInspectionItems);
        return getDataTable(list);
    }

    /**
     * 导出巡检标准列表
     */
    @RequiresPermissions("equipmentItems:items:export")
    @Log(title = "巡检标准", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DInspectionItems dInspectionItems)
    {
        List<DInspectionItems> list = dInspectionItemsService.selectDInspectionItemsList(dInspectionItems);
        ExcelUtil<DInspectionItems> util = new ExcelUtil<DInspectionItems>(DInspectionItems.class);
        util.exportExcel(response, list, "巡检标准数据");
    }

    /**
     * 获取巡检标准详细信息
     */
    @RequiresPermissions("equipmentItems:items:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dInspectionItemsService.selectDInspectionItemsById(id));
    }

    /**
     * 新增巡检标准
     */
    @RequiresPermissions("equipmentItems:items:add")
    @Log(title = "巡检标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DInspectionItems dInspectionItems)
    {
        return dInspectionItemsService.insertDInspectionItems(dInspectionItems);
    }

    /**
     * 修改巡检标准
     */
    @RequiresPermissions("equipmentItems:items:edit")
    @Log(title = "巡检标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DInspectionItems dInspectionItems)
    {
        return toAjax(dInspectionItemsService.updateDInspectionItems(dInspectionItems));
    }

    /**
     * 删除巡检标准
     */
    @RequiresPermissions("equipmentItems:items:remove")
    @Log(title = "巡检标准", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dInspectionItemsService.deleteDInspectionItemsByIds(ids));
    }


}
