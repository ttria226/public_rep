package com.xsrw.wms.kanban.service;

import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.kanban.domain.vo.*;

import java.util.List;

/**
 * 系统看板Service接口
 *
 * @author tyk
 * @date 2023-06-25
 */
public interface ISystemKanbanService {
    /**
     * 任务看板
     * @param timeRangeType 时间范围类型 1:日 2:月 3:年
     * @return
     */
    TaskKanbanVO taskKanban(String timeRangeType);

    /**
     * 设备运行
     * @param timeRangeType 时间范围类型 1:日 2:月 3:年
     * @return
     */
    EquipmentStatisticsVO equipmentStatistics(String timeRangeType);

    /**
     * 设备信息列表
     * @return
     */
    List<WmsEquipment> equipmentList();

    /**
     * 任务执行情况
     * @return
     */
    TaskExecutionVO taskExecutionStatistics();

    /**
     * 任务列表
     * @return
     */
    List<TaskListVO> taskList();

    /**
     * 物料库存top
     * @return
     */
    List<StockListVO> stockList();

    /**
     * 任务状态统计
     * @return
     */
    TaskStatusStatisticsVO taskStatusStatistics();

    /**
     * 仓库使用情况统计
     * @param areaId
     * @return
     */
    List<WarehouseUseStatisticsVO> warehouseUseStatistics(Integer areaId);
}
