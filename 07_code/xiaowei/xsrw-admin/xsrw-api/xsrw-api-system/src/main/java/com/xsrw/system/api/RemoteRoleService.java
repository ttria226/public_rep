package com.xsrw.system.api;

import com.xsrw.common.core.constant.SecurityConstants;
import com.xsrw.common.core.constant.ServiceNameConstants;
import com.xsrw.common.core.domain.R;
import com.xsrw.system.api.domain.SysRole;
import com.xsrw.system.api.factory.RemoteRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 角色服务
 * 
 * @author tyk
 */
@FeignClient(contextId = "remoteRoleService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteRoleFallbackFactory.class)
public interface RemoteRoleService
{
    /**
     * 保存角色
     * @param paramCacheKey 参数缓存key
     * @param source 请求来源
     * @return
     */
    @PostMapping("/role/saveRole")
    public R<Long> saveRole(@RequestParam(value = "paramCacheKey") String paramCacheKey, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 编辑角色
     * @param paramCacheKey 参数缓存key
     * @param source 请求来源
     * @return
     */
    @PostMapping("/role/editRole")
    public R editRole(@RequestParam(value = "paramCacheKey") String paramCacheKey, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 修改角色状态
     * @param sysRole 角色信息
     * @param source 请求来源
     * @return
     */
    @PostMapping("/role/changeRoleStatus")
    public R changeRoleStatus(@RequestBody SysRole sysRole, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 删除角色
     * @param roleIds
     * @param source 请求来源
     * @return
     */
    @DeleteMapping("/role/removeRole")
    public R removeRole(@RequestParam(value = "roleIds")  Long[] roleIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


}
