package com.xsrw.wms.stock.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 移库详情对象 t_move_library_detail
 *
 * @author lyx
 * @date 2023-05-11
 */
@TableName("t_move_library_detail")
public class TMoveLibraryDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 移库编码 */
    @Excel(name = "移库编码")
    private String moveLibraryCode;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private Long factory;

    /** 库存id */
    @Excel(name = "库存id")
    private Long stockId;

    /** 物料标识 */
    @Excel(name = "物料标识")
    private Long materialId;

    /** 移库数量 */
    @Excel(name = "移库数量")
    private Long count;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batchCode;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMoveLibraryCode(String moveLibraryCode)
    {
        this.moveLibraryCode = moveLibraryCode;
    }

    public String getMoveLibraryCode()
    {
        return moveLibraryCode;
    }
    public void setFactory(Long factory)
    {
        this.factory = factory;
    }

    public Long getFactory()
    {
        return factory;
    }
    public void setStockId(Long stockId)
    {
        this.stockId = stockId;
    }

    public Long getStockId()
    {
        return stockId;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setCount(Long count)
    {
        this.count = count;
    }

    public Long getCount()
    {
        return count;
    }
    public void setBatchCode(String batchCode)
    {
        this.batchCode = batchCode;
    }

    public String getBatchCode()
    {
        return batchCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("moveLibraryCode", getMoveLibraryCode())
            .append("factory", getFactory())
            .append("stockId", getStockId())
            .append("materialId", getMaterialId())
            .append("count", getCount())
            .append("batchCode", getBatchCode())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
