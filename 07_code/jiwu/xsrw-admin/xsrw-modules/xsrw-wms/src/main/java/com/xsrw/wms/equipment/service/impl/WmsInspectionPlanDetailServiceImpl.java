package com.xsrw.wms.equipment.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.WmsInspectionPlanDetailMapper;
import com.xsrw.wms.equipment.domain.WmsInspectionPlanDetail;
import com.xsrw.wms.equipment.service.IWmsInspectionPlanDetailService;

/**
 * 巡检计划设备列Service业务层处理
 *
 * @author zjj
 * @date 2023-05-08
 */
@Service
public class WmsInspectionPlanDetailServiceImpl extends ServiceImpl<WmsInspectionPlanDetailMapper, WmsInspectionPlanDetail> implements IWmsInspectionPlanDetailService
{
    @Autowired
    private WmsInspectionPlanDetailMapper wmsInspectionPlanDetailMapper;


    /**
     * 查询巡检计划设备列列表
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 巡检计划设备列
     */
    @Override
    public List<WmsInspectionPlanDetail> selectWmsInspectionPlanDetailList(WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        return wmsInspectionPlanDetailMapper.selectWmsInspectionPlanDetailList(wmsInspectionPlanDetail);
    }

    /**
     * 查询巡检计划设备列
     *
     * @param id 巡检计划设备列主键
     * @return 巡检计划设备列
     */
    @Override
    public WmsInspectionPlanDetail selectWmsInspectionPlanDetailById(Long id)
    {
        return wmsInspectionPlanDetailMapper.selectById(id);
    }

    /**
     * 新增巡检计划设备列
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 结果
     */
    @Override
    public int insertWmsInspectionPlanDetail(WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        return wmsInspectionPlanDetailMapper.insert(wmsInspectionPlanDetail);
    }

    /**
     * 修改巡检计划设备列
     *
     * @param wmsInspectionPlanDetail 巡检计划设备列
     * @return 结果
     */
    @Override
    public int updateWmsInspectionPlanDetail(WmsInspectionPlanDetail wmsInspectionPlanDetail)
    {
        return wmsInspectionPlanDetailMapper.updateById(wmsInspectionPlanDetail);
    }


    /**
     * 批量删除巡检计划设备列
     *
     * @param ids 需要删除的巡检计划设备列主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionPlanDetailByIds(Long[] ids)
    {
        return wmsInspectionPlanDetailMapper.deleteWmsInspectionPlanDetailByIds(ids);
    }

    /**
     * 删除巡检计划设备列信息
     *
     * @param id 巡检计划设备列主键
     * @return 结果
     */
    @Override
    public int deleteWmsInspectionPlanDetailById(Long id)
    {
        return wmsInspectionPlanDetailMapper.deleteWmsInspectionPlanDetailById(id);
    }
}
