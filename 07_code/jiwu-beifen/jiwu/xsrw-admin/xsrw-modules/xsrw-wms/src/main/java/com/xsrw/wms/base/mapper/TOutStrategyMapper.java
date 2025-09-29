package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TOutStrategy;

/**
 * 拣货策略Mapper接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface TOutStrategyMapper extends BaseMapper<TOutStrategy> {

    /**
     * 查询拣货策略列表
     *
     * @param tOutStrategy 拣货策略
     * @return 拣货策略集合
     */
    public List<TOutStrategy> selectTOutStrategyList(TOutStrategy tOutStrategy);


    /**
     * 删除拣货策略
     *
     * @param id 拣货策略主键
     * @return 结果
     */
    public int deleteTOutStrategyById(Long id);

    /**
     * 批量删除拣货策略
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOutStrategyByIds(Long[] ids);
}
