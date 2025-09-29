package com.xsrw.wms.base.common.util;

import com.xsrw.common.core.text.Convert;
import com.xsrw.common.redis.service.RedisService;
import com.xsrw.wms.base.common.Constants;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/20 10:43
 */
@Component
public class SendEmailUtil {

    @Autowired
    private RedisService redisService;

    /**
     * 邮件发送
     * @param emailAddress
     * @param title
     * @param content
     */
    public void sendEmail(String[] emailAddress, String title, String content) {
        String hostName = "smtp.qq.com";
        String from = Convert.toStr(redisService.getCacheObject(Constants.EMAIL_ADDRESS), "");
        String password = Convert.toStr(redisService.getCacheObject(Constants.EMAIL_PWD), "");

        try {
            //支持HTML格式
            HtmlEmail email = new HtmlEmail();
            // 邮箱服务器地址（这里使用的是163邮箱、需要开通POP3/SMTP服务）
            email.setHostName(hostName);
            // 编码格式
            email.setCharset("utf-8");
            // 接收地址（我给注册人发送邮件作为验证码）
            email.addTo(emailAddress);
            // 邮件的发送人（作者自己的邮箱）
            email.setFrom(from);
            // 发送人邮箱的授权密码（可以自己开通自己的163邮箱权限，需要获取序列码）
            email.setAuthentication(from, password);
            // 设置邮箱标题
            email.setSubject(title);
            // 邮箱内容
            email.setHtmlMsg(content);
            // 邮件发送
            email.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
