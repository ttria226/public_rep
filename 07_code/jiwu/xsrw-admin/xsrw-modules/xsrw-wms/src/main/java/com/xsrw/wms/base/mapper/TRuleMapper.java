package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TRule;

/**
 * 规则Mapper接口
 *
 * @author wxr
 * @date 2023-06-12
 */
public interface TRuleMapper extends BaseMapper<TRule> {

    /**
     * 查询规则列表
     *
     * @param tRule 规则
     * @return 规则集合
     */
    public List<TRule> selectTRuleList(TRule tRule);


    /**
     * 删除规则
     *
     * @param id 规则主键
     * @return 结果
     */
    public int deleteTRuleById(Long id);

    /**
     * 批量删除规则
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRuleByIds(Long[] ids);
}
