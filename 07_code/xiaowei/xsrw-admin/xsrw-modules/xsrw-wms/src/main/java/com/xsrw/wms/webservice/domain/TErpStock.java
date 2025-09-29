package com.xsrw.wms.webservice.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * ERP-库存对象 t_erp_stock
 */
public class TErpStock extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 工厂
     */
    @Excel(name = "工厂", readConverterExp = "5866=国能新朔铁路有限责任公司机务分公司物资工厂", sort = 1)
    private String werks;

    /**
     * 库存地点
     */
    @Excel(name = "库存地点",readConverterExp = "Z001=电力配件仓库,Z002=内燃配件仓库,Z003=通用材料仓库",sort = 1)
    private String lgort;

    /**
     * 仓库号
     */
    @Excel(name = "仓库号", sort = 1)
    private String lgnum;

    /**
     * 存储类型
     */
    @Excel(name = "存储类型", sort = 1)
    private String lgtyp;

    /**
     * 物料号
     */
    @Excel(name = "物料号", sort = 1)
    private String matnr;

    /**
     * 物料描述
     */
    @Excel(name = "物料描述", sort = 1)
    private String makt;

    /**
     * 批次
     */
    @Excel(name = "批次", sort = 1)
    private String charg;

    /**
     * 仓位
     */
//    @Excel(name = "仓位")
    private String lgpla;

    /**
     * 存储区
     */
    @Excel(name = "存储区")
    private String lgber;

    /**
     * 供应商
     */
    @Excel(name = "供应商")
    private String lifnr;

    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称")
    private String name1;

    /**
     * 仓位存量
     */
    @Excel(name = "仓位存量")
    private BigDecimal gesme;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private BigDecimal verpr;

    /**
     * 总价
     */
    @Excel(name = "总价")
    private BigDecimal salkv;

    /**
     * 计量单位
     */
    @Excel(name = "计量单位")
    private String meins;

    /**
     * 库存类别
     */
    @Excel(name = "库存类别")
    private String bestq;

    /**
     * 特殊库存标识
     */
    @Excel(name = "特殊库存标识")
    private String sobkz;

    /**
     * 特殊库存编号
     */
    @Excel(name = "特殊库存编号")
    private String posid;

    /**
     * 预留字段1
     */
    @Excel(name = "预留字段1")
    private String field1;

    /**
     * 预留字段2
     */
    @Excel(name = "预留字段2")
    private String field2;

    /**
     * 预留字段3
     */
    @Excel(name = "预留字段3")
    private String field3;

    /**
     * 预留字段4
     */
    @Excel(name = "预留字段4")
    private String field4;

    /**
     * 预留字段5
     */
    @Excel(name = "预留字段5")
    private String field5;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getWerks() {
        return werks;
    }

    public void setLgort(String lgort) {
        this.lgort = lgort;
    }

    public String getLgort() {
        return lgort;
    }

    public void setLgnum(String lgnum) {
        this.lgnum = lgnum;
    }

    public String getLgnum() {
        return lgnum;
    }

    public void setLgtyp(String lgtyp) {
        this.lgtyp = lgtyp;
    }

    public String getLgtyp() {
        return lgtyp;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMakt(String makt) {
        this.makt = makt;
    }

    public String getMakt() {
        return makt;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public String getCharg() {
        return charg;
    }

    public void setLgpla(String lgpla) {
        this.lgpla = lgpla;
    }

    public String getLgpla() {
        return lgpla;
    }

    public void setLgber(String lgber) {
        this.lgber = lgber;
    }

    public String getLgber() {
        return lgber;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName1() {
        return name1;
    }

    public void setGesme(BigDecimal gesme) {
        this.gesme = gesme;
    }

    public BigDecimal getGesme() {
        return gesme;
    }

    public void setVerpr(BigDecimal verpr) {
        this.verpr = verpr;
    }

    public BigDecimal getVerpr() {
        return verpr;
    }

    public void setSalkv(BigDecimal salkv) {
        this.salkv = salkv;
    }

    public BigDecimal getSalkv() {
        return salkv;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getMeins() {
        return meins;
    }

    public void setBestq(String bestq) {
        this.bestq = bestq;
    }

    public String getBestq() {
        return bestq;
    }

    public void setSobkz(String sobkz) {
        this.sobkz = sobkz;
    }

    public String getSobkz() {
        return sobkz;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public String getPosid() {
        return posid;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField1() {
        return field1;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField2() {
        return field2;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField3() {
        return field3;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField4() {
        return field4;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField5() {
        return field5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("werks", getWerks())
                .append("lgort", getLgort())
                .append("lgnum", getLgnum())
                .append("lgtyp", getLgtyp())
                .append("matnr", getMatnr())
                .append("makt", getMakt())
                .append("charg", getCharg())
                .append("lgpla", getLgpla())
                .append("lgber", getLgber())
                .append("lifnr", getLifnr())
                .append("name1", getName1())
                .append("gesme", getGesme())
                .append("verpr", getVerpr())
                .append("salkv", getSalkv())
                .append("meins", getMeins())
                .append("bestq", getBestq())
                .append("sobkz", getSobkz())
                .append("posid", getPosid())
                .append("field1", getField1())
                .append("field2", getField2())
                .append("field3", getField3())
                .append("field4", getField4())
                .append("field5", getField5())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
