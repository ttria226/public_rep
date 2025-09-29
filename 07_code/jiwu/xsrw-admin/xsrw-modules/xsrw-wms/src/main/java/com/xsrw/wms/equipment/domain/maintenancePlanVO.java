package com.xsrw.wms.equipment.domain;

public class maintenancePlanVO {

    /** 计划维修日期 */
    private String planDay;

    /** 维修类型 字典：repair_type*/
    private Integer repairType;

    /** 故障描述 */
    private String content;

    public String getPlanDay() {
        return planDay;
    }

    public void setPlanDay(String planDay) {
        this.planDay = planDay;
    }

    public Integer getRepairType() {
        return repairType;
    }

    public void setRepairType(Integer repairType) {
        this.repairType = repairType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
