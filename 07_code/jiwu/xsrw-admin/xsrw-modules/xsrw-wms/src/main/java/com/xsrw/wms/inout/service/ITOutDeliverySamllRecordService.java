package com.xsrw.wms.inout.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.inout.domain.TOutDeliverySamllRecord;
import com.xsrw.wms.inout.domain.vo.TOutDeliverySamllRecordVO;

/**
 * 小件出库记录Service接口
 *
 * @author zyq
 * @date 2023-05-13
 */
public interface ITOutDeliverySamllRecordService extends IService<TOutDeliverySamllRecord>
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
     * 查询小件出库记录
     *
     * @param id 小件出库记录主键
     * @return 小件出库记录
     */
    public TOutDeliverySamllRecord selectTOutDeliverySamllRecordById(Long id);

    /**
     * 新增小件出库记录
     *
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 结果
     */
    public int insertTOutDeliverySamllRecord(TOutDeliverySamllRecord tOutDeliverySamllRecord);

    /**
     * 修改小件出库记录
     *
     * @param tOutDeliverySamllRecord 小件出库记录
     * @return 结果
     */
    public int updateTOutDeliverySamllRecord(TOutDeliverySamllRecord tOutDeliverySamllRecord);

    /**
     * 批量删除小件出库记录
     *
     * @param ids 需要删除的小件出库记录主键集合
     * @return 结果
     */
    public int deleteTOutDeliverySamllRecordByIds(Long[] ids);

    /**
     * 删除小件出库记录信息
     *
     * @param id 小件出库记录主键
     * @return 结果
     */
    public int deleteTOutDeliverySamllRecordById(Long id);
}
