package com.xsrw.wms.equipment.service;

import java.text.ParseException;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DInspectionDayInfo;

/**
 * 每日巡检记录Service接口
 *
 * @author zjj
 * @date 2023-05-18
 */
public interface IDInspectionDayInfoService extends IService<DInspectionDayInfo>
{

    /**
     * 查询每日巡检记录列表
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 每日巡检记录集合
     */
    public List<DInspectionDayInfo> selectDInspectionDayInfoList(DInspectionDayInfo dInspectionDayInfo);

    /**
     * 查询每日巡检记录
     *
     * @param id 每日巡检记录主键
     * @return 每日巡检记录
     */
    public DInspectionDayInfo selectDInspectionDayInfoById(Long id);

    /**
     * 新增每日巡检记录
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 结果
     */
    public AjaxResult insertDInspectionDayInfo(DInspectionDayInfo dInspectionDayInfo) throws ParseException;

    /**
     * 获取巡检记录详情
     * @param dInspectionDayInfo
     * @return
     */
    AjaxResult getDayInfo(DInspectionDayInfo dInspectionDayInfo);

    /**
     * 修改每日巡检记录
     *
     * @param dInspectionDayInfo 每日巡检记录
     * @return 结果
     */
    public int updateDInspectionDayInfo(DInspectionDayInfo dInspectionDayInfo);

    /**
     * 批量删除每日巡检记录
     *
     * @param ids 需要删除的每日巡检记录主键集合
     * @return 结果
     */
    public int deleteDInspectionDayInfoByIds(Long[] ids);

    /**
     * 删除每日巡检记录信息
     *
     * @param id 每日巡检记录主键
     * @return 结果
     */
    public int deleteDInspectionDayInfoById(Long id);

}
