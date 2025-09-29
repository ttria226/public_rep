package com.xsrw.wms.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.Logical;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.check.domain.TCheckAreaHistory;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.CheckAreaHistoryVO;
import com.xsrw.wms.check.domain.vo.TaskDetailVO;
import com.xsrw.wms.check.service.ITCheckAreaHistoryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 库存盘点Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/taskDetail")
public class TTaskDetailController extends BaseController
{
    @Autowired
    private ITTaskDetailService tTaskDetailService;

    @Autowired
    private ITCheckAreaHistoryService itCheckAreaHistoryService;

    /**
     * 查询库存盘点列表
     */
    @RequiresPermissions("check:taskDetail:list")
    @GetMapping("/list")
    public TableDataInfo list(TaskDetailDTO taskDetail)
    {
        startPage();
        List<TaskDetailVO> list = tTaskDetailService.selectTTaskDetailList(taskDetail);
        return getDataTable(list);
    }

    /**
     * 导出库存盘点列表
     */
    @RequiresPermissions("check:taskDetail:export")
    @Log(title = "库存盘点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TaskDetailDTO taskDetail)
    {
        List<TaskDetailVO> list = tTaskDetailService.selectTTaskDetailList(taskDetail);
        ExcelUtil<TaskDetailVO> util = new ExcelUtil<TaskDetailVO>(TaskDetailVO.class);
        util.exportExcel(response, list, "库存盘点数据");
    }

    /**
     * 获取库存盘点详细信息
     */
    @RequiresPermissions("check:taskDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return tTaskDetailService.selectTTaskDetailById(id);
    }

    /**
     * 新增库存盘点
     */
    @RequiresPermissions("check:taskDetail:add")
    @Log(title = "库存盘点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskDetail tTaskDetail)
    {
        return toAjax(tTaskDetailService.insertTTaskDetail(tTaskDetail));
    }

    /**
     * 修改库存盘点
     */
    @RequiresPermissions("check:taskDetail:edit")
    @Log(title = "库存盘点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskDetail tTaskDetail)
    {
        return toAjax(tTaskDetailService.updateTTaskDetail(tTaskDetail));
    }

    /**
     * 删除库存盘点
     */
    @RequiresPermissions("check:taskDetail:remove")
    @Log(title = "库存盘点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTaskDetailService.deleteTTaskDetailByIds(ids));
    }

    /**
     * 根据任务id获取任务详情详细信息
     */
    @RequiresPermissions(value = {"check:taskDetail:getListByTaskId",
            "wms:checkTask:query",
            "wms:checkTaskDetail:query"},logical = Logical.OR)
    @GetMapping(value = "/getListByTaskId")
    public TableDataInfo getListByTaskId(TaskDetailDTO taskDetail) {
        if(taskDetail.getTaskId() == null){
            return new TableDataInfo();
        }
        startPage();
        List<TaskDetailVO> list = tTaskDetailService.selectTTaskDetailList(taskDetail);
        return getDataTable(list);
    }

    /**
     * 批量 新增、修改 出库详情
     * @param list
     * @return
     */
    //TODO 权限暂不添加
//    @RequiresPermissions("check:outDeliveryDetail:add")
    @Log(title = "出库详情", businessType = BusinessType.INSERT)
    @PostMapping("/batchAddOrUpdate")
    public AjaxResult batchAddOrUpdate(@RequestBody List<TTaskDetail> list) {
        return tTaskDetailService.batchAdd(list);
    }

    /**
     * 执行盘点
     * @param id 任务子表id
     * @return
     */
    @RequiresPermissions("check:taskDetail:performCheck")
    @PostMapping(value = "/performCheck/{id}")
    public AjaxResult performCheck(@PathVariable("id") Long id,@RequestBody TaskDetailDTO taskDetailDTO) {
        if(taskDetailDTO.getCheckNum() == null){
            return AjaxResult.error("盘点数量不可为空");
        }
        return tTaskDetailService.performCheck(id,taskDetailDTO.getCheckNum());
    }

    /**
     * 审核盘点任务子表
     * @param ids
     * @return
     */
    @RequiresPermissions(value = {"check:taskDetail:approve","wms:checkTask:examine"},logical = Logical.OR)
    @Log(title = "审核盘点任务子表", businessType = BusinessType.INSERT)
    @PostMapping("/approve/{ids}")
    public AjaxResult approveCheck(@PathVariable Long[] ids,String status) {
//        return tTaskDetailService.approveCheck(ids,status);
        return tTaskDetailService.checkTaskDetail(ids,status);
    }


    /**
     * 审核区域盘点任务
     * @param param
     * @return
     */
    @PostMapping("/approveAreaCheck")
    public AjaxResult approveAreaCheck(@RequestBody Map<String, Object> param){
        Long taskDetailId = Long.valueOf(param.get("taskDetailId").toString());
        List<Long> ids = (List<Long>) param.get("ids");
        String status = param.get("status").toString();
        return tTaskDetailService.approveAreaCheck(taskDetailId,ids,status);
    }

    /**
     * 查询盘点任务-平库盘点的提交记录
     * @param taskDetailId
     * @param taskId
     * @param materialCode
     * @param trayCode
     * @param status
     * @return
     */
    @GetMapping(value = "/getCheckAreaHistory")
    public TableDataInfo getCheckAreaHistory(@RequestParam(value = "taskDetailId",required = false) Long taskDetailId,
                                             @RequestParam(value = "taskId",required = false)  Long taskId,
                                             @RequestParam(value = "materialCode",required = false)  String materialCode,
                                             @RequestParam(value = "trayCode",required = false)  String trayCode,
                                             @RequestParam(value = "status",required = false) String status){
        startPage();
        List<CheckAreaHistoryVO> list = itCheckAreaHistoryService.getCheckAreaHistory(taskDetailId,taskId,materialCode,trayCode,status);
        return getDataTable(list);
    }

    /**
     * 查询盘点任务-登记盘点结果
     * @param taskDetailId
     * @param taskId
     * @param materialCode
     * @param trayCode
     * @param status
     * @return
     */
    @GetMapping(value = "/getCheckTaskDetail")
    public TableDataInfo getCheckTaskDetail(@RequestParam(value = "taskDetailId",required = false) Long taskDetailId,
                                            @RequestParam(value = "taskId",required = false)  Long taskId,
                                            @RequestParam(value = "materialCode",required = false)  String materialCode,
                                            @RequestParam(value = "trayCode",required = false)  String trayCode,
                                            @RequestParam(value = "status",required = false) String status){
        startPage();
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        if (taskDetailId != null){
            taskDetailDTO.setId(taskDetailId);
        }
        if (taskId != null){
            taskDetailDTO.setTaskId(taskId);
        }
        if (materialCode != null){
            taskDetailDTO.setMaterialCode(materialCode);
        }
        if (trayCode != null){
            taskDetailDTO.setTrayCode(trayCode);
        }
        if (status != null){
            taskDetailDTO.setStatus(status);
        }

        taskDetailDTO.setTaskType(Constants.TASK_TYPE_CHECK);
        List<TaskDetailVO> list = tTaskDetailService.selectCheckTaskResult(taskDetailDTO);
        return getDataTable(list);
    }
}
