package com.xsrw.wms.task.controller;


import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.report.domain.dto.EfficiencyStatisticsDTO;
import com.xsrw.wms.report.domain.dto.WorkStatisticsDTO;
import com.xsrw.wms.report.domain.vo.EfficiencyStatisticsVO;
import com.xsrw.wms.report.domain.vo.WorkStatisticsListsVO;
import com.xsrw.wms.report.service.ITReportCenterService;
import com.xsrw.wms.task.domain.dto.TaskQueryDTO;
import com.xsrw.wms.task.domain.vo.TaskQueryVO;
import com.xsrw.wms.task.service.ITaskManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 任务管理controller
 * @Author tyk
 * @Date 2023-06-25
 */
@RestController
@RequestMapping("/taskManager")
public class TaskManagerController extends BaseController {


    @Autowired
    private ITaskManagerService taskManagerService;

    @Autowired
    private ITReportCenterService reportCenterService;

    /**
     * 任务查询列表
     * @param request
     * @return
     */
    //    @RequiresPermissions("taskManager:taskQuery:list")
    @GetMapping("/taskQuery/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo taskQueryList(TaskQueryDTO request) {
        startPage();
        List<TaskQueryVO> taskQueryList = taskManagerService.taskQueryList(request);
        return getDataTable(taskQueryList);
    }

    /**
     * 任务查询列表导出
     * @param response
     * @param request
     */
//    @RequiresPermissions("taskManager:taskQuery:export")
    @PostMapping("/taskQuery/export")
    @DataScope(deptAlias = "d", userAlias = "u")
    public void taskQueryExport(HttpServletResponse response,TaskQueryDTO request) {
        List<TaskQueryVO> taskQueryList = taskManagerService.taskQueryList(request);
        ExcelUtil<TaskQueryVO> util = new ExcelUtil<>(TaskQueryVO.class);
        util.exportExcel(response, taskQueryList, "sheet1");
    }

    /**
     * 效率统计列表
     * @param request
     * @return
     */
//    @RequiresPermissions("taskManager:efficiencyStatistics:list")
    @GetMapping("/efficiencyStatistics/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo efficiencyStatisticsList(EfficiencyStatisticsDTO request) {
        startPage();
        List<EfficiencyStatisticsVO> efficiencyStatisticsList = reportCenterService.efficiencyStatisticsList(request);
        return getDataTable(efficiencyStatisticsList);
    }

    /**
     * 效率统计列表导出
     * @param request
     * @return
     */
//    @RequiresPermissions("taskManager:efficiencyStatistics:export")
    @PostMapping("/efficiencyStatistics/export")
    @DataScope(deptAlias = "d", userAlias = "u")
    public void efficiencyStatisticsExport(HttpServletResponse response,EfficiencyStatisticsDTO request) {
        List<EfficiencyStatisticsVO> efficiencyStatisticsList = reportCenterService.efficiencyStatisticsList(request);
        ExcelUtil<EfficiencyStatisticsVO> util = new ExcelUtil<>(EfficiencyStatisticsVO.class);
        util.exportExcel(response, efficiencyStatisticsList, "sheet1");
    }

    /**
     * 工作统计列表
     * @param request
     * @return
     */
//    @RequiresPermissions("taskManager:workStatistics:list")
    @GetMapping("/workStatistics/list")
    @DataScope(deptAlias = "d", userAlias = "u")
    public TableDataInfo workStatisticsList(WorkStatisticsDTO request) {
        startPage();
        List<WorkStatisticsListsVO> efficiencyStatisticsList = reportCenterService.workStatisticsList(request);
        return getDataTable(efficiencyStatisticsList);
    }

    /**
     * 工作统计列表导出
     * @param request
     * @return
     */
//    @RequiresPermissions("taskManager:efficiencyStatistics:export")
    @PostMapping("/workStatistics/export")
    @DataScope(deptAlias = "d", userAlias = "u")
    public void workStatisticsExport(HttpServletResponse response,WorkStatisticsDTO request) {
        List<WorkStatisticsListsVO> workStatisticsList = reportCenterService.workStatisticsList(request);
        ExcelUtil<WorkStatisticsListsVO> util = new ExcelUtil<>(WorkStatisticsListsVO.class);
        util.exportExcel(response, workStatisticsList, "sheet1");
    }
}
