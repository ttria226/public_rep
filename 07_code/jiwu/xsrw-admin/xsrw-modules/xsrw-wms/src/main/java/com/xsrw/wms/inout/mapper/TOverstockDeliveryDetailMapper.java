package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOverstockDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOverstockDeliveryDetailVO;
import org.springframework.stereotype.Repository;

/**
 * 越库单详情Mapper接口
 *
 * @author wxr
 * @date 2023-06-25
 */
@Repository
public interface TOverstockDeliveryDetailMapper extends BaseMapper<TOverstockDeliveryDetail> {

    /**
     * 查询越库单详情列表
     *
     * @param tOverstockDeliveryDetail 越库单详情
     * @return 越库单详情集合
     */
    public List<TOverstockDeliveryDetail> selectTOverstockDeliveryDetailList(TOverstockDeliveryDetail tOverstockDeliveryDetail);


    /**
     * 删除越库单详情
     *
     * @param id 越库单详情主键
     * @return 结果
     */
    public int deleteTOverstockDeliveryDetailById(Long id);

    /**
     * 批量删除越库单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOverstockDeliveryDetailByIds(Long[] ids);

    /**
     * 根据主表ids删除数据
     *
     * @param ids
     * @return
     */
    int deleteDetailByOverstockIds(Long[] ids);

    /**
     * 根据主表id查询详细信息
     *
     * @param deliveryId
     * @return
     */
    List<TOverstockDeliveryDetailVO> selectDetailListByDeliveryId(Long deliveryId);
}
