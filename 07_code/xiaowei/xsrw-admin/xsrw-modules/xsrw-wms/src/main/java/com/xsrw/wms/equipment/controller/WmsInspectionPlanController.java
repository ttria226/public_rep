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
import com.xsrw.wms.equipment.domain.WmsInspectionPlan;
import com.xsrw.wms.equipment.service.IWmsInspectionPlanService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 巡检计划Controller
 *
 * @author zjj
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/plan")
public class WmsInspectionPlanController extends BaseController
{
    @Autowired
    private IWmsInspectionPlanService wmsInspectionPlanService;

    /**
     * 查询巡检计划列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("equipmentplan:plan:list")
    @GetMapping("/list")
    public TableDataInfo list(WmsInspectionPlan wmsInspectionPlan)
    {
        startPage();
        List<WmsInspectionPlan> list = wmsInspectionPlanService.selectWmsInspectionPlanList(wmsInspectionPlan);
        return getDataTable(list);
    }

    /**
     * 导出巡检计划列表
     */
    @RequiresPermissions("equipmentplan:plan:export")
    @Log(title = "巡检计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInspectionPlan wmsInspectionPlan)
    {
        List<WmsInspectionPlan> list = wmsInspectionPlanService.selectWmsInspectionPlanList(wmsInspectionPlan);
        ExcelUtil<WmsInspectionPlan> util = new ExcelUtil<WmsInspectionPlan>(WmsInspectionPlan.class);
        util.exportExcel(response, list, "巡检计划数据");
    }

    /**
     * 获取巡检计划详细信息
     */
    @RequiresPermissions("equipmentplan:plan:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsInspectionPlanService.selectWmsInspectionPlanById(id));
    }

    /**
     * 新增巡检计划
     */
    @RequiresPermissions("equipmentplan:plan:add")
    @Log(title = "巡检计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsInspectionPlan wmsInspectionPlan)
    {
        return wmsInspectionPlanService.insertWmsInspectionPlan(wmsInspectionPlan);
    }

    @Log(title = "巡检计划启用", businessType = BusinessType.INSERT)
    @PutMapping("/planStart/{id}")
    public AjaxResult planStart(@PathVariable("id") Long id) throws Exception {
        AjaxResult ajaxResult = wmsInspectionPlanService.startPlan(id);
        return ajaxResult;
    }

    @Log(title = "巡检计划启用", businessType = BusinessType.INSERT)
    @PutMapping("/planEnd/{id}")
    public AjaxResult planEnd(@PathVariable("id") Long id) throws Exception {
        AjaxResult ajaxResult = wmsInspectionPlanService.endPlan(id);
        return ajaxResult;
    }

    /**
     * 修改巡检计划
     */
    @RequiresPermissions("equipmentplan:plan:edit")
    @Log(title = "巡检计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsInspectionPlan wmsInspectionPlan)
    {
        return wmsInspectionPlanService.updateWmsInspectionPlan(wmsInspectionPlan);
    }

    /**
     * 删除巡检计划
     */
    @RequiresPermissions("equipmentplan:plan:remove")
    @Log(title = "巡检计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsInspectionPlanService.deleteWmsInspectionPlanByIds(ids));
    }


}
