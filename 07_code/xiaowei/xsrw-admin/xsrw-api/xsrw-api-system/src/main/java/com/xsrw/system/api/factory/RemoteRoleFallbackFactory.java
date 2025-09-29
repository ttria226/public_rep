package com.xsrw.system.api.factory;

import com.xsrw.common.core.domain.R;
import com.xsrw.system.api.RemoteRoleService;
import com.xsrw.system.api.domain.SysRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 角色服务降级处理
 * 
 * @author zjj
 */
@Component
public class RemoteRoleFallbackFactory implements FallbackFactory<RemoteRoleService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteRoleFallbackFactory.class);

    @Override
    public RemoteRoleService create(Throwable throwable)
    {
        log.error("角色服务调用失败:{}", throwable.getMessage());
        return new RemoteRoleService()
        {
            @Override
            public R<Long> saveRole(String paramCacheKey, String source) {
                return R.fail("保存角色失败:" + throwable.getMessage());
            }

            @Override
            public R editRole(String paramCacheKey, String source) {
                return R.fail("编辑角色失败:" + throwable.getMessage());
            }

            @Override
            public R changeRoleStatus(SysRole sysRole, String source) {
                return R.fail("修改角色状态失败:" + throwable.getMessage());
            }

            @Override
            public R removeRole(Long[] roleIds, String source) {
                return R.fail("删除角色失败失败:" + throwable.getMessage());
            }
        };
    }
}
