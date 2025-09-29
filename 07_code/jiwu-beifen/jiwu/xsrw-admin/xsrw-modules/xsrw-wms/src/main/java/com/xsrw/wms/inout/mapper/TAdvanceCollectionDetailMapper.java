package com.xsrw.wms.inout.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceCollectionDetail;
import org.springframework.stereotype.Repository;

/**
 * 入库收货退货单详情Mapper接口
 *
 * @author wxr
 * @date 2023-06-08
 */
@Repository
public interface TAdvanceCollectionDetailMapper extends BaseMapper<TAdvanceCollectionDetail> {

    /**
     * 查询入库收货退货单详情列表
     *
     * @param tAdvanceCollectionDetail 入库收货退货单详情
     * @return 入库收货退货单详情集合
     */
    public List<TAdvanceCollectionDetail> selectTAdvanceCollectionDetailList(TAdvanceCollectionDetail tAdvanceCollectionDetail);


    /**
     * 删除入库收货退货单详情
     *
     * @param id 入库收货退货单详情主键
     * @return 结果
     */
    public int deleteTAdvanceCollectionDetailById(Long id);

    /**
     * 批量删除入库收货退货单详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceCollectionDetailByIds(Long[] ids);
}
