package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.api.domain.vo.TMaterialDetailApiVO;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.report.domain.vo.DeliveryReportVO;
import com.xsrw.wms.report.domain.vo.MaterialQualificationRateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 入库单详情Mapper接口
 *
 * @author wxr
 * @date 2023-05-08
 */
@Repository
public interface TAdvanceDeliveryDetailMapper extends BaseMapper<TAdvanceDeliveryDetail> {

    /**
     * 查询入库单详情列表
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 入库单详情集合
     */
    public List<TAdvanceDeliveryDetailVO> selectTAdvanceDeliveryDetailList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail);

    /**
     * 查询打印的入库单详情列表
     * @param tAdvanceDeliveryDetail
     * @return
     */
    public List<TAdvanceDeliveryDetailVO> selectPrintDetailList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail);


    /**
     * 删除入库单详情
     *
     * @param id 入库单详情主键
     * @return 结果
     */
    public int deleteTAdvanceDeliveryDetailById(Long id);

    /**
     * 批量删除入库单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceDeliveryDetailByIds(Long[] ids);

    /**
     * 根据入库id获取详情列表
     * @param deliveryIds
     * @return
     */
    List<TAdvanceDeliveryDetailVO> selectDetailListByDeliveryId(@Param("deliveryIds") Long[] deliveryIds);

    /**
     * 根据入库ids删除关联子表数据
     * @param deliveryIds
     * @return
     */
    int deleteDetailByDeliveryIds(@Param("deliveryIds") Long[] deliveryIds);

    /**
     * 根据物料获取详情列表
     * @param materilaId
     * @param batchCode
     * @return
     */
    List<TAdvanceDeliveryDetailVO> getDetailByMaterial(@Param("materilaId") Long materilaId,@Param("batchCode") String batchCode);

    /**
     * 获取可登记物料数量
     * @return
     */
    List<TMaterialDetailApiVO> getMaterialCountList();

    TAdvanceDeliveryDetailVO selectInfoById(Long id);

    /**
     * 根据ids查询入库单未检测通过数量
     * @param ids
     * @param status
     * @return
     */
    int getDeliveryNoCheckStatusByIds(@Param("ids") List<Long> ids);

    /**
     * 根据入库单ids更新状态
     * @param status
     * @param deliveryIds
     * @return
     */
    int updateStatusByDeliveryIds(@Param("status") String status,@Param("deliveryIds") List<Long> deliveryIds);

    /**
     * 供应商交付统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    List<DeliveryReportVO> selectDeliveryStatistics(@Param("contactsUnitName") String contactsUnitName,@Param("contactsUnitContact") String contactsUnitContact);

    List<MaterialQualificationRateVO> materialQualificationRateList(@Param("contactsUnitName") String contactsUnitName,@Param("contactsUnitContact") String contactsUnitContact);


    List<TAdvanceDeliveryDetailVO> getRegistrationList(Long deliveryId);
}
