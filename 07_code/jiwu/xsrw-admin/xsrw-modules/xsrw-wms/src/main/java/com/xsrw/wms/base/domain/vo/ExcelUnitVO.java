package com.xsrw.wms.base.domain.vo;


import com.xsrw.common.core.annotation.Excel;

public class ExcelUnitVO {
    /** 主键 */
    private Long id;

    /** 所属组织 */
    private Long orgId;

    /** 所属组织名称 **/
    private String orgName;

    /** 单位编码 */
    private String code;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String name;

    /**
     * 备注
     */
    @Excel(name = "备注" , width = 20)
    private String remark;

    /** 删除(0:未删除 1:删除) */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrgId(Long orgId)
    {
        this.orgId = orgId;
    }

    public Long getOrgId()
    {
        return orgId;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ExcelUnitVO{" +
            "id=" + id +
            ", orgId=" + orgId +
            ", orgName='" + orgName + '\'' +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", remark='" + remark + '\'' +
            ", delFlag='" + delFlag + '\'' +
            '}';
    }
}
