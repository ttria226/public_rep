package com.xsrw.wms.inout.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.inout.domain.dto.TTaskWcsRecordDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TTaskWcsRecordMapper;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.service.ITTaskWcsRecordService;

/**
 * 任务设备执行记录Service业务层处理
 *
 * @author wxr
 * @date 2023-10-23
 */
@Service
public class TTaskWcsRecordServiceImpl extends ServiceImpl<TTaskWcsRecordMapper, TTaskWcsRecord> implements ITTaskWcsRecordService {
    @Autowired
    private TTaskWcsRecordMapper tTaskWcsRecordMapper;


    /**
     * 查询任务设备执行记录列表
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 任务设备执行记录
     */
    @Override
    public List<TTaskWcsRecordVO> selectTTaskWcsRecordList(TTaskWcsRecord tTaskWcsRecord) {
        return tTaskWcsRecordMapper.selectTTaskWcsRecordList(tTaskWcsRecord);
    }

    /**
     * 查询任务列表
     * @param tTaskWcsRecord
     * @return
     */
    @Override
    public List<TTaskWcsRecordVO> getTaskNoList(TTaskWcsRecord tTaskWcsRecord) {
        return tTaskWcsRecordMapper.getTaskNoList(tTaskWcsRecord);
    }

    /**
     * 查询载具的出库/回库任务执行记录
     * @param tTaskWcsRecord
     * @return
     */
    @Override
    public List<TTaskWcsRecordVO> getListByTray(TTaskWcsRecordDTO tTaskWcsRecord) {
        return tTaskWcsRecordMapper.getListByTray(tTaskWcsRecord);
    }

    /**
     * 查询任务设备执行记录
     *
     * @param id 任务设备执行记录主键
     * @return 任务设备执行记录
     */
    @Override
    public TTaskWcsRecord selectTTaskWcsRecordById(Long id) {
        return tTaskWcsRecordMapper.selectById(id);
    }

    /**
     * 新增任务设备执行记录
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 结果
     */
    @Override
    public int insertTTaskWcsRecord(TTaskWcsRecord tTaskWcsRecord) {
        return tTaskWcsRecordMapper.insert(tTaskWcsRecord);
    }

    /**
     * 修改任务设备执行记录
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 结果
     */
    @Override
    public int updateTTaskWcsRecord(TTaskWcsRecord tTaskWcsRecord) {
        return tTaskWcsRecordMapper.updateById(tTaskWcsRecord);
    }


    /**
     * 批量删除任务设备执行记录
     *
     * @param ids 需要删除的任务设备执行记录主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsRecordByIds(Long[] ids) {
        return tTaskWcsRecordMapper.deleteTTaskWcsRecordByIds(ids);
    }

    /**
     * 删除任务设备执行记录信息
     *
     * @param id 任务设备执行记录主键
     * @return 结果
     */
    @Override
    public int deleteTTaskWcsRecordById(Long id) {
        return tTaskWcsRecordMapper.deleteTTaskWcsRecordById(id);
    }
}
