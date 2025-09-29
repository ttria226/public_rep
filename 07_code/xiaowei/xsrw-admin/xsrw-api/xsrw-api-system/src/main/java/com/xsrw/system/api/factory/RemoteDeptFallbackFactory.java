package com.xsrw.system.api.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.xsrw.common.core.domain.R;
import com.xsrw.system.api.RemoteDeptService;
import com.xsrw.system.api.RemoteUserService;
import com.xsrw.system.api.domain.SysDept;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.system.api.model.LoginUser;

/**
 * 部门服务降级处理
 * 
 * @author zjj
 */
@Component
public class RemoteDeptFallbackFactory implements FallbackFactory<RemoteDeptService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteDeptFallbackFactory.class);

    @Override
    public RemoteDeptService create(Throwable throwable)
    {
        log.error("部门服务调用失败:{}", throwable.getMessage());
        return new RemoteDeptService()
        {
            @Override
            public R<SysDept> getDeptInfo(SysDept dept,String source)
            {
                return R.fail("获取部门失败:" + throwable.getMessage());
            }

        };
    }
}
