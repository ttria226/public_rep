package com.xsrw.wms.check.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TLocation;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.service.ITLocationService;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.check.domain.TCheckDelivery;
import com.xsrw.wms.check.domain.TCheckHistory;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.vo.CheckDeliveryVO;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.service.ITCheckDeliveryService;
import com.xsrw.wms.check.service.ITCheckHistoryService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import com.xsrw.wms.inout.domain.vo.TMaterialDetailVO;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.stock.domain.TStock;
import com.xsrw.wms.stock.mapper.TStockMapper;
import com.xsrw.wms.stock.service.ITStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 盘点计划Controller
 *
 * @author lyx
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/checkDelivery")
public class TCheckDeliveryController extends BaseController
{
    @Autowired
    private ITCheckDeliveryService tCheckDeliveryService;

    @Autowired
    private ITTaskDetailService taskDetailService;
    @Autowired
    private ITMaterialDetailService tMaterialDetailService;
    @Autowired
    private ITLocationService itLocationService;
    @Autowired
    private TStockMapper tStockMapper;

    /**
     * 查询盘点计划列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("check:checkDelivery:list")
    @GetMapping("/list")
    public TableDataInfo list(CheckDeliveryDTO checkDelivery) {
        startPage();
//        if (checkDelivery.getTrayType().equals("1")) { //载具类型 1托盘、2料箱、3地堆
//            checkDelivery.setTrayType("8");
//        } else if (checkDelivery.getTrayType().equals("2")) {
//            checkDelivery.setTrayType("9");
//        } else if (checkDelivery.getTrayType().equals("3")) {
//            checkDelivery.setTrayType("10");
//        }
//        List<CheckDeliveryVO> list = tCheckDeliveryService.selectTCheckDeliveryList(checkDelivery);
        List<CheckDeliveryVO> list = tCheckDeliveryService.selectCheckMaterialDetailList(checkDelivery);
        return getDataTable(list);
    }

    /**
     * 导出盘点计划列表
     */
    @RequiresPermissions("check:checkDelivery:export")
    @Log(title = "盘点计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckDeliveryDTO checkDelivery)
    {
        List<CheckDeliveryVO> list = tCheckDeliveryService.selectTCheckDeliveryList(checkDelivery);
        ExcelUtil<CheckDeliveryVO> util = new ExcelUtil<>(CheckDeliveryVO.class);
        util.exportExcel(response, list, "盘点计划数据");
    }

    /**
     * 获取盘点计划详细信息
     */
    @RequiresPermissions("check:checkDelivery:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCheckDeliveryService.selectTCheckDeliveryById(id));
    }

    /**
     * 新增盘点计划
     */
    @RequiresPermissions("check:checkDelivery:add")
    @Log(title = "盘点计划", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody Map<String,Object> data)
    {
//        return tCheckDeliveryService.insertTCheckDelivery(data);
        if (CollectionUtils.isEmpty(data)){
            return AjaxResult.error("参数不可为空");
        }

        // 载具类型
        String trayType = data.get("trayType").toString();
        // 盘点维度
        String checkType = data.get("checkType").toString();
        // 盘点名称
        String planName = data.get("planName").toString();

        // 盘点计划数据
        List<Map<String,Object>> maps = (List<Map<String, Object>>) data.get("data");
        List<TCheckDelivery> deliveryList = new ArrayList<>();
        maps.forEach(e -> {
            TCheckDelivery checkDelivery = new ObjectMapper().convertValue(e, TCheckDelivery.class);
            deliveryList.add(checkDelivery);
        });
        for (TCheckDelivery delivery : deliveryList) {
            QueryWrapper<TStock> stockQueryWrapper = new QueryWrapper<>();
            stockQueryWrapper.eq("material_id", delivery.getMaterialId());
            if (delivery.getReservoirId() != null) {
                // 查询库区下所有库位
                List<TLocation> locationList = itLocationService.locationList(delivery.getAreaId(), delivery.getReservoirId());
                if (locationList.size() == 0) {
                    return AjaxResult.error("该库区下无库位信息");
                }
                List<Long> collect = locationList.stream().map(TLocation::getId).collect(Collectors.toList());
                stockQueryWrapper.in("location_id", collect);
            }
            if (delivery.getAreaId() != null) {
                stockQueryWrapper.eq("area_id", delivery.getAreaId());
            }

            List<TStock> stockList = tStockMapper.selectList(stockQueryWrapper);
            List<TStock> unFinishList = stockList.stream()
                    .filter(e -> Constants.STOCK_IS_FREEZE_YES.equals(e.getIsFreeze())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(unFinishList)) {
                return AjaxResult.error("所选物料库存已全部冻结不可生成盘点计划");
            }

            // 新增盘点计划
            TCheckDelivery checkDelivery = new TCheckDelivery();
            checkDelivery.setPlanName(planName);
            checkDelivery.setCheckType(checkType);
            checkDelivery.setMaterialId(delivery.getMaterialId());
            checkDelivery.setReservoirId(delivery.getReservoirId());
            checkDelivery.setAreaId(delivery.getAreaId());
            checkDelivery.setDelFlag(Constants.DEL_FLAG_NO);
            checkDelivery.setTrayType(trayType);
            checkDelivery.setLocationId(delivery.getLocationId());
            checkDelivery.setBatchCode(delivery.getBatchCode());
            tCheckDeliveryService.save(checkDelivery);
        }
        return AjaxResult.success();
    }

//    /**
//     * 修改盘点计划
//     */
//    @RequiresPermissions("check:checkDelivery:edit")
//    @Log(title = "盘点计划", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TCheckDelivery tCheckDelivery)
//    {
//        return toAjax(tCheckDeliveryService.updateTCheckDelivery(tCheckDelivery));
//    }

    /**
     * 删除盘点计划
     */
    @RequiresPermissions("check:checkDelivery:remove")
    @Log(title = "盘点计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCheckDeliveryService.deleteTCheckDeliveryByIds(ids));
    }

    /**
     * @description: 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    @Transactional
    @PostMapping("/checkdelivery/submit")
    public AjaxResult checkdeliverySubmit(@RequestBody CheckDeliveryDTO checkDeliveryDTO){
        return tCheckDeliveryService.checkdeliverySubmit(checkDeliveryDTO);
    }


    /**
     * @description: 盘点详情
     * @param trayCode
     * @return
     */
    @GetMapping("/checkdelivery/detail")
    public AjaxResult executeTask(Long taskId,String trayCode,String checkType,
                                  @RequestParam(name = "batch",required = false)String batch,String rfid){
        return tCheckDeliveryService.executeTask(taskId, trayCode, checkType, batch,rfid);
    }


    @GetMapping("/getDropdownData")
    public AjaxResult getDropdownData(@RequestParam String taskId){
        return  taskDetailService.getDropdownData(Long.parseLong(taskId));
    }
}
