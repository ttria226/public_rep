package com.xsrw.wms.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 标签打印对象 t_label_template
 *
 * @author wxr
 * @date 2023-05-06
 */
@TableName("t_label_template")
public class TLabelTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String name;

    /**
     * 类型（1条形码；2二维码）
     */
    @Excel(name = "类型", readConverterExp = "1=条形码；2二维码")
    private String labelType;

    /**
     * 标签宽度
     */
    @Excel(name = "标签宽度")
    private Long labelWidth;

    /**
     * 标签高度
     */
    @Excel(name = "标签高度")
    private Long labelHeight;

    /**
     * 图片宽度
     */
    @Excel(name = "图片宽度")
    private Long imageWidth;

    /**
     * 图片高度
     */
    @Excel(name = "图片高度")
    private Long imageHeight;

    /**
     * 对象宽度
     */
    @Excel(name = "对象宽度")
    private Long objectWidth;

    /**
     * 对象高度
     */
    @Excel(name = "对象高度")
    private Long objectHeight;

    /**
     * 状态（1可用 0不可用）
     */
    @Excel(name = "状态", readConverterExp = "1=可用 0不可用")
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelWidth(Long labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Long getLabelWidth() {
        return labelWidth;
    }

    public void setLabelHeight(Long labelHeight) {
        this.labelHeight = labelHeight;
    }

    public Long getLabelHeight() {
        return labelHeight;
    }

    public void setImageWidth(Long imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Long getImageWidth() {
        return imageWidth;
    }

    public void setImageHeight(Long imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Long getImageHeight() {
        return imageHeight;
    }

    public void setObjectWidth(Long objectWidth) {
        this.objectWidth = objectWidth;
    }

    public Long getObjectWidth() {
        return objectWidth;
    }

    public void setObjectHeight(Long objectHeight) {
        this.objectHeight = objectHeight;
    }

    public Long getObjectHeight() {
        return objectHeight;
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
                .append("name", getName())
                .append("labelType", getLabelType())
                .append("labelWidth", getLabelWidth())
                .append("labelHeight", getLabelHeight())
                .append("imageWidth", getImageWidth())
                .append("imageHeight", getImageHeight())
                .append("objectWidth", getObjectWidth())
                .append("objectHeight", getObjectHeight())
                .append("status", getStatus())
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
