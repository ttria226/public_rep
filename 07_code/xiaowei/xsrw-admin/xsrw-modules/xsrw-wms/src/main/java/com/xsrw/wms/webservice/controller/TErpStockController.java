
package com.xsrw.wms.webservice.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.webservice.domain.TErpStock;
import com.xsrw.wms.webservice.domain.vo.TErpStockVO;
import com.xsrw.wms.webservice.service.ITErpStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 863Soft
 * @date 2024/8/27
 * @description <p>备注：</p>
 */
@RestController
@RequestMapping("/webservice/erp/stock")
public class TErpStockController extends BaseController {

    @Autowired
    private ITErpStockService stockService;

    /**
     * ERP-库存列表
     * @param tErpStock
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(TErpStock tErpStock) {
        startPage();
        List<TErpStockVO> list = stockService.selectTErpStockList(tErpStock);
        return getDataTable(list);
    }


    /**
     * 导出入ERP-库存列表
     */
    @RequiresPermissions("inout:registration:export")
    @Log(title = "ERP-库存列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TErpStock tErpStock) {
        List<TErpStockVO> list = stockService.selectTErpStockList(tErpStock);
        ExcelUtil<TErpStockVO> util = new ExcelUtil<>(TErpStockVO.class);
        util.exportExcel(response, list, "ERP-库存列表");
    }

}
