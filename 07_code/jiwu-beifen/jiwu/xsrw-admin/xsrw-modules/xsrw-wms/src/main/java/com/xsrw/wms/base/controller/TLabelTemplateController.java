package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.vo.LabelByMaterialVo;
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
import com.xsrw.wms.base.domain.TLabelTemplate;
import com.xsrw.wms.base.service.ITLabelTemplateService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 标签打印Controller
 *
 * @author wxr
 * @date 2023-05-06
 */
@RestController
@RequestMapping("/labelTemplate")
public class TLabelTemplateController extends BaseController {
    @Autowired
    private ITLabelTemplateService tLabelTemplateService;

    /**
     * 查询标签打印列表
     */
    @RequiresPermissions("wms:template:list")
    @GetMapping("/list")
    public TableDataInfo list(TLabelTemplate tLabelTemplate) {
        startPage();
        List<TLabelTemplate> list = tLabelTemplateService.selectTLabelTemplateList(tLabelTemplate);
        return getDataTable(list);
    }

    /**
     * 导出标签打印列表
     */
    @RequiresPermissions("wms:template:export")
    @Log(title = "标签打印", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TLabelTemplate tLabelTemplate) {
        List<TLabelTemplate> list = tLabelTemplateService.selectTLabelTemplateList(tLabelTemplate);
        ExcelUtil<TLabelTemplate> util = new ExcelUtil<TLabelTemplate>(TLabelTemplate.class);
        util.exportExcel(response, list, "标签打印数据");
    }

    /**
     * 获取标签打印详细信息
     */
    @RequiresPermissions("wms:template:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tLabelTemplateService.selectTLabelTemplateById(id));
    }

    /**
     * 新增标签打印
     */
    @RequiresPermissions("wms:template:add")
    @Log(title = "标签打印", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TLabelTemplate tLabelTemplate) {
        return toAjax(tLabelTemplateService.insertTLabelTemplate(tLabelTemplate));
    }

    /**
     * 修改标签打印
     */
    @RequiresPermissions("wms:template:edit")
    @Log(title = "标签打印", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TLabelTemplate tLabelTemplate) {
        return toAjax(tLabelTemplateService.updateTLabelTemplate(tLabelTemplate));
    }

    /**
     * 删除标签打印
     */
    @RequiresPermissions("wms:template:remove")
    @Log(title = "标签打印", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tLabelTemplateService.deleteTLabelTemplateByIds(ids));
    }

    /**
     * 标签选择物料列表
     * @param labelByMaterialVo
     * @return
     */
    @GetMapping("/getMaterialList")
    public TableDataInfo getMaterialList(LabelByMaterialVo labelByMaterialVo) {
        startPage();
        List<LabelByMaterialVo> list = tLabelTemplateService.getMaterialList(labelByMaterialVo);
        return getDataTable(list);
    }

    /**
     * 选择模板
     * @param tLabelTemplate
     * @return
     */
    @GetMapping("/selectList")
    public TableDataInfo selectList(TLabelTemplate tLabelTemplate) {
        List<TLabelTemplate> list = tLabelTemplateService.selectTLabelTemplateList(tLabelTemplate);
        return getDataTable(list);
    }

}
