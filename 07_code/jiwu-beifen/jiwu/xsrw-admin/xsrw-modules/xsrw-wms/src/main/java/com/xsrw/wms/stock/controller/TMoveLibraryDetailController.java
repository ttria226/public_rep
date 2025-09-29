package com.xsrw.wms.stock.controller;

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
import com.xsrw.wms.stock.domain.TMoveLibraryDetail;
import com.xsrw.wms.stock.service.ITMoveLibraryDetailService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 移库详情Controller
 *
 * @author lyx
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/moveLibraryDetail")
public class TMoveLibraryDetailController extends BaseController
{
    @Autowired
    private ITMoveLibraryDetailService tMoveLibraryDetailService;

    /**
     * 查询移库详情列表
     */
    @RequiresPermissions("stock:moveLibraryDetail:list")
    @GetMapping("/list")
    public TableDataInfo list(TMoveLibraryDetail tMoveLibraryDetail)
    {
        startPage();
        List<TMoveLibraryDetail> list = tMoveLibraryDetailService.selectTMoveLibraryDetailList(tMoveLibraryDetail);
        return getDataTable(list);
    }

    /**
     * 导出移库详情列表
     */
    @RequiresPermissions("stock:moveLibraryDetail:export")
    @Log(title = "移库详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMoveLibraryDetail tMoveLibraryDetail)
    {
        List<TMoveLibraryDetail> list = tMoveLibraryDetailService.selectTMoveLibraryDetailList(tMoveLibraryDetail);
        ExcelUtil<TMoveLibraryDetail> util = new ExcelUtil<TMoveLibraryDetail>(TMoveLibraryDetail.class);
        util.exportExcel(response, list, "移库详情数据");
    }

    /**
     * 获取移库详情详细信息
     */
    @RequiresPermissions("stock:moveLibraryDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMoveLibraryDetailService.selectTMoveLibraryDetailById(id));
    }

    /**
     * 新增移库详情
     */
    @RequiresPermissions("stock:moveLibraryDetail:add")
    @Log(title = "移库详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMoveLibraryDetail tMoveLibraryDetail)
    {
        return toAjax(tMoveLibraryDetailService.insertTMoveLibraryDetail(tMoveLibraryDetail));
    }

    /**
     * 修改移库详情
     */
    @RequiresPermissions("stock:moveLibraryDetail:edit")
    @Log(title = "移库详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMoveLibraryDetail tMoveLibraryDetail)
    {
        return toAjax(tMoveLibraryDetailService.updateTMoveLibraryDetail(tMoveLibraryDetail));
    }

    /**
     * 删除移库详情
     */
    @RequiresPermissions("stock:moveLibraryDetail:remove")
    @Log(title = "移库详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMoveLibraryDetailService.deleteTMoveLibraryDetailByIds(ids));
    }
}
