package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物料入库详情对象 t_material_detail
 *
 * @author wxr
 * @date 2023-05-11
 */
@TableName("t_material_detail")
public class TMaterialDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 编码
     */
    @Excel(name = "编码")
    private String code;

    /**
     * 入库单详情标识
     */
    private Long advanceRegistrationId;

    /**
     * 物料标识
     */
    private Long materialId;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称")
    private String materialName;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码")
    private String materialCode;

    /**
     * rfid唯一码
     */
    @Excel(name = "rfid")
    private String rfid;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    /**
     * 托盘标识
     */
    private Long trayId;

    /**
     * 库位标识
     */
    private Long locationId;

    /**
     * 状态保留字段(可能是：是否损坏 )
     */
    private String status;

    /**
     * 小件已使用数量
     */
    private Long useCount;

    /**
     * 物料重量（kg）
     */
    @Excel(name = "物料重量（kg）")
    private Double weight;

    /**
     * 物料价格（元）
     */
    @Excel(name = "物料价格（元）")
    private Double price;

    /**
     * 小单位数量
     */
    @Excel(name = "小单位数量")
    private Long smallUnitCount;

    /**
     * 检测失败类型
     */
    private String detectionFailType;

    /**
     * 检测失败备注
     */
    private String detectionFailRemark;

    /**
     * 检测失败状态（0未检测，1检测成功，2检测失败）
     */
//    @Excel(name = "检测失败状态", readConverterExp = "0=未检测，1检测成功，2检测失败")
    private String detectionFailStatus;

    private Long outDeliveryDetailId;

    /**
     * rfid分组编号
     */
    private String rfidHead;


    /**
     * rfid对应数量
     */
    private BigDecimal rfidCount;
    /**
     * 入库扫描状态（0未扫描、1已扫描）
     */
    private  Integer rukuSaomiao;
    /**
     * 出库扫描状态（0未扫描、1已扫描）
     */
    private  Integer chukuSaomiao;
    /**
     * 出库扫描状态正常、异常（0正常、1异常）
     */
    private  Integer chukuSaomiaoFlag;
    /**
     * 入库扫描时间
     */
    private Date rukuSaomiaoTime;
    /**
     * 出库扫描时间
     */
    private Date chukuSaomiaoTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Integer getRukuSaomiao(){ return  rukuSaomiao; }

    public Date getRukuSaomiaoTime(){ return  rukuSaomiaoTime; }
    public Integer getChukuSaomiaoFlag(){ return  chukuSaomiaoFlag; }
    public  void  setChukuSaomiaoFlag(Integer chukuSaomiaoFlag){ this.chukuSaomiaoFlag=chukuSaomiaoFlag; }
    public Integer getChukuSaomiao(){ return  chukuSaomiao; }
    public  void  setChukuSaomiao(Integer chukuSaomiao){ this.chukuSaomiao=chukuSaomiao; }
    public void setChukuSaomiaoTime(Date chukuSaomiaoTime){ this.chukuSaomiaoTime=chukuSaomiaoTime; }
    public Date getChukuSaomiaoTime(){ return  chukuSaomiaoTime; }

    public void setAdvanceRegistrationId(Long advanceRegistrationId) {
        this.advanceRegistrationId = advanceRegistrationId;
    }

    public Long getAdvanceRegistrationId() {
        return advanceRegistrationId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getRfid() {
        return rfid;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setUseCount(Long useCount) {
        this.useCount = useCount;
    }

    public Long getUseCount() {
        return useCount;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSmallUnitCount(Long smallUnitCount) {
        this.smallUnitCount = smallUnitCount;
    }

    public Long getSmallUnitCount() {
        return smallUnitCount;
    }

    public void setDetectionFailType(String detectionFailType) {
        this.detectionFailType = detectionFailType;
    }

    public String getDetectionFailType() {
        return detectionFailType;
    }

    public void setDetectionFailRemark(String detectionFailRemark) {
        this.detectionFailRemark = detectionFailRemark;
    }

    public String getDetectionFailRemark() {
        return detectionFailRemark;
    }

    public void setDetectionFailStatus(String detectionFailStatus) {
        this.detectionFailStatus = detectionFailStatus;
    }

    public String getDetectionFailStatus() {
        return detectionFailStatus;
    }

    public Long getOutDeliveryDetailId() {
        return outDeliveryDetailId;
    }

    public void setOutDeliveryDetailId(Long outDeliveryDetailId) {
        this.outDeliveryDetailId = outDeliveryDetailId;
    }

    public String getRfidHead() {
        return rfidHead;
    }

    public void setRfidHead(String rfidHead) {
        this.rfidHead = rfidHead;
    }

    public BigDecimal getRfidCount() {
        return rfidCount;
    }

    public void setRfidCount(BigDecimal rfidCount) {
        this.rfidCount = rfidCount;
    }

    public void setRukuSaomiao(Integer rukuSaomiao){this.rukuSaomiao=rukuSaomiao;}

    public void setRukuSaomiaoTime(Date rukuSaomiaoTime){this.rukuSaomiaoTime=rukuSaomiaoTime; }
    @Override
    public String toString() {
        return "TMaterialDetail{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", advanceRegistrationId=" + advanceRegistrationId +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", rfid='" + rfid + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", trayId=" + trayId +
                ", locationId=" + locationId +
                ", status='" + status + '\'' +
                ", useCount=" + useCount +
                ", weight=" + weight +
                ", price=" + price +
                ", smallUnitCount=" + smallUnitCount +
                ", detectionFailType='" + detectionFailType + '\'' +
                ", detectionFailRemark='" + detectionFailRemark + '\'' +
                ", detectionFailStatus='" + detectionFailStatus + '\'' +
                ", outDeliveryDetailId=" + outDeliveryDetailId +
                ", rfidHead='" + rfidHead + '\'' +
                ", rfidCount=" + rfidCount +
                '}';
    }
}
