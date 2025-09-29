package com.xsrw.wms.loan.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.loan.domain.DLoanRegister;
import com.xsrw.wms.loan.domain.dto.DLoanRegisterDTO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterEquipmentVO;
import com.xsrw.wms.loan.domain.vo.DLoanRegisterVO;
import org.springframework.stereotype.Repository;

/**
 * 设备借还登记Mapper接口
 *
 * @author wxr
 * @date 2023-06-09
 */
@Repository
public interface DLoanRegisterMapper extends BaseMapper<DLoanRegister> {

    /**
     * 查询设备借还登记列表
     *
     * @param dLoanRegister 设备借还登记
     * @return 设备借还登记集合
     */
    public List<DLoanRegisterVO> selectDLoanRegisterList(DLoanRegisterDTO dLoanRegister);


    /**
     * 删除设备借还登记
     *
     * @param id 设备借还登记主键
     * @return 结果
     */
    public int deleteDLoanRegisterById(Long id);

    /**
     * 批量删除设备借还登记
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDLoanRegisterByIds(Long[] ids);

    /**
     * 获取登记设备列表
     * @param dLoanRegister
     * @return
     */
    List<DLoanRegisterEquipmentVO> getEquipmentList(DLoanRegisterDTO dLoanRegister);
}
