package com.xsrw.gateway.config.properties;

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
@ConfigurationProperties(prefix = "security.repeatsubmit")
public class RepeatSubmitURIProperties
{
    /**
     * 校验重复提交配置
     */
    private List<String> uri = new ArrayList<>();

    public List<String> getUri() {
        return uri;
    }

    public void setUri(List<String> uri) {
        this.uri = uri;
    }
}
