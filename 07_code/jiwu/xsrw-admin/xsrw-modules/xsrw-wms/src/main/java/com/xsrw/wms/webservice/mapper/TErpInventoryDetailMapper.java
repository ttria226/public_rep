package com.xsrw.wms.webservice.mapper;

import java.util.List;

import com.xsrw.wms.webservice.domain.TErpInventoryDetail;
import com.xsrw.wms.webservice.domain.vo.wms2erp.TErpInventoryDetailInfoVO;
import org.springframework.stereotype.Repository;

/**
 * ERP-盘点单明细Mapper接口
 */
@Repository
public interface TErpInventoryDetailMapper {
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
     * 删除ERP-盘点单明细
     *
     * @param id ERP-盘点单明细主键
     * @return 结果
     */
    public int deleteTErpInventoryDetailById(Long id);

    /**
     * 批量删除ERP-盘点单明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTErpInventoryDetailByIds(Long[] ids);

    /**
     * 查询盘点盘erp信息
     * @param taskId
     * @return
     */
    List<TErpInventoryDetailInfoVO> selectErpInfo(Long taskId);

    List<TErpInventoryDetailInfoVO> selectErpInfoByCode(String code);
}
