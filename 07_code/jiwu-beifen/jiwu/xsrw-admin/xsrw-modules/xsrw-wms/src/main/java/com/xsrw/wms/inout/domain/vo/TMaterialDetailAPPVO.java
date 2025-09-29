package com.xsrw.wms.inout.domain.vo;

import java.util.List;

/**
 * 物料入库详情对象 t_material_detail
 */
public class TMaterialDetailAPPVO {
    private static final long serialVersionUID = 1L;


    private Long materialId;

    private String materialName;

    private List<String> rfids;

    private List<String> rfidHeads;//编号

    private String batchCode;//批次号

    private int count;//数量

    private int expectedCount;//预计数量

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public List<String> getRfids() {
        return rfids;
    }

    public void setRfids(List<String> rfids) {
        this.rfids = rfids;
    }

    public List<String> getRfidHeads() {
        return rfidHeads;
    }

    public void setRfidHeads(List<String> rfidHeads) {
        this.rfidHeads = rfidHeads;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getExpectedCount() {
        return expectedCount;
    }

    public void setExpectedCount(int expectedCount) {
        this.expectedCount = expectedCount;
    }
}
