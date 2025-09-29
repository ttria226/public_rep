package com.xsrw.wms.webservice.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;
import com.xsrw.wms.webservice.domain.vo.TErpStockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.wms.webservice.domain.TErpStock;
import com.xsrw.wms.webservice.mapper.TErpStockMapper;
import com.xsrw.wms.webservice.service.ITErpStockService;
import org.springframework.util.CollectionUtils;

/**
 * ERP-库存Service业务层处理
 */
@Service
public class TErpStockServiceImpl implements ITErpStockService {
    @Autowired
    private TErpStockMapper tErpStockMapper;
    @Autowired
    private ITLocationService locationService;

    /**
     * 查询ERP-库存
     *
     * @param id ERP-库存主键
     * @return ERP-库存
     */
    @Override
    public TErpStock selectTErpStockById(Long id)
    {
        return tErpStockMapper.selectTErpStockById(id);
    }

    /**
     * 查询ERP-库存列表
     *
     * @param tErpStock ERP-库存
     * @return ERP-库存
     */
    @Override
    public List<TErpStockVO> selectTErpStockList(TErpStock tErpStock)
    {
        List<TErpStockVO> data = tErpStockMapper.selectTErpStockList(tErpStock);
        return data;
    }

    /**
     * 新增ERP-库存
     *
     * @param tErpStock ERP-库存
     * @return 结果
     */
    @Override
    public int insertTErpStock(TErpStock tErpStock)
    {
        tErpStock.setCreateTime(DateUtils.getNowDate());
        return tErpStockMapper.insertTErpStock(tErpStock);
    }

    /**
     * 修改ERP-库存
     *
     * @param tErpStock ERP-库存
     * @return 结果
     */
    @Override
    public int updateTErpStock(TErpStock tErpStock)
    {
        tErpStock.setUpdateTime(DateUtils.getNowDate());
        return tErpStockMapper.updateTErpStock(tErpStock);
    }

    /**
     * 批量删除ERP-库存
     *
     * @param ids 需要删除的ERP-库存主键
     * @return 结果
     */
    @Override
    public int deleteTErpStockByIds(Long[] ids)
    {
        return tErpStockMapper.deleteTErpStockByIds(ids);
    }

    /**
     * 删除ERP-库存信息
     *
     * @param id ERP-库存主键
     * @return 结果
     */
    @Override
    public int deleteTErpStockById(Long id)
    {
        return tErpStockMapper.deleteTErpStockById(id);
    }

    /**
     * 清除库存数据
     */
    @Override
    public int cleanTErpStock()
    {
        return tErpStockMapper.cleanTErpStock();
    }
}
