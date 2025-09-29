package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.inout.domain.dto.TAdvancePutDTO;
import com.xsrw.wms.inout.domain.vo.TAdvancePutVO;
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
import com.xsrw.wms.inout.domain.TAdvancePut;
import com.xsrw.wms.inout.service.ITAdvancePutService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 入库入库单Controller
 *
 * @author wxr
 * @date 2023-06-05
 */
@RestController
@RequestMapping("/inout/put")
public class TAdvancePutController extends BaseController {
    @Autowired
    private ITAdvancePutService tAdvancePutService;

    /**
     * 查询入库入库单列表
     */
    @RequiresPermissions("inout:put:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvancePutDTO tAdvancePut) {
        startPage();
        List<TAdvancePutVO> list = tAdvancePutService.selectTAdvancePutList(tAdvancePut);
        return getDataTable(list);
    }

    /**
     * 导出入库入库单列表
     */
    @RequiresPermissions("inout:put:export")
    @Log(title = "入库入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvancePutDTO tAdvancePut) {
        List<TAdvancePutVO> list = tAdvancePutService.selectTAdvancePutList(tAdvancePut);
        ExcelUtil<TAdvancePutVO> util = new ExcelUtil<>(TAdvancePutVO.class);
        util.exportExcel(response, list, "入库入库单数据");
    }

    /**
     * 获取入库入库单详细信息
     */
    @RequiresPermissions("inout:put:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvancePutService.selectTAdvancePutById(id));
    }

    /**
     * 新增入库入库单
     */
    @RequiresPermissions("inout:put:add")
    @Log(title = "入库入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAdvancePut tAdvancePut) {
        return tAdvancePutService.insertTAdvancePut(tAdvancePut);
    }

    /**
     * 修改入库入库单
     */
    @RequiresPermissions("inout:put:edit")
    @Log(title = "入库入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAdvancePut tAdvancePut) {
        return toAjax(tAdvancePutService.updateTAdvancePut(tAdvancePut));
    }

    /**
     * 删除入库入库单
     */
    @RequiresPermissions("inout:put:remove")
    @Log(title = "入库入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAdvancePutService.deleteTAdvancePutByIds(ids));
    }

    /**
     * 入库单状态更新
     *
     * @param tAdvancePut
     * @return
     */
    @Log(title = "入库单状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody TAdvancePut tAdvancePut) {
        if (tAdvancePut.getId() == null || StringUtils.isEmpty(tAdvancePut.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tAdvancePutService.updateById(tAdvancePut));
    }

}
