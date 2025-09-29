package com.xsrw.wms.check.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.vo.ExcelTaskVO;
import com.xsrw.wms.check.domain.vo.TaskVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 盘点任务Mapper接口
 *
 * @author lyx
 * @date 2023-05-09
 */
@Repository
public interface TTaskMapper extends BaseMapper<TTask>
{

    /**
     * 查询盘点任务列表
     *
     * @param tTask 盘点任务
     * @return 盘点任务集合
     */
    List<TaskVO> selectTTaskList(TTask tTask);


    /**
     * 删除盘点任务
     *
     * @param id 盘点任务主键
     * @return 结果
     */
    int deleteTTaskById(Long id);

    /**
     * 批量删除盘点任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTTaskByIds(Long[] ids);

    /**
     * 导出
     * @param tTask
     * @return
     */
    List<ExcelTaskVO> export(TTask tTask);

    /**
     * 查询任务
     * @param id 任务主键
     * @return 任务
     */
    TaskVO selectTaskById(Long id);

    int updateTaskStatus(@Param("ids") Long[] ids, @Param("taskStatus") String taskStatus);

    TaskVO getTaskInfoByTaskWcsId(Long taskwcsId);
}
