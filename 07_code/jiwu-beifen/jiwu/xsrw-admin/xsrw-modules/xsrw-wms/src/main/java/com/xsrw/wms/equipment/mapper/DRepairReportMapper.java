package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DRepairReport;

/**
 * 故障报修Mapper接口
 * 
 * @author zjj
 * @date 2023-05-13
 */
public interface DRepairReportMapper extends BaseMapper<DRepairReport>
{

    /**
     * 查询故障报修列表
     * 
     * @param dRepairReport 故障报修
     * @return 故障报修集合
     */
    public List<DRepairReport> selectDRepairReportList(DRepairReport dRepairReport);


    /**
     * 删除故障报修
     * 
     * @param id 故障报修主键
     * @return 结果
     */
    public int deleteDRepairReportById(Long id);

    /**
     * 批量删除故障报修
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDRepairReportByIds(Long[] ids);
}
