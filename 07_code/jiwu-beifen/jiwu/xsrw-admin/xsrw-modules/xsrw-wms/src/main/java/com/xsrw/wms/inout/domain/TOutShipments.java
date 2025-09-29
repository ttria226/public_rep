package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库发货单对象 t_out_shipments
 *
 * @author wxr
 * @date 2023-06-07
 */
@TableName("t_out_shipments")
public class TOutShipments extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 源单id
     */
    @Excel(name = "源单id")
    private Long originId;

    /**
     * 状态（0.已复核 1.已确认 2.已作废）
     */
    @Excel(name = "状态", readConverterExp = "0=未发货,1=已确认,2=已作废")
    private String status;

    /**
     * 目的地
     */
    @Excel(name = "目的地")
    private String destination;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;

    /**
     * 运输方式（1汽车2火车）
     */
    @Excel(name = "运输方式", readConverterExp = "1=汽车,2=火车")
    private String shippingType;

    /**
     * 车牌/车次号
     */
    @Excel(name = "车牌/车次号")
    private String plateNumber;

    /**
     * 发货日期
     */
    @Excel(name = "发货日期")
    private String shipmentDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setShipmentDate(String shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public String getShipmentDate() {
        return shipmentDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("originId", getOriginId())
                .append("status", getStatus())
                .append("destination", getDestination())
                .append("customerName", getCustomerName())
                .append("shippingType", getShippingType())
                .append("plateNumber", getPlateNumber())
                .append("shipmentDate", getShipmentDate())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
