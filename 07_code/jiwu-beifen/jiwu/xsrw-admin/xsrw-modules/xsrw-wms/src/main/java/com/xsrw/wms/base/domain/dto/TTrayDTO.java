package com.xsrw.wms.base.domain.dto;

import com.xsrw.wms.base.domain.TTray;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/12 10:00
 */
public class TTrayDTO extends TTray {

    /**
     * 去除类型
     */
    private String notStatus;

    /**
     * 货位状态
     */
    private String goodsAllocationStatus;

    /**
     * 标签打印数量
     */
    private Integer count;

    /**
     * 批量打印开始编号
     */
    private String startNo;
    /**
     * 批量打印结束编号
     */
    private String endNo;

    /**
     * 库位查询
     */
    private String locationName;

    private List<String> codes;


    /**
     * 盘点任务详情id
     */
    private Long taskDetailId;

    public String getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(String notStatus) {
        this.notStatus = notStatus;
    }

    public String getGoodsAllocationStatus() {
        return goodsAllocationStatus;
    }

    public void setGoodsAllocationStatus(String goodsAllocationStatus) {
        this.goodsAllocationStatus = goodsAllocationStatus;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStartNo() {
        return startNo;
    }

    public void setStartNo(String startNo) {
        this.startNo = startNo;
    }

    public String getEndNo() {
        return endNo;
    }

    public void setEndNo(String endNo) {
        this.endNo = endNo;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Long getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(Long taskDetailId) {
        this.taskDetailId = taskDetailId;
    }
}
