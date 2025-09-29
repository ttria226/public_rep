package com.xsrw.wms.inout.controller;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.inout.domain.TAdvanceDeliveryDetail;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryDetailVO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.service.ITAdvanceDeliveryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 入库单Controller
 *
 * @author wxr
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/inout/delivery")
public class TAdvanceDeliveryController extends BaseController {
    @Autowired
    private ITAdvanceDeliveryService tAdvanceDeliveryService;

    @Autowired
    private ITAdvanceDeliveryDetailService advanceDeliveryDetailService;

    /**
     * 查询入库单列表
     */
    @RequiresPermissions("inout:delivery:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvanceDeliveryDTO tAdvanceDelivery) {
        startPage();
        List<TAdvanceDeliveryVO> list = tAdvanceDeliveryService.selectTAdvanceDeliveryList(tAdvanceDelivery);
        return getDataTable(list);
    }

    /**
     * 导出入库单列表
     */
    @RequiresPermissions("inout:delivery:export")
    @Log(title = "入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvanceDeliveryDTO tAdvanceDelivery) {
        List<TAdvanceDeliveryVO> list = tAdvanceDeliveryService.selectTAdvanceDeliveryList(tAdvanceDelivery);
        ExcelUtil<TAdvanceDeliveryVO> util = new ExcelUtil<>(TAdvanceDeliveryVO.class);
        util.exportExcel(response, list, "入库单数据");
    }

    /**
     * 获取入库单详细信息
     */
    @RequiresPermissions("inout:delivery:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvanceDeliveryService.selectTAdvanceDeliveryById(id));
    }

    /**
     * 新增入库单
     */
    @RequiresPermissions("inout:delivery:add")
    @Log(title = "入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        if (CollectionUtils.isEmpty(tAdvanceDelivery.getDeliveryDetailList())) {
            return AjaxResult.error("物料列表不可为空");
        }
        //判断是否传了物料id
        for (TAdvanceDeliveryDetail deliveryDetail : tAdvanceDelivery.getDeliveryDetailList()) {
            if (deliveryDetail.getMaterialId() == null || deliveryDetail.getPredictCount() == null) {
                return AjaxResult.error("物料列表参数不全");
            }
        }
        if (StringUtils.isEmpty(tAdvanceDelivery.getDeliveryModule())) {
            tAdvanceDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
        }

        // 校验批次号
        List<TAdvanceDeliveryDetail> detailList = tAdvanceDelivery.getDeliveryDetailList();
        List<String> batchCode = detailList.stream().filter((e) -> StringUtils.isNotEmpty(e.getBatchCode())).map(TAdvanceDeliveryDetail::getBatchCode).collect(Collectors.toList());
        if (detailList.size() != batchCode.size()){
            return AjaxResult.error("物料批次号不可为空");
        }

        return toAjax(tAdvanceDeliveryService.insertTAdvanceDelivery(tAdvanceDelivery));
    }

    /**
     * 修改入库单
     */
    @RequiresPermissions("inout:delivery:edit")
    @Log(title = "入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        if (CollectionUtils.isEmpty(tAdvanceDelivery.getDeliveryDetailList())) {
            return AjaxResult.error("物料列表不可为空");
        }
        //判断是否传了物料id
        for (TAdvanceDeliveryDetail deliveryDetail : tAdvanceDelivery.getDeliveryDetailList()) {
            if (deliveryDetail.getMaterialId() == null || deliveryDetail.getPredictCount() == null) {
                return AjaxResult.error("物料列表参数不全");
            }
        }
        if (StringUtils.isEmpty(tAdvanceDelivery.getDeliveryModule())) {
            tAdvanceDelivery.setDeliveryModule(Constants.INOUT_DELIVERY_MODULE_ORDER);
        }

        // 校验批次号
        List<TAdvanceDeliveryDetail> detailList = tAdvanceDelivery.getDeliveryDetailList();
        List<String> batchCode = detailList.stream().filter((e) -> StringUtils.isNotEmpty(e.getBatchCode())).map(TAdvanceDeliveryDetail::getBatchCode).collect(Collectors.toList());
        if (detailList.size() != batchCode.size()){
            return AjaxResult.error("物料批次号不可为空");
        }

        return toAjax(tAdvanceDeliveryService.updateTAdvanceDelivery(tAdvanceDelivery));
    }

    /**
     * 删除入库单
     */
    @RequiresPermissions("inout:delivery:remove")
    @Log(title = "入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return tAdvanceDeliveryService.deleteTAdvanceDeliveryByIds(ids);
    }


    /***
     * 获取单据物料选择列表
     */
    @GetMapping("/getMaterialSelectList")
    public TableDataInfo getMaterialSelectList(TMaterialDTO tMaterial) {
        startPage();
        List<TMaterialVO> list = tAdvanceDeliveryService.getMaterialSelectList(tMaterial);
        return getDataTable(list);
    }

    /**
     * 标签打印详情
     *
     * @param ids
     * @return
     */
    @GetMapping("/getDeatilList/{ids}")
    public AjaxResult getDeatilListByIds(@PathVariable Long[] ids) {
        return AjaxResult.success(tAdvanceDeliveryService.getDeatilListByIds(ids));
    }

    /**
     * 入库单审核
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:approve")
    @Log(title = "入库单审核", businessType = BusinessType.INSERT)
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody TAdvanceDelivery tAdvanceDelivery) {
        if (tAdvanceDelivery.getId() == null || StringUtils.isEmpty(tAdvanceDelivery.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.approveTAdvanceDelivery(tAdvanceDelivery);
    }


    /**
     * 入库单登记-old
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:register")
    @Log(title = "入库单登记", businessType = BusinessType.INSERT)
    @PostMapping("/registerOld")
    public AjaxResult register(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        return tAdvanceDeliveryService.registerTAdvanceDelivery(tAdvanceDelivery);
    }

    /**
     * 入库单检测
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:check")
    @Log(title = "入库单检测", businessType = BusinessType.INSERT)
    @PostMapping("/check")
    public AjaxResult check(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        return tAdvanceDeliveryService.checkTAdvanceDelivery(tAdvanceDelivery);
    }

    /**
     * 通过id获取子表列表
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getRegistrationInfo/{id}")
    public AjaxResult getRegistrationInfo(@PathVariable("id") Long id) {
        List<TAdvanceDeliveryDetailVO> list = advanceDeliveryDetailService.selectDetailListByDeliveryId(id, null);
        return success(list);
    }


    /**
     * 入库单检测
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:check")
    @Log(title = "入库单检测完成", businessType = BusinessType.INSERT)
    @PostMapping("/checkDelivery")
    public AjaxResult checkDelivery(@RequestBody TAdvanceDelivery tAdvanceDelivery) {
        if (tAdvanceDelivery.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.checkDeliveryMaterial(tAdvanceDelivery);
    }

    /**
     * 入库单登记
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:register")
    @Log(title = "入库单登记", businessType = BusinessType.INSERT)
    @PostMapping("/registerDelivery")
    public AjaxResult registerDelivery(@RequestBody TAdvanceDeliveryDTO tAdvanceDelivery) {
        return tAdvanceDeliveryService.registerDelivery(tAdvanceDelivery);
    }

    /**
     * 入库单作废
     *
     * @param tAdvanceDelivery
     * @return
     */
    @RequiresPermissions("inout:delivery:cancellation")
    @Log(title = "入库单作废", businessType = BusinessType.INSERT)
    @PostMapping("/cancellation")
    public AjaxResult cancellation(@RequestBody TAdvanceDelivery tAdvanceDelivery) {
        if (tAdvanceDelivery.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.cancellation(tAdvanceDelivery);
    }

    /**
     * 选择入库单列表
     *
     * @param tAdvanceDelivery
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TAdvanceDeliveryDTO tAdvanceDelivery) {
        List<TAdvanceDeliveryVO> list = tAdvanceDeliveryService.selectTAdvanceDeliveryList(tAdvanceDelivery);
        return AjaxResult.success(list);
    }

    /**
     * 删除入库单
     */
    @Log(title = "根据单据号删除入库单", businessType = BusinessType.DELETE)
    @PostMapping("/deleteByCode")
    public AjaxResult deleteByCode(@RequestBody TAdvanceDelivery advanceDelivery) {
        if (StringUtils.isEmpty(advanceDelivery.getCode())) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceDeliveryService.deleteByCode(advanceDelivery.getCode());
    }

}
