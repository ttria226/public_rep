package com.xsrw.wms.base.common.enums;

public enum CodeEnum {

    IWL("IWL", "物料"),
    IWLD("IWLD", "物料详情"),
    MTP("MTP", "托盘管理"),
    IHJ("IHJ", "货架"),
    IKW("IKW", "库位"),
    BOM("B", "BOM"),

    CRW("CRW", "wcs任务"),

    MRK("MRK", "入库单"),
    MYK("MYK", "越库单"),
    CKJH("CKJH", "出库计划"),
    MKNYW("MKNYW", "库内移位"),
    IKQ("IKQ", "库区"),
    MBC("MBC","波次单"),
    MDB("MDB","调拨单"),
    SBN("SBN","保养单号"),

    MRWLB("MRWLB", "任务列表"),
    ;
    private final String codeName;
    private final String codeDesc;


    public static String getValue(String type) {
        CodeEnum[] carTypeEnums = values();
        for (CodeEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.codeName.equals(type)) {
                return carTypeEnum.codeDesc;
            }
        }
        return null;
    }

    public static String getType(String desc) {
        CodeEnum[] carTypeEnums = values();
        for (CodeEnum carTypeEnum : carTypeEnums) {
            if (carTypeEnum.codeDesc.equals(desc)) {
                return carTypeEnum.codeName;
            }
        }
        return null;
    }


    private CodeEnum(String codeName, String codeDesc) {
        this.codeName = codeName;
        this.codeDesc = codeDesc;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getCodeDesc() {
        return codeDesc;
    }
}
