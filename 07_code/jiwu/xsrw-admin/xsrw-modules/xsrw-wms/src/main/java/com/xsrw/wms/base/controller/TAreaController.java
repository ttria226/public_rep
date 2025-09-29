package com.xsrw.wms.base.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.base.domain.vo.ExcelAreaVO;
import com.xsrw.wms.base.domain.vo.TAreaVO;
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
import com.xsrw.wms.base.domain.TArea;
import com.xsrw.wms.base.service.ITAreaService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 区域Controller
 *
 * @author wxr
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/area")
public class TAreaController extends BaseController {
    @Autowired
    private ITAreaService tAreaService;

    /**
     * 查询区域列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("wms:area:list")
    @GetMapping("/list")
    public TableDataInfo list(TArea tArea) {
        startPage();
        List<TAreaVO> list = tAreaService.selectTAreaList(tArea);
        return getDataTable(list);
    }

    /**
     * 导出区域列表
     */
    @RequiresPermissions("wms:area:export")
    @Log(title = "区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TArea tArea) {
        List<TAreaVO> list = tAreaService.selectTAreaList(tArea);
        ExcelUtil<TAreaVO> util = new ExcelUtil<>(TAreaVO.class);
        util.exportExcel(response, list, "区域数据");
    }

    /**
     * 下载区域模板信息
     *
     * @param response
     */
    @RequiresPermissions("wms:area:exportdemo")
    @Log(title = "下载区域模板信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelAreaVO> util = new ExcelUtil<>(ExcelAreaVO.class);
        util.exportExcel(response, new ArrayList<>(), "区域数据");
    }

    /**
     * 导入区域信息
     *
     * @param file
     * @return
     */
    @RequiresPermissions("wms:area:importData")
    @Log(title = "导入区域信息", businessType = BusinessType.EXPORT)
    @PostMapping("/importData")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tAreaService.importUnit(file);
    }

    /**
     * 获取区域详细信息
     */
    @RequiresPermissions("wms:area:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAreaService.selectTAreaById(id));
    }

    /**
     * 新增区域
     */
    @RequiresPermissions("wms:area:add")
    @Log(title = "区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TArea tArea) {
        return toAjax(tAreaService.insertTArea(tArea));
    }

    /**
     * 修改区域
     */
    @RequiresPermissions("wms:area:edit")
    @Log(title = "区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TArea tArea) {
        return toAjax(tAreaService.updateTArea(tArea));
    }

    /**
     * 删除区域
     */
    @RequiresPermissions("wms:area:remove")
    @Log(title = "区域", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAreaService.deleteTAreaByIds(ids));
    }

    @GetMapping("/chooseListArea")
    public AjaxResult chooseListArea() {
        TArea area = new TArea();
        area.setStatus("0");
        area.setDelFlag("0");
        return AjaxResult.success(tAreaService.selectTAreaList(area));
    }
}
