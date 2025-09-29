package com.xsrw.wms.base.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TRulePutawayDetail;

/**
 * 上架规则详情Service接口
 *
 * @author wxr
 * @date 2023-06-13
 */
public interface ITRulePutawayDetailService extends IService<TRulePutawayDetail> {

    /**
     * 查询上架规则详情列表
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 上架规则详情集合
     */
    public List<TRulePutawayDetail> selectTRulePutawayDetailList(TRulePutawayDetail tRulePutawayDetail);

    /**
     * 查询上架规则详情
     *
     * @param id 上架规则详情主键
     * @return 上架规则详情
     */
    public TRulePutawayDetail selectTRulePutawayDetailById(Long id);

    /**
     * 新增上架规则详情
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 结果
     */
    public int insertTRulePutawayDetail(TRulePutawayDetail tRulePutawayDetail);

    /**
     * 修改上架规则详情
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 结果
     */
    public int updateTRulePutawayDetail(TRulePutawayDetail tRulePutawayDetail);

    /**
     * 批量删除上架规则详情
     *
     * @param ids 需要删除的上架规则详情主键集合
     * @return 结果
     */
    public int deleteTRulePutawayDetailByIds(Long[] ids);

    /**
     * 删除上架规则详情信息
     *
     * @param id 上架规则详情主键
     * @return 结果
     */
    public int deleteTRulePutawayDetailById(Long id);

    /**
     * 根据ids对应的库位名称信息
     *
     * @return
     */
    Map<Long, String> getDetailNameByIds(List<Long> ids);

    /**
     * 根据主表ids删除数据
     * @param ids
     * @return
     */
    int deleteTRulePutawayDetailByPutawayIds(Long[] ids);

    /**
     * 根据主键id查询详情列表
     * @param putId
     * @return
     */
    List<TRulePutawayDetail> selectLocationIdsByPutId(Long putId);
}
