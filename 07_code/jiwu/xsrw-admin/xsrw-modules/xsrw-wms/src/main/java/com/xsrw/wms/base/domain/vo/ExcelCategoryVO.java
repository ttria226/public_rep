package com.xsrw.wms.base.domain.vo;


import com.xsrw.common.core.annotation.Excel;
import com.xsrw.common.core.web.domain.BaseEntity;

public class ExcelCategoryVO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属组织 */
    private Long orgId;

    private String orgName;

    /** 物料类别编号 */
    private String code;

    /** 物料类别名称 */
    @Excel(name = "物料类别名称")
    private String name;

    /**
     * 备注
     */
    @Excel(name = "备注" , width = 20)
    private String remark;

    /** 删除(0:未删除 1:删除) */
    private String delFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "TCategory{" +
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
