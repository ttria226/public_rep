package com.xsrw.wms.check.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.Logical;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.dto.AddTaskDTO;
import com.xsrw.wms.check.domain.vo.ExcelTaskVO;
import com.xsrw.wms.check.domain.vo.TaskVO;
import com.xsrw.wms.check.service.ITTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 盘点任务Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/task")
public class TTaskController extends BaseController
{
    @Autowired
    private ITTaskService tTaskService;

    /**
     * 查询盘点任务列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("check:task:list")
    @GetMapping("/list")
    public TableDataInfo list(TTask tTask)
    {
        startPage();
        List<TaskVO> list = tTaskService.selectTTaskList(tTask);
        return getDataTable(list);
    }

    /**
     * 导出盘点任务列表
     */
    @RequiresPermissions("check:task:export")
    @Log(title = "盘点任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTask tTask)
    {
        List<ExcelTaskVO> list = tTaskService.export(tTask);
        ExcelUtil<ExcelTaskVO> util = new ExcelUtil<>(ExcelTaskVO.class);
        util.exportExcel(response, list, "任务数据");
    }

    /**
     * 获取盘点任务详细信息
     */
    @RequiresPermissions("check:task:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tTaskService.selectTTaskById(id));
    }

    /**
     * 新增盘点任务
     */
    @RequiresPermissions("check:task:add")
    @Log(title = "盘点任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTask tTask)
    {
        return toAjax(tTaskService.insertTTask(tTask));
    }

    /**
     * 修改盘点任务
     */
    @RequiresPermissions("check:task:edit")
    @Log(title = "盘点任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTask tTask)
    {
        return toAjax(tTaskService.updateTTask(tTask));
    }

    /**
     * 删除盘点任务
     */
    @RequiresPermissions("check:task:remove")
    @Log(title = "盘点任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return tTaskService.deleteTTaskByIds(ids);
    }

    /**
     * 审核-盘点
     * @param ids
     * @return
     */
    @RequiresPermissions(value = {"check:task:approve","check:checkTask:approve"},logical = Logical.OR)
    @Log(title = "审核盘点任务", businessType = BusinessType.INSERT)
    @PostMapping("/approve/{ids}")
    public AjaxResult approveCheck(@PathVariable Long[] ids) {
        return tTaskService.approveCheck(ids);
    }

    /**
     * 激活盘点任务
     * @param ids
     * @return
     */
    @RequiresPermissions(value = {"check:task:active","check:checkTask:active"},logical = Logical.OR)
    @Log(title = "激活盘点任务", businessType = BusinessType.INSERT)
    @PostMapping("/active/{ids}")
    public AjaxResult activeCheck(@PathVariable Long[] ids) {
        return tTaskService.activeCheck(ids);
    }

    /**
     * 终止激活盘点任务
     * @param ids
     * @return
     */
    @RequiresPermissions(value = {"check:task:stop","check:checkTask:stop"},logical = Logical.OR)
    @Log(title = "终止已激活的盘点任务", businessType = BusinessType.INSERT)
    @PostMapping("/stop/{ids}")
    public AjaxResult stopCheck(@PathVariable Long[] ids) {
        return tTaskService.stopCheck(ids);
    }

    /**
     * 生成盘点任务
     * @param deliveryIds
     * @return
     */
    @RequiresPermissions("check:task:addCheckTask")
    @Log(title = "生成盘点任务", businessType = BusinessType.INSERT)
    @PostMapping("/addCheckTask")
    public AjaxResult addCheckTask(@RequestBody List<Long> deliveryIds) {
        // 不支持多个盘点计划生成任务
//        if (deliveryIds.size() > 1){
//            return AjaxResult.error("只能选择一条计划生成任务");
//        }
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setDeliveryIds(deliveryIds);
        addTaskDTO.setType(Constants.TASK_TYPE_CHECK);
        return tTaskService.addTask(addTaskDTO);
    }


    /**
     * 生成回库任务
     * @param trayCode
     * @return
     */
    @GetMapping("/addBackTask")
    public AjaxResult addBackTask(String trayCode,String backType) {
        AddTaskDTO addTaskDTO = new AddTaskDTO();
        addTaskDTO.setPalletNum(trayCode);
        addTaskDTO.setType(Constants.TASK_TYPE_BACK);
        addTaskDTO.setBackType(backType);
        return tTaskService.addTask(addTaskDTO);
    }


//    /**
//     * wcs调用接口
//     * wcs 返回数据
//     * code  如果大于0是异常   代表wcs错误编码
//     * isRedo  任务是否需要重新组盘，是否需要重新传递给wcs
//     * errorMsg 错误信息
//     * return 1 代表成功   0失败
//     */
//    @GetMapping("/feign/wcsReturnData")
//    public AjaxResult wcsReturnData(@RequestParam("taskId") Long taskId,@RequestParam("code") Integer code,
//                                    @RequestParam("errorMsg") String errorMsg,@RequestParam("isRedo") Boolean isRedo) {
//        return AjaxResult.success(tTaskService.wcsReturnData(taskId,code,errorMsg,isRedo));
//    }
}
