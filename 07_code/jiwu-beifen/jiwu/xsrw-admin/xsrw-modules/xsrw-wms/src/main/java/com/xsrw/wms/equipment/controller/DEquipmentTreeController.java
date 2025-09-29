package com.xsrw.wms.equipment.controller;

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
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
import com.xsrw.wms.equipment.domain.DEquipmentTree;
import com.xsrw.wms.equipment.service.IDEquipmentTreeService;
import com.xsrw.common.core.web.controller.BaseController;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.core.utils.poi.ExcelUtil;
import com.xsrw.common.core.web.page.TableDataInfo;

/**
 * 经验库分类树Controller
 *
 * @author zjj
 * @date 2023-05-11
 */
@RestController
@RequestMapping("/equipmentTree")
public class DEquipmentTreeController extends BaseController
{
    @Autowired
    private IDEquipmentTreeService dEquipmentTreeService;

    /**
     * 查询经验库分类树列表
     */
//    @RequiresPermissions("wms:equipmentTree:list")
    @GetMapping("/list")
    public TableDataInfo list(DEquipmentTree dEquipmentTree)
    {
        startPage();
        List<DEquipmentTree> list = dEquipmentTreeService.selectDEquipmentTreeList(dEquipmentTree);
        List<DEquipmentTree> collect = list.stream().filter(t -> t.getPid() == 0).map(
                m -> {
                    m.setChildList(getChildren(m, list));
                    return m;
                }
        ).collect(Collectors.toList());
//        System.out.println(JSON.toJSONString(collect));
        return getDataTable(collect);
    }


    /**
     * 递归查询子节点
     * @param root  根节点
     * @param all   所有节点
     * @return 根节点信息
     */
    public static List<DEquipmentTree> getChildren(DEquipmentTree root, List<DEquipmentTree> all) {
        List<DEquipmentTree> children = all.stream().filter(t -> {
            return Objects.equals(t.getPid(), root.getId());
        }).map(
                m -> {
                    m.setChildList(getChildren(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }





    /**
     * 导出经验库分类树列表
     */
    @RequiresPermissions("wms:equipmentTree:export")
    @Log(title = "经验库分类树", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DEquipmentTree dEquipmentTree)
    {
        List<DEquipmentTree> list = dEquipmentTreeService.selectDEquipmentTreeList(dEquipmentTree);
        ExcelUtil<DEquipmentTree> util = new ExcelUtil<DEquipmentTree>(DEquipmentTree.class);
        util.exportExcel(response, list, "经验库分类树数据");
    }

    /**
     * 获取经验库分类树详细信息
     */
    @RequiresPermissions("wms:equipmentTree:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dEquipmentTreeService.selectDEquipmentTreeById(id));
    }

    /**
     * 新增经验库分类树
     */
    @RequiresPermissions("wms:equipmentTree:add")
    @Log(title = "经验库分类树", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DEquipmentTree dEquipmentTree)
    {
        return toAjax(dEquipmentTreeService.insertDEquipmentTree(dEquipmentTree));
    }

    /**
     * 修改经验库分类树
     */
    @RequiresPermissions("wms:equipmentTree:edit")
    @Log(title = "经验库分类树", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DEquipmentTree dEquipmentTree)
    {
        return toAjax(dEquipmentTreeService.updateDEquipmentTree(dEquipmentTree));
    }

    /**
     * 删除经验库分类树
     */
    @RequiresPermissions("wms:equipmentTree:remove")
    @Log(title = "经验库分类树", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dEquipmentTreeService.deleteDEquipmentTreeByIds(ids));
    }
}
