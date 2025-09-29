package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DInspectionItems;

/**
 * 巡检标准Service接口
 *
 * @author zjj
 * @date 2023-05-08
 */
public interface IDInspectionItemsService extends IService<DInspectionItems>
{

    /**
     * 查询巡检标准列表
     *
     * @param dInspectionItems 巡检标准
     * @return 巡检标准集合
     */
    public List<DInspectionItems> selectDInspectionItemsList(DInspectionItems dInspectionItems);

    /**
     * 查询巡检标准
     *
     * @param id 巡检标准主键
     * @return 巡检标准
     */
    public DInspectionItems selectDInspectionItemsById(Long id);

    /**
     * 新增巡检标准
     *
     * @param dInspectionItems 巡检标准
     * @return 结果
     */
    public AjaxResult insertDInspectionItems(DInspectionItems dInspectionItems);

    /**
     * 修改巡检标准
     *
     * @param dInspectionItems 巡检标准
     * @return 结果
     */
    public int updateDInspectionItems(DInspectionItems dInspectionItems);

    /**
     * 批量删除巡检标准
     *
     * @param ids 需要删除的巡检标准主键集合
     * @return 结果
     */
    public int deleteDInspectionItemsByIds(Long[] ids);

    /**
     * 删除巡检标准信息
     *
     * @param id 巡检标准主键
     * @return 结果
     */
    public int deleteDInspectionItemsById(Long id);

    List<DInspectionItems> isadd(String equipmentid);

}
