package com.xsrw.wms.base.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.ExcelMaterialVO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
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
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 物料Controller
 *
 * @author wxr
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/material")
public class TMaterialController extends BaseController {
    @Autowired
    private ITMaterialService tMaterialService;

    /**
     * 查询物料列表
     */
//    @RequiresPermissions("wms:material:list")
    @GetMapping("/list")
    public TableDataInfo list(TMaterial tMaterial) {
        startPage();
        List<TMaterialVO> list = tMaterialService.selectTMaterialList(tMaterial);
        return getDataTable(list);
    }

    /**
     * 导出物料列表
     */
    @RequiresPermissions("wms:material:export")
    @Log(title = "物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMaterial tMaterial) {
        List<TMaterialVO> list = tMaterialService.selectTMaterialList(tMaterial);
        ExcelUtil<TMaterialVO> util = new ExcelUtil<>(TMaterialVO.class);
        util.exportExcel(response, list, "物料数据");
    }

    /**
     * 下载模板信息
     *
     * @param response
     */
    @RequiresPermissions("wms:material:exportdemo")
    @Log(title = "物料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportUnitDemo(HttpServletResponse response) {
        ExcelUtil<ExcelMaterialVO> util = new ExcelUtil<>(ExcelMaterialVO.class);
        util.exportExcel(response, new ArrayList<>(), "物料管理数据");
    }

    /**
     * 导入物料信息
     *
     * @param file
     * @return
     */
    @RequiresPermissions("wms:material:import")
    @Log(title = "物料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/importData")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tMaterialService.importUnit(file);
    }

    /**
     * 导入物料信息
     *
     * @param file
     * @return
     */
    @Log(title = "导入erp物料信息", businessType = BusinessType.EXPORT)
    @PostMapping("/importDataErp")
    public AjaxResult importDataErp(MultipartFile file) throws Exception {
        return tMaterialService.importDataErp(file);
    }

    /**
     * 获取物料详细信息
     */
    @RequiresPermissions("wms:material:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tMaterialService.selectTMaterialById(id));
    }

    /**
     * 新增物料
     */
    @RequiresPermissions("wms:material:add")
    @Log(title = "物料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMaterial tMaterial) {
        return tMaterialService.insertTMaterial(tMaterial);
    }

    /**
     * 修改物料
     */
    @RequiresPermissions("wms:material:edit")
    @Log(title = "物料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMaterial tMaterial) {
        return toAjax(tMaterialService.updateTMaterial(tMaterial));
    }

    /**
     * 删除物料
     */
    @RequiresPermissions("wms:material:remove")
    @Log(title = "物料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tMaterialService.deleteTMaterialByIds(ids));
    }


    /**
     * 批量设置物料库存上限、库存下限
     *
     * @param tMaterial
     * @return
     */
    @Log(title = "批量设置物料库存上限、库存下限", businessType = BusinessType.INSERT)
    @PostMapping("/bacthStock")
    public AjaxResult bacthStock(@RequestBody TMaterialDTO tMaterial) {
        if (tMaterial.getIds() == null) {
            return AjaxResult.error("数据id不可为空");
        }
        if (tMaterial.getStockMax() == null || tMaterial.getStockMax() <= 0) {
            return AjaxResult.error("库存上限不可为空且必须大于0");
        }
        if (tMaterial.getStockMin() == null || tMaterial.getStockMin() <= 0) {
            return AjaxResult.error("库存下限不可为空且必须大于0");
        }

        if (tMaterial.getStockMin() > tMaterial.getStockMax()) {
            return AjaxResult.error("库存下限不可大于库存上限");
        }
        return AjaxResult.success(tMaterialService.bacthStock(tMaterial));
    }


    /***
     * 通过物料ids获取对应的推荐载具类型
     */
    @GetMapping("/getTrayTypeByMaterials/{ids}")
    public AjaxResult getTrayTypeByMaterials(@PathVariable Long[] ids) {
        return tMaterialService.getTrayTypeByMaterials(ids);
    }

    /***
     * 获取物料选择列表
     */
    @GetMapping("/getMaterialSelectList")
    public TableDataInfo getMaterialSelectList(TMaterialDTO tMaterial) {
        startPage();
        List<TMaterialVO> list = tMaterialService.getMaterialSelectList(tMaterial);
        return getDataTable(list);
    }

}
