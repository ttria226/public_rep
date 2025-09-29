package com.xsrw.wms.equipment.controller;

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
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;
import com.xsrw.wms.equipment.service.IWmsInspectionPlanDetailService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 巡检计划设备列Controller
 *
 * @author zjj
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/plandetail")
public class WmsInspectionPlanDetailController extends BaseController
{
    @Autowired
    private IWmsInspectionPlanDetailService wmsInspectionPlanDetailService;

    /**
     * 查询巡检计划设备列列表
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:list")
    @GetMapping("/list")
    public TableDataInfo list(WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        startPage();
        List<WmsInspectionPlanDetail> list = wmsInspectionPlanDetailService.selectWmsInspectionPlanDetailList(wmsInspectionPlanDetail);
        return getDataTable(list);
    }

    /**
     * 导出巡检计划设备列列表
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:export")
    @Log(title = "巡检计划设备列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        List<WmsInspectionPlanDetail> list = wmsInspectionPlanDetailService.selectWmsInspectionPlanDetailList(wmsInspectionPlanDetail);
        ExcelUtil<WmsInspectionPlanDetail> util = new ExcelUtil<WmsInspectionPlanDetail>(WmsInspectionPlanDetail.class);
        util.exportExcel(response, list, "巡检计划设备列数据");
    }

    /**
     * 获取巡检计划设备列详细信息
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsInspectionPlanDetailService.selectWmsInspectionPlanDetailById(id));
    }

    /**
     * 新增巡检计划设备列
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:add")
    @Log(title = "巡检计划设备列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        return toAjax(wmsInspectionPlanDetailService.insertWmsInspectionPlanDetail(wmsInspectionPlanDetail));
    }

    /**
     * 修改巡检计划设备列
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:edit")
    @Log(title = "巡检计划设备列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        return toAjax(wmsInspectionPlanDetailService.updateWmsInspectionPlanDetail(wmsInspectionPlanDetail));
    }

    /**
     * 删除巡检计划设备列
     */
    @RequiresPermissions("equipmentPlanDetail:plandetail:remove")
    @Log(title = "巡检计划设备列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsInspectionPlanDetailService.deleteWmsInspectionPlanDetailByIds(ids));
    }
}
