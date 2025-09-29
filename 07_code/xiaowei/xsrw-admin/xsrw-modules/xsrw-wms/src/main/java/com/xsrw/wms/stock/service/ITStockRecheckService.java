package com.xsrw.wms.stock.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.stock.domain.TStockRecheck;
import com.xsrw.wms.stock.domain.dto.TStockRecheckDTO;
import com.xsrw.wms.stock.domain.vo.TStockRecheckVO;

/**
 * 复检管理Service接口
 *
 * @author wxr
 * @date 2023-06-21
 */
public interface ITStockRecheckService extends IService<TStockRecheck> {

    /**
     * 查询复检管理列表
     *
     * @param tStockRecheck 复检管理
     * @return 复检管理集合
     */
    public List<TStockRecheckVO> selectTStockRecheckList(TStockRecheckDTO tStockRecheck);

    /**
     * 查询复检管理
     *
     * @param id 复检管理主键
     * @return 复检管理
     */
    public TStockRecheck selectTStockRecheckById(Long id);

    /**
     * 新增复检管理
     *
     * @param tStockRecheck 复检管理
     * @return 结果
     */
    public int insertTStockRecheck(TStockRecheck tStockRecheck);

    /**
     * 修改复检管理
     *
     * @param tStockRecheck 复检管理
     * @return 结果
     */
    public int updateTStockRecheck(TStockRecheck tStockRecheck);

    /**
     * 批量删除复检管理
     *
     * @param ids 需要删除的复检管理主键集合
     * @return 结果
     */
    public int deleteTStockRecheckByIds(Long[] ids);

    /**
     * 删除复检管理信息
     *
     * @param id 复检管理主键
     * @return 结果
     */
    public int deleteTStockRecheckById(Long id);

    /**
     * 复检完成
     * @param tStockRecheck
     * @return
     */
    AjaxResult checkEnd(TStockRecheck tStockRecheck);

    /**
     * 获取库存的物料详情列表
     * @param tStockRecheck
     * @return
     */
    List<TMaterialDetailVO> getMaterialDetailList(TStockRecheck tStockRecheck);
}
