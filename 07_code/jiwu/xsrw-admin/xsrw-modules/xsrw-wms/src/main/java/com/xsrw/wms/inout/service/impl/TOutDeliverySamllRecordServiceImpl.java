package com.xsrw.wms.inout.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.wms.inout.domain.vo.TOutDeliverySamllRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.inout.mapper.TOutDeliverySamllRecordMapper;
import com.xsrw.wms.inout.domain.TOutDeliverySamllRecord;
import com.xsrw.wms.inout.service.ITOutDeliverySamllRecordService;

/**
 * 小件出库记录Service业务层处理
 *
 * @author zyq
 * @date 2023-05-13
 */
@Service
public class TOutDeliverySamllRecordServiceImpl extends ServiceImpl<TOutDeliverySamllRecordMapper, TOutDeliverySamllRecord> implements ITOutDeliverySamllRecordService
{
    @Autowired
    private TOutDeliverySamllRecordMapper tOutDeliverySamllRecordMapper;


    /**
     * 查询小件出库记录列表
     *
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 小件出库记录
     */
    @Override
    public List<TOutDeliverySamllRecord> selectTOutDeliverySamllRecordList(TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        return tOutDeliverySamllRecordMapper.selectTOutDeliverySamllRecordList(tOutDeliverySamllRecord);
    }

    public List<TOutDeliverySamllRecordVO> selectTOutDeliverySamllRecordVOList(TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        return tOutDeliverySamllRecordMapper.selectTOutDeliverySamllRecordVOList(tOutDeliverySamllRecord);
    }

    /**
     * 查询小件出库记录
     *
     * @param id 小件出库记录主键
     * @return 小件出库记录
     */
    @Override
    public TOutDeliverySamllRecord selectTOutDeliverySamllRecordById(Long id)
    {
        return tOutDeliverySamllRecordMapper.selectById(id);
    }

    /**
     * 新增小件出库记录
     *
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 结果
     */
    @Override
    public int insertTOutDeliverySamllRecord(TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        return tOutDeliverySamllRecordMapper.insert(tOutDeliverySamllRecord);
    }

    /**
     * 修改小件出库记录
     *
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 结果
     */
    @Override
    public int updateTOutDeliverySamllRecord(TOutDeliverySamllRecord tOutDeliverySamllRecord)
    {
        return tOutDeliverySamllRecordMapper.updateById(tOutDeliverySamllRecord);
    }


    /**
     * 批量删除小件出库记录
     *
     * @param ids 需要删除的小件出库记录主键
     * @return 结果
     */
    @Override
    public int deleteTOutDeliverySamllRecordByIds(Long[] ids)
    {
        return tOutDeliverySamllRecordMapper.deleteTOutDeliverySamllRecordByIds(ids);
    }

    /**
     * 删除小件出库记录信息
     *
     * @param id 小件出库记录主键
     * @return 结果
     */
    @Override
    public int deleteTOutDeliverySamllRecordById(Long id)
    {
        return tOutDeliverySamllRecordMapper.deleteTOutDeliverySamllRecordById(id);
    }
}
