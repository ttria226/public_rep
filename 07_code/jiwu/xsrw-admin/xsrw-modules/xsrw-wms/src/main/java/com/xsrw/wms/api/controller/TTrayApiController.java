package com.xsrw.wms.api.controller;

import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.stock.service.ITStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wangxueru
 * @description pda载具controller
 * @date 2023/5/18 9:30
 */
@RestController
@RequestMapping("/api/base/tray")
public class TTrayApiController extends BaseController {

    @Autowired
    private ITTrayService tTrayService;

    @Autowired
    private ITStockService stockService;

    /**
     * 获取托盘列表
     *
     * @param tTray
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(TTrayDTO tTray) {
        startPage();
        List<TTrayApiVO> list = tTrayService.selectTTrayInfoList(tTray);
        return getDataTable(list);
    }

    /**
     * 根据托盘id获取库存详情
     *
     * @param trayId
     * @return
     */
    @GetMapping("/getStockById")
    public AjaxResult getStockById(Long trayId) {
        return AjaxResult.success(stockService.getStockListByTrayId(trayId));
    }

    /**
     * 根据托盘id出库
     *
     * @param tTray
     * @return
     */
    @PostMapping("/outStock")
    @Log(title = "pda根据托盘code出库", businessType = BusinessType.EXPORT)
    public AjaxResult outStockByTrayId(@RequestBody TTray tTray) {
        if (tTray.getCode() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.takeOut(tTray);
    }

    /**
     * 根据托盘id出库回库
     * @param tTray
     * @return
     */
    @PostMapping("/recycleStock")
    @Log(title = "pda根据托盘code回库", businessType = BusinessType.EXPORT)
    public AjaxResult recycle(@RequestBody TTray tTray) {
        if (tTray.getCode() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.recycle(tTray);
    }

    /**
     * 根据载具编码获取组盘详情信息
     * @param trayCode
     * @return
     */
    @GetMapping("/getDeliveryByTrayCode")
    public AjaxResult getDeliveryByTrayCode(String trayCode) {
        return tTrayService.getDeliveryByTrayCode(trayCode);
    }


    /**
     * 根据载具编号查询入库可用状态
     * @param trayCode
     * @return
     */
    @GetMapping("/getStatusByCode")
    public AjaxResult getStatusByCode(String trayCode) {
        return tTrayService.getStatusByCode(trayCode);
    }
}
