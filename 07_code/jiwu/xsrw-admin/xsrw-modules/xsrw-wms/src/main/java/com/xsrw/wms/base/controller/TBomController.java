package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.vo.TMaterialVO;
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
import com.xsrw.wms.base.domain.TBom;
import com.xsrw.wms.base.service.ITBomService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * bomController
 *
 * @author zjj
 * @date 2023-06-10
 */
@RestController
@RequestMapping("/bom")
public class TBomController extends BaseController {
    @Autowired
    private ITBomService tBomService;

    /**
     * 查询bom列表
     */
    @RequiresPermissions("base:bom:list")
    @GetMapping("/list")
    public TableDataInfo list(TBom tBom) {
        startPage();
        List<TBom> list = tBomService.selectTBomList(tBom);
        return getDataTable(list);
    }

    /**
     * 导出bom列表
     */
    @RequiresPermissions("base:bom:export")
    @Log(title = "bom", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TBom tBom) {
        List<TBom> list = tBomService.selectTBomList(tBom);
        ExcelUtil<TBom> util = new ExcelUtil<TBom>(TBom.class);
        util.exportExcel(response, list, "bom数据");
    }

    /**
     * 获取bom详细信息
     */
    @RequiresPermissions("base:bom:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tBomService.selectTBomById(id));
    }

    /**
     * 新增bom
     */
    @RequiresPermissions("base:bom:add")
    @Log(title = "bom", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TBom tBom) {
        if (CollectionUtils.isEmpty(tBom.getBomDetails())) {
            return AjaxResult.error("物料列表不可为空");
        }
        return toAjax(tBomService.insertTBom(tBom));
    }

    /**
     * 修改bom
     */
    @RequiresPermissions("base:bom:edit")
    @Log(title = "bom", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TBom tBom) {
        if (CollectionUtils.isEmpty(tBom.getBomDetails())) {
            return AjaxResult.error("物料列表不可为空");
        }
        return toAjax(tBomService.updateTBom(tBom));
    }

    /**
     * 删除bom
     */
    @RequiresPermissions("base:bom:remove")
    @Log(title = "bom", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tBomService.deleteTBomByIds(ids));
    }


    /**
     * 获取bom列表
     *
     * @param tBom
     * @return
     */
    @GetMapping("/getBomList")
    public AjaxResult getBomList(TBom tBom) {
        List<TBom> list = tBomService.selectTBomList(tBom);
        return AjaxResult.success(list);
    }

    /***
     * 根据bom-id获取物料信息列表
     */
    @GetMapping("/getMaterialListByBomId")
    public AjaxResult getMaterialListByBomId(Long id) {
        List<TMaterialVO> list = tBomService.getMaterialListByBomId(id);
        return AjaxResult.success(list);
    }

}
