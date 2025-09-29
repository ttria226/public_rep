package com.xsrw.wms.base.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.vo.TGoodShelfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TGoodShelf;
import com.xsrw.wms.base.service.ITGoodShelfService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 货架Controller
 *
 * @author wxr
 * @date 2023-06-01
 */
@RestController
@RequestMapping("/base/shelf")
public class TGoodShelfController extends BaseController {
    @Autowired
    private ITGoodShelfService tGoodShelfService;

    /**
     * 查询货架列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("base:shelf:list")
    @GetMapping("/list")
    public TableDataInfo list(TGoodShelf tGoodShelf) {
        startPage();
        List<TGoodShelfVO> list = tGoodShelfService.selectTGoodShelfList(tGoodShelf);
        return getDataTable(list);
    }

    /**
     * 导出货架列表
     */
    @RequiresPermissions("base:shelf:export")
    @Log(title = "货架", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TGoodShelf tGoodShelf) {
        List<TGoodShelfVO> list = tGoodShelfService.selectTGoodShelfList(tGoodShelf);
        ExcelUtil<TGoodShelfVO> util = new ExcelUtil<>(TGoodShelfVO.class);
        util.exportExcel(response, list, "货架数据");
    }

    /**
     * 获取货架详细信息
     */
    @RequiresPermissions("base:shelf:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tGoodShelfService.selectTGoodShelfById(id));
    }

    /**
     * 新增货架
     */
    @RequiresPermissions("base:shelf:add")
    @Log(title = "货架", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TGoodShelf tGoodShelf) {
        return toAjax(tGoodShelfService.insertTGoodShelf(tGoodShelf));
    }

    /**
     * 修改货架
     */
    @RequiresPermissions("base:shelf:edit")
    @Log(title = "货架", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TGoodShelf tGoodShelf) {
        return toAjax(tGoodShelfService.updateTGoodShelf(tGoodShelf));
    }

    /**
     * 删除货架
     */
    @RequiresPermissions("base:shelf:remove")
    @Log(title = "货架", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tGoodShelfService.deleteTGoodShelfByIds(ids));
    }


    /**
     * 获取选择列表
     * @param tGoodShelf
     * @return
     */
    @GetMapping("/getSelectList")
    public AjaxResult getSelectList(TGoodShelf tGoodShelf) {
        tGoodShelf.setStatus(Constants.NO);
        List<TGoodShelf> list = tGoodShelfService.selectTGoodShelfSimpleList(tGoodShelf);
        return AjaxResult.success(list);
    }

}
