package com.xsrw.wms.inout.domain.dto;

 import com.xsrw.wms.inout.domain.TRejectionDetail;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/9 11:10
 */
public class TRejectionDetailDTO extends TRejectionDetail {

    /**
     * 入库单号
     */
    private String advanceDeliveryCode;
    /**
     * 单据类型字典1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入
     */
    private String advanceDeliveryType;
    /**
     * 物料名称
     */
    private String materialName;

    public String getAdvanceDeliveryCode() {
        return advanceDeliveryCode;
    }

    public void setAdvanceDeliveryCode(String advanceDeliveryCode) {
        this.advanceDeliveryCode = advanceDeliveryCode;
    }

    public String getAdvanceDeliveryType() {
        return advanceDeliveryType;
    }

    public void setAdvanceDeliveryType(String advanceDeliveryType) {
        this.advanceDeliveryType = advanceDeliveryType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

}
