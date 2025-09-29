package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TAdvanceDeliveryApiDTO;
import com.xsrw.wms.api.domain.vo.TMaterialDetailApiVO;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;

/**
 * 入库单Service接口
 *
 * @author wxr
 * @date 2023-05-08
 */
public interface ITAdvanceDeliveryService extends IService<TAdvanceDelivery> {

    /**
     * 查询入库单列表
     *
     * @param tAdvanceDelivery 入库单
     * @return 入库单集合
     */
    public List<TAdvanceDeliveryVO> selectTAdvanceDeliveryList(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 查询入库单
     *
     * @param id 入库单主键
     * @return 入库单
     */
    public TAdvanceDeliveryVO selectTAdvanceDeliveryById(Long id);

    /**
     * 根据code查询入库单
     * @param code
     * @return
     */
    public TAdvanceDeliveryVO getDetailByCode(String code);


    /**
     * 新增入库单
     *
     * @param tAdvanceDelivery 入库单
     * @return 结果
     */
    public int insertTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 修改入库单
     *
     * @param tAdvanceDelivery 入库单
     * @return 结果
     */
    public int updateTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 批量删除入库单
     *
     * @param ids 需要删除的入库单主键集合
     * @return 结果
     */
    public AjaxResult deleteTAdvanceDeliveryByIds(Long[] ids);

    /**
     * 删除入库单信息
     *
     * @param id 入库单主键
     * @return 结果
     */
    public int deleteTAdvanceDeliveryById(Long id);


    /**
     * 获取单据物料选择列表
     *
     * @param tMaterial
     * @return
     */
    List<TMaterialVO> getMaterialSelectList(TMaterialDTO tMaterial);

    /**
     * 审核
     *
     * @param tAdvanceDelivery
     * @return
     */
    AjaxResult approveTAdvanceDelivery(TAdvanceDelivery tAdvanceDelivery);

    /**
     * 登记-old
     *
     * @param tAdvanceDelivery
     * @return
     */
    AjaxResult registerTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 检测-old
     *
     * @param tAdvanceDelivery
     * @return
     */
    AjaxResult checkTAdvanceDelivery(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 通过ids获取标签打印详情
     *
     * @param ids
     * @return
     */
    List<TAdvanceDeliveryDetailVO> getDeatilListByIds(Long[] ids);

    /**
     * pda登记
     *
     * @param advanceDeliveryList
     * @return
     */
    AjaxResult registerCount(List<TAdvanceDeliveryApiDTO> advanceDeliveryList);

    /**
     * pda登记-新
     *
     * @param advanceDeliveryList
     * @return
     */
    AjaxResult registerCountNew(List<TAdvanceDeliveryApiDTO> advanceDeliveryList);

    /**
     * 获取单据物料选择列表
     *
     * @return
     */
    List<TMaterialDetailApiVO> getMaterialCountList();

    /**
     * 入库单检测-new
     */
    AjaxResult checkDeliveryMaterial(TAdvanceDelivery tAdvanceDelivery);

    /**
     * 入库单登记-new
     */
    AjaxResult registerDelivery(TAdvanceDeliveryDTO tAdvanceDelivery);

    /**
     * 根据入库单id获取完成状态
     */
    String getDetailCountStatus(Long deliveryId, int type);

    /**
     * 入库单作废
     *
     * @param tAdvanceDelivery
     * @return
     */
    AjaxResult cancellation(TAdvanceDelivery tAdvanceDelivery);

    /**
     * bom补料
     * @param tBomDetail
     * @return
     */
    AjaxResult bomAdd(TBomDetail tBomDetail);

    /**
     * 根据单据id更新调拨状态
     * @param id
     * @return
     */
    int updateAllotByDeliveryId(Long id);

    /**
     * 根据单据号删除单据
     * @param code
     * @return
     */
    AjaxResult deleteByCode(String code);
}
