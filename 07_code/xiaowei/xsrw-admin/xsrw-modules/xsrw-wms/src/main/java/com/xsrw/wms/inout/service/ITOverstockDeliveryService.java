package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TOverstockDelivery;
import com.xsrw.wms.inout.domain.dto.TOverstockDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TOverstockDeliveryVO;

/**
 * 越库单Service接口
 *
 * @author wxr
 * @date 2023-06-25
 */
public interface ITOverstockDeliveryService extends IService<TOverstockDelivery> {

    /**
     * 查询越库单列表
     *
     * @param tOverstockDelivery 越库单
     * @return 越库单集合
     */
    public List<TOverstockDelivery> selectTOverstockDeliveryList(TOverstockDelivery tOverstockDelivery);

    /**
     * 查询越库单
     *
     * @param id 越库单主键
     * @return 越库单
     */
    public TOverstockDeliveryVO selectTOverstockDeliveryById(Long id);

    /**
     * 新增越库单
     *
     * @param tOverstockDelivery 越库单
     * @return 结果
     */
    public int insertTOverstockDelivery(TOverstockDeliveryDTO tOverstockDelivery);

    /**
     * 修改越库单
     *
     * @param tOverstockDelivery 越库单
     * @return 结果
     */
    public int updateTOverstockDelivery(TOverstockDeliveryDTO tOverstockDelivery);

    /**
     * 批量删除越库单
     *
     * @param ids 需要删除的越库单主键集合
     * @return 结果
     */
    public int deleteTOverstockDeliveryByIds(Long[] ids);

    /**
     * 越库单收货
     *
     * @param tOverstockDelivery
     * @return
     */
    AjaxResult registerDelivery(TOverstockDeliveryDTO tOverstockDelivery);

    /**
     * 越库单出库
     *
     * @param tOverstockDelivery
     * @return
     */
    AjaxResult outDelivery(TOverstockDeliveryDTO tOverstockDelivery);
}
