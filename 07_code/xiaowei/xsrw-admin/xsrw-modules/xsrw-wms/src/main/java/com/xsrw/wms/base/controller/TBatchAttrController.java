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
import com.xsrw.wms.base.domain.TBatchAttr;
import com.xsrw.wms.base.service.ITBatchAttrService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 批次属性Controller
 *
 * @author lyx
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/attr")
public class TBatchAttrController extends BaseController
{
    @Autowired
    private ITBatchAttrService tBatchAttrService;

    /**
     * 查询批次属性列表
     */
//    @RequiresPermissions("wms:attr:list")
    @GetMapping("/list")
    public TableDataInfo list(TBatchAttr tBatchAttr)
    {
        startPage();
        List<TBatchAttr> list = tBatchAttrService.selectTBatchAttrList(tBatchAttr);
        return getDataTable(list);
    }

    /**
     * 导出批次属性列表
     */
    @RequiresPermissions("wms:attr:export")
    @Log(title = "批次属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TBatchAttr tBatchAttr)
    {
        List<TBatchAttr> list = tBatchAttrService.selectTBatchAttrList(tBatchAttr);
        ExcelUtil<TBatchAttr> util = new ExcelUtil<TBatchAttr>(TBatchAttr.class);
        util.exportExcel(response, list, "批次属性数据");
    }

    /**
     * 获取批次属性详细信息
     */
    @RequiresPermissions("wms:attr:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tBatchAttrService.selectTBatchAttrById(id));
    }

    /**
     * 新增批次属性
     */
    @RequiresPermissions("wms:attr:add")
    @Log(title = "批次属性", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TBatchAttr tBatchAttr)
    {
        return toAjax(tBatchAttrService.insertTBatchAttr(tBatchAttr));
    }

    /**
     * 修改批次属性
     */
    @RequiresPermissions("wms:attr:edit")
    @Log(title = "批次属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TBatchAttr tBatchAttr)
    {
        return toAjax(tBatchAttrService.updateTBatchAttr(tBatchAttr));
    }

    /**
     * 删除批次属性
     */
    @RequiresPermissions("wms:attr:remove")
    @Log(title = "批次属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tBatchAttrService.deleteTBatchAttrByIds(ids));
    }
}
