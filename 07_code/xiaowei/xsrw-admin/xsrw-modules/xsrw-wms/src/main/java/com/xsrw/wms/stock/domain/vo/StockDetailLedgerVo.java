package com.xsrw.wms.stock.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @Description: 库存台账VO
 * @Author: lyx
 * @Date: 2023/5/17
 */
public class StockDetailLedgerVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物料编号
     */
    @Excel(name = "物料编号",sort = 1)
    private String materialCode;

    /**
     * 物料名称
     */
    @Excel(name = "物料名称",sort = 2)
    private String materialName;

    /**
     * 物料规格
     */
//    @Excel(name = "物料规格",sort = 3)
    private String specifications;

    /**
     * 单位名称
     */
    @Excel(name = "计量单位",sort = 4)
    private String unitName;

    /**
     * 批次号
     */
    @Excel(name = "批次号",sort = 5)
    private String batchCode;

    /**
     * 任务类型（入库、出库、盘点、移库）
     */
    @Excel(name = "任务类型" , readConverterExp = "1=入库,2=出库,3=盘点,4=回库,5=移库",sort = 6)
    private String type;

    /**
     * 操作后当前数量
     */
    @Excel(name = "当前数量",sort = 7)
    private Long currentCount;
    @TableField(exist = false)
    private List<Long> materialIds;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "日期",sort = 8, width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    public void setMaterialIds(List<Long> materialIds) {
        this.materialIds = materialIds;
    }

    public List<Long> getMaterialIds() {
        return materialIds;
    }
    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Long currentCount) {
        this.currentCount = currentCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "StockDetailLedgerVo{" +
                "materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", specifications='" + specifications + '\'' +
                ", unitName='" + unitName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", type='" + type + '\'' +
                ", currentCount=" + currentCount +
                ", createTime=" + createTime +
                '}';
    }
}
