package com.xsrw.wms.base.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.wms.base.domain.TClasses;

import java.util.Date;

/**
 * @author wangxueru
 * @description
 * @date 2023/6/12 10:25
 */
public class TClassesVO extends TClasses {

    /**
     * 操作人
     */
    @Excel(name = "操作人员", sort = 1)
    private String userName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
