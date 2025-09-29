package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.inout.domain.dto.TAdvanceCollectionDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceCollectionVO;
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
import com.xsrw.wms.inout.domain.TAdvanceCollection;
import com.xsrw.wms.inout.service.ITAdvanceCollectionService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 入库收货退货单Controller
 *
 * @author wxr
 * @date 2023-06-06
 */
@RestController
@RequestMapping("/inout/collection")
public class TAdvanceCollectionController extends BaseController {
    @Autowired
    private ITAdvanceCollectionService tAdvanceCollectionService;

    /**
     * 查询入库收货退货单列表
     */
    @RequiresPermissions("inout:collection:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvanceCollectionDTO tAdvanceCollection) {
        startPage();
        List<TAdvanceCollectionVO> list = tAdvanceCollectionService.selectTAdvanceCollectionList(tAdvanceCollection);
        return getDataTable(list);
    }

    /**
     * 导出入库收货退货单列表
     */
    @RequiresPermissions("inout:collection:export")
    @Log(title = "入库收货退货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvanceCollectionDTO tAdvanceCollection) {
        List<TAdvanceCollectionVO> list = tAdvanceCollectionService.selectTAdvanceCollectionList(tAdvanceCollection);
        ExcelUtil<TAdvanceCollectionVO> util = new ExcelUtil<>(TAdvanceCollectionVO.class);
        util.exportExcel(response, list, "入库收货退货单数据");
    }

    /**
     * 获取入库收货退货单详细信息
     */
    @RequiresPermissions("inout:collection:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvanceCollectionService.selectTAdvanceCollectionById(id));
    }

    /**
     * 新增入库收货退货单
     */
    @RequiresPermissions("inout:collection:add")
    @Log(title = "入库收货退货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAdvanceCollection tAdvanceCollection) {
        return tAdvanceCollectionService.insertTAdvanceCollection(tAdvanceCollection);
    }

    /**
     * 修改入库收货退货单
     */
    @RequiresPermissions("inout:collection:edit")
    @Log(title = "入库收货退货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAdvanceCollection tAdvanceCollection) {
        return toAjax(tAdvanceCollectionService.updateTAdvanceCollection(tAdvanceCollection));
    }

    /**
     * 删除入库收货退货单
     */
    @RequiresPermissions("inout:collection:remove")
    @Log(title = "入库收货退货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAdvanceCollectionService.deleteTAdvanceCollectionByIds(ids));
    }

    /**
     * 入库收货退货单退货
     *
     * @param tAdvanceCollectionDTO
     * @return
     */
    @Log(title = "入库收货退货单退货", businessType = BusinessType.INSERT)
    @PostMapping("/returnStatus")
    public AjaxResult returnStatus(@RequestBody TAdvanceCollectionDTO tAdvanceCollectionDTO) {
        if (tAdvanceCollectionDTO.getId() == null || CollectionUtils.isEmpty(tAdvanceCollectionDTO.getDetailList())) {
            return AjaxResult.error("参数不全");
        }
        return tAdvanceCollectionService.returnStatus(tAdvanceCollectionDTO);
    }


}
