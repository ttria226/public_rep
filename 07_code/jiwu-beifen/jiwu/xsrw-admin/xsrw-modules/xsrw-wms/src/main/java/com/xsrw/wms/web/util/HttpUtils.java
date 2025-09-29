package com.xsrw.wms.web.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xsrw.common.core.web.domain.AjaxResult;
import com.xsrw.wms.web.domain.WcsResultEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类
 *
 * @author wxr
 * @date 2023/10/17 17:08
 */

public class HttpUtils {

    public static String get(String url) {
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param params
     * @return
     */
    public static Map<String,Object> post(String url, JsonObject params) {
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("orderId",null);
        resMap.put("acceptData","");
        resMap.put("errorMessage","");
        resMap.put("sendData",params.toString());
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        System.out.println("============参数：" + params.toString());
        post.setEntity(new StringEntity(params.toString(), "UTF-8"));
        HttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String st = EntityUtils.toString(entity, "UTF-8");
            resMap.put("acceptData",st);
            System.out.println("============结果：" + st);
            JSONObject map = JSONObject.parseObject(st);
            if (map != null && map.get("data") != null) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("data");
                if (CollectionUtils.isNotEmpty(list)) {
                    //订单内部id（当in_order_id>0表示创建订单成功）
                    Long orderId = Long.valueOf(list.get(0).get("in_order_id").toString());
                    if (orderId > 0) {
                        resMap.put("orderId",orderId);
                    }
                }else{
                    resMap.put("errorMessage",map.get("msg"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            resMap.put("acceptData",e.getMessage());
        }
        return resMap;

    }

}