package com.xsrw.wms.report.controller;


import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.report.domain.dto.*;
import com.xsrw.wms.report.domain.vo.*;
import com.xsrw.wms.report.service.ITReportCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description: 报表中心
 * @Author XMING
 * @Date 2022-06-06
 */
@RestController
@RequestMapping("/report/center")
public class ReportCenterController extends BaseController {


    @Autowired
    private ITReportCenterService itReportCenterService;


    /**
     * @param reportCenterDTO
     * @return cn.haiwei.common.core.web.page.TableDataInfo
     * @description: 物料收发汇总
     * @author XMING
     * @date 2022-06-08
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/list")
    public TableDataInfo list(ReportCenterDTO reportCenterDTO) {
        startPage();
        List<Map<String, Object>> data = itReportCenterService.materilaCollect(reportCenterDTO);
        TableDataInfo dataTable = getDataTable(data);
        return dataTable;
    }


    /**
     * @return cn.haiwei.common.core.web.page.TableDataInfo
     * @description: 报表中心-库存上下限预警
     * @author XMING
     * @date 2022-06-10
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/stockWarning")
    public TableDataInfo list(ValidityWarningReportVO vo) {
        startPage();
        List<StockMainReportVO> stockWarning = itReportCenterService.stockWarning(vo);
        TableDataInfo dataTable = getDataTable(stockWarning);
        return dataTable;
    }

    @RequiresPermissions("wms:StockMain:export")
    @Log(title = "库存上下限预警", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ValidityWarningReportVO vo) {
        List<StockMainReportVO> stockWarning = itReportCenterService.stockWarning(vo);
        ExcelUtil<StockMainReportVO> util = new ExcelUtil<StockMainReportVO>(StockMainReportVO.class);
        util.exportExcel(response, stockWarning, "库存上下限预警");
    }

    @RequiresPermissions("wms:MaterialSummary:export")
    @Log(title = "库存收发汇总表", businessType = BusinessType.EXPORT)
    @PostMapping("/MaterialSummary/export")
    public void MaterialSummaryExport(HttpServletResponse response, ReportCenterDTO reportCenterDTO) {
        List<Map<String, Object>> data = itReportCenterService.materilaCollect(reportCenterDTO);
        List<ExcelMaterialSummaryVO> list = itReportCenterService.stockMainService(data);
        ExcelUtil<ExcelMaterialSummaryVO> util = new ExcelUtil<ExcelMaterialSummaryVO>(ExcelMaterialSummaryVO.class);
        util.exportExcel(response, list, "库存上下限预警");
    }

    /**
     * 补货列表
     *
     * @param materailCode
     * @param materialName
     * @param warehouseId
     * @return
     */
    @GetMapping("/stockWarning/replenishment")
    public TableDataInfo getReplenishment(String materailCode, String materialName, Long warehouseId) {
        startPage();
        List<ReplenishmentReportVO> stockWarning = itReportCenterService.getReplenishment(materailCode, materialName, warehouseId);
        TableDataInfo dataTable = getDataTable(stockWarning);
        return dataTable;
    }

    /**
     * 补货列表导出
     *
     * @param response
     * @param materailCode
     * @param materialName
     * @param warehouseId
     */
    @PostMapping("/stockWarning/replenishment/export")
    public void replenishmentExport(HttpServletResponse response, String materailCode, String materialName, Long warehouseId) {
        List<ReplenishmentReportVO> stockWarning = itReportCenterService.getReplenishment(materailCode, materialName, warehouseId);
        ExcelUtil<ReplenishmentReportVO> util = new ExcelUtil<>(ReplenishmentReportVO.class);
        util.exportExcel(response, stockWarning, "库存上下限预警");
    }

    /**
     * 补货列表
     *
     * @return
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/stockWarning/validityWarning")
    public TableDataInfo validityWarning(ValidityWarningReportVO vo) {
        startPage();
        List<ValidityWarningReportVO> stockWarning = itReportCenterService.getValidityWarning(vo);
        TableDataInfo dataTable = getDataTable(stockWarning);
        return dataTable;
    }

    /**
     * 补货列表导出
     *
     * @param response
     */
    @PostMapping("/stockWarning/validityWarning/export")
    public void validityWarning(HttpServletResponse response, ValidityWarningReportVO vo) {
        List<ValidityWarningReportVO> stockWarning = itReportCenterService.getValidityWarning(vo);
        ExcelUtil<ValidityWarningReportVO> util = new ExcelUtil<>(ValidityWarningReportVO.class);
        util.exportExcel(response, stockWarning, "库存上下限预警");
    }


    /**
     * 出入库流水报表
     * @param inOutStatementDTO
     * @return
     */
//    @RequiresPermissions("wms:inOutStatement:list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/inOutStatement/list")
    public TableDataInfo inOutStatementList(InOutStatementDTO inOutStatementDTO) {
        startPage();
        List<InOutStatementVO> inOutStatementList = itReportCenterService.inOutStatementList(inOutStatementDTO);
        return getDataTable(inOutStatementList);
    }

    /**
     * 出入库流水报表导出
     * @param inOutStatementDTO
     * @return
     */
//    @RequiresPermissions("wms:inOutStatement:export")
    @PostMapping("/inOutStatement/export")
    public void inOutStatementListExport(HttpServletResponse response,InOutStatementDTO inOutStatementDTO) {
        itReportCenterService.inOutStatementListExport(response,inOutStatementDTO);
    }

    /**
     * 供应商质量统计报表
     * @param contactsUnitName 供应商名称
     * @param contactsUnitContact 联系人
     * @return
     */
//    @RequiresPermissions("wms:qualityReport:list")
    @GetMapping("/qualityReport/list")
    public TableDataInfo qualityReportList(String contactsUnitName,String contactsUnitContact) {
        startPage();
        List<QualityReportVO> qualityReportList = itReportCenterService.qualityReportList(contactsUnitName,contactsUnitContact);
        return getDataTable(qualityReportList);
    }

    /**
     * 供应商质量统计报表导出
     * @param response
     * @param contactsUnitName
     * @param contactsUnitContact 联系人
     */
//    @RequiresPermissions("wms:qualityReport:export")
    @PostMapping("/qualityReport/export")
    public void qualityReportExport(HttpServletResponse response,String contactsUnitName,String contactsUnitContact) {
        List<QualityReportVO> qualityReportList = itReportCenterService.qualityReportList(contactsUnitName,contactsUnitContact);
        ExcelUtil<QualityReportVO> util = new ExcelUtil<>(QualityReportVO.class);
        util.exportExcel(response, qualityReportList, "sheet1");

    }

    /**
     * 供应商交付统计报表
     * @param contactsUnitName
     * @param contactsUnitContact 联系人
     * @return
     */
//    @RequiresPermissions("wms:deliveryReport:list")
    @GetMapping("/deliveryReport/list")
    public TableDataInfo deliveryReportList(String contactsUnitName,String contactsUnitContact) {
        startPage();
        List<DeliveryReportVO> deliveryReportList = itReportCenterService.deliveryReportList(contactsUnitName,contactsUnitContact);
        return getDataTable(deliveryReportList);
    }

    /**
     * 供应商交付统计报表导出
     * @param response
     * @param contactsUnitName
     * @param contactsUnitContact 联系人
     */
//    @RequiresPermissions("wms:deliveryReport:export")
    @PostMapping("/deliveryReport/export")
    public void deliveryReportExport(HttpServletResponse response,String contactsUnitName,String contactsUnitContact) {
        List<DeliveryReportVO> deliveryReportList = itReportCenterService.deliveryReportList(contactsUnitName,contactsUnitContact);
        ExcelUtil<DeliveryReportVO> util = new ExcelUtil<>(DeliveryReportVO.class);
        util.exportExcel(response, deliveryReportList, "sheet1");

    }

    /**
     * 呆滞品预警列表
     * @param materialCode 物料编码
     * @param materialName 物料名称
     * @return
     */
//    @RequiresPermissions("wms:deadStockWarning:list")
    @GetMapping("/deadStockWarning/list")
    public TableDataInfo deadStockWarningList(String materialCode,String materialName) {
        startPage();
        List<DeadStockWarningVO> deadStockWarningList = itReportCenterService.deadStockWarningList(materialCode,materialName);
        return getDataTable(deadStockWarningList);
    }

    /**
     * 呆滞品预警列表导出
     * @param materialCode 物料编码
     * @param materialName 物料名称
     * @return
     */
//    @RequiresPermissions("wms:deadStockWarning:export")
    @PostMapping("/deadStockWarning/export")
    public void deadStockWarningExport(HttpServletResponse response,String materialCode,String materialName) {
        List<DeadStockWarningVO> deadStockWarningList = itReportCenterService.deadStockWarningList(materialCode,materialName);
        ExcelUtil<DeadStockWarningVO> util = new ExcelUtil<>(DeadStockWarningVO.class);
        util.exportExcel(response, deadStockWarningList, "sheet1");

    }

    /**
     * 效率统计列表
     * @param request
     * @return
     */
//    @RequiresPermissions("wms:efficiencyStatistics:list")
    @GetMapping("/efficiencyStatistics/list")
    public TableDataInfo efficiencyStatisticsList(EfficiencyStatisticsDTO request) {
        startPage();
        List<EfficiencyStatisticsVO> efficiencyStatisticsList = itReportCenterService.efficiencyStatisticsList(request);
        return getDataTable(efficiencyStatisticsList);
    }

    /**
     * 效率统计列表导出
     * @param request
     * @return
     */
//    @RequiresPermissions("wms:efficiencyStatistics:export")
    @PostMapping("/efficiencyStatistics/export")
    public void efficiencyStatisticsExport(HttpServletResponse response,EfficiencyStatisticsDTO request) {
        List<EfficiencyStatisticsVO> efficiencyStatisticsList = itReportCenterService.efficiencyStatisticsList(request);
        ExcelUtil<EfficiencyStatisticsVO> util = new ExcelUtil<>(EfficiencyStatisticsVO.class);
        util.exportExcel(response, efficiencyStatisticsList, "sheet1");
    }

    /**
     * 工作统计列表
     * @param request
     * @return
     */
//    @RequiresPermissions("wms:workStatistics:list")
    @GetMapping("/workStatistics/list")
    public TableDataInfo workStatisticsList(WorkStatisticsDTO request) {
        startPage();
        List<WorkStatisticsListsVO> workStatisticsList = itReportCenterService.workStatisticsList(request);
        return getDataTable(workStatisticsList);
    }

    /**
     * 工作统计列表导出
     * @param request
     * @return
     */
//    @RequiresPermissions("wms:efficiencyStatistics:export")
    @PostMapping("/workStatistics/export")
    public void workStatisticsExport(HttpServletResponse response,WorkStatisticsDTO request) {
        List<WorkStatisticsListsVO> workStatisticsList = itReportCenterService.workStatisticsList(request);
        ExcelUtil<WorkStatisticsListsVO> util = new ExcelUtil<>(WorkStatisticsListsVO.class);
        util.exportExcel(response, workStatisticsList, "sheet1");
    }

    /**
     * 库龄分析列表
     * @param request
     * @return
     */
//    @RequiresPermissions("wms:wareHouseAgeAnalyse:list")
    @GetMapping("/wareHouseAgeAnalyse/list")
    public TableDataInfo wareHouseAgeAnalyseList(WareHouseAgeAnalyseDTO request) {
        startPage();
        List<WareHouseAgeAnalyseVO> wareHouseAgeAnalyseList = itReportCenterService.wareHouseAgeAnalyseList(request);
        return getDataTable(wareHouseAgeAnalyseList);
    }

    /**
     * 库龄分析列表导出
     * @param response
     * @param request
     */
//    @RequiresPermissions("wms:wareHouseAgeAnalyse:export")
    @PostMapping("/wareHouseAgeAnalyse/export")
    public void wareHouseAgeAnalyseExport(HttpServletResponse response,WareHouseAgeAnalyseDTO request) {
        List<WareHouseAgeAnalyseVO> wareHouseAgeAnalyseList = itReportCenterService.wareHouseAgeAnalyseList(request);
        ExcelUtil<WareHouseAgeAnalyseVO> util = new ExcelUtil<>(WareHouseAgeAnalyseVO.class);
        util.exportExcel(response, wareHouseAgeAnalyseList, "sheet1");
    }

    /**
     * 库存统计列表
     * @param request
     * @return
     */
    //    @RequiresPermissions("wms:storeStatistics:list")
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/storeStatistics/list")
    public TableDataInfo storeStatisticsList(StoreStatisticsDTO request) {
        startPage();
        List<StoreStatisticsVO> storeStatisticsList = itReportCenterService.storeStatisticsList(request);
        return getDataTable(storeStatisticsList);
    }

    /**
     * 库存统计列表导出
     * @param response
     * @param request
     */
//    @RequiresPermissions("wms:storeStatistics:export")
    @PostMapping("/storeStatistics/export")
    public void storeStatisticsExport(HttpServletResponse response, StoreStatisticsDTO request) {
        List<StoreStatisticsVO> storeStatisticsList = itReportCenterService.storeStatisticsList(request);
        ExcelUtil<StoreStatisticsVO> util = new ExcelUtil<>(StoreStatisticsVO.class);
        util.exportExcel(response, storeStatisticsList, "sheet1");
    }

    /**
     * 货位使用频率列表
     * @param areaId 区域id
     * @param reservoirId 库区Id
     * @return
     */
    //    @RequiresPermissions("wms:frequencyOfLocation:list")
    @GetMapping("/frequencyOfLocation/list")
    public TableDataInfo frequencyOfLocationList(Integer areaId,Integer reservoirId) {
        startPage();
        List<FrequencyOfLocationVO> frequencyOfLocationList = itReportCenterService.frequencyOfLocationList(areaId,reservoirId);
        return getDataTable(frequencyOfLocationList);
    }

    /**
     * 货位使用频率列表导出
     * @param response
     * @param areaId 区域id
     * @param reservoirId 库区Id
     */
//    @RequiresPermissions("wms:frequencyOfLocation:export")
    @PostMapping("/frequencyOfLocation/export")
    public void frequencyOfLocationExport(HttpServletResponse response,Integer areaId,Integer reservoirId) {
        List<FrequencyOfLocationVO> frequencyOfLocationList = itReportCenterService.frequencyOfLocationList(areaId,reservoirId);
        ExcelUtil<FrequencyOfLocationVO> util = new ExcelUtil<>(FrequencyOfLocationVO.class);
        util.exportExcel(response, frequencyOfLocationList, "sheet1");
    }

    /**
     * 物料合格率列表
     * @param contactsUnitName 供应商名称
     * @param contactsUnitContact 联系人
     * @return
     */
    //    @RequiresPermissions("wms:materialQualificationRate:list")
    @GetMapping("/materialQualificationRate/list")
    public TableDataInfo materialQualificationRateList(String contactsUnitName,String contactsUnitContact) {
        startPage();
        List<MaterialQualificationRateVO> materialQualificationRateList = itReportCenterService.materialQualificationRateList(contactsUnitName,contactsUnitContact);
        return getDataTable(materialQualificationRateList);
    }

    /**
     * 物料合格率列表导出
     * @param response
     * @param contactsUnitName
     * @param contactsUnitContact
     */
//    @RequiresPermissions("wms:materialQualificationRate:export")
    @PostMapping("/materialQualificationRate/export")
    public void materialQualificationRateExport(HttpServletResponse response,String contactsUnitName,String contactsUnitContact) {
        List<MaterialQualificationRateVO> materialQualificationRateList = itReportCenterService.materialQualificationRateList(contactsUnitName,contactsUnitContact);
        ExcelUtil<MaterialQualificationRateVO> util = new ExcelUtil<>(MaterialQualificationRateVO.class);
        util.exportExcel(response, materialQualificationRateList, "sheet1");
    }

    /**
     * 采购交付时间统计
     * @param code
     * @return
     */
    //    @RequiresPermissions("wms:deliveryTimeStatistics:list")
    @GetMapping("/deliveryTimeStatistics/list")
    public TableDataInfo deliveryTimeStatisticsList(String code) {
        startPage();
        List<DeliveryTimeStatisticsVO> list = itReportCenterService.deliveryTimeStatisticsList(code);
        return getDataTable(list);
    }

    /**
     * 采购交付时间统计导出
     * @param response
     */
//    @RequiresPermissions("wms:deliveryTimeStatistics:export")
    @PostMapping("/deliveryTimeStatistics/export")
    public void deliveryTimeStatisticsExport(HttpServletResponse response,String code) {
        List<DeliveryTimeStatisticsVO> list = itReportCenterService.deliveryTimeStatisticsList(code);
        ExcelUtil<DeliveryTimeStatisticsVO> util = new ExcelUtil<>(DeliveryTimeStatisticsVO.class);
        util.exportExcel(response, list, "sheet1");
    }
}
