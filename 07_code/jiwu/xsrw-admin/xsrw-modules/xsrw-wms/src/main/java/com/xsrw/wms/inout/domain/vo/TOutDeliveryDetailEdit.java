package com.xsrw.wms.inout.domain.vo;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库单详情对象 t_out_delivery_detail
 * 
 * @author zyq
 * @date 2023-05-09
 */
public class TOutDeliveryDetailEdit extends BaseEntity
{
    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;
    /** 本次预计数量 */
    @Excel(name = "本次预计数量")
    private Long predictCount;

    /** 小件预计数量 */
    @Excel(name = "小件预计数量")
    private Long smallPredictCount;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(Long smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    @Override
    public String toString() {
        return "TOutDeliveryDetailEdit{" +
                "materialId=" + materialId +
                ", predictCount=" + predictCount +
                ", smallPredictCount=" + smallPredictCount +
                '}';
    }
}
