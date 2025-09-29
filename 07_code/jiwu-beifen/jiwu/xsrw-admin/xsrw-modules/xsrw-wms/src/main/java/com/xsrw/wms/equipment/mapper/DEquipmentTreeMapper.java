package com.xsrw.wms.equipment.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.equipment.domain.DEquipmentTree;

/**
 * 经验库分类树Mapper接口
 * 
 * @author zjj
 * @date 2023-05-11
 */
public interface DEquipmentTreeMapper extends BaseMapper<DEquipmentTree>
{

    /**
     * 查询经验库分类树列表
     * 
     * @param dEquipmentTree 经验库分类树
     * @return 经验库分类树集合
     */
    public List<DEquipmentTree> selectDEquipmentTreeList(DEquipmentTree dEquipmentTree);


    /**
     * 删除经验库分类树
     * 
     * @param id 经验库分类树主键
     * @return 结果
     */
    public int deleteDEquipmentTreeById(Long id);

    /**
     * 批量删除经验库分类树
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDEquipmentTreeByIds(Long[] ids);
}
