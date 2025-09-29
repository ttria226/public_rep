package com.xsrw.wms.base.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * rfid打印记录对象 t_material_detail_print
 *
 * @author wxr
 * @date 2023-11-09
 */
@TableName("t_material_detail_print")
public class TMaterialDetailPrint extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 入库单详情标识
     */
    @Excel(name = "入库单详情标识")
    private Long advanceRegistrationId;

    /**
     * 打印次数
     */
    @Excel(name = "打印次数")
    private Integer printCount;

    /**
     * 打印时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "打印时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date printTime;

    /**
     * 最小单位
     */
    @Excel(name = "最小单位")
    private Long minUnit;

    /**
     * 最小单位名称
     */
    @Excel(name = "最小单位名称")
    private String minUnitName;

    /**
     * 最大单位
     */
    @Excel(name = "最大单位")
    private Long maxUnit;

    /**
     * 最大单位名称
     */
    @Excel(name = "最大单位名称")
    private String maxUnitName;

    /**
     * 转换数量
     */
    @Excel(name = "转换数量")
    private Integer convertCount;
    /**
     * 打印数量
     */
    private Integer sumCount;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;
    /**
     * 关联的rfid分组编号，多个,分割
     */
    private String rfidHeads;


    /**
     * 打印类型  1 RFID打印、2普通标签打印
     */
    private String printType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setAdvanceRegistrationId(Long advanceRegistrationId) {
        this.advanceRegistrationId = advanceRegistrationId;
    }

    public Long getAdvanceRegistrationId() {
        return advanceRegistrationId;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public Date getPrintTime() {
        return printTime;
    }

    public void setMinUnit(Long minUnit) {
        this.minUnit = minUnit;
    }

    public Long getMinUnit() {
        return minUnit;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMaxUnit(Long maxUnit) {
        this.maxUnit = maxUnit;
    }

    public Long getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnitName(String maxUnitName) {
        this.maxUnitName = maxUnitName;
    }

    public String getMaxUnitName() {
        return maxUnitName;
    }

    public Integer getConvertCount() {
        return convertCount;
    }

    public void setConvertCount(Integer convertCount) {
        this.convertCount = convertCount;
    }

    public Integer getSumCount() {
        return sumCount;
    }

    public void setSumCount(Integer sumCount) {
        this.sumCount = sumCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getRfidHeads() {
        return rfidHeads;
    }

    public void setRfidHeads(String rfidHeads) {
        this.rfidHeads = rfidHeads;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }


    @Override
    public String toString() {
        return "TMaterialDetailPrint{" +
                "id=" + id +
                ", advanceRegistrationId=" + advanceRegistrationId +
                ", printCount=" + printCount +
                ", printTime=" + printTime +
                ", minUnit=" + minUnit +
                ", minUnitName='" + minUnitName + '\'' +
                ", maxUnit=" + maxUnit +
                ", maxUnitName='" + maxUnitName + '\'' +
                ", convertCount=" + convertCount +
                ", sumCount=" + sumCount +
                ", status='" + status + '\'' +
                ", rfidHeads='" + rfidHeads + '\'' +
                ", printType='" + printType + '\'' +
                '}';
    }
}
