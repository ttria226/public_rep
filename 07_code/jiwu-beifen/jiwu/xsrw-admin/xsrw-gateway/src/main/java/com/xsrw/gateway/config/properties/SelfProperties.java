package com.xsrw.gateway.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 *
 * @author zjj
 */
@Configuration
@RefreshScope
public class SelfProperties
{
    /**
     * 校验重复提交配置
     */
    @Value("${security.repeatsubmit.time}")
    private Long repeatsubmitTime ;

    public Long getRepeatsubmitTime()
    {
        return repeatsubmitTime;
    }

    public void setRepeatsubmitTime(Long repeatsubmitTime)
    {
        this.repeatsubmitTime = repeatsubmitTime;
    }
}
