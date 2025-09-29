package com.xsrw.wms.inout.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TMergeDelivery;
import com.xsrw.wms.inout.domain.vo.TMergeDeliveryVO;
import com.xsrw.wms.inout.domain.vo.TTaskOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;

/**
 * 波次计划Service接口
 *
 * @author zjj
 * @date 2023-06-25
 */
public interface ITMergeDeliveryService extends IService<TMergeDelivery>
{

    /**
     * 查询波次计划列表
     *
     * @param tMergeDelivery 波次计划
     * @return 波次计划集合
     */
    public List<TMergeDelivery> selectTMergeDeliveryList(TMergeDelivery tMergeDelivery);

    /**
     * 查询波次计划
     *
     * @param id 波次计划主键
     * @return 波次计划
     */
    public TMergeDeliveryVO selectTMergeDeliveryById(Long id);

    /**
     * 新增波次计划
     *
     * @param tMergeDelivery 波次计划
     * @return 结果
     */
    public int insertTMergeDelivery(TMergeDelivery tMergeDelivery);

    /**
     * 修改波次计划
     *
     * @param tMergeDelivery 波次计划
     * @return 结果
     */
    public int updateTMergeDelivery(TMergeDelivery tMergeDelivery);

    /**
     * 批量删除波次计划
     *
     * @param ids 需要删除的波次计划主键集合
     * @return 结果
     */
    public int deleteTMergeDeliveryByIds(Long[] ids);

    /**
     * 删除波次计划信息
     *
     * @param id 波次计划主键
     * @return 结果
     */
    public int deleteTMergeDeliveryById(Long id);

    /**
     * 创建波次
     * @param ids
     * @return
     */
    AjaxResult createMergeDelivery(Long [] ids);


    /**
     * 执行出库 生成任务及WCS相关
     * @param tTaskOut
     * @return
     */
    AjaxResult insertTTaskOut(TTaskOutVO tTaskOut);


    /**
     * 地堆出库
     * @param tTaskOut
     * @return
     */
    AjaxResult addTaskPile(TTaskOutVO tTaskOut);


    /**
     * 强制执行出库任务
     * @param tTaskWcs
     * @return
     */
    AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcs);


    /**
     * 波次分拨
     * @param id
     * @return
     */
    AjaxResult allocate(Long id);



    Map<String, Object> getOutDeliveryCount(String id);



}
