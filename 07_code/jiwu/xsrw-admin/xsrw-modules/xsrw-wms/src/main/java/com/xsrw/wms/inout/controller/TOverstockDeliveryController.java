package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.inout.domain.dto.TOverstockDeliveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import com.xsrw.wms.inout.domain.TOverstockDelivery;
import com.xsrw.wms.inout.service.ITOverstockDeliveryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 越库单Controller
 *
 * @author wxr
 * @date 2023-06-25
 */
@RestController
@RequestMapping("/inout/overstock")
public class TOverstockDeliveryController extends BaseController {
    @Autowired
    private ITOverstockDeliveryService tOverstockDeliveryService;

    /**
     * 查询越库单列表
     */
    @RequiresPermissions("inout:overstock:list")
    @GetMapping("/list")
    public TableDataInfo list(TOverstockDelivery tOverstockDelivery) {
        startPage();
        List<TOverstockDelivery> list = tOverstockDeliveryService.selectTOverstockDeliveryList(tOverstockDelivery);
        return getDataTable(list);
    }

    /**
     * 导出越库单列表
     */
    @RequiresPermissions("inout:overstock:export")
    @Log(title = "越库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOverstockDelivery tOverstockDelivery) {
        List<TOverstockDelivery> list = tOverstockDeliveryService.selectTOverstockDeliveryList(tOverstockDelivery);
        ExcelUtil<TOverstockDelivery> util = new ExcelUtil<TOverstockDelivery>(TOverstockDelivery.class);
        util.exportExcel(response, list, "越库单数据");
    }

    /**
     * 获取越库单详细信息
     */
    @RequiresPermissions("inout:overstock:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOverstockDeliveryService.selectTOverstockDeliveryById(id));
    }

    /**
     * 新增越库单
     */
    @RequiresPermissions("inout:overstock:add")
    @Log(title = "越库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOverstockDeliveryDTO tOverstockDelivery) {
        if (CollectionUtils.isEmpty(tOverstockDelivery.getDeliveryDetailList())) {
            return AjaxResult.error("物料列表不可为空");
        }
        return toAjax(tOverstockDeliveryService.insertTOverstockDelivery(tOverstockDelivery));
    }

    /**
     * 修改越库单
     */
    @RequiresPermissions("inout:overstock:edit")
    @Log(title = "越库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOverstockDeliveryDTO tOverstockDelivery) {
        if (CollectionUtils.isEmpty(tOverstockDelivery.getDeliveryDetailList())) {
            return AjaxResult.error("物料列表不可为空");
        }
        return toAjax(tOverstockDeliveryService.updateTOverstockDelivery(tOverstockDelivery));
    }

    /**
     * 删除越库单
     */
    @RequiresPermissions("inout:overstock:remove")
    @Log(title = "越库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOverstockDeliveryService.deleteTOverstockDeliveryByIds(ids));
    }

    /**
     * 越库单收货
     *
     * @param tOverstockDelivery
     * @return
     */
    @RequiresPermissions("inout:overstock:register")
    @Log(title = "越库单收货", businessType = BusinessType.INSERT)
    @PostMapping("/registerDelivery")
    public AjaxResult registerDelivery(@RequestBody TOverstockDeliveryDTO tOverstockDelivery) {
        return tOverstockDeliveryService.registerDelivery(tOverstockDelivery);
    }

    /**
     * 越库单出库
     *
     * @param tOverstockDelivery
     * @return
     */
    @RequiresPermissions("inout:overstock:out")
    @Log(title = "越库单出库", businessType = BusinessType.INSERT)
    @PostMapping("/outDelivery")
    public AjaxResult outDelivery(@RequestBody TOverstockDeliveryDTO tOverstockDelivery) {
        return tOverstockDeliveryService.outDelivery(tOverstockDelivery);
    }
}
