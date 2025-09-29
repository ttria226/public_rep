package com.xsrw.wms.stock.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.stock.domain.TMoveLibrary;
import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.stock.service.ITMoveLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库内移位Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/moveLibrary")
public class TMoveLibraryController extends BaseController
{
    @Autowired
    private ITMoveLibraryService tMoveLibraryService;

    /**
     * 查询库内移位列表
     */
    @RequiresPermissions("stock:moveLibrary:list")
    @GetMapping("/list")
    public TableDataInfo list(MoveLibraryVo moveLibrary)
    {
        startPage();
        List<MoveLibraryVo> list = tMoveLibraryService.selectTMoveLibraryList(moveLibrary);
        return getDataTable(list);
    }

    /**
     * 导出库内移位列表
     */
    @RequiresPermissions("stock:moveLibrary:export")
    @Log(title = "库内移位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MoveLibraryVo moveLibrary)
    {
        List<MoveLibraryVo> list = tMoveLibraryService.selectTMoveLibraryList(moveLibrary);
        ExcelUtil<MoveLibraryVo> util = new ExcelUtil<MoveLibraryVo>(MoveLibraryVo.class);
        util.exportExcel(response, list, "库内移位数据");
    }

    /**
     * 获取库内移位详细信息
     */
    @RequiresPermissions("stock:moveLibrary:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMoveLibraryService.selectTMoveLibraryById(id));
    }

    /**
     * 新增库内移位
     */
    @RequiresPermissions("stock:moveLibrary:add")
    @Log(title = "库内移位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMoveLibrary tMoveLibrary)
    {
        return toAjax(tMoveLibraryService.insertTMoveLibrary(tMoveLibrary));
    }

    /**
     * 修改库内移位
     */
    @RequiresPermissions("stock:moveLibrary:edit")
    @Log(title = "库内移位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMoveLibrary tMoveLibrary)
    {
        return toAjax(tMoveLibraryService.updateTMoveLibrary(tMoveLibrary));
    }

    /**
     * 删除库内移位
     */
    @RequiresPermissions("stock:moveLibrary:remove")
    @Log(title = "库内移位", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return tMoveLibraryService.deleteTMoveLibraryByIds(ids);
    }

    /**
     * 库内移位添加数据
     * @param stockId 库存id
     * @param locationInId 转入库位ID
     * @return
     */
//    @RequiresPermissions("stock:moveLibrary:shift")
    @Log(title = "库内移位", businessType = BusinessType.INSERT)
    @PostMapping(value = "/shift")
    public AjaxResult shift(@RequestParam("stockId") Long stockId, @RequestParam("locationInId") Long locationInId) {
        return tMoveLibraryService.shift(stockId, locationInId);
    }

    /**
     * 库内移位审核
     * @param id
     * @return
     */
    @RequiresPermissions("stock:moveLibrary:auditor")
    @Log(title = "库内移位", businessType = BusinessType.UPDATE)
    @GetMapping("/auditor/{id}")
    public AjaxResult auditor(@PathVariable Long id) {
        return tMoveLibraryService.updateAuditor(id);
    }

    /**
     * 生成移库任务
     */
    @RequiresPermissions("stock:moveLibrary:shift")
    @GetMapping(value = "/move/{id}")
    public AjaxResult move(@PathVariable("id") Long id) {
        return tMoveLibraryService.move(id);
    }
}
