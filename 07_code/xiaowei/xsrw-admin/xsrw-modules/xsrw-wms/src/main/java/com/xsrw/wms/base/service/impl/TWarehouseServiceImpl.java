package com.xsrw.wms.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankcs.hanlp.HanLP;
import com.xsrw.common.core.constant.SecurityConstants;
import com.xsrw.common.core.domain.R;
import com.xsrw.common.core.exception.ServiceException;
import com.xsrw.common.core.utils.DateUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.common.security.utils.SecurityUtils;
import com.xsrw.system.api.RemoteRoleService;
import com.xsrw.system.api.domain.SysRole;
import com.xsrw.system.api.model.LoginUser;
import com.xsrw.wms.base.common.Constants;
import com.xsrw.wms.base.domain.TWarehouse;
import com.xsrw.wms.base.mapper.TWarehouseMapper;
import com.xsrw.wms.base.service.ITWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 仓库Service业务层处理
 *
 * @author zjj
 * @date 2023-07-12
 */
@Service
public class TWarehouseServiceImpl extends ServiceImpl<TWarehouseMapper, TWarehouse> implements ITWarehouseService
{
    @Resource
    private TWarehouseMapper tWarehouseMapper;

    @Resource
    private RemoteRoleService remoteRoleService;

    @Autowired
    private RedisService redisService;
    /**
     * 查询仓库列表
     *
     * @param tWarehouse 仓库
     * @return 仓库
     */
    @Override
    public List<TWarehouse> selectTWarehouseList(TWarehouse tWarehouse)
    {
        return tWarehouseMapper.selectTWarehouseList(tWarehouse);
    }

    /**
     * 查询仓库
     *
     * @param id 仓库主键
     * @return 仓库
     */
    @Override
    public AjaxResult selectTWarehouseById(Long id)
    {
        TWarehouse tWarehouse = tWarehouseMapper.selectTWarehouse(id);
        if (tWarehouse==null||!Constants.DEL_FLAG_NO.equals(tWarehouse.getDelFlag())){
            return AjaxResult.error("仓库不存在");
        }
        return AjaxResult.success(tWarehouse);
    }

    /**
     * 新增仓库
     *
     * @param entity 仓库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertTWarehouse(TWarehouse entity)
    {
        //添加仓库
        saveWareHouse(entity);
        //添加角色
        R<Long> rResult = saveRole(entity);
        if (R.SUCCESS != rResult.getCode())
        {
            throw new ServiceException(rResult.getMsg());
        }
        //设置仓库roleID
        this.update(Wrappers.<TWarehouse>lambdaUpdate().eq(TWarehouse::getId,entity.getId()).set(TWarehouse::getRoleId,rResult.getData()));
        return AjaxResult.success();
    }

    /**
     * 添加角色
     * @param entity
     * @return
     */
    private R<Long> saveRole(TWarehouse entity){
        SysRole sysRole = new SysRole();
        sysRole.setDeptCheckStrictly(true);
        sysRole.setMenuCheckStrictly(entity.isMenuCheckStrictly());
        sysRole.setRoleSort(0);
        sysRole.setStatus("0");
        sysRole.setRoleName(entity.getName()+"-角色");
        sysRole.setRoleKey(HanLP.convertToPinyinString(sysRole.getRoleName(),"", false));
        sysRole.setMenuIds(entity.getMenuIds());
        String paramCacheKey = Constants.ROLE_SAVE_PARAM_KEY + sysRole.getRoleKey();
        redisService.setCacheObject(paramCacheKey,sysRole,10L, TimeUnit.SECONDS);
        return remoteRoleService.saveRole(paramCacheKey, SecurityConstants.INNER);
    }



    /**
     * 添加仓库
     * @param entity
     * @return
     */
    private TWarehouse saveWareHouse(TWarehouse entity) {
        verifyPhone(entity.getPhone());
        entity.setCreateTime(DateUtils.getNowDate());
        entity.setCreateBy(SecurityUtils.getUsername());
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser!=null){
            entity.setCreateBy(loginUser.getUsername());
            entity.setDeptId(SecurityUtils.getLoginUser().getSysUser().getDeptId());
            entity.setDeptName(SecurityUtils.getLoginUser().getSysUser().getDept().getDeptName());
        }
        List<TWarehouse> warehouseList = tWarehouseMapper.selectList(Wrappers.lambdaQuery(TWarehouse.class)
                .eq(TWarehouse::getName, entity.getName())
                .eq(TWarehouse::getDelFlag, Constants.DEL_FLAG_NO));
        if (!CollectionUtils.isEmpty(warehouseList)) {
            throw new ServiceException("仓库已存在");
        }
        this.save(entity);
        return entity;
    }

    /**
     * 修改仓库
     *
     * @param entity 仓库
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateTWarehouse(TWarehouse entity)
    {
        //编辑仓库
        editWarehouse(entity);
        //编辑角色
        R rResult = updateRole(entity);
        if (R.FAIL == rResult.getCode())
        {
            throw new ServiceException(rResult.getMsg());
        }
        return AjaxResult.success();
    }

    /**
     * 编辑角色
     * @param entity
     * @return
     */
    private R updateRole(TWarehouse entity){
        SysRole sysRole = new SysRole();
        sysRole.setMenuCheckStrictly(entity.isMenuCheckStrictly());
        sysRole.setStatus(entity.getStatus());
        sysRole.setRoleName(entity.getName()+"-角色");
        sysRole.setRoleKey(HanLP.convertToPinyinString(sysRole.getRoleName(),"", false));
        sysRole.setMenuIds(entity.getMenuIds());
        sysRole.setRoleId(entity.getRoleId());
        sysRole.setUpdateBy(SecurityUtils.getUsername());
        sysRole.setRoleId(entity.getRoleId());
        String paramCacheKey = Constants.ROLE_UPDATE_PARAM_KEY + sysRole.getRoleKey();
        redisService.setCacheObject(paramCacheKey,sysRole,10L, TimeUnit.SECONDS);
        return remoteRoleService.editRole(paramCacheKey, SecurityConstants.INNER);
    }

    /**
     * 编辑仓库
     * @param entity
     */
    private void editWarehouse(TWarehouse entity) {
        verifyPhone(entity.getPhone());
        TWarehouse exist = tWarehouseMapper.selectById(entity.getId());
        if (exist==null||Constants.DEL_FLAG_YES.equals(exist.getDelFlag())){
            throw new ServiceException("仓库不存在");
        }
        if (!exist.getName().equals(entity.getName())){
            List<TWarehouse> warehouseList = tWarehouseMapper.selectList(Wrappers.lambdaQuery(TWarehouse.class)
                    .eq(TWarehouse::getName, entity.getName())
                    .eq(TWarehouse::getDelFlag, Constants.DEL_FLAG_NO));
            if (!CollectionUtils.isEmpty(warehouseList)) {
                throw new ServiceException("仓库已存在");
            }
        }
        entity.setRoleId(exist.getRoleId());
        this.update(Wrappers.<TWarehouse>lambdaUpdate()
                .eq(TWarehouse::getId, entity.getId())
                .set(TWarehouse::getUpdateBy,SecurityUtils.getUsername())
                .set(TWarehouse::getUpdateTime,DateUtils.getNowDate())
                .set(TWarehouse::getName, entity.getName())
                .set(TWarehouse::getStatus, entity.getStatus())
                .set(TWarehouse::getPhone, entity.getPhone())
                .set(TWarehouse::getContactPerson, entity.getContactPerson())
                .set(TWarehouse::getRemark, entity.getRemark()));
    }

    /**
     * 手机号校验
     * @param phone
     */
    private void verifyPhone(String phone) {
        if (StringUtils.isNotEmpty(phone)){
            String phoneRegEx = "^1[3-9][0-9]{9}$";
            Pattern pattern = Pattern.compile(phoneRegEx);
            Matcher matcher = pattern.matcher(phone);
            if (!matcher.matches()) {
                throw new ServiceException("手机号格式错误");
            }
        }
    }


    /**
     * 批量删除仓库
     *
     * @param ids 需要删除的仓库主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteTWarehouseByIds(Long[] ids)
    {
        List<TWarehouse> warehouseList = tWarehouseMapper.selectList(Wrappers.<TWarehouse>lambdaQuery().eq(TWarehouse::getDelFlag, Constants.DEL_FLAG_NO)
                .in(TWarehouse::getId, ids));
        if (CollectionUtils.isEmpty(warehouseList)) {
            throw new ServiceException("参数错误");
        }
        //删除仓库
        tWarehouseMapper.deleteTWarehouseByIds(ids);

        R rResult = remoteRoleService.removeRole(warehouseList.stream().map(TWarehouse::getRoleId).distinct().collect(Collectors.toList()).toArray(new Long[]{}), SecurityConstants.INNER);
        if (R.SUCCESS != rResult.getCode())
        {
            throw new ServiceException(rResult.getMsg());
        }
        return AjaxResult.success();
    }

    /**
     * 删除仓库信息
     *
     * @param id 仓库主键
     * @return 结果
     */
    @Override
    public int deleteTWarehouseById(Long id)
    {
        return tWarehouseMapper.deleteTWarehouseById(id);
    }

    /**
     * 修改状态(启用/禁用)
     * @param tWarehouse
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult changeStatus(TWarehouse tWarehouse) {
        TWarehouse warehouse = tWarehouseMapper.selectById(tWarehouse.getId());
        if (warehouse==null||Constants.DEL_FLAG_YES.equals(warehouse.getDelFlag())){
            throw new ServiceException("仓库不存在");
        }
        if (warehouse.getStatus().equals(tWarehouse.getStatus())){
            throw new ServiceException("参数错误");
        }
        //编辑仓库
        this.update(Wrappers.<TWarehouse>lambdaUpdate()
                .eq(TWarehouse::getId,tWarehouse.getId())
                .set(TWarehouse::getStatus,tWarehouse.getStatus()));
        //修改对应的角色和用户的状态
        R rResult = changeRoleStatus(warehouse.getRoleId(), tWarehouse.getStatus());
        if (R.SUCCESS != rResult.getCode())
        {
            throw new ServiceException(rResult.getMsg());
        }
        return AjaxResult.success();
    }

    /**
     * 修改对应的角色和用户的状态
     * @param roleId
     * @param status
     * @return
     */
    private R changeRoleStatus(Long roleId, String status) {
        SysRole sysRole=new SysRole();
        sysRole.setRoleId(roleId);
        sysRole.setStatus(status);
        return remoteRoleService.changeRoleStatus(sysRole, SecurityConstants.INNER);
    }
}
