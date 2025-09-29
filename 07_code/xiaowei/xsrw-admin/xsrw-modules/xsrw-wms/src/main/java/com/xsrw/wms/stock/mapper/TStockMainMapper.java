package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.kanban.domain.vo.StockListVO;
import com.xsrw.wms.report.domain.vo.ReplenishmentReportVO;
import com.xsrw.wms.report.domain.vo.ValidityWarningReportVO;
import com.xsrw.wms.stock.domain.TStockMain;
import com.xsrw.wms.stock.domain.vo.StockMainVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 库存查询Mapper接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface TStockMainMapper extends BaseMapper<TStockMain>
{

    /**
     * 查询库存查询列表
     *
     * @param tStockMain 库存查询
     * @return 库存查询集合
     */
    List<TStockMain> selectTStockMainList(TStockMain tStockMain);

    /**
     * 查询列表
     * @param tStockMain
     * @return
     */
    List<StockMainVo> selectTStockMainInfoList(TStockMain tStockMain);

    List<StockMainVo> selectStockMainListTwo(@Param("stock") TStockMain stockMain, @Param("materialId") List<Long> materialId);

    /**
     * 删除库存查询
     *
     * @param id 库存查询主键
     * @return 结果
     */
    int deleteTStockMainById(Long id);

    /**
     * 批量删除库存查询
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTStockMainByIds(Long[] ids);

    /**
     * 通过部门、仓库、物料更新数量
     * @param stockMain
     * @return
     */
    int updateCountByParam(@Param("stockMain") TStockMain stockMain);

    /**
     * cims根据物料ID获取库存信息
     * @param materialIds
     * @return
     */
    List<TStockMain> getStockByMaterialIds(@Param("materialIds") List<Long> materialIds);

    /**
     * 获取库存补货列表
     * @param materialCode
     * @param materialName
     * @return
     */
    List<ReplenishmentReportVO> getReplenishmentStock(@Param("materialCode") String materialCode, @Param("materialName") String materialName);

    /**
     * 物料库存top
     * @return
     */
    List<StockListVO> selectStockList();


    /**
     * 告警查询使用
     * @return
     */
    List<TStockMain> wranList(ValidityWarningReportVO vo);
}
