package com.xsrw.wms.kanban.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.mapper.TReservoirMapper;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.inout.mapper.TAdvanceDeliveryMapper;
import com.xsrw.wms.inout.mapper.TOutDeliveryMapper;
import com.xsrw.wms.inout.mapper.TTaskWcsMapper;
import com.xsrw.wms.kanban.domain.vo.*;
import com.xsrw.wms.kanban.service.ISystemKanbanService;
import com.xsrw.wms.stock.mapper.TStockMainMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 系统看板Service业务层处理
 *
 * @author tyk
 * @date 2023-06-25
 */
@Service
public class SystemKanbanServiceImpl implements ISystemKanbanService {
    @Resource
    private TAdvanceDeliveryMapper advanceDeliveryMapper;

    @Resource
    private TOutDeliveryMapper outDeliveryMapper;

    @Resource
    private WmsEquipmentMapper equipmentMapper;

    @Resource
    private TTaskWcsMapper taskWcsMapper;

    @Resource
    private TStockMainMapper stockMainMapper;

    @Resource
    private TReservoirMapper reservoirMapper;

    /**
     * 任务看板
     * @param timeRangeType 时间范围类型 1:日 2:月 3:年
     * @return
     */
    @Override
    public TaskKanbanVO taskKanban(String timeRangeType) {
        return advanceDeliveryMapper.selectTaskKanban(getBeginDate(timeRangeType));
    }

    /**
     * 设备运行
     * @param timeRangeType 时间范围类型 1:日 2:月 3:年
     * @return
     */
    @Override
    public EquipmentStatisticsVO equipmentStatistics(String timeRangeType) {
        return equipmentMapper.equipmentStatistics(getBeginDate(timeRangeType));
    }

    /**
     * 设备信息列表
     * @return
     */
    @Override
    public List<WmsEquipment> equipmentList() {
        PageHelper.startPage(1,5);
        return equipmentMapper.selectList(Wrappers.<WmsEquipment>lambdaQuery()
//                .eq(WmsEquipment::getUseStatus,2)
                .eq(WmsEquipment::getDelFlag,Constants.DEL_FLAG_NO)
                .orderByDesc(WmsEquipment::getId));
    }

    /**
     * 任务执行情况 获取近六个月的数据
     * @return
     */
    @Override
    public TaskExecutionVO taskExecutionStatistics() {
        TaskExecutionVO vo = new TaskExecutionVO();
        //开始时间
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH,-5);
        Date beginDate= DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-01 00:00:00",startDate.getTime()));

        //近六个月时间列表
        List<String> statisticsTimeList=new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH,-i);
            statisticsTimeList.add(DateUtils.parseDateToStr("yyyy-MM",instance.getTime()));
        }
        List<TaskExecutionStatisticsVO> statisticsVOList = advanceDeliveryMapper.taskExecutionStatistics(statisticsTimeList, beginDate);
        //拼接返回参数,方便前端显示
        vo.setxList(statisticsTimeList);
        List<Map<String,Object>> data =new ArrayList<>();
        vo.setDataList(data);

        Map<String,Object> inTaskMap=new HashMap<>();
        inTaskMap.put("name","入库数量");
        List<Integer> inTaskCountList=new ArrayList<>();
        inTaskMap.put("yData",inTaskCountList);
        data.add(inTaskMap);

        Map<String,Object> outTaskMap=new HashMap<>();
        outTaskMap.put("name","出库数量");
        List<Integer> outTaskCountList=new ArrayList<>();
        outTaskMap.put("yData",outTaskCountList);
        data.add(outTaskMap);

        Map<String,Object> moveTaskMap=new HashMap<>();
        moveTaskMap.put("name","移库数量");
        List<Integer> moveTaskCountList=new ArrayList<>();
        moveTaskMap.put("yData",moveTaskCountList);
        data.add(moveTaskMap);

        for (TaskExecutionStatisticsVO statisticsVO : statisticsVOList) {
            inTaskCountList.add(statisticsVO.getInTaskCount());
            outTaskCountList.add(statisticsVO.getOutTaskCount());
            moveTaskCountList.add(statisticsVO.getMoveTaskCount());
        }
        return vo;
    }

    /**
     * 任务列表
     * @return
     */
    @Override
    public List<TaskListVO> taskList() {
        PageHelper.startPage(1,10);
        return taskWcsMapper.selectWcsTaskList();
    }

    /**
     * 物料库存top
     * @return
     */
    @Override
    public List<StockListVO> stockList() {
        PageHelper.startPage(1,10);
        return stockMainMapper.selectStockList();
    }

    /**
     * 任务状态统计
     * @return
     */
    @Override
    public TaskStatusStatisticsVO taskStatusStatistics() {
        TaskStatusStatisticsVO vo=new TaskStatusStatisticsVO();
        //入库任务
        TaskStatusVO inTaskVo = advanceDeliveryMapper.selectAdvanceCount();
        //出库任务
        TaskStatusVO outTaskVo = outDeliveryMapper.selectOutCount();
        //盘点任务
        TaskStatusVO checkTaskVo = taskWcsMapper.selectStatisticsByParam(Constants.TASK_TYPE_CHECK);
        //移库任务
        TaskStatusVO moveTaskVo = taskWcsMapper.selectStatisticsByParam(Constants.TASK_TYPE_MOVE);
        //拼接返回参数,方便前端显示
        vo.setxList(Arrays.asList("未执行","执行中","已完成"));
        List<Map<String,Object>> data =new ArrayList<>();
        vo.setDataList(data);

        Map<String,Object> inTaskMap=new HashMap<>();
        inTaskMap.put("name","入库任务");
        List<Integer> inTaskCountList=new ArrayList<>();
        inTaskMap.put("yData",inTaskCountList);
        data.add(inTaskMap);

        Map<String,Object> outTaskMap=new HashMap<>();
        outTaskMap.put("name","出库任务");
        List<Integer> outTaskCountList=new ArrayList<>();
        outTaskMap.put("yData",outTaskCountList);
        data.add(outTaskMap);

        Map<String,Object> moveTaskMap=new HashMap<>();
        moveTaskMap.put("name","移库任务");
        List<Integer> moveTaskCountList=new ArrayList<>();
        moveTaskMap.put("yData",moveTaskCountList);
        data.add(moveTaskMap);

        Map<String,Object> checkTaskMap=new HashMap<>();
        checkTaskMap.put("name","盘点任务");
        List<Integer> checkTaskCountList=new ArrayList<>();
        checkTaskMap.put("yData",checkTaskCountList);
        data.add(checkTaskMap);

        for (int i = 0; i < vo.getxList().size(); i++) {
            if (i==0){
                inTaskCountList.add(inTaskVo.getNotCompleteCount());
                outTaskCountList.add(outTaskVo.getNotCompleteCount());
                moveTaskCountList.add(moveTaskVo.getNotCompleteCount());
                checkTaskCountList.add(checkTaskVo.getNotCompleteCount());
            }
            if (i==1){
                inTaskCountList.add(inTaskVo.getUnderwayCount());
                outTaskCountList.add(outTaskVo.getUnderwayCount());
                moveTaskCountList.add(moveTaskVo.getUnderwayCount());
                checkTaskCountList.add(checkTaskVo.getUnderwayCount());
            }
            if (i==2){
                inTaskCountList.add(inTaskVo.getHasCompleteCount());
                outTaskCountList.add(outTaskVo.getHasCompleteCount());
                moveTaskCountList.add(moveTaskVo.getHasCompleteCount());
                checkTaskCountList.add(checkTaskVo.getHasCompleteCount());
            }
        }
        return vo;
    }

    /**
     * 仓库使用情况统计
     * @param areaId
     * @return
     */
    @Override
    public List<WarehouseUseStatisticsVO> warehouseUseStatistics(Integer areaId) {
        List<WarehouseUseStatisticsVO> list = reservoirMapper.warehouseUseStatistics(areaId);
        list.forEach(p->{
            p.setUseRate(p.getTotalCount()==0?"0.00%":new BigDecimal(p.getUseCount()*100).divide(new BigDecimal(p.getTotalCount()),2, RoundingMode.HALF_UP)+"%");
        });
        return list;
    }

    /**
     * 根据时间范围类型获取开始时间
     * @param timeRangeType 时间范围类型 1:日 2:月 3:年
     * @return
     */
    private Date getBeginDate(String timeRangeType){
        if (StringUtils.isBlank(timeRangeType)){
            timeRangeType= Constants.KANBAN_TIME_SCOPE_DAY;
        }
        Date beginDate;
        Date now=new Date();
        switch (timeRangeType){
            case Constants.KANBAN_TIME_SCOPE_DAY:
                beginDate= DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-dd 00:00:00",now));
                break;
            case Constants.KANBAN_TIME_SCOPE_MONTH:
                beginDate= DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-MM-01 00:00:00",now));
                break;
            case Constants.KANBAN_TIME_SCOPE_YEAR:
                beginDate= DateUtils.parseDate(DateUtils.parseDateToStr("yyyy-01-01 00:00:00",now));
                break;
            default:return null;
        }
        return beginDate;
    }
}
