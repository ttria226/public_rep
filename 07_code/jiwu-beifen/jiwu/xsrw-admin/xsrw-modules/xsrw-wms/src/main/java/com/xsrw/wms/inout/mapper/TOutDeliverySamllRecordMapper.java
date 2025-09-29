package com.xsrw.wms.inout.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.inout.domain.TOutDeliverySamllRecord;
import com.xsrw.wms.inout.domain.vo.TOutDeliverySamllRecordVO;

/**
 * 小件出库记录Mapper接口
 * 
 * @author zyq
 * @date 2023-05-13
 */
public interface TOutDeliverySamllRecordMapper extends BaseMapper<TOutDeliverySamllRecord>
{

    /**
     * 查询小件出库记录列表
     * 
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 小件出库记录集合
     */
    public List<TOutDeliverySamllRecord> selectTOutDeliverySamllRecordList(TOutDeliverySamllRecord tOutDeliverySamllRecord);


     public List<TOutDeliverySamllRecordVO> selectTOutDeliverySamllRecordVOList(TOutDeliverySamllRecord tOutDeliverySamllRecord);


    /**
     * 删除小件出库记录
     * 
     * @param id 小件出库记录主键
     * @return 结果
     */
    public int deleteTOutDeliverySamllRecordById(Long id);

    /**
     * 批量删除小件出库记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutDeliverySamllRecordByIds(Long[] ids);
}
