package com.xsrw.wms.base.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.base.domain.TUnitConfig;
import com.xsrw.wms.base.domain.vo.TUnitConfigVO;

import java.util.List;
import java.util.Map;

/**
 * 包装配置Service接口
 *
 * @author lyx
 * @date 2023-05-06
 */
public interface ITUnitConfigService extends IService<TUnitConfig>
{

    /**
     * 查询包装配置列表
     *
     * @param tUnitConfig 包装配置
     * @return 包装配置集合
     */
    List<TUnitConfigVO> selectTUnitConfigList(TUnitConfig tUnitConfig);

    /**
     * 查询包装配置
     *
     * @param id 包装配置主键
     * @return 包装配置
     */
    TUnitConfig selectTUnitConfigById(Long id);

    /**
     * 新增包装配置
     *
     * @param tUnitConfig 包装配置
     * @return 结果
     */
    AjaxResult insertTUnitConfig(TUnitConfig tUnitConfig);

    /**
     * 修改包装配置
     *
     * @param tUnitConfig 包装配置
     * @return 结果
     */
    AjaxResult updateTUnitConfig(TUnitConfig tUnitConfig);

    /**
     * 批量删除包装配置
     *
     * @param ids 需要删除的包装配置主键集合
     * @return 结果
     */
    int deleteTUnitConfigByIds(Long[] ids);

    /**
     * 删除包装配置信息
     *
     * @param id 包装配置主键
     * @return 结果
     */
    int deleteTUnitConfigById(Long id);

    /**
     * 根据物料ids获取对应的小件数量
     * @param materialIds
     * @return
     */
    Map<Long, Long> getUnitCount(List<Long> materialIds);
}
