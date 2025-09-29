package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.equipment.domain.DExpBase;

/**
 * 保养/维修经验库Service接口
 *
 * @author zjj
 * @date 2023-05-11
 */
public interface IDExpBaseService extends IService<DExpBase>
{

    /**
     * 查询保养/维修经验库列表
     *
     * @param dExpBase 保养/维修经验库
     * @return 保养/维修经验库集合
     */
    public List<DExpBase> selectDExpBaseList(DExpBase dExpBase);

    /**
     * 查询保养/维修经验库
     *
     * @param id 保养/维修经验库主键
     * @return 保养/维修经验库
     */
    public DExpBase selectDExpBaseById(Long id);

    /**
     * 新增保养/维修经验库
     *
     * @param dExpBase 保养/维修经验库
     * @return 结果
     */
    public AjaxResult insertDExpBase(DExpBase dExpBase);

    /**
     * 修改保养/维修经验库
     *
     * @param dExpBase 保养/维修经验库
     * @return 结果
     */
    public int updateDExpBase(DExpBase dExpBase);

    /**
     * 批量删除保养/维修经验库
     *
     * @param ids 需要删除的保养/维修经验库主键集合
     * @return 结果
     */
    public int deleteDExpBaseByIds(Long[] ids);

    /**
     * 删除保养/维修经验库信息
     *
     * @param id 保养/维修经验库主键
     * @return 结果
     */
    public int deleteDExpBaseById(Long id);
}
