package com.xsrw.wms.stock.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.vo.StockMainVo;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import com.xsrw.wms.stock.service.ITStockMainService;

/**
 * 库存查询Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TStockMainServiceImpl extends ServiceImpl<TStockMainMapper, TStockMain> implements ITStockMainService
{
    @Autowired
    private TStockMainMapper tStockMainMapper;


    /**
     * 查询库存查询列表
     *
     * @param stockMain 库存查询
     * @return 库存查询
     */
    @Override
    public List<StockMainVo> selectTStockMainList(StockMainVo stockMain)
    {
        List<StockMainVo> stockMainVoList = tStockMainMapper.selectTStockMainInfoList(stockMain);
        return stockMainVoList;
    }

    /**
     * 查询库存查询
     *
     * @param id 库存查询主键
     * @return 库存查询
     */
    @Override
    public TStockMain selectTStockMainById(Long id)
    {
        return tStockMainMapper.selectById(id);
    }

    /**
     * 新增库存查询
     *
     * @param tStockMain 库存查询
     * @return 结果
     */
    @Override
    public int insertTStockMain(TStockMain tStockMain)
    {
        return tStockMainMapper.insert(tStockMain);
    }

    /**
     * 修改库存查询
     *
     * @param tStockMain 库存查询
     * @return 结果
     */
    @Override
    public int updateTStockMain(TStockMain tStockMain)
    {
        return tStockMainMapper.updateById(tStockMain);
    }


    /**
     * 批量删除库存查询
     *
     * @param ids 需要删除的库存查询主键
     * @return 结果
     */
    @Override
    public int deleteTStockMainByIds(Long[] ids)
    {
        return tStockMainMapper.deleteTStockMainByIds(ids);
    }

    /**
     * 删除库存查询信息
     *
     * @param id 库存查询主键
     * @return 结果
     */
    @Override
    public int deleteTStockMainById(Long id)
    {
        return tStockMainMapper.deleteTStockMainById(id);
    }

    /**
     * cims根据物料ID获取库存信息
     * @param materialIds
     * @return
     */
    @Override
    public List<TStockMain> getStockByMaterialIds(List<Long> materialIds){
        return tStockMainMapper.getStockByMaterialIds(materialIds);
    }
}
