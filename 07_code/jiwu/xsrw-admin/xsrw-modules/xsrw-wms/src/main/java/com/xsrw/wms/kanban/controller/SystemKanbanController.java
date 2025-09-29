package com.xsrw.wms.kanban.controller;


import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.kanban.service.ISystemKanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 系统看板controller
 * @Author tyk
 * @Date 2023-06-25
 */
@RestController
@RequestMapping("/systemKanban")
public class SystemKanbanController extends BaseController {


    @Autowired
    private ISystemKanbanService systemKanbanService;

    /**
     * 任务看板
     * @param timeRangeType 时间范围类型 日 月 年
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:taskKanban")
    @GetMapping("/taskKanban")
    public AjaxResult taskKanban(String timeRangeType) {
        return success(systemKanbanService.taskKanban(timeRangeType));
    }


    /**
     * 设备运行
     * @param timeRangeType 时间范围类型 日 月 年
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:equipmentStatistics")
    @GetMapping("/equipmentStatistics")
    public AjaxResult equipmentStatistics(String timeRangeType) {
        return success(systemKanbanService.equipmentStatistics(timeRangeType));
    }

    /**
     * 设备信息列表
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:equipmentList")
    @GetMapping("/equipmentList")
    public AjaxResult equipmentList() {
        return success(systemKanbanService.equipmentList());
    }

    /**
     * 任务执行情况
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:taskExecutionStatistics")
    @GetMapping("/taskExecutionStatistics")
    public AjaxResult taskExecutionStatistics() {
        return success(systemKanbanService.taskExecutionStatistics());
    }


    /**
     * 任务列表
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:taskList")
    @GetMapping("/taskList")
    public AjaxResult taskList() {
        return success(systemKanbanService.taskList());
    }

    /**
     * 物料库存top
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:stockList")
    @GetMapping("/stockList")
    public AjaxResult stockList() {
        return success(systemKanbanService.stockList());
    }

    /**
     * 任务状态统计
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:taskStatusStatistics")
    @GetMapping("/taskStatusStatistics")
    public AjaxResult taskStatusStatistics() {
        return success(systemKanbanService.taskStatusStatistics());
    }

    /**
     * 仓库使用情况统计
     * @param areaId 区域id
     * @return
     */
    //    @RequiresPermissions("kanban:systemKanban:warehouseUseStatistics")
    @GetMapping("/warehouseUseStatistics")
    public AjaxResult warehouseUseStatistics(Integer areaId) {
        return success(systemKanbanService.warehouseUseStatistics(areaId));
    }
}
