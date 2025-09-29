package com.xsrw.wms.api.controller;

import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: PDA 直接盘点
 * @Author XMING
 * @Date 2023-06-29
 */
@RestController
@RequestMapping("/api/checkDelivery")
public class TCheckDeliveryApiController {


    @Autowired
    ITCheckDeliveryService itCheckDeliveryService;


    /**
     * 查询库位信息
     * @param code
     * @return
     */
    @GetMapping("/locationInfo")
    public AjaxResult locationInfo(String code){
        return itCheckDeliveryService.locationInfo(code);
    }


    /**
     * 提交盘点
     * @param map
     * @return
     */
    @Log(title = "pda提交盘点", businessType = BusinessType.INSERT)
    @PostMapping("/checkData")
    public AjaxResult checkData(@RequestBody List<Map<String,Object>> map){
        return itCheckDeliveryService.checkData(map);
    }



}
