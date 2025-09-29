package com.xsrw.wms.loan.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.equipment.domain.WmsEquipment;
import com.xsrw.wms.equipment.mapper.WmsEquipmentMapper;
import com.xsrw.wms.loan.domain.dto.DLoanRegisterDTO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterEquipmentVO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterVO;
import com.xsrw.wms.loan.mapper.DLoanReturnRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.loan.mapper.DLoanRegisterMapper;
import com.xsrw.wms.loan.domain.DLoanRegister;
import com.xsrw.wms.loan.service.IDLoanRegisterService;

/**
 * 设备借还登记Service业务层处理
 *
 * @author wxr
 * @date 2023-06-09
 */
@Service
public class DLoanRegisterServiceImpl extends ServiceImpl<DLoanRegisterMapper, DLoanRegister> implements IDLoanRegisterService {
    @Autowired
    private DLoanRegisterMapper dLoanRegisterMapper;
    @Autowired
    private DLoanReturnRecordMapper dLoanReturnRecordMapper;
    @Autowired
    private WmsEquipmentMapper wmsEquipmentMapper;

    /**
     * 查询设备借还登记列表
     *
     * @param dLoanRegister 设备借还登记
     * @return 设备借还登记
     */
    @Override
    public List<DLoanRegisterVO> selectDLoanRegisterList(DLoanRegisterDTO dLoanRegister) {
        return dLoanRegisterMapper.selectDLoanRegisterList(dLoanRegister);
    }

    /**
     * 查询设备借还登记
     *
     * @param id 设备借还登记主键
     * @return 设备借还登记
     */
    @Override
    public DLoanRegisterVO selectDLoanRegisterById(Long id) {
        DLoanRegisterVO dLoanRegisterVO = new DLoanRegisterVO();
        DLoanRegister dLoanRegister = dLoanRegisterMapper.selectById(id);
        BeanUtils.copyBeanProp(dLoanRegisterVO,dLoanRegister);
        WmsEquipment wmsEquipment = wmsEquipmentMapper.selectById(dLoanRegister.getEquipmentId());
        if(wmsEquipment != null){
            dLoanRegisterVO.setEquipmentCode(wmsEquipment.getEquNo());
            dLoanRegisterVO.setEquipmentName(wmsEquipment.getName());
        }
        return dLoanRegisterVO;
    }

    /**
     * 新增设备借还登记
     *
     * @param dLoanRegister 设备借还登记
     * @return 结果
     */
    @Override
    public AjaxResult insertDLoanRegister(DLoanRegister dLoanRegister) {
        Long count = this.getExistCountByOriginId(dLoanRegister.getEquipmentId(), null);
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        dLoanRegister.setAvailableCount(dLoanRegister.getRegisterCount());
        dLoanRegisterMapper.insert(dLoanRegister);
        return AjaxResult.success();
    }

    /**
     * 获取原单是否已存在
     *
     * @param originId
     * @return
     */
    public Long getExistCountByOriginId(Long originId, Long id) {
        QueryWrapper<DLoanRegister> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.eq("equipment_id", originId);
        if (id != null) {
            queryWrapper.ne("id", id);
        }
        return dLoanRegisterMapper.selectCount(queryWrapper);
    }

    /**
     * 修改设备借还登记
     *
     * @param dLoanRegister 设备借还登记
     * @return 结果
     */
    @Override
    public AjaxResult updateDLoanRegister(DLoanRegister dLoanRegister) {
//        DLoanRegister oldDO = dLoanRegisterMapper.selectById(dLoanRegister.getId());
//        if (oldDO == null) {
//            return AjaxResult.error("未查询到相关信息");
//        }
        //查询已借出数量，登记数量不可比已借出数量少
        Long loanCount = dLoanReturnRecordMapper.getLoanCountByRegisterId(dLoanRegister.getId());
        if (loanCount > dLoanRegister.getRegisterCount()) {
            return AjaxResult.error("登记数量不可小于已借出数量");
        }
        Long count = this.getExistCountByOriginId(dLoanRegister.getEquipmentId(), dLoanRegister.getId());
        if (count > 0) {
            return AjaxResult.error("当前单据已添加，不可重复添加");
        }
        dLoanRegister.setAvailableCount(dLoanRegister.getRegisterCount() - loanCount);
        dLoanRegisterMapper.updateById(dLoanRegister);
        return AjaxResult.success();
    }


    /**
     * 批量删除设备借还登记
     *
     * @param ids 需要删除的设备借还登记主键
     * @return 结果
     */
    @Override
    public int deleteDLoanRegisterByIds(Long[] ids) {
        return dLoanRegisterMapper.deleteDLoanRegisterByIds(ids);
    }

    /**
     * 删除设备借还登记信息
     *
     * @param id 设备借还登记主键
     * @return 结果
     */
    @Override
    public int deleteDLoanRegisterById(Long id) {
        return dLoanRegisterMapper.deleteDLoanRegisterById(id);
    }

    /**
     * 获取登记设备列表
     * @param dLoanRegister
     * @return
     */
    @Override
    public List<DLoanRegisterEquipmentVO> getEquipmentList(DLoanRegisterDTO dLoanRegister) {
        return dLoanRegisterMapper.getEquipmentList(dLoanRegister);
    }
}
