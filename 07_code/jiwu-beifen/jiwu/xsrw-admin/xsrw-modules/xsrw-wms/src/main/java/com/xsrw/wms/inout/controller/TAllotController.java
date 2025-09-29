package com.xsrw.wms.inout.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TAllot;
import com.xsrw.wms.inout.domain.vo.TAllotVO;
import com.xsrw.wms.inout.service.ITAllotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 调拨单Controller
 *
 * @author zjj
 * @date 2023-06-26
 */
@RestController
@RequestMapping("/allot")
public class TAllotController extends BaseController
{
    @Autowired
    private ITAllotService tAllotService;

    /**
     * 查询调拨单列表
     */
    @RequiresPermissions("inout:allot:list")
    @GetMapping("/list")
    public TableDataInfo list(TAllot tAllot)
    {
        startPage();
        List<TAllotVO> list = tAllotService.selectTAllotList(tAllot);
        return getDataTable(list);
    }

    /**
     * 导出调拨单列表
     */
    @RequiresPermissions("inout:allot:export")
    @Log(title = "调拨单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAllot tAllot)
    {
        List<TAllotVO> list = tAllotService.selectTAllotList(tAllot);
        ExcelUtil<TAllotVO> util = new ExcelUtil<>(TAllotVO.class);
        util.exportExcel(response, list, "调拨单数据");
    }

    /**
     * 获取调拨单详细信息
     */
    @RequiresPermissions("inout:allot:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tAllotService.selectTAllotById(id));
    }

    /**
     * 新增调拨单
     */
    @RequiresPermissions("inout:allot:add")
    @Log(title = "调拨单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody TAllot tAllot)
    {
        return tAllotService.insertTAllot(tAllot);
    }

    /**
     * 修改调拨单
     */
    @RequiresPermissions("inout:allot:edit")
    @Log(title = "调拨单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAllot tAllot)
    {
        return toAjax(tAllotService.updateTAllot(tAllot));
    }

    /**
     * 删除调拨单
     */
    @RequiresPermissions("inout:allot:remove")
    @Log(title = "调拨单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tAllotService.deleteTAllotByIds(ids));
    }


    /**
     * 调拨生成对应的出库计划、入库计划
     * @param id
     * @return
     */
    @GetMapping(value = "/createDelivery")
    public AjaxResult createDelivery(Long id,String remark,String allotStatus){
        return tAllotService.createDelivery(id,remark,allotStatus);
    }
}
