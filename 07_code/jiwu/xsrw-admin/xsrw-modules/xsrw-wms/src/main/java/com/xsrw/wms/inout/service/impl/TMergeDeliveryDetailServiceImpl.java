package com.xsrw.wms.inout.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.inout.domain.TMergeDelivery;
import com.xsrw.wms.inout.domain.vo.TMergeDeliveryDetailVO;
import com.xsrw.wms.inout.mapper.TMergeDeliveryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TMergeDeliveryDetailMapper;
import com.xsrw.wms.inout.domain.TMergeDeliveryDetail;
import com.xsrw.wms.inout.service.ITMergeDeliveryDetailService;

/**
 * 波次计划详情Service业务层处理
 *
 * @author zjj
 * @date 2023-06-25
 */
@Service
public class TMergeDeliveryDetailServiceImpl extends ServiceImpl<TMergeDeliveryDetailMapper, TMergeDeliveryDetail> implements ITMergeDeliveryDetailService
{
    @Autowired
    private TMergeDeliveryDetailMapper tMergeDeliveryDetailMapper;

    @Autowired
    private TMergeDeliveryMapper tMergeDeliveryMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    /**
     * 查询波次计划详情列表
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 波次计划详情
     */
    @Override
    public List<TMergeDeliveryDetailVO> selectTMergeDeliveryDetailList(TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        List<TMergeDeliveryDetailVO> deliveryDetailList = tMergeDeliveryDetailMapper.selectTMergeDeliveryDetailList(tMergeDeliveryDetail);
        for (TMergeDeliveryDetailVO detail : deliveryDetailList) {
            TMergeDelivery mergeDelivery = tMergeDeliveryMapper.selectById(detail.getMergeDeliveryId());
            detail.setOriginCode(mergeDelivery.getCode());
            TMaterial material = tMaterialMapper.selectById(detail.getMaterialId());
            if(material!=null){
                detail.setMaterialName(material.getName());
                detail.setMaterialCode(material.getCode());
                detail.setNum(detail.getPredictReceiveCount());
                TUnit unit = tUnitMapper.selectById(material.getUnitId());
                detail.setUnitName(unit != null? unit.getName():null);
            }
        }
        return deliveryDetailList;
    }

    /**
     * 查询波次计划详情
     *
     * @param id 波次计划详情主键
     * @return 波次计划详情
     */
    @Override
    public TMergeDeliveryDetail selectTMergeDeliveryDetailById(Long id)
    {
        return tMergeDeliveryDetailMapper.selectById(id);
    }

    /**
     * 新增波次计划详情
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 结果
     */
    @Override
    public int insertTMergeDeliveryDetail(TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        return tMergeDeliveryDetailMapper.insert(tMergeDeliveryDetail);
    }

    /**
     * 修改波次计划详情
     *
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 结果
     */
    @Override
    public int updateTMergeDeliveryDetail(TMergeDeliveryDetail tMergeDeliveryDetail)
    {
        return tMergeDeliveryDetailMapper.updateById(tMergeDeliveryDetail);
    }


    /**
     * 批量删除波次计划详情
     *
     * @param ids 需要删除的波次计划详情主键
     * @return 结果
     */
    @Override
    public int deleteTMergeDeliveryDetailByIds(Long[] ids)
    {
        return tMergeDeliveryDetailMapper.deleteTMergeDeliveryDetailByIds(ids);
    }

    /**
     * 删除波次计划详情信息
     *
     * @param id 波次计划详情主键
     * @return 结果
     */
    @Override
    public int deleteTMergeDeliveryDetailById(Long id)
    {
        return tMergeDeliveryDetailMapper.deleteTMergeDeliveryDetailById(id);
    }
}
