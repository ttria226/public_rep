package com.xsrw.wms.inout.domain;

import java.util.List;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 入库单对象 t_out_delivery
 *
 * @author zyq
 * @date 2023-05-09
 */
@TableName("t_out_delivery")
public class TOutDelivery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 编号 */
    @Excel(name = "出库单号",sort = 1)
    private String code;

    /** 源单单号 */
    private String originCode;

    /** 源单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Excel(name = "源单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date originDate;

    /** 源单内容 */
//    @Excel(name = "源单内容")
    private String originData;

    /** 单据类型字典（1调拨出库 2销售出库  3领用出库） */
    @Excel(name = "出库类型", sort = 1, readConverterExp = "1=调拨出库,2=销售出库,3=领用出库,4=系统生成")
    private String type;

//   @Excel(name = "单据类型字典", readConverterExp = "是否转为出库任务(0待转为出库任务，1 已转为出库任务)")
    private String nextFlag;

    /** 订单状态（1待审核 2.审核通过  9.审核不通过） */
    @Excel(name = "状态", sort = 4, readConverterExp = "1=待审核,2=审核通过,6=部分退货,7=全部退货,9=审核不通过,10=已分配,11=部分出库,12=已出库,8=已作废")
    private String status;

    @Excel(name = "出库状态", sort = 4, readConverterExp = "1=未完成,2=部分完成,3=已完成")
    private String completeState;

    /** 审核人 */
    @Excel(name = "审核人", sort = 4)
    private String auditor;

    /** 来源字典（1.本地创建 2.erp接口 3.调拨单） */
    @Excel(name = "来源", sort = 3, readConverterExp = "1=本地创建,2=erp接口,3=调拨单")
    private String newLocal;

    /** 单据来源  1本地单据 2调拨单 **/
    private String deliveryModule;

    /** bom标识 **/
    private Long bomId;

    /** bom套数 **/
    private Integer bomCount;

    /**
     * 库区id
     */
    private Long reservoirId;

    /** 是否回库（0否1是） */
    private String refluxStatus;

    /** 审核备注 **/
    private String checkRemark;

//    /** 入库单详情信息 */
    @TableField(exist = false)
    private List<TOutDeliveryDetail> tOutDeliveryDetailList;


    public String getCompleteState() {
        return completeState;
    }

    public void setCompleteState(String completeState) {
        this.completeState = completeState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOriginCode() {
        return originCode;
    }

    public void setOriginCode(String originCode) {
        this.originCode = originCode;
    }

    public Date getOriginDate() {
        return originDate;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public String getOriginData() {
        return originData;
    }

    public void setOriginData(String originData) {
        this.originData = originData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getNewLocal() {
        return newLocal;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public List<TOutDeliveryDetail> gettOutDeliveryDetailList() {
        return tOutDeliveryDetailList;
    }

    public void settOutDeliveryDetailList(List<TOutDeliveryDetail> tOutDeliveryDetailList) {
        this.tOutDeliveryDetailList = tOutDeliveryDetailList;
    }

    public String getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(String nextFlag) {
        this.nextFlag = nextFlag;
    }

    public String getDeliveryModule() {
        return deliveryModule;
    }

    public void setDeliveryModule(String deliveryModule) {
        this.deliveryModule = deliveryModule;
    }

    public Long getBomId() {
        return bomId;
    }

    public void setBomId(Long bomId) {
        this.bomId = bomId;
    }

    public Integer getBomCount() {
        return bomCount;
    }

    public void setBomCount(Integer bomCount) {
        this.bomCount = bomCount;
    }

    public String getRefluxStatus() {
        return refluxStatus;
    }

    public void setRefluxStatus(String refluxStatus) {
        this.refluxStatus = refluxStatus;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public void setCheckRemark(String checkRemark) {
        this.checkRemark = checkRemark;
    }


    @Override
    public String toString() {
        return "TOutDelivery{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", originCode='" + originCode + '\'' +
                ", originDate=" + originDate +
                ", originData='" + originData + '\'' +
                ", type='" + type + '\'' +
                ", nextFlag='" + nextFlag + '\'' +
                ", status='" + status + '\'' +
                ", completeState='" + completeState + '\'' +
                ", auditor='" + auditor + '\'' +
                ", newLocal='" + newLocal + '\'' +
                ", deliveryModule='" + deliveryModule + '\'' +
                ", bomId=" + bomId +
                ", bomCount=" + bomCount +
                ", reservoirId=" + reservoirId +
                ", refluxStatus='" + refluxStatus + '\'' +
                ", checkRemark='" + checkRemark + '\'' +
                ", tOutDeliveryDetailList=" + tOutDeliveryDetailList +
                '}';
    }
}
