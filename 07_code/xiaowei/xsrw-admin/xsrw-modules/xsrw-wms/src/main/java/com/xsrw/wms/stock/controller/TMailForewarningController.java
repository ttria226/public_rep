package com.xsrw.wms.stock.controller;

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
import com.xsrw.wms.stock.domain.TMailForewarning;
import com.xsrw.wms.stock.service.ITMailForewarningService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 预警邮件配置Controller
 *
 * @author wxr
 * @date 2023-06-19
 */
@RestController
@RequestMapping("/stock/forewarning")
public class TMailForewarningController extends BaseController {
    @Autowired
    private ITMailForewarningService tMailForewarningService;

    /**
     * 查询预警邮件配置列表
     */
    @RequiresPermissions("stock:forewarning:list")
    @GetMapping("/list")
    public TableDataInfo list(TMailForewarning tMailForewarning) {
        startPage();
        List<TMailForewarning> list = tMailForewarningService.selectTMailForewarningList(tMailForewarning);
        return getDataTable(list);
    }

    /**
     * 导出预警邮件配置列表
     */
//    @RequiresPermissions("stock:forewarning:export")
    @Log(title = "预警邮件配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMailForewarning tMailForewarning) {
        List<TMailForewarning> list = tMailForewarningService.selectTMailForewarningList(tMailForewarning);
        ExcelUtil<TMailForewarning> util = new ExcelUtil<TMailForewarning>(TMailForewarning.class);
        util.exportExcel(response, list, "预警邮件配置数据");
    }

    /**
     * 获取预警邮件配置详细信息
     */
//    @RequiresPermissions("stock:forewarning:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tMailForewarningService.selectTMailForewarningById(id));
    }

    /**
     * 新增预警邮件配置
     */
//    @RequiresPermissions("stock:forewarning:add")
    @Log(title = "预警邮件配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMailForewarning tMailForewarning) {
        return toAjax(tMailForewarningService.insertTMailForewarning(tMailForewarning));
    }

    /**
     * 修改预警邮件配置
     */
//    @RequiresPermissions("stock:forewarning:edit")
    @Log(title = "预警邮件配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMailForewarning tMailForewarning) {
        return toAjax(tMailForewarningService.updateTMailForewarning(tMailForewarning));
    }

    /**
     * 删除预警邮件配置
     */
    @RequiresPermissions("stock:forewarning:remove")
    @Log(title = "预警邮件配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tMailForewarningService.deleteTMailForewarningByIds(ids));
    }

    /**
     * 根据类型发送邮件
     * @param tMailForewarning
     * @return
     */
    @PostMapping("/sendEmail")
    public AjaxResult sendEmail(@RequestBody TMailForewarning tMailForewarning) {
        if(StringUtils.isEmpty(tMailForewarning.getType())){
            return AjaxResult.error("参数不全");
        }
        return tMailForewarningService.sendEmail(tMailForewarning);
    }
}
