package com.xsrw.wms.task.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 人员对象 t_person
 *
 * @author zjj
 * @date 2023-06-29
 */
@TableName("t_person")
public class TPerson extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 人员id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名",sort = 1)
    private String name;

    /** 用户邮箱 */
    @Excel(name = "邮箱",sort = 2)
    private String email;

    /** 地址 */
    @Excel(name = "地址",sort = 3)
    private String address;

    /** 联系电话 */
    @Excel(name = "手机号",sort = 4)
    private String phone;

    /** 公司 */
    @Excel(name = "公司",sort = 5)
    private String companyName;

    /** 用户性别（0男 1女 2未知） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女,2=未知",sort = 6)
    private String sex;

    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSex()
    {
        return sex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("email", getEmail())
            .append("address", getAddress())
            .append("phone", getPhone())
            .append("companyName", getCompanyName())
            .append("remark", getRemark())
            .append("sex", getSex())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
