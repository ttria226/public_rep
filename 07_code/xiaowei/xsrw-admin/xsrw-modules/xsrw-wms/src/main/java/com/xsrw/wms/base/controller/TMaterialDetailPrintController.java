package com.xsrw.wms.base.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.base.domain.dto.TMaterialDetailPrintDTO;
import com.xsrw.wms.base.domain.vo.TMaterialDetailPrintVO;
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
import com.xsrw.wms.base.domain.TMaterialDetailPrint;
import com.xsrw.wms.base.service.ITMaterialDetailPrintService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * rfid打印记录Controller
 *
 * @author wxr
 * @date 2023-11-09
 */
@RestController
@RequestMapping("/base/material/print")
public class TMaterialDetailPrintController extends BaseController {
    @Autowired
    private ITMaterialDetailPrintService tMaterialDetailPrintService;

    /**
     * 查询rfid打印记录列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("base:print:list")
    @GetMapping("/list")
    public TableDataInfo list(TMaterialDetailPrintVO tMaterialDetailPrint) {
        startPage();
        List<TMaterialDetailPrintVO> list = tMaterialDetailPrintService.selectTMaterialDetailPrintList(tMaterialDetailPrint);
        return getDataTable(list);
    }

    /**
     * 导出rfid打印记录列表
     */
    @RequiresPermissions("base:print:export")
    @Log(title = "rfid打印记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TMaterialDetailPrintVO tMaterialDetailPrint) {
        List<TMaterialDetailPrintVO> list = tMaterialDetailPrintService.selectTMaterialDetailPrintList(tMaterialDetailPrint);
        ExcelUtil<TMaterialDetailPrintVO> util = new ExcelUtil<>(TMaterialDetailPrintVO.class);
        util.exportExcel(response, list, "rfid打印记录数据");
    }

    /**
     * 获取rfid打印记录详细信息
     */
    @RequiresPermissions("base:print:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tMaterialDetailPrintService.selectTMaterialDetailPrintById(id));
    }

    /**
     * 新增rfid打印记录
     */
    @RequiresPermissions("base:print:add")
    @Log(title = "rfid打印记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TMaterialDetailPrint tMaterialDetailPrint) {
        if(tMaterialDetailPrint.getSumCount() == null || tMaterialDetailPrint.getConvertCount() == null){
            return AjaxResult.error("参数全");
        }
        return tMaterialDetailPrintService.insertTMaterialDetailPrint(tMaterialDetailPrint);
    }

    /**
     * 修改rfid打印记录
     */
    @RequiresPermissions("base:print:edit")
    @Log(title = "rfid打印记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TMaterialDetailPrint tMaterialDetailPrint) {
        if(tMaterialDetailPrint.getSumCount() == null || tMaterialDetailPrint.getConvertCount() == null){
            return AjaxResult.error("参数全");
        }
        return tMaterialDetailPrintService.updateTMaterialDetailPrint(tMaterialDetailPrint);
    }

    /**
     * 删除rfid打印记录
     */
    @RequiresPermissions("base:print:remove")
    @Log(title = "rfid打印记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tMaterialDetailPrintService.deleteTMaterialDetailPrintByIds(ids));
    }

    /**
     * rfid打印
     * @param tMaterialDetailPrint
     * @return
     */
    @Log(title = "rfid打印记录-打印", businessType = BusinessType.INSERT)
    @PostMapping("/printInfo")
    public AjaxResult printInfo(@RequestBody TMaterialDetailPrintDTO tMaterialDetailPrint) {
        if(tMaterialDetailPrint.getId() == null){
            return AjaxResult.error("参数不全");
        }
        if(StringUtils.isEmpty(tMaterialDetailPrint.getPrintFloor())){
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailPrintService.print(tMaterialDetailPrint);
    }


    /**
     * 普通打印
     * @param tMaterialDetailPrint
     * @return
     */
    @Log(title = "普通打印记录-打印", businessType = BusinessType.INSERT)
    @PostMapping("/printInfo/erCode")
    public AjaxResult printInfoErcode(@RequestBody TMaterialDetailPrint tMaterialDetailPrint) {
        if(tMaterialDetailPrint.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailPrintService.printErCode(tMaterialDetailPrint);
    }


    /**
     * 根据入库单据id删除打印信息
     */
    @Log(title = "根据入库单据id删除打印信息", businessType = BusinessType.DELETE)
    @PostMapping("/deleteByDeliveryId")
    public AjaxResult deleteByDeliveryId(@RequestBody TMaterialDetailPrint advanceDelivery) {
        if (advanceDelivery.getAdvanceRegistrationId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tMaterialDetailPrintService.deleteByDeliveryId(advanceDelivery.getAdvanceRegistrationId());
    }
}
