package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TPutAwayRuleDetail;

/**
 * 上架策略详情Service接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface ITPutAwayRuleDetailService extends IService<TPutAwayRuleDetail> {

    /**
     * 查询上架策略详情列表
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 上架策略详情集合
     */
    public List<TPutAwayRuleDetail> selectTPutAwayRuleDetailList(TPutAwayRuleDetail tPutAwayRuleDetail);

    /**
     * 查询上架策略详情
     *
     * @param id 上架策略详情主键
     * @return 上架策略详情
     */
    public TPutAwayRuleDetail selectTPutAwayRuleDetailById(Long id);

    /**
     * 新增上架策略详情
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 结果
     */
    public int insertTPutAwayRuleDetail(TPutAwayRuleDetail tPutAwayRuleDetail);

    /**
     * 修改上架策略详情
     *
     * @param tPutAwayRuleDetail 上架策略详情
     * @return 结果
     */
    public int updateTPutAwayRuleDetail(TPutAwayRuleDetail tPutAwayRuleDetail);

    /**
     * 批量删除上架策略详情
     *
     * @param ids 需要删除的上架策略详情主键集合
     * @return 结果
     */
    public int deleteTPutAwayRuleDetailByIds(Long[] ids);

    /**
     * 删除上架策略详情信息
     *
     * @param id 上架策略详情主键
     * @return 结果
     */
    public int deleteTPutAwayRuleDetailById(Long id);

    /**
     * 根据主表获取子表数据
     * @param id
     * @return
     */
    List<TPutAwayRuleDetail> selectDetailListByRuleId(Long id,Boolean sortFlag);

    /**
     * 根据主表id删除子表数据
     * @param id
     * @return
     */
    int deleteDetailByRuleId(Long id);

}
