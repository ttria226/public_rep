package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.xsrw.common.core.annotation.Excel;

public class ExcelLocationVO {
    /**
     * 名称
     */
    @Excel(name = "库位名称")
    private String name;

    /**
     * 库房名称
     */
    @Excel(name = "所属仓库")
    private String warehouseName;

    /**
     * 区域名称
     */
    @Excel(name = "所属区域")
    private String areaName;

    /**
     * 库区名称
     */
    @Excel(name = "所属库区")
    private String reservoirName;

    /**
     * 组织
     */
    private String orgName;

    /**
     * 主键
     */
    private Long id;

    /**
     * 仓库
     */
    private Long warehouseId;

    /**
     * 区域
     */
    private Long areaId;

    /**
     * 库区
     */
    private Long reservoirId;

    /**
     * 编号
     */
    private String code;


    @Excel(name = "存放物料类别")
    private String categoryName;

    //    @Excel(name="存放物料包装方式" )
    private String unitName;

    /**
     * 是否允许物料混放  0 否  1是
     */
//    @Excel(name = "是否允许混物料存放" ,readConverterExp = "0=否,1=是")
    private String sameMaterialFlag;

    /**
     * 是否允许混批次  0 否 1是
     */
//    @Excel(name = "是否允许混批次" ,readConverterExp = "0=否,1=是")
    private String sameBatchFlag;

    //    @Excel(name = "默认往来单位" )
    private String contactsUnitName;

    /**
     * 存放上限
     **/
//    @Excel(name = "存放上限")
    private Integer upperLimit;

    /**
     * 库位排数
     **/
    @Excel(name = "排")
    private Long locationRow;

    /**
     * 库位列数
     **/
    @Excel(name = "列")
    private Long locationColumn;

    /**
     * 库位层数
     **/
    @Excel(name = "层")
    private Long locationPlies;

    /**
     * 货位类型（1货位，2轨道，3入库口，4出库口，5提升机，6充电位）
     **/
//    @Excel(name = "货位类型" ,readConverterExp = "1=货位,2=轨道,3=入库口,4=出库口,5=提升机,6=充电位")
    private String goodsAllocationType;

    /**
     * 货位状态(0:禁用,1:无货,2:有货,3:标记出库,4:标记入库)
     **/
//    @Excel(name = "货位状态" ,readConverterExp = "0=禁用,1=空闲,2=使用中,3=标记出库,4=标记入库")
    private String goodsAllocationStatus;

    /**
     * 托盘编号
     **/
//    @Excel(name = "托盘编号")
    private String palletNum;

    /**
     * 巷道号
     **/
//    @Excel(name = "巷道号")
    private String narrowAisleNum;


    @Excel(name = "备注", width = 50)
    private String remark;

    /**
     * 状态
     */
//    @Excel(name = "状态",readConverterExp = "0=未使用,1=使用中")
    private String status;

    /**
     * 删除(0:未删除 1:删除)
     */
    @TableLogic
    private String delFlag;

    /**
     * 库房CODE
     */
    @TableField(exist = false)
    private String warehouseCode;

    /**
     * 物料类别
     */
    private Long categoryId;

    /**
     * 存放物料类别
     */
    private Long depositCategoryId;

    /**
     * 物料包装（主数据管理--单位）
     */
    private Long unitId;

    /**
     * 来往单位
     **/
    private Long contactsUnitId;

    /**
     * 货位允许行驶方向（左）
     **/
    private String allowedWayLeft;
    /**
     * 货位允许行驶方向（右）
     **/
    private String allowedWayRight;
    /**
     * 货位允许行驶方向（下）
     **/
    private String allowedWayDown;
    /**
     * 货位允许行驶方向（上）
     **/
    private String allowedWayUp;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getContactsUnitName() {
        return contactsUnitName;
    }

    public void setContactsUnitName(String contactsUnitName) {
        this.contactsUnitName = contactsUnitName;
    }


    public String getGoodsAllocationType() {
        return goodsAllocationType;
    }

    public void setGoodsAllocationType(String goodsAllocationType) {
        this.goodsAllocationType = goodsAllocationType;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }

    public String getPalletNum() {
        return palletNum;
    }

    public void setPalletNum(String palletNum) {
        this.palletNum = palletNum;
    }

    public String getNarrowAisleNum() {
        return narrowAisleNum;
    }

    public void setNarrowAisleNum(String narrowAisleNum) {
        this.narrowAisleNum = narrowAisleNum;
    }

    public String getAllowedWayLeft() {
        return allowedWayLeft;
    }

    public void setAllowedWayLeft(String allowedWayLeft) {
        this.allowedWayLeft = allowedWayLeft;
    }

    public String getAllowedWayRight() {
        return allowedWayRight;
    }

    public void setAllowedWayRight(String allowedWayRight) {
        this.allowedWayRight = allowedWayRight;
    }

    public String getAllowedWayDown() {
        return allowedWayDown;
    }

    public void setAllowedWayDown(String allowedWayDown) {
        this.allowedWayDown = allowedWayDown;
    }

    public String getAllowedWayUp() {
        return allowedWayUp;
    }

    public void setAllowedWayUp(String allowedWayUp) {
        this.allowedWayUp = allowedWayUp;
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

    public Long getReservoirId() {
        return reservoirId;
    }

    public void setReservoirId(Long reservoirId) {
        this.reservoirId = reservoirId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getDepositCategoryId() {
        return depositCategoryId;
    }

    public void setDepositCategoryId(Long depositCategoryId) {
        this.depositCategoryId = depositCategoryId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getSameMaterialFlag() {
        return sameMaterialFlag;
    }

    public void setSameMaterialFlag(String sameMaterialFlag) {
        this.sameMaterialFlag = sameMaterialFlag;
    }

    public String getSameBatchFlag() {
        return sameBatchFlag;
    }

    public void setSameBatchFlag(String sameBatchFlag) {
        this.sameBatchFlag = sameBatchFlag;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Long getContactsUnitId() {
        return contactsUnitId;
    }

    public void setContactsUnitId(Long contactsUnitId) {
        this.contactsUnitId = contactsUnitId;
    }

    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    public Long getLocationPlies() {
        return locationPlies;
    }

    public void setLocationPlies(Long locationPlies) {
        this.locationPlies = locationPlies;
    }


    @Override
    public String toString() {
        return "ExcelLocationVO{" +
                "orgName='" + orgName + '\'' +
                ", id=" + id +
                ", warehouseId=" + warehouseId +
                ", areaId=" + areaId +
                ", reservoirId=" + reservoirId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", reservoirName='" + reservoirName + '\'' +
                ", areaName='" + areaName + '\'' +
                ", warehouseName='" + warehouseName + '\'' +
                ", categoryId=" + categoryId +
                ", unitId=" + unitId +
                ", sameMaterialFlag='" + sameMaterialFlag + '\'' +
                ", sameBatchFlag='" + sameBatchFlag + '\'' +
                ", upperLimit=" + upperLimit +
                ", contactsUnitId=" + contactsUnitId +
                ", locationRow=" + locationRow +
                ", locationColumn=" + locationColumn +
                ", locationPlies=" + locationPlies +
                ", goodsAllocationType='" + goodsAllocationType + '\'' +
                ", goodsAllocationStatus='" + goodsAllocationStatus + '\'' +
                ", palletNum='" + palletNum + '\'' +
                ", narrowAisleNum='" + narrowAisleNum + '\'' +
                ", allowedWayLeft='" + allowedWayLeft + '\'' +
                ", allowedWayRight='" + allowedWayRight + '\'' +
                ", allowedWayDown='" + allowedWayDown + '\'' +
                ", allowedWayUp='" + allowedWayUp + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", contactsUnitName='" + contactsUnitName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
