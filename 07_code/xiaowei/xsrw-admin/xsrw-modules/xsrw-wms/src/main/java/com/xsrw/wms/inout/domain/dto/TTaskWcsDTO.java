package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TTaskWcs;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/8/1 14:23
 */
public class TTaskWcsDTO extends TTaskWcs {

    /**
     * 任务详情
     */
    private List<TTaskWcsDetailDTO> taskWcsDetailVOList;

    public List<TTaskWcsDetailDTO> getTaskWcsDetailVOList() {
        return taskWcsDetailVOList;
    }

    public void setTaskWcsDetailVOList(List<TTaskWcsDetailDTO> taskWcsDetailVOList) {
        this.taskWcsDetailVOList = taskWcsDetailVOList;
    }
}
