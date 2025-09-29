package com.xsrw.wms.equipment.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonElement;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.equipment.utils.GenerateNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.service.IDEquipmentMaintenanceDayService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;


/**
 * 保养工单Controller
 *
 * @author zjj
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/maintenanceDay")
public class DEquipmentMaintenanceDayController extends BaseController
{
    @Autowired
    private IDEquipmentMaintenanceDayService dEquipmentMaintenanceDayService;

    @Autowired
    private GenerateNumberUtil generateNumberUtil;

    /**
     * 查询保养工单列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("wms:maintenanceDay:list")
    @GetMapping("/list")
    public TableDataInfo list(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        startPage();
        dEquipmentMaintenanceDay.setType(1);
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        return getDataTable(list);
    }

    /**
     * 查询维修工单列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("wms:maintenanceDay:list")
    @GetMapping("/repairList")
    public TableDataInfo repairList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        startPage();
        dEquipmentMaintenanceDay.setType(2);
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        return getDataTable(list);
    }

    /**
     * 导出保养工单列表
     */
    @RequiresPermissions("wms:maintenanceDay:export")
    @Log(title = "保养工单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        ExcelUtil<DEquipmentMaintenanceDay> util = new ExcelUtil<DEquipmentMaintenanceDay>(DEquipmentMaintenanceDay.class);
        util.exportExcel(response, list, "保养工单数据");
    }

    /**
     * 获取保养工单详细信息
     */
    @RequiresPermissions("wms:maintenanceDay:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayById(id));
    }

    /**
     * 新增保养工单
     */
    @RequiresPermissions("wms:maintenanceDay:add")
    @Log(title = "保养工单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        dEquipmentMaintenanceDay.setType(1);//1：保养工单 2：维修工单
        dEquipmentMaintenanceDay.setSource(2);//来源 1：计划生成 2：手动新建
        dEquipmentMaintenanceDay.setDayNo(generateNumberUtil.generateNum("BY",4));
        return dEquipmentMaintenanceDayService.insertDEquipmentMaintenanceDay(dEquipmentMaintenanceDay);
    }

    /**
     * 新增维修工单
     */
    @RequiresPermissions("wms:maintenanceDay:add")
    @Log(title = "手动新增维修工单", businessType = BusinessType.INSERT)
    @PostMapping("/repairAdd")
    public AjaxResult repairAdd(@RequestBody DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        dEquipmentMaintenanceDay.setType(2);//1：保养工单 2：维修工单
        dEquipmentMaintenanceDay.setSource(2);//来源 1：计划生成 2：手动新建
        dEquipmentMaintenanceDay.setDayNo(generateNumberUtil.generateNum("WX",4));
        return dEquipmentMaintenanceDayService.insertDEquipmentMaintenanceDay(dEquipmentMaintenanceDay);
    }


    /**
     * 修改保养工单
     */
    @RequiresPermissions("wms:maintenanceDay:edit")
    @Log(title = "保养工单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        return toAjax(dEquipmentMaintenanceDayService.updateDEquipmentMaintenanceDay(dEquipmentMaintenanceDay));
    }

    /**
     * 保养工单-分派/重新分派
     */
    @RequiresPermissions("wms:maintenanceDay:assign")
    @Log(title = "保养工单-分派/重新分派", businessType = BusinessType.UPDATE)
    @PutMapping("/assign")
    public AjaxResult assign(@RequestBody JSONObject jsonObject)
    {
        Long id = jsonObject.getLong("id");
        Long companyId = jsonObject.getLong("companyId");
        Long executorId = jsonObject.getLong("executorId");
        String executorName = jsonObject.getString("executorName");
        return dEquipmentMaintenanceDayService.assign(Long.valueOf(id),companyId,executorId, executorName);
    }

    /**
     * 删除保养工单
     */
    @RequiresPermissions("wms:maintenanceDay:remove")
    @Log(title = "保养工单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dEquipmentMaintenanceDayService.deleteDEquipmentMaintenanceDayByIds(ids));
    }

    @RequiresPermissions("wms:maintenanceDay:dayCancel")
    @Log(title = "保养工单作废", businessType = BusinessType.OTHER)
    @PutMapping("/dayCancel/{id}")
    public AjaxResult dayCancel(@PathVariable("id") Long id) throws Exception {
        AjaxResult ajaxResult = dEquipmentMaintenanceDayService.cancelDay(id);
        return ajaxResult;
    }

    /**
     * 维修记录
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("wms:maintenanceDay:repairedList")
    @GetMapping("/repairedList")
    public TableDataInfo repairedList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        startPage();
        dEquipmentMaintenanceDay.setType(2);//维修工单
        dEquipmentMaintenanceDay.setStatus(3);//已完成
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        return getDataTable(list);
    }

    /**
     * 导出维修记录
     */
    @RequiresPermissions("wms:maintenanceDay:exportRepair")
    @Log(title = "保养工单", businessType = BusinessType.EXPORT)
    @PostMapping("/exportRepair")
    public void exportRepair(HttpServletResponse response, DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        dEquipmentMaintenanceDay.setType(2);//维修工单
        dEquipmentMaintenanceDay.setStatus(3);//已完成
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        ExcelUtil<DEquipmentMaintenanceDay> util = new ExcelUtil<DEquipmentMaintenanceDay>(DEquipmentMaintenanceDay.class);
        util.exportExcel(response, list, "保养工单数据");
    }

    /**
     * 保养记录
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("wms:maintenanceDay:repairedList")
    @GetMapping("/baoyangedList")
    public TableDataInfo baoyangedList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        startPage();
        dEquipmentMaintenanceDay.setType(1);//维修工单
        dEquipmentMaintenanceDay.setStatus(3);//已完成
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        return getDataTable(list);
    }
}
