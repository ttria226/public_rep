package com.xsrw.wms.base.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xsrw.common.core.annotation.Excel;

public class ExcelAreaVO {

    /** 名称 */
    @Excel(name = "区域名称")
    private String name;

    /** 主键 */
    private Long id;

    /** 区域的禁用状态（0：启用 1：禁用）仓库为平库才可禁用 **/
    @Excel(name = "状态" , readConverterExp ="0=启用,1=禁用")
    private String status;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ExcelAreaVO{" +
            ", name='" + name + '\'' +
            ", id=" + id +
            ", createBy='" + createBy + '\'' +
            ", remark='" + remark + '\'' +
            '}';
    }
}
