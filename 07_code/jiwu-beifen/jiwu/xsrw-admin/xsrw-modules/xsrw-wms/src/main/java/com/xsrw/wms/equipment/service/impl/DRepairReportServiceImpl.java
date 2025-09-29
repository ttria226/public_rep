package com.xsrw.wms.repairReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.domain.DInspectionDayInfo;
import com.xsrw.wms.equipment.domain.DRepairReport;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.mapper.DEquipmentMaintenanceDayMapper;
import com.xsrw.wms.equipment.mapper.DInspectionDayInfoMapper;
import com.xsrw.wms.equipment.mapper.DRepairReportMapper;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.equipment.service.IDRepairReportService;
import com.xsrw.wms.equipment.utils.GenerateNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 故障报修Service业务层处理
 *
 * @author zjj
 * @date 2023-05-13
 */
@Service
public class DRepairReportServiceImpl extends ServiceImpl<DRepairReportMapper, DRepairReport> implements IDRepairReportService
{
    @Autowired
    private DRepairReportMapper dRepairReportMapper;

    @Autowired
    private DInspectionDayInfoMapper dayInfoMapper;

    @Autowired
    private GenerateNumberUtil generateNumberUtil;

    @Autowired
    private DEquipmentMaintenanceDayMapper equipmentMaintenanceDayMapper;

    @Autowired
    private WmsEquipmentMapper equipmentMapper;


    /**
     * 查询故障报修列表
     *
     * @param dRepairReport 故障报修
     * @return 故障报修
     */
    @Override
    public List<DRepairReport> selectDRepairReportList(DRepairReport dRepairReport)
    {
        List<DRepairReport> dRepairReports = dRepairReportMapper.selectDRepairReportList(dRepairReport);
        for (DRepairReport repairReport : dRepairReports) {
            WmsEquipment wmsEquipment = equipmentMapper.selectById(repairReport.getEquipmentId());
            if (wmsEquipment!=null){
                repairReport.setEquName(wmsEquipment.getName());
                repairReport.setEquNo(wmsEquipment.getEquNo());
                repairReport.setEquipmentStatus(wmsEquipment.getUseStatus());
            }
        }
        return dRepairReports;
    }

    /**
     * 查询故障报修
     *
     * @param id 故障报修主键
     * @return 故障报修
     */
    @Override
    public DRepairReport selectDRepairReportById(Long id)
    {
        DRepairReport repairReport = dRepairReportMapper.selectById(id);
        WmsEquipment wmsEquipment = equipmentMapper.selectById(repairReport.getEquipmentId());
        if (wmsEquipment!=null){
            repairReport.setEquName(wmsEquipment.getName());
            repairReport.setEquNo(wmsEquipment.getEquNo());
        }
        return repairReport;
    }

    /**
     * 新增故障报修
     *
     * @param dRepairReport 故障报修
     * @return 结果
     */
    @Override
    public AjaxResult insertDRepairReport(DRepairReport dRepairReport)
    {
        WmsEquipment wmsEquipment = equipmentMapper.selectById(dRepairReport.getEquipmentId());
        DInspectionDayInfo dInspectionDayInfos = dayInfoMapper.selectOne(new LambdaQueryWrapper<DInspectionDayInfo>()
                .eq(DInspectionDayInfo::getDayId, dRepairReport.getDayId())
                .eq(DInspectionDayInfo::getEquipmentId, dRepairReport.getEquipmentId()));
        if (dInspectionDayInfos != null){
            dInspectionDayInfos.setStatus(2);//设置巡检记录状态为已报修
            dayInfoMapper.updateById(dInspectionDayInfos);
        }
        if (dRepairReport.getIsShutdown() == 1){
            if (wmsEquipment!=null){
                wmsEquipment.setUseStatus(2);
                equipmentMapper.updateById(wmsEquipment);
            }
        }else if (dRepairReport.getIsShutdown() == 0){
            if (wmsEquipment!=null){
                wmsEquipment.setUseStatus(1);
                equipmentMapper.updateById(wmsEquipment);
            }
        }
        dRepairReportMapper.insert(dRepairReport);
        return AjaxResult.success();
    }

    /**
     * 修改故障报修
     *
     * @param dRepairReport 故障报修
     * @return 结果
     */
    @Override
    public int updateDRepairReport(DRepairReport dRepairReport)
    {
        WmsEquipment wmsEquipment = equipmentMapper.selectById(dRepairReport.getEquipmentId());
        if (dRepairReport.getIsShutdown() == 1){
            wmsEquipment.setUseStatus(2);
            equipmentMapper.updateById(wmsEquipment);
        } else if (dRepairReport.getIsShutdown() == 0) {
            wmsEquipment.setUseStatus(1);
            equipmentMapper.updateById(wmsEquipment);
        }
        int i = dRepairReportMapper.updateById(dRepairReport);
        return i;
    }


    /**
     * 批量删除故障报修
     *
     * @param ids 需要删除的故障报修主键
     * @return 结果
     */
    @Override
    public int deleteDRepairReportByIds(Long[] ids)
    {
        return dRepairReportMapper.deleteDRepairReportByIds(ids);
    }

    /**
     * 删除故障报修信息
     *
     * @param id 故障报修主键
     * @return 结果
     */
    @Override
    public int deleteDRepairReportById(Long id)
    {
        return dRepairReportMapper.deleteDRepairReportById(id);
    }

    @Override
    public AjaxResult createOrder(Long id) {
        DRepairReport repairReport = dRepairReportMapper.selectById(id);
        if (repairReport==null){
            AjaxResult.error("未查询到报修记录！");
        }
        DEquipmentMaintenanceDay day = new DEquipmentMaintenanceDay();//构建工单
        day.setSource(2);//设置来源为手动生成
        day.setEquipmentId(repairReport.getEquipmentId());
        day.setDayNo(generateNumberUtil.generateNum("WX",4));
        if (repairReport.getFaultyAccessoryName()!=null){
            day.setPartName(repairReport.getFaultyAccessoryName());
        }
        day.setEquFaultLv(repairReport.getFaultLv());
        day.setStatus(1);
        day.setType(2);
        day.setRemark(repairReport.getFaultMessage());
        day.setBeforeImg(repairReport.getImg());
        day.setIsShutdown(repairReport.getIsShutdown());
        equipmentMaintenanceDayMapper.insert(day);
        repairReport.setStatus(2);//修改工单状态为已生成
        dRepairReportMapper.updateById(repairReport);
        return AjaxResult.success();
    }
}
