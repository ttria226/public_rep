package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.dto.TTaskWcsDTO;
import com.xsrw.wms.inout.domain.vo.TTaskInVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsOutVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;

/**
 * wcs任务Service接口
 *
 * @author wxr
 * @date 2023-05-10
 */
public interface ITTaskWcsService extends IService<TTaskWcs> {

    /**
     * 查询wcs任务列表
     *
     * @param tTaskWcs wcs任务
     * @return wcs任务集合
     */
    public List<TTaskWcsVO> selectTTaskWcsList(TTaskWcs tTaskWcs);

    /**
     * 查询wcs任务
     *
     * @param id wcs任务主键
     * @return wcs任务
     */
    public TTaskWcsVO selectTTaskWcsById(Long id);

    /**
     * 新增wcs任务
     *
     * @param tTaskWcs wcs任务
     * @return 结果
     */
    public int insertTTaskWcs(TTaskWcs tTaskWcs);

    /**
     * 修改wcs任务
     *
     * @param tTaskWcs wcs任务
     * @return 结果
     */
    public int updateTTaskWcs(TTaskWcs tTaskWcs);

    /**
     * 出库执行
     *
     * @param tTaskWcs wcs任务
     * @return 结果
     */
    public AjaxResult executeOut(TTaskWcs tTaskWcs);

    /**
     * 批量删除wcs任务
     *
     * @param ids 需要删除的wcs任务主键集合
     * @return 结果
     */
    public int deleteTTaskWcsByIds(Long[] ids);

    /**
     * 删除wcs任务信息
     *
     * @param id wcs任务主键
     * @return 结果
     */
    public int deleteTTaskWcsById(Long id);

    /**
     * 执行 --已弃用
     * @param tTaskWcs
     * @return
     */
    AjaxResult executeTask(TTaskWcs tTaskWcs);
    /**
     * 入库单执行-新方法
     * @param tTaskWcs
     * @return
     */
    AjaxResult executeTaskNew(TTaskWcsDTO tTaskWcs);


    AjaxResult executeOutTask(TTaskWcsOutVO tTaskWcs);

    /**
     * 通过载具强制执行入库
     * @param tTaskWcs
     * @return
     */
    AjaxResult executeInByTray(TTaskWcs tTaskWcs);

    /**
     * 通过载具编号获取运行任务信息
     * @param trayCode
     * @return
     */
    TTaskWcsVO getTaskInfoByTrayCode(String trayCode, String taskType);

    /**
     * 根据任务编号获取运行任务信息
     * @param taskNo
     * @return
     */
    TTaskWcsVO getTaskInfoByTaskNo(String taskNo);

    /**
     * 入库重新执行
     * @param taskWcs
     * @return
     */
    AjaxResult enforcementDelivery(TTaskWcs taskWcs);

    /**
     * 出库重新执行
     * @param taskWcs
     * @return
     */
    AjaxResult enforcementDeliveryOut(TTaskWcs taskWcs);

    /**
     * 删除任务
     * @param taskWcs
     * @param taskInIds
     */
    void deleteWcsTaskById(TTaskWcs taskWcs, List<Long> taskInIds);

    /**
     * 移库回调
     * @param tTaskWcs
     * @return
     */
    AjaxResult executeTaskMove(TTaskWcsVO tTaskWcs);

    /**
     * 查询对应的移库任务是否完成
     * @param mainTaskNo
     * @return
     */
    Long getMoveCountByMainNo(String mainTaskNo);

}
