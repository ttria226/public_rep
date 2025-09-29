package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TMaterial;

import java.math.BigDecimal;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 15:52
 */
public class TMaterialSelectVO {
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
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 单位名称
     */
    @Excel(name = "单位")
    private String unitName;

     @Excel(name = "单位")
    private String minUnitName;

    private Long unitId;

    private Integer count;

    private BigDecimal stock;

    private BigDecimal materialTray;

    private BigDecimal materiaLocal;

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public BigDecimal getMaterialTray() {
        return materialTray;
    }

    public void setMaterialTray(BigDecimal materialTray) {
        this.materialTray = materialTray;
    }

    public BigDecimal getMateriaLocal() {
        return materiaLocal;
    }

    public void setMateriaLocal(BigDecimal materiaLocal) {
        this.materiaLocal = materiaLocal;
    }
}
