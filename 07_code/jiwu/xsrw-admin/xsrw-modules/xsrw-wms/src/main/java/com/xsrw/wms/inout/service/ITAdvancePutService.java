package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TAdvancePut;
import com.xsrw.wms.inout.domain.dto.TAdvancePutDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TAdvancePutVO;

/**
 * 入库入库单Service接口
 *
 * @author wxr
 * @date 2023-06-05
 */
public interface ITAdvancePutService extends IService<TAdvancePut> {

    /**
     * 查询入库入库单列表
     *
     * @param tAdvancePut 入库入库单
     * @return 入库入库单集合
     */
    public List<TAdvancePutVO> selectTAdvancePutList(TAdvancePutDTO tAdvancePut);

    /**
     * 查询入库入库单
     *
     * @param id 入库入库单主键
     * @return 入库入库单
     */
    public TAdvanceDeliveryVO selectTAdvancePutById(Long id);

    /**
     * 新增入库入库单
     *
     * @param tAdvancePut 入库入库单
     * @return 结果
     */
    public AjaxResult insertTAdvancePut(TAdvancePut tAdvancePut);

    /**
     * 修改入库入库单
     *
     * @param tAdvancePut 入库入库单
     * @return 结果
     */
    public int updateTAdvancePut(TAdvancePut tAdvancePut);

    /**
     * 批量删除入库入库单
     *
     * @param ids 需要删除的入库入库单主键集合
     * @return 结果
     */
    public int deleteTAdvancePutByIds(Long[] ids);

    /**
     * 删除入库入库单信息
     *
     * @param id 入库入库单主键
     * @return 结果
     */
    public int deleteTAdvancePutById(Long id);
}
