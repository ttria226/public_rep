package com.xsrw.wms.inout.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.service.ITTaskWcsDetailService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * wcs任务Controller
 *
 * @author wxr
 * @date 2023-05-10
 */
@RestController
@RequestMapping("/inout/task")
public class TTaskWcsController extends BaseController {
    @Autowired
    private ITTaskWcsService tTaskWcsService;
    @Autowired
    private ITTaskWcsDetailService itTaskWcsDetailService;
    @Autowired
    private ITTrayService trayService;

    /**
     * 查询wcs任务列表
     */
    @RequiresPermissions("inout:task:list")
    @GetMapping("/list")
    public TableDataInfo list(TTaskWcs tTaskWcs) {
        startPage();
        if(StringUtils.isBlank(tTaskWcs.getTaskType())){
            return new TableDataInfo();
        }
        List<TTaskWcsVO> list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        return getDataTable(list);
    }

    /**
     * 查询全部列表
     * @param tTaskWcs
     * @return
     */
    @GetMapping("/listAll")
    public TableDataInfo listAll(TTaskWcs tTaskWcs) {
        startPage();
        List<TTaskWcsVO> list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        return getDataTable(list);
    }
    /**
     * 导出wcs任务列表
     */
    @RequiresPermissions("inout:task:export")
    @Log(title = "wcs任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTaskWcs tTaskWcs) {
        List<TTaskWcsVO> list = new ArrayList<>();
        if(StringUtils.isEmpty(tTaskWcs.getTaskType())){
           tTaskWcs.setTaskType("5");
        }
        list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        ExcelUtil<TTaskWcsVO> util = new ExcelUtil<>(TTaskWcsVO.class);
        util.exportExcel(response, list, "wcs任务数据");
    }

    /**
     * 获取wcs任务详细信息
     */
//    @RequiresPermissions("inout:task:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tTaskWcsService.selectTTaskWcsById(id));
    }

    /**
     * 新增wcs任务
     */
    @RequiresPermissions("inout:task:add")
    @Log(title = "wcs任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskWcs tTaskWcs) {
        return toAjax(tTaskWcsService.insertTTaskWcs(tTaskWcs));
    }

    /**
     * 修改wcs任务
     */
    @RequiresPermissions("inout:task:edit")
    @Log(title = "wcs任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskWcs tTaskWcs) {
        return toAjax(tTaskWcsService.updateTTaskWcs(tTaskWcs));
    }

    /**
     * 删除wcs任务
     */
    @RequiresPermissions("inout:task:remove")
    @Log(title = "wcs任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tTaskWcsService.deleteTTaskWcsByIds(ids));
    }


    /**
     * 强制执行-Old
     * @param tTaskWcs
     * @return
     */
//    @RequiresPermissions("inout:task:execute")
    @Log(title = "强制执行", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOld")
    public AjaxResult executeTaskOld(@RequestBody TTaskWcs tTaskWcs) {
        if(tTaskWcs.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.executeTask(tTaskWcs);
    }
    /**
     * 强制执行
     * @param tTaskWcs
     * @return
     */
//    @RequiresPermissions("inout:task:execute")
    @Log(title = "强制执行", businessType = BusinessType.UPDATE)
    @PostMapping("/execute")
    public AjaxResult executeTask(@RequestBody TTaskWcsDTO tTaskWcs) {
        if(tTaskWcs.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.executeTaskNew(tTaskWcs);
    }
    /**
     * 强制执行出库任务
     * @param tTaskWcsOutVO
     * @return
     */
//    @RequiresPermissions("inout:task:execute")
    @Log(title = "强制执行出库任务", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOutTask")
    public AjaxResult executeOutTask(@RequestBody TTaskWcsOutVO tTaskWcsOutVO) {
        if(tTaskWcsOutVO.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.executeOutTask(tTaskWcsOutVO);
    }
    /**
     * 执行更新库位
     * @param tTaskWcs
     * @return
     */
//    @RequiresPermissions("inout:task:execute")
    @Log(title = "执行更新库位", businessType = BusinessType.UPDATE)
    @PostMapping("/updateLocation")
    public AjaxResult updateLocation(@RequestBody TTaskWcs tTaskWcs) {
        if(tTaskWcs.getId() == null || tTaskWcs.getLocationId() == null){
            return AjaxResult.error("参数不全");
        }
        return AjaxResult.success(tTaskWcsService.updateTTaskWcs(tTaskWcs));
    }

    /**
     * 出库执行
     * @param tTaskWcs
     * @return
     */
    @RequiresPermissions("inout:task:executeOut")
    @Log(title = "出库执行", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOut")
    public AjaxResult executeOut(@RequestBody TTaskWcs tTaskWcs) {
        if(tTaskWcs.getId() == null){
            return AjaxResult.error("请选择要执行出库的任务");
        }
        return tTaskWcsService.executeOut(tTaskWcs);
    }

    /**
     * 移位监控--详情
     * @param id
     * @return
     */
    @GetMapping("/getShiftDetail")
    public TableDataInfo getShiftDetail(@RequestParam String id){
        if(StringUtils.isEmpty(id)){
            return new TableDataInfo();
        }
        startPage();
        List<TTaskWcsDetailVO> list=itTaskWcsDetailService.getShiftDetail(Long.parseLong(id));
        return getDataTable(list);
    }

    /**
     * 移位监控--强制执行
     * @param id
     * @return
     */
    @RequiresPermissions("inout:task:updateStock")
    @GetMapping("/updateStock")
    public AjaxResult updateStock(@RequestParam String id){
        if(StringUtils.isEmpty(id)){
            return  AjaxResult.error("参数错误");
        }
        return itTaskWcsDetailService.updateStock(Long.parseLong(id));
    }

    /**
     * 双伸位移库
     * @param taskWcsVO
     * @return
     */
    @PostMapping("/executeMoveDoubleEx")
    public AjaxResult executeMoveDoubleEx(@RequestBody TTaskWcsVO taskWcsVO){
        if(taskWcsVO.getId() == null){
            return  AjaxResult.error("参数错误");
        }
        TTaskWcs byId = tTaskWcsService.getById(taskWcsVO.getId());
        if (byId == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(byId.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        BeanUtils.copyBeanProp(taskWcsVO,byId);
        return tTaskWcsService.executeTaskMove(taskWcsVO);
    }

    /**
     * wcs作废
     * @param taskWcs
     * @return
     */
    @RequiresPermissions("inout:task:cancellation")
    @Log(title = "wcs作废", businessType = BusinessType.INSERT)
    @PostMapping("/delivery/cancellation")
    public AjaxResult cancellationDelivery(@RequestBody TTaskWcs taskWcs) {
        if (taskWcs.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return itTaskWcsDetailService.cancellationDelivery(taskWcs);
    }


    /**
     * 更新拣货任务优先级
     * @param id
     * @param priority
     * @return
     */
    @GetMapping("/updatePriority")
    public AjaxResult updatePriority(Long id,String priority) {
        return itTaskWcsDetailService.updatePriority(id, priority);
    }


    /**
     * 出库拣货任务作废
     * @param taskNo
     * @return
     */
    @GetMapping("/out/cancellation")
    public AjaxResult outCancellation(String taskNo) {
        return itTaskWcsDetailService.outCancellation(taskNo);
    }


    /**
     * 移库任务作废
     * @param taskNo
     * @return
     */
    @GetMapping("/move/cancellation")
    public AjaxResult moveCancellation(String taskNo) {
        return itTaskWcsDetailService.moveCancellation(taskNo);
    }


    /**
     * WCS入库任务重新执行
     * @param taskWcs
     * @return
     */
    @Log(title = "WCS入库任务重新执行", businessType = BusinessType.INSERT)
    @PostMapping("/enforcementDelivery")
    public AjaxResult enforcementDelivery(@RequestBody TTaskWcs taskWcs) {
        if (taskWcs.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.enforcementDelivery(taskWcs);
    }


    /**
     * WCS出库重新执行
     * @param taskWcs
     * @return
     */
    @Log(title = "WCS出库任务重新执行", businessType = BusinessType.INSERT)
    @PostMapping("/enforcementDelivery/out")
    public AjaxResult enforcementDeliveryOut(@RequestBody TTaskWcs taskWcs) {
        if (taskWcs.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.enforcementDeliveryOut(taskWcs);
    }

    /**
     * 载具出入库强制执行
     * @param taskWcsVO
     * @return
     */
    @Log(title = "载具出入库强制执行", businessType = BusinessType.UPDATE)
    @PostMapping("/executeTray")
    public AjaxResult executeTray(@RequestBody TTaskWcsVO taskWcsVO) {
        if(taskWcsVO.getId() == null){
            return AjaxResult.error("请选择要执行的任务");
        }
        TTaskWcs byId = tTaskWcsService.getById(taskWcsVO.getId());
        if (byId == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.WCS_EXECUTE_STATUS_END.equals(byId.getTaskStatus())) {
            return AjaxResult.error("当前状态不可强制执行");
        }
        BeanUtils.copyBeanProp(taskWcsVO, byId);
        return AjaxResult.success(trayService.completeTrayBack(taskWcsVO));
    }
}
