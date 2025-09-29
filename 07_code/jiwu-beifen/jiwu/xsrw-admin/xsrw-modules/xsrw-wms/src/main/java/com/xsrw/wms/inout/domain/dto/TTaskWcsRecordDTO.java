package com.xsrw.wms.inout.domain.dto;

import com.xsrw.wms.inout.domain.TTaskWcsRecord;

/**
 * @author wxr
 * @date 2023/11/16 14:33
 */
public class TTaskWcsRecordDTO extends TTaskWcsRecord {

    /**
     * 载具id
     */
    private Long trayId;

    public Long getTrayId() {
        return trayId;
    }

    public void setTrayId(Long trayId) {
        this.trayId = trayId;
    }
}
