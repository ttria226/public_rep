package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TRulePutawayDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 上架规则详情Mapper接口
 *
 * @author wxr
 * @date 2023-06-13
 */
public interface TRulePutawayDetailMapper extends BaseMapper<TRulePutawayDetail>
{

    /**
     * 查询上架规则详情列表
     *
     * @param tRulePutawayDetail 上架规则详情
     * @return 上架规则详情集合
     */
    public List<TRulePutawayDetail> selectTRulePutawayDetailList(TRulePutawayDetail tRulePutawayDetail);


    /**
     * 删除上架规则详情
     *
     * @param id 上架规则详情主键
     * @return 结果
     */
    public int deleteTRulePutawayDetailById(Long id);

    /**
     * 批量删除上架规则详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRulePutawayDetailByIds(Long[] ids);

    /**
     * 根据主表ids删除数据
     * @param ids
     * @return
     */
    int deleteTRulePutawayDetailByPutawayIds(Long[] ids);

    /**
     * 根据id获取库位详情
     * @param putawayIds
     * @return
     */
    List<TRulePutawayDetail> selectDetailListByPutawayIds(@Param("putawayIds") List<Long> putawayIds);


}
