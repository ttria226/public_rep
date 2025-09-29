package com.xsrw.wms.base.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.base.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.xsrw.wms.base.mapper.TRuleMapper;
import com.xsrw.wms.base.domain.TRule;
import com.xsrw.wms.base.service.ITRuleService;

/**
 * 规则Service业务层处理
 *
 * @author wxr
 * @date 2023-06-12
 */
@Service
public class TRuleServiceImpl extends ServiceImpl<TRuleMapper, TRule> implements ITRuleService {
    @Autowired
    private TRuleMapper tRuleMapper;


    /**
     * 查询规则列表
     *
     * @param tRule 规则
     * @return 规则
     */
    @Override
    public List<TRule> selectTRuleList(TRule tRule) {
        return tRuleMapper.selectTRuleList(tRule);
    }

    /**
     * 查询规则
     *
     * @param id 规则主键
     * @return 规则
     */
    @Override
    public TRule selectTRuleById(Long id) {
        return tRuleMapper.selectById(id);
    }

    /**
     * 新增规则
     *
     * @param tRule 规则
     * @return 结果
     */
    @Override
    public int insertTRule(TRule tRule) {
        return tRuleMapper.insert(tRule);
    }

    /**
     * 修改规则
     *
     * @param tRule 规则
     * @return 结果
     */
    @Override
    public int updateTRule(TRule tRule) {
        if (Constants.YES.equals(tRule.getFlag())) {
            tRule = tRuleMapper.selectById(tRule.getId());
            // 将启用的策略关闭
            LambdaUpdateWrapper<TRule> lambdaUpdateWrapper = new LambdaUpdateWrapper();
            lambdaUpdateWrapper.eq(TRule::getFlag, Constants.YES).set(TRule::getFlag, Constants.NO);
            tRuleMapper.update(null, lambdaUpdateWrapper);
            // 更新当前启用数据
            tRule.setFlag(Constants.YES);
        }
        tRuleMapper.updateById(tRule);
        return 1;
    }


    /**
     * 批量删除规则
     *
     * @param ids 需要删除的规则主键
     * @return 结果
     */
    @Override
    public int deleteTRuleByIds(Long[] ids) {
        return tRuleMapper.deleteTRuleByIds(ids);
    }

    /**
     * 删除规则信息
     *
     * @param id 规则主键
     * @return 结果
     */
    @Override
    public int deleteTRuleById(Long id) {
        return tRuleMapper.deleteTRuleById(id);
    }

    /**
     * 根据类型获取规则启用状态
     *
     * @param module
     * @return
     */
    @Override
    public String getStatusByMoule(String module) {
        QueryWrapper<TRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("rule_module", module);
        TRule tRule = tRuleMapper.selectOne(queryWrapper);
        return tRule != null ? tRule.getFlag() : Constants.NO;
    }

}
