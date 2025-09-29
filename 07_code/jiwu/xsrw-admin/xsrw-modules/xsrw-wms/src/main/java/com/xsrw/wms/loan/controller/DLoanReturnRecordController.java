package com.xsrw.wms.loan.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.loan.domain.dto.DLoanReturnRecordDTO;
import com.xsrw.wms.loan.domain.vo.DLoanReturnExcelVO;
import com.xsrw.wms.loan.domain.vo.DLoanReturnRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;
import com.xsrw.wms.loan.service.IDLoanReturnRecordService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 设备借还借出换入记录Controller
 *
 * @author wxr
 * @date 2023-06-09
 */
@RestController
@RequestMapping("/loan/record")
public class DLoanReturnRecordController extends BaseController {
    @Autowired
    private IDLoanReturnRecordService dLoanReturnRecordService;

    /**
     * 查询设备借还借出换入记录列表
     */
    @DataScope(deptAlias = "p", userAlias = "u")
    @RequiresPermissions("loan:record:list")
    @GetMapping("/list")
    public TableDataInfo list(DLoanReturnRecordDTO dLoanReturnRecord) {
        startPage();
        List<DLoanReturnRecordVO> list = dLoanReturnRecordService.selectDLoanReturnRecordList(dLoanReturnRecord);
        return getDataTable(list);
    }

    /**
     * 导出设备借还借出换入记录列表
     */
//    @RequiresPermissions("loan:record:export")
    @Log(title = "设备借还借出换入记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DLoanReturnRecordDTO dLoanReturnRecord) {
        List<DLoanReturnRecordVO> list = dLoanReturnRecordService.selectDLoanReturnRecordList(dLoanReturnRecord);
        if("2".equals(dLoanReturnRecord.getExportType())){
            List<DLoanReturnExcelVO> dataLsit = BeanUtils.copyToList(list, DLoanReturnExcelVO.class);
            ExcelUtil<DLoanReturnExcelVO> util = new ExcelUtil<>(DLoanReturnExcelVO.class);
            util.exportExcel(response, dataLsit, "设备还入");
        }else{
            ExcelUtil<DLoanReturnRecordVO> util = new ExcelUtil<>(DLoanReturnRecordVO.class);
            util.exportExcel(response, list, "设备借出");
        }

    }

    /**
     * 获取设备借还借出换入记录详细信息
     */
//    @RequiresPermissions("loan:record:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dLoanReturnRecordService.selectDLoanReturnRecordById(id));
    }

    /**
     * 新增设备借出
     */
//    @RequiresPermissions("loan:record:add")
    @Log(title = "新增设备借出", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DLoanReturnRecord dLoanReturnRecord) {
        if(dLoanReturnRecord.getLoanRegisterId() == null || dLoanReturnRecord.getLoanCount() == null){
            return AjaxResult.error("参数不全");
        }
        return dLoanReturnRecordService.insertDLoanReturnRecord(dLoanReturnRecord);
    }

    /**
     * 设备还入
     */
//    @RequiresPermissions("loan:record:return")
    @Log(title = "设备还入", businessType = BusinessType.UPDATE)
    @PostMapping("/return")
    public AjaxResult returnRecord(@RequestBody DLoanReturnRecord dLoanReturnRecord) {
        if(dLoanReturnRecord.getId() == null || dLoanReturnRecord.getReturnCount() == null){
            return AjaxResult.error("参数不全");
        }
        return dLoanReturnRecordService.returnRecord(dLoanReturnRecord);
    }

    /**
     * 删除设备借还借出换入记录
     */
//    @RequiresPermissions("loan:record:remove")
    @Log(title = "设备借还借出换入记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dLoanReturnRecordService.deleteDLoanReturnRecordByIds(ids));
    }
}
