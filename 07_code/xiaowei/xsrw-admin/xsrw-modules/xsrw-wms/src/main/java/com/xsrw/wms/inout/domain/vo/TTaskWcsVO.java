package com.xsrw.wms.inout.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.inout.domain.TTaskWcs;

import java.util.Date;
import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/10 10:01
 */
public class TTaskWcsVO extends TTaskWcs {

    /**
     * 库位名称
     */
    @Excel(name = "目标库位", sort = 3)
    private String locationName;
    /**
     * 库位编码
     */
    private String locationCode;

    /**
     * 伸位类型
     */
    private String extentionType;
    /**
     * 库位楼层（1一楼 2二楼）
     */
    private String floorType;

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
     * 任务详情列表
     */
    private List<TTaskWcsDetailVO> taskWcsDetailVOList;

    /**
     * 载具状态
     */
    private String trayStatus;


    private String locationPlies;

    private String palletNodeId;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getExtentionType() {
        return extentionType;
    }

    public void setExtentionType(String extentionType) {
        this.extentionType = extentionType;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
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

    public List<TTaskWcsDetailVO> getTaskWcsDetailVOList() {
        return taskWcsDetailVOList;
    }

    public void setTaskWcsDetailVOList(List<TTaskWcsDetailVO> taskWcsDetailVOList) {
        this.taskWcsDetailVOList = taskWcsDetailVOList;
    }

    public String getTrayStatus() {
        return trayStatus;
    }

    public void setTrayStatus(String trayStatus) {
        this.trayStatus = trayStatus;
    }

    public String getLocationPlies() {
        return locationPlies;
    }

    public void setLocationPlies(String locationPlies) {
        this.locationPlies = locationPlies;
    }

    public String getPalletNodeId() {
        return palletNodeId;
    }

    public void setPalletNodeId(String palletNodeId) {
        this.palletNodeId = palletNodeId;
    }
}
