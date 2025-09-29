package com.xsrw.wms.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.TaskDetailDTO;
import com.xsrw.wms.check.domain.vo.TaskDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库存盘点Mapper接口
 *
 * @author lyx
 * @date 2023-05-09
 */
@Repository
public interface TTaskDetailMapper extends BaseMapper<TTaskDetail>
{

    /**
     * 查询库存盘点列表
     *
     * @param tTaskDetail 库存盘点
     * @return 库存盘点集合
     */
    List<TTaskDetail> selectTTaskDetailList(TTaskDetail tTaskDetail);


    /**
     * 删除库存盘点
     *
     * @param id 库存盘点主键
     * @return 结果
     */
    int deleteTTaskDetailById(Long id);

    /**
     * 批量删除库存盘点
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTTaskDetailByIds(Long[] ids);

    /**
     * 查询任务详情列表
     *
     * @param taskDetailDTO 任务详情
     * @return 任务详情集合
     */
    List<TaskDetailVO> selectTaskDetailList(TaskDetailDTO taskDetailDTO);
    List<TaskDetailVO> selectTaskDetailListCheck(TaskDetailDTO taskDetail);
    List<TaskDetailVO> selectTaskDetailListCheck2(TaskDetailDTO taskDetail);
    /**
     * 根据任务ids删除详情信息
     * @param ids
     * @return
     */
    int deleteDetailByTaskIds(Long[] ids);

    /**
     * 根据任务ids更新状态
     * @param taskIds 任务id
     * @param status 要更新的状态
     * @param delStatus 要去除的状态(非必填)
     * @return
     */
    Integer updateStatusByTaskIds(@Param("taskIds")Long[] taskIds, @Param("status") String status, @Param("delStatus") String delStatus);

    /**
     * 根据任务ids更新状态
     * @param ids
     * @param status
     * @return
     */
    Integer updateStatusByIds(@Param("ids")Long[] ids, @Param("status") String status, @Param("checkCount") Integer checkCount);

    /**
     * 查询任务子表是否都已完成
     * @param taskId 任务id
     * @param status 要查询的状态
     * @param delStatus 要去除的状态(非必填)
     * @return
     */
    Integer selectUnFinishedCount(@Param("taskId") Long taskId,@Param("status") String status,@Param("delStatus") String delStatus);

    List<TaskDetailVO> selectCheckTaskResult(TaskDetailDTO taskDetail);
}
