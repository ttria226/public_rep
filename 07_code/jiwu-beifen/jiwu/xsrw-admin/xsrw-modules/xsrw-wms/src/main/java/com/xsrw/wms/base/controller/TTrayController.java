package com.xsrw.wms.base.controller;

import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.api.domain.vo.TTrayApiVO;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TTrayDTO;
import com.xsrw.wms.base.domain.vo.ExcelTrayVO;
import com.xsrw.wms.base.service.ITTrayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 载具管理Controller
 *
 * @author lyx
 * @date 2023-05-05
 */
@RestController
@RequestMapping("/tray")
public class TTrayController extends BaseController {
    @Autowired
    private ITTrayService tTrayService;

    /**
     * 查询载具管理列表
     */
//    @RequiresPermissions("wms:tray:list")
    @GetMapping("/list")
    public TableDataInfo list(TTrayDTO tTray) {
        startPage();
        List<TTrayApiVO> list = tTrayService.selectTTrayList(tTray);
        return getDataTable(list);
    }

    /**
     * 导出载具管理列表
     */
    @RequiresPermissions("wms:tray:export")
    @Log(title = "载具管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTrayDTO tTray) {
        List<TTrayApiVO> list = tTrayService.selectTTrayList(tTray);
        ExcelUtil<TTrayApiVO> util = new ExcelUtil<>(TTrayApiVO.class);
        util.exportExcel(response, list, "载具管理数据");
    }

    /**
     * 导入载具管理 //todo 权限待添加
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequiresPermissions("wms:Tray:export")
    @Log(title = "载具管理", businessType = BusinessType.EXPORT)
    @PostMapping("/importData")
    public AjaxResult importUnit(MultipartFile file) throws Exception {
        return tTrayService.importUnit(file);
    }

    /**
     * 下载导入模板信息 //todo 权限待添加
     */
    @RequiresPermissions("wms:Tray:export")
    @Log(title = "载具管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export/demo")
    public void exportTrayDemo(HttpServletResponse response) {
        ExcelUtil<ExcelTrayVO> util = new ExcelUtil<ExcelTrayVO>(ExcelTrayVO.class);
        util.exportExcel(response, new ArrayList<>(), "载具管理导入模板");
    }

    /**
     * 获取载具管理详细信息
     */
    @RequiresPermissions("wms:tray:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tTrayService.selectTTrayById(id));
    }

    /**
     * 新增载具管理
     */
    @RequiresPermissions("wms:tray:add")
    @Log(title = "载具管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTray tTray) {
        return tTrayService.insertTTray(tTray);
    }

    /**
     * 修改载具管理
     */
    @RequiresPermissions("wms:tray:edit")
    @Log(title = "载具管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTray tTray) {
        return tTrayService.updateTTray(tTray);
    }

    /**
     * 删除载具管理
     */
    @RequiresPermissions("wms:tray:remove")
    @Log(title = "载具管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tTrayService.deleteTTrayByIds(ids));
    }

    /**
     * 通过标签ID查询托盘信息
     *
     * @param tTray
     * @return
     */
//    @RequiresPermissions("wms:Tray:query")
    @GetMapping("/listBylabelTemplateId")
    public TableDataInfo selectListBylabelTemplateId(TTray tTray) {
        startPage();
        List<TTray> trays = tTrayService.selectListBylabelTemplateId(tTray);
        return getDataTable(trays);
    }

    /**
     * 选择列表
     *
     * @param tTray
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TTrayDTO tTray) {
        tTray.setNotStatus(Constants.TRAY_STATUS_FULL);
        List<TTrayApiVO> trays = tTrayService.selectTTrayList(tTray);
        return AjaxResult.success(trays);
    }
    /**
     * 选择列表
     *
     * @param tTray
     * @return
     */
    @GetMapping("/selectPageList")
    public TableDataInfo selectPageList(TTrayDTO tTray) {
        startPage();
        tTray.setNotStatus(Constants.TRAY_STATUS_FULL);
        List<TTrayApiVO> trays = tTrayService.selectTTrayList(tTray);
        return getDataTable(trays);
    }

    /**
     * 入库-选择上架载具列表
     *
     * @param tTray
     * @return
     */
    @GetMapping("/selectPutWayList")
    public TableDataInfo selectPutWayList(TTrayDTO tTray) {
        startPage();
        String code = tTray.getCode();
        if(StringUtils.isNotEmpty(code)){
            String[] split = code.split(",");
            if(split.length > 1){
                tTray.setCodes(Arrays.asList(split));
                tTray.setCode(null);
            }
        }
        List<TTrayApiVO> trays = tTrayService.selectPutWayList(tTray);
        return getDataTable(trays);
    }
    /**
     * 托盘出库
     * @param tTray
     * @return
     */
    @PostMapping("/takeOut")
    public AjaxResult takeOut(@RequestBody TTray tTray) {
        if (tTray.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.takeOut(tTray);
    }


    /**
     * 托盘出库 -- 盘点出库
     * @param tTray
     * @return
     */
    @PostMapping("/takeOut/check")
    public AjaxResult takeOutCheck(@RequestBody TTrayDTO tTray) {
        if (tTray.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.takeOutCheck(tTray);
    }

    /**
     * 托盘回库
     * @param tTray
     * @return
     */
    @PostMapping("/recycle")
    public AjaxResult recycle(@RequestBody TTray tTray) {
        if (tTray.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.recycle(tTray);
    }



    /**
     * 出库任务--托盘强制回库
     * @param tTray
     * @return
     */
    @PostMapping("/recycleOut")
    public AjaxResult recycleOut(@RequestBody TTray tTray) {
        if (tTray.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.recycleOut(tTray);
    }


    /**
     * 批量打印载具
     *
     * @param trayDTO
     * @return
     */
    @PostMapping("/batchPrint")
    public AjaxResult batchCreate(@RequestBody TTrayDTO trayDTO) {
        if(StringUtils.isEmpty(trayDTO.getTrayCategory())){
            return AjaxResult.error("载具类型不可为空");
        }
        if(trayDTO.getCount() == null || trayDTO.getCount() <= 0){
            return AjaxResult.error("生成数量需大于0");
        }
        return tTrayService.batchCreate(trayDTO);
    }

    /**
     * 获取批量打印列表
     * @param trayDTO
     * @return
     */
    @PostMapping("/batchPrintList")
    public AjaxResult batchPrintList(@RequestBody TTrayDTO trayDTO) {
        if(StringUtils.isEmpty(trayDTO.getTrayCategory())){
            return AjaxResult.error("载具类型不可为空");
        }
        if(StringUtils.isEmpty(trayDTO.getStartNo()) ||StringUtils.isEmpty(trayDTO.getEndNo())){
            return AjaxResult.error("编号不可为空");
        }
        trayDTO.setStartNo(trayDTO.getStartNo().substring(1,trayDTO.getStartNo().length()));
        trayDTO.setEndNo(trayDTO.getEndNo().substring(1,trayDTO.getEndNo().length()));
        List<String> batchList = tTrayService.getBatchList(trayDTO);
        return AjaxResult.success(batchList);
    }

    /**
     * 载具表解除绑定库位
     * @param tTray
     * @return
     */
    @PostMapping("/relieveLocation")
    public AjaxResult relieveLocation(@RequestBody TTray tTray) {
        if (tTray.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTrayService.relieveLocation(tTray.getId());
    }

}
