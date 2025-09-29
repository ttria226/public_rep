package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.domain.vo.ExcelUnitVO;
import com.xsrw.wms.base.service.ITUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 单位Controller
 *
 * @author lyx
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/unit")
public class TUnitController extends BaseController
{
    @Autowired
    private ITUnitService tUnitService;

    /**
     * 查询单位列表
     */
//    @RequiresPermissions("wms:unit:list")
    @GetMapping("/list")
    public TableDataInfo list(TUnit tUnit)
    {
        startPage();
        List<TUnit> list = tUnitService.selectTUnitList(tUnit);
        return getDataTable(list);
    }

    /**
     * 导出单位列表
     */
    @RequiresPermissions("wms:unit:export")
    @Log(title = "单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TUnit tUnit)
    {
        List<TUnit> list = tUnitService.selectTUnitList(tUnit);
        ExcelUtil<TUnit> util = new ExcelUtil<TUnit>(TUnit.class);
        util.exportExcel(response, list, "单位数据");
    }

    /**
     * 导入单位列表 //todo 权限待添加
     */
    @RequiresPermissions("wms:unit:import")
    @Log(title = "单位", businessType = BusinessType.EXPORT)
    @PostMapping("/import")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tUnitService.importUnit(file);
    }

    /**
     * 下载单位导入模板 //todo 权限待添加
     * @param response
     */
    @RequiresPermissions("wms:unit:exportdemo")
    @Log(title = "单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelUnitVO> util = new ExcelUtil<ExcelUnitVO>(ExcelUnitVO.class);
        util.exportExcel(response,new ArrayList<>(), "单位数据");
    }

    /**
     * 获取单位详细信息
     */
    @RequiresPermissions("wms:unit:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tUnitService.selectTUnitById(id));
    }

    /**
     * 新增单位
     */
    @RequiresPermissions("wms:unit:add")
    @Log(title = "单位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TUnit tUnit)
    {
        return tUnitService.insertTUnit(tUnit);
    }

    /**
     * 修改单位
     */
    @RequiresPermissions("wms:unit:edit")
    @Log(title = "单位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TUnit tUnit)
    {
        return tUnitService.updateTUnit(tUnit);
    }

    /**
     * 删除单位
     */
    @RequiresPermissions("wms:unit:remove")
    @Log(title = "单位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tUnitService.deleteTUnitByIds(ids));
    }
}
