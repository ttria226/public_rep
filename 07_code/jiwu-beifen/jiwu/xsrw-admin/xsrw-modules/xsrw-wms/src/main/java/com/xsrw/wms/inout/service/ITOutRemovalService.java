package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutRemoval;
import com.xsrw.wms.inout.domain.dto.TOutRemovalDTO;
import com.xsrw.wms.inout.domain.vo.TOutRemovalVO;

/**
 * 出库单Service接口
 *
 * @author zjj
 * @date 2023-06-05
 */
public interface ITOutRemovalService extends IService<TOutRemoval> {

    /**
     * 查询出库单列表
     *
     * @param tOutRemoval 出库单
     * @return 出库单集合
     */
    public List<TOutRemovalVO> selectTOutRemovalList(TOutRemoval tOutRemoval);

    /**
     * 查询出库单
     *
     * @param id 出库单主键
     * @return 出库单
     */
    public TOutDelivery selectTOutRemovalById(Long id);

    /**
     * 新增出库单
     *
     * @param tOutRemoval 出库单
     * @return 结果
     */
    public AjaxResult insertTOutRemoval(TOutRemoval tOutRemoval);

    /**
     * 修改出库单
     *
     * @param tOutRemoval 出库单
     * @return 结果
     */
    public int updateTOutRemoval(TOutRemoval tOutRemoval);

    /**
     * 批量删除出库单
     *
     * @param ids 需要删除的出库单主键集合
     * @return 结果
     */
    public int deleteTOutRemovalByIds(Long[] ids);

    /**
     * 删除出库单信息
     *
     * @param id 出库单主键
     * @return 结果
     */
    public int deleteTOutRemovalById(Long id);

    /**
     * 退货
     *
     * @param tOutRemovalDTO
     * @return
     */
    AjaxResult returnStatus(TOutRemovalDTO tOutRemovalDTO);

}
