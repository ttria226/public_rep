package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.inout.domain.TOutRemoval;
import com.xsrw.wms.inout.domain.dto.TAdvanceCollectionDTO;
import com.xsrw.wms.inout.domain.dto.TOutRemovalDTO;
import com.xsrw.wms.inout.domain.vo.TOutRemovalVO;
import com.xsrw.wms.inout.service.ITOutRemovalService;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 出库单Controller
 *
 * @author zjj
 * @date 2023-06-05
 */
@RestController
@RequestMapping("/inout/removal")
public class TOutRemovalController extends BaseController {
    @Autowired
    private ITOutRemovalService tOutRemovalService;

    /**
     * 查询出库单列表
     */
//    @RequiresPermissions("base:removal:list")
    @GetMapping("/list")
    public TableDataInfo list(TOutRemoval tOutRemoval) {
        startPage();
        List<TOutRemovalVO> list = tOutRemovalService.selectTOutRemovalList(tOutRemoval);
        return getDataTable(list);
    }

    /**
     * 导出出库单列表
     */
    @RequiresPermissions("base:removal:export")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOutRemoval tOutRemoval) {
        List<TOutRemovalVO> list = tOutRemovalService.selectTOutRemovalList(tOutRemoval);
        ExcelUtil<TOutRemovalVO> util = new ExcelUtil<>(TOutRemovalVO.class);
        util.exportExcel(response, list, "出库单数据");
    }

    /**
     * 获取出库单详细信息
     */
    @RequiresPermissions("base:removal:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tOutRemovalService.selectTOutRemovalById(id));
    }

    /**
     * 新增出库单
     */
//    @RequiresPermissions("base:removal:add")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutRemoval tOutRemoval) {
        if (tOutRemoval.getOriginId() == null) {
            return AjaxResult.error("来源单据编号不可以为空！");
        }
        return tOutRemovalService.insertTOutRemoval(tOutRemoval);
    }

    /**
     * 修改出库单
     */
    @RequiresPermissions("base:removal:edit")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutRemoval tOutRemoval) {
        return toAjax(tOutRemovalService.updateTOutRemoval(tOutRemoval));
    }

    /**
     * 删除出库单
     */
    @RequiresPermissions("base:removal:remove")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tOutRemovalService.deleteTOutRemovalByIds(ids));
    }

    /**
     * 发货退货状态更新
     *
     * @param tOutRemoval
     * @return
     */
    @Log(title = "发货退货状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody TOutRemoval tOutRemoval) {
        if (tOutRemoval.getId() == null || StringUtils.isEmpty(tOutRemoval.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tOutRemovalService.updateById(tOutRemoval));
    }

    /**
     * 出库发货退货单退货
     *
     * @param tOutRemovalDTO
     * @return
     */
    @Log(title = "出库发货退货单退货", businessType = BusinessType.INSERT)
    @PostMapping("/returnStatus")
    public AjaxResult returnStatus(@RequestBody TOutRemovalDTO tOutRemovalDTO) {
        if (tOutRemovalDTO.getId() == null || CollectionUtils.isEmpty(tOutRemovalDTO.gettOutDeliveryDetailList())) {
            return AjaxResult.error("参数不全");
        }
        return tOutRemovalService.returnStatus(tOutRemovalDTO);
    }

}
