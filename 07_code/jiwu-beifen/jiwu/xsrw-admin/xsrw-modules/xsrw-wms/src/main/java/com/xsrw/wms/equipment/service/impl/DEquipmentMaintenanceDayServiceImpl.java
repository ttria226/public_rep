package com.xsrw.wms.equipment.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DEquipmentMaintenanceDayMapper;
import com.xsrw.wms.equipment.domain.DEquipmentMaintenanceDay;
import com.xsrw.wms.equipment.service.IDEquipmentMaintenanceDayService;

/**
 * 保养工单Service业务层处理
 *
 * @author zjj
 * @date 2023-05-11
 */
@Service
public class DEquipmentMaintenanceDayServiceImpl extends ServiceImpl<DEquipmentMaintenanceDayMapper, DEquipmentMaintenanceDay> implements IDEquipmentMaintenanceDayService
{
    @Autowired
    private DEquipmentMaintenanceDayMapper dEquipmentMaintenanceDayMapper;
    @Autowired
    private WmsEquipmentMapper equipmentMapper;


    /**
     * 查询保养工单列表
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 保养工单
     */
    @Override
    public List<DEquipmentMaintenanceDay> selectDEquipmentMaintenanceDayList(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        if (dEquipmentMaintenanceDay.getCreateTime()!=null){
            Date date = dEquipmentMaintenanceDay.getCreateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String time = simpleDateFormat.format(date);
            dEquipmentMaintenanceDay.setCreateTimeSearch(time);
        }
        List<DEquipmentMaintenanceDay> dEquipmentMaintenanceDays = dEquipmentMaintenanceDayMapper.selectDEquipmentMaintenanceDayList(dEquipmentMaintenanceDay);
        for (DEquipmentMaintenanceDay equipmentMaintenanceDay : dEquipmentMaintenanceDays) {
            if (equipmentMaintenanceDay.getExecutorId()!=null){
                equipmentMaintenanceDay.setExecutorName(equipmentMapper.getUserByUserId(equipmentMaintenanceDay.getExecutorId().toString()));
            }
            if (equipmentMaintenanceDay.getEquipmentId()!=null){
                equipmentMaintenanceDay.setEquName(equipmentMapper.selectById(equipmentMaintenanceDay.getEquipmentId()).getName());
                equipmentMaintenanceDay.setEquNo(equipmentMapper.selectById(equipmentMaintenanceDay.getEquipmentId()).getEquNo());
            }
        }
        return dEquipmentMaintenanceDays;
    }

    /**
     * 查询保养工单
     *
     * @param id 保养工单主键
     * @return 保养工单
     */
    @Override
    public DEquipmentMaintenanceDay selectDEquipmentMaintenanceDayById(Long id)
    {
        DEquipmentMaintenanceDay dEquipmentMaintenanceDay = dEquipmentMaintenanceDayMapper.selectById(id);
        dEquipmentMaintenanceDay.setEquName(equipmentMapper.selectById(dEquipmentMaintenanceDay.getEquipmentId()).getName());
        dEquipmentMaintenanceDay.setEquNo(equipmentMapper.selectById(dEquipmentMaintenanceDay.getEquipmentId()).getEquNo());
        return dEquipmentMaintenanceDay;
    }

    /**
     * 新增保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    @Override
    public AjaxResult insertDEquipmentMaintenanceDay(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        dEquipmentMaintenanceDayMapper.insert(dEquipmentMaintenanceDay);
        if (dEquipmentMaintenanceDay.getIsShutdown()!=null){
        if (dEquipmentMaintenanceDay.getIsShutdown() == 1){
            WmsEquipment wmsEquipment = equipmentMapper.selectById(dEquipmentMaintenanceDay.getEquipmentId());
            if (wmsEquipment!=null){
                wmsEquipment.setUseStatus(2);//若选择停机状态 则设置设备状态为故障
                equipmentMapper.updateById(wmsEquipment);
            }
        }
        }
        return AjaxResult.success();
    }

    /**
     * 修改保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    @Override
    public int updateDEquipmentMaintenanceDay(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        return dEquipmentMaintenanceDayMapper.updateById(dEquipmentMaintenanceDay);
    }

    /**
     * 保养工单-分派/重新分派
     *
     * @param id
     * @param executorId 保养人id
     * @param executorName 保养人姓名
     * @return 结果
     */
    @Override
    public AjaxResult assign(Long id,Long companyId, Long executorId, String executorName)
    {
        DEquipmentMaintenanceDay day = dEquipmentMaintenanceDayMapper.selectById(id);
        if (day==null){
            return AjaxResult.error("工单查询失败！");
        }
        if (day.getStatus() == 3){
            return AjaxResult.error("当前工单已完成，无法修改！");
        }
        day.setCompanyId(companyId);
        day.setExecutorId(executorId);
        day.setExecutorName(executorName);
        day.setStatus(2);//设置工单状态  1：未分派 2：已分派 0：撤销 3：已完成（已检测）
        return AjaxResult.success(dEquipmentMaintenanceDayMapper.updateById(day));
    }

    @Override
    public AjaxResult cancelDay(Long id) {
        DEquipmentMaintenanceDay day = dEquipmentMaintenanceDayMapper.selectById(id);
        if (day==null){
            return AjaxResult.error("未查询到工单！");
        }
        if (day.getStatus() == 0){//1：未分派 2：已分派 0：撤销 3：已完成（已检测）
            return AjaxResult.error("当前工单已作废！");
        }
        if (day.getStatus() == 3){
            return AjaxResult.error("当前工单已完成检测，无法作废！");
        }
        day.setStatus(0);
        dEquipmentMaintenanceDayMapper.updateById(day);
        return AjaxResult.success();
    }

    @Override
    public List<DEquipmentMaintenanceDay> byListApp(Integer type) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(date);
        List list = dEquipmentMaintenanceDayMapper.byListApp(type, time, SecurityUtils.getUserId());
        return list;
    }


    /**
     * 批量删除保养工单
     *
     * @param ids 需要删除的保养工单主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentMaintenanceDayByIds(Long[] ids)
    {
        return dEquipmentMaintenanceDayMapper.deleteDEquipmentMaintenanceDayByIds(ids);
    }

    /**
     * 删除保养工单信息
     *
     * @param id 保养工单主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentMaintenanceDayById(Long id)
    {
        return dEquipmentMaintenanceDayMapper.deleteDEquipmentMaintenanceDayById(id);
    }

    /**
     * 修改保养工单
     *
     * @param dEquipmentMaintenanceDay 保养工单
     * @return 结果
     */
    @Override
    public AjaxResult startBy(DEquipmentMaintenanceDay dEquipmentMaintenanceDay)
    {
        DEquipmentMaintenanceDay dEquipmentMaintenanceDay1 = dEquipmentMaintenanceDayMapper.selectById(dEquipmentMaintenanceDay.getId());
        if (dEquipmentMaintenanceDay1.getStatus() == 3){
            return AjaxResult.error("当前保养任务已完成检测！");
        }
        dEquipmentMaintenanceDay.setStatus(3);//设置工单状态为已完成
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        dEquipmentMaintenanceDay.setTrueDay(time);//设置实际保养日期
        dEquipmentMaintenanceDayMapper.updateById(dEquipmentMaintenanceDay);
        return AjaxResult.success();
    }
}
