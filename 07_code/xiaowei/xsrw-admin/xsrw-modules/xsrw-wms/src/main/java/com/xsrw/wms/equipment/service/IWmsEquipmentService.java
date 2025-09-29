package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.WmsEquipment;

/**
 * 设备台账Service接口
 *
 * @author zjj
 * @date 2023-05-08
 */
public interface IWmsEquipmentService extends IService<WmsEquipment>
{

    /**
     * 查询设备台账列表
     *
     * @param wmsEquipment 设备台账
     * @return 设备台账集合
     */
    public List<WmsEquipment> selectWmsEquipmentList(WmsEquipment wmsEquipment);

    /**
     * 查询设备台账
     *
     * @param id 设备台账主键
     * @return 设备台账
     */
    public WmsEquipment selectWmsEquipmentById(Long id);

    /**
     * 新增设备台账
     *
     * @param wmsEquipment 设备台账
     * @return 结果
     */
    public AjaxResult insertWmsEquipment(WmsEquipment wmsEquipment);

    /**
     * 修改设备台账
     *
     * @param wmsEquipment 设备台账
     * @return 结果
     */
    public int updateWmsEquipment(WmsEquipment wmsEquipment);

    /**
     * 批量删除设备台账
     *
     * @param ids 需要删除的设备台账主键集合
     * @return 结果
     */
    public int deleteWmsEquipmentByIds(Long[] ids);

    /**
     * 删除设备台账信息
     *
     * @param id 设备台账主键
     * @return 结果
     */
    public int deleteWmsEquipmentById(Long id);
}
