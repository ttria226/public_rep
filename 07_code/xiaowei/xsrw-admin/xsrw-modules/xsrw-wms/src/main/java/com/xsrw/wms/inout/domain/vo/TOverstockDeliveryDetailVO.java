package com.xsrw.wms.inout.domain.vo;

import com.xsrw.wms.inout.domain.TOverstockDeliveryDetail;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/25 15:55
 */
public class TOverstockDeliveryDetailVO extends TOverstockDeliveryDetail {

    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料单位名称
     */
    private String unitName;

    /**
     * 财务凭证号
     */
    private String financeVoucherNo;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFinanceVoucherNo() {
        return financeVoucherNo;
    }

    public void setFinanceVoucherNo(String financeVoucherNo) {
        this.financeVoucherNo = financeVoucherNo;
    }
}
