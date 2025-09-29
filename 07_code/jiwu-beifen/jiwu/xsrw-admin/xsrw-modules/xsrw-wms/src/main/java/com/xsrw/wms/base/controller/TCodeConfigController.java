package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.base.common.enums.CodeEnum;
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
import com.xsrw.wms.base.domain.TCodeConfig;
import com.xsrw.wms.base.service.ITCodeConfigService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 编码配置Controller
 *
 * @author wxr
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/codeConfig")
public class TCodeConfigController extends BaseController {
    @Autowired
    private ITCodeConfigService tTCodeConfigService;

    /**
     * 查询编码配置列表
     */
    @RequiresPermissions("wms:config:list")
    @GetMapping("/list")
    public TableDataInfo list(TCodeConfig tCodeConfig) {
        startPage();
        List<TCodeConfig> list = tTCodeConfigService.selectCodeConfigList(tCodeConfig);
        return getDataTable(list);
    }

    /**
     * 导出编码配置列表
     */
    @RequiresPermissions("wms:config:export")
    @Log(title = "编码配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCodeConfig tCodeConfig) {
        List<TCodeConfig> list = tTCodeConfigService.selectCodeConfigList(tCodeConfig);
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(e->{
                e.setTypeCode(CodeEnum.getValue(e.getTypeCode()));
            });
        }
        ExcelUtil<TCodeConfig> util = new ExcelUtil<>(TCodeConfig.class);
        util.exportExcel(response, list, "编码配置数据");
    }

    /**
     * 获取编码配置详细信息
     */
    @RequiresPermissions("wms:config:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tTCodeConfigService.selectCodeConfigById(id));
    }

    /**
     * 新增编码配置
     */
    @RequiresPermissions("wms:config:add")
    @Log(title = "编码配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCodeConfig tTCodeConfig) {
        return tTCodeConfigService.insertCodeConfig(tTCodeConfig);
    }

    /**
     * 修改编码配置
     */
    @RequiresPermissions("wms:config:edit")
    @Log(title = "编码配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCodeConfig tTCodeConfig) {
        return tTCodeConfigService.updateCodeConfig(tTCodeConfig);
    }

    /**
     * 删除编码配置
     */
    @RequiresPermissions("wms:config:remove")
    @Log(title = "编码配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tTCodeConfigService.deleteCodeConfigByIds(ids));
    }
}
