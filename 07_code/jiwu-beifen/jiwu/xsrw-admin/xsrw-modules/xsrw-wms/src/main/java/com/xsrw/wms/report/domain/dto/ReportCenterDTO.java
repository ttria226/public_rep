package com.xsrw.wms.report.domain.dto;

/**
 * @Description: 报表中心 查询DTO
 * @Author XMING
 * @Date 2022-06-08
 */
public class ReportCenterDTO {

    /**
     * 物料编码
     * **/
    private String materialCode;

    /**
     * 物料名称
     * **/
    private String materialName;

    /**
     * 仓库
     * **/
    private String warehouseId;

    /**
     * 开始时间
     * **/
    private String beginDate;

    /**
     * 结束时间
     * **/
    private String endDate;


    private String materialId;


    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}
