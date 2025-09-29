package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.inout.domain.vo.TMergeDeliveryDetailVO;
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
import com.xsrw.wms.inout.domain.TMergeDeliveryDetail;
import com.xsrw.wms.inout.service.ITMergeDeliveryDetailService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 波次计划详情Controller
 *
 * @author zjj
 * @date 2023-06-25
 */
@RestController
@RequestMapping("/mergeDeliveryDetail")
public class TMergeDeliveryDetailController extends BaseController
{
    @Autowired
    private ITMergeDeliveryDetailService tMergeDeliveryDetailService;

    /**
     * 查询波次计划详情列表
     */
    @RequiresPermissions("out:mergeDeliveryDetail:list")
    @GetMapping("/list")
    public TableDataInfo list(TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        startPage();
        List<TMergeDeliveryDetailVO> list = tMergeDeliveryDetailService.selectTMergeDeliveryDetailList(tMergeDeliveryDetail);
        return getDataTable(list);
    }

//    /**
//     * 导出波次计划详情列表
//     */
//    @RequiresPermissions("out:mergeDeliveryDetail:export")
//    @Log(title = "波次计划详情", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TMergeDeliveryDetail tMergeDeliveryDetail)
//    {
//        List<TMergeDeliveryDetail> list = tMergeDeliveryDetailService.selectTMergeDeliveryDetailList(tMergeDeliveryDetail);
//        ExcelUtil<TMergeDeliveryDetail> util = new ExcelUtil<TMergeDeliveryDetail>(TMergeDeliveryDetail.class);
//        util.exportExcel(response, list, "波次计划详情数据");
//    }

    /**
     * 获取波次计划详情详细信息
     */
    @RequiresPermissions("out:mergeDeliveryDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMergeDeliveryDetailService.selectTMergeDeliveryDetailById(id));
    }

    /**
     * 新增波次计划详情
     */
    @RequiresPermissions("out:mergeDeliveryDetail:add")
    @Log(title = "波次计划详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        return toAjax(tMergeDeliveryDetailService.insertTMergeDeliveryDetail(tMergeDeliveryDetail));
    }

    /**
     * 修改波次计划详情
     */
    @RequiresPermissions("out:mergeDeliveryDetail:edit")
    @Log(title = "波次计划详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        return toAjax(tMergeDeliveryDetailService.updateTMergeDeliveryDetail(tMergeDeliveryDetail));
    }

    /**
     * 删除波次计划详情
     */
    @RequiresPermissions("out:mergeDeliveryDetail:remove")
    @Log(title = "波次计划详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMergeDeliveryDetailService.deleteTMergeDeliveryDetailByIds(ids));
    }
}
