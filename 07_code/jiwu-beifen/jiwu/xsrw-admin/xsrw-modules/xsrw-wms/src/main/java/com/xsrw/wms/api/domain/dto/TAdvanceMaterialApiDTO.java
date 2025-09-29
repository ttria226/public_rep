package com.xsrw.wms.api.domain.dto;

import com.xsrw.common.core.web.domain.BaseEntity;
import com.xsrw.wms.inout.domain.TMaterialDetail;

import java.util.List;

/**
 * @author wangxueru
 * @description
 * @date 2023/7/31 13:56
 */

public class TAdvanceMaterialApiDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private List<TMaterialDetail> tMaterialDetailList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TMaterialDetail> gettMaterialDetailList() {
        return tMaterialDetailList;
    }

    public void settMaterialDetailList(List<TMaterialDetail> tMaterialDetailList) {
        this.tMaterialDetailList = tMaterialDetailList;
    }
}
