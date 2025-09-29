package com.xsrw.wms.equipment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.constant.SecurityConstants;
import com.xsrw.common.core.domain.R;
import com.xsrw.common.security.auth.AuthUtil;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.system.api.model.LoginUser;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.domain.DInspectionDayInfo;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;
import com.xsrw.wms.equipment.service.IDEquipmentMaintenanceDayService;
import com.xsrw.wms.equipment.service.IDInspectionDayInfoService;
import com.xsrw.wms.equipment.service.IWmsInspectionPlanDetailService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.equipment.domain.DInspectionPlanDay;
import com.xsrw.wms.equipment.service.IDInspectionPlanDayService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.wms.equipment.service.IDRepairReportService;
import com.xsrw.wms.equipment.domain.DRepairReport;

/**
 * 巡检记录Controller
 *
 * @author zjj
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/day")
public class DInspectionPlanDayController extends BaseController
{
    @Autowired
    private IDInspectionPlanDayService dInspectionPlanDayService;

    @Autowired
    private IWmsInspectionPlanDetailService wmsInspectionPlanDetailService;

    @Autowired
    private IDInspectionDayInfoService dInspectionDayInfoService;

    @Autowired
    private IDRepairReportService dRepairReportService;

    @Autowired
    private IDEquipmentMaintenanceDayService dEquipmentMaintenanceDayService;

    /**
     * 查询巡检记录列表
     */
    @RequiresPermissions("wms:day:list")
    @GetMapping("/list")
    public TableDataInfo list(DInspectionPlanDay dInspectionPlanDay)
    {
        startPage();
        List<DInspectionPlanDay> list = dInspectionPlanDayService.selectDInspectionPlanDayList(dInspectionPlanDay);
        return getDataTable(list);
    }

    /**
     * 导出巡检记录列表
     */
    @RequiresPermissions("wms:day:export")
    @Log(title = "巡检记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DInspectionPlanDay dInspectionPlanDay)
    {
        List<DInspectionPlanDay> list = dInspectionPlanDayService.selectDInspectionPlanDayList(dInspectionPlanDay);
        ExcelUtil<DInspectionPlanDay> util = new ExcelUtil<DInspectionPlanDay>(DInspectionPlanDay.class);
        util.exportExcel(response, list, "巡检记录数据");
    }

    /**
     * 获取巡检记录详细信息
     */
    @RequiresPermissions("wms:day:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dInspectionPlanDayService.selectDInspectionPlanDayById(id));
    }

    /**
     * 新增巡检记录
     */
    @RequiresPermissions("wms:day:add")
    @Log(title = "巡检记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DInspectionPlanDay dInspectionPlanDay)
    {
        return toAjax(dInspectionPlanDayService.insertDInspectionPlanDay(dInspectionPlanDay));
    }

    /**
     * 修改巡检记录
     */
    @RequiresPermissions("wms:day:edit")
    @Log(title = "巡检记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DInspectionPlanDay dInspectionPlanDay)
    {
        return toAjax(dInspectionPlanDayService.updateDInspectionPlanDay(dInspectionPlanDay));
    }

    /**
     * 删除巡检记录
     */
    @RequiresPermissions("wms:day:remove")
    @Log(title = "巡检记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dInspectionPlanDayService.deleteDInspectionPlanDayByIds(ids));
    }

    /**
     * 调班
     */
    @RequiresPermissions("wms:day:exchange")
    @Log(title = "巡检计划-调班", businessType = BusinessType.UPDATE)
    @PutMapping("/exchange")
    public AjaxResult exchange(@RequestBody JSONObject jsonObject)
    {
        String id = jsonObject.getString("id");
        String userid = jsonObject.getString("userid");
        String username = jsonObject.getString("username");
        String reason = jsonObject.getString("reason");
//        @RequestParam(name = "id") String id,
//        @RequestParam(name = "userid") String userid,
//        @RequestParam(name = "username") String username,
//        @RequestParam(name = "reason") String reason
        return dInspectionPlanDayService.exchange(id,userid,reason,username);
    }

    /**
     * App巡检待办列表
     */
//    @RequiresPermissions("wms:day:listApp")
    @GetMapping("/listApp")
    public TableDataInfo listApp()
    {
        startPage();
        List<DInspectionPlanDay> list = dInspectionPlanDayService.appPlanList();
        return getDataTable(list);
    }

    /**
     * App巡检待办详情
     */
    @GetMapping("/getInfoApp")
    public TableDataInfo getInfoApp(@RequestParam(name = "planid") Long planid,
                                    @RequestParam(name = "dayid") Long dayid)
    {
        startPage();
        List<WmsInspectionPlanDetail> list = dInspectionPlanDayService.getInfoApp(planid,dayid);
        return getDataTable(list);
    }


    /**
     * APP巡检办理
     */
    @Log(title = "巡检办理", businessType = BusinessType.INSERT)
    @PostMapping("/addDayInfo")
    public AjaxResult addDayInfo(@RequestBody DInspectionDayInfo dInspectionDayInfo) throws ParseException {
        return dInspectionDayInfoService.insertDInspectionDayInfo(dInspectionDayInfo);
    }

    /**
     * APP巡检办理-获取详情
     */
    @Log(title = "巡检办理-获取详情", businessType = BusinessType.INSERT)
    @GetMapping("/getDayInfo")
    public AjaxResult getDayInfo(DInspectionDayInfo dInspectionDayInfo) throws ParseException {
        return dInspectionDayInfoService.getDayInfo(dInspectionDayInfo);
    }
    /**
     * APP故障报修
     */
    @RequiresPermissions("wms:repairReport:add")
    @Log(title = "APP故障报修", businessType = BusinessType.INSERT)
    @PostMapping("/addRepair")
    public AjaxResult addRepair(@RequestBody DRepairReport dRepairReport)
    {
        dRepairReport.setSource(2);// 1新建 2 设备巡检
        return dRepairReportService.insertDRepairReport(dRepairReport);
    }


    /**
     * 查询保养工单待办列表
     */
    @GetMapping("/byListApp")
    public TableDataInfo byListApp()
    {
        startPage();
        Integer type = 1;
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.byListApp(type);
        return getDataTable(list);
    }

    /**
     * 保养/维修办理
     */
    @PutMapping("/startBy")
    public AjaxResult startBy(@RequestBody DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        return dEquipmentMaintenanceDayService.startBy(dEquipmentMaintenanceDay);
    }

    /**
     * 查询维修工单待办列表
     */
    @GetMapping("/wxListApp")
    public TableDataInfo wxListApp()
    {
        startPage();
        Integer type = 2;
        List<DEquipmentMaintenanceDay> list = dEquipmentMaintenanceDayService.byListApp(type);
        return getDataTable(list);
    }


}
