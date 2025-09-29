package com.xsrw.wms.equipment.controller;

import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.equipment.domain.DInspectionItems;
import com.xsrw.wms.equipment.service.IDInspectionItemsService;
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
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.service.IWmsEquipmentService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 设备台账Controller
 *
 * @author zjj
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/equipment")
public class WmsEquipmentController extends BaseController
{
    @Autowired
    private IWmsEquipmentService wmsEquipmentService;
    @Autowired
    private IDInspectionItemsService idInspectionItemsService;

    /**
     * 查询设备台账列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
//    @RequiresPermissions("base:equipment:list")
    @GetMapping("/list")
    public TableDataInfo list(WmsEquipment wmsEquipment)
    {
        startPage();
        List<WmsEquipment> list = wmsEquipmentService.selectWmsEquipmentList(wmsEquipment);
        return getDataTable(list);
    }

    /**
     * 查询设备台账列表
     */
    @GetMapping("/selectList")
    public TableDataInfo selectList(WmsEquipment wmsEquipment)
    {
        startPage();
        List<WmsEquipment> list = wmsEquipmentService.selectWmsEquipmentList(wmsEquipment);
        for (WmsEquipment equipment : list) {//设置筛选标准字段
            List<DInspectionItems> isadd = idInspectionItemsService.isadd(equipment.getId().toString());
            if (isadd.size()>0){
                equipment.setInspectionItems(isadd.get(0).getName());
            }
        }
        List<WmsEquipment> collect = list.stream().filter(d -> d.getInspectionItems() != null).collect(Collectors.toList());//过滤检验标准为空的数据
        return getDataTable(collect);
    }
    /**
     * 导出设备台账列表
     */
    @RequiresPermissions("base:equipment:export")
    @Log(title = "设备台账", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WmsEquipment wmsEquipment)
    {
        List<WmsEquipment> list = wmsEquipmentService.selectWmsEquipmentList(wmsEquipment);
        ExcelUtil<WmsEquipment> util = new ExcelUtil<WmsEquipment>(WmsEquipment.class);
        util.exportExcel(response, list, "设备台账数据");
    }

    /**
     * 获取设备台账详细信息
     */
    @RequiresPermissions("base:equipment:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wmsEquipmentService.selectWmsEquipmentById(id));
    }

    /**
     * 新增设备台账
     */
    @RequiresPermissions("base:equipment:add")
    @Log(title = "设备台账", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WmsEquipment wmsEquipment)
    {
        return wmsEquipmentService.insertWmsEquipment(wmsEquipment);
    }

    /**
     * 修改设备台账
     */
    @RequiresPermissions("base:equipment:edit")
    @Log(title = "设备台账", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WmsEquipment wmsEquipment)
    {
        return toAjax(wmsEquipmentService.updateWmsEquipment(wmsEquipment));
    }

    /**
     * 删除设备台账
     */
    @RequiresPermissions("base:equipment:remove")
    @Log(title = "设备台账", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wmsEquipmentService.deleteWmsEquipmentByIds(ids));
    }

    /**
     * 获取设备下拉列表
     * @param wmsEquipment
     * @return
     */
    @GetMapping("/getSimpleList")
    public AjaxResult getSimpleList(WmsEquipment wmsEquipment)
    {
        List<WmsEquipment> list = wmsEquipmentService.selectWmsEquipmentList(wmsEquipment);
        return AjaxResult.success(list);
    }

}
