package com.xsrw.wms.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.equipment.domain.DEquipmentTree;

/**
 * 经验库分类树Service接口
 *
 * @author zjj
 * @date 2023-05-11
 */
public interface IDEquipmentTreeService extends IService<DEquipmentTree>
{

    /**
     * 查询经验库分类树列表
     *
     * @param dEquipmentTree 经验库分类树
     * @return 经验库分类树集合
     */
    public List<DEquipmentTree> selectDEquipmentTreeList(DEquipmentTree dEquipmentTree);

    /**
     * 查询经验库分类树
     *
     * @param id 经验库分类树主键
     * @return 经验库分类树
     */
    public DEquipmentTree selectDEquipmentTreeById(Long id);

    /**
     * 新增经验库分类树
     *
     * @param dEquipmentTree 经验库分类树
     * @return 结果
     */
    public int insertDEquipmentTree(DEquipmentTree dEquipmentTree);

    /**
     * 修改经验库分类树
     *
     * @param dEquipmentTree 经验库分类树
     * @return 结果
     */
    public int updateDEquipmentTree(DEquipmentTree dEquipmentTree);

    /**
     * 批量删除经验库分类树
     *
     * @param ids 需要删除的经验库分类树主键集合
     * @return 结果
     */
    public int deleteDEquipmentTreeByIds(Long[] ids);

    /**
     * 删除经验库分类树信息
     *
     * @param id 经验库分类树主键
     * @return 结果
     */
    public int deleteDEquipmentTreeById(Long id);
}
