package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.inout.service.ITTaskOutService;
import com.xsrw.wms.stock.domain.TStock;
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
import com.xsrw.wms.inout.domain.TMergeDelivery;
import com.xsrw.wms.inout.service.ITMergeDeliveryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 波次计划Controller
 *
 * @author zjj
 * @date 2023-06-25
 */
@RestController
@RequestMapping("/mergeDelivery")
public class TMergeDeliveryController extends BaseController
{
    @Autowired
    private ITMergeDeliveryService tMergeDeliveryService;

    @Autowired
    private ITTaskOutService tTaskOutService;

    /**
     * 查询波次计划列表
     */
    @RequiresPermissions("out:mergeDelivery:list")
    @GetMapping("/list")
    public TableDataInfo list(TMergeDelivery tMergeDelivery)
    {
        startPage();
        List<TMergeDelivery> list = tMergeDeliveryService.selectTMergeDeliveryList(tMergeDelivery);
        return getDataTable(list);
    }

    /**
     * 导出波次计划列表
     */
    @RequiresPermissions("out:mergeDelivery:export")
    @Log(title = "波次计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMergeDelivery tMergeDelivery)
    {
        List<TMergeDelivery> list = tMergeDeliveryService.selectTMergeDeliveryList(tMergeDelivery);
        ExcelUtil<TMergeDelivery> util = new ExcelUtil<TMergeDelivery>(TMergeDelivery.class);
        util.exportExcel(response, list, "波次计划数据");
    }

    /**
     * 获取波次计划详细信息
     */
    @RequiresPermissions("out:mergeDelivery:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tMergeDeliveryService.selectTMergeDeliveryById(id));
    }

    /**
     * 新增波次计划
     */
    @RequiresPermissions("out:mergeDelivery:add")
    @Log(title = "波次计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMergeDelivery tMergeDelivery)
    {
        return toAjax(tMergeDeliveryService.insertTMergeDelivery(tMergeDelivery));
    }

    /**
     * 创建波次
     * @param ids
     * @return
     */
    @GetMapping("/create")
    public AjaxResult create(Long [] ids)
    {
        return tMergeDeliveryService.createMergeDelivery(ids);
    }

    /**
     * 修改波次计划
     */
    @RequiresPermissions("out:mergeDelivery:edit")
    @Log(title = "波次计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMergeDelivery tMergeDelivery)
    {
        return toAjax(tMergeDeliveryService.updateTMergeDelivery(tMergeDelivery));
    }

    /**
     * 删除波次计划
     */
    @RequiresPermissions("out:mergeDelivery:remove")
    @Log(title = "波次计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tMergeDeliveryService.deleteTMergeDeliveryByIds(ids));
    }


    /**
     * 执行出库 生成任务及WCS相关
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/addTask")
    public AjaxResult add(@RequestBody TTaskOutVO tTaskOutVO){
        if(tTaskOutVO.getOutDeliveryDetailId() == null){
            return AjaxResult.error("请选择要执行的单子");
        }
        if(tTaskOutVO.gettTaskOutDetailListVOS().size() == 0){
            return AjaxResult.error("请选择要物料的载具");
        }
        return tMergeDeliveryService.insertTTaskOut(tTaskOutVO);
    }


    /**
     * 地堆拣货
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/addTaskPile")
    public AjaxResult addTaskPile(@RequestBody TTaskOutVO tTaskOutVO){
        return tMergeDeliveryService.addTaskPile(tTaskOutVO);
    }


    /**
     * 强制执行出库任务
     * @param tTaskWcsOutVO
     * @return
     */
    @PostMapping("/executeOutTask")
    public AjaxResult executeOutTask(@RequestBody TTaskWcsOutVO tTaskWcsOutVO) {
        if(tTaskWcsOutVO.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tMergeDeliveryService.executeOutTask(tTaskWcsOutVO);
    }


    /**
     * 执行出库选择载具列表--地堆
     * @param request
     * @param tStock
     * @return
     */
    @GetMapping("/groundPileTrayList")
    public AjaxResult groundPileTrayList(HttpServletRequest request, TStock tStock)
    {
        String id=request.getParameter("id");
        if(StringUtils.isEmpty(id)){
            throw  new ServiceException("参数错误！");
        }
        String materialId=request.getParameter("materialId");
        if(StringUtils.isEmpty(materialId)){
            throw  new ServiceException("参数错误，物料不可以为空！");
        }
        tStock.setLocationType("1");

        Map<String,Object> objectMap = tMergeDeliveryService.getOutDeliveryCount(id);
        objectMap.put("data",getDataTable(tTaskOutService.selectTTrayList(materialId,tStock)));
        return  AjaxResult.success(objectMap);
    }
}
