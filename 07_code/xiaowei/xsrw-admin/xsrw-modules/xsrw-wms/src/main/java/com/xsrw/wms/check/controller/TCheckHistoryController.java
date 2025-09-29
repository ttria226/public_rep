package com.xsrw.wms.check.controller;

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
import com.xsrw.wms.check.domain.TCheckHistory;
import com.xsrw.wms.check.service.ITCheckHistoryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 盘点历史记录Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/checkHistory")
public class TCheckHistoryController extends BaseController
{
    @Autowired
    private ITCheckHistoryService tCheckHistoryService;

    /**
     * 查询盘点历史记录列表
     */
    @RequiresPermissions("check:checkHistory:list")
    @GetMapping("/list")
    public TableDataInfo list(TCheckHistory tCheckHistory)
    {
        startPage();
        List<TCheckHistory> list = tCheckHistoryService.selectTCheckHistoryList(tCheckHistory);
        return getDataTable(list);
    }

    /**
     * 导出盘点历史记录列表
     */
    @RequiresPermissions("check:checkHistory:export")
    @Log(title = "盘点历史记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckHistory tCheckHistory)
    {
        List<TCheckHistory> list = tCheckHistoryService.selectTCheckHistoryList(tCheckHistory);
        ExcelUtil<TCheckHistory> util = new ExcelUtil<TCheckHistory>(TCheckHistory.class);
        util.exportExcel(response, list, "盘点历史记录数据");
    }

    /**
     * 获取盘点历史记录详细信息
     */
    @RequiresPermissions("check:checkHistory:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckHistoryService.selectTCheckHistoryById(id));
    }

    /**
     * 新增盘点历史记录
     */
    @RequiresPermissions("check:checkHistory:add")
    @Log(title = "盘点历史记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCheckHistory tCheckHistory)
    {
        return toAjax(tCheckHistoryService.insertTCheckHistory(tCheckHistory));
    }

    /**
     * 修改盘点历史记录
     */
    @RequiresPermissions("check:checkHistory:edit")
    @Log(title = "盘点历史记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckHistory tCheckHistory)
    {
        return toAjax(tCheckHistoryService.updateTCheckHistory(tCheckHistory));
    }

    /**
     * 删除盘点历史记录
     */
    @RequiresPermissions("check:checkHistory:remove")
    @Log(title = "盘点历史记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckHistoryService.deleteTCheckHistoryByIds(ids));
    }
}
