package com.xsrw.wms.webservice.controller;

import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.wms.webservice.domain.TErpInventory;
import com.xsrw.wms.webservice.domain.TTaskErpRecord;
import com.xsrw.wms.webservice.domain.dto.erp2wms.InOutRequest;
import com.xsrw.wms.webservice.service.ErpService;
import com.xsrw.wms.webservice.service.ITaskErpRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 863Soft
 * @date 2024/8/27
 * @description <p>备注：</p>
 */
@RestController
@RequestMapping("/webservice/erp/record")
public class TaskErpRecordController extends BaseController {

    @Autowired
    private ITaskErpRecordService taskErpRecordService;
    @Autowired
    private ErpService erpService;

    /**
     * 查询列表
     *
     * @param taskErpRecord
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(TTaskErpRecord taskErpRecord) {
        startPage();
        List<TTaskErpRecord> list = taskErpRecordService.selectList(taskErpRecord);
        return getDataTable(list);
    }

    /**
     * 重新发送
     *
     * @param taskErpRecord
     * @return
     */
    @PostMapping("/sendRecord")
    public AjaxResult sendRecord(@RequestBody TTaskErpRecord taskErpRecord) {
        return taskErpRecordService.sendRecord(taskErpRecord.getId());
    }

    @PostMapping("/test")
    public AjaxResult test(@RequestBody TErpInventory erpInventory) {
        return erpService.test(erpInventory.getIvnum());
    }
    
    @PostMapping("/saveByErp")
    public AjaxResult saveByErp(@RequestBody InOutRequest inOutRequest) {
    	List<InOutRequest> inOutRequestList = new ArrayList<InOutRequest>();
    	inOutRequestList.add(inOutRequest);
        return AjaxResult.success(erpService.inOut(inOutRequestList));
    }

}
