package com.xsrw.wms.task.service;

import com.xsrw.wms.task.domain.dto.TaskQueryDTO;
import com.xsrw.wms.task.domain.vo.TaskQueryVO;

import java.util.List;

/**
 * 任务管理Service接口
 *
 * @author tyk
 * @date 2023-06-25
 */
public interface ITaskManagerService {
    /**
     * 任务查询列表
     * @param request
     * @return
     */
    List<TaskQueryVO> taskQueryList(TaskQueryDTO request);

}
