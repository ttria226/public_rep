package com.xsrw.wms.inout.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.dto.TMaterialAPPDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSerachDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailAPPVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailMonthlyCountVo;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料入库详情Controller
 *
 * @author wxr
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/inout/detail")
public class TMaterialDetailController extends BaseController {
    @Autowired
    private ITMaterialDetailService tMaterialDetailService;
    @Autowired
    private RedisService redisService;


    @PostMapping("/alllist")
    public AjaxResult alllist(@RequestBody TMaterialAPPDTO tMaterialAPPDTO) {
        List<TMaterialDetailDTO> tMaterialDetailDTOS = tMaterialAPPDTO.gettMaterialDetailDTOS();
        if (ObjectUtils.isEmpty(tMaterialDetailDTOS)) {
            return null;
        }
        List<TMaterialDetailAPPVO> tMaterialDetailAPPVOS = new ArrayList<>();

        System.out.println(".........................." + tMaterialDetailDTOS.size() + "..........................");
        List<TMaterialDetailRedisVO> tMaterialDetailVOS = new ArrayList<>();
        for (int i = 0; i < tMaterialDetailDTOS.size(); i++) {
            TMaterialDetailDTO tMaterialDetailDTO = tMaterialDetailDTOS.get(i);
            TMaterialDetailRedisVO cacheObject = redisService.getCacheObject("wms:materialDetail:" + tMaterialDetailDTO.getId());
            if (cacheObject != null) {
                tMaterialDetailVOS.add(cacheObject);
            }
        }
        Map<String, List<TMaterialDetailRedisVO>> redisVOMap = tMaterialDetailVOS.stream().collect(Collectors.groupingBy(TMaterialDetailRedisVO::getBatchCode));
        redisVOMap.forEach((key, value) -> {
            if (CollectionUtils.isNotEmpty(value)) {
                TMaterialDetailAPPVO tMaterialDetailAPPVO = new TMaterialDetailAPPVO();
                List<String> rfids = value.stream().flatMap(x -> x.getRfids().stream()).collect(Collectors.toList());
                List<String> rfidHeads = value.stream().map(TMaterialDetailRedisVO::getRfid).collect(Collectors.toList());
                tMaterialDetailAPPVO.setRfids(rfids);
                tMaterialDetailAPPVO.setRfidHeads(rfidHeads);
                tMaterialDetailAPPVO.setCount(rfids.size());
                tMaterialDetailAPPVO.setBatchCode(key);
                tMaterialDetailAPPVO.setMaterialId(value.get(0).getMaterialId());
                tMaterialDetailAPPVO.setMaterialName(value.get(0).getMaterialName());
                tMaterialDetailAPPVOS.add(tMaterialDetailAPPVO);
            }

        });
        return AjaxResult.success(tMaterialDetailAPPVOS);
    }


    /**
     * redis更新
     *
     * @param tMaterialDetail
     * @return
     */
    @GetMapping("/alllist2")
    public AjaxResult alllist(TMaterialDetail tMaterialDetail) {
        List<TMaterialDetailVO> list = tMaterialDetailService.selectTMaterialDetailAllList(tMaterialDetail);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, List<TMaterialDetailVO>> dataList = list.stream().collect(Collectors.groupingBy(TMaterialDetailVO::getRfidHead));
            dataList.forEach((key, value) -> {
                TMaterialDetailVO detail = value.get(0);
                List<String> rfids = value.stream().map(TMaterialDetailVO::getRfid).collect(Collectors.toList());
                //redis存放
                TMaterialDetailRedisVO tMaterialDetailVO = new TMaterialDetailRedisVO();
                tMaterialDetailVO.setBatchCode(detail.getBatchCode());
                tMaterialDetailVO.setMaterialId(detail.getMaterialId());
                tMaterialDetailVO.setMaterialName(detail.getMaterialName());
                tMaterialDetailVO.setRfid(key);
                tMaterialDetailVO.setCount(rfids.size());
                tMaterialDetailVO.setRfids(rfids);
                redisService.setCacheObject("wms:materialDetail:" + tMaterialDetailVO.getRfid(), tMaterialDetailVO);

            });
            return AjaxResult.success();
        } else {
            return AjaxResult.error("未查询到数据");
        }
    }

    /**
     * redis查询
     */
    @GetMapping("/alllist3")
    public TableDataInfo alllist3(TMaterialDetail tMaterialDetail) {
        List<TMaterialDetailRedisVO> data = new ArrayList<>();
        List<TMaterialDetailVO> list = tMaterialDetailService.selectTMaterialDetailAllList(tMaterialDetail);
        if (CollectionUtils.isNotEmpty(list)) {
            Map<String, List<TMaterialDetailVO>> dataList = list.stream().collect(Collectors.groupingBy(TMaterialDetailVO::getRfidHead));
            dataList.forEach((key, value) -> {
                TMaterialDetailRedisVO cacheObject = redisService.getCacheObject("wms:materialDetail:" + key);
                data.add(cacheObject);
            });
        }
        return getDataTable(data);
    }

    /**
     * 查询物料入库详情列表
     */
//    @RequiresPermissions("inout:detail:list")
    @GetMapping("/list")
    public TableDataInfo list(TMaterialDetailSerachDTO tMaterialDetail) {
        startPage();
        List<TMaterialDetailVO> list = tMaterialDetailService.selectTMaterialDetailList(tMaterialDetail);
        return getDataTable(list);
    }

    /**
     * 导出物料入库详情列表
     */
    @RequiresPermissions("inout:detail:export")
    @Log(title = "物料入库详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMaterialDetailSerachDTO tMaterialDetail) {
        List<TMaterialDetailVO> list = tMaterialDetailService.selectTMaterialDetailList(tMaterialDetail);
        ExcelUtil<TMaterialDetailVO> util = new ExcelUtil<TMaterialDetailVO>(TMaterialDetailVO.class);
        util.exportExcel(response, list, "物料入库详情数据");
    }

    /**
     * 获取物料入库详情详细信息
     */
    @RequiresPermissions("inout:detail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tMaterialDetailService.selectTMaterialDetailById(id));
    }

    /**
     * 新增物料入库详情
     */
    @RequiresPermissions("inout:detail:add")
    @Log(title = "物料入库详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMaterialDetail tMaterialDetail) {
        return toAjax(tMaterialDetailService.insertTMaterialDetail(tMaterialDetail));
    }

    /**
     * 修改物料入库详情
     */
    @RequiresPermissions("inout:detail:edit")
    @Log(title = "物料入库详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMaterialDetail tMaterialDetail) {
        return toAjax(tMaterialDetailService.updateTMaterialDetail(tMaterialDetail));
    }

    /**
     * 删除物料入库详情
     */
    @RequiresPermissions("inout:detail:remove")
    @Log(title = "物料入库详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tMaterialDetailService.deleteTMaterialDetailByIds(ids));
    }

    /**
     * 库存物资月报表查询
     *
     * @param monthlyCountVo
     * @return
     */
    @RequiresPermissions("inout:detail:materialDetailMonthlyCountList")
    @GetMapping("/materialDetailMonthlyCountList")
    public TableDataInfo list(TMaterialDetailMonthlyCountVo monthlyCountVo) {
        startPage();
        List<TMaterialDetailMonthlyCountVo> list = tMaterialDetailService.materialDetailMonthlyCountList(monthlyCountVo);
        return getDataTable(list);
    }

    /**
     * 导出库存物资月报表
     */
    @RequiresPermissions("inout:detail:materialDetailMonthlyExport")
    @Log(title = "导出库存物资月报表", businessType = BusinessType.EXPORT)
    @PostMapping("/materialDetailMonthlyExport")
    public void materialDetailMonthlyExport(HttpServletResponse response, TMaterialDetailMonthlyCountVo monthlyCountVo) {
        List<TMaterialDetailMonthlyCountVo> list = tMaterialDetailService.materialDetailMonthlyCountList(monthlyCountVo);
        ExcelUtil<TMaterialDetailMonthlyCountVo> util = new ExcelUtil<>(TMaterialDetailMonthlyCountVo.class);
        util.exportExcel(response, list, "库存物资月报表数据");
    }

    /**
     * 通过入库单id获取物料详情
     *
     * @param deliveryId
     * @return
     */
    @GetMapping(value = "/getRegistrationList/{deliveryId}")
    public TableDataInfo getRegistrationInfo(@PathVariable("deliveryId") Long deliveryId, TMaterialDetailSerachDTO tMaterialDetail) {
        startPage();
        tMaterialDetail.setAdvanceRegistrationId(deliveryId);
        List<TMaterialDetailVO> list = tMaterialDetailService.selectTMaterialDetailList(tMaterialDetail);
        return getDataTable(list);
    }


    /**
     * 入库单检测失败
     *
     * @param tMaterialDetail
     * @return
     */
    @Log(title = "入库单检测失败", businessType = BusinessType.INSERT)
    @PostMapping("/checkMaterial")
    public AjaxResult checkMaterial(@RequestBody List<TMaterialDetail> tMaterialDetail) {
        if (CollectionUtils.isEmpty(tMaterialDetail)) {
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailService.checkMaterial(tMaterialDetail);
    }


    /**
     * 在库检测失败
     *
     * @param tMaterialDetail
     * @return
     */
    @Log(title = "在库检测失败", businessType = BusinessType.INSERT)
    @PostMapping("/checkStockMaDetail")
    public AjaxResult checkStockMaDetail(@RequestBody List<TMaterialDetail> tMaterialDetail) {
        if (CollectionUtils.isEmpty(tMaterialDetail)) {
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailService.checkStockMaDetail(tMaterialDetail);
    }

    /**
     * 单个打印rfid
     *
     * @param tMaterialDetail
     * @return
     */
    @Log(title = "打印RFID", businessType = BusinessType.INSERT)
    @PostMapping("/printRfidById")
    public AjaxResult printMaterialDetailByRfid(@RequestBody TMaterialDetailDTO tMaterialDetail) {
        if (tMaterialDetail.getDetailId() == null) {
            return AjaxResult.error("参数不全");
        }
        if (StringUtils.isEmpty(tMaterialDetail.getPrintFloor())) {
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailService.printMaterialDetailById(tMaterialDetail.getDetailId(), tMaterialDetail.getPrintFloor());
    }

//    /**
//     * 根据入库单物料打印rfid
//     * @param tMaterialDetail
//     * @return
//     */
//    @Log(title = "根据入库单物料打印rfid", businessType = BusinessType.INSERT)
//    @PostMapping("/printRfidByAdvanceId")
//    public AjaxResult printRfidByAdvanceId(@RequestBody TMaterialDetail tMaterialDetail) {
//        if(tMaterialDetail.getId() == null){
//            return AjaxResult.error("参数不全");
//        }
//        return tMaterialDetailService.printMaterialDetailByAdvanceId(tMaterialDetail.getId());
//    }

}
