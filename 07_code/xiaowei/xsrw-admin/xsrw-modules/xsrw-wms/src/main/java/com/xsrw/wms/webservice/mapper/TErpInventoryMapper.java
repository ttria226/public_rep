package com.xsrw.wms.webservice.mapper;

import java.util.List;

import com.xsrw.wms.webservice.domain.TErpInventory;

/**
 * ERP-盘点单Mapper接口
 */
public interface TErpInventoryMapper {
    /**
     * 查询ERP-盘点单
     * 
     * @param id ERP-盘点单主键
     * @return ERP-盘点单
     */
    public TErpInventory selectTErpInventoryById(Long id);

    /**
     * 查询ERP-盘点单列表
     * 
     * @param tErpInventory ERP-盘点单
     * @return ERP-盘点单集合
     */
    public List<TErpInventory> selectTErpInventoryList(TErpInventory tErpInventory);

    /**
     * 新增ERP-盘点单
     * 
     * @param tErpInventory ERP-盘点单
     * @return 结果
     */
    public int insertTErpInventory(TErpInventory tErpInventory);

    /**
     * 修改ERP-盘点单
     * 
     * @param tErpInventory ERP-盘点单
     * @return 结果
     */
    public int updateTErpInventory(TErpInventory tErpInventory);

    /**
     * 删除ERP-盘点单
     * 
     * @param id ERP-盘点单主键
     * @return 结果
     */
    public int deleteTErpInventoryById(Long id);

    /**
     * 批量删除ERP-盘点单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTErpInventoryByIds(Long[] ids);
}
