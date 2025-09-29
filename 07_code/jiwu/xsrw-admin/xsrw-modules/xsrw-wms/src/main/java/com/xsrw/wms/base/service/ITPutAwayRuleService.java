package com.xsrw.wms.base.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TPutAwayRule;
import com.xsrw.wms.base.domain.vo.TPutAwayRuleVO;

/**
 * 上架策略Service接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface ITPutAwayRuleService extends IService<TPutAwayRule> {

    /**
     * 查询上架策略列表
     *
     * @param tPutAwayRule 上架策略
     * @return 上架策略集合
     */
    public List<TPutAwayRule> selectTPutAwayRuleList(TPutAwayRule tPutAwayRule);

    /**
     * 查询上架策略
     *
     * @param id 上架策略主键
     * @return 上架策略
     */
    public TPutAwayRuleVO selectTPutAwayRuleById(Long id);

    /**
     * 新增上架策略
     *
     * @param tPutAwayRule 上架策略
     * @return 结果
     */
    public int insertTPutAwayRule(TPutAwayRuleVO tPutAwayRule);

    /**
     * 修改上架策略
     *
     * @param tPutAwayRule 上架策略
     * @return 结果
     */
    public int updateTPutAwayRule(TPutAwayRuleVO tPutAwayRule);

    /**
     * 批量删除上架策略
     *
     * @param ids 需要删除的上架策略主键集合
     * @return 结果
     */
    public int deleteTPutAwayRuleByIds(Long[] ids);

    /**
     * 删除上架策略信息
     *
     * @param id 上架策略主键
     * @return 结果
     */
    public int deleteTPutAwayRuleById(Long id);

    /**
     * 推荐库位
     * @return
     */
    public Long recommendedLocation(List<Long> removeLocations, Long trayId, Set<Long> categoryIds);

}
