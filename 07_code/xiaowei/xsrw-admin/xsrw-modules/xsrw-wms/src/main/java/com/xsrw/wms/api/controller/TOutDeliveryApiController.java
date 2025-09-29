package com.xsrw.wms.api.controller;

import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialSelectVO;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.inout.service.ITTaskOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveryOut")
public class TOutDeliveryApiController extends BaseController {
    @Autowired
    private ITOutDeliveryService tOutDeliveryService;

    @Autowired
    private ITTaskOutService tTaskOutService;

    /**
     * 查询出库单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TOutDelivery tOutDelivery) {
        startPage();
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }

    /**
     * 出库执行列表
     */
    @RequiresPermissions("out:deliveryOut:list")
    @GetMapping("/outTasklist")
    public TableDataInfo outTasklist(TOutDeliveryDetail tOutDeliveryDetail) {
        startPage();
        List<TOutDeliveryDetailVO> list = tOutDeliveryService.outTasklist(tOutDeliveryDetail);
        return getDataTable(list);
    }


    /**
     * 获取出库单详细信息
     */
    @RequiresPermissions("inout:deliveryOut:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOutDeliveryService.selectTOutDeliveryById(id));
    }

    /**
     * 新增出库计划
     */
    @RequiresPermissions("inout:deliveryOut:add")
    @Log(title = "pda出库单新增", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutDeliveryVO tOutDeliveryVO) {
        return tOutDeliveryService.insertTOutDelivery(tOutDeliveryVO);
    }

    /**
     * 修改出库单
     */
    @RequiresPermissions("inout:deliveryOut:edit")
    @Log(title = "pda出库单修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutDeliveryVO tOutDelivery) {
        return tOutDeliveryService.updateTOutDelivery(tOutDelivery);
    }

    /**
     * 出库计划审核
     */
    @RequiresPermissions("inout:deliveryOut:approve")
    @Log(title = "pda出库单审核", businessType = BusinessType.UPDATE)
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody TOutDelivery tOutDelivery) {
        return tOutDeliveryService.approveTOutDelivery(tOutDelivery);
    }

    /**
     * 生成出库任务
     */
    @RequiresPermissions("inout:deliveryOut:toOutTask")
    @Log(title = "pda生成出库任务", businessType = BusinessType.OTHER)
    @GetMapping("/toOutTask")
    public AjaxResult toOutTask(@RequestParam Long[] ids) {
        return tOutDeliveryService.toOutTask(ids);
    }

    /***
     * 获取单据物料选择列表
     */
    @GetMapping("/getMaterialSelectList")
    public TableDataInfo getMaterialSelectList(TMaterialDTO tMaterial) {
        startPage();
        List<TMaterialSelectVO> list = tOutDeliveryService.getMaterialSelectList(tMaterial);
        return getDataTable(list);
    }

    /**
     * 删除出库单
     */
    @RequiresPermissions("inout:deliveryOut:remove")
    @Log(title = "pda删除出库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return tOutDeliveryService.deleteTOutDeliveryByIds(ids);
    }

    /**
     * 删除出库执行列表任务
     */
    @RequiresPermissions("inout:deliveryOut:remove")
    @Log(title = "pda删除出库执行列表任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeTasks/{ids}")
    public AjaxResult removeTasks(@PathVariable Long[] ids) {
        return tOutDeliveryService.deleteTOutDeliveryDetailByIds(ids);
    }


    /**
     * 快捷出库列表
     * @param tOutDelivery
     * @return
     */
    @GetMapping("/suit/list")
    public TableDataInfo suitlist(TOutDelivery tOutDelivery)
    {
        startPage();
        // 齐套出库
        tOutDelivery.setDeliveryModule("2");
        // 审核通过
        tOutDelivery.setStatus("2");
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }

    /**
     * 执行出库--自动分配载具
     * @return
     */
    @GetMapping("/traylist/voluntarilyAll")
    public AjaxResult traylistVoluntarily(Long outDeliveryId)
    {
        return tTaskOutService.voluntarilyAll(outDeliveryId);
    }


    /**
     * 齐套 执行出库
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/suit/addTask")
    @Log(title = "pda齐套执行出库", businessType = BusinessType.UPDATE)
    public AjaxResult suitAddTask(@RequestBody List<TTaskOutVO> tTaskOutVO){
        return tOutDeliveryService.suitAddTask(tTaskOutVO);
    }
}
