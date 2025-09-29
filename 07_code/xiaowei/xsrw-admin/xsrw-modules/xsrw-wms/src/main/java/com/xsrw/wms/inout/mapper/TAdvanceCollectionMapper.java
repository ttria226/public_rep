package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceCollection;
import com.xsrw.wms.inout.domain.dto.TAdvanceCollectionDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceCollectionVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import org.springframework.stereotype.Repository;

/**
 * 入库收货退货单Mapper接口
 *
 * @author wxr
 * @date 2023-06-06
 */
@Repository
public interface TAdvanceCollectionMapper extends BaseMapper<TAdvanceCollection> {

    /**
     * 查询入库收货退货单列表
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 入库收货退货单集合
     */
    public List<TAdvanceCollectionVO> selectTAdvanceCollectionList(TAdvanceCollectionDTO tAdvanceCollection);


    /**
     * 删除入库收货退货单
     *
     * @param id 入库收货退货单主键
     * @return 结果
     */
    public int deleteTAdvanceCollectionById(Long id);

    /**
     * 批量删除入库收货退货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceCollectionByIds(Long[] ids);

    /**
     * 根据入库单id获取收货退货入库单详情列表
     * @param deliveryId
     * @return
     */
    List<TAdvanceDeliveryDetailVO> selectDetailListByDeliveryId(Long deliveryId);
}
