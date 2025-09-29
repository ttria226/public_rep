package com.xsrw.wms.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.domain.TCheckHistory;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.vo.CheckDeliveryVO;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.check.service.ITCheckHistoryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.service.ITStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 盘点计划Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/checkDelivery")
public class TCheckDeliveryController extends BaseController
{
    @Autowired
    private ITCheckDeliveryService tCheckDeliveryService;

    @Autowired
    private ITTaskDetailService taskDetailService;



    /**
     * 查询盘点计划列表
     */
    @RequiresPermissions("check:checkDelivery:list")
    @GetMapping("/list")
    public TableDataInfo list(CheckDeliveryDTO checkDelivery)
    {
        startPage();
        List<CheckDeliveryVO> list = tCheckDeliveryService.selectTCheckDeliveryList(checkDelivery);
        return getDataTable(list);
    }

    /**
     * 导出盘点计划列表
     */
    @RequiresPermissions("check:checkDelivery:export")
    @Log(title = "盘点计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckDeliveryDTO checkDelivery)
    {
        List<CheckDeliveryVO> list = tCheckDeliveryService.selectTCheckDeliveryList(checkDelivery);
        ExcelUtil<CheckDeliveryVO> util = new ExcelUtil<>(CheckDeliveryVO.class);
        util.exportExcel(response, list, "盘点计划数据");
    }

    /**
     * 获取盘点计划详细信息
     */
    @RequiresPermissions("check:checkDelivery:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckDeliveryService.selectTCheckDeliveryById(id));
    }

    /**
     * 新增盘点计划
     */
    @RequiresPermissions("check:checkDelivery:add")
    @Log(title = "盘点计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Map<String,Object> data)
    {
        return tCheckDeliveryService.insertTCheckDelivery(data);
    }

//    /**
//     * 修改盘点计划
//     */
//    @RequiresPermissions("check:checkDelivery:edit")
//    @Log(title = "盘点计划", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TCheckDelivery tCheckDelivery)
//    {
//        return toAjax(tCheckDeliveryService.updateTCheckDelivery(tCheckDelivery));
//    }

    /**
     * 删除盘点计划
     */
    @RequiresPermissions("check:checkDelivery:remove")
    @Log(title = "盘点计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckDeliveryService.deleteTCheckDeliveryByIds(ids));
    }

    /**
     * @description: 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    @Transactional
    @PostMapping("/checkdelivery/submit")
    public AjaxResult checkdeliverySubmit(@RequestBody CheckDeliveryDTO checkDeliveryDTO){
        return tCheckDeliveryService.checkdeliverySubmit(checkDeliveryDTO);
    }


    /**
     * @description: 盘点详情
     * @param trayCode
     * @return
     */
    @GetMapping("/checkdelivery/detail")
    public AjaxResult executeTask(Long taskId,String trayCode,String checkType,
                                  @RequestParam(name = "batch",required = false)String batch,String rfid){
        return tCheckDeliveryService.executeTask(taskId, trayCode, checkType, batch,rfid);
    }


    @GetMapping("/getDropdownData")
    public AjaxResult getDropdownData(@RequestParam String taskId){
        return  taskDetailService.getDropdownData(Long.parseLong(taskId));
    }
}
