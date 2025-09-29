package com.xsrw.wms.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.vo.StockMainVo;

import java.util.List;

/**
 * 库存查询Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITStockMainService extends IService<TStockMain>
{

    /**
     * 查询库存查询列表
     *
     * @param stockMain 库存查询
     * @return 库存查询集合
     */
    List<StockMainVo> selectTStockMainList(StockMainVo stockMain);

    /**
     * 查询库存查询
     *
     * @param id 库存查询主键
     * @return 库存查询
     */
    TStockMain selectTStockMainById(Long id);

    /**
     * 新增库存查询
     *
     * @param tStockMain 库存查询
     * @return 结果
     */
    int insertTStockMain(TStockMain tStockMain);

    /**
     * 修改库存查询
     *
     * @param tStockMain 库存查询
     * @return 结果
     */
    int updateTStockMain(TStockMain tStockMain);

    /**
     * 批量删除库存查询
     *
     * @param ids 需要删除的库存查询主键集合
     * @return 结果
     */
    int deleteTStockMainByIds(Long[] ids);

    /**
     * 删除库存查询信息
     *
     * @param id 库存查询主键
     * @return 结果
     */
    int deleteTStockMainById(Long id);

    /**
     * cims根据物料ID获取库存信息
     * @param materialIds
     * @return
     */
    List<TStockMain> getStockByMaterialIds(List<Long> materialIds);
}
