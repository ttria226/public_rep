package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.TAdvancePut;
import com.xsrw.wms.inout.domain.dto.TAdvanceQualityDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceQualityVO;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryDetailMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TAdvanceQualityMapper;
import com.xsrw.wms.inout.domain.TAdvanceQuality;
import com.xsrw.wms.inout.service.ITAdvanceQualityService;

/**
 * 入库质检单Service业务层处理
 *
 * @author wxr
 * @date 2023-06-05
 */
@Service
public class TAdvanceQualityServiceImpl extends ServiceImpl<TAdvanceQualityMapper, TAdvanceQuality> implements ITAdvanceQualityService {
    @Autowired
    private TAdvanceQualityMapper tAdvanceQualityMapper;
    @Autowired
    private TAdvanceDeliveryMapper tAdvanceDeliveryMapper;
    @Autowired
    private TAdvanceDeliveryDetailMapper tAdvanceDeliveryDetailMapper;


    /**
     * 查询入库质检单列表
     *
     * @param tAdvanceQuality 入库质检单
     * @return 入库质检单
     */
    @Override
    public List<TAdvanceQualityVO> selectTAdvanceQualityList(TAdvanceQualityDTO tAdvanceQuality) {
        return tAdvanceQualityMapper.selectTAdvanceQualityList(tAdvanceQuality);
    }

    /**
     * 查询入库质检单
     *
     * @param id 入库质检单主键
     * @return 入库质检单
     */
    @Override
    public TAdvanceDeliveryVO selectTAdvanceQualityById(Long id) {
        TAdvanceDeliveryVO tAdvanceDeliveryVO = new TAdvanceDeliveryVO();
        TAdvanceQuality advanceQuality = tAdvanceQualityMapper.selectById(id);
        if(advanceQuality != null){
            TAdvanceDelivery tAdvanceDelivery = tAdvanceDeliveryMapper.selectById(advanceQuality.getOriginId());
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
     * 新增入库质检单
     *
     * @param tAdvanceQuality 入库质检单
     * @return 结果
     */
    @Override
    public AjaxResult insertTAdvanceQuality(TAdvanceQuality tAdvanceQuality) {
        TAdvanceDelivery tAdvanceDeliveryVO = tAdvanceDeliveryMapper.selectById(tAdvanceQuality.getOriginId());
        if(tAdvanceDeliveryVO == null || Constants.YES.equals(tAdvanceDeliveryVO.getQualityStatus())){
            return AjaxResult.error("已生成不可再次生成");
        }
        tAdvanceQuality.setStatus(Constants.INOUT_FORM_STATUS_NOT);
        TAdvanceDelivery tAdvanceDelivery = new TAdvanceDelivery();
        tAdvanceDelivery.setId(tAdvanceQuality.getOriginId());
        tAdvanceDelivery.setQualityStatus(Constants.YES);
        tAdvanceDeliveryMapper.updateById(tAdvanceDelivery);
        tAdvanceQualityMapper.insert(tAdvanceQuality);
        return AjaxResult.success();
    }

    /**
     * 修改入库质检单
     *
     * @param tAdvanceQuality 入库质检单
     * @return 结果
     */
    @Override
    public int updateTAdvanceQuality(TAdvanceQuality tAdvanceQuality) {
        return tAdvanceQualityMapper.updateById(tAdvanceQuality);
    }


    /**
     * 批量删除入库质检单
     *
     * @param ids 需要删除的入库质检单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceQualityByIds(Long[] ids) {
        return tAdvanceQualityMapper.deleteTAdvanceQualityByIds(ids);
    }

    /**
     * 删除入库质检单信息
     *
     * @param id 入库质检单主键
     * @return 结果
     */
    @Override
    public int deleteTAdvanceQualityById(Long id) {
        return tAdvanceQualityMapper.deleteTAdvanceQualityById(id);
    }
}
