package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 包装配置对象 t_unit_config
 *
 * @author lyx
 * @date 2023-05-06
 */
@TableName("t_unit_config")
public class TUnitConfig extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 物料
     */
//    @Excel(name = "物料")
    private Long materialId;

    /**
     * 小的单位
     */
//    @Excel(name = "小的单位")
    private Long minUnitId;

    /**
     * 大的单位
     */
//    @Excel(name = "大的单位")
    private Long maxUnitId;

    /**
     * 数量
     */
//    @Excel(name = "数量")
    private Long count;

    /**
     * 状态（1可用 0不可用）
     */
    @Excel(name = "状态", sort = 4, readConverterExp = "1=可用,0=不可用")
    private String status;

    /**
     * 换算关系
     */
    @Excel(name = "换算关系", sort = 3)
    private String unitConfigName;

    /**
     * 物料名称
     */
    @Excel(name = "物料", sort = 2)
    private String materialName;

    /**
     * 小单位名称
     */
//    @Excel(name = "小单位名称")
    private String minUnitName;

    /**
     * 大单位名称
     */
    @Excel(name = "包装单位", sort = 1)
    private String maxUnitName;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMinUnitName() {
        return minUnitName;
    }

    public void setMinUnitName(String minUnitName) {
        this.minUnitName = minUnitName;
    }

    public String getMaxUnitName() {
        return maxUnitName;
    }

    public void setMaxUnitName(String maxUnitName) {
        this.maxUnitName = maxUnitName;
    }

    public String getUnitConfigName() {
        return unitConfigName;
    }

    public void setUnitConfigName(String unitConfigName) {
        this.unitConfigName = unitConfigName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMinUnitId(Long minUnitId) {
        this.minUnitId = minUnitId;
    }

    public Long getMinUnitId() {
        return minUnitId;
    }

    public void setMaxUnitId(Long maxUnitId) {
        this.maxUnitId = maxUnitId;
    }

    public Long getMaxUnitId() {
        return maxUnitId;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("minUnitId", getMinUnitId())
                .append("maxUnitId", getMaxUnitId())
                .append("count", getCount())
                .append("status", getStatus())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("delFlag", getDelFlag())
                .append("unitConfigName", getUnitConfigName())
                .append("materialName", getMaterialName())
                .append("minUnitName", getMinUnitName())
                .append("maxUnitName", getMaxUnitName())
                .toString();
    }
}
