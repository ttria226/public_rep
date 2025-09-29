package com.xsrw.wms.base.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TGoodsAllocation;

/**
 * 接货位Service接口
 *
 * @author zjj
 * @date 2023-06-12
 */
public interface ITGoodsAllocationService extends IService<TGoodsAllocation>
{

    /**
     * 查询接货位列表
     *
     * @param tGoodsAllocation 接货位
     * @return 接货位集合
     */
    public List<TGoodsAllocation> selectTGoodsAllocationList(TGoodsAllocation tGoodsAllocation);

    /**
     * 查询接货位
     *
     * @param id 接货位主键
     * @return 接货位
     */
    public TGoodsAllocation selectTGoodsAllocationById(Long id);

    /**
     * 新增接货位
     *
     * @param tGoodsAllocation 接货位
     * @return 结果
     */
    public int insertTGoodsAllocation(TGoodsAllocation tGoodsAllocation);

    /**
     * 修改接货位
     *
     * @param tGoodsAllocation 接货位
     * @return 结果
     */
    public int updateTGoodsAllocation(TGoodsAllocation tGoodsAllocation);

    /**
     * 批量删除接货位
     *
     * @param ids 需要删除的接货位主键集合
     * @return 结果
     */
    public int deleteTGoodsAllocationByIds(Long[] ids);

    /**
     * 删除接货位信息
     *
     * @param id 接货位主键
     * @return 结果
     */
    public int deleteTGoodsAllocationById(Long id);
}
