package com.xsrw.wms.api.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.Logical;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.dto.AddTaskDTO;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.ExcelTaskVO;
import com.xsrw.wms.check.domain.vo.TTaskCheckVO;
import com.xsrw.wms.check.domain.vo.TaskDetailVO;
import com.xsrw.wms.check.domain.vo.TaskVO;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/taskApi")
public class TTaskApiController extends BaseController
{
    @Autowired
    private ITTaskService tTaskService;

    @Autowired
    private ITTaskDetailService tTaskDetailService;

    @Autowired
    private ITCheckDeliveryService tCheckDeliveryService;


    /**
     * 查询盘点任务列表
     */
    @RequiresPermissions("check:task:list")
    @GetMapping("/list")
    @ApiResponses(value = {
            @ApiResponse(code=200,message = "",response = TaskVO.class)
    })
    public TableDataInfo list(TTask tTask)
    {
        startPage();
        List<TaskVO> list = tTaskService.selectTTaskList(tTask);
        return getDataTable(list);
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
     * 盘点数据提交
     */
    @Log(title = "pda盘点数据提交", businessType = BusinessType.UPDATE)
    @RequiresPermissions("check:task:query")
    @PostMapping(value = "/check")
    public AjaxResult check(@RequestBody TTaskCheckVO tTaskCheckVO)
    {
        if(tTaskCheckVO.getId()==null){
            return AjaxResult.error("盘点任务不能为空");
        }
        if(tTaskCheckVO.getTaskDetailCheckVOS().size() == 0){
            return AjaxResult.error("盘点物料不能为空");
        }
        return tTaskService.check(tTaskCheckVO);
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



    /**
     * 查询盘点任务列表
     * @param tTask
     * @return
     */
    @GetMapping("/checkList")
    public TableDataInfo checkList(TTask tTask)
    {
        startPage();
        List<TaskVO> list = tTaskService.selectTTaskList(tTask);
        return getDataTable(list);
    }


    /**
     * 盘点任务详情
     * @param taskDetail
     * @return
     */
    @GetMapping(value = "/checkList/detail")
    public TableDataInfo getListByTaskId(TaskDetailDTO taskDetail) {
        if(taskDetail.getTaskId() == null){
            return new TableDataInfo();
        }
        startPage();
        List<TaskDetailVO> list = tTaskDetailService.selectTTaskDetailList(taskDetail);
        return getDataTable(list);
    }


    /**
     * 待盘点物料列表
     * @param taskId
     * @param trayCode
     * @param checkType
     * @return
     */
    @GetMapping("/checkList/trayDetail")
    public AjaxResult trayDetail(Long taskId,String trayCode,String checkType){
        return tTaskDetailService.trayDetail(taskId, trayCode, checkType);
    }


    /**
     * 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    @Log(title = "pda提交盘点数据", businessType = BusinessType.UPDATE)
    @PostMapping("/checkList/submit")
    public AjaxResult checkSubmit(@RequestBody CheckDeliveryDTO checkDeliveryDTO){
//        return tTaskDetailService.checkSubmit(checkDeliveryDTO);
        return tCheckDeliveryService.checkdeliverySubmit(checkDeliveryDTO);
    }

}
