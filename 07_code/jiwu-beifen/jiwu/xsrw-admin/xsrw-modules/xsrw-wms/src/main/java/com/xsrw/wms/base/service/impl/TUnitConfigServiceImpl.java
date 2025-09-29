package com.xsrw.wms.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TUnit;
import com.xsrw.wms.base.domain.TUnitConfig;
import com.xsrw.wms.base.domain.vo.TUnitConfigVO;
import com.xsrw.wms.base.mapper.TMaterialMapper;
import com.xsrw.wms.base.mapper.TUnitConfigMapper;
import com.xsrw.wms.base.mapper.TUnitMapper;
import com.xsrw.wms.base.service.ITUnitConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包装配置Service业务层处理
 *
 * @author lyx
 * @date 2023-05-06
 */
@Service
public class TUnitConfigServiceImpl extends ServiceImpl<TUnitConfigMapper, TUnitConfig> implements ITUnitConfigService
{
    @Autowired
    private TUnitConfigMapper tUnitConfigMapper;

    @Autowired
    private TUnitMapper tUnitMapper;

    @Autowired
    private TMaterialMapper tMaterialMapper;


    /**
     * 查询包装配置列表
     *
     * @param entity 包装配置
     * @return 包装配置
     */
    @Override
    public List<TUnitConfigVO> selectTUnitConfigList(TUnitConfig entity)
    {
        return tUnitConfigMapper.selectTUnitConfigList(entity);
    }

    /**
     * 查询包装配置
     *
     * @param id 包装配置主键
     * @return 包装配置
     */
    @Override
    public TUnitConfig selectTUnitConfigById(Long id)
    {
        return tUnitConfigMapper.selectById(id);
    }

    /**
     * 新增包装配置
     *
     * @param entity 包装配置
     * @return 结果
     */
    @Override
    public AjaxResult insertTUnitConfig(TUnitConfig entity)
    {
//        TMaterial material = tMaterialMapper.selectById(entity.getMaterialId());
//        if (!entity.getMinUnitId().equals(material.getUnitId()) || !entity.getMaxUnitId().equals(material.getUnitId())){
//            return AjaxResult.error("该单位与物料无法进行换算，请重新选择");
//        }
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setCreateBy(SecurityUtils.getUsername());
        //校验大包装名称是否存在
        Long count = tUnitConfigMapper.selectCount(Wrappers.lambdaQuery(TUnitConfig.class)
                .eq(TUnitConfig::getMaterialId,entity.getMaterialId())
                .eq(TUnitConfig::getDelFlag,Constants.NO));
        if (count>=1){
            return AjaxResult.error("此物料已存在,请勿重复添加");
        }
        TUnit minUnit = tUnitMapper.selectById(entity.getMinUnitId());
        TUnit maxUnit = tUnitMapper.selectById(entity.getMaxUnitId());
        entity.setUnitConfigName("1" + maxUnit.getName() + entity.getCount() + minUnit.getName());
        return AjaxResult.success(tUnitConfigMapper.insert(entity));
    }

    /**
     * 修改包装配置
     *
     * @param entity 包装配置
     * @return 结果
     */
    @Override
    public AjaxResult updateTUnitConfig(TUnitConfig entity)
    {
        entity.setUpdateTime(DateUtils.getNowDate());
        entity.setUpdateBy(SecurityUtils.getUsername());
        TUnit minUnit = tUnitMapper.selectById(entity.getMinUnitId());
        TUnit maxUnit = tUnitMapper.selectById(entity.getMaxUnitId());
        entity.setUnitConfigName("1" + maxUnit.getName() + entity.getCount() + minUnit.getName());
        return AjaxResult.success(tUnitConfigMapper.updateById(entity));
    }


    /**
     * 批量删除包装配置
     *
     * @param ids 需要删除的包装配置主键
     * @return 结果
     */
    @Override
    public int deleteTUnitConfigByIds(Long[] ids)
    {
        return tUnitConfigMapper.deleteTUnitConfigByIds(ids);
    }

    /**
     * 删除包装配置信息
     *
     * @param id 包装配置主键
     * @return 结果
     */
    @Override
    public int deleteTUnitConfigById(Long id)
    {
        return tUnitConfigMapper.deleteTUnitConfigById(id);
    }

    /**
     * 根据物料ids获取对应的小件数量
     * @param materialIds
     * @return
     */
    @Override
    public Map<Long, Long> getUnitCount(List<Long> materialIds) {
        Map<Long, Long> resMap = new HashMap<>();
        QueryWrapper<TUnitConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", Constants.DEL_FLAG_NO);
        queryWrapper.in("material_id", materialIds);
        List<TUnitConfig> list = tUnitConfigMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(list)){
            resMap = list.stream().collect(Collectors.toMap(TUnitConfig::getMaterialId,TUnitConfig::getCount));
        }
        return resMap;
    }
}
