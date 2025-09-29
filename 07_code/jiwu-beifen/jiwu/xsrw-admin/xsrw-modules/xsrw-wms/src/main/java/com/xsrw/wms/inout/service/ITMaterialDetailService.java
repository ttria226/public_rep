package com.xsrw.wms.inout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TAdvanceMaterialApiDTO;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailMonthlyCountVo;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;

import java.util.List;

/**
 * 物料入库详情Service接口
 *
 * @author wxr
 * @date 2023-05-11
 */
public interface ITMaterialDetailService extends IService<TMaterialDetail> {

    /**
     * 查询物料入库详情列表
     *
     * @param tMaterialDetail 物料入库详情
     * @return 物料入库详情集合
     */
    public List<TMaterialDetailVO> selectTMaterialDetailList(TMaterialDetailSerachDTO tMaterialDetail);

    /**
     * 查询物料入库详情
     *
     * @param id 物料入库详情主键
     * @return 物料入库详情
     */
    public TMaterialDetail selectTMaterialDetailById(Long id);

    /**
     * 新增物料入库详情
     *
     * @param tMaterialDetail 物料入库详情
     * @return 结果
     */
    public int insertTMaterialDetail(TMaterialDetail tMaterialDetail);

    /**
     * 修改物料入库详情
     *
     * @param tMaterialDetail 物料入库详情
     * @return 结果
     */
    public int updateTMaterialDetail(TMaterialDetail tMaterialDetail);

    /**
     * 批量删除物料入库详情
     *
     * @param ids 需要删除的物料入库详情主键集合
     * @return 结果
     */
    public int deleteTMaterialDetailByIds(Long[] ids);

    /**
     * 删除物料入库详情信息
     *
     * @param id 物料入库详情主键
     * @return 结果
     */
    public int deleteTMaterialDetailById(Long id);

    /**
     * 根据入库单删除物料详情列表
     * @param ids
     * @return
     */
    int deleteTMaterialDetailByDeliveryIds(Long[] ids);

    /**
     * 获取所有物料详情
     * @return
     */
    List<TMaterialDetailVO> selectTMaterialDetailAllList(TMaterialDetail tMaterialDetail);

    /**
     * 库存物资月报表查询
     * @param monthlyCountVo
     * @return
     */
    List<TMaterialDetailMonthlyCountVo> materialDetailMonthlyCountList(TMaterialDetailMonthlyCountVo monthlyCountVo);

    /**
     * 根据入库详情标识更新物料详情的检测状态
     * @param detailId
     * @return
     */
    int updateStatusByDeliveryId(Long detailId);

    /**
     * 入库单检测
     * @param tMaterialDetail
     * @return
     */
    AjaxResult checkMaterial(List<TMaterialDetail> tMaterialDetail);

    /**
     * 入库单通过单子检测
     * @param materialApiDTO
     * @return
     */
    AjaxResult checkMaterialByDelivery(TAdvanceMaterialApiDTO materialApiDTO);

    /**
     * 在库检测失败
     * @param tMaterialDetail
     * @return
     */
    AjaxResult checkStockMaDetail(List<TMaterialDetail> tMaterialDetail);

    /**
     * 根据入库单查询物料详情总数
     * @param deliveryId
     * @param status
     * @return
     */
    int getMaterialDetailByDelivery(Long deliveryId, String status);

    /**
     * 打印rfid标签
     * @param id
     * @return
     */
    AjaxResult printMaterialDetailById(Long id,String printFloor);

//    /**
//     * 根据入库单物料打印rfid
//     * @param advanceId
//     * @return
//     */
//    AjaxResult printMaterialDetailByAdvanceId(Long advanceId);
}
