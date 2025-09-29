package com.xsrw.wms.report.service;

import com.xsrw.wms.report.domain.dto.*;
import com.xsrw.wms.report.domain.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface ITReportCenterService {
    List<Map<String, Object>> materilaCollect(ReportCenterDTO reportCenterDTO);

    List<StockMainReportVO> stockWarning(ValidityWarningReportVO vo);

    List<ExcelMaterialSummaryVO> stockMainService(List<Map<String, Object>> data);

    /**
     * 获取补货列表
     * @param materialCode
     * @param materialName
     * @param warehouseId
     * @return
     */
    List<ReplenishmentReportVO> getReplenishment(String materialCode, String materialName, Long warehouseId);

    /**
     * 获取有效期预警列表
     * @param materialCode
     * @param materialName
     * @param warehouseId
     * @return
     */
    List<ValidityWarningReportVO> getValidityWarning(ValidityWarningReportVO vo);

    /**
     * 出入库流水报表
     * @param inOutStatementDTO
     * @return
     */
    List<InOutStatementVO> inOutStatementList(InOutStatementDTO inOutStatementDTO);

    /**
     * 出入库流水报表导出
     * @param response
     * @param inOutStatementDTO
     */
    void inOutStatementListExport(HttpServletResponse response, InOutStatementDTO inOutStatementDTO);

    /**
     * 供应商质量统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    List<QualityReportVO> qualityReportList(String contactsUnitName, String contactsUnitContact);

    /**
     * 供应商交付统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    List<DeliveryReportVO> deliveryReportList(String contactsUnitName, String contactsUnitContact);

    /**
     * 呆滞品预警列表
     * @param materialCode
     * @param materialName
     * @return
     */
    List<DeadStockWarningVO> deadStockWarningList(String materialCode, String materialName);

    /**
     * 效率统计列表
     * @param request
     * @return
     */
    List<EfficiencyStatisticsVO> efficiencyStatisticsList(EfficiencyStatisticsDTO request);

    /**
     * 工作统计列表
     * @param request
     * @return
     */
    List<WorkStatisticsListsVO> workStatisticsList(WorkStatisticsDTO request);

    /**
     * 库龄分析列表
     * @param request
     * @return
     */
    List<WareHouseAgeAnalyseVO> wareHouseAgeAnalyseList(WareHouseAgeAnalyseDTO request);

    /**
     * 库存统计列表
     * @param request
     * @return
     */
    List<StoreStatisticsVO> storeStatisticsList(StoreStatisticsDTO request);

    /**
     * 货位使用频率列表
     * @param areaId
     * @param reservoirId
     * @return
     */
    List<FrequencyOfLocationVO> frequencyOfLocationList(Integer areaId, Integer reservoirId);

    /**
     * 物料合格率列表
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    List<MaterialQualificationRateVO> materialQualificationRateList(String contactsUnitName, String contactsUnitContact);

    /**
     * 采购交付时间统计
     * @param code
     * @return
     */
    List<DeliveryTimeStatisticsVO> deliveryTimeStatisticsList(String code);
}
