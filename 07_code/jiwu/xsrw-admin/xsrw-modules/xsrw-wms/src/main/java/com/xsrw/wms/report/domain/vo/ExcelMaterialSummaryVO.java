package com.xsrw.wms.report.domain.vo;


import com.xsrw.common.core.annotation.Excel;

public class ExcelMaterialSummaryVO {
    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String code;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 累计入库数量
     */
    @Excel(name = "累计入库数量")
    private Long inDeliveryNum;

    /**
     * 累计出库数量
     */
    @Excel(name = "累计出库数量")
    private Long outDeliveryNum;

    /**
     * 库存结余数量
     */
    @Excel(name = "库存结余数量")
    private Long libraryCount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getInDeliveryNum() {
        return inDeliveryNum;
    }

    public void setInDeliveryNum(Long inDeliveryNum) {
        this.inDeliveryNum = inDeliveryNum;
    }

    public Long getOutDeliveryNum() {
        return outDeliveryNum;
    }

    public void setOutDeliveryNum(Long outDeliveryNum) {
        this.outDeliveryNum = outDeliveryNum;
    }

    public Long getLibraryCount() {
        return libraryCount;
    }

    public void setLibraryCount(Long libraryCount) {
        this.libraryCount = libraryCount;
    }

    @Override
    public String toString() {
        return "ExcelMaterialSummaryVO{" +
            "code='" + code + '\'' +
            ", materialName='" + materialName + '\'' +
            ", inDeliveryNum=" + inDeliveryNum +
            ", outDeliveryNum=" + outDeliveryNum +
            ", libraryCount=" + libraryCount +
            '}';
    }
}

