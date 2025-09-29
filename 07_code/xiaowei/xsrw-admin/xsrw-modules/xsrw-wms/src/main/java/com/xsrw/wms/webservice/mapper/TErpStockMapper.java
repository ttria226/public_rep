package com.xsrw.wms.webservice.mapper;

import java.util.List;

import com.xsrw.wms.webservice.domain.TErpStock;
import com.xsrw.wms.webservice.domain.vo.TErpStockVO;

/**
 * ERP-库存Mapper接口
 */
public interface TErpStockMapper {
    /**
     * 查询ERP-库存
     *
     * @param id ERP-库存主键
     * @return ERP-库存
     */
    public TErpStock selectTErpStockById(Long id);

    /**
     * 查询ERP-库存列表
     *
     * @param tErpStock ERP-库存
     * @return ERP-库存集合
     */
    public List<TErpStockVO> selectTErpStockList(TErpStock tErpStock);

    /**
     * 新增ERP-库存
     *
     * @param tErpStock ERP-库存
     * @return 结果
     */
    public int insertTErpStock(TErpStock tErpStock);

    /**
     * 修改ERP-库存
     *
     * @param tErpStock ERP-库存
     * @return 结果
     */
    public int updateTErpStock(TErpStock tErpStock);

    /**
     * 删除ERP-库存
     *
     * @param id ERP-库存主键
     * @return 结果
     */
    public int deleteTErpStockById(Long id);

    /**
     * 批量删除ERP-库存
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTErpStockByIds(Long[] ids);

    /**
     * 清除库存数据
     * @return
     */
    public int cleanTErpStock();
}
