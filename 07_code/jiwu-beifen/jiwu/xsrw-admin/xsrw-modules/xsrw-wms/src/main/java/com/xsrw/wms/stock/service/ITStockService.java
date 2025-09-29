package com.xsrw.wms.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.api.domain.dto.TStockMoveApiDTO;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.domain.dto.StockListDTO;
import com.xsrw.wms.stock.domain.dto.TStockInDTO;
import com.xsrw.wms.stock.domain.vo.TStockListVo;
import com.xsrw.wms.stock.domain.vo.StockVo;

import java.util.List;
import java.util.Map;

/**
 * 库存详情Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITStockService extends IService<TStock> {

    /**
     * 查询库存详情列表
     *
     * @param stock 库存详情
     * @return 库存详情集合
     */
    List<StockVo> selectTStockList(StockVo stock);

    /**
     * 查询库存详情
     *
     * @param id 库存详情主键
     * @return 库存详情
     */
    TStock selectTStockById(Long id);

    /**
     * 新增库存详情
     *
     * @param tStock 库存详情
     * @return 结果
     */
    int insertTStock(TStock tStock);

    /**
     * 修改库存详情
     *
     * @param tStock 库存详情
     * @return 结果
     */
    int updateTStock(TStock tStock);

    /**
     * 批量删除库存详情
     *
     * @param ids 需要删除的库存详情主键集合
     * @return 结果
     */
    int deleteTStockByIds(Long[] ids);

    /**
     * 删除库存详情信息
     *
     * @param id 库存详情主键
     * @return 结果
     */
    int deleteTStockById(Long id);

    /**
     * 根据ids-批量冻结/解冻
     *
     * @param stockIds
     * @param isFreeze   冻结标识
     * @param originType 冻结原因
     * @return
     */
    AjaxResult updateFreezeByIds(List<Long> stockIds, String isFreeze, String originType);

    /**
     * 批次总数量
     *
     * @param stock
     * @return
     */
    List<StockVo> listBatchSum(StockVo stock);

    /**
     * 库内移位添加数据
     *
     * @param stockId 库存id
     * @return
     */
    AjaxResult shift(Long stockId);

    /**
     * 查询托盘物料总数
     *
     * @param trayCode
     * @return
     */
    AjaxResult getTrayNum(String trayCode);

    /**
     * 通过库位id查询库存数据
     *
     * @param locationId
     * @return
     */
    AjaxResult queryLocation(Long locationId);


    /**
     * 查询库存
     *
     * @param materialId
     * @param batchCode
     * @return
     */
    AjaxResult queryStock(Long materialId, String batchCode);

    /**
     * 根据载具id获取对应的库存信息
     *
     * @param trayId
     * @return
     */
    List<StockVo> getStockListByTrayId(Long trayId);

    //    /**
//     * @description: 自动拣货任务
//     * @param outDeliveryId
//     * @return
//     */
//    AjaxResult autoPickTask(Long outDeliveryId);
//
//    /**
//     * 查询出库库存列表
//     * @param stock
//     * @return
//     */
//    List<StockVo> selectStockListByOut(StockVo stock);
    AjaxResult unFreeLocation(String locationsId, String isFreeze, String originType);

    /**
     * pda库存查询
     * @param stockVo
     * @return
     */
    List<StockVo> getStockList(StockVo stockVo);
    List<Map<String,Object>>getMaterialBatchByLocationId(List<Long> locationIds);
    List<TStock>getStockByMaterialList(Long materialId);

    /**
     * 直接移库
     * @param stockMoveApiDTO
     * @return
     */
    AjaxResult directTransfer(TStockMoveApiDTO stockMoveApiDTO);

    /**
     * 根据物料和批次号查询在库信息
     * @param deliveryApiDTO
     * @return
     */
    List<StockVo> getStockListByMaterial(TStockMoveApiDTO deliveryApiDTO);

    /**
     * 计算库存
     * @param tStockInDTO
     */
    void moveInfoStock(TStockInDTO tStockInDTO);

    /**
     * 获取库存信息列表
     * @param stockListDTO
     * @return
     */
    List<TStockListVo> stockList(StockListDTO stockListDTO);
}
