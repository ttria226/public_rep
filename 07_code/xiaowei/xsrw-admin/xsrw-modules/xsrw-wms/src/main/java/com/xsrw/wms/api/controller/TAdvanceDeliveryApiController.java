package com.xsrw.wms.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.wms.api.domain.dto.TAdvanceDeliveryApiDTO;
import com.xsrw.wms.api.domain.dto.TAdvanceMaterialApiDTO;
import com.xsrw.wms.api.domain.dto.TAdvanceRegistrationApiDTO;
import com.xsrw.wms.api.domain.vo.TMaterialDetailApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TBomDetail;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.inout.domain.*;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDetailDTO;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/15 13:53
 */
@RestController
@RequestMapping("/api/inout/delivery")
public class TAdvanceDeliveryApiController extends BaseController {
    @Autowired
    private ITAdvanceDeliveryService tAdvanceDeliveryService;
    @Autowired
    private ITAdvanceRegistrationService tAdvanceRegistrationService;
    @Autowired
    private ITAdvanceDeliveryDetailService advanceDeliveryDetailService;
    @Autowired
    private ITMaterialDetailService materialDetailService;
    @Autowired
    private ITTrayService trayService;
    @Autowired
    private ITTaskWcsService tTaskWcsService;
    @Autowired
    private ITAdvanceQualityService tAdvanceQualityService;
    @Autowired
    private ITMaterialService tMaterialService;


    /**
     * 预约单列表
     *
     * @param tAdvanceDelivery
     * @return
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/warehousingList")
    public TableDataInfo warehousingList(TAdvanceDeliveryDTO tAdvanceDelivery) {
        startPage();
        List<TAdvanceDeliveryVO> list = tAdvanceDeliveryService.selectTAdvanceDeliveryList(tAdvanceDelivery);
        if(CollectionUtils.isNotEmpty(list)){
            if (Constants.INOUT_STATUS_CHECKED.equals(tAdvanceDelivery.getStatus())) {
                //未完成，查询是否全部上架过
                List<Long> ids = list.stream().map(TAdvanceDeliveryVO::getId).collect(Collectors.toList());
                Long[] deliveryIds = ids.stream().toArray(Long[]::new);
                List<TAdvanceDeliveryDetailVO> detailVOS = advanceDeliveryDetailService.selectDetailListByDeliveryId(null, deliveryIds);
                Map<Long, List<TAdvanceDeliveryDetailVO>> detailMap = detailVOS.stream().collect(Collectors.groupingBy(TAdvanceDeliveryDetailVO::getAdvanceDeliveryId));
                list.forEach(e -> {
                    e.setPutStatus(Constants.NO);
                    List<TAdvanceDeliveryDetailVO> detailList = detailMap.get(e.getId());
                    if(CollectionUtils.isNotEmpty(detailList)){
                        List<TAdvanceDeliveryDetailVO> equalList = detailList.stream().filter(detail -> detail.getPutawayCount().equals(detail.getDetectionCount())).collect(Collectors.toList());
                        if(detailList.size() == equalList.size()){
                            e.setPutStatus(Constants.YES);
                        }
                    }
                });
            }
        }
        return getDataTable(list);
    }

    /***
     * 获取单据物料选择列表
     */
    @GetMapping("/getMaterialCountList")
    public AjaxResult getMaterialCountList() {
        Map<String, Long> collect = new HashMap<>();
        List<TMaterialDetailApiVO> list = tAdvanceDeliveryService.getMaterialCountList();
        if (CollectionUtils.isNotEmpty(list)) {
            collect = list.stream().collect(Collectors.toMap(TMaterialDetailApiVO::getBatchCode, TMaterialDetailApiVO::getPredictCount));
        }
        return AjaxResult.success(collect);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getDetail/{id}")
    public AjaxResult getDetail(@PathVariable("id") Long id) {
        return success(tAdvanceDeliveryService.selectTAdvanceDeliveryById(id));
    }

    /**
     * 详情
     *
     * @param code
     * @return
     */
    @GetMapping("/getDetailByCode/{code}")
    public AjaxResult getDetailByCode(@PathVariable("code") String code) {
        return success(tAdvanceDeliveryService.getDetailByCode(code));
    }

    /**
     * 入库单审核
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Log(title = "入库单审核", businessType = BusinessType.INSERT)
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody TAdvanceDelivery tAdvanceDelivery) {
        if (tAdvanceDelivery.getId() == null || StringUtils.isEmpty(tAdvanceDelivery.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.approveTAdvanceDelivery(tAdvanceDelivery);
    }

    /**
     * 收货
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Log(title = "预约单收货", businessType = BusinessType.INSERT)
    @PostMapping("/registerDelivery")
    public AjaxResult registerDelivery(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        return tAdvanceDeliveryService.registerDelivery(tAdvanceDelivery);
    }

    /**
     * PDA检测接口 已废弃，与PC公用一个接口
     * 小数点修改版本备注
     *
     * @param materialApiDTO
     * @return
     */
    @Log(title = "检测", businessType = BusinessType.INSERT)
    @PostMapping("/checkMaterial")
    public AjaxResult checkMaterial(@RequestBody TAdvanceMaterialApiDTO materialApiDTO) {
        if (materialApiDTO.getId() == null || CollectionUtils.isEmpty(materialApiDTO.gettMaterialDetailList())) {
            return AjaxResult.error("参数不全");
        }
        return materialDetailService.checkMaterialByDelivery(materialApiDTO);
    }

    /**
     * 检测完成
     *
     * @param tAdvanceDelivery
     * @return
     */
    @Log(title = "检测完成", businessType = BusinessType.INSERT)
    @PostMapping("/checkDelivery")
    public AjaxResult checkDelivery(@RequestBody TAdvanceDelivery tAdvanceDelivery) {
        if (tAdvanceDelivery.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.checkDeliveryMaterial(tAdvanceDelivery);
    }


    /**
     * 快捷入库
     *
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "快捷入库", businessType = BusinessType.INSERT)
    @PostMapping("/putawayComplete")
    public AjaxResult putawayComplete(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if (tAdvanceDeliveryDetailDTO.getId() == null || CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())) {
            return AjaxResult.error("参数不全");
        }
        Long taryId = null;
        if (StringUtils.isNotEmpty(tAdvanceDeliveryDetailDTO.getTrayCode())) {
            TTray tTray = trayService.selectTTrayByCode(tAdvanceDeliveryDetailDTO.getTrayCode());
            if (tTray == null) {
                return AjaxResult.error("未查询到载具信息");
            }
            taryId = tTray.getId();
        }
        for (TTaskIn taskIn : tAdvanceDeliveryDetailDTO.getTaskInList()) {
            taskIn.setTrayId(taryId);
            taskIn.setLocationId(tAdvanceDeliveryDetailDTO.getLocationId());
        }
        return advanceDeliveryDetailService.putawayComplete(tAdvanceDeliveryDetailDTO);
    }

    /**
     * 登记
     *
     * @param advanceDeliveryList
     * @return
     */
//    @RequiresPermissions("inout:delivery:registerCount")
    @PostMapping("/registerCount")
    public AjaxResult registerCountNew(@RequestBody List<TAdvanceDeliveryApiDTO> advanceDeliveryList) {
        return tAdvanceDeliveryService.registerCountNew(advanceDeliveryList);
    }

    /**
     * 上架
     *
     * @param tAdvanceRegistrationApiDTO
     * @return
     */
    @Log(title = "pda上架", businessType = BusinessType.INSERT)
    @PostMapping("/putawayTask")
    public AjaxResult putawayTaskNew(@RequestBody TAdvanceRegistrationApiDTO tAdvanceRegistrationApiDTO) {
        if (StringUtils.isEmpty(tAdvanceRegistrationApiDTO.getTrayCode()) || CollectionUtils.isEmpty(tAdvanceRegistrationApiDTO.getMaterialList())) {
            return AjaxResult.error("参数不全");
        }
        return advanceDeliveryDetailService.putawayTaskNew(tAdvanceRegistrationApiDTO);
    }

    /**
     * pda根据单子上架
     *
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "pda根据单子上架", businessType = BusinessType.INSERT)
    @PostMapping("/putaway")
    public synchronized AjaxResult putaway(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if (CollectionUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTaskInList())) {
            return AjaxResult.error("参数不全");
        }
        //判断pda是否传了rfid
        for (TTaskIn taskIn : tAdvanceDeliveryDetailDTO.getTaskInList()) {
            if (taskIn.getActualCount().compareTo(BigDecimal.ZERO) == 1 && CollectionUtils.isEmpty(taskIn.getRfIds())) {
                return AjaxResult.error("参数不全,未获取到对应rfid信息！");
            }
        }
        if (StringUtils.isEmpty(tAdvanceDeliveryDetailDTO.getTrayCode())) {
            return AjaxResult.error("参数不全");
        } else {
            TTray tTray = trayService.selectTTrayByCode(tAdvanceDeliveryDetailDTO.getTrayCode());
            if (tTray == null) {
                return AjaxResult.error("未查询到对应载具信息");
            }
            tAdvanceDeliveryDetailDTO.getTaskInList().forEach(e -> {
                e.setTrayId(tTray.getId());
            });
        }
        return advanceDeliveryDetailService.putaway(tAdvanceDeliveryDetailDTO);
    }

    /**
     * pda地堆上架
     *
     * @param tAdvanceDeliveryDetailDTO
     * @return
     */
    @Log(title = "pda地堆上架", businessType = BusinessType.INSERT)
//    @RequiresPermissions("inout:delivery:floorTask")
    @PostMapping("/floorTask")
    public AjaxResult floorTask(@RequestBody TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetailDTO) {
        if (tAdvanceDeliveryDetailDTO.getId() == null
                || tAdvanceDeliveryDetailDTO.getLocationId() == null
//                || StringUtils.isEmpty(tAdvanceDeliveryDetailDTO.getFloorStatus())
        ) {
            return AjaxResult.error("参数不全");
        }
        tAdvanceDeliveryDetailDTO.setFloorStatus(Constants.NO);
        return advanceDeliveryDetailService.floorStocking(tAdvanceDeliveryDetailDTO);
    }

    /**
     * 获取地堆上架列表
     *
     * @param tAdvanceDeliveryDetail
     * @return
     */
    @DataScope(deptAlias = "d", userAlias = "u")
//    @RequiresPermissions("inout:delivery:floorList")
    @GetMapping("/floorList")
    public TableDataInfo getFloorList(TAdvanceDeliveryDetailDTO tAdvanceDeliveryDetail) {
        startPage();
        List<String> inStatusList = new ArrayList<>();
        inStatusList.add(Constants.INOUT_NEXTFLAG_CHECKED);
        inStatusList.add(Constants.INOUT_NEXTFLAG_PUT);
        inStatusList.add(Constants.INOUT_NEXTFLAG_EXE_PART);
        tAdvanceDeliveryDetail.setInStatusList(inStatusList);
        List<TAdvanceDeliveryDetailVO> list = advanceDeliveryDetailService.selectTAdvanceDeliveryDetailList(tAdvanceDeliveryDetail);
        return getDataTable(list);
    }


    /**
     * 查询wcs任务列表-上架任务
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @GetMapping("/getTaskList")
    public TableDataInfo getTaskList(TTaskWcs tTaskWcs) {
        startPage();
        tTaskWcs.setTaskType(Constants.TASK_TYPE_PUT);
        if (StringUtils.isEmpty(tTaskWcs.getTaskStatus())) {
            tTaskWcs.setTaskStatus(Constants.WCS_EXECUTE_STATUS_NOT);
        }
        List<TTaskWcsVO> list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        return getDataTable(list);
    }

    /**
     * 根据任务id查询详情-上架任务
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getDetailByTaskId/{id}")
    public AjaxResult getDetailByTaskId(@PathVariable("id") Long id) {
        return success(tTaskWcsService.selectTTaskWcsById(id));
    }

    /**
     * 强制执行入库-上架任务
     *
     * @param tTaskWcs
     * @return
     */
    @Log(title = "pda强制执行入库", businessType = BusinessType.UPDATE)
    @PostMapping("/executeIn")
    public AjaxResult executeTask(@RequestBody TTaskWcsDTO tTaskWcs) {
        if (tTaskWcs.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.executeTaskNew(tTaskWcs);
    }

    /**
     * 通过载具强制执行入库
     *
     * @param tTaskWcs
     * @return
     */
    @Log(title = "pda通过载具强制执行入库", businessType = BusinessType.UPDATE)
    @PostMapping("/executeInByTray")
    public AjaxResult executeInByTray(@RequestBody TTaskWcs tTaskWcs) {
        if (tTaskWcs.getTrayId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTaskWcsService.executeInByTray(tTaskWcs);
    }

    /**
     * bom补料
     *
     * @param tBomDetail
     * @return
     */
    @PostMapping(value = "/bomAdd")
    public AjaxResult bomAdd(@RequestBody TBomDetail tBomDetail) {
        return tAdvanceDeliveryService.bomAdd(tBomDetail);
    }


    /**
     * pda新增入库质检单
     */
    @Log(title = "pda新增入库质检单", businessType = BusinessType.INSERT)
    @PostMapping("/quality/addPda")
    public AjaxResult add(@RequestBody TAdvanceQuality tAdvanceQuality) {
        return tAdvanceQualityService.insertTAdvanceQuality(tAdvanceQuality);
    }


    /**
     * 根据物料编码 查询物料信息
     *
     * @param code
     * @return
     */
    @GetMapping(value = "/getMaterialInfo")
    public AjaxResult getMaterialInfo(String code) {
        return AjaxResult.success(tMaterialService.getCodeById(code));
    }

}
