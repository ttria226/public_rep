package com.xsrw.wms.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsrw.common.core.text.Convert;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TCategory;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.mapper.TCategoryMapper;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import com.xsrw.wms.inout.mapper.TMaterialDetailMapper;
import com.xsrw.wms.inout.mapper.TOutDeliveryMapper;
import com.xsrw.wms.report.domain.dto.*;
import com.xsrw.wms.report.domain.vo.*;
import com.xsrw.wms.report.service.ITReportCenterService;
import com.xsrw.wms.stock.domain.TStockDetail;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.mapper.TStockDetailMapper;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import com.xsrw.wms.stock.mapper.TStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IReportCenterServiceImpl implements ITReportCenterService {


    @Autowired
    private TOutDeliveryMapper outDeliveryMapper;

    @Autowired
    private TStockMainMapper stockMainMapper;

    @Autowired
    private TStockMapper stockMapper;

    @Autowired
    private TMaterialMapper materialMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    @Autowired
    private TCategoryMapper tCategoryMapper;

    @Resource
    private TStockDetailMapper stockDetailMapper;

    @Resource
    private TMaterialDetailMapper materialDetailMapper;

    @Resource
    private TAdvanceDeliveryDetailMapper advanceDeliveryDetailMapper;

    @Autowired
    private RedisService redisService;

    @Resource
    private TLocationMapper locationMapper;

    @Resource
    private TAdvanceDeliveryMapper advanceDeliveryMapper;

    /**
     * @param reportCenterDTO
     * @return cn.haiwei.common.core.web.domain.AjaxResult
     * @description: 物料收发汇总
     */
    @Override
    public List<Map<String, Object>> materilaCollect(ReportCenterDTO reportCenterDTO) {
        if (StringUtils.isNotBlank(reportCenterDTO.getBeginDate())) {
            reportCenterDTO.setBeginDate(reportCenterDTO.getBeginDate() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(reportCenterDTO.getEndDate())) {
            reportCenterDTO.setEndDate(reportCenterDTO.getEndDate() + " 23:59:59");
        }

        return outDeliveryMapper.materilaCollect(reportCenterDTO);
    }

    /**
     * @description: 外部添加接口  调拨使用、上游供应商使用
     */
    @Override
    public List<StockMainReportVO> stockWarning(String materailCode, String materialName, Long warehouseId) {
        QueryWrapper<TStockMain> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", "0");
        QueryWrapper<TMaterial> queryWrapper1 = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(materailCode) || StringUtils.isNotEmpty(materialName)) {
            if (StringUtils.isNotEmpty(materialName)) {
                queryWrapper1.and(wrapper -> wrapper.like("name", materialName));
            }
            if (StringUtils.isNotEmpty(materailCode)) {
                queryWrapper1.and(wrapper -> wrapper.like("code", materailCode));
            }
            List<TMaterial> materialIds = materialMapper.selectList(queryWrapper1);
            if (materialIds == null || materialIds.size() <= 0) {
                return new ArrayList<>();
            } else {
                List<Long> ids = materialIds.stream().map(TMaterial::getId).collect(Collectors.toList());
                queryWrapper.in("material_id", ids);

            }
        }
        List<TStockMain> stockMainList = stockMainMapper.selectList(queryWrapper);
        List<StockMainReportVO> voList = new ArrayList<>();
        if (stockMainList.size() > 0) {
            for (int i = 0; i < stockMainList.size(); i++) {
                TStockMain stockMain = stockMainList.get(i);
                StockMainReportVO vo = new StockMainReportVO();
                BeanUtils.copyBeanProp(vo, stockMain);
                voList.add(vo);
            }
            voList.forEach(vo -> {
                TMaterial material = materialMapper.selectById(vo.getMaterialId());

                vo.setMaterialCode(material.getCode());
                vo.setMaterialName(material.getName());
                vo.setSpecifications(material.getSpecifications());
                vo.setStockMax(material.getStockMax());
                vo.setStockMin(material.getStockMin());
                TUnit tUnit = tUnitMapper.selectById(material.getUnitId());
                if (StringUtils.isNotNull(tUnit)) {
                    vo.setUnitName(tUnit.getName());
                }
                TCategory tCategory = tCategoryMapper.selectById(material.getCategoryId());
                if (StringUtils.isNotNull(tCategory)) {
                    vo.setCategoryName(tCategory.getName());
                }
                vo.setDescription(material.getDescription());
                // 计算低于下限百分比
                if (material.getStockMin() != null) {
                    if (vo.getLibraryCount().intValue() >= material.getStockMin().intValue()) {
                        vo.setBelowPercentage("0%");
                    } else {
                        int dif = material.getStockMin().intValue() - vo.getLibraryCount().intValue();
                        BigDecimal divide = new BigDecimal(String.valueOf(dif))
                                .divide(new BigDecimal(material.getStockMin().toString()), 2, BigDecimal.ROUND_HALF_DOWN);
                        vo.setBelowPercentage(divide.multiply(new BigDecimal("100")).setScale(0).toString() + "%");
                    }
                } else {
                    vo.setBelowPercentage("");
                }
                if (material.getStockMax() != null) {
                    // 计算超过上限百分比
                    if (vo.getLibraryCount().intValue() <= material.getStockMax()) {
                        vo.setExcessPercentage("0%");
                    } else {
                        int dif = vo.getLibraryCount().intValue() - material.getStockMax().intValue();
                        BigDecimal divide = new BigDecimal(String.valueOf(dif))
                                .divide(new BigDecimal(vo.getStockMax().toString()), 2, BigDecimal.ROUND_HALF_DOWN);
                        vo.setExcessPercentage(divide.multiply(new BigDecimal("100")).setScale(0).toString() + "%");
                    }
                } else {
                    vo.setExcessPercentage("");
                }
            });

        }
        return voList;
    }

    /**
     * excel 收发货导出
     *
     * @param data
     * @return
     */
    @Override
    public List<ExcelMaterialSummaryVO> stockMainService(List<Map<String, Object>> data) {
        List<ExcelMaterialSummaryVO> list = new ArrayList<>();
        for (Map<String, Object> datum : data) {
            ExcelMaterialSummaryVO bean = new ExcelMaterialSummaryVO();
            bean.setCode(datum.get("code").toString());
            bean.setMaterialName(datum.get("name").toString());
            bean.setInDeliveryNum(Long.valueOf(datum.get("inCount").toString()));
            bean.setOutDeliveryNum(Long.valueOf(datum.get("outCount").toString()));
            bean.setLibraryCount(Long.valueOf(datum.get("stockCount").toString()));
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取库存补货列表
     *
     * @param materialCode
     * @param materialName
     * @param warehouseId
     * @return
     */
    @Override
    public List<ReplenishmentReportVO> getReplenishment(String materialCode, String materialName, Long warehouseId) {
        List<ReplenishmentReportVO> voList = stockMainMapper.getReplenishmentStock(materialCode, materialName);
        if (CollectionUtils.isNotEmpty(voList)) {
            voList.forEach(e -> {
                e.setReplenishmentCount(e.getStockMin() - e.getLibraryCount());
            });
        }
        return voList;
    }

    /**
     * 获取有效期预警列表
     *
     * @param materialCode
     * @param materialName
     * @param warehouseId
     * @return
     */
    @Override
    public List<ValidityWarningReportVO> getValidityWarning(String materialCode, String materialName, Long warehouseId) {
        List<ValidityWarningReportVO> voList = stockMapper.getValidityWarning(materialCode, materialName);
        return voList;
    }

    /**
     * 出入库流水报表
     *
     * @param inOutStatementDTO
     * @return
     */
    @Override
    public List<InOutStatementVO> inOutStatementList(InOutStatementDTO inOutStatementDTO) {
        if (inOutStatementDTO.getBeginDate() != null && inOutStatementDTO.getEndDate() != null) {
            inOutStatementDTO.setBeginDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 00:00:00", inOutStatementDTO.getBeginDate())));
            inOutStatementDTO.setEndDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 23:59:59", inOutStatementDTO.getEndDate())));
        }
        return stockDetailMapper.selectListByParam(inOutStatementDTO);
    }

    /**
     * 出入库流水报表导出
     *
     * @param response
     * @param inOutStatementDTO
     */
    @Override
    public void inOutStatementListExport(HttpServletResponse response, InOutStatementDTO inOutStatementDTO) {
        List<InOutStatementVO> inOutStatementList = this.inOutStatementList(inOutStatementDTO);
        //入库
        if (Constants.WCS_TASK_TYPE_IN.equals(inOutStatementDTO.getType())) {
            List<InStatementExcelVO> list = inOutStatementList.stream().map(p -> {
                InStatementExcelVO inStatementExcelVO = new InStatementExcelVO();
                org.springframework.beans.BeanUtils.copyProperties(p, inStatementExcelVO);
                return inStatementExcelVO;
            }).collect(Collectors.toList());
            ExcelUtil<InStatementExcelVO> util = new ExcelUtil<>(InStatementExcelVO.class);
            util.exportExcel(response, list, "sheet1");
        } else if (Constants.WCS_TASK_TYPE_OUT.equals(inOutStatementDTO.getType())) {
            //出库
            List<OutStatementExcelVO> list = inOutStatementList.stream().map(p -> {
                OutStatementExcelVO inStatementExcelVO = new OutStatementExcelVO();
                org.springframework.beans.BeanUtils.copyProperties(p, inStatementExcelVO);
                return inStatementExcelVO;
            }).collect(Collectors.toList());
            ExcelUtil<OutStatementExcelVO> util = new ExcelUtil<>(OutStatementExcelVO.class);
            util.exportExcel(response, list, "sheet1");
        } else {
            //入出库
            ExcelUtil<InOutStatementVO> util = new ExcelUtil<>(InOutStatementVO.class);
            util.exportExcel(response, inOutStatementList, "sheet1");
        }
    }

    /**
     * 供应商质量统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    @Override
    public List<QualityReportVO> qualityReportList(String contactsUnitName, String contactsUnitContact) {
        List<QualityReportVO> voList = materialDetailMapper.selectDetectionStatistics(contactsUnitName, contactsUnitContact);
        voList.forEach(p ->
                {
                    if (p.getDetectionCount() == null || Integer.valueOf(0).equals(p.getDetectionCount())) {
                        p.setPassRate("0.0%");
                    } else {
                        p.setPassRate(new BigDecimal(p.getDetectionPassCount() * 100).divide(new BigDecimal(p.getDetectionCount()), 1, RoundingMode.HALF_UP) + "%");
                    }
                }
        );
        return voList;
    }

    /**
     * 供应商交付统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    @Override
    public List<DeliveryReportVO> deliveryReportList(String contactsUnitName, String contactsUnitContact) {
        List<DeliveryReportVO> voList = advanceDeliveryDetailMapper.selectDeliveryStatistics(contactsUnitName, contactsUnitContact);
        voList.forEach(p ->
                {
                    if (p.getRegistrationCount() == null || Integer.valueOf(0).equals(p.getRegistrationCount())) {
                        p.setPassRate("0.0%");
                    } else {
                        p.setPassRate(new BigDecimal(p.getReceiveCount() * 100).divide(new BigDecimal(p.getRegistrationCount()), 1, RoundingMode.HALF_UP) + "%");
                    }
                }
        );
        return voList;
    }

    /**
     * 呆滞品预警列表
     *
     * @param materialCode
     * @param materialName
     * @return
     */
    @Override
    public List<DeadStockWarningVO> deadStockWarningList(String materialCode, String materialName) {
        Integer dzpDate = Convert.toInt(redisService.getCacheObject(Constants.DZP_DATE), 30);
        return stockMapper.selectDeadStockList(materialCode, materialName, dzpDate);
    }

    /**
     * 效率统计列表
     *
     * @param request
     * @return
     */
    @Override
    public List<EfficiencyStatisticsVO> efficiencyStatisticsList(EfficiencyStatisticsDTO request) {
        return stockDetailMapper.selectEfficiencyStatistics(request);
    }

    /**
     * 工作统计列表
     *
     * @param request
     * @return
     */
    @Override
    public List<WorkStatisticsListsVO> workStatisticsList(WorkStatisticsDTO request) {
        return stockDetailMapper.selectWorkStatistics(request);
    }

    /**
     * 库龄分析列表
     *
     * @param request
     * @return
     */
    @Override
    public List<WareHouseAgeAnalyseVO> wareHouseAgeAnalyseList(WareHouseAgeAnalyseDTO request) {
        if (request.getBeginDate() != null && request.getEndDate() != null) {
            request.setBeginDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 00:00:00", request.getBeginDate())));
            request.setEndDate(DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 23:59:59", request.getEndDate())));
        }
        return stockMapper.selectAareHouseAgeAnalyse(request);
    }

    /**
     * 库存统计列表
     *
     * @param request
     * @return
     */
    @Override
    public List<StoreStatisticsVO> storeStatisticsList(StoreStatisticsDTO request) {
        return stockMapper.selectStoreStatisticsList(request);
    }

    /**
     * 货位使用频率列表
     *
     * @param areaId
     * @param reservoirId
     * @return
     */
    @Override
    public List<FrequencyOfLocationVO> frequencyOfLocationList(Integer areaId, Integer reservoirId) {
        List<FrequencyOfLocationVO> frequencyOfLocationList = locationMapper.frequencyOfLocationList(areaId, reservoirId);
        Long inCount = stockDetailMapper.selectCount(Wrappers.<TStockDetail>lambdaQuery().eq(TStockDetail::getDelFlag, Constants.DEL_FLAG_NO).eq(TStockDetail::getType, Constants.WCS_TASK_TYPE_IN));
        frequencyOfLocationList.forEach(p -> {
            if (inCount == null || Long.valueOf(0).equals(inCount)) {
                p.setFrequency("0.0%");
            } else {
                p.setFrequency(new BigDecimal(p.getInCount() * 100).divide(new BigDecimal(inCount), 1, RoundingMode.HALF_UP) + "%");
            }
        });
        return frequencyOfLocationList;
    }

    /**
     * 物料合格率列表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    @Override
    public List<MaterialQualificationRateVO> materialQualificationRateList(String contactsUnitName, String contactsUnitContact) {
        List<MaterialQualificationRateVO> voList = advanceDeliveryDetailMapper.materialQualificationRateList(contactsUnitName, contactsUnitContact);
        voList.forEach(p ->
                {
                    if (p.getRegistrationCount() == null || Integer.valueOf(0).equals(p.getRegistrationCount())) {
                        p.setPassRate("0.0%");
                    } else {
                        p.setPassRate(new BigDecimal(p.getDetectionCount() * 100).divide(new BigDecimal(p.getRegistrationCount()), 1, RoundingMode.HALF_UP) + "%");
                    }
                }
        );
        return voList;
    }

    /**
     * 采购交付时间统计
     *
     * @param code
     * @return
     */
    @Override
    public List<DeliveryTimeStatisticsVO> deliveryTimeStatisticsList(String code) {
        return advanceDeliveryMapper.deliveryTimeStatistics(code);
    }

}
