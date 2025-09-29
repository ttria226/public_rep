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
import com.xsrw.wms.check.domain.TCheckAreaHistory;
import com.xsrw.wms.check.service.ITCheckAreaHistoryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 平库盘点提交历史Controller
 *
 * @author lyx
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/checkAreaHistory")
public class TCheckAreaHistoryController extends BaseController
{
    @Autowired
    private ITCheckAreaHistoryService tCheckAreaHistoryService;

    /**
     * 查询平库盘点提交历史列表
     */
    @RequiresPermissions("check:checkAreaHistory:list")
    @GetMapping("/list")
    public TableDataInfo list(TCheckAreaHistory tCheckAreaHistory)
    {
        startPage();
        List<TCheckAreaHistory> list = tCheckAreaHistoryService.selectTCheckAreaHistoryList(tCheckAreaHistory);
        return getDataTable(list);
    }

    /**
     * 导出平库盘点提交历史列表
     */
    @RequiresPermissions("check:checkAreaHistory:export")
    @Log(title = "平库盘点提交历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCheckAreaHistory tCheckAreaHistory)
    {
        List<TCheckAreaHistory> list = tCheckAreaHistoryService.selectTCheckAreaHistoryList(tCheckAreaHistory);
        ExcelUtil<TCheckAreaHistory> util = new ExcelUtil<TCheckAreaHistory>(TCheckAreaHistory.class);
        util.exportExcel(response, list, "平库盘点提交历史数据");
    }

    /**
     * 获取平库盘点提交历史详细信息
     */
    @RequiresPermissions("check:checkAreaHistory:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckAreaHistoryService.selectTCheckAreaHistoryById(id));
    }

    /**
     * 新增平库盘点提交历史
     */
    @RequiresPermissions("check:checkAreaHistory:add")
    @Log(title = "平库盘点提交历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCheckAreaHistory tCheckAreaHistory)
    {
        return toAjax(tCheckAreaHistoryService.insertTCheckAreaHistory(tCheckAreaHistory));
    }

    /**
     * 修改平库盘点提交历史
     */
    @RequiresPermissions("check:checkAreaHistory:edit")
    @Log(title = "平库盘点提交历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckAreaHistory tCheckAreaHistory)
    {
        return toAjax(tCheckAreaHistoryService.updateTCheckAreaHistory(tCheckAreaHistory));
    }

    /**
     * 删除平库盘点提交历史
     */
    @RequiresPermissions("check:checkAreaHistory:remove")
    @Log(title = "平库盘点提交历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckAreaHistoryService.deleteTCheckAreaHistoryByIds(ids));
    }
}
