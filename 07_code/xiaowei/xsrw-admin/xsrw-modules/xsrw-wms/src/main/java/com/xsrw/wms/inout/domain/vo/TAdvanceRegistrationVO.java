package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TAdvanceRegistration;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/9 14:38
 */
public class TAdvanceRegistrationVO extends TAdvanceRegistration {

    /**
     * 入库单号
     */
    @Excel(name = "入库单号", sort = 1)
    private String advanceDeliveryCode;
    /**
     * 入库类型
     */
    @Excel(name = "入库类型", sort = 6, readConverterExp = "1=采购收货入库,2=生产产品入库,3=领用退还入库,4=借货入库,5=借出还入")
    private String advanceDeliveryType;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", sort = 2)
    private String materialName;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", sort = 2)
    private String materialCode;
    /**
     * 物料单位名称
     */
    @Excel(name = "计量单位", sort = 4)
    private String unitName;
    /**
     * 制单人
     */
    @Excel(name = "制单人", sort = 6)
    private String maker;

    /**
     * 创建者
     */
    @Excel(name = "执行人", sort = 8)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", sort = 8, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 审核备注
     */
    private String advanceDeliveryRemark;
    /**
     * 来源
     */
    private String newLocal;

    /**
     * 任务列表
     */
    private List<TTaskInVO> taskInList;

    public String getAdvanceDeliveryCode() {
        return advanceDeliveryCode;
    }

    public void setAdvanceDeliveryCode(String advanceDeliveryCode) {
        this.advanceDeliveryCode = advanceDeliveryCode;
    }

    public String getAdvanceDeliveryType() {
        return advanceDeliveryType;
    }

    public void setAdvanceDeliveryType(String advanceDeliveryType) {
        this.advanceDeliveryType = advanceDeliveryType;
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

    public String getAdvanceDeliveryRemark() {
        return advanceDeliveryRemark;
    }

    public void setAdvanceDeliveryRemark(String advanceDeliveryRemark) {
        this.advanceDeliveryRemark = advanceDeliveryRemark;
    }

    public String getNewLocal() {
        return newLocal;
    }

    public void setNewLocal(String newLocal) {
        this.newLocal = newLocal;
    }

    public List<TTaskInVO> getTaskInList() {
        return taskInList;
    }

    public void setTaskInList(List<TTaskInVO> taskInList) {
        this.taskInList = taskInList;
    }
}
