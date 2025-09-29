package com.xsrw.wms.inout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.wms.inout.domain.dto.TAdvanceRegistrationDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceRegistrationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.service.ITAdvanceRegistrationService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 入库登记Controller
 *
 * @author wxr
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/inout/registration")
public class TAdvanceRegistrationController extends BaseController {
    @Autowired
    private ITAdvanceRegistrationService tAdvanceRegistrationService;

    /**
     * 查询入库登记列表
     */
    @RequiresPermissions("inout:registration:list")
    @GetMapping("/list")
    public TableDataInfo list(TAdvanceRegistrationDTO tAdvanceRegistration) {
        startPage();
        List<TAdvanceRegistrationVO> list = tAdvanceRegistrationService.selectTAdvanceRegistrationList(tAdvanceRegistration);
        return getDataTable(list);
    }

    /**
     * 导出入库登记列表
     */
    @RequiresPermissions("inout:registration:export")
    @Log(title = "入库登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAdvanceRegistrationDTO tAdvanceRegistration) {
        List<TAdvanceRegistrationVO> list = tAdvanceRegistrationService.selectTAdvanceRegistrationList(tAdvanceRegistration);
        ExcelUtil<TAdvanceRegistrationVO> util = new ExcelUtil<>(TAdvanceRegistrationVO.class);
        util.exportExcel(response, list, "入库登记数据");
    }

    /**
     * 获取入库登记详细信息
     */
    @RequiresPermissions("inout:registration:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tAdvanceRegistrationService.selectTAdvanceRegistrationById(id));
    }

    /**
     * 删除入库登记
     */
    @RequiresPermissions("inout:registration:remove")
    @Log(title = "删除入库登记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tAdvanceRegistrationService.deleteTAdvanceRegistrationByIds(ids));
    }

    /**
     * 生成上架任务
     * @param tAdvanceRegistration
     * @return
     */
    @Log(title = "生成上架任务", businessType = BusinessType.INSERT)
    @PostMapping("/putaway")
    public AjaxResult putaway(@RequestBody TAdvanceRegistrationDTO tAdvanceRegistration) {
        if(CollectionUtils.isEmpty(tAdvanceRegistration.getTaskInList())){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceRegistrationService.putaway(tAdvanceRegistration);
    }

    /**
     * 地堆上架
     * @param tAdvanceRegistration
     * @return
     */
    @Log(title = "地堆上架", businessType = BusinessType.INSERT)
    @PostMapping("/floorStocking")
    public AjaxResult floorStocking(@RequestBody TAdvanceRegistrationDTO tAdvanceRegistration) {
        if(tAdvanceRegistration.getId() == null || tAdvanceRegistration.getLocationId() == null){
            return AjaxResult.error("参数不全");
        }
        return tAdvanceRegistrationService.floorStocking(tAdvanceRegistration);
    }

    /***
     * 通过物料ids获取对应的推荐载具类型
     */
    @GetMapping("/getTrayTypeByMaterials/{ids}")
    public AjaxResult getTrayTypeByMaterials(@PathVariable Long[] ids) {
        return tAdvanceRegistrationService.getTrayTypeByMaterials(ids);
    }

}
