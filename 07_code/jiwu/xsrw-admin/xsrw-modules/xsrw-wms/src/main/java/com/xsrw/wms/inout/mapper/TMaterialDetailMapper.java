package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSZiDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.report.domain.vo.QualityReportVO;
import com.xsrw.wms.stock.domain.dto.TStockInDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物料入库详情Mapper接口
 *
 * @author wxr
 * @date 2023-05-11
 */
@Repository
public interface TMaterialDetailMapper extends BaseMapper<TMaterialDetail> {

    /**
     * 查询物料入库详情列表
     *
     * @param tMaterialDetail 物料入库详情
     * @return 物料入库详情集合
     */
    public List<TMaterialDetailVO> selectTMaterialDetailList(TMaterialDetailSerachDTO tMaterialDetail);


    /**
     * 删除物料入库详情
     *
     * @param id 物料入库详情主键
     * @return 结果
     */
    public int deleteTMaterialDetailById(Long id);

    /**
     * 批量删除物料入库详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMaterialDetailByIds(Long[] ids);

    /**
     * 根据入库单详情ids删除
     *
     * @param deliveryDetails
     * @return
     */
    int deleteTMaterialDetailByDeliveryIds(@Param("deliveryDetails") List<Long> deliveryDetails);

    /**
     * 查询物料详情信息
     * @param tMaterialDetail
     * @return
     */
    List<TMaterialDetailVO> selectTMaterialDetailAllList(TMaterialDetail tMaterialDetail);

    /**
     * 库存物资月报表查询
     *
     * @param monthlyCountVo
     * @return
     */
    List<TMaterialDetailMonthlyCountVo> materialDetailMonthlyCountList(@Param("entity") TMaterialDetailMonthlyCountVo monthlyCountVo);

    /**
     * 根据物料详情ids查询
     *
     * @param ids
     * @return
     */
    List<TMaterialDetailVO> selectTMaterialDetailInfoByIds(@Param("ids") List<Long> ids, @Param("deliveryId") Long deliveryId);

    /**
     * 更新物料详情信息
     *
     * @param tAdvanceDeliveryDetailDTO
     * @param trayId
     * @return
     */
    int updateInfoByIdsOrRelId(@Param("deliveryDTO") TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO, @Param("trayId") Long trayId);

    /**
     * 更新物料详情库位信息
     *
     * @param rfIds
     * @param trayId
     * @param locationId
     * @return
     */
    int updateInfoByRfIds(@Param("rfIds") List<String> rfIds, @Param("trayId") Long trayId, @Param("locationId") Long locationId, @Param("status") String status);


    /**
     * 根据入库单详情表id更新物料详情表
     *
     * @param advanceDetailId
     * @param trayId
     * @param locationId
     * @param status
     * @return
     */
    int updateInfoByDetailId(@Param("advanceDetailId") Long advanceDetailId, @Param("trayId") Long trayId, @Param("locationId") Long locationId, @Param("status") String status);

    /**
     * 根据参数更新物料详情表状态
     *
     * @param status
     * @param advanceDetailIds
     * @param trayId
     * @return
     */
    int updateStatusByParam(@Param("status") String status, @Param("advanceDetailIds") List<Long> advanceDetailIds, @Param("trayId") Long trayId, @Param("locationId") Long locationId);

    /**
     * 供应商质量统计报表
     *
     * @param contactsUnitName
     * @param contactsUnitContact
     * @return
     */
    List<QualityReportVO> selectDetectionStatistics(@Param("contactsUnitName") String contactsUnitName, @Param("contactsUnitContact") String contactsUnitContact);

    /**
     * 根据rfids查询物料详情信息
     *
     * @param rfidList
     * @return
     */
    List<TStockInDTO> selectStockMaterialByRfidIds(@Param("rfidList") List<String> rfidList);

    /**
     * 根据入库单查询物料详情总数
     *
     * @param deliveryId
     * @param status
     * @return
     */
    int getMaterialDetailByDelivery(@Param("deliveryId") Long deliveryId, @Param("status") String status);

    /**
     * 查看物料详情
     *
     * @param id
     * @return
     */
    TMaterialDetailVO selectInfoById(Long id);

    /**
     * 根据入库单详情id查询物料详情
     *
     * @param advanceId
     * @return
     */
    List<TMaterialDetailVO> selectMDetailByAdvanceDetailId(Long advanceId);

    /**
     * 根据入库单据详情查询可打印rfid信息
     * @param deliveryDetailId
     * @return
     */
    List<TMaterialDetailVO> selectRfIdInfoByDetailId(Long deliveryDetailId);

    /**
     * 根据rfid分组编号查询打印rfid信息
     * @param rfidHeads
     * @return
     */
    List<TMaterialDetailVO> selectMDetailByRfidHeadIds(@Param("rfidHeads") String[] rfidHeads);


    /**
     * xxxxxxxxxxx
     * @return 物料入库扫描列表
     */
    public List<TMaterialDetailSVO> selectSMMaterialDetailList(@Param("sdto") TMaterialDetailSDTO tMaterialDetailS1);


    /**
     * xxxxxxxxxxx
     * @return 物料入库扫描详情
     */
    public List<TMaterialDetailSZiVO> selectSMMaterialDetailZiList(@Param("sdtoZi") TMaterialDetailSZiDTO tMaterialDetailSZiDTO);

    /**
     * 根据载具类型获取要盘点的物料统计
     *
     * @param trayType
     * @return
     */
    public List<TMaterialDetailVO> selectCheckMaterialDetailList(@Param("trayType") String trayType,@Param("entity") TMaterialDetailVO tMaterialDetailVO);
}
