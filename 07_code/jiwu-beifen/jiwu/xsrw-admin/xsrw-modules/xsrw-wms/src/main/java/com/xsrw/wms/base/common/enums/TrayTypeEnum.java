package com.xsrw.wms.base.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum TrayTypeEnum {

    //料箱
    WORKBIN("2", 600L, 400L, 280L, "600mm（L）*400mm（W）*280mm(H)（总高度≤280mm）"),
    //托盘
    STOCK("1", 600L, 400L, null, "600mm（L）*400mm（W）"),
    //料笼
    CAGE("3", 1000L, 1200L, 1200L, "1000mm（L）*1200mm（W）*1200mm(H)（总高度≤1200mm");


    private final String value;
    private final Long length;
    private final Long width;
    private final Long height;
    private final String description;

    TrayTypeEnum(String value, Long length, Long width, Long height, String description) {
        this.value = value;
        this.length = length;
        this.width = width;
        this.height = height;
        this.description = description;
    }

    public static Map<String, TrayTypeEnum> getAllMap() {
        Map<String, TrayTypeEnum> resMap = new HashMap<>();
        TrayTypeEnum[] carTypeEnums = values();
        for (TrayTypeEnum carTypeEnum : carTypeEnums) {
            resMap.put(carTypeEnum.value, carTypeEnum);
        }
        return resMap;
    }

    public static Integer compareTrayType(Long len, Long wid, Long heig) {
        String value = "0";
        len = len == null ? 0 : len;
        wid = wid == null ? 0 : wid;
        heig = heig == null ? 0 : heig;
        TrayTypeEnum[] carTypeEnums = values();
        for (TrayTypeEnum trayTypeEnum : carTypeEnums) {
            if (len <= trayTypeEnum.length && wid <= trayTypeEnum.width && (trayTypeEnum.height == null || (trayTypeEnum.height != null && heig <= trayTypeEnum.height))) {
                value = trayTypeEnum.value;
                break;
            }
        }
        return Integer.valueOf(value);
    }

    public static Boolean compareTrayType(String type, Long len, Long wid, Long heig) {
        len = len == null ? 0 : len;
        wid = wid == null ? 0 : wid;
        heig = heig == null ? 0 : heig;
        TrayTypeEnum[] carTypeEnums = values();
        for (TrayTypeEnum trayTypeEnum : carTypeEnums) {
            if (trayTypeEnum.value.equals(type)) {
                if (len <= trayTypeEnum.length && wid <= trayTypeEnum.width && (trayTypeEnum.height == null || (trayTypeEnum.height != null && heig <= trayTypeEnum.height))) {
                    return true;
                }
            }

        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public Long getLength() {
        return length;
    }

    public Long getWidth() {
        return width;
    }

    public Long getHeight() {
        return height;
    }

    public String getDescription() {
        return description;
    }
}
