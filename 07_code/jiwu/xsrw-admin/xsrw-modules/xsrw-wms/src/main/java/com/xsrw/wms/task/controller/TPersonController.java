package com.xsrw.wms.task.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.task.domain.TPerson;
import com.xsrw.wms.task.service.ITPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 人员Controller
 *
 * @author tyk
 * @date 2023-06-29
 */
@RestController
@RequestMapping("/person")
public class TPersonController extends BaseController
{
    @Autowired
    private ITPersonService tPersonService;

    /**
     * 查询人员列表
     */
//    @RequiresPermissions("base:person:list")
    @GetMapping("/list")
    public TableDataInfo list(TPerson tPerson)
    {
        startPage();
        List<TPerson> list = tPersonService.selectTPersonList(tPerson);
        return getDataTable(list);
    }

    /**
     * 导出人员列表
     */
//    @RequiresPermissions("base:person:export")
    @Log(title = "人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TPerson tPerson)
    {
        List<TPerson> list = tPersonService.selectTPersonList(tPerson);
        ExcelUtil<TPerson> util = new ExcelUtil<TPerson>(TPerson.class);
        util.exportExcel(response, list, "人员数据");
    }

    /**
     * 获取人员详细信息
     */
//    @RequiresPermissions("base:person:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return tPersonService.selectTPersonById(id);
    }

    /**
     * 新增人员
     */
//    @RequiresPermissions("base:person:add")
    @Log(title = "人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TPerson tPerson)
    {
        return tPersonService.insertTPerson(tPerson);
    }

    /**
     * 修改人员
     */
//    @RequiresPermissions("base:person:edit")
    @Log(title = "人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TPerson tPerson)
    {
        return tPersonService.updateTPerson(tPerson);
    }

    /**
     * 删除人员
     */
//    @RequiresPermissions("base:person:remove")
    @Log(title = "人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tPersonService.deleteTPersonByIds(ids));
    }
}
