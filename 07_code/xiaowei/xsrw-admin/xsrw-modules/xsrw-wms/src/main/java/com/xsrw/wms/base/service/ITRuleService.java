package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TRule;

/**
 * 规则Service接口
 *
 * @author wxr
 * @date 2023-06-12
 */
public interface ITRuleService extends IService<TRule> {

    /**
     * 查询规则列表
     *
     * @param tRule 规则
     * @return 规则集合
     */
    public List<TRule> selectTRuleList(TRule tRule);

    /**
     * 查询规则
     *
     * @param id 规则主键
     * @return 规则
     */
    public TRule selectTRuleById(Long id);

    /**
     * 新增规则
     *
     * @param tRule 规则
     * @return 结果
     */
    public int insertTRule(TRule tRule);

    /**
     * 修改规则
     *
     * @param tRule 规则
     * @return 结果
     */
    public int updateTRule(TRule tRule);

    /**
     * 批量删除规则
     *
     * @param ids 需要删除的规则主键集合
     * @return 结果
     */
    public int deleteTRuleByIds(Long[] ids);

    /**
     * 删除规则信息
     *
     * @param id 规则主键
     * @return 结果
     */
    public int deleteTRuleById(Long id);

    /**
     * 根据类型获取规则启用状态
     * @param module
     * @return
     */
    String getStatusByMoule(String module);
}
