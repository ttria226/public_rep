package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutShipments;
import com.xsrw.wms.inout.domain.vo.TOutShipmentsVO;

/**
 * 出库发货单Service接口
 *
 * @author wxr
 * @date 2023-06-07
 */
public interface ITOutShipmentsService extends IService<TOutShipments> {

    /**
     * 查询出库发货单列表
     *
     * @param tOutShipments 出库发货单
     * @return 出库发货单集合
     */
    public List<TOutShipmentsVO> selectTOutShipmentsList(TOutShipments tOutShipments);

    /**
     * 查询出库发货单
     *
     * @param id 出库发货单主键
     * @return 出库发货单
     */
    public TOutDelivery selectTOutShipmentsById(Long id);

    /**
     * 新增出库发货单
     *
     * @param tOutShipments 出库发货单
     * @return 结果
     */
    public AjaxResult insertTOutShipments(TOutShipments tOutShipments);

    /**
     * 修改出库发货单
     *
     * @param tOutShipments 出库发货单
     * @return 结果
     */
    public int updateTOutShipments(TOutShipments tOutShipments);

    /**
     * 批量删除出库发货单
     *
     * @param ids 需要删除的出库发货单主键集合
     * @return 结果
     */
    public int deleteTOutShipmentsByIds(Long[] ids);

    /**
     * 删除出库发货单信息
     *
     * @param id 出库发货单主键
     * @return 结果
     */
    public int deleteTOutShipmentsById(Long id);
}
