package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库发货退货单详情对象 t_out_removal_detail
 *
 * @author wxr
 * @date 2023-06-09
 */
@TableName("t_out_removal_detail")
public class TOutRemovalDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 源单id
     */
    @Excel(name = "源单id")
    private Long detailOriginId;

    /**
     * 退货数量
     */
    @Excel(name = "退货数量")
    private Long returnCount;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDetailOriginId(Long detailOriginId) {
        this.detailOriginId = detailOriginId;
    }

    public Long getDetailOriginId() {
        return detailOriginId;
    }

    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }

    public Long getReturnCount() {
        return returnCount;
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
                .append("detailOriginId", getDetailOriginId())
                .append("returnCount", getReturnCount())
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
