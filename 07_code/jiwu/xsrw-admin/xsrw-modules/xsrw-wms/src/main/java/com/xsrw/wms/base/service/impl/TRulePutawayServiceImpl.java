package com.xsrw.wms.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TRulePutawayDetail;
import com.xsrw.wms.base.domain.dto.TRulePutawayDTO;
import com.xsrw.wms.base.domain.vo.TRulePutawayVO;
import com.xsrw.wms.base.mapper.TLocationMapper;
import com.xsrw.wms.base.service.ITRulePutawayDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TRulePutawayMapper;
import com.xsrw.wms.base.domain.TRulePutaway;
import com.xsrw.wms.base.service.ITRulePutawayService;

/**
 * 上架规则Service业务层处理
 *
 * @author wxr
 * @date 2023-06-13
 */
@Service
public class TRulePutawayServiceImpl extends ServiceImpl<TRulePutawayMapper, TRulePutaway> implements ITRulePutawayService {
    @Autowired
    private TRulePutawayMapper tRulePutawayMapper;
    @Autowired
    private ITRulePutawayDetailService rulePutawayDetailService;

    @Autowired
    private TLocationMapper tLocationMapper;

    /**
     * 查询上架规则列表
     *
     * @param tRulePutaway 上架规则
     * @return 上架规则
     */
    @Override
    public List<TRulePutawayVO> selectTRulePutawayList(TRulePutawayDTO tRulePutaway) {
        List<TRulePutawayVO> list = tRulePutawayMapper.selectTRulePutawayList(tRulePutaway);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(TRulePutawayVO::getId).collect(Collectors.toList());
            Map<Long, String> locationNameMap = rulePutawayDetailService.getDetailNameByIds(ids);
            list.forEach(e -> {
                e.setLocationName(locationNameMap.get(e.getId()));
            });
        }
        return list;
    }

    /**
     * 查询上架规则
     *
     * @param id 上架规则主键
     * @return 上架规则
     */
    @Override
    public TRulePutawayVO selectTRulePutawayById(Long id) {
        TRulePutawayVO tRulePutawayVO = tRulePutawayMapper.selectInfoById(id);
        List<Long> detailList = new ArrayList<>();
        List<TRulePutawayDetail> tRulePutawayDetails = rulePutawayDetailService.selectLocationIdsByPutId(id);
        if (CollectionUtils.isNotEmpty(tRulePutawayDetails)) {
            detailList = tRulePutawayDetails.stream().map(TRulePutawayDetail::getLocationId).collect(Collectors.toList());

            // 查询区域、库区
            Long locationId = tRulePutawayDetails.get(0).getLocationId();
            TLocation tLocation = tLocationMapper.selectById(locationId);
            tRulePutawayVO.setAreaId(tLocation.getAreaId());
            tRulePutawayVO.setReservoirId(tLocation.getReservoirId());
        }
        tRulePutawayVO.setDetailList(detailList);
        return tRulePutawayVO;
    }

    /**
     * 新增上架规则
     *
     * @param tRulePutaway 上架规则
     * @return 结果
     */
    @Override
    public int insertTRulePutaway(TRulePutawayDTO tRulePutaway) {
        tRulePutaway.setStatus(Constants.YES);
        tRulePutawayMapper.insert(tRulePutaway);
        List<Long> detailList = tRulePutaway.getDetailList();
        List<TRulePutawayDetail> detailSaveList = new ArrayList<>();
        detailList.forEach(e -> {
            TRulePutawayDetail tRulePutawayDetail = new TRulePutawayDetail();
            tRulePutawayDetail.setRulePutawayId(tRulePutaway.getId());
            tRulePutawayDetail.setLocationId(e);
            detailSaveList.add(tRulePutawayDetail);
        });
        rulePutawayDetailService.saveBatch(detailSaveList);
        return 1;
    }

    /**
     * 修改上架规则
     *
     * @param tRulePutaway 上架规则
     * @return 结果
     */
    @Override
    public int updateTRulePutaway(TRulePutawayDTO tRulePutaway) {
        tRulePutawayMapper.updateById(tRulePutaway);
        Long[] ids = new Long[1];
        ids[0] = tRulePutaway.getId();
        rulePutawayDetailService.deleteTRulePutawayDetailByPutawayIds(ids);
        List<TRulePutawayDetail> detailSaveList = new ArrayList<>();
        List<Long> detailList = tRulePutaway.getDetailList();
        detailList.forEach(e -> {
            TRulePutawayDetail tRulePutawayDetail = new TRulePutawayDetail();
            tRulePutawayDetail.setRulePutawayId(tRulePutaway.getId());
            tRulePutawayDetail.setLocationId(e);
            detailSaveList.add(tRulePutawayDetail);
        });
        rulePutawayDetailService.saveBatch(detailSaveList);
        return 1;
    }


    /**
     * 批量删除上架规则
     *
     * @param ids 需要删除的上架规则主键
     * @return 结果
     */
    @Override
    public int deleteTRulePutawayByIds(Long[] ids) {
        rulePutawayDetailService.deleteTRulePutawayDetailByPutawayIds(ids);
        return tRulePutawayMapper.deleteTRulePutawayByIds(ids);
    }

    /**
     * 删除上架规则信息
     *
     * @param id 上架规则主键
     * @return 结果
     */
    @Override
    public int deleteTRulePutawayById(Long id) {
        return tRulePutawayMapper.deleteTRulePutawayById(id);
    }
}
