package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TAdvanceRegistrationApiDTO;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;

/**
 * 入库单详情Service接口
 *
 * @author wxr
 * @date 2023-05-08
 */
public interface ITAdvanceDeliveryDetailService extends IService<TAdvanceDeliveryDetail> {

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
     * 查询入库单详情
     *
     * @param id 入库单详情主键
     * @return 入库单详情
     */
    public TAdvanceDeliveryDetailVO selectTAdvanceDeliveryDetailById(Long id);

    /**
     * 新增入库单详情
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 结果
     */
    public int insertTAdvanceDeliveryDetail(TAdvanceDeliveryDetail tAdvanceDeliveryDetail);

    /**
     * 修改入库单详情
     *
     * @param tAdvanceDeliveryDetail 入库单详情
     * @return 结果
     */
    public int updateTAdvanceDeliveryDetail(TAdvanceDeliveryDetail tAdvanceDeliveryDetail);

    /**
     * 批量删除入库单详情
     *
     * @param ids 需要删除的入库单详情主键集合
     * @return 结果
     */
    public int deleteTAdvanceDeliveryDetailByIds(Long[] ids);

    /**
     * 删除入库单详情信息
     *
     * @param id 入库单详情主键
     * @return 结果
     */
    public int deleteTAdvanceDeliveryDetailById(Long id);

    /**
     * 根据入库id获取详情列表
     * @param deliveryId
     * @return
     */
    List<TAdvanceDeliveryDetailVO> selectDetailListByDeliveryId(Long deliveryId,Long[] deliveryIds);

    /**
     * 根据入库id删除详情列表
     * @param deliveryIds
     * @return
     */
    int deleteDetailByDeliveryIds(Long[] deliveryIds);

    /**
     * 根据入库单id
     * @param deliveryId
     * @return
     */
    List<TAdvanceDeliveryDetail> getListByDeliveryId(Long deliveryId);

    /**
     * 上架-新
     */
    AjaxResult putaway(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO);

    /**
     * 地堆上架-新
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    AjaxResult floorStocking(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO);

    /**
     * pda上架新
     * @param tAdvanceRegistrationApiDTO
     * @return
     */
    AjaxResult putawayTaskNew(TAdvanceRegistrationApiDTO tAdvanceRegistrationApiDTO);

    /**
     * 重新组盘
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    AjaxResult afreshPutaway(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO);


    /**
     * 齐套入库上架
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    AjaxResult putawayComplete(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO);

    /**
     * 快捷入库-收货上架
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
//    AjaxResult putawayFask(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO);
}
