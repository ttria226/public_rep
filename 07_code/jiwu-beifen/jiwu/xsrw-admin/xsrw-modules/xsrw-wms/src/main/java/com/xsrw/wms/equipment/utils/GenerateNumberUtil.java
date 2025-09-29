package com.xsrw.wms.equipment.utils;


import com.xsrw.common.core.utils.DateUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.repeat;

@Component
public class GenerateNumberUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String generateNum(String type,Integer width){
        StringBuilder generateNum = new StringBuilder();
        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String dateStr = simpleDateFormat.format(date);
//        String dateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        generateNum .append(type).append(dateStr);
        String key = type+ ":" + dateStr;
        Long number = redisTemplate.opsForValue().increment(key, 1);
        generateNum .append(leftPad(""+number, width, '0'));
        return generateNum .toString();
    }
    public String generateNumMonth(String type,Integer width){
        StringBuilder generateNum = new StringBuilder();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        String dateStr = simpleDateFormat.format(date);
//        String dateStr = DateUtil.format(new Date(), "yyyyMM");
        generateNum .append(type).append(dateStr);
        String key = type+ ":" + dateStr;
        Long number = redisTemplate.opsForValue().increment(key, 1);
        generateNum .append(leftPad(""+number, width, '0'));
        return generateNum .toString();
    }


    public String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        } else {
            int pads = size - str.length();
            if (pads <= 0) {
                return str;
            } else {
                return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : repeat(padChar, pads).concat(str);
            }
        }
    }

    public String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        } else {
            if (StringUtils.isEmpty(padStr)) {
                padStr = " ";
            }

            int padLen = padStr.length();
            int strLen = str.length();
            int pads = size - strLen;
            if (pads <= 0) {
                return str;
            } else if (padLen == 1 && pads <= 8192) {
                return leftPad(str, size, padStr.charAt(0));
            } else if (pads == padLen) {
                return padStr.concat(str);
            } else if (pads < padLen) {
                return padStr.substring(0, pads).concat(str);
            } else {
                char[] padding = new char[pads];
                char[] padChars = padStr.toCharArray();

                for(int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }
}
