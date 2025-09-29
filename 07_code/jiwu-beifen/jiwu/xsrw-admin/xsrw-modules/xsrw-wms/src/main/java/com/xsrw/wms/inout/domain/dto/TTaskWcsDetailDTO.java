package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TTaskWcsDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/8/1 14:44
 */
public class TTaskWcsDetailDTO extends TTaskWcsDetail {

    /**
     * 单据子表id
     */
    private Long advanceRegistrationId;

    /**
     * rfids
     */
    private List<String> rfIds;

    public Long getAdvanceRegistrationId() {
        return advanceRegistrationId;
    }

    public void setAdvanceRegistrationId(Long advanceRegistrationId) {
        this.advanceRegistrationId = advanceRegistrationId;
    }

    public List<String> getRfIds() {
        return rfIds;
    }

    public void setRfIds(List<String> rfIds) {
        this.rfIds = rfIds;
    }
}
