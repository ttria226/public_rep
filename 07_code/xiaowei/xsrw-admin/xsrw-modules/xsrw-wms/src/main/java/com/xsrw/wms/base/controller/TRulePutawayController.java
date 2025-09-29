package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.domain.dto.TRulePutawayDTO;
import com.xsrw.wms.base.domain.vo.TRulePutawayVO;
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
import com.xsrw.wms.base.domain.TRulePutaway;
import com.xsrw.wms.base.service.ITRulePutawayService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 上架规则Controller
 *
 * @author wxr
 * @date 2023-06-13
 */
@RestController
@RequestMapping("/base/putaway")
public class TRulePutawayController extends BaseController {
    @Autowired
    private ITRulePutawayService tRulePutawayService;

    /**
     * 查询上架规则列表
     */
//    @RequiresPermissions("base:putaway:list")
    @GetMapping("/list")
    public TableDataInfo list(TRulePutawayDTO tRulePutaway) {
        startPage();
        List<TRulePutawayVO> list = tRulePutawayService.selectTRulePutawayList(tRulePutaway);
        return getDataTable(list);
    }

    /**
     * 导出上架规则列表
     */
//    @RequiresPermissions("base:putaway:export")
    @Log(title = "上架规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRulePutawayDTO tRulePutaway) {
        List<TRulePutawayVO> list = tRulePutawayService.selectTRulePutawayList(tRulePutaway);
        ExcelUtil<TRulePutawayVO> util = new ExcelUtil<>(TRulePutawayVO.class);
        util.exportExcel(response, list, "上架规则数据");
    }

    /**
     * 获取上架规则详细信息
     */
//    @RequiresPermissions("base:putaway:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tRulePutawayService.selectTRulePutawayById(id));
    }

    /**
     * 新增上架规则
     */
//    @RequiresPermissions("base:putaway:add")
    @Log(title = "上架规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRulePutawayDTO tRulePutaway) {
        if (CollectionUtils.isEmpty(tRulePutaway.getDetailList())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tRulePutawayService.insertTRulePutaway(tRulePutaway));
    }

    /**
     * 修改上架规则
     */
//    @RequiresPermissions("base:putaway:edit")
    @Log(title = "上架规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRulePutawayDTO tRulePutaway) {
        if (CollectionUtils.isEmpty(tRulePutaway.getDetailList())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tRulePutawayService.updateTRulePutaway(tRulePutaway));
    }

    /**
     * 删除上架规则
     */
    @RequiresPermissions("base:putaway:remove")
    @Log(title = "上架规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tRulePutawayService.deleteTRulePutawayByIds(ids));
    }

    /**
     * 更改状态
     *
     * @param tRulePutaway
     * @return
     */
    @PutMapping("/updateStatus")
    @Log(title = "存储策略更新状态", businessType = BusinessType.UPDATE)
    public AjaxResult updateStatus(@RequestBody TRulePutaway tRulePutaway) {
        if (tRulePutaway.getId() == null || StringUtils.isEmpty(tRulePutaway.getStatus())) {
            return AjaxResult.error("参数不全");
        }
        return toAjax(tRulePutawayService.updateById(tRulePutaway));
    }

}
