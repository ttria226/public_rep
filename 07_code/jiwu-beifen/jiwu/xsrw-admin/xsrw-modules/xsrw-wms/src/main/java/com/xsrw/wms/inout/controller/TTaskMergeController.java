package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.inout.domain.vo.TTaskMergeVO;
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
import com.xsrw.wms.inout.domain.TTaskMerge;
import com.xsrw.wms.inout.service.ITTaskMergeService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 波次单任务详情Controller
 *
 * @author zjj
 * @date 2023-06-26
 */
@RestController
@RequestMapping("/mergeTask")
public class TTaskMergeController extends BaseController
{
    @Autowired
    private ITTaskMergeService tTaskMergeService;

    /**
     * 查询出库任务详情列表
     */
    @RequiresPermissions("inout:mergeTask:list")
    @GetMapping("/list")
    public TableDataInfo list(TTaskMerge tTaskMerge)
    {
        List<TTaskMergeVO> list = tTaskMergeService.selectTTaskMergeList(tTaskMerge);
        return getDataTable(list);
    }

//    /**
//     * 导出出库任务详情列表
//     */
//    @RequiresPermissions("inout:mergeTask:export")
//    @Log(title = "出库任务详情", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TTaskMerge tTaskMerge)
//    {
//        List<TTaskMerge> list = tTaskMergeService.selectTTaskMergeList(tTaskMerge);
//        ExcelUtil<TTaskMerge> util = new ExcelUtil<TTaskMerge>(TTaskMerge.class);
//        util.exportExcel(response, list, "出库任务详情数据");
//    }

    /**
     * 获取出库任务详情详细信息
     */
    @RequiresPermissions("inout:mergeTask:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tTaskMergeService.selectTTaskMergeById(id));
    }

    /**
     * 新增出库任务详情
     */
    @RequiresPermissions("inout:mergeTask:add")
    @Log(title = "出库任务详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskMerge tTaskMerge)
    {
        return toAjax(tTaskMergeService.insertTTaskMerge(tTaskMerge));
    }

    /**
     * 修改出库任务详情
     */
    @RequiresPermissions("inout:mergeTask:edit")
    @Log(title = "出库任务详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskMerge tTaskMerge)
    {
        return toAjax(tTaskMergeService.updateTTaskMerge(tTaskMerge));
    }

    /**
     * 删除出库任务详情
     */
    @RequiresPermissions("inout:mergeTask:remove")
    @Log(title = "出库任务详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTaskMergeService.deleteTTaskMergeByIds(ids));
    }


    /**
     * 执行出库--自动分配载具
     * @return
     */
    @GetMapping("/traylist/voluntarily")
    public AjaxResult traylistVoluntarily(Long mergeDeliveryId,Long materialId)
    {
        return tTaskMergeService.voluntarily(mergeDeliveryId, materialId);
    }

    /**
     * 地堆拣货--自动分配载具
     * @return
     */
    @GetMapping("/groundPileTrayList/voluntarily")
    public AjaxResult groundPileTrayListVoluntarily(Long mergeDeliveryId,Long materialId)
    {
        return tTaskMergeService.groundPileTrayListVoluntarily(mergeDeliveryId, materialId);
    }



}
