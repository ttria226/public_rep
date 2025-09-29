package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.dto.DemandCheckDTO;
import com.xsrw.wms.base.domain.dto.TLocationDTO;
import com.xsrw.wms.base.domain.vo.DemandCheckVO;
import com.xsrw.wms.base.domain.vo.ExcelLocationVO;
import com.xsrw.wms.base.domain.vo.TLocationVO;
import com.xsrw.wms.base.service.ITLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 库位Controller
 *
 * @author wxr
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/location")
public class TLocationController extends BaseController {
    @Autowired
    private ITLocationService tLocationService;

    /**
     * 查询库位列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
//    @RequiresPermissions("wms:location:list")
    @GetMapping("/list")
    public TableDataInfo list(TLocationDTO tLocation) {
        startPage();
        List<TLocationVO> list = tLocationService.selectTLocationList(tLocation);
        return getDataTable(list);
    }

    /**
     * 导出库位列表
     */
    @RequiresPermissions("wms:location:export")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TLocationDTO tLocation) {
        List<TLocationVO> list = tLocationService.selectTLocationList(tLocation);
        ExcelUtil<TLocationVO> util = new ExcelUtil<>(TLocationVO.class);
        util.exportExcel(response, list, "库位数据");
    }

    /**
     * 获取库位详细信息
     */
    @RequiresPermissions("wms:location:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tLocationService.selectTLocationById(id));
    }

    /**
     * 新增库位
     */
    @RequiresPermissions("wms:location:add")
    @Log(title = "库位", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TLocation tLocation) {
        return toAjax(tLocationService.insertTLocation(tLocation));
    }

    /**
     * 修改库位
     */
    @RequiresPermissions("wms:location:edit")
    @Log(title = "库位", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TLocation tLocation) {
        return toAjax(tLocationService.updateTLocation(tLocation));
    }

    /**
     * 删除库位
     */
    @RequiresPermissions("wms:location:remove")
    @Log(title = "库位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tLocationService.deleteTLocationByIds(ids));
    }

    /**
     * 库位批量修改
     *
     * @param location
     * @return
     */
    @RequiresPermissions("wms:location:plcUpdate")
    @PostMapping("/plcUpdate")
    public AjaxResult plcUpdate(@RequestBody TLocationDTO location) {
        return tLocationService.plcUpdate(location);
    }

    /**
     * 导入模板信息
     *
     * @param response
     */
    @RequiresPermissions("wms:location:exportdemo")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelLocationVO> util = new ExcelUtil<ExcelLocationVO>(ExcelLocationVO.class);
        util.exportExcel(response, new ArrayList<>(), "库位数据");
    }

    /**
     * 导入库位信息
     *
     * @param file
     * @return
     */
    @RequiresPermissions("wms:location:importData")
    @Log(title = "库位", businessType = BusinessType.EXPORT)
    @PostMapping("/importData")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tLocationService.importUnit(file);
    }

    /**
     * 启用禁用
     *
     * @param id
     * @param status
     * @return
     */
    @RequiresPermissions("wms:location:updateStatus")
    @GetMapping("/updateStatus")
    public AjaxResult updateStatusById(@RequestParam("id") Long id, @RequestParam("status") String status) {
        return tLocationService.updateStatusById(id, status);
    }

    /**
     * 获取某个仓库的最大排，列，层数
     */
    @PostMapping("/plcCount")
    public Map<String, Integer> plcCount(@RequestBody TLocation location) {
        Map<String, Integer> plcMap = tLocationService.plcCount(location);
        return (plcMap);
    }

    /**
     * 新增库位
     */
//    @RequiresPermissions("cims:location:padd")
    @Log(title = "批量添加库位", businessType = BusinessType.INSERT)
    @PostMapping("/padd")
    public AjaxResult padd(@RequestBody TLocationDTO locationDto) {
        return toAjax(tLocationService.pinsertTLocation(locationDto));
    }


    @Log(title = "获取空闲库位列表", businessType = BusinessType.INSERT)
    @GetMapping("/getOtherLocation")
    public AjaxResult getOtherLocation(Long locationId) {
        return AjaxResult.success(tLocationService.getOtherLocation(locationId));
    }

    /**
     * 获取库位实时使用详情
     *
     * @param locationRow
     * @param reservoirId
     * @return
     */
    @GetMapping("/getLocationCurrentDetail")
    public AjaxResult getLocationCurrentDetail(@RequestParam("locationRow") Integer locationRow,
                                               @RequestParam("reservoirId") Long reservoirId) {
        return AjaxResult.success(tLocationService.getLocationCurrentDetail(locationRow, reservoirId));
    }

    /**
     * 获取库位实时使用详情
     *
     * @param reservoirId
     * @return
     */
    @GetMapping("/getLocationListByReservoirId")
    public AjaxResult getLocationListByReservoirId(@RequestParam("reservoirId") Long reservoirId) {
        return AjaxResult.success(tLocationService.getLocationListByReservoirId(reservoirId));
    }

    @PutMapping("/updateLocationStatus")
    public AjaxResult updateLocationStatus(@RequestBody TLocation tLocation) {
        if (tLocation.getId() == null || tLocation.getId() <= 0) {
            return AjaxResult.error("库位编号不可以为空！");
        }
        TLocation location = tLocationService.getById(tLocation.getId());
        if (location == null) {
            return AjaxResult.error("库位编号不存在！");
        }
        //只有 标记出库和标记入库的支持库位状态的修改
        if (StringUtils.isNotEmpty(tLocation.getGoodsAllocationStatus())) {
            //货位状态(1,无货,2,有货,3,标记出库,4,标记入库)
            if (!location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_3) &&
                    !location.getGoodsAllocationStatus().equals(Constants.LOCATION_GOODS_ALLOCATION_STATUS_4)) {
                return AjaxResult.error("库位当前状态不支持修改！");
            }
        }
        return AjaxResult.success(tLocationService.updateTLocation(tLocation));
    }

    /**
     * 获取库位列表
     * @param tLocation
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TLocationDTO tLocation) {
        List<TLocationVO> list = tLocationService.getLocationList(tLocation);
        return AjaxResult.success(list);
    }

    /**
     * 选择列表
     *
     * @param tLocation
     * @return
     */
    @GetMapping("/selectPageList")
    public TableDataInfo selectPageList(TLocationDTO tLocation) {
        startPage();
        List<TLocationVO> trays = tLocationService.getLocationList(tLocation);
        return getDataTable(trays);
    }

    /**
     * 需盘点列表
     * @param request
     * @return
     */
    @GetMapping("/demandCheck/list")
    public TableDataInfo demandCheckList(DemandCheckDTO request) {
        startPage();
        List<DemandCheckVO> demandCheckList = tLocationService.demandCheckList(request);
        return getDataTable(demandCheckList);
    }

    /**
     * 需盘点列表导出
     * @param response
     * @param request
     */
    @PostMapping("/demandCheck/export")
    public void demandCheckExport(HttpServletResponse response,DemandCheckDTO request) {
        List<DemandCheckVO> demandCheckList = tLocationService.demandCheckList(request);
        ExcelUtil<DemandCheckVO> util = new ExcelUtil<>(DemandCheckVO.class);
        util.exportExcel(response, demandCheckList, "sheet1");
    }

    /**
     * 需盘点修改货位状态(标记有货/标记无货)
     */
    @PostMapping("/updateGoodsAllocationStatus")
    public AjaxResult updateGoodsAllocationStatus(@RequestBody TLocation tLocation) {
        return tLocationService.updateGoodsAllocationStatus(tLocation);
    }
}
