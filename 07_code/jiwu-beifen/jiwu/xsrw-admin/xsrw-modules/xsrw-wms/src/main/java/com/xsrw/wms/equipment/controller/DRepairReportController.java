package com.xsrw.wms.equipment.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
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
import com.xsrw.wms.equipment.domain.DRepairReport;
import com.xsrw.wms.equipment.service.IDRepairReportService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 故障报修Controller
 *
 * @author zjj
 * @date 2023-05-13
 */
@RestController
@RequestMapping("/repairReport")
public class DRepairReportController extends BaseController
{
    @Autowired
    private IDRepairReportService dRepairReportService;

    /**
     * 查询故障报修列表
     */
    @RequiresPermissions("wms:repairReport:list")
    @GetMapping("/list")
    public TableDataInfo list(DRepairReport dRepairReport)
    {
        startPage();
        List<DRepairReport> list = dRepairReportService.selectDRepairReportList(dRepairReport);
        return getDataTable(list);
    }

    /**
     * 导出故障报修列表
     */
    @RequiresPermissions("wms:repairReport:export")
    @Log(title = "故障报修", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DRepairReport dRepairReport)
    {
        List<DRepairReport> list = dRepairReportService.selectDRepairReportList(dRepairReport);
        ExcelUtil<DRepairReport> util = new ExcelUtil<DRepairReport>(DRepairReport.class);
        util.exportExcel(response, list, "故障报修数据");
    }

    /**
     * 获取故障报修详细信息
     */
    @RequiresPermissions("wms:repairReport:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dRepairReportService.selectDRepairReportById(id));
    }

    /**
     * 新增故障报修
     */
    @RequiresPermissions("wms:repairReport:add")
    @Log(title = "故障报修", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DRepairReport dRepairReport)
    {
        dRepairReport.setSource(1);// 1新建 2 设备巡检
        return dRepairReportService.insertDRepairReport(dRepairReport);
    }

    /**
     * 修改故障报修
     */
    @RequiresPermissions("wms:repairReport:edit")
    @Log(title = "故障报修", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DRepairReport dRepairReport)
    {
        return toAjax(dRepairReportService.updateDRepairReport(dRepairReport));
    }

    /**
     * 删除故障报修
     */
    @RequiresPermissions("wms:repairReport:remove")
    @Log(title = "故障报修", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dRepairReportService.deleteDRepairReportByIds(ids));
    }

    /**
     * 生成工单
     */
    @RequiresPermissions("wms:repairReport:createOrder")
    @Log(title = "生成工单", businessType = BusinessType.INSERT)
    @PostMapping("/createOrder")
    public AjaxResult createOrder(@RequestBody JSONObject jsonObject)
    {
        Long id = jsonObject.getLong("id");
        return dRepairReportService.createOrder(id);
    }
}
