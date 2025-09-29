package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.domain.vo.TOutShipmentsVO;
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
import com.xsrw.wms.inout.domain.TOutShipments;
import com.xsrw.wms.inout.service.ITOutShipmentsService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 出库发货单Controller
 *
 * @author wxr
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/inout/shipments")
public class TOutShipmentsController extends BaseController {
    @Autowired
    private ITOutShipmentsService tOutShipmentsService;

    /**
     * 查询出库发货单列表
     */
    @RequiresPermissions("inout:shipments:list")
    @GetMapping("/list")
    public TableDataInfo list(TOutShipments tOutShipments) {
        startPage();
        List<TOutShipmentsVO> list = tOutShipmentsService.selectTOutShipmentsList(tOutShipments);
        return getDataTable(list);
    }

    /**
     * 导出出库发货单列表
     */
    @RequiresPermissions("inout:shipments:export")
    @Log(title = "出库发货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOutShipments tOutShipments) {
        List<TOutShipmentsVO> list = tOutShipmentsService.selectTOutShipmentsList(tOutShipments);
        ExcelUtil<TOutShipmentsVO> util = new ExcelUtil<>(TOutShipmentsVO.class);
        util.exportExcel(response, list, "出库发货单数据");
    }

    /**
     * 获取出库发货单详细信息
     */
    @RequiresPermissions("inout:shipments:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOutShipmentsService.selectTOutShipmentsById(id));
    }

    /**
     * 新增出库发货单
     */
    @RequiresPermissions("inout:shipments:add")
    @Log(title = "出库发货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutShipments tOutShipments) {
        return tOutShipmentsService.insertTOutShipments(tOutShipments);
    }

    /**
     * 修改出库发货单
     */
    @RequiresPermissions("inout:shipments:edit")
    @Log(title = "出库发货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutShipments tOutShipments) {
        return toAjax(tOutShipmentsService.updateTOutShipments(tOutShipments));
    }

    /**
     * 删除出库发货单
     */
    @RequiresPermissions("inout:shipments:remove")
    @Log(title = "出库发货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOutShipmentsService.deleteTOutShipmentsByIds(ids));
    }

    /**
     * 出库发货单单状态更新
     *
     * @param tOutShipments
     * @return
     */
    @Log(title = "出库发货单单状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody TOutShipments tOutShipments) {
        if (tOutShipments.getId() == null || StringUtils.isEmpty(tOutShipments.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tOutShipmentsService.updateById(tOutShipments));
    }

    /**
     * 选择列表
     *
     * @param tOutShipments
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TOutShipments tOutShipments) {
        List<TOutShipmentsVO> list = tOutShipmentsService.selectTOutShipmentsList(tOutShipments);
        return AjaxResult.success(list);
    }
}
