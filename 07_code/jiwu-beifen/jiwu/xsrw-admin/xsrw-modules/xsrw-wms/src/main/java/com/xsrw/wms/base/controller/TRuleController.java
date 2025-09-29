package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.StringUtils;
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
import com.xsrw.wms.base.domain.TRule;
import com.xsrw.wms.base.service.ITRuleService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 规则Controller
 *
 * @author wxr
 * @date 2023-06-12
 */
@RestController
@RequestMapping("/base/rule")
public class TRuleController extends BaseController {
    @Autowired
    private ITRuleService tRuleService;

    /**
     * 查询规则列表
     */
//    @RequiresPermissions("base:rule:list")
    @GetMapping("/list")
    public TableDataInfo list(TRule tRule) {
        if (StringUtils.isEmpty(tRule.getRuleModule())) {
            return new TableDataInfo();
        }
        startPage();
        List<TRule> list = tRuleService.selectTRuleList(tRule);
        return getDataTable(list);
    }

    /**
     * 导出规则列表
     */
//    @RequiresPermissions("base:rule:export")
    @Log(title = "规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRule tRule) {
        List<TRule> list = tRuleService.selectTRuleList(tRule);
        ExcelUtil<TRule> util = new ExcelUtil<TRule>(TRule.class);
        util.exportExcel(response, list, "规则数据");
    }

    /**
     * 获取规则详细信息
     */
//    @RequiresPermissions("base:rule:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tRuleService.selectTRuleById(id));
    }

    /**
     * 新增规则
     */
//    @RequiresPermissions("base:rule:add")
    @Log(title = "规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRule tRule) {
        return toAjax(tRuleService.insertTRule(tRule));
    }

    /**
     * 修改规则
     */
//    @RequiresPermissions("base:rule:edit")
    @Log(title = "规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRule tRule) {
        return toAjax(tRuleService.updateTRule(tRule));
    }

    /**
     * 删除规则
     */
//    @RequiresPermissions("base:rule:remove")
    @Log(title = "规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tRuleService.deleteTRuleByIds(ids));
    }

    /**
     * 获取规则启用状态
     *
     * @param module
     * @return
     */
    @GetMapping(value = "/getStatus/{module}")
    public AjaxResult getStatusByMoule(@PathVariable("module") String module) {
        return AjaxResult.success("msg",tRuleService.getStatusByMoule(module));
    }

}
