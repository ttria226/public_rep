package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TOutRemoval;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/8 9:36
 */
public class TOutRemovalVO extends TOutRemoval {
    /**
     * 单号
     */
    @Excel(name = "单号")
    private String code;
    /**
     * 单据类型字典
     */
    @Excel(name = "出库类型")
    private String type;
    /**
     * 部门名称
     */
    @Excel(name = "物料使用部门")
    private String deptName;
    /**
     * 来源字典（1.本地创建 2.erp接口 3.调拨单）
     */
    @Excel(name = "来源", readConverterExp = "1=本地创建,2=erp接口,3=调拨单")
    private String newLocal;
    /**
     * 制单人
     */
    @Excel(name = "制单人")
    private String maker;
    /**
     * 创建者
     */
    @Excel(name = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String getNewLocal() {
        return newLocal;
    }

    @Override
    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
