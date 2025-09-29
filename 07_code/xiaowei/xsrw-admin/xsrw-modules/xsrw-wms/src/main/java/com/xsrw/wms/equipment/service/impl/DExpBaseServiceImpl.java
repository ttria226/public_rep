package com.xsrw.wms.equipment.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DExpBaseMapper;
import com.xsrw.wms.equipment.domain.DExpBase;
import com.xsrw.wms.equipment.service.IDExpBaseService;

/**
 * 保养/维修经验库Service业务层处理
 *
 * @author zjj
 * @date 2023-05-11
 */
@Service
public class DExpBaseServiceImpl extends ServiceImpl<DExpBaseMapper, DExpBase> implements IDExpBaseService
{
    @Autowired
    private DExpBaseMapper dExpBaseMapper;


    /**
     * 查询保养/维修经验库列表
     *
     * @param dExpBase 保养/维修经验库
     * @return 保养/维修经验库
     */
    @Override
    public List<DExpBase> selectDExpBaseList(DExpBase dExpBase)
    {
        return dExpBaseMapper.selectDExpBaseList(dExpBase);
    }

    /**
     * 查询保养/维修经验库
     *
     * @param id 保养/维修经验库主键
     * @return 保养/维修经验库
     */
    @Override
    public DExpBase selectDExpBaseById(Long id)
    {
        return dExpBaseMapper.selectById(id);
    }

    /**
     * 新增保养/维修经验库
     *
     * @param dExpBase 保养/维修经验库
     * @return 结果
     */
    @Override
    public AjaxResult insertDExpBase(DExpBase dExpBase)
    {
        if (dExpBase.getTreeId()==null){
            return AjaxResult.error("请选择所要归属的分类！");
        }
        dExpBaseMapper.insert(dExpBase);
        return AjaxResult.success();
    }

    /**
     * 修改保养/维修经验库
     *
     * @param dExpBase 保养/维修经验库
     * @return 结果
     */
    @Override
    public int updateDExpBase(DExpBase dExpBase)
    {
        return dExpBaseMapper.updateById(dExpBase);
    }


    /**
     * 批量删除保养/维修经验库
     *
     * @param ids 需要删除的保养/维修经验库主键
     * @return 结果
     */
    @Override
    public int deleteDExpBaseByIds(Long[] ids)
    {
        return dExpBaseMapper.deleteDExpBaseByIds(ids);
    }

    /**
     * 删除保养/维修经验库信息
     *
     * @param id 保养/维修经验库主键
     * @return 结果
     */
    @Override
    public int deleteDExpBaseById(Long id)
    {
        return dExpBaseMapper.deleteDExpBaseById(id);
    }
}
