package com.xsrw.wms.api.controller;

import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TMergeDelivery;
import com.xsrw.wms.inout.service.ITMergeDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.xsrw.common.core.utils.PageUtils.startPage;

/**
 * @Description:
 * @Author XMING
 * @Date 2023-06-28
 */
@RestController
@RequestMapping("/mergeApi")
public class TMergeDeliveryApiController extends BaseController {

    @Autowired
    private ITMergeDeliveryService tMergeDeliveryService;

    /**
     * 查询待分拨列表
     */
    @RequiresPermissions("out:mergeDelivery:list")
    @GetMapping("/list")
    public TableDataInfo list(TMergeDelivery tMergeDelivery)
    {
        startPage();
        tMergeDelivery.setAllocateFlag("0");
        List<TMergeDelivery> list = tMergeDeliveryService.selectTMergeDeliveryList(tMergeDelivery);
        return getDataTable(list);
    }

    /**
     * 波次分拨
     * @param id
     * @return
     */
    @GetMapping("/allocate")
    public AjaxResult allocate(Long id){
        return tMergeDeliveryService.allocate(id);
    }


}
