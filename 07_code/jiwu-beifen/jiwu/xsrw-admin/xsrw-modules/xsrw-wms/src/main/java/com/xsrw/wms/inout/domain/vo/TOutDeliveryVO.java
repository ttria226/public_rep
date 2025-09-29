package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TOutDelivery;

import java.util.Date;

/**
 * 入库单对象 t_out_delivery
 *
 * @author zyq
 * @date 2023-05-09
 */
public class TOutDeliveryVO extends TOutDelivery {

    /**
     * 1 应急出库 0 其他
     */
    private String deliveryType;
    /**
     * 预计出库数量
     * 应急出库
     */
    private Long predictCount;
    /**
     * 小件领取数量
     * 应急出库
     */
    private Long smallPredictCount;
    /**
     * 物料编号
     * 应急出库
     */
    private Long materialId;

    /**
     * 齐套物料数量
     **/
    private Integer suitNum;

    /**
     * 齐套物料ID
     **/
    private Long suitMaterialId;


    /** bom名称 **/
    private String bomName;

    /**
     * 库区名称
     */
    private String reservoirName;
    /**
     * 部门名称
     */
    @Excel(name = "物料使用部门", sort = 2)
    private String deptName;
    /**
     * 创建者
     */
    @Excel(name = "申请人", sort = 2)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(Long predictCount) {
        this.predictCount = predictCount;
    }

    public Long getSmallPredictCount() {
        return smallPredictCount;
    }

    public void setSmallPredictCount(Long smallPredictCount) {
        this.smallPredictCount = smallPredictCount;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Integer getSuitNum() {
        return suitNum;
    }

    public void setSuitNum(Integer suitNum) {
        this.suitNum = suitNum;
    }

    public Long getSuitMaterialId() {
        return suitMaterialId;
    }

    public void setSuitMaterialId(Long suitMaterialId) {
        this.suitMaterialId = suitMaterialId;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getReservoirName() {
        return reservoirName;
    }

    public void setReservoirName(String reservoirName) {
        this.reservoirName = reservoirName;
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
