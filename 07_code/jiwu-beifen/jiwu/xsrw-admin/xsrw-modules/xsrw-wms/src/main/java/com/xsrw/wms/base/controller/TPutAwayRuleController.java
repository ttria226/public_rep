package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.vo.TPutAwayRuleVO;
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
import com.xsrw.wms.base.domain.TPutAwayRule;
import com.xsrw.wms.base.service.ITPutAwayRuleService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 上架策略Controller
 *
 * @author wxr
 * @date 2023-05-06
 */
@RestController
@RequestMapping("/rule")
public class TPutAwayRuleController extends BaseController {
    @Autowired
    private ITPutAwayRuleService tPutAwayRuleService;

    /**
     * 查询上架策略列表
     */
    @RequiresPermissions("wms:rule:list")
    @GetMapping("/list")
    public TableDataInfo list(TPutAwayRule tPutAwayRule) {
        startPage();
        List<TPutAwayRule> list = tPutAwayRuleService.selectTPutAwayRuleList(tPutAwayRule);
        return getDataTable(list);
    }

    /**
     * 导出上架策略列表
     */
    @RequiresPermissions("wms:rule:export")
    @Log(title = "上架策略", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TPutAwayRule tPutAwayRule) {
        List<TPutAwayRule> list = tPutAwayRuleService.selectTPutAwayRuleList(tPutAwayRule);
        ExcelUtil<TPutAwayRule> util = new ExcelUtil<TPutAwayRule>(TPutAwayRule.class);
        util.exportExcel(response, list, "上架策略数据");
    }

    /**
     * 获取上架策略详细信息
     */
    @RequiresPermissions("wms:rule:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tPutAwayRuleService.selectTPutAwayRuleById(id));
    }

    /**
     * 新增上架策略
     */
    @RequiresPermissions("wms:rule:add")
    @Log(title = "上架策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TPutAwayRuleVO tPutAwayRule) {
        return toAjax(tPutAwayRuleService.insertTPutAwayRule(tPutAwayRule));
    }

    /**
     * 修改上架策略
     */
    @RequiresPermissions("wms:rule:edit")
    @Log(title = "上架策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TPutAwayRuleVO tPutAwayRule) {
        return toAjax(tPutAwayRuleService.updateTPutAwayRule(tPutAwayRule));
    }

    /**
     * 删除上架策略
     */
    @RequiresPermissions("wms:rule:remove")
    @Log(title = "上架策略", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tPutAwayRuleService.deleteTPutAwayRuleByIds(ids));
    }


}
