package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xsrw.common.core.annotation.Excel;

/**
 * @Description: 库区对象VO
 * @Author XMING
 * @Date 2022-05-20
 */
public class ExcelReservoirVO {

    private Long id;

    private Long warehouseId;

    private Long areaId;

    private String type;

    private String warehouseCode;

    private Long adminUser;

//    @Excel(name = "所属组织")
    private String orgName;


    private String code;

    @Excel(name = "库区名称")
    private String name;

    @TableField(exist = false)
    @Excel(name = "所属区域")
    private String areaName;

    @Excel(name = "备注" , width = 50)
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;


    /** 物料类别名称 */
    private String categoryName;

    /** 是否允许物料混放  0 否  1是 */
    private String sameMaterialFlag;

    /** 物料类别 */
    private Long categoryId;

    /** 物料包装（主数据管理--单位） */
    private Long unitId;

    /** 是否允许混批次  0 否 1是 */
    private String sameBatchFlag;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(Long adminUser) {
        this.adminUser = adminUser;
    }

    public String getSameMaterialFlag() {
        return sameMaterialFlag;
    }

    public void setSameMaterialFlag(String sameMaterialFlag) {
        this.sameMaterialFlag = sameMaterialFlag;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getSameBatchFlag() {
        return sameBatchFlag;
    }

    public void setSameBatchFlag(String sameBatchFlag) {
        this.sameBatchFlag = sameBatchFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public String toString() {
        return "ExcelReservoirVO{" +
            "id=" + id +
            ", warehouseId=" + warehouseId +
            ", areaId=" + areaId +
            ", type='" + type + '\'' +
            ", warehouseCode='" + warehouseCode + '\'' +
            ", adminUser=" + adminUser +
            ", orgName='" + orgName + '\'' +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", areaName='" + areaName + '\'' +
            ", remark='" + remark + '\'' +
            ", createBy='" + createBy + '\'' +
            ", categoryName='" + categoryName + '\'' +
            ", sameMaterialFlag='" + sameMaterialFlag + '\'' +
            ", categoryId=" + categoryId +
            ", unitId=" + unitId +
            ", sameBatchFlag='" + sameBatchFlag + '\'' +
            '}';
    }
}
