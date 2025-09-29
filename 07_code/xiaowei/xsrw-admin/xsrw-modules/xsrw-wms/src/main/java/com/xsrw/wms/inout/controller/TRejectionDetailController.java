package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.inout.domain.dto.TRejectionDetailDTO;
import com.xsrw.wms.inout.domain.vo.TRejectionDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.service.ITRejectionDetailService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 拒收管理Controller
 *
 * @author wxr
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/inout/rejection")
public class TRejectionDetailController extends BaseController {
    @Autowired
    private ITRejectionDetailService tRejectionDetailService;

    /**
     * 查询拒收管理列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("inout:rejection:list")
    @GetMapping("/list")
    public TableDataInfo list(TRejectionDetailDTO tRejectionDetail) {
        startPage();
        List<TRejectionDetailVO> list = tRejectionDetailService.selectTRejectionDetailList(tRejectionDetail);
        return getDataTable(list);
    }

    /**
     * 导出拒收管理列表
     */
    @RequiresPermissions("inout:rejection:export")
    @Log(title = "拒收管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRejectionDetailDTO tRejectionDetail) {
        List<TRejectionDetailVO> list = tRejectionDetailService.selectTRejectionDetailList(tRejectionDetail);
        ExcelUtil<TRejectionDetailVO> util = new ExcelUtil<>(TRejectionDetailVO.class);
        util.exportExcel(response, list, "拒收管理数据");
    }

    /**
     * 获取拒收管理详细信息
     */
    @RequiresPermissions("inout:rejection:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tRejectionDetailService.selectTRejectionDetailById(id));
    }

    /**
     * 删除拒收管理
     */
    @RequiresPermissions("inout:rejection:remove")
    @Log(title = "拒收管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tRejectionDetailService.deleteTRejectionDetailByIds(ids));
    }
}
