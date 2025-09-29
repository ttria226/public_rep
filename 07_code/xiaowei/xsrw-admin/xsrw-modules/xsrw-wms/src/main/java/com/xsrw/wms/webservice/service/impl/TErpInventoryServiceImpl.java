package com.xsrw.wms.webservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.wms.webservice.domain.TErpInventory;
import com.xsrw.wms.webservice.mapper.TErpInventoryMapper;
import com.xsrw.wms.webservice.service.ITErpInventoryService;

/**
 * ERP-盘点单Service业务层处理
 */
@Service
public class TErpInventoryServiceImpl implements ITErpInventoryService {
    @Autowired
    private TErpInventoryMapper tErpInventoryMapper;

    /**
     * 查询ERP-盘点单
     * 
     * @param id ERP-盘点单主键
     * @return ERP-盘点单
     */
    @Override
    public TErpInventory selectTErpInventoryById(Long id)
    {
        return tErpInventoryMapper.selectTErpInventoryById(id);
    }

    /**
     * 查询ERP-盘点单列表
     * 
     * @param tErpInventory ERP-盘点单
     * @return ERP-盘点单
     */
    @Override
    public List<TErpInventory> selectTErpInventoryList(TErpInventory tErpInventory)
    {
        return tErpInventoryMapper.selectTErpInventoryList(tErpInventory);
    }

    /**
     * 新增ERP-盘点单
     * 
     * @param tErpInventory ERP-盘点单
     * @return 结果
     */
    @Override
    public int insertTErpInventory(TErpInventory tErpInventory)
    {
        tErpInventory.setCreateTime(DateUtils.getNowDate());
        return tErpInventoryMapper.insertTErpInventory(tErpInventory);
    }

    /**
     * 修改ERP-盘点单
     * 
     * @param tErpInventory ERP-盘点单
     * @return 结果
     */
    @Override
    public int updateTErpInventory(TErpInventory tErpInventory)
    {
        tErpInventory.setUpdateTime(DateUtils.getNowDate());
        return tErpInventoryMapper.updateTErpInventory(tErpInventory);
    }

    /**
     * 批量删除ERP-盘点单
     * 
     * @param ids 需要删除的ERP-盘点单主键
     * @return 结果
     */
    @Override
    public int deleteTErpInventoryByIds(Long[] ids)
    {
        return tErpInventoryMapper.deleteTErpInventoryByIds(ids);
    }

    /**
     * 删除ERP-盘点单信息
     * 
     * @param id ERP-盘点单主键
     * @return 结果
     */
    @Override
    public int deleteTErpInventoryById(Long id)
    {
        return tErpInventoryMapper.deleteTErpInventoryById(id);
    }
}
