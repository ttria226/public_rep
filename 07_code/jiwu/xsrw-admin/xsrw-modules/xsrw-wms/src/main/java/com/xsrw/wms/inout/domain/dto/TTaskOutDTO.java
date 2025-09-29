package com.xsrw.wms.inout.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 出库任务详情对象 t_task_out
 *
 * @author zyq
 * @date 2023-05-08
 */
public class TTaskOutDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

   /** 出库单详情标识 */
    @Excel(name = "出库单详情标识")
    private String outDeliveryDetailId;

    @Excel(name = "库存")
    private Long stockId;

   /** 在库数量 */
   @Excel(name = "在库数量")
   private Long count;

   /** 物料标识 */
   @Excel(name = "物料标识")
   private Long materialId;

    private Long receiveCount;
   /**
    * 物料编码
    */
   private String materialCode;
   /**
    * 物料名称
    */
   private String materialName;
   /**
    * 物料单位名称
    */
   private String unitName;

   private String smallUnitName;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchCode;
    /** 本次预计数量 */
    @Excel(name = "本次预计数量")
    private BigDecimal predictCount;
    /**
     * 可用数量
     */
    private BigDecimal availableCount;

    /** 小件预计数量 */
    @Excel(name = "小件预计数量")
    private Long smallPredictCount;
    /** 载具 */
    @Excel(name = "载具")
    private Long trayId;

    @Excel(name = "状态")
    private Long status;

   /** 载具 */
    @Excel(name = "载具")
    private String trayCode;

    /** 库位 */
    @Excel(name = "库位")
    private Long locationId;

    /** 库位 */
    @Excel(name = "库位")
    private String locationName;

   @Excel(name = "库区")
    private Long reservoirId;

    /** 库位 */
    @Excel(name = "库区")
    private String reservoirName;

    @Excel(name = "库区")
    private String areaName;

    /**
     * 实际数量
     */
    private BigDecimal actualCount;

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getSmallUnitName() {
        return smallUnitName;
    }

    public void setSmallUnitName(String smallUnitName) {
        this.smallUnitName = smallUnitName;
    }

    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public Long getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(Long smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getAreaName() {
  return areaName;
 }

 public void setAreaName(String areaName) {
  this.areaName = areaName;
 }

 public String getOutDeliveryDetailId() {
     return outDeliveryDetailId;
    }

    public void setOutDeliveryDetailId(String outDeliveryDetailId) {
     this.outDeliveryDetailId = outDeliveryDetailId;
    }

    public Long getStockId() {
     return stockId;
    }

    public void setStockId(Long stockId) {
     this.stockId = stockId;
    }

    public Long getCount() {
     return count;
    }

    public void setCount(Long count) {
     this.count = count;
    }

    public Long getMaterialId() {
     return materialId;
    }

    public void setMaterialId(Long materialId) {
     this.materialId = materialId;
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

    public String getBatchCode() {
     return batchCode;
    }

    public void setBatchCode(String batchCode) {
     this.batchCode = batchCode;
    }

    public Long getTrayId() {
     return trayId;
    }

    public void setTrayId(Long trayId) {
     this.trayId = trayId;
    }

    public BigDecimal getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(BigDecimal availableCount) {
        this.availableCount = availableCount;
    }

    public String getTrayCode() {
     return trayCode;
    }

    public void setTrayCode(String trayCode) {
     this.trayCode = trayCode;
    }

    public Long getLocationId() {
     return locationId;
    }

    public void setLocationId(Long locationId) {
     this.locationId = locationId;
    }

    public String getLocationName() {
     return locationName;
    }

    public void setLocationName(String locationName) {
     this.locationName = locationName;
    }

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
    }
}
