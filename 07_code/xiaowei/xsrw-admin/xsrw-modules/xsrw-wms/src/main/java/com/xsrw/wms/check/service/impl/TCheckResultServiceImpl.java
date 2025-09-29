package com.xsrw.wms.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.service.ITMaterialService;
import com.xsrw.wms.check.domain.TCheckResult;
import com.xsrw.wms.check.domain.TTask;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.dto.CheckResultDTO;
import com.xsrw.wms.check.domain.vo.CheckResultVO;
import com.xsrw.wms.check.mapper.TCheckResultMapper;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.mapper.TTaskMapper;
import com.xsrw.wms.check.service.ITCheckResultService;
import com.xsrw.wms.check.service.ITTaskDetailService;
import com.xsrw.wms.check.service.ITTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 盘点差异Service业务层处理
 *
 * @author lyx
 * @date 2023-05-09
 */
@Service
public class TCheckResultServiceImpl extends ServiceImpl<TCheckResultMapper, TCheckResult> implements ITCheckResultService
{
    @Autowired
    private TCheckResultMapper tCheckResultMapper;

    @Autowired
    private ITMaterialService itMaterialService;

    @Autowired
    private ITTaskService itTaskService;

    @Autowired
    @Lazy
    private ITTaskDetailService itTaskDetailService;

    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;

    @Autowired
    private TTaskMapper tTaskMapper;

    /**
     * 查询盘点差异列表
     *
     * @param checkResult 盘点差异
     * @return 盘点差异
     */
    @Override
    public List<CheckResultVO> selectTCheckResultList(CheckResultDTO checkResult)
    {

        if (StringUtils.isNotEmpty(checkResult.getMaterialName())){
            // 查询物料名称
            List<TMaterial> tMaterialList = itMaterialService.list(Wrappers.lambdaQuery(TMaterial.class)
                    .like(TMaterial::getName,checkResult.getMaterialName())
                    .eq(TMaterial::getDelFlag, Constants.NO));
            List<Long> materialIds = tMaterialList.stream().map(TMaterial::getId).collect(Collectors.toList());
            if (materialIds.size() == 0){
                return new ArrayList<>();
            }
            checkResult.setMaterialIds(materialIds);
        }

        List<CheckResultVO> list = tCheckResultMapper.selectCheckResultList(checkResult);
        if(!CollectionUtils.isEmpty(list)){
            //查询物料
            list.forEach(e->{
                TMaterial material = itMaterialService.getById(e.getMaterialId());
                if(material != null){
                    // 物料编码
                    e.setMaterialCode(material.getCode());
                    // 物料名称
                    e.setMaterialName(material.getName());
                }
            });
        }
        return list;
    }

    @Override
    public List<CheckResultVO> getCheckResultList(CheckResultDTO checkResult){
        List<CheckResultVO> list = tCheckResultMapper.selectCheckResultList2(checkResult);
        return list;
    }

    /**
     * 查询盘点差异
     *
     * @param id 盘点差异主键
     * @return 盘点差异
     */
    @Override
    public TCheckResult selectTCheckResultById(Long id)
    {
        return tCheckResultMapper.selectById(id);
    }

    /**
     * 新增盘点差异
     *
     * @param tCheckResult 盘点差异
     * @return 结果
     */
    @Override
    public int insertTCheckResult(List<TCheckResult> tCheckResultList)
    {
        for (TCheckResult tCheckResult:tCheckResultList) {
            //修改任务明细实盘数量
            TTaskDetail tTaskDetail = tTaskDetailMapper.selectById(tCheckResult.getTaskDetailId());
            if (tTaskDetail != null){
                tTaskDetail.setActualCount(tCheckResult.getActualCount());
                tTaskDetail.setStatus("2");
                tTaskDetailMapper.updateById(tTaskDetail);
            }

            QueryWrapper<TCheckResult> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("task_detail_id", tCheckResult.getTaskDetailId());
            queryWrapper.eq("material_id", tCheckResult.getMaterialId());
            queryWrapper.eq("batch_code", tCheckResult.getBatchCode());
            List<TCheckResult> tCheckResultList1 = tCheckResultMapper.selectList(queryWrapper);
            //修改盘点差异，更新盘点任务状态
            TTask task = new TTask();
            task.setId(tCheckResult.getTaskId());
            task.setTaskStatus("2");
            tTaskMapper.updateById(task);

            if (tCheckResultList1.size() > 0) {
                TCheckResult checkResult = tCheckResultList1.get(0);
                tCheckResult.setCheckDifferenceCount(tCheckResult.getActualCount().subtract(tCheckResult.getPredictCount()));
                tCheckResult.setId(checkResult.getId());
                tCheckResultMapper.updateById(tCheckResult);
            } else {
                tCheckResult.setId(null);
                tCheckResult.setCheckDifferenceCount(tCheckResult.getActualCount().subtract(tCheckResult.getPredictCount()));
                tCheckResultMapper.insert(tCheckResult);
            }
        }
        return 1;
    }

    /**
     * 修改盘点差异
     *
     * @param tCheckResult 盘点差异
     * @return 结果
     */
    @Override
    public int updateTCheckResult(TCheckResult tCheckResult)
    {
        return tCheckResultMapper.updateById(tCheckResult);
    }


    /**
     * 批量删除盘点差异
     *
     * @param ids 需要删除的盘点差异主键
     * @return 结果
     */
    @Override
    public int deleteTCheckResultByIds(Long[] ids)
    {
        return tCheckResultMapper.deleteTCheckResultByIds(ids);
    }

    /**
     * 删除盘点差异信息
     *
     * @param id 盘点差异主键
     * @return 结果
     */
    @Override
    public int deleteTCheckResultById(Long id)
    {
        return tCheckResultMapper.deleteTCheckResultById(id);
    }


    /**
     * 生成盘点差异报表
     * @param taskIds
     * @return
     */
    @Override
    public AjaxResult createCheckResult(Long[] taskIds) {
        //查询所选任务信息是否都已完成
        List<TTask> tasks = itTaskService.list(Wrappers.lambdaQuery(TTask.class)
                .in(TTask::getId, taskIds)
                .eq(TTask::getDelFlag,Constants.NO));
        if (CollectionUtils.isEmpty(tasks)) {
            return AjaxResult.error("未查询到数据");
        }
        List<TTask> unFinishList = tasks.stream().filter(e -> !Constants.TASK_STATUS_APPROVED.equals(e.getTaskStatus())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(unFinishList)) {
            String code = unFinishList.get(0).getCode();
            return AjaxResult.error(code + "任务未审核，无法生成盘点报表");
        }
        //查询任务详情信息,组成数据生成盘点报表
        List<TTaskDetail> taskDetails = itTaskDetailService.list(Wrappers.lambdaQuery(TTaskDetail.class)
                .in(TTaskDetail::getTaskId,taskIds)
                .eq(TTaskDetail::getDelFlag,Constants.NO));
        if (!CollectionUtils.isEmpty(taskDetails)) {
            List<Long> taskDetailIds = new ArrayList<>();
            List<Long> detailIds = taskDetails.stream().map(TTaskDetail::getId).collect(Collectors.toList());
            List<TCheckResult> checkResultList =  tCheckResultMapper.selectList(Wrappers.lambdaQuery(TCheckResult.class)
                    .in(TCheckResult::getTaskDetailId,detailIds)
                    .eq(TCheckResult::getDelFlag,Constants.NO));
            if (!CollectionUtils.isEmpty(checkResultList)) {
                taskDetailIds = checkResultList.stream().map(TCheckResult::getTaskDetailId).collect(Collectors.toList());
            }
            for (TTaskDetail taskDetail : taskDetails) {
                //已生成过的不再重复生成
                if (!taskDetailIds.contains(taskDetail.getId())) {
                    TCheckResult checkResult = new TCheckResult();
                    checkResult.setTaskDetailId(taskDetail.getId());
                    checkResult.setCheckDifferenceCount(taskDetail.getActualCount().subtract(taskDetail.getPredictCount()));
                    checkResult.setDelFlag(Constants.DEL_FLAG_NO);
                    tCheckResultMapper.insert(checkResult);
                }
            }
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     * 根据任务id生成盘点差异报表
     * @param taskId
     * @return
     */
    @Override
    public int saveCheckResultByTaskId(Long taskId) {
        List<TCheckResult> resultSaveList = new ArrayList<>();
        QueryWrapper<TTaskDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        List<TTaskDetail> taskDetails = tTaskDetailMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(taskDetails)) {
            taskDetails.forEach(taskDetail->{
                TCheckResult checkResult = new TCheckResult();
                checkResult.setTaskDetailId(taskDetail.getId());
                checkResult.setDelFlag(Constants.DEL_FLAG_NO);
                resultSaveList.add(checkResult);
            });
        }
        if(!CollectionUtils.isEmpty(resultSaveList)){
            this.saveBatch(resultSaveList);
            return 1;
        }
        return 0;
    }

    /**
     * 盘差分析明细
     * @param checkResult
     * @return
     */
    @Override
    public List<CheckResultVO> getCheckResultDetail(CheckResultDTO checkResult){
        return  tCheckResultMapper.getCheckResultDetail(checkResult);
    }
}
