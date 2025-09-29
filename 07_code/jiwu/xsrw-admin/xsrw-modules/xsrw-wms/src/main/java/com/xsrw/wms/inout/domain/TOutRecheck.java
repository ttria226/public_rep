package com.xsrw.wms.inout.domain;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 出库复核单对象 t_out_recheck
 *
 * @author wxr
 * @date 2023-06-07
 */
@TableName("t_out_recheck")
public class TOutRecheck extends BaseEntity {
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
    private Long originId;

    /**
     * 状态（0=已完成,1=已确认,2=已作废）
     */
    @Excel(name = "状态", readConverterExp = "0=已完成,1=已确认,2=已作废")
    private String status;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private String type;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOriginId(Long originId) {
        this.originId = originId;
    }

    public Long getOriginId() {
        return originId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TOutRecheck{" +
                "id=" + id +
                ", originId=" + originId +
                ", status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
