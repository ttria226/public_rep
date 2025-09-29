package com.xsrw.wms.inout.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TMergeDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TMergeDeliveryDetailVO;

/**
 * 波次计划详情Mapper接口
 * 
 * @author zjj
 * @date 2023-06-25
 */
public interface TMergeDeliveryDetailMapper extends BaseMapper<TMergeDeliveryDetail>
{

    /**
     * 查询波次计划详情列表
     * 
     * @param tMergeDeliveryDetail 波次计划详情
     * @return 波次计划详情集合
     */
    public List<TMergeDeliveryDetailVO> selectTMergeDeliveryDetailList(TMergeDeliveryDetail tMergeDeliveryDetail);


    /**
     * 删除波次计划详情
     * 
     * @param id 波次计划详情主键
     * @return 结果
     */
    public int deleteTMergeDeliveryDetailById(Long id);

    /**
     * 批量删除波次计划详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTMergeDeliveryDetailByIds(Long[] ids);
}
