package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TAdvanceCollection;
import com.xsrw.wms.inout.domain.dto.TAdvanceCollectionDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceCollectionVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;

/**
 * 入库收货退货单Service接口
 *
 * @author wxr
 * @date 2023-06-06
 */
public interface ITAdvanceCollectionService extends IService<TAdvanceCollection> {

    /**
     * 查询入库收货退货单列表
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 入库收货退货单集合
     */
    public List<TAdvanceCollectionVO> selectTAdvanceCollectionList(TAdvanceCollectionDTO tAdvanceCollection);

    /**
     * 查询入库收货退货单
     *
     * @param id 入库收货退货单主键
     * @return 入库收货退货单
     */
    public TAdvanceDeliveryVO selectTAdvanceCollectionById(Long id);

    /**
     * 新增入库收货退货单
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 结果
     */
    public AjaxResult insertTAdvanceCollection(TAdvanceCollection tAdvanceCollection);

    /**
     * 修改入库收货退货单
     *
     * @param tAdvanceCollection 入库收货退货单
     * @return 结果
     */
    public int updateTAdvanceCollection(TAdvanceCollection tAdvanceCollection);

    /**
     * 批量删除入库收货退货单
     *
     * @param ids 需要删除的入库收货退货单主键集合
     * @return 结果
     */
    public int deleteTAdvanceCollectionByIds(Long[] ids);

    /**
     * 删除入库收货退货单信息
     *
     * @param id 入库收货退货单主键
     * @return 结果
     */
    public int deleteTAdvanceCollectionById(Long id);

    /**
     * 入库收货退货单退货
     * @param tAdvanceCollectionDTO
     * @return
     */
    AjaxResult returnStatus(TAdvanceCollectionDTO tAdvanceCollectionDTO);
}
