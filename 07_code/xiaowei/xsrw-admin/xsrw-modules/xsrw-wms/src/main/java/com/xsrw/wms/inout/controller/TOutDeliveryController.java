package com.xsrw.wms.inout.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.base.domain.dto.TMaterialDTO;
import com.xsrw.wms.base.domain.vo.TMaterialSelectVO;
import com.xsrw.wms.base.domain.vo.TMaterialVO;
import com.xsrw.wms.base.service.ITTrayService;
import com.xsrw.wms.inout.domain.TOutDeliveryDetail;
import com.xsrw.wms.inout.domain.TOutDeliverySamllRecord;
import com.xsrw.wms.inout.domain.dto.TMaterialDetailSDTO;
import com.xsrw.wms.inout.domain.dto.TOutboundScanningDTO;
import com.xsrw.wms.inout.domain.vo.*;
import com.xsrw.wms.inout.service.ITOutDeliverySamllRecordService;
import com.xsrw.wms.inout.service.ITTaskOutService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xsrw.common.log.annotation.Log;
import com.xsrw.common.log.enums.BusinessType;
import com.xsrw.common.security.annotation.RequiresPermissions;
import com.xsrw.wms.inout.domain.TOutDelivery;
import com.xsrw.wms.inout.service.ITOutDeliveryService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 出库单Controller
 *
 * @author zyq
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/deliveryOut")
public class TOutDeliveryController extends BaseController
{
    @Autowired
    private ITOutDeliveryService tOutDeliveryService;

    @Autowired
    private ITTrayService trayService;

    @Autowired
    private ITOutDeliverySamllRecordService tOutDeliverySamllRecordService;

    @Autowired
    private ITTaskOutService tTaskOutService;


    /**
     * 查询出库单列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("inout:deliveryOut:list")
    @GetMapping("/list")
    public TableDataInfo list(TOutDelivery tOutDelivery)
    {
        startPage();
        tOutDelivery.setDeliveryModule("1");
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }

    /**
     * 查询小件出库记录列表
     */
    @RequiresPermissions("inout:inout:list")
    @GetMapping("/smallRecordList")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "",response = TOutDeliverySamllRecordVO.class)
    })
    public TableDataInfo smallRecordList(TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        startPage();
        List<TOutDeliverySamllRecordVO> list = tOutDeliverySamllRecordService.selectTOutDeliverySamllRecordVOList(tOutDeliverySamllRecord);
        return getDataTable(list);
    }

    /**
     * 导出小件出库记录列表
     */
    @RequiresPermissions("inout:inout:export")
    @Log(title = "小件出库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/smallRecordexport")
    public void export(HttpServletResponse response, TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        List<TOutDeliverySamllRecordVO> list = tOutDeliverySamllRecordService.selectTOutDeliverySamllRecordVOList(tOutDeliverySamllRecord);
        ExcelUtil<TOutDeliverySamllRecordVO> util = new ExcelUtil<TOutDeliverySamllRecordVO>(TOutDeliverySamllRecordVO.class);
        util.exportExcel(response, list, "小件出库记录数据");
    }
    /**
     * 出库执行列表
     */
    @DataScope(deptAlias = "d", userAlias = "u")
    @RequiresPermissions("out:deliveryOut:list")
    @GetMapping("/outTasklist")
    public TableDataInfo outTasklist(TOutDeliveryDetail tOutDeliveryDetail)
    {
        startPage();
        List<TOutDeliveryDetailVO> list = tOutDeliveryService.outTasklist(tOutDeliveryDetail);
        return getDataTable(list);
    }

    /**
     * 导出出库执行列表
     */
    @RequiresPermissions("out:outTasklist:export")
    @PostMapping("/outTasklist/export")
    public void outTasklistExport(HttpServletResponse response,TOutDeliveryDetail tOutDeliveryDetail)
    {
        List<TOutDeliveryDetailVO> list = tOutDeliveryService.outTasklist(tOutDeliveryDetail);
        ExcelUtil<TOutDeliveryDetailVO> util = new ExcelUtil<>(TOutDeliveryDetailVO.class);
        util.exportExcel(response, list, "出库执行");
    }
    /**
     * 导出出库单列表
     */
    @RequiresPermissions("inout:deliveryOut:export")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TOutDelivery tOutDelivery)
    {
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        ExcelUtil<TOutDeliveryVO> util = new ExcelUtil<>(TOutDeliveryVO.class);
        util.exportExcel(response, list, "出库单数据");
    }

    /**
     * 获取出库单详细信息
     */
    @RequiresPermissions("inout:deliveryOut:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tOutDeliveryService.selectTOutDeliveryById(id));
    }

    /**
     * 新增出库计划
     */
    @RequiresPermissions("inout:deliveryOut:add")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOutDeliveryVO tOutDeliveryVO)
    {
        return tOutDeliveryService.insertTOutDelivery(tOutDeliveryVO);
    }

    /**
     * 修改出库单
     */
    @RequiresPermissions("inout:deliveryOut:edit")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOutDeliveryVO tOutDelivery)
    {
        return tOutDeliveryService.updateTOutDelivery(tOutDelivery);
    }

   /**
     * 出库计划审核
     */
    @RequiresPermissions("inout:deliveryOut:approve")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody TOutDelivery tOutDelivery)
    {
        return tOutDeliveryService.approveTOutDelivery(tOutDelivery);
    }
    /**
     * 生成出库任务
     */
    @RequiresPermissions("inout:deliveryOut:toOutTask")
    @Log(title = "出库单", businessType = BusinessType.OTHER)
    @GetMapping("/toOutTask")
    public AjaxResult toOutTask(@RequestParam Long[] ids)
    {
        return tOutDeliveryService.toOutTask(ids);
    }

    /***
     * 获取单据物料选择列表
     */
    @GetMapping("/getMaterialSelectList")
    public TableDataInfo getMaterialSelectList(TMaterialDTO tMaterial) {
        startPage();
        List<TMaterialSelectVO> list = tOutDeliveryService.getMaterialSelectList(tMaterial);
        return getDataTable(list);
    }

    /**
     * 删除出库单
     */
    @RequiresPermissions("inout:deliveryOut:remove")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return tOutDeliveryService.deleteTOutDeliveryByIds(ids);
    }
    /**
     * 删除出库执行列表任务
     */
    @RequiresPermissions("inout:deliveryOut:remove")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/removeTasks/{ids}")
    public AjaxResult removeTasks(@PathVariable Long[] ids)
    {
        return tOutDeliveryService.deleteTOutDeliveryDetailByIds(ids);
    }

    /**
     * 可视化出库
     * @param tOutDelivery
     * @return
     */
    @PostMapping("/visualOutbound")
    public  AjaxResult visualOutbound(@RequestBody TOutDeliveryVO tOutDelivery){
        return tOutDeliveryService.outBound(tOutDelivery);
    }


    /**
     * 获取出库计划列表
     * @param tOutDelivery
     * @return
     */
    @GetMapping("/selectList")
    public AjaxResult selectList(TOutDelivery tOutDelivery)
    {
//        tOutDelivery.setCompleteState(Constants.OUT_DELIVERY_COMPLETE_STATE_COMPLETED);
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return AjaxResult.success(list);
    }



    /**
     * 查询可以合并为波次的出库单
     */
    @RequiresPermissions("inout:deliveryOut:list")
    @GetMapping("/merge/list")
    public TableDataInfo mergeList(TOutDelivery tOutDelivery)
    {
        startPage();
        List<TOutDelivery> list = tOutDeliveryService.getMergeList(tOutDelivery);
        return getDataTable(list);
    }







    /**
     * 查询齐套出库列表
     */
    @RequiresPermissions("inout:deliveryOut:list")
    @GetMapping("/suit/list")
    public TableDataInfo suitlist(TOutDelivery tOutDelivery)
    {
        startPage();
        tOutDelivery.setDeliveryModule("2");
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }


    /**
     * 齐套出库新增
     * @param tOutDeliveryVO
     * @return
     */
    @PostMapping("/suit/add")
    public AjaxResult suitAdd(@RequestBody TOutDeliveryVO tOutDeliveryVO) throws ServiceException {
        return tOutDeliveryService.suitAdd(tOutDeliveryVO);
    }


    /**
     * 查询物料分配列表
     * @param materialId
     * @return
     */
    @GetMapping("/suit/materialList")
    public TableDataInfo suitMaterial(Long materialId,String type){
        startPage();
        return getDataTable(tOutDeliveryService.suitMaterial(materialId,type));
    }


    /**
     * 齐套 执行出库
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/suit/addTask")
    public AjaxResult suitAddTask(@RequestBody List<TTaskOutVO> tTaskOutVO){
        return tOutDeliveryService.suitAddTask(tTaskOutVO);
    }


    /**
     * 齐套 地堆出库
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/suit/addTaskPile")
    public AjaxResult addTaskPile(@RequestBody List<TTaskOutVO> tTaskOutVO){
        return tOutDeliveryService.addTaskPile(tTaskOutVO);
    }




    /**
     * 查询快捷出库列表
     * @param tOutDelivery
     * @return
     */
    @GetMapping("/quick/list")
    public TableDataInfo quickList(TOutDelivery tOutDelivery) {
        startPage();
        tOutDelivery.setDeliveryModule("3");
        List<TOutDeliveryVO> list = tOutDeliveryService.selectTOutDeliveryList(tOutDelivery);
        return getDataTable(list);
    }


    /**
     * 查询快捷出库物料分配列表 展示数据
     * @param outDeliveryId
     * @return
     */
    @GetMapping("/quick/traylist/voluntarily/show")
    public AjaxResult traylistVoluntarilyShow(Long outDeliveryId) {
        return tTaskOutService.voluntarilyAll(outDeliveryId);
    }

    /**
     * 查询快捷出库物料分配 组装给前端  需要提交的数据格式
     * @param outDeliveryId
     * @return
     */
    @GetMapping("/quick/traylist/voluntarily/submit")
    public AjaxResult traylistVoluntarilySubmit(Long outDeliveryId) {
        return tTaskOutService.voluntarilyAllQuick(outDeliveryId);
    }


    /**
     * 执行出库分配
     * @param tTaskOutVO
     * @return
     */
    @PostMapping("/quick/execute")
    public AjaxResult add(@RequestBody List<TTaskOutVO> tTaskOutVO) {
        for (int i = 0; i < tTaskOutVO.size(); i++) {
            TTaskOutVO taskOutVO = tTaskOutVO.get(i);

            if(taskOutVO.getOutDeliveryDetailId() == null){
                return AjaxResult.error("请选择要执行的单子");
            }
            if(taskOutVO.gettTaskOutDetailListVOS().size() == 0){
                return AjaxResult.error("请选择要物料的载具");
            }
            tTaskOutService.insertTTaskOut(taskOutVO);
        }

        return AjaxResult.success();
    }





    /**
     * 删除出库所有相关单据 -- 谨慎使用
     * @param tOutDelivery
     * @return
     */
    @PostMapping("/delAll")
    public AjaxResult delOutAll(@RequestBody TOutDelivery tOutDelivery){
        return tOutDeliveryService.delOutAll(tOutDelivery);
    }


    /**
     * 物料出库扫描列表
     */
    @GetMapping(value = "/selectChuKuList")
    public TableDataInfo selectChuKuList(TOutboundScanningDTO tOutboundScanningDTO){
        startPage();
        List<TOutboundScanningVO> list = tOutDeliveryService.selectChuKuList(tOutboundScanningDTO);
        return getDataTable(list);
    }



}
