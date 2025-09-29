package com.xsrw.wms.loan.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

/**
 * 设备借还借出换入记录对象 d_loan_return_record
 *
 * @author wxr
 * @date 2023-06-09
 */
@TableName("d_loan_return_record")
public class DLoanReturnRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备借还登记id
     */
    private Long loanRegisterId;

    /**
     * 借出人
     */
    @Excel(name = "借出人",sort = 4)
    private String loanBy;

    /**
     * 借出数量
     */
    @Excel(name = "借出数量",sort = 2)
    private Long loanCount;

    /**
     * 借出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "借出时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loanTime;

    /**
     * 还入数量
     */
    private Long returnCount;

    /**
     * 还入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;

    /**
     * 状态
     */
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getLoanRegisterId() {
        return loanRegisterId;
    }

    public void setLoanRegisterId(Long loanRegisterId) {
        this.loanRegisterId = loanRegisterId;
    }

    public void setLoanBy(String loanBy) {
        this.loanBy = loanBy;
    }

    public String getLoanBy() {
        return loanBy;
    }

    public void setLoanCount(Long loanCount) {
        this.loanCount = loanCount;
    }

    public Long getLoanCount() {
        return loanCount;
    }

    public void setLoanTime(Date loanTime) {
        this.loanTime = loanTime;
    }

    public Date getLoanTime() {
        return loanTime;
    }

    public void setReturnCount(Long returnCount) {
        this.returnCount = returnCount;
    }

    public Long getReturnCount() {
        return returnCount;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() {
        return returnTime;
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
                .append("loanRegisterId", getLoanRegisterId())
                .append("loanBy", getLoanBy())
                .append("loanCount", getLoanCount())
                .append("loanTime", getLoanTime())
                .append("returnCount", getReturnCount())
                .append("returnTime", getReturnTime())
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
