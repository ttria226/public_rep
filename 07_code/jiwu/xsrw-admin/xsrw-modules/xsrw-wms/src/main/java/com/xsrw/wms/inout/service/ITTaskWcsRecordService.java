package com.xsrw.wms.inout.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TTaskWcsRecord;
import com.xsrw.wms.inout.domain.dto.TTaskWcsRecordDTO;
import com.xsrw.wms.inout.domain.vo.TTaskWcsRecordVO;

/**
 * 任务设备执行记录Service接口
 *
 * @author wxr
 * @date 2023-10-23
 */
public interface ITTaskWcsRecordService extends IService<TTaskWcsRecord> {

    /**
     * 查询任务设备执行记录列表
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 任务设备执行记录集合
     */
    public List<TTaskWcsRecordVO> selectTTaskWcsRecordList(TTaskWcsRecord tTaskWcsRecord);

    /**
     * 查询任务列表
     * @param tTaskWcsRecord
     * @return
     */
    List<TTaskWcsRecordVO> getTaskNoList(TTaskWcsRecord tTaskWcsRecord);

    /**
     * 查询载具的出库/回库任务执行记录
     * @param tTaskWcsRecord
     * @return
     */
    List<TTaskWcsRecordVO> getListByTray(TTaskWcsRecordDTO tTaskWcsRecord);

    /**
     * 查询任务设备执行记录
     *
     * @param id 任务设备执行记录主键
     * @return 任务设备执行记录
     */
    public TTaskWcsRecord selectTTaskWcsRecordById(Long id);

    /**
     * 新增任务设备执行记录
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 结果
     */
    public int insertTTaskWcsRecord(TTaskWcsRecord tTaskWcsRecord);

    /**
     * 修改任务设备执行记录
     *
     * @param tTaskWcsRecord 任务设备执行记录
     * @return 结果
     */
    public int updateTTaskWcsRecord(TTaskWcsRecord tTaskWcsRecord);

    /**
     * 批量删除任务设备执行记录
     *
     * @param ids 需要删除的任务设备执行记录主键集合
     * @return 结果
     */
    public int deleteTTaskWcsRecordByIds(Long[] ids);

    /**
     * 删除任务设备执行记录信息
     *
     * @param id 任务设备执行记录主键
     * @return 结果
     */
    public int deleteTTaskWcsRecordById(Long id);

}
