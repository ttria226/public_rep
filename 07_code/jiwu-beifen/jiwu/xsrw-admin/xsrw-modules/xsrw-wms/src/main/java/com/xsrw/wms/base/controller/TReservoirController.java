package com.xsrw.wms.base.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.vo.ExcelReservoirVO;
import com.xsrw.wms.base.domain.vo.TReservoirVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.TReservoir;
import com.xsrw.wms.base.service.ITReservoirService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 库区Controller
 *
 * @author wxr
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/reservoir")
public class TReservoirController extends BaseController {
    @Autowired
    private ITReservoirService tReservoirService;

    /**
     * 查询库区列表
     */
//    @RequiresPermissions("wms:reservoir:list")
    @GetMapping("/list")
    public TableDataInfo list(TReservoir tReservoir) {
        startPage();
        List<TReservoirVO> list = tReservoirService.selectTReservoirList(tReservoir);
        return getDataTable(list);
    }

    /**
     * 导出库区列表
     */
    @RequiresPermissions("wms:reservoir:export")
    @Log(title = "库区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TReservoir tReservoir) {
        List<TReservoirVO> list = tReservoirService.selectTReservoirList(tReservoir);
        ExcelUtil<TReservoirVO> util = new ExcelUtil<>(TReservoirVO.class);
        util.exportExcel(response, list, "库区数据");
    }

    /**
     * 获取库区详细信息
     */
    @RequiresPermissions("wms:reservoir:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tReservoirService.selectTReservoirById(id));
    }

    /**
     * 新增库区
     */
    @RequiresPermissions("wms:reservoir:add")
    @Log(title = "库区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TReservoir tReservoir) {
        return toAjax(tReservoirService.insertTReservoir(tReservoir));
    }

    /**
     * 修改库区
     */
    @RequiresPermissions("wms:reservoir:edit")
    @Log(title = "库区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TReservoir tReservoir) {
        return toAjax(tReservoirService.updateTReservoir(tReservoir));
    }

    /**
     * 删除库区
     */
    @RequiresPermissions("wms:reservoir:remove")
    @Log(title = "库区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tReservoirService.deleteTReservoirByIds(ids));
    }

    /**
     * 导入模板信息
     *
     * @param response
     */
    @RequiresPermissions("wms:reservoir:exportdemo")
    @Log(title = "库区模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelReservoirVO> util = new ExcelUtil<>(ExcelReservoirVO.class);
        util.exportExcel(response, new ArrayList<>(), "仓库数据");
    }

    /**
     * 导入库区列表
     */
    @RequiresPermissions("wms:reservoir:import")
    @Log(title = "库区", businessType = BusinessType.EXPORT)
    @PostMapping("/import")
    public AjaxResult importReservoir(MultipartFile file) throws Exception {
        return tReservoirService.importReservoir(file);
    }

    /**
     * 库区禁用、启用
     */
    @RequiresPermissions("wms:reservoir:updateStatus")
    @Log(title = "库区禁用、启用", businessType = BusinessType.DELETE)
    @GetMapping("/status")
    public AjaxResult removeStatus(@RequestParam("status") String status, @RequestParam("id") Long id) {
        return toAjax(tReservoirService.deleteTReservoirStatusByIds(status, id));
    }

//    @RequiresPermissions("wms:reservoir:getReservoirList")
    @GetMapping("/getReservoirList")
    public AjaxResult getReservoirList(@RequestParam("areaId")Integer areaId ){
        return  AjaxResult.success(tReservoirService.getReservoirList(0,areaId));
    }

    @GetMapping("/reservoirList")
    public AjaxResult reservoirList(){
        return  AjaxResult.success(tReservoirService.getReservoirList(1,0));
    }

}
