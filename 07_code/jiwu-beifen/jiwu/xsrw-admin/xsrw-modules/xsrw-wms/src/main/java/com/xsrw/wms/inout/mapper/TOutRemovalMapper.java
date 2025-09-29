package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutRemoval;
import com.xsrw.wms.inout.domain.vo.TOutRemovalVO;
import org.springframework.stereotype.Repository;

/**
 * 出库单Mapper接口
 *
 * @author zjj
 * @date 2023-06-05
 */
@Repository
public interface TOutRemovalMapper extends BaseMapper<TOutRemoval> {

    /**
     * 查询出库单列表
     *
     * @param tOutRemoval 出库单
     * @return 出库单集合
     */
    public List<TOutRemovalVO> selectTOutRemovalList(TOutRemoval tOutRemoval);


    /**
     * 删除出库单
     *
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteTOutRemovalById(Long id);

    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutRemovalByIds(Long[] ids);

    /**
     * 根据出库单id获取发货退货单详情列表
     * @param deliveryId
     * @return
     */
    List<TOutDeliveryDetail> selectDetailListByDeliveryId(Long deliveryId);
}
