package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutRemovalDetail;
import org.springframework.stereotype.Repository;

/**
 * 出库发货退货单详情Mapper接口
 *
 * @author wxr
 * @date 2023-06-09
 */
@Repository
public interface TOutRemovalDetailMapper extends BaseMapper<TOutRemovalDetail> {

    /**
     * 查询出库发货退货单详情列表
     *
     * @param tOutRemovalDetail 出库发货退货单详情
     * @return 出库发货退货单详情集合
     */
    public List<TOutRemovalDetail> selectTOutRemovalDetailList(TOutRemovalDetail tOutRemovalDetail);


    /**
     * 删除出库发货退货单详情
     *
     * @param id 出库发货退货单详情主键
     * @return 结果
     */
    public int deleteTOutRemovalDetailById(Long id);

    /**
     * 批量删除出库发货退货单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutRemovalDetailByIds(Long[] ids);
}
