package com.xsrw.wms.inout.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TMergeDelivery;

/**
 * 波次计划Mapper接口
 * 
 * @author zjj
 * @date 2023-06-25
 */
public interface TMergeDeliveryMapper extends BaseMapper<TMergeDelivery>
{

    /**
     * 查询波次计划列表
     * 
     * @param tMergeDelivery 波次计划
     * @return 波次计划集合
     */
    public List<TMergeDelivery> selectTMergeDeliveryList(TMergeDelivery tMergeDelivery);


    /**
     * 删除波次计划
     * 
     * @param id 波次计划主键
     * @return 结果
     */
    public int deleteTMergeDeliveryById(Long id);

    /**
     * 批量删除波次计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMergeDeliveryByIds(Long[] ids);
}
