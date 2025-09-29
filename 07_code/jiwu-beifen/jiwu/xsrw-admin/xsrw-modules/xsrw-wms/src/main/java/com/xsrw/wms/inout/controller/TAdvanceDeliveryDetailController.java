package com.xsrw.wms.inout.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/26 10:19
 */
@RestController
@RequestMapping("/inout/delivery/detail")
public class TAdvanceDeliveryDetailController extends BaseController {

    @Autowired
    private ITAdvanceDeliveryDetailService tAdvanceDeliveryDetailService;

    /**
     * 查询入库单详情列表
     */
    @RequiresPermissions("inout:registration:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        startPage();
        List<String> inStatusList = new ArrayList<>();
        inStatusList.add(Constants.INOUT_NEXTFLAG_CHECKED);
        inStatusList.add(Constants.INOUT_NEXTFLAG_PUT);
        inStatusList.add(Constants.INOUT_NEXTFLAG_EXE_PART);
        inStatusList.add(Constants.INOUT_NEXTFLAG_EXE_END);
        tAdvanceDeliveryDetail.setInStatusList(inStatusList);
        List<TAdvanceDeliveryDetailVO> list = tAdvanceDeliveryDetailService.selectTAdvanceDeliveryDetailList(tAdvanceDeliveryDetail);
        return getDataTable(list);
    }

    /**
     * 查询可打印的单据详情列表
     * @param tAdvanceDeliveryDetail
     * @return
     */
    @GetMapping("/getPrintList")
    public TableDataInfo getPrintList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        startPage();
        //查询打印数量
        List<TAdvanceDeliveryDetailVO> list = tAdvanceDeliveryDetailService.selectPrintDetailList(tAdvanceDeliveryDetail);
        return getDataTable(list);
    }

    /**
     * 导出入库登记列表
     */
    @RequiresPermissions("inout:registration:export")
    @Log(title = "入库登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        List<String> inStatusList = new ArrayList<>();
        inStatusList.add(Constants.INOUT_NEXTFLAG_CHECKED);
        inStatusList.add(Constants.INOUT_NEXTFLAG_PUT);
        inStatusList.add(Constants.INOUT_NEXTFLAG_EXE_PART);
        inStatusList.add(Constants.INOUT_NEXTFLAG_EXE_END);
        tAdvanceDeliveryDetail.setInStatusList(inStatusList);
        List<TAdvanceDeliveryDetailVO> list = tAdvanceDeliveryDetailService.selectTAdvanceDeliveryDetailList(tAdvanceDeliveryDetail);
        ExcelUtil<TAdvanceDeliveryDetailVO> util = new ExcelUtil<>(TAdvanceDeliveryDetailVO.class);
        util.exportExcel(response, list, "入库登记数据");
    }

    /**
     * 获取入库登记详细信息
     */
    @RequiresPermissions("inout:registration:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvanceDeliveryDetailService.selectTAdvanceDeliveryDetailById(id));
    }

    /**
     * 生成上架任务
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "生成上架任务", businessType = BusinessType.INSERT)
    @PostMapping("/putaway")
    public AjaxResult putaway(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if(CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryDetailService.putaway(tAdvanceDeliveryDetailDTO);
    }

    /**
     * 地堆上架
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "地堆上架", businessType = BusinessType.INSERT)
    @PostMapping("/floorStocking")
    public AjaxResult floorStocking(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if(tAdvanceDeliveryDetailDTO.getId() == null
                || tAdvanceDeliveryDetailDTO.getLocationId() == null
                || StringUtils.isEmpty(tAdvanceDeliveryDetailDTO.getFloorStatus())){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryDetailService.floorStocking(tAdvanceDeliveryDetailDTO);
    }


//    ================================================================小微库==================
    /**
     * 齐套入库上架
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "齐套入库上架", businessType = BusinessType.INSERT)
    @PostMapping("/putawayComplete")
    public AjaxResult putawayComplete(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if(tAdvanceDeliveryDetailDTO.getId() == null || CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryDetailService.putawayComplete(tAdvanceDeliveryDetailDTO);
    }

    /**
     * 入库重新组盘
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "入库重新组盘", businessType = BusinessType.INSERT)
    @PostMapping("/afreshPutaway")
    public AjaxResult afreshPutaway(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if(tAdvanceDeliveryDetailDTO.getId() == null || CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryDetailService.afreshPutaway(tAdvanceDeliveryDetailDTO);
    }

    /**
     * 快捷入库
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
//    @Log(title = "快捷入库", businessType = BusinessType.INSERT)
//    @PostMapping("/putawayFask")
//    public AjaxResult putawayFask(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
//        if(tAdvanceDeliveryDetailDTO.getId() == null || CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())){
//            return AjaxResult.error("参数不全");
//        }
//        return tAdvanceDeliveryDetailService.putawayComplete(tAdvanceDeliveryDetailDTO);
//    }
}
