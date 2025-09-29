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
import com.xsrw.wms.equipment.domain.DExpBase;
import com.xsrw.wms.equipment.service.IDExpBaseService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 保养/维修经验库Controller
 *
 * @author zjj
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/equipmentExpBase")
public class DExpBaseController extends BaseController
{
    @Autowired
    private IDExpBaseService dExpBaseService;

    /**
     * 查询保养/维修经验库列表
     */
    @RequiresPermissions("wms:equipmentExpBase:list")
    @GetMapping("/list")
    public TableDataInfo list(DExpBase dExpBase)
    {
        startPage();
        List<DExpBase> list = dExpBaseService.selectDExpBaseList(dExpBase);
        return getDataTable(list);
    }

    /**
     * 导出保养/维修经验库列表
     */
    @RequiresPermissions("wms:equipmentExpBase:export")
    @Log(title = "保养/维修经验库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DExpBase dExpBase)
    {
        List<DExpBase> list = dExpBaseService.selectDExpBaseList(dExpBase);
        ExcelUtil<DExpBase> util = new ExcelUtil<DExpBase>(DExpBase.class);
        util.exportExcel(response, list, "经验库数据");
    }

    /**
     * 获取保养/维修经验库详细信息
     */
    @RequiresPermissions("wms:equipmentExpBase:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dExpBaseService.selectDExpBaseById(id));
    }

    /**
     * 新增保养/维修经验库
     */
    @RequiresPermissions("wms:equipmentExpBase:add")
    @Log(title = "保养/维修经验库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DExpBase dExpBase)
    {
        return dExpBaseService.insertDExpBase(dExpBase);
    }

    /**
     * 修改保养/维修经验库
     */
    @RequiresPermissions("wms:equipmentExpBase:edit")
    @Log(title = "保养/维修经验库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DExpBase dExpBase)
    {
        return toAjax(dExpBaseService.updateDExpBase(dExpBase));
    }

    /**
     * 删除保养/维修经验库
     */
    @RequiresPermissions("wms:equipmentExpBase:remove")
    @Log(title = "保养/维修经验库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dExpBaseService.deleteDExpBaseByIds(ids));
    }
}
