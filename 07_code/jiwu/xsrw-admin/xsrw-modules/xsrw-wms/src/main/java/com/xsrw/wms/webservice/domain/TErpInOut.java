package com.xsrw.wms.webservice.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * ERP-出入库信息对象 t_erp_in_out
 */
public class TErpInOut extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 装运类型 */
    @Excel(name = "装运类型")
    private String trart;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String zzywlx;

    /** 单据编码 */
    @Excel(name = "单据编码")
    private String zzdjbm;

    /** 单据行号 */
    @Excel(name = "单据行号")
    private String zzdjhh;

    /** 工厂 */
    @Excel(name = "工厂")
    private String werks;

    /** 库存地点 */
    @Excel(name = "库存地点")
    private String lgort;

    /** 仓库号 */
    @Excel(name = "仓库号")
    private String lgnum;

    /** 转移要求号 */
    @Excel(name = "转移要求号")
    private String tbnum;

    /** 行项目 */
    @Excel(name = "行项目")
    private String tbpos;

    /** 物料号 */
    @Excel(name = "物料号")
    private String matnr;

    /** 物料描述 */
    @Excel(name = "物料描述")
    private String maktx;

    /** 物料长描述 */
    @Excel(name = "物料长描述")
    private String maktg;

    /** 物料组 */
    @Excel(name = "物料组")
    private String matkl;

    /** 物料组描述 */
    @Excel(name = "物料组描述")
    private String wgbez;

    /** 批次 */
    @Excel(name = "批次")
    private String charg;

    /** 库存类别 */
    @Excel(name = "库存类别")
    private String bestq;

    /** 特殊库存标识 */
    @Excel(name = "特殊库存标识")
    private String sobkz;

    /** wbs元素(项目号） */
    @Excel(name = "wbs元素(项目号）")
    private String pspnr;

    /** 数量 */
    @Excel(name = "数量")
    private BigDecimal menge;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String meins;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal dmbtr;

    /** 供应商编号 */
    @Excel(name = "供应商编号")
    private String lifnr;

    /** 供应商描述 */
    @Excel(name = "供应商描述")
    private String name1;

    /** 采购订单号 */
    @Excel(name = "采购订单号")
    private String ebeln;

    /** 采购订单行项目 */
    @Excel(name = "采购订单行项目")
    private String ebelp;

    /** 领料单号 */
    @Excel(name = "领料单号")
    private String rsnum;

    /** 领料单行项目 */
    @Excel(name = "领料单行项目")
    private String rspos;

    /** 用户名 */
    @Excel(name = "用户名")
    private String usnamMkpf;

    /** 预留字段1 */
    @Excel(name = "预留字段1")
    private String zzyl1;

    /** 预留字段2 */
    @Excel(name = "预留字段2")
    private String zzyl2;

    /** 预留字段3 */
    @Excel(name = "预留字段3")
    private String zzyl3;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTrart(String trart) 
    {
        this.trart = trart;
    }

    public String getTrart() 
    {
        return trart;
    }
    public void setZzywlx(String zzywlx) 
    {
        this.zzywlx = zzywlx;
    }

    public String getZzywlx() 
    {
        return zzywlx;
    }
    public void setZzdjbm(String zzdjbm) 
    {
        this.zzdjbm = zzdjbm;
    }

    public String getZzdjbm() 
    {
        return zzdjbm;
    }
    public void setZzdjhh(String zzdjhh) 
    {
        this.zzdjhh = zzdjhh;
    }

    public String getZzdjhh() 
    {
        return zzdjhh;
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
    public void setLgnum(String lgnum) 
    {
        this.lgnum = lgnum;
    }

    public String getLgnum() 
    {
        return lgnum;
    }
    public void setTbnum(String tbnum) 
    {
        this.tbnum = tbnum;
    }

    public String getTbnum() 
    {
        return tbnum;
    }
    public void setTbpos(String tbpos) 
    {
        this.tbpos = tbpos;
    }

    public String getTbpos() 
    {
        return tbpos;
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
    public void setMaktg(String maktg) 
    {
        this.maktg = maktg;
    }

    public String getMaktg() 
    {
        return maktg;
    }
    public void setMatkl(String matkl) 
    {
        this.matkl = matkl;
    }

    public String getMatkl() 
    {
        return matkl;
    }
    public void setWgbez(String wgbez) 
    {
        this.wgbez = wgbez;
    }

    public String getWgbez() 
    {
        return wgbez;
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
    public void setSobkz(String sobkz) 
    {
        this.sobkz = sobkz;
    }

    public String getSobkz() 
    {
        return sobkz;
    }
    public void setPspnr(String pspnr) 
    {
        this.pspnr = pspnr;
    }

    public String getPspnr() 
    {
        return pspnr;
    }
    public void setMenge(BigDecimal menge) 
    {
        this.menge = menge;
    }

    public BigDecimal getMenge() 
    {
        return menge;
    }
    public void setMeins(String meins) 
    {
        this.meins = meins;
    }

    public String getMeins() 
    {
        return meins;
    }
    public void setDmbtr(BigDecimal dmbtr) 
    {
        this.dmbtr = dmbtr;
    }

    public BigDecimal getDmbtr() 
    {
        return dmbtr;
    }
    public void setLifnr(String lifnr) 
    {
        this.lifnr = lifnr;
    }

    public String getLifnr() 
    {
        return lifnr;
    }
    public void setName1(String name1) 
    {
        this.name1 = name1;
    }

    public String getName1() 
    {
        return name1;
    }
    public void setEbeln(String ebeln) 
    {
        this.ebeln = ebeln;
    }

    public String getEbeln() 
    {
        return ebeln;
    }
    public void setEbelp(String ebelp) 
    {
        this.ebelp = ebelp;
    }

    public String getEbelp() 
    {
        return ebelp;
    }
    public void setRsnum(String rsnum) 
    {
        this.rsnum = rsnum;
    }

    public String getRsnum() 
    {
        return rsnum;
    }
    public void setRspos(String rspos) 
    {
        this.rspos = rspos;
    }

    public String getRspos() 
    {
        return rspos;
    }
    public void setUsnamMkpf(String usnamMkpf) 
    {
        this.usnamMkpf = usnamMkpf;
    }

    public String getUsnamMkpf() 
    {
        return usnamMkpf;
    }
    public void setZzyl1(String zzyl1) 
    {
        this.zzyl1 = zzyl1;
    }

    public String getZzyl1() 
    {
        return zzyl1;
    }
    public void setZzyl2(String zzyl2) 
    {
        this.zzyl2 = zzyl2;
    }

    public String getZzyl2() 
    {
        return zzyl2;
    }
    public void setZzyl3(String zzyl3) 
    {
        this.zzyl3 = zzyl3;
    }

    public String getZzyl3() 
    {
        return zzyl3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("trart", getTrart())
            .append("zzywlx", getZzywlx())
            .append("zzdjbm", getZzdjbm())
            .append("zzdjhh", getZzdjhh())
            .append("werks", getWerks())
            .append("lgort", getLgort())
            .append("lgnum", getLgnum())
            .append("tbnum", getTbnum())
            .append("tbpos", getTbpos())
            .append("matnr", getMatnr())
            .append("maktx", getMaktx())
            .append("maktg", getMaktg())
            .append("matkl", getMatkl())
            .append("wgbez", getWgbez())
            .append("charg", getCharg())
            .append("bestq", getBestq())
            .append("sobkz", getSobkz())
            .append("pspnr", getPspnr())
            .append("menge", getMenge())
            .append("meins", getMeins())
            .append("dmbtr", getDmbtr())
            .append("lifnr", getLifnr())
            .append("name1", getName1())
            .append("ebeln", getEbeln())
            .append("ebelp", getEbelp())
            .append("rsnum", getRsnum())
            .append("rspos", getRspos())
            .append("usnamMkpf", getUsnamMkpf())
            .append("zzyl1", getZzyl1())
            .append("zzyl2", getZzyl2())
            .append("zzyl3", getZzyl3())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
