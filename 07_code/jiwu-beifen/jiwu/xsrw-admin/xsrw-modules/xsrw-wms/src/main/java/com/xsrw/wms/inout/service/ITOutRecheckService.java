package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TOutRecheckVO;

/**
 * 出库复核单Service接口
 *
 * @author wxr
 * @date 2023-06-07
 */
public interface ITOutRecheckService extends IService<TOutRecheck> {

    /**
     * 查询出库复核单列表
     *
     * @param tOutRecheck 出库复核单
     * @return 出库复核单集合
     */
    public List<TOutRecheckVO> selectTOutRecheckList(TOutRecheck tOutRecheck);

    /**
     * 查询出库复核单
     *
     * @param id 出库复核单主键
     * @return 出库复核单
     */
    public TOutDelivery selectTOutRecheckById(Long id);

    /**
     * 新增出库复核单
     *
     * @param tOutRecheck 出库复核单
     * @return 结果
     */
    public AjaxResult insertTOutRecheck(TOutRecheck tOutRecheck);

    /**
     * 修改出库复核单
     *
     * @param tOutRecheck 出库复核单
     * @return 结果
     */
    public int updateTOutRecheck(TOutRecheck tOutRecheck);

    /**
     * 批量删除出库复核单
     *
     * @param ids 需要删除的出库复核单主键集合
     * @return 结果
     */
    public int deleteTOutRecheckByIds(Long[] ids);

    /**
     * 删除出库复核单信息
     *
     * @param id 出库复核单主键
     * @return 结果
     */
    public int deleteTOutRecheckById(Long id);
}
