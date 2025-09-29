package com.xsrw.wms.webservice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.wms.webservice.domain.TErpInventoryDetail;
import com.xsrw.wms.webservice.mapper.TErpInventoryDetailMapper;
import com.xsrw.wms.webservice.service.ITErpInventoryDetailService;

/**
 * ERP-盘点单明细Service业务层处理
 */
@Service
public class TErpInventoryDetailServiceImpl implements ITErpInventoryDetailService {
    @Autowired
    private TErpInventoryDetailMapper tErpInventoryDetailMapper;

    /**
     * 查询ERP-盘点单明细
     * 
     * @param id ERP-盘点单明细主键
     * @return ERP-盘点单明细
     */
    @Override
    public TErpInventoryDetail selectTErpInventoryDetailById(Long id)
    {
        return tErpInventoryDetailMapper.selectTErpInventoryDetailById(id);
    }

    /**
     * 查询ERP-盘点单明细列表
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return ERP-盘点单明细
     */
    @Override
    public List<TErpInventoryDetail> selectTErpInventoryDetailList(TErpInventoryDetail tErpInventoryDetail)
    {
        return tErpInventoryDetailMapper.selectTErpInventoryDetailList(tErpInventoryDetail);
    }

    /**
     * 新增ERP-盘点单明细
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return 结果
     */
    @Override
    public int insertTErpInventoryDetail(TErpInventoryDetail tErpInventoryDetail)
    {
        tErpInventoryDetail.setCreateTime(DateUtils.getNowDate());
        return tErpInventoryDetailMapper.insertTErpInventoryDetail(tErpInventoryDetail);
    }

    /**
     * 修改ERP-盘点单明细
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return 结果
     */
    @Override
    public int updateTErpInventoryDetail(TErpInventoryDetail tErpInventoryDetail)
    {
        tErpInventoryDetail.setUpdateTime(DateUtils.getNowDate());
        return tErpInventoryDetailMapper.updateTErpInventoryDetail(tErpInventoryDetail);
    }

    /**
     * 批量删除ERP-盘点单明细
     * 
     * @param ids 需要删除的ERP-盘点单明细主键
     * @return 结果
     */
    @Override
    public int deleteTErpInventoryDetailByIds(Long[] ids)
    {
        return tErpInventoryDetailMapper.deleteTErpInventoryDetailByIds(ids);
    }

    /**
     * 删除ERP-盘点单明细信息
     * 
     * @param id ERP-盘点单明细主键
     * @return 结果
     */
    @Override
    public int deleteTErpInventoryDetailById(Long id)
    {
        return tErpInventoryDetailMapper.deleteTErpInventoryDetailById(id);
    }
}
