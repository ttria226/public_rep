package com.xsrw.wms.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TTray;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.dto.AddTaskDTO;
import com.xsrw.wms.check.domain.vo.ExcelTaskVO;
import com.xsrw.wms.check.domain.vo.TTaskCheckVO;
import com.xsrw.wms.check.domain.vo.TaskVO;

import java.util.List;

/**
 * 盘点任务Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITTaskService extends IService<TTask>
{

    /**
     * 查询盘点任务列表
     *
     * @param tTask 盘点任务
     * @return 盘点任务集合
     */
    List<TaskVO> selectTTaskList(TTask tTask);

    /**
     * 查询盘点任务
     *
     * @param id 盘点任务主键
     * @return 盘点任务
     */
    TaskVO selectTTaskById(Long id);

     AjaxResult check(TTaskCheckVO tTaskCheckVO);

    /**
     * 新增盘点任务
     *
     * @param tTask 盘点任务
     * @return 结果
     */
    int insertTTask(TTask tTask);

    /**
     * 修改盘点任务
     *
     * @param tTask 盘点任务
     * @return 结果
     */
    int updateTTask(TTask tTask);

    /**
     * 批量删除盘点任务
     *
     * @param ids 需要删除的盘点任务主键集合
     * @return 结果
     */
    AjaxResult deleteTTaskByIds(Long[] ids);

    /**
     * 删除盘点任务信息
     *
     * @param id 盘点任务主键
     * @return 结果
     */
    int deleteTTaskById(Long id);

    /**
     * 导出
     * @param tTask
     * @return
     */
    List<ExcelTaskVO> export(TTask tTask);

    /**
     * 批量审核
     * @param ids
     * @return
     */
    AjaxResult approveCheck(Long[] ids);

    /**
     * 激活盘点任务
     * @param ids
     * @return
     */
    AjaxResult activeCheck(Long[] ids);

    /**
     * 终止已激活的盘点任务
     *
     * @param ids
     * @return
     */
    AjaxResult stopCheck(Long[] ids);

    /**
     * 批量更新任务状态
     * @param taskIdFinishIds
     * @param status
     * @return
     */
    Boolean updateTaskStatusBatch(Long[] taskIdFinishIds, String status);

    /**
     * 共同生成任务  1：上架等等
     * @param addTaskDTO
     * @return
     */
    AjaxResult addTask(AddTaskDTO addTaskDTO) ;
    TaskVO getTaskInfoByTaskWcsId(Long taskwcsId);

    /**
     * 盘点计划任务激活，发送出库指令
     * @param tTray
     * @return
     */
    AjaxResult takeOut(TTray tTray,Long taskId);
    /**
     * 盘点计划任务，发送入库指令
     * @param tTray
     * @return
     */
    AjaxResult recycle(TTray tTray);
}
