package com.xsrw.wms.task.service.impl;

import com.xsrw.common.datascope.annotation.DataScope;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.task.domain.dto.TaskQueryDTO;
import com.xsrw.wms.task.domain.vo.TaskQueryVO;
import com.xsrw.wms.task.service.ITaskManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 任务管理Service业务层处理
 *
 * @author tyk
 * @date 2023-06-25
 */
@Service
public class TaskManagerServiceImpl implements ITaskManagerService {

    @Resource
    private TTaskWcsMapper tTaskWcsMapper;

    /**
     * 任务查询列表
     * @param request
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<TaskQueryVO> taskQueryList(TaskQueryDTO request) {
        return tTaskWcsMapper.selectTaskList(request);
    }
}
