package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.domain.vo.TOutShipmentsVO;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOutShipmentsMapper;
import com.xsrw.wms.inout.domain.TOutShipments;
import com.xsrw.wms.inout.service.ITOutShipmentsService;

/**
 * 出库发货单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-07
 */
@Service
public class TOutShipmentsServiceImpl extends ServiceImpl<TOutShipmentsMapper, TOutShipments> implements ITOutShipmentsService {
    @Autowired
    private TOutShipmentsMapper tOutShipmentsMapper;
    @Autowired
    private ITOutDeliveryService outDeliveryService;


    /**
     * 查询出库发货单列表
     *
     * @param tOutShipments 出库发货单
     * @return 出库发货单
     */
    @Override
    public List<TOutShipmentsVO> selectTOutShipmentsList(TOutShipments tOutShipments) {
        return tOutShipmentsMapper.selectTOutShipmentsList(tOutShipments);
    }

    /**
     * 查询出库发货单
     *
     * @param id 出库发货单主键
     * @return 出库发货单
     */
    @Override
    public TOutDelivery selectTOutShipmentsById(Long id) {
        TOutDelivery tOutDelivery = new TOutDelivery();
        TOutShipments tOutShipments = tOutShipmentsMapper.selectById(id);
        if (tOutShipments != null && tOutShipments.getOriginId() != null) {
            tOutDelivery = outDeliveryService.selectTOutDeliveryById(tOutShipments.getOriginId());
        }
        tOutDelivery.setId(id);
        return tOutDelivery;
    }

    /**
     * 新增出库发货单
     *
     * @param tOutShipments 出库发货单
     * @return 结果
     */
    @Override
    public AjaxResult insertTOutShipments(TOutShipments tOutShipments) {
        Long count = this.getExistCountByOriginId(tOutShipments.getOriginId());
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        tOutShipments.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        tOutShipmentsMapper.insert(tOutShipments);
        return AjaxResult.success();
    }

    /**
     * 获取原单是否已存在
     *
     * @param originId
     * @return
     */
    public Long getExistCountByOriginId(Long originId) {
        QueryWrapper<TOutShipments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("origin_id", originId);
        queryWrapper.in("status", Constants.INOUT_FORM_STATUS_NOT, Constants.INOUT_FORM_STATUS_PART);
        return tOutShipmentsMapper.selectCount(queryWrapper);
    }

    /**
     * 修改出库发货单
     *
     * @param tOutShipments 出库发货单
     * @return 结果
     */
    @Override
    public int updateTOutShipments(TOutShipments tOutShipments) {
        return tOutShipmentsMapper.updateById(tOutShipments);
    }


    /**
     * 批量删除出库发货单
     *
     * @param ids 需要删除的出库发货单主键
     * @return 结果
     */
    @Override
    public int deleteTOutShipmentsByIds(Long[] ids) {
        return tOutShipmentsMapper.deleteTOutShipmentsByIds(ids);
    }

    /**
     * 删除出库发货单信息
     *
     * @param id 出库发货单主键
     * @return 结果
     */
    @Override
    public int deleteTOutShipmentsById(Long id) {
        return tOutShipmentsMapper.deleteTOutShipmentsById(id);
    }
}
