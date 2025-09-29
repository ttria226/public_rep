package com.xsrw.wms.base.controller;

import java.util.List;
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
import com.xsrw.wms.base.domain.TOutStrategy;
import com.xsrw.wms.base.service.ITOutStrategyService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 拣货策略Controller
 *
 * @author wxr
 * @date 2023-05-06
 */
@RestController
@RequestMapping("/outStrategy")
public class TOutStrategyController extends BaseController {
    @Autowired
    private ITOutStrategyService tOutStrategyService;

    /**
     * 查询拣货策略列表
     */
    @RequiresPermissions("wms:strategy:list")
    @GetMapping("/list")
    public TableDataInfo list(TOutStrategy tOutStrategy) {
        startPage();
        List<TOutStrategy> list = tOutStrategyService.selectTOutStrategyList(tOutStrategy);
        return getDataTable(list);
    }

    /**
     * 导出拣货策略列表
     */
    @RequiresPermissions("wms:strategy:export")
    @Log(title = "拣货策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOutStrategy tOutStrategy) {
        List<TOutStrategy> list = tOutStrategyService.selectTOutStrategyList(tOutStrategy);
        ExcelUtil<TOutStrategy> util = new ExcelUtil<TOutStrategy>(TOutStrategy.class);
        util.exportExcel(response, list, "拣货策略数据");
    }

    /**
     * 获取拣货策略详细信息
     */
    @RequiresPermissions("wms:strategy:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOutStrategyService.selectTOutStrategyById(id));
    }

    /**
     * 新增拣货策略
     */
    @RequiresPermissions("wms:strategy:add")
    @Log(title = "拣货策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutStrategy tOutStrategy) {
        return toAjax(tOutStrategyService.insertTOutStrategy(tOutStrategy));
    }

    /**
     * 修改拣货策略
     */
    @RequiresPermissions("wms:strategy:edit")
    @Log(title = "拣货策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutStrategy tOutStrategy) {
        return toAjax(tOutStrategyService.updateTOutStrategy(tOutStrategy));
    }

    /**
     * 删除拣货策略
     */
    @RequiresPermissions("wms:strategy:remove")
    @Log(title = "拣货策略", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOutStrategyService.deleteTOutStrategyByIds(ids));
    }
}
