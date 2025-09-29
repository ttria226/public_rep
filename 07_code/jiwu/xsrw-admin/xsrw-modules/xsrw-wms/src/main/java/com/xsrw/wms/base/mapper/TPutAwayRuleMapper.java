package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TPutAwayRule;
import com.xsrw.wms.base.domain.vo.TPutAwayRuleVO;

/**
 * 上架策略Mapper接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface TPutAwayRuleMapper extends BaseMapper<TPutAwayRule> {

    /**
     * 查询上架策略列表
     *
     * @param tPutAwayRule 上架策略
     * @return 上架策略集合
     */
    public List<TPutAwayRule> selectTPutAwayRuleList(TPutAwayRule tPutAwayRule);


    /**
     * 删除上架策略
     *
     * @param id 上架策略主键
     * @return 结果
     */
    public int deleteTPutAwayRuleById(Long id);

    /**
     * 批量删除上架策略
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTPutAwayRuleByIds(Long[] ids);


}
