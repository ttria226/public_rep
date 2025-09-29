package com.xsrw.wms.loan.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;
import com.xsrw.wms.loan.domain.dto.DLoanReturnRecordDTO;
import com.xsrw.wms.loan.domain.vo.DLoanReturnRecordVO;

/**
 * 设备借还借出换入记录Service接口
 *
 * @author wxr
 * @date 2023-06-09
 */
public interface IDLoanReturnRecordService extends IService<DLoanReturnRecord> {

    /**
     * 查询设备借还借出换入记录列表
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 设备借还借出换入记录集合
     */
    public List<DLoanReturnRecordVO> selectDLoanReturnRecordList(DLoanReturnRecordDTO dLoanReturnRecord);

    /**
     * 查询设备借还借出换入记录
     *
     * @param id 设备借还借出换入记录主键
     * @return 设备借还借出换入记录
     */
    public DLoanReturnRecord selectDLoanReturnRecordById(Long id);

    /**
     * 新增设备借还借出换入记录
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 结果
     */
    public AjaxResult insertDLoanReturnRecord(DLoanReturnRecord dLoanReturnRecord);

    /**
     * 修改设备借还借出换入记录
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 结果
     */
    public int updateDLoanReturnRecord(DLoanReturnRecord dLoanReturnRecord);

    /**
     * 批量删除设备借还借出换入记录
     *
     * @param ids 需要删除的设备借还借出换入记录主键集合
     * @return 结果
     */
    public int deleteDLoanReturnRecordByIds(Long[] ids);

    /**
     * 删除设备借还借出换入记录信息
     *
     * @param id 设备借还借出换入记录主键
     * @return 结果
     */
    public int deleteDLoanReturnRecordById(Long id);

    /**
     * 设备还入
     * @param dLoanReturnRecord
     * @return
     */
    AjaxResult returnRecord(DLoanReturnRecord dLoanReturnRecord);
}
