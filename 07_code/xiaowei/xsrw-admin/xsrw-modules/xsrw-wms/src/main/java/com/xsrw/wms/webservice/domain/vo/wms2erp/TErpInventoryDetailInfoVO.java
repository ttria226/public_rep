package com.xsrw.wms.webservice.domain.vo.wms2erp;

import com.xsrw.wms.webservice.domain.TErpInventoryDetail;

import java.math.BigDecimal;

public class TErpInventoryDetailInfoVO extends TErpInventoryDetail {

    /** 计划数量 */
    private BigDecimal predictCount;

    /** 实际数量 */
    private BigDecimal actualCount;

    /**
     * 盘点任务id
     */
    private Long taskId;


    public BigDecimal getPredictCount() {
        return predictCount;
    }

    public void setPredictCount(BigDecimal predictCount) {
        this.predictCount = predictCount;
    }

    public BigDecimal getActualCount() {
        return actualCount;
    }

    public void setActualCount(BigDecimal actualCount) {
        this.actualCount = actualCount;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
