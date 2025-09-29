package com.xsrw.wms.dispatch.controller;


import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.wms.dispatch.domain.vo.AllotDispatchVO;
import com.xsrw.wms.dispatch.domain.vo.BusinessMonitorsVO;
import com.xsrw.wms.dispatch.domain.vo.WareHouseStatusVO;
import com.xsrw.wms.dispatch.service.IDispatchCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 调度中台controller
 * @Author tyk
 * @Date 2023-06-25
 */
@RestController
@RequestMapping("/dispatchCenter")
public class DispatchCenterController extends BaseController {


    @Autowired
    private IDispatchCenterService dispatchCenterService;


    /**
     * 业务监控列表
     * @param taskNo 任务编号
     * @param materialName 物料名称
     * @param taskStatus 执行状态（1未执行,2执行中,3执行完成,4执行失败）
     * @return
     */
    //    @RequiresPermissions("dispatchCenter:businessMonitors:list")
    @GetMapping("/businessMonitors/list")
    public TableDataInfo businessMonitorsList(String taskNo,String materialName,String taskStatus) {
        startPage();
        List<BusinessMonitorsVO> businessMonitorsList = dispatchCenterService.businessMonitorsList(taskNo,materialName,taskStatus);
        return getDataTable(businessMonitorsList);
    }

    /**
     * 业务监控列表导出
     * @param response
     * @param taskNo 任务编号
     * @param materialName 物料名称
     * @param taskStatus 执行状态（1未执行,2执行中,3执行完成,4执行失败）
     */
//    @RequiresPermissions("dispatchCenter:businessMonitors:export")
    @PostMapping("/businessMonitors/export")
    public void businessMonitorsExport(HttpServletResponse response,String taskNo,String materialName,String taskStatus) {
        List<BusinessMonitorsVO> businessMonitorsList = dispatchCenterService.businessMonitorsList(taskNo,materialName,taskStatus);
        ExcelUtil<BusinessMonitorsVO> util = new ExcelUtil<>(BusinessMonitorsVO.class);
        util.exportExcel(response, businessMonitorsList, "sheet1");
    }

    /**
     * 入出调度列表
     * @param taskNo 任务编号
     * @param materialName 物料名称
     * @param taskStatus 执行状态（1未执行,2执行中,3执行完成,4执行失败）
     * @return
     */
    //    @RequiresPermissions("dispatchCenter:inOutDispatch:list")
    @GetMapping("/inOutDispatch/list")
    public TableDataInfo inOutDispatchList(String taskNo,String materialName,String taskStatus) {
        startPage();
        List<BusinessMonitorsVO> businessMonitorsList = dispatchCenterService.businessMonitorsList(taskNo,materialName,taskStatus);
        return getDataTable(businessMonitorsList);
    }

    /**
     * 入出调度列表导出
     * @param response
     * @param taskNo 任务编号
     * @param materialName 物料名称
     * @param taskStatus 执行状态（1未执行,2执行中,3执行完成,4执行失败）
     */
//    @RequiresPermissions("dispatchCenter:inOutDispatch:export")
    @PostMapping("/inOutDispatch/export")
    public void inOutDispatchExport(HttpServletResponse response,String taskNo,String materialName,String taskStatus) {
        List<BusinessMonitorsVO> businessMonitorsList = dispatchCenterService.businessMonitorsList(taskNo,materialName,taskStatus);
        ExcelUtil<BusinessMonitorsVO> util = new ExcelUtil<>(BusinessMonitorsVO.class);
        util.exportExcel(response, businessMonitorsList, "sheet1");
    }

    /**
     * 仓库状态列表
     * @param materialName 物料名称
     * @param deptId 部门Id
     * @return
     */
    //    @RequiresPermissions("dispatchCenter:wareHouseStatus:list")
    @GetMapping("/wareHouseStatus/list")
    public TableDataInfo wareHouseStatusList(String materialName,Integer deptId) {
        startPage();
        List<WareHouseStatusVO> wareHouseStatusList = dispatchCenterService.wareHouseStatusList(materialName,deptId);
        return getDataTable(wareHouseStatusList);
    }

    /**
     * 仓库状态列表导出
     * @param response
     * @param materialName
     */
//    @RequiresPermissions("dispatchCenter:wareHouseStatus:export")
    @PostMapping("/wareHouseStatus/export")
    public void wareHouseStatusExport(HttpServletResponse response,String materialName,Integer deptId) {
        List<WareHouseStatusVO> wareHouseStatusList = dispatchCenterService.wareHouseStatusList(materialName,deptId);
        ExcelUtil<WareHouseStatusVO> util = new ExcelUtil<>(WareHouseStatusVO.class);
        util.exportExcel(response, wareHouseStatusList, "sheet1");
    }

    /**
     * 调拨调度列表
     * @param code 调拨单号
     * @param materialName 物料名称
     * @param allotStatus 状态 1待审核、2等待出库、3等待入库、4已完成
     * @return
     */
    //    @RequiresPermissions("dispatchCenter:allotDispatch:list")
    @GetMapping("/allotDispatch/list")
    public TableDataInfo allotDispatchList(String code,String materialName,String allotStatus) {
        startPage();
        List<AllotDispatchVO> allotDispatchList = dispatchCenterService.allotDispatchList(code,materialName,allotStatus);
        return getDataTable(allotDispatchList);
    }

    /**
     * 调拨调度列表导出
     * @param response
     * @param code 调拨单号
     * @param materialName 物料名称
     * @param allotStatus 状态 1待审核、2等待出库、3等待入库、4已完成
     */
//    @RequiresPermissions("dispatchCenter:allotDispatch:export")
    @PostMapping("/allotDispatch/export")
    public void allotDispatchExport(HttpServletResponse response,String code,String materialName,String allotStatus) {
        List<AllotDispatchVO> allotDispatchList = dispatchCenterService.allotDispatchList(code,materialName,allotStatus);
        ExcelUtil<AllotDispatchVO> util = new ExcelUtil<>(AllotDispatchVO.class);
        util.exportExcel(response, allotDispatchList, "sheet1");
    }
}
