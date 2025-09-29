package com.xsrw.wms.inout.domain.dto;

import java.util.List;

/**
 * 物料入库详情对象 t_material_detail
 *
 */
public class TMaterialAPPDTO {

    //rfids数组
    List<TMaterialDetailDTO> tMaterialDetailDTOS;

    public List<TMaterialDetailDTO> gettMaterialDetailDTOS() {
        return tMaterialDetailDTOS;
    }

    public void settMaterialDetailDTOS(List<TMaterialDetailDTO> tMaterialDetailDTOS) {
        this.tMaterialDetailDTOS = tMaterialDetailDTOS;
    }
}
