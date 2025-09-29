package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TRejectionDetail;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/9 11:32
 */
public class TRejectionDetailVO extends TRejectionDetail {

    /**
     * 入库单号
     */
    @Excel(name = "入库单号", sort = 1)
    private String advanceDeliveryCode;
    /**
     * 入库类型
     */
    @Excel(name = "入库类型", sort = 6, readConverterExp = "1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入")
    private String advanceDeliveryType;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 3)
    private String materialCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;
    /**
     * 物料单位名称
     */
    @Excel(name = "计量单位", sort = 5)
    private String unitName;

    /**
     * 创建者
     */
    @Excel(name = "检测人", sort = 9)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "检测时间", sort = 10, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 状态
     */
    @Excel(name = "状态", sort = 8, defaultValue = "检测不通过")
    private String status;

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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
