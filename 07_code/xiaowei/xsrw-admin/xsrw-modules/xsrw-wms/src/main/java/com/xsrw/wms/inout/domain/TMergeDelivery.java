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
 * 波次计划对象 t_merge_delivery
 *
 * @author zjj
 * @date 2023-06-25
 */
@TableName("t_merge_delivery")
public class TMergeDelivery extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 波次编码 */
    @Excel(name = "波次编码")
    private String code;

    /** 状态    未完成、部分完成、已完成 */
    @Excel(name = "状态    未完成、部分完成、已完成")
    private String status;

    /** 出库单code 多个逗号分割 **/
    private String outDeliveryCode;

    /** 是否分拨  0未分拨、1已分拨 **/
    private String allocateFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public String getOutDeliveryCode() {
        return outDeliveryCode;
    }

    public void setOutDeliveryCode(String outDeliveryCode) {
        this.outDeliveryCode = outDeliveryCode;
    }

    public String getAllocateFlag() {
        return allocateFlag;
    }

    public void setAllocateFlag(String allocateFlag) {
        this.allocateFlag = allocateFlag;
    }

    @Override
    public String toString() {
        return "TMergeDelivery{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", outDeliveryCode='" + outDeliveryCode + '\'' +
                ", allocateFlag='" + allocateFlag + '\'' +
                '}';
    }
}
