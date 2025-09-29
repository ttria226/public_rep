package com.xsrw.wms.check.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.check.domain.TCheckResult;
import com.xsrw.wms.check.domain.dto.CheckResultDTO;
import com.xsrw.wms.check.domain.vo.CheckResultVO;
import com.xsrw.wms.check.service.ITCheckResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 盘点差异Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/checkResult")
public class TCheckResultController extends BaseController
{
    @Autowired
    private ITCheckResultService tCheckResultService;

    /**
     * 查询盘点差异列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("check:checkResult:list")
    @GetMapping("/list")
    public TableDataInfo list(CheckResultDTO checkResult)
    {
        startPage();
        List<CheckResultVO> list = tCheckResultService.getCheckResultList(checkResult);
        return getDataTable(list);
    }

    /**
     * 导出盘点差异列表
     */
    @RequiresPermissions("check:checkResult:export")
    @Log(title = "盘点差异", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckResultDTO checkResult)
    {
        List<CheckResultVO> list = tCheckResultService.selectTCheckResultList(checkResult);
        ExcelUtil<CheckResultVO> util = new ExcelUtil<>(CheckResultVO.class);
        util.exportExcel(response, list, "盘点差异数据");
    }

    /**
     * 获取盘点差异详细信息
     */
    @RequiresPermissions("check:checkResult:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckResultService.selectTCheckResultById(id));
    }

    /**
     * 新增盘点差异
     */
    @RequiresPermissions("check:checkResult:add")
    @Log(title = "盘点差异", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody List<TCheckResult> tCheckResultList)
    {
        return toAjax(tCheckResultService.insertTCheckResult(tCheckResultList));
    }

    /**
     * 修改盘点差异
     */
    @RequiresPermissions("check:checkResult:edit")
    @Log(title = "盘点差异", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCheckResult tCheckResult)
    {
        return toAjax(tCheckResultService.updateTCheckResult(tCheckResult));
    }

    /**
     * 删除盘点差异
     */
    @RequiresPermissions("check:checkResult:remove")
    @Log(title = "盘点差异", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckResultService.deleteTCheckResultByIds(ids));
    }

    /**
     * 盘差分析明细
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("check:checkResult:resultDetailList")
    @GetMapping("/resultDetailList")
    public TableDataInfo resultDetailList(CheckResultDTO checkResult)
    {
        startPage();
        List<CheckResultVO> list = tCheckResultService.getCheckResultDetail(checkResult);
        return getDataTable(list);
    }
}
