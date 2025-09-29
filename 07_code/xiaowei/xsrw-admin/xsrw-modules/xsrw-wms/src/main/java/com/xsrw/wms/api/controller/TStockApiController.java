package com.xsrw.wms.api.controller;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.api.domain.dto.TStockMoveApiDTO;
import com.xsrw.wms.inout.service.ITTaskWcsDetailService;
import com.xsrw.wms.stock.domain.vo.StockMainVo;
import com.xsrw.wms.stock.domain.vo.StockVo;
import com.xsrw.wms.stock.service.ITMoveLibraryService;
import com.xsrw.wms.stock.service.ITStockMainService;
import com.xsrw.wms.stock.service.ITStockService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/18 14:44
 */
@RestController
@RequestMapping("/api/stock")
public class TStockApiController extends BaseController {

    @Autowired
    private ITStockService tStockService;
    @Autowired
    private ITStockMainService tStockMainService;
    @Autowired
    private ITMoveLibraryService tMoveLibraryService;
    @Autowired
    private ITTaskWcsDetailService itTaskWcsDetailService;


    /**
     * 库存查询
     *
     * @param stockMain
     * @return
     */
    @GetMapping("/getMainList")
    public TableDataInfo list(StockMainVo stockMain) {
        startPage();
        List<StockMainVo> list = tStockMainService.selectTStockMainList(stockMain);
        return getDataTable(list);
    }

    /**
     * pda库存详细信息查询
     *
     * @param stockVo
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(StockVo stockVo) {
        startPage();
        List<StockVo> list = tStockService.getStockList(stockVo);
        return getDataTable(list);
    }

    /**
     * 查询库存详情
     * @param stock
     * @return
     */
    @GetMapping("/selectTStockList")
    public TableDataInfo selectTStockList(StockVo stock) {
        startPage();
        List<StockVo> list = tStockService.selectTStockList(stock);
        return getDataTable(list);
    }

    /**
     * 生成移库任务
     * 所属模块：库存-移库
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/shift/{id}")
    public AjaxResult shift(@PathVariable Long id) {
        return tStockService.shift(id);
    }

    /**
     * 库内移位
     * 所属模块：库存-移库
     *
     * @param stockId
     * @param locationInId
     * @return
     */
    @Log(title = "pda库内移位", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/shiftMoveLibrary")
    public AjaxResult shiftMoveLibrary(@RequestParam("stockId") Long stockId, @RequestParam("locationInId") Long locationInId) {
        return tMoveLibraryService.shiftMoveLibrary(stockId, locationInId);
    }


    /**
     * 移位监控--强制执行
     * 所属模块：库存-移库
     *
     * @param id
     * @return
     */
    @Log(title = "pda移位监控--强制执行", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStock")
    public AjaxResult updateStock(Long id) {
        if (id == null) {
            return AjaxResult.error("参数错误");
        }
        return itTaskWcsDetailService.updateStock(id);
    }

    /***
     * 根据物料和批次号查询在库信息
     */
    @GetMapping("/getStockListByMaterial")
    public AjaxResult getStockListByMaterial(TStockMoveApiDTO stockMoveApiDTO) {
        if (StringUtils.isEmpty(stockMoveApiDTO.getRfid())) {
            return AjaxResult.error("参数错误");
        }
        return success(tStockService.getStockListByMaterial(stockMoveApiDTO));
    }

    /**
     * 直接移库
     *
     * @param stockMoveApiDTO
     * @return
     */
    @Log(title = "pda直接移库", businessType = BusinessType.UPDATE)
    @PostMapping("/directTransfer")
    public AjaxResult directTransfer(@RequestBody TStockMoveApiDTO stockMoveApiDTO) {
        if (StringUtils.isEmpty(stockMoveApiDTO.getLocationCode())
                || StringUtils.isEmpty(stockMoveApiDTO.getRfid())
                || stockMoveApiDTO.getStockId() == null) {
            return AjaxResult.error("参数错误");
        }
        return tStockService.directTransfer(stockMoveApiDTO);
    }


}
