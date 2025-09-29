package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.vo.TClassesVO;
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
import com.xsrw.wms.base.domain.TClasses;
import com.xsrw.wms.base.service.ITClassesService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 班次管理Controller
 *
 * @author wxr
 * @date 2023-06-12
 */
@RestController
@RequestMapping("/base/classes")
public class TClassesController extends BaseController {
    @Autowired
    private ITClassesService tClassesService;

    /**
     * 查询班次管理列表
     */
    @RequiresPermissions("base:classes:list")
    @GetMapping("/list")
    public TableDataInfo list(TClasses tClasses) {
        startPage();
        List<TClassesVO> list = tClassesService.selectTClassesList(tClasses);
        return getDataTable(list);
    }

    /**
     * 导出班次管理列表
     */
    @RequiresPermissions("base:classes:export")
    @Log(title = "班次管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TClasses tClasses) {
        List<TClassesVO> list = tClassesService.selectTClassesList(tClasses);
        ExcelUtil<TClassesVO> util = new ExcelUtil<>(TClassesVO.class);
        util.exportExcel(response, list, "班次管理数据");
    }

    /**
     * 获取班次管理详细信息
     */
    @RequiresPermissions("base:classes:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tClassesService.selectTClassesById(id));
    }

    /**
     * 新增班次管理
     */
    @RequiresPermissions("base:classes:add")
    @Log(title = "班次管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TClasses tClasses) {
        if (tClasses.getOperatorId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tClassesService.insertTClasses(tClasses);
    }

    /**
     * 修改班次管理
     */
    @RequiresPermissions("base:classes:edit")
    @Log(title = "班次管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TClasses tClasses) {
        if (tClasses.getId() == null || tClasses.getOperatorId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tClassesService.updateTClasses(tClasses);
    }

    /**
     * 删除班次管理
     */
    @RequiresPermissions("base:classes:remove")
    @Log(title = "班次管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tClassesService.deleteTClassesByIds(ids));
    }
}
