package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.dispatch.domain.vo.BusinessMonitorsVO;
import com.xsrw.wms.dispatch.domain.vo.WareHouseStatusVO;
import com.xsrw.wms.inout.domain.TTaskWcs;
import com.xsrw.wms.inout.domain.vo.TTaskWcsDetailVO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsVO;
import com.xsrw.wms.kanban.domain.vo.TaskListVO;
import com.xsrw.wms.kanban.domain.vo.TaskStatusVO;
import com.xsrw.wms.task.domain.dto.TaskQueryDTO;
import com.xsrw.wms.task.domain.vo.TaskQueryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * wcs任务Mapper接口
 *
 * @author wxr
 * @date 2023-05-10
 */
@Repository
public interface TTaskWcsMapper extends BaseMapper<TTaskWcs> {

    /**
     * 查询wcs任务列表
     *
     * @param tTaskWcs wcs任务
     * @return wcs任务集合
     */
    public List<TTaskWcsVO> selectTTaskWcsList(TTaskWcs tTaskWcs);


    /**
     * 删除wcs任务
     *
     * @param id wcs任务主键
     * @return 结果
     */
    public int deleteTTaskWcsById(Long id);

    /**
     * 批量删除wcs任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTTaskWcsByIds(Long[] ids);

    List<TTaskWcsDetailVO> selectStatusWcsListByTrayId(@Param("trayId") Long trayId, @Param("taskType") String taskType);

    /**
     * 根据id批量更新状态
     * @param taskIds
     * @param taskWcs
     * @return
     */
    int updateStatusByIds(@Param("taskIds") List<Long> taskIds,@Param("taskWcs") TTaskWcs taskWcs);

    List<BusinessMonitorsVO> selectListByParam(@Param("taskNo")String taskNo,@Param("materialName") String materialName,@Param("taskStatus") String taskStatus);

    List<WareHouseStatusVO> selectListByKey(@Param("materialName")String materialName, @Param("deptId")Integer deptId);

    List<TaskQueryVO> selectTaskList(TaskQueryDTO request);

    List<TaskListVO> selectWcsTaskList();

    TaskStatusVO selectStatisticsByParam(@Param("taskType") String taskType);

    /**
     * 通过载具编号获取运行任务信息
     * @param trayCode
     * @return
     */
    TTaskWcsVO getTaskInfoByTrayCode(@Param("trayCode") String trayCode,@Param("taskType") String taskType);

    /**
     * 根据任务编号获取运行任务信息
     * @param taskNo
     * @return
     */
    TTaskWcsVO getTaskInfoByTaskNo(String taskNo);

    /**
     * 根据id更新状态
     * @param id
     * @param status
     * @return
     */
    int updateStuasById(@Param("id") Long id, @Param("status") String status);

    /**
     * 根据载具查询暂存位最后一个主任务号
     * @param palletNum
     * @param tempPallet
     * @return
     */
    TTaskWcs selectLastEndMove(@Param("trayCode") String palletNum, @Param("tempPallet") String tempPallet);

    /**
     * @description: 查询是否有入库或回库的未完成任务
     * taskType不等于入库1和回库4；且 taskStatus状态不等于3
     * @author shizhiqiang
     * @date: 2024/5/21 16:16
     * @return long
     */
    Long countNotDone();
 
    
}
