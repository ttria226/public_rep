package com.xsrw.wms.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TWarehouse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库Mapper接口
 * 
 * @author tyk
 * @date 2023-07-12
 */
public interface TWarehouseMapper extends BaseMapper<TWarehouse>
{

    /**
     * 查询仓库列表
     * 
     * @param tWarehouse 仓库
     * @return 仓库集合
     */
    public List<TWarehouse> selectTWarehouseList(TWarehouse tWarehouse);


    TWarehouse selectTWarehouse(@Param("id") Long id);


    /**
     * 删除仓库
     * 
     * @param id 仓库主键
     * @return 结果
     */
    public int deleteTWarehouseById(Long id);

    /**
     * 批量删除仓库
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTWarehouseByIds(Long[] ids);
}
