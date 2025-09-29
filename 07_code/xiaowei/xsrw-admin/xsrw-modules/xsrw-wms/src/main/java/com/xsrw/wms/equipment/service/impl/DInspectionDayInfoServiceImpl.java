package com.xsrw.wms.equipment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.*;
import com.xsrw.wms.equipment.mapper.DInspectionPlanDayMapper;
import com.xsrw.wms.equipment.mapper.DRepairReportMapper;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DInspectionDayInfoMapper;
import com.xsrw.wms.equipment.service.IDInspectionDayInfoService;

/**
 * 每日巡检记录Service业务层处理
 *
 * @author zjj
 * @date 2023-05-18
 */
@Service
public class DInspectionDayInfoServiceImpl extends ServiceImpl<DInspectionDayInfoMapper, DInspectionDayInfo> implements IDInspectionDayInfoService
{
    @Autowired
    private DInspectionDayInfoMapper dInspectionDayInfoMapper;

    @Autowired
    private DInspectionDayInfoMapper dayInfoMapper;

    @Autowired
    private DInspectionPlanDayMapper dayMapper;

    @Autowired
    private WmsEquipmentMapper equipmentMapper;
    @Autowired
    private DRepairReportMapper repairReportMapper;


    /**
     * 查询每日巡检记录列表
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 每日巡检记录
     */
    @Override
    public List<DInspectionDayInfo> selectDInspectionDayInfoList(DInspectionDayInfo dInspectionDayInfo)
    {
        List<DInspectionDayInfo> dInspectionDayInfos = dInspectionDayInfoMapper.selectDInspectionDayInfoList(dInspectionDayInfo);
        for (DInspectionDayInfo info : dInspectionDayInfos) {
            WmsEquipment wmsEquipment = equipmentMapper.selectById(info.getEquipmentId());
            info.setEquipmentName(wmsEquipment.getName());
            info.setEquipmentStatus(wmsEquipment.getUseStatus());
            info.setEquipmentRegion(wmsEquipment.getfunctionLocation());
            info.setCreateBy(equipmentMapper.getNickName(info.getCreateBy()));
//            if (info.getStatus() ==2){
//                repairReportMapper.selectOne();
//                DRepairReport
//            }

        }
        return dInspectionDayInfos;
    }

    /**
     * 查询每日巡检记录
     *
     * @param id 每日巡检记录主键
     * @return 每日巡检记录
     */
    @Override
    public DInspectionDayInfo selectDInspectionDayInfoById(Long id)
    {
        return dInspectionDayInfoMapper.selectById(id);
    }

    /**
     * 新增每日巡检记录
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 结果
     */
    @Override
    public AjaxResult insertDInspectionDayInfo(DInspectionDayInfo dInspectionDayInfo) throws ParseException {
        DInspectionPlanDay day = dayMapper.selectById(dInspectionDayInfo.getDayId());//判断是否在检测时间内
        List<WmsInspectionPlanDetail> infoApp = dayMapper.getInfoApp(day.getPlanId());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String begin = day.getDay()+" "+day.getInspectionStartTime();
        String end = day.getDay()+" "+day.getInspectionEndTime();
        Date beginTime = simpleDateFormat.parse(begin);
        Date endTime = simpleDateFormat.parse(end);
        if (date.before(beginTime) && date.after(endTime)){
            return AjaxResult.error("请在规定时间内检测！");
        }
        List<DInspectionDayInfo> DayInfoBefore = dayInfoMapper.selectList(new LambdaQueryWrapper<DInspectionDayInfo>()
                .eq(DInspectionDayInfo::getDayId, dInspectionDayInfo.getDayId())
                .eq(DInspectionDayInfo::getEquipmentId, dInspectionDayInfo.getEquipmentId()));
        if (DayInfoBefore.size()>0){
            return AjaxResult.error("当天已检测过当前设备，请勿重复检测！");
        }
        dInspectionDayInfoMapper.insert(dInspectionDayInfo);
        if("2".equals(dInspectionDayInfo.getStatus())){
            DRepairReport repairReport = new DRepairReport();
            repairReport.setEquipmentStatus(dInspectionDayInfo.getEquipmentStatus());
            repairReport.setFaultLv(dInspectionDayInfo.getFaultLv());
            repairReport.setFaultMessage(dInspectionDayInfo.getFaultMessage());
            repairReport.setIsShutdown(dInspectionDayInfo.getIsShutdown());
            repairReport.setSource(2);//2设备巡检
            repairReport.setStatus(1);
            repairReport.setImg(dInspectionDayInfo.getImg());
            repairReport.setEquipmentId(dInspectionDayInfo.getEquipmentId());
//            if(Constants.YES.equals(repairReport.getIsShutdown())){
//                repairReport.setEquipmentStatus(2L);//故障
//            }else{
//                repairReport.setEquipmentStatus(1L);//正常
//            }
            repairReportMapper.insert(repairReport);
        }
        List<DInspectionDayInfo> dInspectionDayInfos = dayInfoMapper.selectList(new LambdaQueryWrapper<DInspectionDayInfo>()
                .eq(DInspectionDayInfo::getDayId, dInspectionDayInfo.getDayId()));
        if (infoApp.size() ==dInspectionDayInfos.size()){
            day.setStatus(1);//如果巡检单据跟所需巡检数量一致 标记当天巡检为已完成
            dayMapper.updateById(day);
        }else{
            day.setStatus(2);//如果巡检单据跟所需巡检数量不一致 标记当天巡检为进行中
            dayMapper.updateById(day);
        }
        return AjaxResult.success();
    }

    /**
     * 获取巡检记录详情
     * @param dInspectionDayInfo
     * @return
     */
    @Override
    public AjaxResult getDayInfo(DInspectionDayInfo dInspectionDayInfo) {
        DInspectionDayInfo DayInfoBefore = dayInfoMapper.selectOne(new LambdaQueryWrapper<DInspectionDayInfo>()
                .eq(DInspectionDayInfo::getDayId, dInspectionDayInfo.getDayId())
                .eq(DInspectionDayInfo::getEquipmentId, dInspectionDayInfo.getEquipmentId()));
        return AjaxResult.success(DayInfoBefore);
    }

    /**
     * 修改每日巡检记录
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 结果
     */
    @Override
    public int updateDInspectionDayInfo(DInspectionDayInfo dInspectionDayInfo)
    {
        return dInspectionDayInfoMapper.updateById(dInspectionDayInfo);
    }


    /**
     * 批量删除每日巡检记录
     *
     * @param ids 需要删除的每日巡检记录主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionDayInfoByIds(Long[] ids)
    {
        return dInspectionDayInfoMapper.deleteDInspectionDayInfoByIds(ids);
    }

    /**
     * 删除每日巡检记录信息
     *
     * @param id 每日巡检记录主键
     * @return 结果
     */
    @Override
    public int deleteDInspectionDayInfoById(Long id)
    {
        return dInspectionDayInfoMapper.deleteDInspectionDayInfoById(id);
    }
}
