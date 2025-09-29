package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TAdvanceQuality;
import com.xsrw.wms.inout.domain.dto.TAdvanceQualityDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceQualityVO;

/**
 * 入库质检单Service接口
 *
 * @author wxr
 * @date 2023-06-05
 */
public interface ITAdvanceQualityService extends IService<TAdvanceQuality> {

    /**
     * 查询入库质检单列表
     *
     * @param tAdvanceQuality 入库质检单
     * @return 入库质检单集合
     */
    public List<TAdvanceQualityVO> selectTAdvanceQualityList(TAdvanceQualityDTO tAdvanceQuality);

    /**
     * 查询入库质检单
     *
     * @param id 入库质检单主键
     * @return 入库质检单
     */
    public TAdvanceDeliveryVO selectTAdvanceQualityById(Long id);

    /**
     * 新增入库质检单
     *
     * @param tAdvanceQuality 入库质检单
     * @return 结果
     */
    public AjaxResult insertTAdvanceQuality(TAdvanceQuality tAdvanceQuality);

    /**
     * 修改入库质检单
     *
     * @param tAdvanceQuality 入库质检单
     * @return 结果
     */
    public int updateTAdvanceQuality(TAdvanceQuality tAdvanceQuality);

    /**
     * 批量删除入库质检单
     *
     * @param ids 需要删除的入库质检单主键集合
     * @return 结果
     */
    public int deleteTAdvanceQualityByIds(Long[] ids);

    /**
     * 删除入库质检单信息
     *
     * @param id 入库质检单主键
     * @return 结果
     */
    public int deleteTAdvanceQualityById(Long id);
}
