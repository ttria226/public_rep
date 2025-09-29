package com.xsrw.wms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TWarehouse;

import java.util.List;

/**
 * 仓库Service接口
 *
 * @author zjj
 * @date 2023-07-12
 */
public interface ITWarehouseService extends IService<TWarehouse>
{

    /**
     * 查询仓库列表
     *
     * @param tWarehouse 仓库
     * @return 仓库集合
     */
    public List<TWarehouse> selectTWarehouseList(TWarehouse tWarehouse);

    /**
     * 查询仓库
     *
     * @param id 仓库主键
     * @return 仓库
     */
    public AjaxResult selectTWarehouseById(Long id);

    /**
     * 新增仓库
     *
     * @param tWarehouse 仓库
     * @return 结果
     */
    public AjaxResult insertTWarehouse(TWarehouse tWarehouse);

    /**
     * 修改仓库
     *
     * @param tWarehouse 仓库
     * @return 结果
     */
    public AjaxResult updateTWarehouse(TWarehouse tWarehouse);

    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库主键集合
     * @return 结果
     */
    public AjaxResult deleteTWarehouseByIds(Long[] ids);

    /**
     * 删除仓库信息
     *
     * @param id 仓库主键
     * @return 结果
     */
    public int deleteTWarehouseById(Long id);

    /**
     * 修改状态(启用/禁用)
     * @param tWarehouse
     * @return
     */
    AjaxResult changeStatus(TWarehouse tWarehouse);
}
