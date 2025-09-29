package com.xsrw.wms.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.domain.*;
import com.xsrw.wms.base.domain.vo.TAreaVO;
import com.xsrw.wms.base.domain.vo.TReservoirVO;
import com.xsrw.wms.base.service.ITAreaService;
import com.xsrw.wms.base.service.ITBomService;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.base.service.ITReservoirService;
import com.xsrw.wms.inout.domain.dto.TMaterialAPPDTO;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailDTO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailAPPVO;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailRedisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/22 17:18
 */
@RestController
@RequestMapping("/api/base")
public class TBaseApiController extends BaseController {

    @Autowired
    private ITAreaService tAreaService;
    @Autowired
    private ITReservoirService tReservoirService;
    @Autowired
    private ITLocationService tLocationService;
    @Autowired
    private ITBomService tBomService;
    @Autowired
    private RedisService redisService;
    /**
     * 查询区域列表
     */
    @GetMapping("/area/selectList")
    public AjaxResult list(TArea tArea) {
        List<TAreaVO> list = tAreaService.selectTAreaList(tArea);
        return AjaxResult.success(list);
    }

    /**
     * 查询库区列表
     *
     * @param tReservoir
     * @return
     */
    @GetMapping("/reservoir/selectList")
    public AjaxResult list(TReservoir tReservoir) {
        List<TReservoirVO> list = tReservoirService.selectTReservoirList(tReservoir);
        return AjaxResult.success(list);
    }

    /**
     * 查询库位列表
     *
     * @param tLocation
     * @return
     */
    @GetMapping("/location/selectList")
    public AjaxResult list(TLocation tLocation) {
        List<TLocation> list = tLocationService.selectSimpleList(tLocation);
        return AjaxResult.success(list);
    }


    /**
     * 物料详情列表
     *
     * @param tMaterialAPPDTO
     * @return
     */
//    @PostMapping("/material/detail/alllist")
//    public AjaxResult alllist(@RequestBody TMaterialAPPDTO tMaterialAPPDTO) {
//        List<TMaterialDetailDTO> tMaterialDetailDTOS = tMaterialAPPDTO.gettMaterialDetailDTOS();
//        if (ObjectUtils.isEmpty(tMaterialDetailDTOS)) {
//            return null;
//        }
//        List<TMaterialDetailAPPVO> tMaterialDetailAPPVOS = new ArrayList<>();
//
//        System.out.println(".........................." + tMaterialDetailDTOS.size() + "..........................");
//        List<TMaterialDetailRedisVO> tMaterialDetailVOS = new ArrayList<>();
//        for (int i = 0; i < tMaterialDetailDTOS.size(); i++) {
//            TMaterialDetailDTO tMaterialDetailDTO = tMaterialDetailDTOS.get(i);
//            TMaterialDetailRedisVO cacheObject = redisService.getCacheObject("wms:materialDetail:" + tMaterialDetailDTO.getId());
//            if (cacheObject != null) {
//                tMaterialDetailVOS.add(cacheObject);
//            }
//        }
//        Map<String, List<TMaterialDetailRedisVO>> redisVOMap = tMaterialDetailVOS.stream().collect(Collectors.groupingBy(TMaterialDetailRedisVO::getBatchCode));
//        redisVOMap.forEach((key, value) -> {
//            if (CollectionUtils.isNotEmpty(value)) {
//                TMaterialDetailAPPVO tMaterialDetailAPPVO = new TMaterialDetailAPPVO();
//                List<String> rfids = value.stream().map(TMaterialDetailRedisVO::getRfid).collect(Collectors.toList());
//                tMaterialDetailAPPVO.setRfids(rfids);
//                tMaterialDetailAPPVO.setCount(rfids.size());
//                tMaterialDetailAPPVO.setBatchCode(key);
//                tMaterialDetailAPPVO.setMaterialId(value.get(0).getMaterialId());
//                tMaterialDetailAPPVO.setMaterialName(value.get(0).getMaterialName());
//                tMaterialDetailAPPVOS.add(tMaterialDetailAPPVO);
//            }
//
//        });
//        return AjaxResult.success(tMaterialDetailAPPVOS);
//    }

    /**
     * 物料详情列表
     *
     * @param tMaterialAPPDTO
     * @return
     */
    @PostMapping("/material/detail/alllist")
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
                tMaterialDetailAPPVO.setCount(value.get(0).getCount());
                tMaterialDetailAPPVO.setBatchCode(key);
                tMaterialDetailAPPVO.setMaterialId(value.get(0).getMaterialId());
                tMaterialDetailAPPVO.setMaterialName(value.get(0).getMaterialName());
                tMaterialDetailAPPVOS.add(tMaterialDetailAPPVO);
            }

        });
        return AjaxResult.success(tMaterialDetailAPPVOS);
    }
    /**
     * 获取bom列表
     *
     * @param tBom
     * @return
     */
    @GetMapping("/getBomList")
    public TableDataInfo getBomList(TBom tBom) {
        startPage();
        List<TBom> list = tBomService.selectTBomList(tBom);
        return getDataTable(list);
    }

    /**
     * 获取bom详细信息
     */
    @GetMapping(value = "/getBomeDetail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tBomService.selectTBomDetailStockList(id));
    }


    /**
     * 获取空闲库位列表
     * @param locationId
     * @return
     */
    @GetMapping("/getOtherLocation")
    public AjaxResult getOtherLocation(Long locationId) {
        return AjaxResult.success(tLocationService.getOtherLocation(locationId));
    }

}
