package com.xsrw.wms.base.domain.vo;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xsrw.common.core.annotation.Excel;

public class ExcelTrayVO {
    /** 主键 */
    @TableId(value = "id")
    private Long id;

    /** 所属仓库 */
    private Long warehouseId;

    /** 载具类型 */
    @Excel(name = "载具类型" , readConverterExp = "1=托盘,2=料箱,3=货笼",sort = 2)
    private String trayCategory;

    /** 数量 */
    private Long count;

    /** 手工创建（5:手工创建6:系统录入） */
    private String type;

    /** 状态（0：空闲 1：使用中） */
    @Excel(name = "载具状态" , readConverterExp = "0=空闲,1=半托,2=全托",sort = 3)
    private String status;

    /** 载具编码 */
    private String code;

    /** 部门标识 */
    private Long factory;

    /** 标签模板ID */
    private Long labelTemplateId;

    @Excel(name = "备注",sort = 5)
    private String remark;

    /** 是否绑定库位 */
//    @Excel(name = "是否绑定库位" ,readConverterExp = "0=是,1=否" ,sort = 4)
    private String locationType;

    /** 区域ID */
    private Long areaId;

    /** 库位ID */
    private Long locationId;

    /** 库区ID */
    private Long reservoirId;
    /** 删除(0:未删除 1:删除) */
    @TableLogic
    private String delFlag;

    /** 所属仓库 */
    @Excel(name = "所属仓库",sort = 4)
    private String warehouse;

    /** 区域ID */
//    @Excel(name = "区域" , sort = 6)
    private String areaName;

    /** 库位ID */
//    @Excel(name = "库位" ,sort = 8)
    private String locationName;

    /** 库区ID */
//    @Excel(name = "库区" , sort = 7)
    private String reservoirName;

    /** 部门名称 */
    private String department;

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

    public String getTrayCategory() {
        return trayCategory;
    }

    public void setTrayCategory(String trayCategory) {
        this.trayCategory = trayCategory;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getFactory() {
        return factory;
    }

    public void setFactory(Long factory) {
        this.factory = factory;
    }

    public Long getLabelTemplateId() {
        return labelTemplateId;
    }

    public void setLabelTemplateId(Long labelTemplateId) {
        this.labelTemplateId = labelTemplateId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "ExcelTrayVO{" +
            "id=" + id +
            ", warehouseId=" + warehouseId +
            ", trayCategory='" + trayCategory + '\'' +
            ", count=" + count +
            ", type='" + type + '\'' +
            ", status='" + status + '\'' +
            ", code='" + code + '\'' +
            ", factory=" + factory +
            ", labelTemplateId=" + labelTemplateId +
            ", remark='" + remark + '\'' +
            ", locationType='" + locationType + '\'' +
            ", areaId=" + areaId +
            ", locationId=" + locationId +
            ", reservoirId=" + reservoirId +
            ", delFlag='" + delFlag + '\'' +
            ", warehouse='" + warehouse + '\'' +
            ", areaName='" + areaName + '\'' +
            ", locationName='" + locationName + '\'' +
            ", reservoirName='" + reservoirName + '\'' +
            ", department='" + department + '\'' +
            '}';
    }
}
