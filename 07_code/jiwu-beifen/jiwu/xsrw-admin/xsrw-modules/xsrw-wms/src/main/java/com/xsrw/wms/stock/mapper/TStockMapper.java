package com.xsrw.wms.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.report.domain.dto.StoreStatisticsDTO;
import com.xsrw.wms.report.domain.dto.WareHouseAgeAnalyseDTO;
import com.xsrw.wms.report.domain.vo.DeadStockWarningVO;
import com.xsrw.wms.report.domain.vo.StoreStatisticsVO;
import com.xsrw.wms.report.domain.vo.ValidityWarningReportVO;
import com.xsrw.wms.report.domain.vo.WareHouseAgeAnalyseVO;
import com.xsrw.wms.stock.domain.ErpWmsBatchCode;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.vo.TStockListVo;
import com.xsrw.wms.stock.domain.vo.StockVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 库存详情Mapper接口
 *
 * @author lyx
 * @date 2023-05-09
 */
@Repository
public interface TStockMapper extends BaseMapper<TStock>
{

    /**
     * 查询库存详情列表
     *
     * @param tStock 库存详情
     * @return 库存详情集合
     */
    public List<TStock> selectTStockList(TStock tStock);


    /**
     * 删除库存详情
     *
     * @param id 库存详情主键
     * @return 结果
     */
    public int deleteTStockById(Long id);

    /**
     * 批量删除库存详情
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTStockByIds(Long[] ids);

    /**
     * 批量冻结/解冻
     * @param ids
     * @param isFreeze 冻结标识
     * @param originType 冻结原因
     * @return
     */
    int updateFreezeByIds(@Param("ids") List<Long> ids,@Param("isFreeze") String isFreeze,@Param("originType") String originType);

    /**
     * 查询库存列表
     *
     * @param stock 库存
     * @return 库存集合
     */
    List<StockVo> selectStockList(@Param("stock") StockVo stock);

   /**
     * 获取库存列表数据
     * @param stock
     * @param materialIds
     * @return
     */
    List<StockVo> listBatchSum(@Param("stock")  StockVo stock, @Param("materialIds")  List<Long> materialIds);

    /**
     * 获取托盘库存详情
     * @param trayId
     * @return
     */
    List<StockVo> getStockListByTrayId(Long trayId);

    /**
     * 库存查询
     * @param stockVo
     * @return
     */
    List<StockVo> getStockList(StockVo stockVo);

    List<Map<String,Object>> getMaterialBatchByLocationId(@Param("locationIds") List<Long> locationIds);

    List<TStock>getStockByMaterialList(Long materialId);

    /**
     * 获取库位有效期预警列表
     * @param materialCode
     * @param materialName
     * @return
     */
    List<ValidityWarningReportVO> getValidityWarning(@Param("materialCode") String materialCode,@Param("materialName") String materialName);

    List<DeadStockWarningVO> selectDeadStockList(@Param("materialCode") String materialCode, @Param("materialName") String materialName,@Param("dzpDate") Integer dzpDate);

    List<WareHouseAgeAnalyseVO> selectAareHouseAgeAnalyse(WareHouseAgeAnalyseDTO request);

    List<StoreStatisticsVO> selectStoreStatisticsList(StoreStatisticsDTO request);

    /**
     * 根据载具id获取库存信息
     * @param trayId
     * @return
     */
    List<StockVo> getDeliveryDetailByTray(Long trayId);

    /**
     * 根据ids查询库存信息
     * @param stockIds
     * @return
     */
    List<StockVo> selectStockInfoByIds(@Param("stockIds") List<Long> stockIds);


    /**
     * 盘点任务生成 查询库存数据
     * @param materialId
     * @return
     */
    List<TStock> selectDeliveryStock(@Param("trayType") String trayType,
                                     @Param("materialId") Long materialId,
                                     @Param("materialIds") List<Long> materialIds,
                                     @Param("locationId") List<Long> locationId);

    /**
     * 根据rfid查询相关在库信息
     * @param rfid
     * @return
     */
    List<StockVo> getStockListByMaterial(@Param("rfid") String rfid);

    List<TStockListVo> selectListByParam(StockListDTO stockListDTO);


    /**
     * 查询 随机物料id
     * @param randomNum
     * @return
     */
    List<Long> getRoundMaterial(Integer randomNum);


    /**
     * 查询物料库存
     * @param materialId
     * @return
     */
    Integer getMaterialNum(@Param("materialId") Long materialId,@Param("type") String type);









    /**
     * 库存总额
     * @return
     */
    BigDecimal stockTotalAamount();


    /**
     * 库存类别比例
     * @return
     */
    List<Map<String,Object>> categoryRatio(String name);


    /**
     * 物资库龄分析
     * @param type
     * @return
     */
    Integer storageAge(String type);


    /**
     * 物资库龄情况
     * @param type
     * @return
     */
    Map<String,Object> storageAgeInfo(String type);


    /**
     * 最低库存预警
     * @return
     */
    List<Map<String,Object>> minimumStock();


    /**
     * 最高库存预警
     * @return
     */
    List<Map<String,Object>> maximumStock();

    /**
     * 处理批次号--查询所有要处理数据
     * @return
     */
    List<ErpWmsBatchCode> selectWmsList(String type);
}
