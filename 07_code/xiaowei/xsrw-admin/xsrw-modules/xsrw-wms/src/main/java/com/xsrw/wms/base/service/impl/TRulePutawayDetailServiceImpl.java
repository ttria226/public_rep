package com.xsrw.wms.base.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TRulePutawayDetailMapper;
import com.xsrw.wms.base.domain.TRulePutawayDetail;
import com.xsrw.wms.base.service.ITRulePutawayDetailService;

/**
 * 上架规则详情Service业务层处理
 *
 * @author wxr
 * @date 2023-06-13
 */
@Service
public class TRulePutawayDetailServiceImpl extends ServiceImpl<TRulePutawayDetailMapper, TRulePutawayDetail> implements ITRulePutawayDetailService {
    @Autowired
    private TRulePutawayDetailMapper tRulePutawayDetailMapper;


    /**
     * 查询上架规则详情列表
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 上架规则详情
     */
    @Override
    public List<TRulePutawayDetail> selectTRulePutawayDetailList(TRulePutawayDetail tRulePutawayDetail) {
        return tRulePutawayDetailMapper.selectTRulePutawayDetailList(tRulePutawayDetail);
    }

    /**
     * 查询上架规则详情
     *
     * @param id 上架规则详情主键
     * @return 上架规则详情
     */
    @Override
    public TRulePutawayDetail selectTRulePutawayDetailById(Long id) {
        return tRulePutawayDetailMapper.selectById(id);
    }

    /**
     * 新增上架规则详情
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 结果
     */
    @Override
    public int insertTRulePutawayDetail(TRulePutawayDetail tRulePutawayDetail) {
        return tRulePutawayDetailMapper.insert(tRulePutawayDetail);
    }

    /**
     * 修改上架规则详情
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 结果
     */
    @Override
    public int updateTRulePutawayDetail(TRulePutawayDetail tRulePutawayDetail) {
        return tRulePutawayDetailMapper.updateById(tRulePutawayDetail);
    }


    /**
     * 批量删除上架规则详情
     *
     * @param ids 需要删除的上架规则详情主键
     * @return 结果
     */
    @Override
    public int deleteTRulePutawayDetailByIds(Long[] ids) {
        return tRulePutawayDetailMapper.deleteTRulePutawayDetailByIds(ids);
    }

    /**
     * 删除上架规则详情信息
     *
     * @param id 上架规则详情主键
     * @return 结果
     */
    @Override
    public int deleteTRulePutawayDetailById(Long id) {
        return tRulePutawayDetailMapper.deleteTRulePutawayDetailById(id);
    }

    /**
     * 根据ids对应的库位名称信息
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, String> getDetailNameByIds(List<Long> ids) {
        Map<Long, String> resMap = new HashMap<>();
        List<TRulePutawayDetail> list = tRulePutawayDetailMapper.selectDetailListByPutawayIds(ids);
        if (CollectionUtils.isNotEmpty(list)) {
            resMap = list.stream().collect(Collectors.toMap(
                    TRulePutawayDetail::getRulePutawayId,
                    e -> e.getRemark() == null ? "":e.getRemark()));
        }
        return resMap;
    }

    /**
     * 根据主表ids删除数据
     *
     * @param ids
     * @return
     */
    @Override
    public int deleteTRulePutawayDetailByPutawayIds(Long[] ids) {
        return tRulePutawayDetailMapper.deleteTRulePutawayDetailByPutawayIds(ids);
    }

    /**
     * 根据主键id查询详情列表
     * @param putId
     * @return
     */
    @Override
    public List<TRulePutawayDetail> selectLocationIdsByPutId(Long putId) {
        QueryWrapper<TRulePutawayDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rule_putaway_id", putId);
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        List<TRulePutawayDetail> tRulePutawayDetails = tRulePutawayDetailMapper.selectList(queryWrapper);
        return tRulePutawayDetails;
    }
}
