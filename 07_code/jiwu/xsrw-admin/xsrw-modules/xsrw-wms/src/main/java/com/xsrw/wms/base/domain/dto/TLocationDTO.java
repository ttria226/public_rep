package com.xsrw.wms.base.domain.dto;

import com.xsrw.wms.base.domain.TLocation;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/5/5 18:17
 */
public class TLocationDTO extends TLocation {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 库区名称
     */
    private String reservoirName;

    /**
     * 起始库位排数
     **/
    private Long startRow;

    /**
     * 起始库位列数
     **/
    private Long startColumn;

    /**
     * 起始库位层数
     **/
    private Long startPlies;

    /**
     * 起始库位排数
     **/
    private Long endRow;

    /**
     * 起始库位列数
     **/
    private Long endColumn;

    /**
     * 起始库位层数
     **/
    private Long endPlies;

    /**
     * 货位类型（1货位，2轨道，3入库口，4出库口，5提升机，6充电位）
     **/
    private String goodsAllocationType;
    /**
     * 托盘编号
     **/
    private String palletNum;
    /**
     * 巷道号
     **/
    private String narrowAisleNum;
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

    /**
     * ids
     */
    private List<Long> ids;

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

    public Long getStartRow() {
        return startRow;
    }

    public void setStartRow(Long startRow) {
        this.startRow = startRow;
    }

    public Long getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(Long startColumn) {
        this.startColumn = startColumn;
    }

    public Long getStartPlies() {
        return startPlies;
    }

    public void setStartPlies(Long startPlies) {
        this.startPlies = startPlies;
    }

    public Long getEndRow() {
        return endRow;
    }

    public void setEndRow(Long endRow) {
        this.endRow = endRow;
    }

    public Long getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(Long endColumn) {
        this.endColumn = endColumn;
    }

    public Long getEndPlies() {
        return endPlies;
    }

    public void setEndPlies(Long endPlies) {
        this.endPlies = endPlies;
    }

    public String getGoodsAllocationType() {
        return goodsAllocationType;
    }

    public void setGoodsAllocationType(String goodsAllocationType) {
        this.goodsAllocationType = goodsAllocationType;
    }

    @Override
    public String getPalletNum() {
        return palletNum;
    }

    @Override
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

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
