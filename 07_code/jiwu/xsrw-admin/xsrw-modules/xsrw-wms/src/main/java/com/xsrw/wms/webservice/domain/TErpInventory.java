package com.xsrw.wms.webservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * ERP-盘点单对象 t_erp_inventory
 */
public class TErpInventory extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 盘点凭证 */
    @Excel(name = "盘点凭证")
    private String ivnum;

    /** 盘点时间 */
    @Excel(name = "盘点时间")
    private String pdatu;

    /** 创建人 */
    @Excel(name = "创建人")
    private String uname;

    /** 仓库号 */
    @Excel(name = "仓库号")
    private String lgnum;

    /** 工厂 */
    @Excel(name = "工厂")
    private String werks;

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
    public void setLgnum(String lgnum) 
    {
        this.lgnum = lgnum;
    }

    public String getLgnum() 
    {
        return lgnum;
    }
    public void setWerks(String werks) 
    {
        this.werks = werks;
    }

    public String getWerks() 
    {
        return werks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ivnum", getIvnum())
            .append("pdatu", getPdatu())
            .append("uname", getUname())
            .append("lgnum", getLgnum())
            .append("werks", getWerks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
