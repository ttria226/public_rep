package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DRepairReport;

/**
 * 故障报修Service接口
 *
 * @author zjj
 * @date 2023-05-13
 */
public interface IDRepairReportService extends IService<DRepairReport>
{

    /**
     * 查询故障报修列表
     *
     * @param dRepairReport 故障报修
     * @return 故障报修集合
     */
    public List<DRepairReport> selectDRepairReportList(DRepairReport dRepairReport);

    /**
     * 查询故障报修
     *
     * @param id 故障报修主键
     * @return 故障报修
     */
    public DRepairReport selectDRepairReportById(Long id);

    /**
     * 新增故障报修
     *
     * @param dRepairReport 故障报修
     * @return 结果
     */
    public AjaxResult insertDRepairReport(DRepairReport dRepairReport);

    /**
     * 修改故障报修
     *
     * @param dRepairReport 故障报修
     * @return 结果
     */
    public int updateDRepairReport(DRepairReport dRepairReport);

    /**
     * 批量删除故障报修
     *
     * @param ids 需要删除的故障报修主键集合
     * @return 结果
     */
    public int deleteDRepairReportByIds(Long[] ids);

    /**
     * 删除故障报修信息
     *
     * @param id 故障报修主键
     * @return 结果
     */
    public int deleteDRepairReportById(Long id);

    /**
     * 新增故障报修
     *
     * @param id 故障报修
     * @return 结果
     */
    public AjaxResult createOrder(Long id);
}
