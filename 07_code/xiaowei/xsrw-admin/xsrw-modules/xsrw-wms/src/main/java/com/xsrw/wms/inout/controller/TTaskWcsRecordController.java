package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.inout.domain.dto.TTaskWcsRecordDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsRecordVO;
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
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.service.ITTaskWcsRecordService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 任务设备执行记录Controller
 *
 * @author wxr
 * @date 2023-10-23
 */
@RestController
@RequestMapping("/inout/wcsRecord")
public class TTaskWcsRecordController extends BaseController {
    @Autowired
    private ITTaskWcsRecordService tTaskWcsRecordService;

    /**
     * 查询任务设备执行记录列表
     */
    @RequiresPermissions("inout:wcsRecord:list")
    @GetMapping("/list")
    public TableDataInfo list(TTaskWcsRecord tTaskWcsRecord) {
        startPage();
        List<TTaskWcsRecordVO> list = tTaskWcsRecordService.selectTTaskWcsRecordList(tTaskWcsRecord);
        return getDataTable(list);
    }

    /**
     * 导出任务设备执行记录列表
     */
    @RequiresPermissions("inout:wcsRecord:export")
    @Log(title = "任务设备执行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTaskWcsRecord tTaskWcsRecord) {
        List<TTaskWcsRecordVO> list = tTaskWcsRecordService.selectTTaskWcsRecordList(tTaskWcsRecord);
        ExcelUtil<TTaskWcsRecordVO> util = new ExcelUtil<>(TTaskWcsRecordVO.class);
        util.exportExcel(response, list, "任务设备执行记录数据");
    }

    /**
     * 查询任务列表
     */
    @GetMapping("/getTaskNoList")
    public TableDataInfo getTaskNoList(TTaskWcsRecord tTaskWcsRecord) {
        startPage();
        List<TTaskWcsRecordVO> list = tTaskWcsRecordService.getTaskNoList(tTaskWcsRecord);
        return getDataTable(list);
    }

    /**
     * 查询载具的出库/回库任务执行记录
     */
    @GetMapping("/getListByTray")
    public TableDataInfo getListByTray(TTaskWcsRecordDTO tTaskWcsRecord) {
        startPage();
        List<TTaskWcsRecordVO> list = tTaskWcsRecordService.getListByTray(tTaskWcsRecord);
        return getDataTable(list);
    }

    /**
     * 获取任务设备执行记录详细信息
     */
    @RequiresPermissions("inout:wcsRecord:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tTaskWcsRecordService.selectTTaskWcsRecordById(id));
    }

    /**
     * 新增任务设备执行记录
     */
    @RequiresPermissions("inout:wcsRecord:add")
    @Log(title = "任务设备执行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskWcsRecord tTaskWcsRecord) {
        return toAjax(tTaskWcsRecordService.insertTTaskWcsRecord(tTaskWcsRecord));
    }

    /**
     * 修改任务设备执行记录
     */
    @RequiresPermissions("inout:wcsRecord:edit")
    @Log(title = "任务设备执行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskWcsRecord tTaskWcsRecord) {
        return toAjax(tTaskWcsRecordService.updateTTaskWcsRecord(tTaskWcsRecord));
    }

    /**
     * 删除任务设备执行记录
     */
    @RequiresPermissions("inout:wcsRecord:remove")
    @Log(title = "任务设备执行记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tTaskWcsRecordService.deleteTTaskWcsRecordByIds(ids));
    }
}
