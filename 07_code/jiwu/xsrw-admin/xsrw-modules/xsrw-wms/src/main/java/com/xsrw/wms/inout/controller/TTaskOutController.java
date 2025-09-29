package com.xsrw.wms.inout.controller;

import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.domain.vo.TTrayVO;
import com.xsrw.wms.inout.domain.TMaterialDetail;
import com.xsrw.wms.inout.domain.TTaskOut;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.inout.service.ITOutDeliveryDetailService;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.wms.inout.service.ITTaskOutService;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import com.xsrw.wms.stock.domain.TStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 出库任务详情Controller
 *
 * @author zyq
 * @date 2023-05-08
 */
@RestController
@RequestMapping("/out")
public class TTaskOutController extends BaseController
{
    @Autowired
    private ITTaskOutService tTaskOutService;

    @Autowired
    private ITTaskWcsService tTaskWcsService;
    @Autowired
    private ITOutDeliveryDetailService itOutDeliveryDetailService;
    @Autowired
    private ITOutDeliveryService tOutDeliveryService;


    /**
     * 拣选出库列表
     */
    @RequiresPermissions("inout:out:list")
    @GetMapping("/list")
    public TableDataInfo list(TTaskOut tTaskOut)
    {
        startPage();
        List<TTaskOut> list = tTaskOutService.selectTTaskOutList(tTaskOut);
        return getDataTable(list);
    }

    /**
     * 执行出库选择载具列表
     */
//    @RequiresPermissions("inout:out:list")
    @GetMapping("/traylist")
    public TableDataInfo traylist(@RequestParam String id,TStock tStock)
    {
        startPage();
        tStock.setLocationType("0");
        List<TTrayVO> list = tTaskOutService.selectTTrayList(id,tStock);
        return getDataTable(list);
    }


    /**
     * 执行出库--自动分配载具
     * @return
     */
    @GetMapping("/traylist/voluntarily")
    public AjaxResult traylistVoluntarily(Long outDeliveryId,Long materialId)
    {
        return tTaskOutService.voluntarily(outDeliveryId, materialId);
    }


    /**
     * 执行出库选择载具列表--地堆
     */
//    @RequiresPermissions("inout:out:list")
    @GetMapping("/groundPileTrayList")
    public AjaxResult groundPileTrayList(HttpServletRequest request, TStock tStock)
    {
        startPage();
        String id=request.getParameter("id");
        if(StringUtils.isEmpty(id)){
            throw  new ServiceException("参数错误！");
        }
        String materialId=request.getParameter("materialId");
        if(StringUtils.isEmpty(materialId)){
            throw  new ServiceException("参数错误，物料不可以为空！");
        }
        String batchCode=request.getParameter("batchCode");

        tStock.setLocationType("1");
        Map<String,Object> objectMap=tTaskOutService.getOutDeliveryCount(id);
        objectMap.put("data",getDataTable(tTaskOutService.selectGroundPile(materialId,batchCode)));
        return  AjaxResult.success(objectMap);
    }

    /**
     * 地堆拣货--自动分配载具
     * @return
     */
    @GetMapping("/groundPileTrayList/voluntarily")
    public AjaxResult groundPileTrayListVoluntarily(Long outDeliveryId,Long materialId)
    {
        return tTaskOutService.groundPileTrayListVoluntarily(outDeliveryId, materialId);
    }


    /**
     * 地堆出库
     * @return
     */
    @PostMapping("/groundPileOutbound")
    public  AjaxResult groundPileOutbound(@RequestBody TTaskOutVO tTaskOutVO){
        return  tTaskOutService.groundPileOutbound(tTaskOutVO);
    }
    /**
     * 导出出库任务详情列表
     */
    @RequiresPermissions("inout:out:export")
    @Log(title = "出库任务详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTaskOut tTaskOut)
    {
        List<TTaskOut> list = tTaskOutService.selectTTaskOutList(tTaskOut);
        ExcelUtil<TTaskOut> util = new ExcelUtil<TTaskOut>(TTaskOut.class);
        util.exportExcel(response, list, "出库任务详情数据");
    }

    /**
     * 获取出库任务详情详细信息
     * type 1出库执行页面  2 拣货出库页面
     */
//    @RequiresPermissions("inout:out:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id , @RequestParam("type") Integer type)
    {
        return success(tTaskOutService.selectTTaskOutById(id,type));
    }


    /**
     * 获取物料RFID列表
     * @param locationId
     * @param batchCode
     * @return
     */
    @GetMapping(value = "/materialRfidList")
    public TableDataInfo getMaterialRfidList(Long locationId, String batchCode, String rfidHead,Long materialId,Long trayId) {
        startPage();
        List<TMaterialDetail> rfidList = tTaskOutService.getMaterialRfidList(locationId, batchCode, rfidHead,materialId,trayId);
        return getDataTable(rfidList);
    }


   /**
     * 获取出库任务详情详细信息
     * type 1出库执行页面  2 拣货出库页面
     */
//    @RequiresPermissions("inout:out:query")
    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfodetail(@PathVariable("id") Long id )
    {
        return success(tTaskOutService.selectTTaskOut(id));
    }

    /**
     * 强制执行出库任务
     * @param tTaskWcsOutVO
     * @return
     */
    @RequiresPermissions("inout:task:executeOutTask")
    @Log(title = "强制执行出库任务", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOutTask")
    public AjaxResult executeOutTask(@RequestBody TTaskWcsOutVO tTaskWcsOutVO) {
        if(tTaskWcsOutVO.getId() == null){
            return AjaxResult.error("参数不全");
        }
       
        return tTaskOutService.executeOutTask(tTaskWcsOutVO);
    }


   /**
     * PDA出库
     * @param tTaskWcsOutVO
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PDA出库", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOutTaskPDA")
    public AjaxResult executeOutTaskPDA(@RequestBody TTaskWcsOutVO tTaskWcsOutVO) {
        if(tTaskWcsOutVO.getId() == null){
            return AjaxResult.error("参数不全");
        }
        return tTaskOutService.executeOutTaskPDA(tTaskWcsOutVO);
    }

   /**
     * PAD端--扫描载具
     * @param trayCode
     * @return
     */
    @RequiresPermissions("inout:task:execute")
    @Log(title = "PAD端--扫描载具", businessType = BusinessType.UPDATE)
    @GetMapping("/scanTray")
    public AjaxResult scanTray(@RequestParam String trayCode) {
        if(trayCode == null){
            return AjaxResult.error("请扫描载具标签");
        }
        return tTaskOutService.scanTrayNew(trayCode);
    }

    /**
     * 出库执行--执行出库--生成出库任务和wcs
     */
//    @RequiresPermissions("inout:out:add")
    @Log(title = "出库执行--执行出库--生成出库任务和wcs", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskOutVO tTaskOutVO)
    {
        if(tTaskOutVO.getOutDeliveryDetailId() == null){
            return AjaxResult.error("请选择要执行的单子");
        }
        if(tTaskOutVO.gettTaskOutDetailListVOS().size() == 0){
            return AjaxResult.error("请选择要物料的载具");
        }
        return tTaskOutService.insertTTaskOut(tTaskOutVO);
    }

    /**
     * 修改出库任务详情
     */
    @RequiresPermissions("inout:out:edit")
    @Log(title = "出库任务详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskOut tTaskOut)
    {
        return toAjax(tTaskOutService.updateTTaskOut(tTaskOut));
    }

    /**
     * 删除出库任务详情
     */
    @RequiresPermissions("inout:out:remove")
    @Log(title = "出库任务详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTaskOutService.deleteTTaskOutByIds(ids));
    }




    /*
    * 出库完成反馈弹窗
    * */
    @Log(title = "出库反馈详情", businessType = BusinessType.INSERT)
    @GetMapping("/outFeedback")
    public void outFeedback(@RequestParam String taskNo , Integer sort){
      tTaskOutService.outFeedback(taskNo , sort);
    }

}



