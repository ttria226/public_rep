package com.xsrw.wms.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.model.LoginUser;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.task.domain.TPerson;
import com.xsrw.wms.task.mapper.TPersonMapper;
import com.xsrw.wms.task.service.ITPersonService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 人员Service业务层处理
 *
 * @author zjj
 * @date 2023-06-29
 */
@Service
public class TPersonServiceImpl extends ServiceImpl<TPersonMapper, TPerson> implements ITPersonService
{
    @Resource
    private TPersonMapper tPersonMapper;


    /**
     * 查询人员列表
     *
     * @param tPerson 人员
     * @return 人员
     */
    @Override
    public List<TPerson> selectTPersonList(TPerson tPerson)
    {
        return tPersonMapper.selectTPersonList(tPerson);
    }

    /**
     * 查询人员
     *
     * @param id 人员主键
     * @return 人员
     */
    @Override
    public AjaxResult selectTPersonById(Long id)
    {
        TPerson tPerson = tPersonMapper.selectById(id);
        if (tPerson==null||Constants.DEL_FLAG_YES.equals(tPerson.getDelFlag())){
            return AjaxResult.error("人员不存在");
        }
        return AjaxResult.success(tPerson);
    }

    /**
     * 新增人员
     *
     * @param tPerson 人员
     * @return 结果
     */
    @Override
    public AjaxResult insertTPerson(TPerson tPerson)
    {
        String phone = tPerson.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String phoneRegEx = "^1[3-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                return AjaxResult.error("手机号格式错误");
            }
        }
        List<TPerson> personList = tPersonMapper.selectList(Wrappers.<TPerson>lambdaQuery()
                .eq(TPerson::getName, tPerson.getName())
                .eq(TPerson::getDelFlag, Constants.DEL_FLAG_NO));
        if (!CollectionUtils.isEmpty(personList)){
            return AjaxResult.error("该人员已存在");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        tPerson.setCreateTime(new Date());
        if (loginUser!=null){
            tPerson.setCreateBy(loginUser.getUsername());
            tPerson.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
            tPerson.setDeptName(SecurityUtils.getLoginUser().getSysUser().getDept().getDeptName());
        }

        return AjaxResult.success(tPersonMapper.insert(tPerson));
    }

    /**
     * 修改人员
     *
     * @param tPerson 人员
     * @return 结果
     */
    @Override
    public AjaxResult updateTPerson(TPerson tPerson)
    {
        String phone = tPerson.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String phoneRegEx = "^1[3-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                return AjaxResult.error("手机号格式错误");
            }
        }
        List<TPerson> personList = tPersonMapper.selectList(Wrappers.<TPerson>lambdaQuery()
                .eq(TPerson::getName, tPerson.getName())
                .not(tPersonLambdaQueryWrapper -> tPersonLambdaQueryWrapper.eq(TPerson::getId, tPerson.getId()))
                .eq(TPerson::getDelFlag, Constants.DEL_FLAG_NO));
        if (!CollectionUtils.isEmpty(personList)){
            return AjaxResult.error("该人员已存在");
        }
        tPerson.setUpdateBy(SecurityUtils.getUsername());
        tPerson.setUpdateTime(new Date());
        return AjaxResult.success(tPersonMapper.updateById(tPerson));
    }


    /**
     * 批量删除人员
     *
     * @param ids 需要删除的人员主键
     * @return 结果
     */
    @Override
    public int deleteTPersonByIds(Long[] ids)
    {
        return tPersonMapper.deleteTPersonByIds(ids);
    }

    /**
     * 删除人员信息
     *
     * @param id 人员主键
     * @return 结果
     */
    @Override
    public int deleteTPersonById(Long id)
    {
        return tPersonMapper.deleteTPersonById(id);
    }
}
