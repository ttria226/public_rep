package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 入库任务详情对象 t_task_in
 *
 * @author wxr
 * @date 2023-05-09
 */
@TableName("t_task_in")
public class TTaskIn extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登记标识
     */
    @Excel(name = "登记标识")
    private Long advanceRegistrationId;

    /**
     * 物料标识
     */
    @Excel(name = "物料标识")
    private Long materialId;

    /**
     * 计划数量
     */
    @Excel(name = "计划数量")
    private BigDecimal predictCount;

    /**
     * 实际数量
     */
    @Excel(name = "实际数量")
    private BigDecimal actualCount;

    /**
     * 状态(0未完成，1进行中 2已完成 )
     */
    @Excel(name = "状态(0未完成，1进行中 2已完成 )")
    private String status;

    /**
     * 载具
     */
    @Excel(name = "载具")
    private Long trayId;

    /**
     * 库区
     */
    @Excel(name = "库区")
    private Long reservoirId;

    /**
     * 库位
     */
    @Excel(name = "库位")
    private Long locationId;

    /**
     * 推荐库位
     */
    @Excel(name = "推荐库位")
    private Long recommendLocationId;

    /**
     * 区域
     */
    @Excel(name = "区域")
    private Long areaId;

    /**
     * 批次号
     */
    @Excel(name = "批次号")
    private String batchCode;

    /**
     * 库存
     */
    @Excel(name = "库存")
    private Long stockId;

    /**
     * wcs任务标识
     */
    @Excel(name = "wcs任务标识")
    private Long wcsId;

    /**
     * 载具状态
     */
    @TableField(exist = false)
    private String trayStatus;

    /**
     * 上架状态（0部分1全部）
     */
    @TableField(exist = false)
    private String actualFlag;

    /**
     * rfid
     */
    @TableField(exist = false)
    private List<String> rfIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvanceRegistrationId() {
        return advanceRegistrationId;
    }

    public void setAdvanceRegistrationId(Long advanceRegistrationId) {
        this.advanceRegistrationId = advanceRegistrationId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getRecommendLocationId() {
        return recommendLocationId;
    }

    public void setRecommendLocationId(Long recommendLocationId) {
        this.recommendLocationId = recommendLocationId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getWcsId() {
        return wcsId;
    }

    public void setWcsId(Long wcsId) {
        this.wcsId = wcsId;
    }

    public String getTrayStatus() {
        return trayStatus;
    }

    public void setTrayStatus(String trayStatus) {
        this.trayStatus = trayStatus;
    }

    public String getActualFlag() {
        return actualFlag;
    }

    public void setActualFlag(String actualFlag) {
        this.actualFlag = actualFlag;
    }

    public List<String> getRfIds() {
        return rfIds;
    }

    public void setRfIds(List<String> rfIds) {
        this.rfIds = rfIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("advanceRegistrationId", getAdvanceRegistrationId())
                .append("materialId", getMaterialId())
                .append("predictCount", getPredictCount())
                .append("actualCount", getActualCount())
                .append("status", getStatus())
                .append("trayId", getTrayId())
                .append("reservoirId", getReservoirId())
                .append("locationId", getLocationId())
                .append("recommendLocationId", getRecommendLocationId())
                .append("areaId", getAreaId())
                .append("batchCode", getBatchCode())
                .append("stockId", getStockId())
                .append("wcsId", getWcsId())
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
