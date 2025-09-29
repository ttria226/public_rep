package com.xsrw.wms.stock.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.stock.domain.TMoveLibrary;

import java.util.List;

/**
 * 库内移位对象 t_move_library
 *
 */
public class MoveLibraryVo extends TMoveLibrary {

    /** 物料名称  */
    @Excel(name = "物料名称",sort = 3)
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号",sort = 4)
    private String specifications;

    /** 调拨类型*/
    @Excel(name = "调拨类型", sort = 1)
    private String moveType;

    /** 转出区域 */
    @Excel(name = "转出区域", sort = 5)
    private String areaName;

    /** 转出库区 */
    @Excel(name = "转出库区", sort = 6)
    private String reservoirName;

    /** 转出库位 */
    @Excel(name = "转出库位", sort = 7)
    private String locationOutName;


    /** 转入库位 */
    @Excel(name = "转入库位", sort = 8)
    private String locationInName;
    /*移库载具编码*/
    @Excel(name = "载具编码", sort = 9)
    private String zaijuCode;
    /*wcs任务ID*/
    @Excel(name = "wcs任务ID", sort = 10)
    private Long wcsId;
    /*wcs任务类型*/
    private String taskType;
    /*wcs任务状态*/
    private String taskStatus;
    /*wcs载具编号*/
    private String trayCode;

    public Long getWcsId() {
        return wcsId;
    }

    public void setWcsId(Long wcsId) {
        this.wcsId = wcsId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getZaijuCode() {
        return zaijuCode;
    }

    public void setZaijuCode(String zaijuCode) {
        this.zaijuCode = zaijuCode;
    }

    /** 详细列表 */
    private List<MoveLibraryDetailVo> moveLibraryDetailVoList;

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getSpecifications() {
        return specifications;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
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

    public String getLocationOutName() {
        return locationOutName;
    }

    public void setLocationOutName(String locationOutName) {
        this.locationOutName = locationOutName;
    }

    public String getLocationInName() {
        return locationInName;
    }

    public void setLocationInName(String locationInName) {
        this.locationInName = locationInName;
    }

    public List<MoveLibraryDetailVo> getMoveLibraryDetailVoList() {
        return moveLibraryDetailVoList;
    }

    public void setMoveLibraryDetailVoList(List<MoveLibraryDetailVo> moveLibraryDetailVoList) {
        this.moveLibraryDetailVoList = moveLibraryDetailVoList;
    }
}
