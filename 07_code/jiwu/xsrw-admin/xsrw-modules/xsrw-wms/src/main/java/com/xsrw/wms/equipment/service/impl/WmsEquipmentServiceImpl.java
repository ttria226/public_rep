package com.xsrw.wms.equipment.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.base.domain.TContactsUnit;
import com.xsrw.wms.base.mapper.TContactsUnitMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.service.IWmsEquipmentService;

/**
 * 设备台账Service业务层处理
 *
 * @author zjj
 * @date 2023-05-08
 */
@Service
public class WmsEquipmentServiceImpl extends ServiceImpl<WmsEquipmentMapper, WmsEquipment> implements IWmsEquipmentService
{
    @Autowired
    private WmsEquipmentMapper wmsEquipmentMapper;
    @Autowired
    private TContactsUnitMapper tContactsUnitMapper;



    /**
     * 查询设备台账列表
     *
     * @param wmsEquipment 设备台账
     * @return 设备台账
     */
    @Override
    public List<WmsEquipment> selectWmsEquipmentList(WmsEquipment wmsEquipment)
    {
        List<WmsEquipment> wmsEquipments = wmsEquipmentMapper.selectWmsEquipmentList(wmsEquipment);
        wmsEquipments.forEach(wmsEquipment1 -> {
            if (StringUtils.isNotBlank(wmsEquipment1.getSupplier())){
                TContactsUnit tContactsUnit = tContactsUnitMapper.selectById(wmsEquipment1.getSupplier());
                wmsEquipment1.setSupplierName(tContactsUnit.getName());
            }
            if (StringUtils.isNotBlank(wmsEquipment1.getPerson())){
                wmsEquipment1.setPerson(wmsEquipmentMapper.getUserByUserId(wmsEquipment1.getPerson()));
            }
        });
        return wmsEquipments;
    }

    /**
     * 查询设备台账
     *
     * @param id 设备台账主键
     * @return 设备台账
     */
    @Override
    public WmsEquipment selectWmsEquipmentById(Long id)
    {
        return wmsEquipmentMapper.selectById(id);
    }

    /**
     * 新增设备台账
     *
     * @param wmsEquipment 设备台账
     * @return 结果
     */
    @Override
    public AjaxResult insertWmsEquipment(WmsEquipment wmsEquipment)
    {
        List<WmsEquipment> wmsEquipments = wmsEquipmentMapper.selectList(new LambdaQueryWrapper<WmsEquipment>().eq(WmsEquipment::getEquNo, wmsEquipment.getEquNo()).eq(BaseEntity::getDelFlag, 0));
        if (wmsEquipments.size()>0){
            return AjaxResult.error("当前设备编号已存在，请重新输入！");
        }
        wmsEquipmentMapper.insert(wmsEquipment);
        return AjaxResult.success();
    }

    /**
     * 修改设备台账
     *
     * @param wmsEquipment 设备台账
     * @return 结果
     */
    @Override
    public int updateWmsEquipment(WmsEquipment wmsEquipment)
    {
        return wmsEquipmentMapper.updateById(wmsEquipment);
    }


    /**
     * 批量删除设备台账
     *
     * @param ids 需要删除的设备台账主键
     * @return 结果
     */
    @Override
    public int deleteWmsEquipmentByIds(Long[] ids)
    {
        return wmsEquipmentMapper.deleteWmsEquipmentByIds(ids);
    }

    /**
     * 删除设备台账信息
     *
     * @param id 设备台账主键
     * @return 结果
     */
    @Override
    public int deleteWmsEquipmentById(Long id)
    {
        return wmsEquipmentMapper.deleteWmsEquipmentById(id);
    }
}
