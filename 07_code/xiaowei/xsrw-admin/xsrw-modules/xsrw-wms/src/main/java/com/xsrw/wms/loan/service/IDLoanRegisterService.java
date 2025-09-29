package com.xsrw.wms.loan.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.loan.domain.DLoanRegister;
import com.xsrw.wms.loan.domain.dto.DLoanRegisterDTO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterEquipmentVO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterVO;

/**
 * 设备借还登记Service接口
 *
 * @author wxr
 * @date 2023-06-09
 */
public interface IDLoanRegisterService extends IService<DLoanRegister> {

    /**
     * 查询设备借还登记列表
     *
     * @param dLoanRegister 设备借还登记
     * @return 设备借还登记集合
     */
    public List<DLoanRegisterVO> selectDLoanRegisterList(DLoanRegisterDTO dLoanRegister);

    /**
     * 查询设备借还登记
     *
     * @param id 设备借还登记主键
     * @return 设备借还登记
     */
    public DLoanRegisterVO selectDLoanRegisterById(Long id);

    /**
     * 新增设备借还登记
     *
     * @param dLoanRegister 设备借还登记
     * @return 结果
     */
    public AjaxResult insertDLoanRegister(DLoanRegister dLoanRegister);

    /**
     * 修改设备借还登记
     *
     * @param dLoanRegister 设备借还登记
     * @return 结果
     */
    public AjaxResult updateDLoanRegister(DLoanRegister dLoanRegister);

    /**
     * 批量删除设备借还登记
     *
     * @param ids 需要删除的设备借还登记主键集合
     * @return 结果
     */
    public int deleteDLoanRegisterByIds(Long[] ids);

    /**
     * 删除设备借还登记信息
     *
     * @param id 设备借还登记主键
     * @return 结果
     */
    public int deleteDLoanRegisterById(Long id);

    /**
     * 获取登记设备列表
     * @param dLoanRegister
     * @return
     */
    List<DLoanRegisterEquipmentVO> getEquipmentList(DLoanRegisterDTO dLoanRegister);

}
