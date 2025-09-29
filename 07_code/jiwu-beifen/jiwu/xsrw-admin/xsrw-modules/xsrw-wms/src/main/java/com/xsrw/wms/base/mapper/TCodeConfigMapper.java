package com.xsrw.wms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xsrw.wms.base.domain.TCodeConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 编码配置Mapper接口
 */
@Mapper
public interface TCodeConfigMapper extends BaseMapper<TCodeConfig> {

    /**
     * 查询编码配置列表
     *
     * @param codeConfig 编码配置
     * @return 编码配置集合
     */
    List<TCodeConfig> selectCodeConfigList(TCodeConfig codeConfig);

    /**
     * 删除编码配置
     *
     * @param id 编码配置主键
     * @return 结果
     */
    int deleteCodeConfigById(Long id);

    /**
     * 批量删除编码配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCodeConfigByIds(Long[] ids);
}
