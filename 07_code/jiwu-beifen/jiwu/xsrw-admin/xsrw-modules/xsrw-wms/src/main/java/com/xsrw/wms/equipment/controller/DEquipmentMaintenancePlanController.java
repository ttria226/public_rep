package com.xsrw.wms.equipment.controller;

import java.text.ParseException;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenancePlan;
import com.xsrw.wms.equipment.service.IDEquipmentMaintenancePlanService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 设备保养计划Controller
 *
 * @author zjj
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/maintanenceplan")
public class DEquipmentMaintenancePlanController extends BaseController
{
    @Autowired
    private IDEquipmentMaintenancePlanService dEquipmentMaintenancePlanService;

    /**
     * 查询设备保养计划列表
     */
    @RequiresPermissions("wms:maintanenceplan:list")
    @GetMapping("/list")
    public TableDataInfo list(DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        startPage();
        dEquipmentMaintenancePlan.setType(1);
        List<DEquipmentMaintenancePlan> list = dEquipmentMaintenancePlanService.selectDEquipmentMaintenancePlanList(dEquipmentMaintenancePlan);
        return getDataTable(list);
    }

    /**
     * 查询设备维修计划列表
     */
    @RequiresPermissions("wms:maintanenceplan:repairList")
    @GetMapping("/repairList")
    public TableDataInfo repairList(DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        startPage();
        dEquipmentMaintenancePlan.setType(2);
        List<DEquipmentMaintenancePlan> list = dEquipmentMaintenancePlanService.selectDEquipmentMaintenancePlanList(dEquipmentMaintenancePlan);
        return getDataTable(list);
    }

    /**
     * 导出设备保养计划列表
     */
    @RequiresPermissions("wms:maintanenceplan:export")
    @Log(title = "设备保养计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        dEquipmentMaintenancePlan.setType(1);
        List<DEquipmentMaintenancePlan> list = dEquipmentMaintenancePlanService.selectDEquipmentMaintenancePlanList(dEquipmentMaintenancePlan);
        ExcelUtil<DEquipmentMaintenancePlan> util = new ExcelUtil<DEquipmentMaintenancePlan>(DEquipmentMaintenancePlan.class);
        util.exportExcel(response, list, "设备保养计划数据");
    }

    /**
     * 导出设备维修计划列表
     */
    @RequiresPermissions("wms:maintanenceplan:repairexport")
    @Log(title = "设备维修计划", businessType = BusinessType.EXPORT)
    @PostMapping("/repairexport")
    public void repairexport(HttpServletResponse response, DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        dEquipmentMaintenancePlan.setType(2);
        List<DEquipmentMaintenancePlan> list = dEquipmentMaintenancePlanService.selectDEquipmentMaintenancePlanList(dEquipmentMaintenancePlan);
        ExcelUtil<DEquipmentMaintenancePlan> util = new ExcelUtil<DEquipmentMaintenancePlan>(DEquipmentMaintenancePlan.class);
        util.exportExcel(response, list, "设备维修计划数据");
    }

    /**
     * 获取设备保养/维修计划详细信息
     */
    @RequiresPermissions("wms:maintanenceplan:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dEquipmentMaintenancePlanService.selectDEquipmentMaintenancePlanById(id));
    }

    /**
     * 新增设备保养计划
     */
    @RequiresPermissions("wms:maintanenceplan:add")
    @Log(title = "设备保养计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        dEquipmentMaintenancePlan.setType(1);
        return toAjax(dEquipmentMaintenancePlanService.insertDEquipmentMaintenancePlan(dEquipmentMaintenancePlan));
    }

    /**
     * 新增设备维修计划
     */
    @RequiresPermissions("wms:maintanenceplan:repairAdd")
    @Log(title = "设备维修计划", businessType = BusinessType.INSERT)
    @PostMapping("/repairAdd")
    public AjaxResult repairAdd(@RequestBody DEquipmentMaintenancePlan dEquipmentMaintenancePlan) throws ParseException {
        return dEquipmentMaintenancePlanService.insertRepairPlan(dEquipmentMaintenancePlan);
    }

    /**
     * 保养计划启用
     */
    @RequiresPermissions("wms:maintenanceDay:add")
    @Log(title = "保养计划启用", businessType = BusinessType.INSERT)
    @PutMapping("/planStart/{id}")
    public AjaxResult planStart(@PathVariable("id") Long id) throws Exception {
        AjaxResult ajaxResult = dEquipmentMaintenancePlanService.startPlan(id);
        return ajaxResult;
    }

    /**
     * 保养计划作废
     */
    @RequiresPermissions("wms:maintanenceplan:planCancel")
    @Log(title = "保养计划作废", businessType = BusinessType.OTHER)
    @PutMapping("/planCancel/{id}")
    public AjaxResult planCancel(@PathVariable("id") Long id) throws Exception {
        AjaxResult ajaxResult = dEquipmentMaintenancePlanService.cancelPlan(id);
        return ajaxResult;
    }

    /**
     * 修改设备保养/维修计划
     */
    @RequiresPermissions("wms:maintanenceplan:edit")
    @Log(title = "设备保养计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DEquipmentMaintenancePlan dEquipmentMaintenancePlan)
    {
        return dEquipmentMaintenancePlanService.updateDEquipmentMaintenancePlan(dEquipmentMaintenancePlan);
    }

    /**
     * 删除设备保养/维修计划
     */
    @RequiresPermissions("wms:maintanenceplan:remove")
    @Log(title = "设备保养计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dEquipmentMaintenancePlanService.deleteDEquipmentMaintenancePlanByIds(ids));
    }

}
