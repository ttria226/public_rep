package com.xsrw.wms.stock.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.stock.domain.TStockChangeLog;

/**
 * 库存详情Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITStockChangeLogService extends IService<TStockChangeLog> {

    public List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stockChangLogList(Long materialDetailId);

    public List<com.xsrw.wms.stock.domain.vo.StockChangeLogVo> stocklist(String materialCode);

    public AjaxResult updateStock(TStockChangeLog tStockChangeLog);
}
