package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TAdvanceCollection;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/6 13:56
 */
public class TAdvanceCollectionVO extends TAdvanceCollection {

    /**
     * 单号
     */
    @Excel(name = "单号", sort = 1)
    private String code;
    /**
     * 单据类型字典（1采购收货入库 2生产产品入库  3领用退还入库 4借货入库 5借出还入）
     */
    @Excel(name = "入库类型", readConverterExp = "1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入", sort = 1)
    private String type;
    /**
     * 来源字典（1.本地创建 2.erp接口 3.调拨单）
     */
    @Excel(name = "来源", sort = 3, readConverterExp = "1=本地创建,2=erp接口,3=调拨单")
    private String newLocal;

    /**
     * 部门名称
     */
    @Excel(name = "物料使用部门", sort = 2)
    private String deptName;
    /**
     * 制单人
     */
    @Excel(name = "制单人", sort = 6)
    private String maker;
    /**
     * 创建者
     */
    @Excel(name = "创建人", sort = 2)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", sort = 7, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewLocal() {
        return newLocal;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    @Override
    public String getDeptName() {
        return deptName;
    }

    @Override
    public void setDeptName(String deptName) {
        this.deptName = deptName;
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
