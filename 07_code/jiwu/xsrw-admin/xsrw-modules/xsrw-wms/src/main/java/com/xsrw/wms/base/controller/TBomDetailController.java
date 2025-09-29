package com.xsrw.wms.base.controller;

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
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.service.ITBomDetailService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * bom详情Controller
 *
 * @author zjj
 * @date 2023-06-10
 */
@RestController
@RequestMapping("/detail")
public class TBomDetailController extends BaseController
{
    @Autowired
    private ITBomDetailService tBomDetailService;

    /**
     * 查询bom详情列表
     */
    @RequiresPermissions("base:detail:list")
    @GetMapping("/list")
    public TableDataInfo list(TBomDetail tBomDetail)
    {
        startPage();
        List<TBomDetail> list = tBomDetailService.selectTBomDetailList(tBomDetail);
        return getDataTable(list);
    }

    /**
     * 导出bom详情列表
     */
    @RequiresPermissions("base:detail:export")
    @Log(title = "bom详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TBomDetail tBomDetail)
    {
        List<TBomDetail> list = tBomDetailService.selectTBomDetailList(tBomDetail);
        ExcelUtil<TBomDetail> util = new ExcelUtil<TBomDetail>(TBomDetail.class);
        util.exportExcel(response, list, "bom详情数据");
    }

    /**
     * 获取bom详情详细信息
     */
    @RequiresPermissions("base:detail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tBomDetailService.selectTBomDetailById(id));
    }

    /**
     * 新增bom详情
     */
    @RequiresPermissions("base:detail:add")
    @Log(title = "bom详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TBomDetail tBomDetail)
    {
        return toAjax(tBomDetailService.insertTBomDetail(tBomDetail));
    }

    /**
     * 修改bom详情
     */
    @RequiresPermissions("base:detail:edit")
    @Log(title = "bom详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TBomDetail tBomDetail)
    {
        return toAjax(tBomDetailService.updateTBomDetail(tBomDetail));
    }

    /**
     * 删除bom详情
     */
    @RequiresPermissions("base:detail:remove")
    @Log(title = "bom详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tBomDetailService.deleteTBomDetailByIds(ids));
    }
}
