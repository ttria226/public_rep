package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TContactsUnit;
import com.xsrw.wms.base.mapper.TContactsUnitMapper;
import com.xsrw.wms.base.service.ITContactsUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 供应商Service业务层处理
 *
 * @author lyx
 * @date 2023-05-05
 */
@Service
public class TContactsUnitServiceImpl extends ServiceImpl<TContactsUnitMapper, TContactsUnit> implements ITContactsUnitService
{
    @Autowired
    private TContactsUnitMapper tContactsUnitMapper;


    /**
     * 查询供应商列表
     *
     * @param entity 供应商
     * @return 供应商
     */
    @Override
    public List<TContactsUnit> selectTContactsUnitList(TContactsUnit entity)
    {
        return tContactsUnitMapper.selectTContactsUnitList(entity);
    }

    /**
     * 查询供应商
     *
     * @param id 供应商主键
     * @return 供应商
     */
    @Override
    public TContactsUnit selectTContactsUnitById(Long id)
    {
        return tContactsUnitMapper.selectById(id);
    }

    /**
     * 新增供应商
     *
     * @param entity 供应商
     * @return 结果
     */
    @Override
    public AjaxResult insertTContactsUnit(TContactsUnit entity)
    {
        String phone = entity.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String phoneRegEx = "^1[3-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                return AjaxResult.error("联系电话格式错误");
            }
        }
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setCreateBy(SecurityUtils.getUsername());
        List<TContactsUnit> contactsUnitList = tContactsUnitMapper.selectList(Wrappers.lambdaQuery(TContactsUnit.class)
                .eq(TContactsUnit::getName,entity.getName())
                .eq(StringUtils.isNotNull(entity.getDeptId()),TContactsUnit::getDeptId,entity.getDeptId())
                .eq(TContactsUnit::getDelFlag, Constants.NO));
        if (StringUtils.isNotEmpty(contactsUnitList) && contactsUnitList.size() > 0) {
            return AjaxResult.error("该往来单位已存在");
        }
        return AjaxResult.success(tContactsUnitMapper.insert(entity));
    }

    /**
     * 修改供应商
     *
     * @param entity 供应商
     * @return 结果
     */
    @Override
    public AjaxResult updateTContactsUnit(TContactsUnit entity)
    {
        String phone = entity.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String phoneRegEx = "^1[3-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                return AjaxResult.error("联系电话格式错误");
            }
        }
        entity.setUpdateTime(DateUtils.getNowDate());
        entity.setUpdateBy(SecurityUtils.getUsername());
        TContactsUnit contactsUnit = tContactsUnitMapper.selectById(entity.getId());
        if (!contactsUnit.getName().equals(entity.getName())) {
            List<TContactsUnit> unitList = tContactsUnitMapper.selectList(Wrappers.lambdaQuery(TContactsUnit.class)
                    .eq(StringUtils.isNotNull(entity.getDeptId()),TContactsUnit::getDeptId,entity.getDeptId())
                    .eq(TContactsUnit::getName,entity.getName())
                    .eq(TContactsUnit::getDelFlag,Constants.NO));
            if (StringUtils.isNotEmpty(unitList) && unitList.size() > 0) {
                return AjaxResult.error("该往来单位已存在");
            }
        }
        return AjaxResult.success(tContactsUnitMapper.updateById(entity));
    }


    /**
     * 批量删除供应商
     *
     * @param ids 需要删除的供应商主键
     * @return 结果
     */
    @Override
    public int deleteTContactsUnitByIds(Long[] ids)
    {
        return tContactsUnitMapper.deleteTContactsUnitByIds(ids);
    }

    /**
     * 删除供应商信息
     *
     * @param id 供应商主键
     * @return 结果
     */
    @Override
    public int deleteTContactsUnitById(Long id)
    {
        return tContactsUnitMapper.deleteTContactsUnitById(id);
    }
}
