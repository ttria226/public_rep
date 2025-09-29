package com.xsrw.wms.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TPutAwayRuleDetailMapper;
import com.xsrw.wms.base.domain.TPutAwayRuleDetail;
import com.xsrw.wms.base.service.ITPutAwayRuleDetailService;

/**
 * 上架策略详情Service业务层处理
 *
 * @author wxr
 * @date 2023-05-06
 */
@Service
public class TPutAwayRuleDetailServiceImpl extends ServiceImpl<TPutAwayRuleDetailMapper, TPutAwayRuleDetail> implements ITPutAwayRuleDetailService {
    @Autowired
    private TPutAwayRuleDetailMapper tPutAwayRuleDetailMapper;


    /**
     * 查询上架策略详情列表
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 上架策略详情
     */
    @Override
    public List<TPutAwayRuleDetail> selectTPutAwayRuleDetailList(TPutAwayRuleDetail tPutAwayRuleDetail) {
        return tPutAwayRuleDetailMapper.selectTPutAwayRuleDetailList(tPutAwayRuleDetail);
    }

    /**
     * 查询上架策略详情
     *
     * @param id 上架策略详情主键
     * @return 上架策略详情
     */
    @Override
    public TPutAwayRuleDetail selectTPutAwayRuleDetailById(Long id) {
        return tPutAwayRuleDetailMapper.selectById(id);
    }

    /**
     * 新增上架策略详情
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 结果
     */
    @Override
    public int insertTPutAwayRuleDetail(TPutAwayRuleDetail tPutAwayRuleDetail) {
        return tPutAwayRuleDetailMapper.insert(tPutAwayRuleDetail);
    }

    /**
     * 修改上架策略详情
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 结果
     */
    @Override
    public int updateTPutAwayRuleDetail(TPutAwayRuleDetail tPutAwayRuleDetail) {
        return tPutAwayRuleDetailMapper.updateById(tPutAwayRuleDetail);
    }


    /**
     * 批量删除上架策略详情
     *
     * @param ids 需要删除的上架策略详情主键
     * @return 结果
     */
    @Override
    public int deleteTPutAwayRuleDetailByIds(Long[] ids) {
        return tPutAwayRuleDetailMapper.deleteTPutAwayRuleDetailByIds(ids);
    }

    /**
     * 删除上架策略详情信息
     *
     * @param id 上架策略详情主键
     * @return 结果
     */
    @Override
    public int deleteTPutAwayRuleDetailById(Long id) {
        return tPutAwayRuleDetailMapper.deleteTPutAwayRuleDetailById(id);
    }

    /**
     * 根据主表获取子表数据
     * @param ruleId
     * @return
     */
    @Override
    public List<TPutAwayRuleDetail> selectDetailListByRuleId(Long ruleId,Boolean sortFlag) {
        QueryWrapper<TPutAwayRuleDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("rule_id",ruleId);
        if(sortFlag){
            queryWrapper.eq("status", 1);
            queryWrapper.orderByAsc("rule_order");
        }
        return tPutAwayRuleDetailMapper.selectList(queryWrapper);
    }

    /**
     * 根据主表id删除子表数据
     * @param ruleId
     * @return
     */
    @Override
    public int deleteDetailByRuleId(Long ruleId) {
        return tPutAwayRuleDetailMapper.deletePutAwayRuleDetailByRuleId(ruleId);
    }

}
