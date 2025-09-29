package com.xsrw.wms.base.domain.dto;

import com.xsrw.wms.base.domain.TRulePutaway;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/13 11:08
 */
public class TRulePutawayDTO extends TRulePutaway {

    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 详情列表
     */
    private List<Long> detailList;

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

    public List<Long> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Long> detailList) {
        this.detailList = detailList;
    }
}
