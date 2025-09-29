package com.xsrw.wms.loan.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.loan.domain.dto.DLoanRegisterDTO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterEquipmentVO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterVO;
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
import com.xsrw.wms.loan.domain.DLoanRegister;
import com.xsrw.wms.loan.service.IDLoanRegisterService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 设备借还登记Controller
 *
 * @author wxr
 * @date 2023-06-09
 */
@RestController
@RequestMapping("/loan/register")
public class DLoanRegisterController extends BaseController {
    @Autowired
    private IDLoanRegisterService dLoanRegisterService;

    /**
     * 查询设备借还登记列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("loan:register:list")
    @GetMapping("/list")
    public TableDataInfo list(DLoanRegisterDTO dLoanRegister) {
        startPage();
        List<DLoanRegisterVO> list = dLoanRegisterService.selectDLoanRegisterList(dLoanRegister);
        return getDataTable(list);
    }

    /**
     * 导出设备借还登记列表
     */
    @RequiresPermissions("loan:register:export")
    @Log(title = "设备借还登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DLoanRegisterDTO dLoanRegister) {
        List<DLoanRegisterVO> list = dLoanRegisterService.selectDLoanRegisterList(dLoanRegister);
        ExcelUtil<DLoanRegisterVO> util = new ExcelUtil<>(DLoanRegisterVO.class);
        util.exportExcel(response, list, "设备借还登记数据");
    }

    /**
     * 获取设备借还登记详细信息
     */
    @RequiresPermissions("loan:register:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dLoanRegisterService.selectDLoanRegisterById(id));
    }

    /**
     * 新增设备借还登记
     */
    @RequiresPermissions("loan:register:add")
    @Log(title = "设备借还登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DLoanRegister dLoanRegister) {
        return dLoanRegisterService.insertDLoanRegister(dLoanRegister);
    }

    /**
     * 修改设备借还登记
     */
    @RequiresPermissions("loan:register:edit")
    @Log(title = "设备借还登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DLoanRegister dLoanRegister) {
        return dLoanRegisterService.updateDLoanRegister(dLoanRegister);
    }

    /**
     * 删除设备借还登记
     */
    @RequiresPermissions("loan:register:remove")
    @Log(title = "设备借还登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dLoanRegisterService.deleteDLoanRegisterByIds(ids));
    }

    /**
     * 获取登记设备列表
     * @param dLoanRegister
     * @return
     */
    @GetMapping("/getEquipmentList")
    public TableDataInfo getEquipmentList(DLoanRegisterDTO dLoanRegister) {
        startPage();
        List<DLoanRegisterEquipmentVO> list = dLoanRegisterService.getEquipmentList(dLoanRegister);
        return getDataTable(list);
    }

}
