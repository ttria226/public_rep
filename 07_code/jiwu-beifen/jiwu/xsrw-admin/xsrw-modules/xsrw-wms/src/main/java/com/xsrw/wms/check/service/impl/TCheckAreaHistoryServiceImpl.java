package com.xsrw.wms.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TMaterial;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.check.domain.TCheckAreaHistory;
import com.xsrw.wms.check.domain.TTaskDetail;
import com.xsrw.wms.check.domain.vo.CheckAreaHistoryVO;
import com.xsrw.wms.check.mapper.TCheckAreaHistoryMapper;
import com.xsrw.wms.check.mapper.TTaskDetailMapper;
import com.xsrw.wms.check.service.ITCheckAreaHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 平库盘点提交历史Service业务层处理
 *
 * @author lyx
 * @date 2023-05-11
 */
@Service
public class TCheckAreaHistoryServiceImpl extends ServiceImpl<TCheckAreaHistoryMapper, TCheckAreaHistory> implements ITCheckAreaHistoryService
{
    @Autowired
    private TCheckAreaHistoryMapper tCheckAreaHistoryMapper;

    @Autowired
    private TTaskDetailMapper tTaskDetailMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;


    /**
     * 查询平库盘点提交历史列表
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 平库盘点提交历史
     */
    @Override
    public List<TCheckAreaHistory> selectTCheckAreaHistoryList(TCheckAreaHistory tCheckAreaHistory)
    {
        return tCheckAreaHistoryMapper.selectTCheckAreaHistoryList(tCheckAreaHistory);
    }

    /**
     * 查询平库盘点提交历史
     *
     * @param id 平库盘点提交历史主键
     * @return 平库盘点提交历史
     */
    @Override
    public TCheckAreaHistory selectTCheckAreaHistoryById(Long id)
    {
        return tCheckAreaHistoryMapper.selectById(id);
    }

    /**
     * 新增平库盘点提交历史
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 结果
     */
    @Override
    public int insertTCheckAreaHistory(TCheckAreaHistory tCheckAreaHistory)
    {
        return tCheckAreaHistoryMapper.insert(tCheckAreaHistory);
    }

    /**
     * 修改平库盘点提交历史
     *
     * @param tCheckAreaHistory 平库盘点提交历史
     * @return 结果
     */
    @Override
    public int updateTCheckAreaHistory(TCheckAreaHistory tCheckAreaHistory)
    {
        return tCheckAreaHistoryMapper.updateById(tCheckAreaHistory);
    }


    /**
     * 批量删除平库盘点提交历史
     *
     * @param ids 需要删除的平库盘点提交历史主键
     * @return 结果
     */
    @Override
    public int deleteTCheckAreaHistoryByIds(Long[] ids)
    {
        return tCheckAreaHistoryMapper.deleteTCheckAreaHistoryByIds(ids);
    }

    /**
     * 删除平库盘点提交历史信息
     *
     * @param id 平库盘点提交历史主键
     * @return 结果
     */
    @Override
    public int deleteTCheckAreaHistoryById(Long id)
    {
        return tCheckAreaHistoryMapper.deleteTCheckAreaHistoryById(id);
    }

    /**
     * 查询平库盘点记录
     * @param taskDetailId
     * @return
     */
    @Override
    public List<CheckAreaHistoryVO> getCheckAreaHistory(Long taskDetailId, Long taskId, String materialCode, String trayCode, String status) {
        List<CheckAreaHistoryVO> result = new ArrayList<>();

        QueryWrapper<TCheckAreaHistory> queryWrapper = new QueryWrapper<>();
        if (taskDetailId != null){
            queryWrapper.eq("task_detail_id", taskDetailId);
        }
        if (taskId != null){
            queryWrapper.eq("task_id",taskId);
        }
        queryWrapper.eq("is_draft", Constants.YES);
        if (StringUtils.isNotEmpty(status)){
            if (Constants.CHECK_AREA_CONFIRM.equals(status)){
                queryWrapper.and(i-> i.eq("status",Constants.CHECK_AREA_CONFIRM).or().eq("status",Constants.CHECK_AREA_NO));
            }else {
                queryWrapper.eq("status",status);
            }
        }
        if (StringUtils.isNotEmpty(materialCode)){
            queryWrapper.like("material_code",materialCode);
        }
        if (StringUtils.isNotEmpty(trayCode)){
            queryWrapper.like("tray_code",trayCode);
        }

        List<TCheckAreaHistory> historyList = tCheckAreaHistoryMapper.selectList(queryWrapper);
        if (historyList.size() == 0){
            return result;
        }

        historyList.forEach(e ->{
            CheckAreaHistoryVO historyVO = new CheckAreaHistoryVO();
            BeanUtils.copyBeanProp(historyVO,e);
            result.add(historyVO);
        });
        if (taskDetailId == null){
            TTaskDetail detail = tTaskDetailMapper.selectOne(new QueryWrapper<TTaskDetail>().eq("task_id", taskId));
            taskDetailId = detail.getId();
        }
        TTaskDetail taskDetail = tTaskDetailMapper.selectById(taskDetailId);
        result.forEach(e->{
            TMaterial material = tMaterialMapper.selectById(e.getMaterialId());
            if (material != null){
                e.setMaterialName(material.getName());
            }
            e.setDeptId(taskDetail.getDeptId());
            e.setCheckDifferenceCount(e.getActualCount() - e.getPredictCount());
        });
        return result;
    }
}
