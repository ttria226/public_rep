package com.xsrw.wms.base.controller;


import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TUnitConfig;
import com.xsrw.wms.base.domain.vo.TUnitConfigVO;
import com.xsrw.wms.base.service.ITUnitConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 包装配置Controller
 *
 * @author lyx
 * @date 2023-05-06
 */
@RestController
@RequestMapping("/unit/config")
public class TUnitConfigController extends BaseController
{
    @Autowired
    private ITUnitConfigService tUnitConfigService;

    /**
     * 查询包装配置列表
     */
    @RequiresPermissions("wms:unit/config:list")
    @GetMapping("/list")
    public TableDataInfo list(TUnitConfig tUnitConfig)
    {
        startPage();
        List<TUnitConfigVO> list = tUnitConfigService.selectTUnitConfigList(tUnitConfig);
        return getDataTable(list);
    }

    /**
     * 导出包装配置列表
     */
    @RequiresPermissions("wms:unit/config:export")
    @Log(title = "包装配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TUnitConfig tUnitConfig)
    {
        List<TUnitConfigVO> list = tUnitConfigService.selectTUnitConfigList(tUnitConfig);
        ExcelUtil<TUnitConfigVO> util = new ExcelUtil<>(TUnitConfigVO.class);
        util.exportExcel(response, list, "包装配置数据");
    }

    /**
     * 获取包装配置详细信息
     */
    @RequiresPermissions("wms:unit/config:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tUnitConfigService.selectTUnitConfigById(id));
    }

    /**
     * 新增包装配置
     */
    @RequiresPermissions("wms:unit/config:add")
    @Log(title = "包装配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TUnitConfig tUnitConfig)
    {
        return tUnitConfigService.insertTUnitConfig(tUnitConfig);
    }

    /**
     * 修改包装配置
     */
    @RequiresPermissions("wms:unit/config:edit")
    @Log(title = "包装配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TUnitConfig tUnitConfig)
    {
        return tUnitConfigService.updateTUnitConfig(tUnitConfig);
    }

    /**
     * 删除包装配置
     */
    @RequiresPermissions("wms:unit/config:remove")
    @Log(title = "包装配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tUnitConfigService.deleteTUnitConfigByIds(ids));
    }
}
