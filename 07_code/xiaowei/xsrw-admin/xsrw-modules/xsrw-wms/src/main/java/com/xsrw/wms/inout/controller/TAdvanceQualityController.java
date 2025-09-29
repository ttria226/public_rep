package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.inout.domain.dto.TAdvanceQualityDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceQualityVO;
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
import com.xsrw.wms.inout.domain.TAdvanceQuality;
import com.xsrw.wms.inout.service.ITAdvanceQualityService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 入库质检单Controller
 *
 * @author wxr
 * @date 2023-06-05
 */
@RestController
@RequestMapping("/inout/quality")
public class TAdvanceQualityController extends BaseController {
    @Autowired
    private ITAdvanceQualityService tAdvanceQualityService;

    /**
     * 查询入库质检单列表
     */
    @RequiresPermissions("inout:quality:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvanceQualityDTO tAdvanceQuality) {
        startPage();
        List<TAdvanceQualityVO> list = tAdvanceQualityService.selectTAdvanceQualityList(tAdvanceQuality);
        return getDataTable(list);
    }

    /**
     * 导出入库质检单列表
     */
    @RequiresPermissions("inout:quality:export")
    @Log(title = "入库质检单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvanceQualityDTO tAdvanceQuality) {
        List<TAdvanceQualityVO> list = tAdvanceQualityService.selectTAdvanceQualityList(tAdvanceQuality);
        ExcelUtil<TAdvanceQualityVO> util = new ExcelUtil<>(TAdvanceQualityVO.class);
        util.exportExcel(response, list, "入库质检单数据");
    }

    /**
     * 获取入库质检单详细信息
     */
    @RequiresPermissions("inout:quality:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvanceQualityService.selectTAdvanceQualityById(id));
    }

    /**
     * 新增入库质检单
     */
    @RequiresPermissions("inout:quality:add")
    @Log(title = "入库质检单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAdvanceQuality tAdvanceQuality) {
        return tAdvanceQualityService.insertTAdvanceQuality(tAdvanceQuality);
    }

    /**
     * 修改入库质检单
     */
    @RequiresPermissions("inout:quality:edit")
    @Log(title = "入库质检单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAdvanceQuality tAdvanceQuality) {
        return toAjax(tAdvanceQualityService.updateTAdvanceQuality(tAdvanceQuality));
    }

    /**
     * 删除入库质检单
     */
    @RequiresPermissions("inout:quality:remove")
    @Log(title = "入库质检单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAdvanceQualityService.deleteTAdvanceQualityByIds(ids));
    }

    /**
     * 质检单状态更新
     *
     * @param tAdvanceQuality
     * @return
     */
    @Log(title = "质检单状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody TAdvanceQuality tAdvanceQuality) {
        if (tAdvanceQuality.getId() == null || StringUtils.isEmpty(tAdvanceQuality.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tAdvanceQualityService.updateById(tAdvanceQuality));
    }

}
