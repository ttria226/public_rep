package com.xsrw.wms.equipment.controller;

import java.text.ParseException;
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
import com.xsrw.wms.equipment.domain.DInspectionDayInfo;
import com.xsrw.wms.equipment.service.IDInspectionDayInfoService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 每日巡检记录Controller
 *
 * @author zjj
 * @date 2023-05-18
 */
@RestController
@RequestMapping("/dayInfo")
public class DInspectionDayInfoController extends BaseController
{
    @Autowired
    private IDInspectionDayInfoService dInspectionDayInfoService;

    /**
     * 查询每日巡检记录列表
     */
    @RequiresPermissions("wms:dayInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(DInspectionDayInfo dInspectionDayInfo)
    {
        startPage();
        List<DInspectionDayInfo> list = dInspectionDayInfoService.selectDInspectionDayInfoList(dInspectionDayInfo);
        return getDataTable(list);
    }

    /**
     * 导出每日巡检记录列表
     */
    @RequiresPermissions("wms:dayInfo:export")
    @Log(title = "每日巡检记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DInspectionDayInfo dInspectionDayInfo)
    {
        List<DInspectionDayInfo> list = dInspectionDayInfoService.selectDInspectionDayInfoList(dInspectionDayInfo);
        ExcelUtil<DInspectionDayInfo> util = new ExcelUtil<DInspectionDayInfo>(DInspectionDayInfo.class);
        util.exportExcel(response, list, "每日巡检记录数据");
    }

    /**
     * 获取每日巡检记录详细信息
     */
    @RequiresPermissions("wms:dayInfo:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dInspectionDayInfoService.selectDInspectionDayInfoById(id));
    }

    /**
     * 新增每日巡检记录
     */
    @RequiresPermissions("wms:dayInfo:add")
    @Log(title = "每日巡检记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DInspectionDayInfo dInspectionDayInfo) throws ParseException {
        return dInspectionDayInfoService.insertDInspectionDayInfo(dInspectionDayInfo);
    }

    /**
     * 修改每日巡检记录
     */
    @RequiresPermissions("wms:dayInfo:edit")
    @Log(title = "每日巡检记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DInspectionDayInfo dInspectionDayInfo)
    {
        return toAjax(dInspectionDayInfoService.updateDInspectionDayInfo(dInspectionDayInfo));
    }

    /**
     * 删除每日巡检记录
     */
    @RequiresPermissions("wms:dayInfo:remove")
    @Log(title = "每日巡检记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dInspectionDayInfoService.deleteDInspectionDayInfoByIds(ids));
    }
}
