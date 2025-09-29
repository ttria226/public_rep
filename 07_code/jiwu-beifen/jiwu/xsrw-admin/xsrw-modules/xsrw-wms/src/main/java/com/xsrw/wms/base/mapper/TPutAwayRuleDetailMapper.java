package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TPutAwayRuleDetail;

/**
 * 上架策略详情Mapper接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface TPutAwayRuleDetailMapper extends BaseMapper<TPutAwayRuleDetail> {

    /**
     * 查询上架策略详情列表
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 上架策略详情集合
     */
    public List<TPutAwayRuleDetail> selectTPutAwayRuleDetailList(TPutAwayRuleDetail tPutAwayRuleDetail);


    /**
     * 删除上架策略详情
     *
     * @param id 上架策略详情主键
     * @return 结果
     */
    public int deleteTPutAwayRuleDetailById(Long id);

    /**
     * 批量删除上架策略详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPutAwayRuleDetailByIds(Long[] ids);

    List<TPutAwayRuleDetail> selectDetailListByRuleId(Long ruleId);

    int deletePutAwayRuleDetailByRuleId(Long ruleId);

}
