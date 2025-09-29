package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOutDeliveryDetailVO;
import com.xsrw.wms.stock.domain.dto.TStockInDTO;
import org.springframework.stereotype.Repository;

/**
 * 出库单详情Mapper接口
 *
 * @author zyq
 * @date 2023-05-09
 */
@Repository
public interface TOutDeliveryDetailMapper extends BaseMapper<TOutDeliveryDetail> {

    /**
     * 查询出库单详情列表
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 出库单详情集合
     */
    public List<TOutDeliveryDetail> selectTOutDeliveryDetailList(TOutDeliveryDetail tOutDeliveryDetail);


    /**
     * 查询出库单详情列表
     *
     * @param tOutDeliveryDetail 出库单详情
     * @return 出库单详情集合
     */
    public List<TOutDeliveryDetailVO> selectTOutDeliveryDetailVOList(TOutDeliveryDetail tOutDeliveryDetail);


    /**
     * 删除出库单详情
     *
     * @param id 出库单详情主键
     * @return 结果
     */
    public int deleteTOutDeliveryDetailById(Long id);

    /**
     * 批量删除出库单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutDeliveryDetailByIds(Long[] ids);

    /**
     * 根据出库单查询库存信息
     * @param deliveryId
     * @return
     */
    List<TStockInDTO> getStockInfoByDeliveryId(Long deliveryId);
}
