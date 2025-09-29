package com.xsrw.wms.base.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TGoodsAllocation;

/**
 * 接货位Mapper接口
 * 
 * @author zjj
 * @date 2023-06-12
 */
public interface TGoodsAllocationMapper extends BaseMapper<TGoodsAllocation>
{

    /**
     * 查询接货位列表
     * 
     * @param tGoodsAllocation 接货位
     * @return 接货位集合
     */
    public List<TGoodsAllocation> selectTGoodsAllocationList(TGoodsAllocation tGoodsAllocation);


    /**
     * 删除接货位
     * 
     * @param id 接货位主键
     * @return 结果
     */
    public int deleteTGoodsAllocationById(Long id);

    /**
     * 批量删除接货位
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTGoodsAllocationByIds(Long[] ids);
}
