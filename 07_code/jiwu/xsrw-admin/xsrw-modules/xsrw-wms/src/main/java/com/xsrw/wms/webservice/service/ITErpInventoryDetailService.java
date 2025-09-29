package com.xsrw.wms.webservice.service;

import java.util.List;

import com.xsrw.wms.webservice.domain.TErpInventoryDetail;

/**
 * ERP-盘点单明细Service接口
 */
public interface ITErpInventoryDetailService {
    /**
     * 查询ERP-盘点单明细
     * 
     * @param id ERP-盘点单明细主键
     * @return ERP-盘点单明细
     */
    public TErpInventoryDetail selectTErpInventoryDetailById(Long id);

    /**
     * 查询ERP-盘点单明细列表
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return ERP-盘点单明细集合
     */
    public List<TErpInventoryDetail> selectTErpInventoryDetailList(TErpInventoryDetail tErpInventoryDetail);

    /**
     * 新增ERP-盘点单明细
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return 结果
     */
    public int insertTErpInventoryDetail(TErpInventoryDetail tErpInventoryDetail);

    /**
     * 修改ERP-盘点单明细
     * 
     * @param tErpInventoryDetail ERP-盘点单明细
     * @return 结果
     */
    public int updateTErpInventoryDetail(TErpInventoryDetail tErpInventoryDetail);

    /**
     * 批量删除ERP-盘点单明细
     * 
     * @param ids 需要删除的ERP-盘点单明细主键集合
     * @return 结果
     */
    public int deleteTErpInventoryDetailByIds(Long[] ids);

    /**
     * 删除ERP-盘点单明细信息
     * 
     * @param id ERP-盘点单明细主键
     * @return 结果
     */
    public int deleteTErpInventoryDetailById(Long id);
}
