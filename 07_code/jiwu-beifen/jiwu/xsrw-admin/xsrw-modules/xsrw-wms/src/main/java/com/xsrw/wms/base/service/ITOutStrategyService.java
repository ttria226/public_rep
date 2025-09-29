package com.xsrw.wms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.wms.base.domain.TOutStrategy;

/**
 * 拣货策略Service接口
 *
 * @author wxr
 * @date 2023-05-06
 */
public interface ITOutStrategyService extends IService<TOutStrategy> {

    /**
     * 查询拣货策略列表
     *
     * @param tOutStrategy 拣货策略
     * @return 拣货策略集合
     */
    public List<TOutStrategy> selectTOutStrategyList(TOutStrategy tOutStrategy);

    /**
     * 查询拣货策略
     *
     * @param id 拣货策略主键
     * @return 拣货策略
     */
    public TOutStrategy selectTOutStrategyById(Long id);

    /**
     * 新增拣货策略
     *
     * @param tOutStrategy 拣货策略
     * @return 结果
     */
    public int insertTOutStrategy(TOutStrategy tOutStrategy);

    /**
     * 修改拣货策略
     *
     * @param tOutStrategy 拣货策略
     * @return 结果
     */
    public int updateTOutStrategy(TOutStrategy tOutStrategy);

    /**
     * 批量删除拣货策略
     *
     * @param ids 需要删除的拣货策略主键集合
     * @return 结果
     */
    public int deleteTOutStrategyByIds(Long[] ids);

    /**
     * 删除拣货策略信息
     *
     * @param id 拣货策略主键
     * @return 结果
     */
    public int deleteTOutStrategyById(Long id);

    /**
     * 获取自动拣货策略是否启用批次
     * @return true 启用，需要循环物料信息 FALSE： 不启用，则一个出库任务调用一次getStrategy
     */
    boolean getStrategyByIsBatch();

    /**
     * 获取拣货策略
     *
     * @return 结果
     */
    String getStrategy(Long materialId);

}
