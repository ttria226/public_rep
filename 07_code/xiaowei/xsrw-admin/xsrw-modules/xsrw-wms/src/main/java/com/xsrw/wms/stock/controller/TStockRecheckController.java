package com.xsrw.wms.stock.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.stock.domain.dto.TStockRecheckDTO;
import com.xsrw.wms.stock.domain.vo.TStockRecheckVO;
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
import com.xsrw.wms.stock.domain.TStockRecheck;
import com.xsrw.wms.stock.service.ITStockRecheckService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 复检管理Controller
 *
 * @author wxr
 * @date 2023-06-21
 */
@RestController
@RequestMapping("/stock/recheck")
public class TStockRecheckController extends BaseController {
    @Autowired
    private ITStockRecheckService tStockRecheckService;

    /**
     * 查询复检管理列表
     */
    @RequiresPermissions("stock:recheck:list")
    @GetMapping("/list")
    public TableDataInfo list(TStockRecheckDTO tStockRecheck) {
        startPage();
        List<TStockRecheckVO> list = tStockRecheckService.selectTStockRecheckList(tStockRecheck);
        return getDataTable(list);
    }

    /**
     * 导出复检管理列表
     */
    @RequiresPermissions("stock:recheck:export")
    @Log(title = "复检管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TStockRecheckDTO tStockRecheck) {
        List<TStockRecheckVO> list = tStockRecheckService.selectTStockRecheckList(tStockRecheck);
        ExcelUtil<TStockRecheckVO> util = new ExcelUtil<>(TStockRecheckVO.class);
        util.exportExcel(response, list, "复检管理数据");
    }

    /**
     * 获取复检管理详细信息
     */
    @RequiresPermissions("stock:recheck:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tStockRecheckService.selectTStockRecheckById(id));
    }

    /**
     * 新增复检管理
     */
    @RequiresPermissions("stock:recheck:add")
    @Log(title = "复检管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TStockRecheck tStockRecheck) {
        return toAjax(tStockRecheckService.insertTStockRecheck(tStockRecheck));
    }

    /**
     * 修改复检管理
     */
    @RequiresPermissions("stock:recheck:edit")
    @Log(title = "复检管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TStockRecheck tStockRecheck) {
        return toAjax(tStockRecheckService.updateTStockRecheck(tStockRecheck));
    }

    /**
     * 删除复检管理
     */
    @RequiresPermissions("stock:recheck:remove")
    @Log(title = "复检管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tStockRecheckService.deleteTStockRecheckByIds(ids));
    }

    /**
     * 检测完成
     *
     * @param tStockRecheck
     * @return
     */
    @PostMapping("/checkEnd")
    public AjaxResult checkEnd(@RequestBody TStockRecheck tStockRecheck) {
        if (tStockRecheck.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tStockRecheckService.checkEnd(tStockRecheck);
    }

    /**
     * 获取库存的物料详情列表
     *
     * @param tStockRecheck
     * @return
     */
    @GetMapping(value = "/getMaterialDetailList")
    public TableDataInfo getMaterialDetailList(TStockRecheck tStockRecheck) {
        if (tStockRecheck.getId() == null) {
            new TableDataInfo();
        }
        List<TMaterialDetailVO> list = tStockRecheckService.getMaterialDetailList(tStockRecheck);
        return getDataTable(list);
    }


}
