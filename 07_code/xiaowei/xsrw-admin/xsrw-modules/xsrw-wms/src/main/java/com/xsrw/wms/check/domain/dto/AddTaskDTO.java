package com.xsrw.wms.check.domain.dto;


import com.xsrw.wms.stock.domain.vo.MoveLibraryVo;
import com.xsrw.wms.inout.domain.TOutDelivery;

import java.util.List;

/**
 * @Description: 任务统一添加DTO
 * @Author XMING
 * @Date 2022-05-26
 */
public class AddTaskDTO {

    // 任务类型
    private String type;

    private List<Long> deliveryIds;

    private TOutDelivery outDelivery;

    private List<Long> outDeliveryIdList;

//    private TMergeDelivery mergeDelivery;

    private List<Long> mergeDeliveryId;

//    private TTakeDelivery takeDelivery;

//    private List<TakeDeliveryDetailRecordVo> takeDeliveryDetailRecordList;

    private MoveLibraryVo moveLibrary;

    // 1 出库计划生成 2 波次计划生成 3 空托盘上架
    private Integer pickType;

    // 托盘编号
    private String palletNum;

    private String batchCode;

    // 回库类型  1 普通回库 2 盘点回库
    private String backType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getDeliveryIds() {
        return deliveryIds;
    }

    public void setDeliveryIds(List<Long> deliveryIds) {
        this.deliveryIds = deliveryIds;
    }

    public TOutDelivery getOutDelivery() {
        return outDelivery;
    }

    public void setOutDelivery(TOutDelivery outDelivery) {
        this.outDelivery = outDelivery;
    }

    public List<Long> getOutDeliveryIdList() {
        return outDeliveryIdList;
    }

    public void setOutDeliveryIdList(List<Long> outDeliveryIdList) {
        this.outDeliveryIdList = outDeliveryIdList;
    }

    public List<Long> getMergeDeliveryId() {
        return mergeDeliveryId;
    }

    public void setMergeDeliveryId(List<Long> mergeDeliveryId) {
        this.mergeDeliveryId = mergeDeliveryId;
    }

    public MoveLibraryVo getMoveLibrary() {
        return moveLibrary;
    }

    public void setMoveLibrary(MoveLibraryVo moveLibrary) {
        this.moveLibrary = moveLibrary;
    }

    public Integer getPickType() {
        return pickType;
    }

    public void setPickType(Integer pickType) {
        this.pickType = pickType;
    }

    public String getPalletNum() {
        return palletNum;
    }

    public void setPalletNum(String palletNum) {
        this.palletNum = palletNum;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    @Override
    public String toString() {
        return "AddTaskDTO{" +
                "type='" + type + '\'' +
                ", deliveryIds=" + deliveryIds +
                ", outDelivery=" + outDelivery +
                ", outDeliveryIdList=" + outDeliveryIdList +
                ", mergeDeliveryId=" + mergeDeliveryId +
                ", moveLibrary=" + moveLibrary +
                ", pickType=" + pickType +
                ", palletNum='" + palletNum + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", backType='" + backType + '\'' +
                '}';
    }
}
