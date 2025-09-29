package com.xsrw.wms.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.CheckDeliveryDTO;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.TaskDetailVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存盘点Service接口
 *
 * @author lyx
 * @date 2023-05-09
 */
public interface ITTaskDetailService extends IService<TTaskDetail>
{

    /**
     * 查询库存盘点列表
     *
     * @param taskDetailDTO 库存盘点
     * @return 库存盘点集合
     */
    List<TaskDetailVO> selectTTaskDetailList(TaskDetailDTO taskDetailDTO);

    List<TaskDetailVO> selectCheckTaskResult(TaskDetailDTO taskDetail);

    /**
     * 查询库存盘点
     *
     * @param id 库存盘点主键
     * @return 库存盘点
     */
    AjaxResult selectTTaskDetailById(Long id);

    /**
     * 新增库存盘点
     *
     * @param tTaskDetail 库存盘点
     * @return 结果
     */
    int insertTTaskDetail(TTaskDetail tTaskDetail);

    /**
     * 修改库存盘点
     *
     * @param tTaskDetail 库存盘点
     * @return 结果
     */
    int updateTTaskDetail(TTaskDetail tTaskDetail);

    /**
     * 批量删除库存盘点
     *
     * @param ids 需要删除的库存盘点主键集合
     * @return 结果
     */
    int deleteTTaskDetailByIds(Long[] ids);

    /**
     * 删除库存盘点信息
     *
     * @param id 库存盘点主键
     * @return 结果
     */
    int deleteTTaskDetailById(Long id);

    /**
     * 批量 新增、修改 出库详情
     * @param list
     * @return
     */
    AjaxResult batchAdd(List<TTaskDetail> list);

    /**
     * 执行盘点
     * @param id
     * @param checkNum
     * @return
     */
    AjaxResult performCheck(Long id, BigDecimal checkNum);

    /**
     * 审核盘点子表
     * @param ids
     * @param status
     * @return
     */
    AjaxResult approveCheck(Long[] ids,String status);

    /**
     * 审核区域盘点任务
     * @param taskDetailId
     * @param ids
     * @param status
     * @return
     */
    AjaxResult  approveAreaCheck(Long taskDetailId,List<Long> ids,String status);
    AjaxResult getDropdownData(Long taskId);


    /**
     * 待盘点物料列表
     * @param taskId
     * @param trayCode
     * @param checkType
     * @return
     */
    AjaxResult trayDetail(Long taskId,String trayCode,String checkType);


    /**
     * 提交盘点数据
     * @param checkDeliveryDTO
     * @return
     */
    AjaxResult checkSubmit(CheckDeliveryDTO checkDeliveryDTO);

    AjaxResult checkTaskDetail (Long[] ids, String status);
}
