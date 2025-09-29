package com.xsrw.wms.loan.service.impl;

import java.util.List;

import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.loan.domain.DLoanRegister;
import com.xsrw.wms.loan.domain.dto.DLoanReturnRecordDTO;
import com.xsrw.wms.loan.domain.vo.DLoanReturnRecordVO;
import com.xsrw.wms.loan.mapper.DLoanRegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.loan.mapper.DLoanReturnRecordMapper;
import com.xsrw.wms.loan.domain.DLoanReturnRecord;
import com.xsrw.wms.loan.service.IDLoanReturnRecordService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备借还借出换入记录Service业务层处理
 *
 * @author wxr
 * @date 2023-06-09
 */
@Service
public class DLoanReturnRecordServiceImpl extends ServiceImpl<DLoanReturnRecordMapper, DLoanReturnRecord> implements IDLoanReturnRecordService {
    @Autowired
    private DLoanReturnRecordMapper dLoanReturnRecordMapper;
    @Autowired
    private DLoanRegisterMapper dLoanRegisterMapper;

    /**
     * 查询设备借还借出换入记录列表
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 设备借还借出换入记录
     */
    @Override
    public List<DLoanReturnRecordVO> selectDLoanReturnRecordList(DLoanReturnRecordDTO dLoanReturnRecord) {
        return dLoanReturnRecordMapper.selectDLoanReturnRecordList(dLoanReturnRecord);
    }

    /**
     * 查询设备借还借出换入记录
     *
     * @param id 设备借还借出换入记录主键
     * @return 设备借还借出换入记录
     */
    @Override
    public DLoanReturnRecord selectDLoanReturnRecordById(Long id) {
        return dLoanReturnRecordMapper.selectById(id);
    }

    /**
     * 新增设备借还借出换入记录
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 结果
     */
    @Override
    @Transactional
    public AjaxResult insertDLoanReturnRecord(DLoanReturnRecord dLoanReturnRecord) {
        DLoanRegister dLoanRegister = dLoanRegisterMapper.selectById(dLoanReturnRecord.getLoanRegisterId());
        if (dLoanRegister == null) {
            return AjaxResult.error("未查询到设备登记信息");
        }
        if (dLoanReturnRecord.getLoanCount() > dLoanRegister.getAvailableCount()) {
            return AjaxResult.error("借出设备不可大于当前设备可用数量");
        }
        dLoanReturnRecord.setStatus(Constants.INOUT_STATUS_NOT);
        dLoanReturnRecord.setReturnCount(0L);
        dLoanReturnRecordMapper.insert(dLoanReturnRecord);
        DLoanRegister registerUpdate = new DLoanRegister();
        registerUpdate.setId(dLoanReturnRecord.getLoanRegisterId());
        registerUpdate.setAvailableCount(dLoanRegister.getAvailableCount() - dLoanReturnRecord.getLoanCount());
        dLoanRegisterMapper.updateById(registerUpdate);
        return AjaxResult.success();
    }

    /**
     * 修改设备借还借出换入记录
     *
     * @param dLoanReturnRecord 设备借还借出换入记录
     * @return 结果
     */
    @Override
    public int updateDLoanReturnRecord(DLoanReturnRecord dLoanReturnRecord) {
        return dLoanReturnRecordMapper.updateById(dLoanReturnRecord);
    }


    /**
     * 批量删除设备借还借出换入记录
     *
     * @param ids 需要删除的设备借还借出换入记录主键
     * @return 结果
     */
    @Override
    public int deleteDLoanReturnRecordByIds(Long[] ids) {
        return dLoanReturnRecordMapper.deleteDLoanReturnRecordByIds(ids);
    }

    /**
     * 删除设备借还借出换入记录信息
     *
     * @param id 设备借还借出换入记录主键
     * @return 结果
     */
    @Override
    public int deleteDLoanReturnRecordById(Long id) {
        return dLoanReturnRecordMapper.deleteDLoanReturnRecordById(id);
    }

    /**
     * 设备还入
     * @param dLoanReturnRecord
     * @return
     */
    @Override
    @Transactional
    public AjaxResult returnRecord(DLoanReturnRecord dLoanReturnRecord) {
        //查询原数据
        DLoanReturnRecord oldDO = dLoanReturnRecordMapper.selectById(dLoanReturnRecord.getId());
        if (oldDO == null) {
            return AjaxResult.error("未查询到对应信息");
        }
        if (Constants.INOUT_STATUS_END.equals(oldDO.getStatus())) {
            return AjaxResult.error("设备数量已还完，不可再次还入");
        }
        if (dLoanReturnRecord.getReturnCount() > oldDO.getLoanCount()) {
            return AjaxResult.error("还入数量不可大于当前设备借出数量");
        }
        //查询对应的设备登记数据
        DLoanRegister dLoanRegister = dLoanRegisterMapper.selectById(oldDO.getLoanRegisterId());
        if (dLoanRegister == null) {
            return AjaxResult.error("未查询到设备登记信息");
        }
        if (dLoanReturnRecord.getReturnCount().equals(oldDO.getLoanCount())) {
            dLoanReturnRecord.setStatus(Constants.INOUT_STATUS_END);
        } else {
            dLoanReturnRecord.setStatus(Constants.INOUT_STATUS_PART);
        }
        //更新借出还入表
        dLoanReturnRecordMapper.updateById(dLoanReturnRecord);
        //更新登记表
        DLoanRegister registerUpdate = new DLoanRegister();
        registerUpdate.setId(oldDO.getLoanRegisterId());
        Long differCount = dLoanReturnRecord.getReturnCount() - oldDO.getReturnCount();
        registerUpdate.setAvailableCount(dLoanRegister.getAvailableCount() + differCount);
        dLoanRegisterMapper.updateById(registerUpdate);
        return AjaxResult.success();
    }
}
