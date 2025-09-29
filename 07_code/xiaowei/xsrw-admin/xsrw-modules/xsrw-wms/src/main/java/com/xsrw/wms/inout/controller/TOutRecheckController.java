package com.xsrw.wms.inout.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.vo.TOutRecheckVO;
import com.xsrw.wms.inout.service.ITMaterialDetailService;
import com.xsrw.wms.inout.service.ITOutDeliveryDetailService;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.service.ITOutRecheckService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 出库复核单Controller
 *
 * @author wxr
 * @date 2023-06-07
 */
@RestController
@RequestMapping("/inout/recheck")
public class TOutRecheckController extends BaseController {
    @Autowired
    private ITOutRecheckService tOutRecheckService;

    @Autowired
    private ITOutDeliveryDetailService itOutDeliveryDetailService;

    @Autowired
    private ITMaterialDetailService itMaterialDetailService;


    /**
     * 查询出库复核单列表
     */
    @RequiresPermissions("inout:recheck:list")
    @GetMapping("/list")
    public TableDataInfo list(TOutRecheck tOutRecheck) {
        startPage();
        List<TOutRecheckVO> list = tOutRecheckService.selectTOutRecheckList(tOutRecheck);
        return getDataTable(list);
    }

    /**
     * 导出出库复核单列表
     */
    @RequiresPermissions("inout:recheck:export")
    @Log(title = "出库复核单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOutRecheck tOutRecheck) {
        List<TOutRecheckVO> list = tOutRecheckService.selectTOutRecheckList(tOutRecheck);
        ExcelUtil<TOutRecheckVO> util = new ExcelUtil<>(TOutRecheckVO.class);
        util.exportExcel(response, list, "出库复核单数据");
    }

    /**
     * 获取出库复核单详细信息
     */
    @RequiresPermissions("inout:recheck:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOutRecheckService.selectTOutRecheckById(id));
    }

    /**
     * 新增出库复核单
     */
    @RequiresPermissions("inout:recheck:add")
    @Log(title = "出库复核单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutRecheck tOutRecheck) {
        return tOutRecheckService.insertTOutRecheck(tOutRecheck);
    }

    /**
     * 修改出库复核单
     */
    @RequiresPermissions("inout:recheck:edit")
    @Log(title = "出库复核单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutRecheck tOutRecheck) {
        return toAjax(tOutRecheckService.updateTOutRecheck(tOutRecheck));
    }

    /**
     * 删除出库复核单
     */
    @RequiresPermissions("inout:recheck:remove")
    @Log(title = "出库复核单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOutRecheckService.deleteTOutRecheckByIds(ids));
    }

    /**
     * 出库复核单状态更新
     *
     * @param tOutRecheck
     * @return
     */
    @Transactional
    @Log(title = "出库复核单状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody TOutRecheck tOutRecheck) {
        if (tOutRecheck.getId() == null || StringUtils.isEmpty(tOutRecheck.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        // 删除物料rfid数据
        TOutRecheck recheck = tOutRecheckService.getById(tOutRecheck.getId());
        List<TOutDeliveryDetail> deliveryDetailList = itOutDeliveryDetailService.list(
                new QueryWrapper<TOutDeliveryDetail>().eq("out_delivery_id", recheck.getOriginId()));

        List<Long> collect = deliveryDetailList.stream().map(TOutDeliveryDetail::getId).collect(Collectors.toList());
        itMaterialDetailService.update(new TMaterialDetail(),
                new UpdateWrapper<TMaterialDetail>()
                        .in("out_delivery_detail_id",collect)
                        .set("del_flag", Constants.DEL_FLAG_YES));

        return toAjax(tOutRecheckService.updateById(tOutRecheck));
    }

    /**
     * 选择列表
     *
     * @param tOutRecheck
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TOutRecheck tOutRecheck) {
        List<TOutRecheckVO> list = tOutRecheckService.selectTOutRecheckList(tOutRecheck);
        return AjaxResult.success(list);
    }

}
