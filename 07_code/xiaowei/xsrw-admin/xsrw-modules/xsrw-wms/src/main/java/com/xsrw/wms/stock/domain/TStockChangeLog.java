package com.xsrw.wms.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.utils.bean.BeanUtils;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存详情对象 t_stock
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_stock_change_log")
public class TStockChangeLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物料详情源ID
     */
    @Excel(name = "物料详情源ID")
    private Long materialDetailId;

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    /**
     * 调整类型  0增加  1减少
     */
    public String changeType;

    public BigDecimal getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(BigDecimal changeNum) {
        this.changeNum = changeNum;
    }

    /**
     * 调整数量
     */
    public BigDecimal changeNum;
    public Long getMaterialDetailId() {
        return materialDetailId;
    }

    public void setMaterialDetailId(Long materialDetailId) {
        this.materialDetailId = materialDetailId;
    }

    public BigDecimal getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(BigDecimal beforeCount) {
        this.beforeCount = beforeCount;
    }

    /**
     * 原库存数量
     */
    @Excel(name = "原库存数量")
    private BigDecimal beforeCount;

    @Override
    public String toString() {
        return "TStock{" +
                "id=" + id +
                ", materialDetailId=" + materialDetailId +
                ", beforeCount=" + beforeCount +
                '}';
    }
}
