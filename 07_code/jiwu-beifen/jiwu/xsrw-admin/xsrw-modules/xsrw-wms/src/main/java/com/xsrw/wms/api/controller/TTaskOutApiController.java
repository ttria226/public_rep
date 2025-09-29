package com.xsrw.wms.api.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutRecheck;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.inout.service.ITOutRecheckService;
import com.xsrw.wms.inout.service.ITTaskOutService;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;
import com.xsrw.wms.loan.domain.dto.DLoanRegisterDTO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterEquipmentVO;
import com.xsrw.wms.loan.service.IDLoanRegisterService;
import com.xsrw.wms.loan.service.IDLoanReturnRecordService;
import com.xsrw.wms.stock.domain.TStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库任务详情Controller
 *
 * @author zyq
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/outApi")
public class TTaskOutApiController extends BaseController {
    @Autowired
    private ITTaskOutService tTaskOutService;

    @Autowired
    private ITTaskWcsService tTaskWcsService;

    @Autowired
    private ITOutDeliveryService tOutDeliveryService;
    @Autowired
    private ITOutRecheckService tOutRecheckService;
    @Autowired
    private IDLoanRegisterService dLoanRegisterService;
    @Autowired
    private IDLoanReturnRecordService dLoanReturnRecordService;
    @Autowired
    private ITTrayService trayService;


    /**
     * pda出库计划列表
     */
    @RequiresPermissions("out:deliveryOut:list")
    @GetMapping("/outDeliveryList")
    public TableDataInfo outDeliveryList(TOutDelivery tOutDelivery) {
        startPage();
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }

    /**
     * pda出库计划详情
     */
    @RequiresPermissions("inout:deliveryOut:query")
    @GetMapping(value = "/outDelivery/{id}")
    public AjaxResult outDelivery(@PathVariable("id") Long id) {
        return success(tOutDeliveryService.selectTOutDeliveryById(id));
    }


    /**
     * 出库计划审核
     */
    @RequiresPermissions("inout:deliveryOut:approve")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PostMapping("/outDelivery/approve")
    public AjaxResult outDeliveryApprove(@RequestBody TOutDelivery tOutDelivery) {
        return tOutDeliveryService.approveTOutDelivery(tOutDelivery);
    }

    /**
     * PDA执行出库选择载具列表
     */
    @RequiresPermissions("inout:out:pdaTraylist")
    @GetMapping("/pdaTraylist")
    public TableDataInfo pdaTraylist(@RequestParam String id, TStock tStock) {
        startPage();
        List<TTrayVO> list = tTaskOutService.selectTTrayList(id, tStock);
        return getDataTable(list);
    }

    /**
     * pda出库执行列表
     */
    @RequiresPermissions("out:deliveryOut:pdaOutTasklist")
    @GetMapping("/pdaOutTasklist")
    public TableDataInfo pdaOutTasklist(TOutDeliveryDetail tOutDeliveryDetail) {
        startPage();
        // pda只展示可分配的数据
        tOutDeliveryDetail.setPadFlag("1");
        List<TOutDeliveryDetailVO> list = tOutDeliveryService.outTasklist(tOutDeliveryDetail);
        return getDataTable(list);
    }

    /**
     * pda出库拣选列表
     * @param tTaskWcs
     * @return
     */
    @GetMapping("/pdaTaskChooselist")
    public TableDataInfo pdaTaskChooselist(TTaskWcs tTaskWcs) {
        startPage();
        if(StringUtils.isBlank(tTaskWcs.getTaskType())){
            return new TableDataInfo();
        }
        List<TTaskWcsVO> list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        return getDataTable(list);
    }


    /**
     * pda出库执行--执行出库--生成出库任务和wcs
     */
    @RequiresPermissions("inout:out:add")
    @Log(title = "出库任务详情", businessType = BusinessType.INSERT)
    @PostMapping("/padadd")
    public AjaxResult padadd(@RequestBody TTaskOutVO tTaskOutVO) {
        if (tTaskOutVO.getOutDeliveryDetailId() == null) {
            return AjaxResult.error("请选择要执行的单子");
        }
        if (tTaskOutVO.gettTaskOutDetailListVOS().size() == 0) {
            return AjaxResult.error("请选择要物料的载具");
        }
        return tTaskOutService.insertTTaskOut(tTaskOutVO);
    }

    /**
     * PDA出库
     *
     * @param tTaskWcsOutVO
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PDA出库", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOutTaskPDA")
    public AjaxResult executeOutTaskPDA(@RequestBody TTaskWcsOutVO tTaskWcsOutVO) {
        if (tTaskWcsOutVO.getMaterialId() == null || tTaskWcsOutVO.getTrayCode() == null) {
            return AjaxResult.error("参数不全");
        }
        return tTaskOutService.executeOutTaskPDA(tTaskWcsOutVO);
    }

    /**
     * PAD端--扫描载具
     *
     * @param trayCode
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PAD端--小件领取", businessType = BusinessType.UPDATE)
    @GetMapping("/smallOut")
    public AjaxResult smallOut(@RequestParam String trayCode, @RequestParam String rfid) {
        if (trayCode == null) {
            return AjaxResult.error("请扫描载具标签");
        }
        if (rfid == null) {
            return AjaxResult.error("请扫描rfid");
        }
        return tTaskOutService.scanTray(trayCode, rfid);
    }

    /**
     * PAD端--扫描载具
     *
     * @param trayCode
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PAD端--扫描载具", businessType = BusinessType.UPDATE)
    @GetMapping("/scanTray")
    public AjaxResult scanTray(@RequestParam String trayCode) {
        if (trayCode == null) {
            return AjaxResult.error("请扫描载具标签");
        }
        return tTaskOutService.scanTrayNew(trayCode);
    }
    /**
     * PAD端--小件领取
     * @param trayCode
     * @return
     */
   /* @RequiresPermissions("inout:task:execute")
    @Log(title = "PAD端--出库--小件领取", businessType = BusinessType.UPDATE)
    @GetMapping("/smallOut")
    public AjaxResult smallOut(@RequestParam String trayCode, @RequestParam String rfid ) {
        if(trayCode == null){
            return AjaxResult.error("请扫描载具标签");
        }
         if(rfid == null){
            return AjaxResult.error("请扫描载具标签");
        }
        return tTaskOutService.scanTray(trayCode);
    }*/

    /**
     * PAD端--载具回库
     *
     * @param trayCode
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PAD端--载具回库", businessType = BusinessType.UPDATE)
    @GetMapping("/trayBack")
    public AjaxResult trayBack(@RequestParam String trayCode) {
        if (trayCode == null) {
            return AjaxResult.error("请扫描载具标签");
        }
        TTray tTray = new TTray();
        tTray.setCode(trayCode);
        return trayService.recycle(tTray);
    }

    /**
     * 执行出库选择载具列表
     */
    @GetMapping("/traylist")
    public TableDataInfo traylist(@RequestParam String id, TStock tStock) {
        startPage();
        List<TTrayVO> list = tTaskOutService.selectTTrayList(id, tStock);
        return getDataTable(list);
    }

    @PostMapping("/executeOutDelivery")
    public AjaxResult add(@RequestBody TTaskOutVO tTaskOutVO) {
        if (tTaskOutVO.getOutDeliveryDetailId() == null) {
            return AjaxResult.error("请选择要执行的单子");
        }
        if (tTaskOutVO.gettTaskOutDetailListVOS().size() == 0) {
            return AjaxResult.error("请选择要物料的载具");
        }
        return tTaskOutService.insertTTaskOut(tTaskOutVO);
    }

    /**
     * 地堆出库
     *
     * @return
     */
    @PostMapping("/groundPileOutbound")
    public AjaxResult groundPileOutbound(@RequestBody TTaskOutVO tTaskOutVO) {
        return tTaskOutService.groundPileOutbound(tTaskOutVO);
    }

    /**
     * 复核打包
     */
    @GetMapping("/recheck/list")
    public TableDataInfo getRecheckList(TOutRecheck tOutRecheck) {
        startPage();
        List<TOutRecheckVO> list = tOutRecheckService.selectTOutRecheckList(tOutRecheck);
        return getDataTable(list);
    }

    /**
     * 获取出库复核单详细信息
     */
    @GetMapping(value = "/recheck/{id}")
    public AjaxResult getRecheckInfo(@PathVariable("id") Long id) {
        return success(tOutRecheckService.selectTOutRecheckById(id));
    }

    /**
     * 出库复核单状态更新
     *
     * @param tOutRecheck
     * @return
     */
    @Log(title = "出库复核单状态更新", businessType = BusinessType.INSERT)
    @PostMapping("/recheck/updateStatus")
    public AjaxResult updateStatus(@RequestBody TOutRecheck tOutRecheck) {
        if (tOutRecheck.getId() == null) {
            return AjaxResult.error("参数不全");
        }
        tOutRecheck.setStatus(Constants.YES);
        return toAjax(tOutRecheckService.updateById(tOutRecheck));
    }

    /**
     * 查询设备借还登记列表
     */
    /**
     * 获取登记设备列表
     *
     * @param dLoanRegister
     * @return
     */
    @GetMapping("/loan/getEquipmentList")
    public TableDataInfo getEquipmentList(DLoanRegisterDTO dLoanRegister) {
        startPage();
        List<DLoanRegisterEquipmentVO> list = dLoanRegisterService.getEquipmentList(dLoanRegister);
        return getDataTable(list);
    }

    /**
     * pda新增设备借出
     */
    @Log(title = "pda新增设备借出", businessType = BusinessType.INSERT)
    @PostMapping("/loan/addReturn")
    public AjaxResult addReturn(@RequestBody DLoanReturnRecord dLoanReturnRecord) {
        if (dLoanReturnRecord.getLoanRegisterId() == null || dLoanReturnRecord.getLoanCount() == null) {
            return AjaxResult.error("参数不全");
        }
        if (StringUtils.isEmpty(dLoanReturnRecord.getLoanBy())) {
            dLoanReturnRecord.setLoanBy(SecurityUtils.getUsername());
        }
        if (dLoanReturnRecord.getLoanTime() == null) {
            dLoanReturnRecord.setLoanTime(DateUtils.getNowDate());
        }
        return dLoanReturnRecordService.insertDLoanReturnRecord(dLoanReturnRecord);
    }

    /**
     * 通过载具拣出出库
     * @param tTaskOutVO
     * @return
     */
    @Log(title = "通过载具拣出出库", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOutByTray")
    public AjaxResult executeOutByTray(@RequestBody TTaskOutVO tTaskOutVO) {
        if (tTaskOutVO.getTrayId() == null) {
            return AjaxResult.error("参数不全");
        }
        if (CollectionUtils.isEmpty(tTaskOutVO.gettTaskOutDetailListVOS())) {
            return AjaxResult.error("请选择要出库的物料");
        }
        return tTaskOutService.executeOutByTray(tTaskOutVO);
    }

    /**
     * 出库单-回流
     * @param tOutDelivery
     * @return
     */
    @PostMapping("/refluxOutDelivery")
    public AjaxResult refluxOutDelivery(@RequestBody TOutDelivery tOutDelivery) {
        if(tOutDelivery.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tTaskOutService.refluxOutDelivery(tOutDelivery);
    }

}
