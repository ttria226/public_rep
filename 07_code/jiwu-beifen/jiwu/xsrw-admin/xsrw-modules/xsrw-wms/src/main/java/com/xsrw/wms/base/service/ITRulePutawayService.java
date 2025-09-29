package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TRulePutaway;
import com.xsrw.wms.base.domain.dto.TRulePutawayDTO;
import com.xsrw.wms.base.domain.vo.TRulePutawayVO;

/**
 * 上架规则Service接口
 *
 * @author wxr
 * @date 2023-06-13
 */
public interface ITRulePutawayService extends IService<TRulePutaway> {

    /**
     * 查询上架规则列表
     *
     * @param tRulePutaway 上架规则
     * @return 上架规则集合
     */
    public List<TRulePutawayVO> selectTRulePutawayList(TRulePutawayDTO tRulePutaway);

    /**
     * 查询上架规则
     *
     * @param id 上架规则主键
     * @return 上架规则
     */
    public TRulePutawayVO selectTRulePutawayById(Long id);

    /**
     * 新增上架规则
     *
     * @param tRulePutaway 上架规则
     * @return 结果
     */
    public int insertTRulePutaway(TRulePutawayDTO tRulePutaway);

    /**
     * 修改上架规则
     *
     * @param tRulePutaway 上架规则
     * @return 结果
     */
    public int updateTRulePutaway(TRulePutawayDTO tRulePutaway);

    /**
     * 批量删除上架规则
     *
     * @param ids 需要删除的上架规则主键集合
     * @return 结果
     */
    public int deleteTRulePutawayByIds(Long[] ids);

    /**
     * 删除上架规则信息
     *
     * @param id 上架规则主键
     * @return 结果
     */
    public int deleteTRulePutawayById(Long id);
}
