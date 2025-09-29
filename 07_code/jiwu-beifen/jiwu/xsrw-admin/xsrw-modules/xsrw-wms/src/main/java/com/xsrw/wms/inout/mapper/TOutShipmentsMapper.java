package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutShipments;
import com.xsrw.wms.inout.domain.vo.TOutShipmentsVO;
import org.springframework.stereotype.Repository;

/**
 * 出库发货单Mapper接口
 *
 * @author wxr
 * @date 2023-06-07
 */
@Repository
public interface TOutShipmentsMapper extends BaseMapper<TOutShipments> {

    /**
     * 查询出库发货单列表
     *
     * @param tOutShipments 出库发货单
     * @return 出库发货单集合
     */
    public List<TOutShipmentsVO> selectTOutShipmentsList(TOutShipments tOutShipments);


    /**
     * 删除出库发货单
     *
     * @param id 出库发货单主键
     * @return 结果
     */
    public int deleteTOutShipmentsById(Long id);

    /**
     * 批量删除出库发货单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutShipmentsByIds(Long[] ids);
}
