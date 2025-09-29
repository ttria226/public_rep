package com.xsrw.wms.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 库存查询对象 t_stock_main
 *
 * @author lyx
 * @date 2023-05-09
 */
@TableName("t_stock_main")
public class TStockMain extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 物料标识 */
//    @Excel(name = "物料标识")
    private Long materialId;

    /** 库存数量 */
//    @Excel(name = "库存数量")
    private Long libraryCount;

    /** 可用数量 */
    @Excel(name = "现有库存",sort = 6)
    private Long availableCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMaterialId(Long materialId)
    {
        this.materialId = materialId;
    }

    public Long getMaterialId()
    {
        return materialId;
    }
    public void setLibraryCount(Long libraryCount)
    {
        this.libraryCount = libraryCount;
    }

    public Long getLibraryCount()
    {
        return libraryCount;
    }
    public void setAvailableCount(Long availableCount)
    {
        this.availableCount = availableCount;
    }

    public Long getAvailableCount()
    {
        return availableCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialId", getMaterialId())
            .append("libraryCount", getLibraryCount())
            .append("availableCount", getAvailableCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
