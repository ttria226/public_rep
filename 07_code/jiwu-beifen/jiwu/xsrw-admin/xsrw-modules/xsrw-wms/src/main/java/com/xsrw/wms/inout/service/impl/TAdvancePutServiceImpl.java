package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvancePutDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TAdvancePutVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvancePutMapper;
import com.xsrw.wms.inout.domain.TAdvancePut;
import com.xsrw.wms.inout.service.ITAdvancePutService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库入库单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-05
 */
@Service
public class TAdvancePutServiceImpl extends ServiceImpl<TAdvancePutMapper, TAdvancePut> implements ITAdvancePutService {
    @Autowired
    private TAdvancePutMapper tAdvancePutMapper;
    @Autowired
    private TAdvanceDeliveryMapper tAdvanceDeliveryMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;


    /**
     * 查询入库入库单列表
     *
     * @param tAdvancePut 入库入库单
     * @return 入库入库单
     */
    @Override
    public List<TAdvancePutVO> selectTAdvancePutList(TAdvancePutDTO tAdvancePut) {
        return tAdvancePutMapper.selectTAdvancePutList(tAdvancePut);
    }

    /**
     * 查询入库入库单
     *
     * @param id 入库入库单主键
     * @return 入库入库单
     */
    @Override
    public TAdvanceDeliveryVO selectTAdvancePutById(Long id) {
        TAdvanceDeliveryVO tAdvanceDeliveryVO = new TAdvanceDeliveryVO();
        TAdvancePut tAdvancePut = tAdvancePutMapper.selectById(id);
        if(tAdvancePut != null){
            TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(tAdvancePut.getOriginId());
            if(tAdvanceDelivery != null){
                BeanUtils.copyBeanProp(tAdvanceDeliveryVO, tAdvanceDelivery);
                Long[] deliveryIds = new Long[1];
                deliveryIds[0] = tAdvanceDelivery.getId();
                tAdvanceDeliveryDetailMapper.selectDetailListByDeliveryId(deliveryIds);
                tAdvanceDeliveryVO.setDeliveryDetailList(tAdvanceDeliveryDetailMapper.selectDetailListByDeliveryId(deliveryIds));
            }
        }
        return tAdvanceDeliveryVO;
    }

    /**
     * 新增入库入库单
     *
     * @param tAdvancePut 入库入库单
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertTAdvancePut(TAdvancePut tAdvancePut) {
        TAdvanceDelivery tAdvanceDeliveryVO = tAdvanceDeliveryMapper.selectById(tAdvancePut.getOriginId());
        if(tAdvanceDeliveryVO == null || Constants.YES.equals(tAdvanceDeliveryVO.getPutStatus())){
            return AjaxResult.error("已生成不可再次生成");
        }
        tAdvancePut.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        TAdvanceDelivery tAdvanceDelivery = new TAdvanceDelivery();
        tAdvanceDelivery.setId(tAdvancePut.getOriginId());
        tAdvanceDelivery.setPutStatus(Constants.YES);
        tAdvanceDeliveryMapper.updateById(tAdvanceDelivery);
        tAdvancePutMapper.insert(tAdvancePut);
        return AjaxResult.success();
    }

    /**
     * 修改入库入库单
     *
     * @param tAdvancePut 入库入库单
     * @return 结果
     */
    @Override
    public int updateTAdvancePut(TAdvancePut tAdvancePut) {
        return tAdvancePutMapper.updateById(tAdvancePut);
    }


    /**
     * 批量删除入库入库单
     *
     * @param ids 需要删除的入库入库单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvancePutByIds(Long[] ids) {
        return tAdvancePutMapper.deleteTAdvancePutByIds(ids);
    }

    /**
     * 删除入库入库单信息
     *
     * @param id 入库入库单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvancePutById(Long id) {
        return tAdvancePutMapper.deleteTAdvancePutById(id);
    }
}
