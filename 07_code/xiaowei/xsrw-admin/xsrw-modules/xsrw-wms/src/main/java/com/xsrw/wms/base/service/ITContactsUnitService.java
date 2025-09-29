package com.xsrw.wms.base.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TContactsUnit;

/**
 * 供应商Service接口
 *
 * @author lyx
 * @date 2023-05-05
 */
public interface ITContactsUnitService extends IService<TContactsUnit>
{

    /**
     * 查询供应商列表
     *
     * @param tContactsUnit 供应商
     * @return 供应商集合
     */
    List<TContactsUnit> selectTContactsUnitList(TContactsUnit tContactsUnit);

    /**
     * 查询供应商
     *
     * @param id 供应商主键
     * @return 供应商
     */
    TContactsUnit selectTContactsUnitById(Long id);

    /**
     * 新增供应商
     *
     * @param tContactsUnit 供应商
     * @return 结果
     */
    AjaxResult insertTContactsUnit(TContactsUnit tContactsUnit);

    /**
     * 修改供应商
     *
     * @param tContactsUnit 供应商
     * @return 结果
     */
    AjaxResult updateTContactsUnit(TContactsUnit tContactsUnit);

    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的供应商主键集合
     * @return 结果
     */
    int deleteTContactsUnitByIds(Long[] ids);

    /**
     * 删除供应商信息
     *
     * @param id 供应商主键
     * @return 结果
     */
    int deleteTContactsUnitById(Long id);
}
