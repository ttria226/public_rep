package com.xsrw.gateway.filter;

import com.xsrw.common.core.constant.CacheConstants;
import com.xsrw.common.core.constant.HttpStatus;
import com.xsrw.common.core.constant.SecurityConstants;
import com.xsrw.common.core.constant.TokenConstants;
import com.xsrw.common.core.context.SecurityContextHolder;
import com.xsrw.common.core.utils.JwtUtils;
import com.xsrw.common.core.utils.ServletUtils;
import com.xsrw.common.core.utils.StringUtils;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.gateway.config.properties.IgnoreWhiteProperties;
import com.xsrw.gateway.config.properties.RepeatSubmitURIProperties;
import com.xsrw.gateway.config.properties.SelfProperties;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 网关鉴权
 *
 * @author zjj
 */
@Component
@ConditionalOnProperty(value = "security.repeatsubmit.enabled", havingValue = "true")
public class RepeatSubmitFilter implements GlobalFilter, Ordered
{
    private static final Logger log = LoggerFactory.getLogger(RepeatSubmitFilter.class);

    // 校验重复提交配置，nacos自行添加
    @Autowired
    private RepeatSubmitURIProperties repeatSubmitUri;
    // nacos配置
    @Autowired
    private SelfProperties selfProperties;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
//        log.info("[防重复提交过滤]开始！");
        long timeMillis = System.currentTimeMillis();

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();

        // 获取x-www-form-urlencoded参数
        Mono<MultiValueMap<String, String>> formDataMono = exchange.getFormData();

        String url = request.getURI().getPath();

        // 验证的配置路径
        if (StringUtils.matches(url, repeatSubmitUri.getUri()))
        {
            String userkey = request.getHeaders().getFirst(SecurityConstants.USER_KEY);
            String userid = request.getHeaders().getFirst(SecurityConstants.DETAILS_USER_ID);
            String username = request.getHeaders().getFirst(SecurityConstants.DETAILS_USERNAME);
            String first = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);
            Long userId = SecurityContextHolder.getUserId();

//            MultiValueMap<String, String> queryParams = request.getQueryParams();

            String redisKey = generateRedisKey(url, first, Long.valueOf(userid));
            log.info(redisKey);

            if(redisService.hasKey(redisKey)){
                log.info("[防重复提交过滤]请求重复提交！耗时：" + (System.currentTimeMillis() - timeMillis) + "毫秒");
                return repeatSubmitResponse(exchange, "请求重复提交！");
            }
            if(!redisService.setCacheObjectIfAbsent(redisKey,System.currentTimeMillis(), selfProperties.getRepeatsubmitTime(), TimeUnit.MILLISECONDS)){
                log.info("[防重复提交过滤]请求重复提交了！耗时：" + (System.currentTimeMillis() - timeMillis) + "毫秒");
                return repeatSubmitResponse(exchange, "请求重复提交！");
            }

//            log.info("[防重复提交过滤]无重复提交，耗时：" + (System.currentTimeMillis() - timeMillis) + "毫秒");
            Mono<Void> filter = chain.filter(exchange);
//            try {
//                Thread.sleep(200L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            redisService.deleteObject(redisKey);
//            log.info("[防重复提交过滤]请求处理完成，耗时：" + (System.currentTimeMillis() - timeMillis) + "毫秒");
            return filter;
        }

        return chain.filter(exchange);

    }


    private String generateRedisKey(String url, String token ,Long userId) {
        // 将请求路径、请求参数和当前登录人拼接成唯一的Redis Key
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(":");
        sb.append(userId);
        sb.append(":");
        sb.append(token);
//        if(!CollectionUtils.isEmpty(queryParams)) {
//            // 将请求参数按照一定的规则组合成Redis Key
//            List<String> paramList = new ArrayList<>();
//            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
//                List<String> values = entry.getValue();
//                if(CollectionUtils.isEmpty(values)) {
//                    for (String value : values) {
//                        paramList.add(entry.getKey() + "=" + value);
//                    }
//                }else{
//                    paramList.add(entry.getKey() + "=" + values);
//                }
//            }
//            // 对键值对列表进行排序 防止顺序变化 拦截不到
//            Collections.sort(paramList);
//            // 将排序后的键值对列表转换为字符串并添加到Redis Key中
//            sb.append(String.join("&", paramList));
//
//        }

        return sb.toString();
    }

    private Mono<Void> repeatSubmitResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[重复提交处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.REPEATSUBMIT);
    }

    @Override
    public int getOrder()
    {
        return Integer.MAX_VALUE;
    }
}
