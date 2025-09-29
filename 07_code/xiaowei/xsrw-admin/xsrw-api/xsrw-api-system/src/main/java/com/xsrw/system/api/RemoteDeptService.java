package com.xsrw.system.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.xsrw.common.core.constant.SecurityConstants;
import com.xsrw.common.core.constant.ServiceNameConstants;
import com.xsrw.common.core.domain.R;
import com.xsrw.system.api.domain.SysDept;
import com.xsrw.system.api.domain.SysUser;
import com.xsrw.system.api.factory.RemoteDeptFallbackFactory;
import com.xsrw.system.api.factory.RemoteUserFallbackFactory;
import com.xsrw.system.api.model.LoginUser;

/**
 * 用户服务
 * 
 * @author zjj
 */
@FeignClient(contextId = "remoteDeptService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDeptFallbackFactory.class)
public interface RemoteDeptService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/dept/findDept")
    public R<SysDept> getDeptInfo(SysDept sysDept, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

   
}
