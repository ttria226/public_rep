package com.xsrw.wms.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TUnitConfig;
import com.xsrw.wms.base.domain.vo.TUnitConfigVO;

import java.util.List;

/**
 * 包装配置Mapper接口
 *
 * @author lyx
 * @date 2023-05-06
 */
public interface TUnitConfigMapper extends BaseMapper<TUnitConfig>
{

    /**
     * 查询包装配置列表
     *
     * @param tUnitConfig 包装配置
     * @return 包装配置集合
     */
    public List<TUnitConfigVO> selectTUnitConfigList(TUnitConfig tUnitConfig);


    /**
     * 删除包装配置
     *
     * @param id 包装配置主键
     * @return 结果
     */
    public int deleteTUnitConfigById(Long id);

    /**
     * 批量删除包装配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTUnitConfigByIds(Long[] ids);
}
