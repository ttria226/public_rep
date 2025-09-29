package com.xsrw.wms.inout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TAdvanceDelivery;
import com.xsrw.wms.inout.domain.dto.TAdvanceDeliveryDTO;
import com.xsrw.wms.inout.domain.vo.TAdvanceDeliveryVO;
import com.xsrw.wms.kanban.domain.vo.TaskExecutionStatisticsVO;
import com.xsrw.wms.kanban.domain.vo.TaskKanbanVO;
import com.xsrw.wms.kanban.domain.vo.TaskStatusVO;
import com.xsrw.wms.report.domain.vo.DeliveryTimeStatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 入库单Mapper接口
 *
 * @author wxr
 * @date 2023-05-08
 */
@Repository
public interface TAdvanceDeliveryMapper extends BaseMapper<TAdvanceDelivery> {

    /**
     * 查询入库单列表
     *
     * @param tAdvanceDelivery 入库单
     * @return 入库单集合
     */
    public List<TAdvanceDeliveryVO> selectTAdvanceDeliveryList(TAdvanceDeliveryDTO tAdvanceDelivery);


    /**
     * 删除入库单
     *
     * @param id 入库单主键
     * @return 结果
     */
    public int deleteTAdvanceDeliveryById(Long id);

    /**
     * 批量删除入库单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAdvanceDeliveryByIds(Long[] ids);

    Integer selectDelStatusCountByIds(Long[] ids);

    /**
     * 根据ids批量更新状态值
     * @param status
     * @param ids
     * @return
     */
    int updateStatusByIds(@Param("status") String status, @Param("ids") List<Long> ids);

    /**
     * 任务看板
     * @param beginDate
     * @return
     */
    TaskKanbanVO selectTaskKanban(@Param("beginDate") Date beginDate);

    /**
     * 任务执行情况
     * @param statisticsTimeList
     * @param beginDate
     * @return
     */
    List<TaskExecutionStatisticsVO> taskExecutionStatistics(@Param("statisticsTimeList") List<String> statisticsTimeList, @Param("beginDate") Date beginDate);

    /**
     * 查询入库任务数量
     * @return
     */
    TaskStatusVO selectAdvanceCount();

    List<DeliveryTimeStatisticsVO> deliveryTimeStatistics(@Param("code") String code);
}
