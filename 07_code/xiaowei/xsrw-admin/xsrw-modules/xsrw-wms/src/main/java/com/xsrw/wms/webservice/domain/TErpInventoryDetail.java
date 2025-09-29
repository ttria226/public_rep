package com.xsrw.wms.webservice.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * ERP-盘点单明细对象 t_erp_inventory_detail
 */
public class TErpInventoryDetail extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 盘点凭证 */
    @Excel(name = "盘点凭证")
    private String ivnum;

    /** 行项目号 */
    @Excel(name = "行项目号")
    private String ivpos;

    /** 仓库号 */
    @Excel(name = "仓库号")
    private String lgnum;

    /** 仓储类型 */
    @Excel(name = "仓储类型")
    private String lgtyp;

    /** 仓储类型名称 */
    @Excel(name = "仓储类型名称")
    private String ltypt;

    /** 仓位 */
    @Excel(name = "仓位")
    private String lgpla;

    /** 工厂 */
    @Excel(name = "工厂")
    private String werks;

    /** 库存地点 */
    @Excel(name = "库存地点")
    private String lgort;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String matnr;

    /** 物料描述 */
    @Excel(name = "物料描述")
    private String maktx;

    /** 批次 */
    @Excel(name = "批次")
    private String charg;

    /** 库存状态 */
    @Excel(name = "库存状态")
    private String bestq;

    /** 在库数量 */
    @Excel(name = "在库数量")
    private BigDecimal gesme;

    /** 基本计量单位 */
    @Excel(name = "基本计量单位")
    private String meins;

    /** 特殊库存标识 */
    @Excel(name = "特殊库存标识")
    private String sobkz;

    /** 盘点日期 */
    @Excel(name = "盘点日期")
    private String pdatu;

    /** 分配人 */
    @Excel(name = "分配人")
    private String uname;

    /** wbs元素显示编号 */
    @Excel(name = "wbs元素显示编号")
    private String psPosnr;

    /** wbs元素的描述 */
    @Excel(name = "wbs元素的描述")
    private String psPspt;

    /** 预留字段1 */
    @Excel(name = "预留字段1")
    private String field1;

    /** 预留字段2 */
    @Excel(name = "预留字段2")
    private String field2;

    /** 预留字段3 */
    @Excel(name = "预留字段3")
    private String field3;

    /** 预留字段4 */
    @Excel(name = "预留字段4")
    private String field4;

    /** 预留字段5 */
    @Excel(name = "预留字段5")
    private String field5;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setIvnum(String ivnum) 
    {
        this.ivnum = ivnum;
    }

    public String getIvnum() 
    {
        return ivnum;
    }
    public void setIvpos(String ivpos) 
    {
        this.ivpos = ivpos;
    }

    public String getIvpos() 
    {
        return ivpos;
    }
    public void setLgnum(String lgnum) 
    {
        this.lgnum = lgnum;
    }

    public String getLgnum() 
    {
        return lgnum;
    }
    public void setLgtyp(String lgtyp) 
    {
        this.lgtyp = lgtyp;
    }

    public String getLgtyp() 
    {
        return lgtyp;
    }
    public void setLtypt(String ltypt) 
    {
        this.ltypt = ltypt;
    }

    public String getLtypt() 
    {
        return ltypt;
    }
    public void setLgpla(String lgpla) 
    {
        this.lgpla = lgpla;
    }

    public String getLgpla() 
    {
        return lgpla;
    }
    public void setWerks(String werks) 
    {
        this.werks = werks;
    }

    public String getWerks() 
    {
        return werks;
    }
    public void setLgort(String lgort) 
    {
        this.lgort = lgort;
    }

    public String getLgort() 
    {
        return lgort;
    }
    public void setMatnr(String matnr) 
    {
        this.matnr = matnr;
    }

    public String getMatnr() 
    {
        return matnr;
    }
    public void setMaktx(String maktx) 
    {
        this.maktx = maktx;
    }

    public String getMaktx() 
    {
        return maktx;
    }
    public void setCharg(String charg) 
    {
        this.charg = charg;
    }

    public String getCharg() 
    {
        return charg;
    }
    public void setBestq(String bestq) 
    {
        this.bestq = bestq;
    }

    public String getBestq() 
    {
        return bestq;
    }
    public void setGesme(BigDecimal gesme) 
    {
        this.gesme = gesme;
    }

    public BigDecimal getGesme() 
    {
        return gesme;
    }
    public void setMeins(String meins) 
    {
        this.meins = meins;
    }

    public String getMeins() 
    {
        return meins;
    }
    public void setSobkz(String sobkz) 
    {
        this.sobkz = sobkz;
    }

    public String getSobkz() 
    {
        return sobkz;
    }
    public void setPdatu(String pdatu) 
    {
        this.pdatu = pdatu;
    }

    public String getPdatu() 
    {
        return pdatu;
    }
    public void setUname(String uname) 
    {
        this.uname = uname;
    }

    public String getUname() 
    {
        return uname;
    }
    public void setPsPosnr(String psPosnr) 
    {
        this.psPosnr = psPosnr;
    }

    public String getPsPosnr() 
    {
        return psPosnr;
    }
    public void setPsPspt(String psPspt) 
    {
        this.psPspt = psPspt;
    }

    public String getPsPspt() 
    {
        return psPspt;
    }
    public void setField1(String field1) 
    {
        this.field1 = field1;
    }

    public String getField1() 
    {
        return field1;
    }
    public void setField2(String field2) 
    {
        this.field2 = field2;
    }

    public String getField2() 
    {
        return field2;
    }
    public void setField3(String field3) 
    {
        this.field3 = field3;
    }

    public String getField3() 
    {
        return field3;
    }
    public void setField4(String field4) 
    {
        this.field4 = field4;
    }

    public String getField4() 
    {
        return field4;
    }
    public void setField5(String field5) 
    {
        this.field5 = field5;
    }

    public String getField5() 
    {
        return field5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ivnum", getIvnum())
            .append("ivpos", getIvpos())
            .append("lgnum", getLgnum())
            .append("lgtyp", getLgtyp())
            .append("ltypt", getLtypt())
            .append("lgpla", getLgpla())
            .append("werks", getWerks())
            .append("lgort", getLgort())
            .append("matnr", getMatnr())
            .append("maktx", getMaktx())
            .append("charg", getCharg())
            .append("bestq", getBestq())
            .append("gesme", getGesme())
            .append("meins", getMeins())
            .append("sobkz", getSobkz())
            .append("pdatu", getPdatu())
            .append("uname", getUname())
            .append("psPosnr", getPsPosnr())
            .append("psPspt", getPsPspt())
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
