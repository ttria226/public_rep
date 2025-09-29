package com.xsrw.wms.stock.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.stock.domain.TStockRecheck;
import com.xsrw.wms.stock.domain.dto.TStockRecheckDTO;
import com.xsrw.wms.stock.domain.vo.TStockRecheckVO;

/**
 * 复检管理Mapper接口
 *
 * @author wxr
 * @date 2023-06-21
 */
public interface TStockRecheckMapper extends BaseMapper<TStockRecheck> {

    /**
     * 查询复检管理列表
     *
     * @param tStockRecheck 复检管理
     * @return 复检管理集合
     */
    public List<TStockRecheckVO> selectTStockRecheckList(TStockRecheckDTO tStockRecheck);


    /**
     * 删除复检管理
     *
     * @param id 复检管理主键
     * @return 结果
     */
    public int deleteTStockRecheckById(Long id);

    /**
     * 批量删除复检管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTStockRecheckByIds(Long[] ids);

}
