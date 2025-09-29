package com.xsrw.wms.check.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.check.domain.TTaskDetail;
import io.swagger.models.auth.In;

import java.math.BigDecimal;

/**
 * @description 盘点子表VO
 */
public class TaskDetailCheckVO{


   /**
     * 物料编码
     */
    @Excel(name = "物料",sort = 2)
    private Long materiaId;

    @Excel(name = "库区id",sort = 2)
    private Long reservoirid;

    @Excel(name = "账上库存",sort = 2)
    private Integer stock;

    @Excel(name = "任务详情id",sort = 2)
    private Long taskDetailId;

     @TableField(exist = false)
    private Long id;

    @Excel(name = "批号")
    private String batchNumber;



    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 实盘数量
     */
    private BigDecimal realyNum;

    private Long stockId;

    private Long trayId;

    private String materialCode;

    private Long materialId;

    private BigDecimal stockNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(Long taskDetailId) {
        this.taskDetailId = taskDetailId;
    }

    public Long getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(Long materiaId) {
        this.materiaId = materiaId;
    }


    public String getMaterialName() {
        return materialName;
    }

    public Long getReservoirid() {
        return reservoirid;
    }

    public void setReservoirid(Long reservoirid) {
        this.reservoirid = reservoirid;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getRealyNum() {
        return realyNum;
    }

    public void setRealyNum(BigDecimal realyNum) {
        this.realyNum = realyNum;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getStockNum() {
        return stockNum;
    }

    public void setStockNum(BigDecimal stockNum) {
        this.stockNum = stockNum;
    }
}
