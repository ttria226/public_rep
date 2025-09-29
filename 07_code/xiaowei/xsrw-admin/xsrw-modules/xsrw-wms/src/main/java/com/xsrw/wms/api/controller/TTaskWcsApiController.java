package com.xsrw.wms.api.controller;

import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.web.page.TableDataInfo;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.inout.service.ITTaskWcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangxueru
 * @description pda wcs接口
 * @date 2023/6/28 18:10
 */
@RestController
@RequestMapping("/api/task/wcs")
public class TTaskWcsApiController extends BaseController {

    @Autowired
    private ITTaskWcsService tTaskWcsService;

    /**
     * 查询wcs任务列表-上架任务
     */
    @GetMapping("/getTaskList")
    public TableDataInfo getTaskList(TTaskWcs tTaskWcs) {
        startPage();
        List<TTaskWcsVO> list = tTaskWcsService.selectTTaskWcsList(tTaskWcs);
        return getDataTable(list);
    }

    /**
     * 根据任务id查询详情-上架任务
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/getDetailByTaskId/{id}")
    public AjaxResult getDetailByTaskId(@PathVariable("id") Long id) {
        return success(tTaskWcsService.selectTTaskWcsById(id));
    }
}
