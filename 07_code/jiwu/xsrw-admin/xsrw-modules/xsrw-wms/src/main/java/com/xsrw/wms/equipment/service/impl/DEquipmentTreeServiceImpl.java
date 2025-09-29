package com.xsrw.wms.equipment.service.impl;

import java.util.List;
import com.xsrw.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xsrw.wms.equipment.mapper.DEquipmentTreeMapper;
import com.xsrw.wms.equipment.domain.DEquipmentTree;
import com.xsrw.wms.equipment.service.IDEquipmentTreeService;

/**
 * 经验库分类树Service业务层处理
 *
 * @author zjj
 * @date 2023-05-11
 */
@Service
public class DEquipmentTreeServiceImpl extends ServiceImpl<DEquipmentTreeMapper, DEquipmentTree> implements IDEquipmentTreeService
{
    @Autowired
    private DEquipmentTreeMapper dEquipmentTreeMapper;


    /**
     * 查询经验库分类树列表
     *
     * @param dEquipmentTree 经验库分类树
     * @return 经验库分类树
     */
    @Override
    public List<DEquipmentTree> selectDEquipmentTreeList(DEquipmentTree dEquipmentTree)
    {
        return dEquipmentTreeMapper.selectDEquipmentTreeList(dEquipmentTree);
    }

    /**
     * 查询经验库分类树
     *
     * @param id 经验库分类树主键
     * @return 经验库分类树
     */
    @Override
    public DEquipmentTree selectDEquipmentTreeById(Long id)
    {
        return dEquipmentTreeMapper.selectById(id);
    }

    /**
     * 新增经验库分类树
     *
     * @param dEquipmentTree 经验库分类树
     * @return 结果
     */
    @Override
    public int insertDEquipmentTree(DEquipmentTree dEquipmentTree)
    {
        return dEquipmentTreeMapper.insert(dEquipmentTree);
    }

    /**
     * 修改经验库分类树
     *
     * @param dEquipmentTree 经验库分类树
     * @return 结果
     */
    @Override
    public int updateDEquipmentTree(DEquipmentTree dEquipmentTree)
    {
        return dEquipmentTreeMapper.updateById(dEquipmentTree);
    }


    /**
     * 批量删除经验库分类树
     *
     * @param ids 需要删除的经验库分类树主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentTreeByIds(Long[] ids)
    {
        return dEquipmentTreeMapper.deleteDEquipmentTreeByIds(ids);
    }

    /**
     * 删除经验库分类树信息
     *
     * @param id 经验库分类树主键
     * @return 结果
     */
    @Override
    public int deleteDEquipmentTreeById(Long id)
    {
        return dEquipmentTreeMapper.deleteDEquipmentTreeById(id);
    }
}
