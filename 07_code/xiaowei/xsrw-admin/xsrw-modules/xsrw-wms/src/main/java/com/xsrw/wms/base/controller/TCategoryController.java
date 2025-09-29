package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TCategory;
import com.xsrw.wms.base.domain.vo.ExcelCategoryVO;
import com.xsrw.wms.base.service.ITCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料类别Controller
 *
 * @author lyx
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/category")
public class TCategoryController extends BaseController
{
    @Autowired
    private ITCategoryService tCategoryService;

    /**
     * 查询物料类别列表
     */
//    @RequiresPermissions("wms:category:list")
    @GetMapping("/list")
    public TableDataInfo list(TCategory tCategory)
    {
        startPage();
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        return getDataTable(list);
    }

    /**
     * 导出物料类别列表
     */
    @RequiresPermissions("wms:category:export")
    @Log(title = "物料类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCategory tCategory)
    {
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        ExcelUtil<TCategory> util = new ExcelUtil<TCategory>(TCategory.class);
        util.exportExcel(response, list, "物料类别数据");
    }

    /**
     * 导入物料信息 //todo 权限待添加
     * @param file
     * @return
     */
    @RequiresPermissions("wms:category:import")
    @Log(title = "物料类别", businessType = BusinessType.EXPORT)
    @PostMapping("/importData")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tCategoryService.importUnit(file);
    }

    /**
     * 下载导入模板信息 //todo 权限待添加
     * @param response
     */
    @RequiresPermissions("wms:category:exportdemo")
    @Log(title = "物料类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelCategoryVO> util = new ExcelUtil<ExcelCategoryVO>(ExcelCategoryVO.class);
        util.exportExcel(response,new ArrayList<>(), "物料类别数据");
    }

    /**
     * 获取物料类别详细信息
     */
    @RequiresPermissions("wms:category:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCategoryService.selectTCategoryById(id));
    }

    /**
     * 新增物料类别
     */
    @RequiresPermissions("wms:category:add")
    @Log(title = "物料类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCategory tCategory)
    {
        return tCategoryService.insertTCategory(tCategory);
    }

    /**
     * 修改物料类别
     */
    @RequiresPermissions("wms:category:edit")
    @Log(title = "物料类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCategory tCategory)
    {
        return tCategoryService.updateTCategory(tCategory);
    }

    /**
     * 删除物料类别
     */
    @RequiresPermissions("wms:category:remove")
    @Log(title = "物料类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCategoryService.deleteTCategoryByIds(ids));
    }
}
