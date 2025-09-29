package com.xsrw.wms.inout.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 物料入库详情对象 t_material_detail
 *
 */
public class TMaterialDetailDTO {
    private static final long serialVersionUID = 1L;

    private String id;
    private int count;
    private int len;
    private int issi;


    /**
     * 物料详情id
     */
    private Long detailId;

    /**
     * 打印机位置 1一层、2二层
     */
    private String printFloor;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getIssi() {
        return issi;
    }

    public void setIssi(int issi) {
        this.issi = issi;
    }

    public String getPrintFloor() {
        return printFloor;
    }

    public void setPrintFloor(String printFloor) {
        this.printFloor = printFloor;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
}
