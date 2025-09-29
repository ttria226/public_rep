package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TAllot;

import java.util.Date;

/**
 * @Description: 调拨单VO
 * @Author XMING
 * @Date 2023-06-27
 */
public class TAllotVO extends TAllot {

    /** 物料名称 **/
    @Excel(name = "物料名称",sort = 3)
    private String materialName;

    /** 物料单号 **/
    @Excel(name = "物料编码",sort = 2)
    private String materialCode;

    /** 计量单位 **/
    @Excel(name = "计量单位",sort = 5)
    private String unitName;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间",sort = 11,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "TAllotVO{" +
                "materialName='" + materialName + '\'' +
                ", materialCode='" + materialCode + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
