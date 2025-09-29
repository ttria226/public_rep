package com.xsrw.wms.loan.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;
import com.xsrw.wms.loan.domain.dto.DLoanReturnRecordDTO;
import com.xsrw.wms.loan.domain.vo.DLoanReturnRecordVO;
import org.springframework.stereotype.Repository;

/**
 * 设备借还借出换入记录Mapper接口
 *
 * @author wxr
 * @date 2023-06-09
 */
@Repository
public interface DLoanReturnRecordMapper extends BaseMapper<DLoanReturnRecord> {

    /**
     * 查询设备借还借出换入记录列表
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 设备借还借出换入记录集合
     */
    public List<DLoanReturnRecordVO> selectDLoanReturnRecordList(DLoanReturnRecordDTO dLoanReturnRecord);


    /**
     * 删除设备借还借出换入记录
     *
     * @param id 设备借还借出换入记录主键
     * @return 结果
     */
    public int deleteDLoanReturnRecordById(Long id);

    /**
     * 批量删除设备借还借出换入记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDLoanReturnRecordByIds(Long[] ids);

    /**
     * 根据登记id获取正在借出的设备数量
     * @param registerId
     * @return
     */
    Long getLoanCountByRegisterId(Long registerId);

}
