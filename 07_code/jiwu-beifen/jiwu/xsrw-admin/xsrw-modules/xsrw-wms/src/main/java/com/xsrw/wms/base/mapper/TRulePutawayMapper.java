package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TRulePutaway;
import com.xsrw.wms.base.domain.TRulePutawayDetail;
import com.xsrw.wms.base.domain.dto.TRulePutawayDTO;
import com.xsrw.wms.base.domain.vo.TRulePutawayVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 上架规则Mapper接口
 *
 * @author wxr
 * @date 2023-06-13
 */
@Repository
public interface TRulePutawayMapper extends BaseMapper<TRulePutaway> {

    /**
     * 查询上架规则列表
     *
     * @param tRulePutaway 上架规则
     * @return 上架规则集合
     */
    public List<TRulePutawayVO> selectTRulePutawayList(TRulePutawayDTO tRulePutaway);


    /**
     * 删除上架规则
     *
     * @param id 上架规则主键
     * @return 结果
     */
    public int deleteTRulePutawayById(Long id);

    /**
     * 批量删除上架规则
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRulePutawayByIds(Long[] ids);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    TRulePutawayVO selectInfoById(Long id);

    /**
     * 根据物料id查询对应的目标库位
     * @param materialIds
     * @return
     */
    List<TRulePutawayVO> selectPutawayListByMaterIds(@Param("materialIds") List<Long> materialIds);


}
