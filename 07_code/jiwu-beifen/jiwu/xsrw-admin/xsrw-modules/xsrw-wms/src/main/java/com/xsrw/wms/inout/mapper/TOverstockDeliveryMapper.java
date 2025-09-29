package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOverstockDelivery;
import org.springframework.stereotype.Repository;

/**
 * 越库单Mapper接口
 *
 * @author wxr
 * @date 2023-06-25
 */
@Repository
public interface TOverstockDeliveryMapper extends BaseMapper<TOverstockDelivery> {

    /**
     * 查询越库单列表
     *
     * @param tOverstockDelivery 越库单
     * @return 越库单集合
     */
    public List<TOverstockDelivery> selectTOverstockDeliveryList(TOverstockDelivery tOverstockDelivery);


    /**
     * 删除越库单
     *
     * @param id 越库单主键
     * @return 结果
     */
    public int deleteTOverstockDeliveryById(Long id);

    /**
     * 批量删除越库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOverstockDeliveryByIds(Long[] ids);
}
